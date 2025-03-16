package com.ttb.crm.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // ไม่รวมค่า null ใน response
public class ErrorResponse {
    int status;
    String message;
    List<String> errors;
    LocalDateTime timestamp;
}
