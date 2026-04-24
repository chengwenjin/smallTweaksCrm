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
public class CustomerContractVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long customerId;

    private String contractNo;

    private String contractName;

    private String contractType;

    private BigDecimal contractAmount;

    private LocalDate signedDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer status;

    private String statusName;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
