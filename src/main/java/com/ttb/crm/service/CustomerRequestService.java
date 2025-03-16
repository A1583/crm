package com.ttb.crm.service;

import com.ttb.crm.constant.CustomerRequestStatusEnum;
import com.ttb.crm.constant.PriorityEnum;
import com.ttb.crm.exception.CustomException;
import com.ttb.crm.model.request.CustomerRequestDto;
import com.ttb.crm.model.request.NewCaseRequest;
import com.ttb.crm.model.request.SearchCustomerRequestRequestDto;
import com.ttb.crm.model.request.UpdateStatusRequest;
import com.ttb.crm.model.response.CustomerRequestResponse;
import com.ttb.crm.model.response.UpdateStatusResponse;
import com.ttb.crm.repository.CustomerRequestJdbcTemplateRepository;
import com.ttb.crm.repository.CustomerRequestRepository;
import com.ttb.crm.repository.entity.CustomerRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomerRequestService {
    ExternalService externalService;
    ModelMapper modelMapper;

    CustomerRequestRepository customerRequestRepository;
    CustomerRequestJdbcTemplateRepository customerRequestJdbcTemplateRepository;

    @Transactional
    public List<CustomerRequestResponse> newCase(@Valid NewCaseRequest request) {
        var customerRequests = new ArrayList<CustomerRequest>();
        var now = getCurrentDateTime();
        var index = 0;
        for (var input : request.getCustomerRequests()) {
            validateNewCaseRequest(input, index++);

            customerRequests.add(CustomerRequest.builder()
                    .customerId(input.getCustomerId())
                    .customerName(input.getCustomerName())
                    .customerLastName(input.getCustomerLastName())
                    .email(input.getEmail())
                    .telephone(input.getTelephone())
                    .requestType(input.getRequestType())
                    .requestDetails(input.getRequestDetails())
                    .priority(input.getPriority())
                    .channel(input.getChannel())
                    .branchId(input.getBranchId())
                    .createdAt(now)
                    .createdBy(input.getEmployeeId())
                    .updatedAt(now)
                    .build());
        }

        var customerRequestNew = customerRequestRepository.saveAll(customerRequests);

        forwardCaseToBackOfficeService(customerRequestNew);

        return customerRequestNew.stream()
                .map(i -> {
                    var response = modelMapper.map(i, CustomerRequestResponse.class);
                    response.setCustomerRequestId(i.getId());

                    return response;
                }).toList();
    }

    protected void forwardCaseToBackOfficeService(List<CustomerRequest> customerRequestNew) {
        for (var customerRequest : customerRequestNew) {
            externalService.callBackOfficeService(customerRequest);
        }

    }

    protected void validateNewCaseRequest(CustomerRequestDto input, int index) {
        if (Objects.isNull(PriorityEnum.findByKey(input.getPriority()))) {
            throwCustomerExceptionCustomerRequests(index, "priority", "is not valid");
        }

        if ("BRANCH".equalsIgnoreCase(input.getChannel()) && StringUtils.isBlank(input.getBranchId())) {
            throwCustomerExceptionCustomerRequests(index, "branchId", "is required");
        }

    }

    private void throwCustomerExceptionCustomerRequests(int index, String fieldName, String message) {
        throw new CustomException(MessageFormat.format("customerRequests[{0}].{1} {2}", index, fieldName, message));
    }

    @Transactional
    public UpdateStatusResponse updateStatus(@Valid UpdateStatusRequest request) {
        if (Objects.isNull(CustomerRequestStatusEnum.findByKey(request.getStatus()))) {
            throw new CustomException("status is not valid");
        }

        var customerRequest = customerRequestRepository.findById(request.getCustomerRequestId())
                .orElseThrow(() -> new CustomException("customerRequestId is not exist"));
        log.info("customerRequest: {}", customerRequest);

        customerRequest.setStatus(request.getStatus());
        customerRequest.setUpdatedAt(LocalDateTime.now());
        customerRequest.setUpdatedBy(request.getUpdatedBy());
        customerRequestRepository.save(customerRequest);

        log.info("customerRequest updated: {}", customerRequest);
        return UpdateStatusResponse.builder()
                .customerRequestId(customerRequest.getId())
                .build();
    }

    public List<CustomerRequestResponse> search(SearchCustomerRequestRequestDto request) {
        return customerRequestJdbcTemplateRepository.search(request);
    }

    protected LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
