package com.ttb.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttb.crm.model.request.CustomerRequestDto;
import com.ttb.crm.model.request.NewCaseRequest;
import com.ttb.crm.model.request.SearchCustomerRequestRequestDto;
import com.ttb.crm.model.request.UpdateStatusRequest;
import com.ttb.crm.model.response.UpdateStatusResponse;
import com.ttb.crm.service.CustomerRequestService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CustomerRequestControllerTest {
    final ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    CustomerRequestController customerRequestController;
    @Mock
    CustomerRequestService customerRequestService;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerRequestController).build();
    }

    @Test
    void testSearchCustomerRequest_ShouldReturn200() throws Exception {
        var request = SearchCustomerRequestRequestDto.builder()
                .employeeId("1")
                .build();

        when(customerRequestService.search(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/customer-request/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testNewCase_ShouldReturn200_WhenValidRequest() throws Exception {
        var request = NewCaseRequest.builder()
                .customerRequests(List.of(CustomerRequestDto.builder()
                        .customerId("123456789")
                        .customerName("สมชาย")
                        .customerLastName("ใจดี")
                        .telephone("0812345678")
                        .requestType("CHECK_BALANCE")
                        .priority("HIGH")
                        .employeeId("EMP00123")
                        .channel("CALL_CENTER")
                        .build()))
                .build();

        when(customerRequestService.newCase(any(NewCaseRequest.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/customer-request/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    void testUpdateStatus_ShouldReturn200_WhenValidRequest() throws Exception {
        var request = UpdateStatusRequest.builder()
                .customerRequestId(1L)
                .status("IN_PROGRESS")
                .updatedBy("EMP001")
                .build();

        var response = UpdateStatusResponse.builder().customerRequestId(1L).build();
        when(customerRequestService.updateStatus(any(UpdateStatusRequest.class))).thenReturn(response);

        mockMvc.perform(put("/customer-request/update-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerRequestId").value(1));
    }

}