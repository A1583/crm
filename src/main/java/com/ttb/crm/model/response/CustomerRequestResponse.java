package com.ttb.crm.model.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRequestResponse {
    @Schema(description = "รหัสคำขอของลูกค้า", example = "1001")
    Long customerRequestId;
    @Schema(description = "รหัสลูกค้า", example = "123456789")
    String customerId;
    @Schema(description = "ชื่อลูกค้า", example = "สมชาย")
    String customerName;
    @Schema(description = "นามสกุลลูกค้า", example = "ใจดี")
    String customerLastName;
    @Schema(description = "อีเมลลูกค้า", example = "a@gmail.com")
    String email;
    @Schema(description = "เบอร์โทรศัพท์ลูกค้า", example = "0812345678")
    String telephone;
    @Schema(description = "ประเภทคำขอ", example = "CHECK_BALANCE")
    String requestType;
    @Schema(description = "รายละเอียดเพิ่มเติมของคำขอ", example = "ขอตรวจสอบยอดเงินคงเหลือในบัญชี")
    String requestDetails;
    @Schema(description = "ระดับความสำคัญของคำขอ เช่น LOW, MEDIUM, HIGH", example = "HIGH")
    String priority;
    @Schema(description = "ช่องทางที่ลูกค้าใช้ติดต่อ เช่น Call Center, Branch", example = "CALL_CENTER")
    String channel;
    @Schema(description = "รหัสสาขาที่ลูกค้าติดต่อ (กรณีติดต่อผ่านสาขา)", example = "BRANCH001")
    String branchId;
    @Schema(description = "สถานะของคำขอ เช่น IN_PROGRESS, COMPLETED, CANCELED", example = "IN_PROGRESS")
    String status;
    @Schema(description = "วันที่สร้างคำขอ", example = "2025-03-16")
    LocalDateTime createdAt;
    @Schema(description = "พนักงานที่สร้างคำขอ", example = "EMP12345")
    String createdBy;
    @Schema(description = "วันที่อัปเดตล่าสุด", example = "2025-03-16")
    LocalDateTime updatedAt;
    @Schema(description = "พนักงานที่อัปเดตคำขอ", example = "EMP67890")
    String updatedBy;
}
