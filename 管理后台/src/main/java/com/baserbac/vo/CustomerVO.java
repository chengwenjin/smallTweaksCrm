package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String customerNo;

    private String customerName;

    private String shortName;

    private String customerType;

    private String customerTypeName;

    private String creditCode;

    private String legalPerson;

    private BigDecimal registeredCapital;

    private LocalDate establishDate;

    private String businessStatus;

    private String businessScope;

    private String registeredAddress;

    private String industry;

    private String province;

    private String city;

    private String district;

    private String address;

    private String fullAddress;

    private String website;

    private String email;

    private String phone;

    private String fax;

    private Integer employeeCount;

    private BigDecimal annualRevenue;

    private String companyScale;

    private String levelCode;

    private String levelName;

    private Long ownerUserId;

    private String ownerUserName;

    private String tags;

    private List<String> tagList;

    private String source;

    private Integer status;

    private String statusName;

    private LocalDateTime firstContactTime;

    private LocalDateTime lastContactTime;

    private String description;

    private String remark;

    private Long parentCustomerId;

    private String parentCustomerName;

    private List<CustomerContactVO> contacts;

    private List<CustomerFollowVO> follows;

    private List<CustomerQuoteVO> quotes;

    private List<CustomerContractVO> contracts;

    private List<CustomerPaymentVO> payments;

    private List<CustomerTicketVO> tickets;

    private String createBy;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}
