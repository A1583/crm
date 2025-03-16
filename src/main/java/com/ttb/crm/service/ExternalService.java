package com.ttb.crm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttb.crm.repository.BackOfficeNewCaseLogRepository;
import com.ttb.crm.repository.entity.BackOfficeNewCaseLog;
import com.ttb.crm.repository.entity.CustomerRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExternalService {
    BackOfficeNewCaseLogRepository backOfficeNewCaseLogRepository;

    RestTemplate restTemplate;
    ObjectMapper objectMapper;

    public void callBackOfficeService(CustomerRequest customerRequest) {
        var backOfficeNewCaseLog = BackOfficeNewCaseLog.builder()
                .createdAt(getCurrentDateTime())
                .success(false)
                .customerRequestId(customerRequest.getId())
                .build();

        Integer statusCode = null;
        try {
            var endpoint = "https://dummyjson.com/users/add";
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            var httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(customerRequest), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    endpoint,
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );
            statusCode = response.getStatusCode().value();
            backOfficeNewCaseLog.setSuccess(true);
        } catch (HttpServerErrorException e) {
            statusCode = e.getStatusCode().value();
            backOfficeNewCaseLog.setErrorMessage(e.getMessage());
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            backOfficeNewCaseLog.setErrorMessage(e.getMessage());
            log.error(e.getMessage(), e);
        } finally {
            backOfficeNewCaseLog.setResponseStatus(statusCode);
            backOfficeNewCaseLogRepository.save(backOfficeNewCaseLog);
        }
    }

    protected LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
