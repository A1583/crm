package com.ttb.crm.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRequestDto {
    @NotBlank
    @Size(max = 50)
    @Schema(description = "รหัสลูกค้า", example = "123456789")
    String customerId;
    @NotBlank
    @Size(max = 100)
    @Schema(description = "ชื่อลูกค้า", example = "สมชาย")
    String customerName;
    @NotBlank
    @Size(max = 100)
    @Schema(description = "นามสกุลลูกค้า", example = "ใจดี")
    String customerLastName;
    @Email
    @Size(max = 100)
    @Schema(description = "อีเมลลูกค้า", example = "a@gmail.com")
    String email;
    @NotBlank
    @Size(max = 20)
    @Schema(description = "เบอร์โทรศัพท์ลูกค้า", example = "0812345678")
    String telephone;
    @NotBlank
    @Size(max = 100)
    @Schema(description = "ประเภทคำขอ", example = "CHECK_BALANCE")
    String requestType;
    @Schema(description = "รายละเอียดเพิ่มเติมของคำขอ", example = "ขอตรวจสอบยอดเงินคงเหลือในบัญชี")
    String requestDetails;
    @NotBlank
    @Size(max = 20)
    @Schema(description = "ระดับความสำคัญของคำขอ เช่น LOW, MEDIUM, HIGH", example = "HIGH")
    String priority;
    @NotBlank
    @Size(max = 50)
    @Schema(description = "รหัสพนักงานที่รับเรื่อง", example = "EMP00123")
    String employeeId;
    @NotBlank
    @Size(max = 50)
    @Schema(description = "ช่องทางที่ลูกค้าใช้ติดต่อ เช่น Call Center, Branch", example = "CALL_CENTER")
    String channel;
    @Size(max = 50)
    @Schema(description = "รหัสสาขาที่ลูกค้าติดต่อ (กรณีติดต่อผ่านสาขา)", example = "BRANCH001")
    String branchId;
}
