package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPaymentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long customerId;

    private Long contractId;

    private String contractNo;

    private String paymentNo;

    private BigDecimal paymentAmount;

    private LocalDate paymentDate;

    private String paymentMethod;

    private String paymentMethodName;

    private Integer paymentStatus;

    private String paymentStatusName;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
