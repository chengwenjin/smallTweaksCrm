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
public class CustomerQuoteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long customerId;

    private String quoteNo;

    private String quoteName;

    private BigDecimal quoteAmount;

    private LocalDate quoteDate;

    private LocalDate validDate;

    private Integer status;

    private String statusName;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
