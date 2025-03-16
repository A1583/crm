package com.ttb.crm.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchCustomerRequestRequestDto extends CustomerRequestDto {
    @Schema(description = "รหัสคำขอของลูกค้า", example = "1001")
    Long customerRequestId;
    @Schema(description = "สถานะของคำขอ เช่น IN_PROGRESS, COMPLETED, CANCELED", example = "IN_PROGRESS")
    String status;
    @Schema(description = "วันที่เริ่มต้นการสร้างคำขอ", example = "2025-03-16")
    LocalDate createdAtFrom;
    @Schema(description = "วันที่สิ้นสุดการสร้างคำขอ", example = "2025-03-16")
    LocalDate createdAtTo;
    @Schema(description = "วันที่เริ่มต้นการอัปเดตล่าสุด", example = "2025-03-16")
    LocalDate updatedAtFrom;
    @Schema(description = "วันที่สิ้นสุดการอัปเดตล่าสุด", example = "2025-03-16")
    LocalDate updatedAtTo;
    @Schema(description = "พนักงานที่อัปเดตคำขอ", example = "EMP67890")
    String updatedBy;
}
