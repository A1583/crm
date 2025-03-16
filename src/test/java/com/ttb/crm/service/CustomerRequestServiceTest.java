package com.ttb.crm.service;

import com.ttb.crm.constant.CustomerRequestStatusEnum;
import com.ttb.crm.exception.CustomException;
import com.ttb.crm.model.request.CustomerRequestDto;
import com.ttb.crm.model.request.NewCaseRequest;
import com.ttb.crm.model.request.SearchCustomerRequestRequestDto;
import com.ttb.crm.model.request.UpdateStatusRequest;
import com.ttb.crm.model.response.CustomerRequestResponse;
import com.ttb.crm.repository.CustomerRequestJdbcTemplateRepository;
import com.ttb.crm.repository.CustomerRequestRepository;
import com.ttb.crm.repository.entity.CustomerRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CustomerRequestServiceTest {
    @InjectMocks
    @Spy
    CustomerRequestService customerRequestService;

    @Spy
    ModelMapper modelMapper = new ModelMapper();

    @Mock
    ExternalService externalService;


    @Mock
    CustomerRequestRepository customerRequestRepository;
    @Mock
    CustomerRequestJdbcTemplateRepository customerRequestJdbcTemplateRepository;

    @Test
    void testCustomerRequestNewCaseSuccess() {
        var dateTime = LocalDateTime.of(2025, 3, 16, 12, 0);

        var customerRequestDto1 = CustomerRequestDto.builder()
                .customerId("123456789")
                .customerName("สมชาย")
                .customerLastName("ใจดี")
                .email("a@gmail.com")
                .telephone("0812345678")
                .requestType("CHECK_BALANCE")
                .requestDetails("ขอตรวจสอบยอดเงินคงเหลือในบัญชี")
                .priority("HIGH")
                .channel("CALL_CENTER")
                .branchId("BRANCH001")
                .employeeId("EMP00123")
                .build();

        var customerRequestDto2 = CustomerRequestDto.builder()
                .customerId("987654321")
                .customerName("สายฝน")
                .customerLastName("สุขใจ")
                .email("b@gmail.com")
                .telephone("0891234567")
                .requestType("LOAN_REQUEST")
                .requestDetails("ขอข้อมูลสินเชื่อ")
                .priority("MEDIUM")
                .channel("BRANCH")
                .branchId("BRANCH002")
                .employeeId("EMP00234")
                .build();

        var request = NewCaseRequest.builder()
                .customerRequests(List.of(customerRequestDto1, customerRequestDto2))
                .build();

        var customerRequest1 = CustomerRequest.builder()
                .id(1L)
                .customerId("123456789")
                .customerName("สมชาย")
                .customerLastName("ใจดี")
                .email("a@gmail.com")
                .telephone("0812345678")
                .requestType("CHECK_BALANCE")
                .requestDetails("ขอตรวจสอบยอดเงินคงเหลือในบัญชี")
                .priority("HIGH")
                .channel("CALL_CENTER")
                .branchId("BRANCH001")
                .createdAt(dateTime)
                .createdBy("EMP00123")
                .updatedAt(dateTime)
                .build();

        var customerRequest2 = CustomerRequest.builder()
                .id(2L)
                .customerId("987654321")
                .customerName("สายฝน")
                .customerLastName("สุขใจ")
                .email("b@gmail.com")
                .telephone("0891234567")
                .requestType("LOAN_REQUEST")
                .requestDetails("ขอข้อมูลสินเชื่อ")
                .priority("MEDIUM")
                .channel("BRANCH")
                .branchId("BRANCH002")
                .createdAt(dateTime)
                .createdBy("EMP00234")
                .updatedAt(dateTime)
                .build();

        when(customerRequestService.getCurrentDateTime()).thenReturn(dateTime);
        when(customerRequestRepository.saveAll(anyList())).thenReturn(List.of(customerRequest1, customerRequest2));
        doNothing().when(externalService).callBackOfficeService(any());

        var actual = customerRequestService.newCase(request);

        var customerRequestResponse1 = CustomerRequestResponse.builder()
                .customerRequestId(1L)
                .customerId("123456789")
                .customerName("สมชาย")
                .customerLastName("ใจดี")
                .email("a@gmail.com")
                .telephone("0812345678")
                .requestType("CHECK_BALANCE")
                .requestDetails("ขอตรวจสอบยอดเงินคงเหลือในบัญชี")
                .priority("HIGH")
                .channel("CALL_CENTER")
                .branchId("BRANCH001")
                .createdAt(dateTime)
                .createdBy("EMP00123")
                .updatedAt(dateTime)
                .build();

        var customerRequestResponse2 = CustomerRequestResponse.builder()
                .customerRequestId(2L)
                .customerId("987654321")
                .customerName("สายฝน")
                .customerLastName("สุขใจ")
                .email("b@gmail.com")
                .telephone("0891234567")
                .requestType("LOAN_REQUEST")
                .requestDetails("ขอข้อมูลสินเชื่อ")
                .priority("MEDIUM")
                .channel("BRANCH")
                .branchId("BRANCH002")
                .createdAt(dateTime)
                .createdBy("EMP00234")
                .updatedAt(dateTime)
                .build();

        assertEquals(List.of(customerRequestResponse1, customerRequestResponse2), actual);

        customerRequest1.setId(null);
        customerRequest2.setId(null);

        verify(externalService, times(2)).callBackOfficeService(any());
        verify(customerRequestRepository).saveAll(List.of(customerRequest1, customerRequest2));
    }

    @Test
    void testValidateNewCaseRequestInvalidPriorityThrowException() {
        var request = CustomerRequestDto.builder()
                .priority("INVALID_PRIORITY")
                .build();

        var exception = assertThrows(CustomException.class, () ->
                customerRequestService.validateNewCaseRequest(request, 0)
        );

        assertEquals("customerRequests[0].priority is not valid", exception.getMessage());
    }

    @Test
    void testValidateNewCaseRequestBranchIdEmptyForBranchChannelThrowException() {
        var request = CustomerRequestDto.builder()
                .priority("HIGH")
                .customerId("123456789")
                .customerName("สมชาย")
                .customerLastName("ใจดี")
                .telephone("0812345678")
                .email("test@example.com")
                .channel("BRANCH")
                .branchId(null)
                .build();

        var exception = assertThrows(CustomException.class, () ->
                customerRequestService.validateNewCaseRequest(request, 4)
        );

        assertEquals("customerRequests[4].branchId is required", exception.getMessage());
    }

    @Test
    void testValidateNewCaseRequestValidDataThenNotThrowException() {
        var request = CustomerRequestDto.builder()
                .priority("HIGH")
                .customerId("123456789")
                .customerName("สมชาย")
                .customerLastName("ใจดี")
                .telephone("0812345678")
                .email("test@example.com")
                .channel("CALL_CENTER")
                .branchId("BRANCH001")
                .build();

        assertDoesNotThrow(() -> customerRequestService.validateNewCaseRequest(request, 5));
    }

    @Test
    void testUpdateStatusInvalidStatusThenThrowException() {
        var request = UpdateStatusRequest.builder()
                .customerRequestId(1L)
                .status("INVALID_STATUS")
                .updatedBy("EMP001")
                .build();

        var exception = assertThrows(CustomException.class, () ->
                customerRequestService.updateStatus(request)
        );

        assertEquals("status is not valid", exception.getMessage());
    }

    @Test
    void testUpdateStatusCustomerRequestIdNotExistThenThrowException() {
        var request = UpdateStatusRequest.builder()
                .customerRequestId(99L)
                .status(CustomerRequestStatusEnum.IN_PROGRESS.name())
                .updatedBy("EMP001")
                .build();

        when(customerRequestRepository.findById(99L)).thenReturn(Optional.empty());

        var exception = assertThrows(CustomException.class, () ->
                customerRequestService.updateStatus(request)
        );

        assertEquals("customerRequestId is not exist", exception.getMessage());
    }

    @Test
    void testUpdateStatusSuccess() {
        var now = LocalDateTime.now();

        var existingRequest = CustomerRequest.builder()
                .id(1L)
                .customerId("123456789")
                .status(CustomerRequestStatusEnum.IN_PROGRESS.name())
                .createdAt(now)
                .createdBy("EMP001")
                .updatedAt(now)
                .updatedBy("EMP002")
                .build();

        var request = UpdateStatusRequest.builder()
                .customerRequestId(1L)
                .status(CustomerRequestStatusEnum.COMPLETED.name())
                .updatedBy("EMP003")
                .build();

        when(customerRequestRepository.findById(1L)).thenReturn(Optional.of(existingRequest));

        var response = customerRequestService.updateStatus(request);

        assertEquals(1L, response.getCustomerRequestId());
        assertEquals(CustomerRequestStatusEnum.COMPLETED.name(), existingRequest.getStatus());
        assertEquals("EMP003", existingRequest.getUpdatedBy());

        verify(customerRequestRepository).save(existingRequest);
    }

    @Test
    void testCustomerRequestSearchSuccess() {
        var request = SearchCustomerRequestRequestDto.builder().build();

        when(customerRequestJdbcTemplateRepository.search(any())).thenReturn(List.of());

        var actual = customerRequestService.search(request);

        assertEquals(List.of(), actual);
    }

}