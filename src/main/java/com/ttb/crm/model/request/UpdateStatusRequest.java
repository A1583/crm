package com.ttb.crm.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStatusRequest {
    @NotNull
    @Schema(description = "รหัสคำขอของลูกค้า", example = "1001")
    Long customerRequestId;
    @NotBlank
    @Schema(description = "สถานะของคำขอ เช่น IN_PROGRESS, COMPLETED, CANCELED", example = "IN_PROGRESS")
    String status;
    @NotBlank
    @Schema(description = "พนักงานที่อัปเดตคำขอ", example = "EMP67890")
    String updatedBy;
}
