package com.ttb.crm.repository.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "back_office_new_case_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BackOfficeNewCaseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long customerRequestId;
    Integer responseStatus;
    Boolean success;
    String errorMessage;
    LocalDateTime createdAt;
}
