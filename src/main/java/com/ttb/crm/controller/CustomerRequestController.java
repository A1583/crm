package com.ttb.crm.controller;

import com.ttb.crm.model.request.NewCaseRequest;
import com.ttb.crm.model.request.SearchCustomerRequestRequestDto;
import com.ttb.crm.model.request.UpdateStatusRequest;
import com.ttb.crm.model.response.CustomerRequestResponse;
import com.ttb.crm.model.response.UpdateStatusResponse;
import com.ttb.crm.service.CustomerRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer Request")
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer-request")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomerRequestController {
    CustomerRequestService customerRequestService;

    @PostMapping("/search")
    @Operation(summary = "Search customer request", description = "API สำหรับค้นหาข้อมูลของคำขอลูกค้า")
    public List<CustomerRequestResponse> search(@RequestBody SearchCustomerRequestRequestDto request) {
        return customerRequestService.search(request);
    }

    @PostMapping("/new")
    @Operation(summary = "New customer request", description = "API สำหรับสร้างคำขอใหม่")
    public List<CustomerRequestResponse> newCase(@RequestBody @Valid NewCaseRequest request) {
        return customerRequestService.newCase(request);
    }

    @PutMapping("/update-status")
    @Operation(summary = "Update customer request status", description = "API สำหรับอัพเดทสถานะคำขอ")
    public UpdateStatusResponse updateStatus(@RequestBody @Valid UpdateStatusRequest request) {
        return customerRequestService.updateStatus(request);
    }

}
