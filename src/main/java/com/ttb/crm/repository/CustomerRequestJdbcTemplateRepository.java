package com.ttb.crm.repository;

import com.ttb.crm.model.request.SearchCustomerRequestRequestDto;
import com.ttb.crm.model.response.CustomerRequestResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomerRequestJdbcTemplateRepository {
    NamedParameterJdbcTemplate jdbcTemplate;

    public List<CustomerRequestResponse> search(SearchCustomerRequestRequestDto request) {
        var params = new MapSqlParameterSource();
        var sql = new StringBuilder("select  id as customerRequestId,* from customer_request where 1=1 ");
        if (Objects.nonNull(request.getCustomerRequestId())) {
            sql.append(" and id = :customerRequestId");
            params.addValue("customerRequestId", request.getCustomerRequestId());
        }

        if (StringUtils.isNotBlank(request.getCustomerId())) {
            sql.append(" and customer_id ilike concat('%', :customerId, '%')");
            params.addValue("customerId", request.getCustomerId());
        }

        if (StringUtils.isNotBlank(request.getCustomerName())) {
            sql.append(" and customer_name  ilike concat('%', :customerName, '%')");
            params.addValue("customerName", request.getCustomerName());
        }

        if (StringUtils.isNotBlank(request.getCustomerLastName())) {
            sql.append(" and customer_last_name  ilike concat('%', :customerLastName, '%')");
            params.addValue("customerLastName", request.getCustomerLastName());
        }

        if (StringUtils.isNotBlank(request.getEmail())) {
            sql.append(" and email  ilike concat('%', :email, '%')");
            params.addValue("email", request.getEmail());
        }

        if (StringUtils.isNotBlank(request.getTelephone())) {
            sql.append(" and telephone  ilike concat('%', :telephone, '%')");
            params.addValue("telephone", request.getTelephone());
        }

        if (StringUtils.isNotBlank(request.getRequestType())) {
            sql.append(" and request_type  ilike concat('%', :requestType, '%')");
            params.addValue("requestType", request.getRequestType());
        }

        if (Objects.nonNull(request.getPriority())) {
            sql.append(" and priority = :priority");
            params.addValue("priority", request.getPriority());
        }

        if (StringUtils.isNotBlank(request.getChannel())) {
            sql.append(" and channel = :channel");
            params.addValue("channel", request.getChannel());
        }

        if (StringUtils.isNotBlank(request.getBranchId())) {
            sql.append(" and branch_id = :branchId");
            params.addValue("branchId", request.getBranchId());
        }

        if (Objects.nonNull(request.getStatus())) {
            sql.append(" and status = :status");
            params.addValue("status", request.getStatus());
        }

        if (Objects.nonNull(request.getCreatedAtFrom())) {
            if (Objects.isNull(request.getCreatedAtTo())) {
                request.setCreatedAtTo(request.getCreatedAtFrom());
            }

            sql.append(" and created_at::date >= :createdAtFrom and created_at::date <= :createdAtTo");
            params.addValue("createdAtFrom", request.getCreatedAtFrom());
            params.addValue("createdAtTo", request.getCreatedAtTo());
        }

        if (StringUtils.isNotBlank(request.getCreatedBy())) {
            sql.append(" and created_by = :createdBy");
            params.addValue("createdBy", request.getCreatedBy());
        }

        if (Objects.nonNull(request.getUpdatedAtFrom())) {
            if (Objects.isNull(request.getUpdatedAtTo())) {
                request.setUpdatedAtTo(request.getUpdatedAtFrom());
            }
            sql.append(" and updated_at::date >= :updatedAtFrom and updated_at::date <= :updatedAtTo");
            params.addValue("updatedAtFrom", request.getUpdatedAtFrom());
            params.addValue("updatedAtTo", request.getUpdatedAtTo());
        }

        if (StringUtils.isNotBlank(request.getUpdatedBy())) {
            sql.append(" and updated_by = :updatedBy");
            params.addValue("updatedBy", request.getUpdatedBy());
        }

        sql.append(" order by id desc");

        log.info("sql: {}", sql);
        log.info("params: {}", params);

        return jdbcTemplate.query(sql.toString(), params, new BeanPropertyRowMapper<>(CustomerRequestResponse.class));
    }
}
