package com.ttb.crm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttb.crm.repository.BackOfficeNewCaseLogRepository;
import com.ttb.crm.repository.entity.BackOfficeNewCaseLog;
import com.ttb.crm.repository.entity.CustomerRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ExternalServiceTest {
    @InjectMocks
    @Spy
    ExternalService externalService;

    @Mock
    BackOfficeNewCaseLogRepository backOfficeNewCaseLogRepository;

    @Mock
    RestTemplate restTemplate;

    @Spy
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCallBackOfficeService_SuccessfulRequest() throws JsonProcessingException {
        var customerRequest = CustomerRequest.builder()
                .id(1L)
                .build();

        var dateTime = LocalDateTime.of(2025, 3, 16, 12, 0);

        when(externalService.getCurrentDateTime()).thenReturn(dateTime);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("Success", HttpStatus.OK));

        externalService.callBackOfficeService(customerRequest);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(customerRequest), headers);

        var backOfficeNewCaseLog = BackOfficeNewCaseLog.builder()
                .createdAt(dateTime)
                .success(true)
                .responseStatus(200)
                .customerRequestId(customerRequest.getId())
                .build();

        verify(restTemplate).exchange(
                "https://dummyjson.com/users/add",
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        verify(backOfficeNewCaseLogRepository).save(backOfficeNewCaseLog);
    }

    @Test
    void testCallBackOfficeService_HttpServerErrorException() throws JsonProcessingException {
        var customerRequest = CustomerRequest.builder()
                .id(1L)
                .build();

        var dateTime = LocalDateTime.of(2025, 3, 16, 12, 0);
        var exception = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error");

        when(externalService.getCurrentDateTime()).thenReturn(dateTime);
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenThrow(exception);

        externalService.callBackOfficeService(customerRequest);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(customerRequest), headers);

        var backOfficeNewCaseLog = BackOfficeNewCaseLog.builder()
                .createdAt(dateTime)
                .success(false)
                .responseStatus(500)
                .errorMessage("500 Server Error")
                .customerRequestId(customerRequest.getId())
                .build();

        verify(restTemplate).exchange(
                "https://dummyjson.com/users/add",
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        verify(backOfficeNewCaseLogRepository).save(backOfficeNewCaseLog);
    }

    @Test
    void testCallBackOfficeService_GeneralException() throws JsonProcessingException {
        var customerRequest = CustomerRequest.builder()
                .id(1L)
                .build();

        var dateTime = LocalDateTime.of(2025, 3, 16, 12, 0);

        when(externalService.getCurrentDateTime()).thenReturn(dateTime);
        when(restTemplate.exchange(anyString(), any(), any(), eq(String.class))).thenThrow(new RuntimeException("Connection timeout"));

        externalService.callBackOfficeService(customerRequest);

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(customerRequest), headers);

        var backOfficeNewCaseLog = BackOfficeNewCaseLog.builder()
                .createdAt(dateTime)
                .success(false)
                .errorMessage("Connection timeout")
                .customerRequestId(customerRequest.getId())
                .build();

        verify(restTemplate).exchange(
                "https://dummyjson.com/users/add",
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        verify(backOfficeNewCaseLogRepository).save(backOfficeNewCaseLog);
    }
}