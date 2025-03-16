package com.ttb.crm.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStatusResponse {
    @Schema(description = "รหัสคำขอของลูกค้า", example = "1001")
    Long customerRequestId;
}
