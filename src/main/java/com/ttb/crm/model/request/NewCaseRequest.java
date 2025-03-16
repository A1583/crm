package com.ttb.crm.model.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCaseRequest {
    @NotEmpty
    @ArraySchema(schema = @Schema(implementation = CustomerRequestDto.class))
    List<@Valid CustomerRequestDto> customerRequests;
}
