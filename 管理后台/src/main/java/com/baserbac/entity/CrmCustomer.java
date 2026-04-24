package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("crm_customer")
public class CrmCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_POTENTIAL = 0;
    public static final int STATUS_COOPERATING = 1;
    public static final int STATUS_LOST = 2;
    public static final int STATUS_SLEEPING = 3;

    public static final String[] STATUS_NAMES = {"潜在", "合作中", "已流失", "休眠"};

    public static final String TYPE_ENTERPRISE = "1";
    public static final String TYPE_PERSONAL = "2";

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String customerNo;

    private String customerName;

    private String shortName;

    private String customerType;

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

    private String website;

    private String email;

    private String phone;

    private String fax;

    private Integer employeeCount;

    private BigDecimal annualRevenue;

    private String companyScale;

    private String levelCode;

    private Long ownerUserId;

    private String ownerUserName;

    private String tags;

    private String source;

    private Integer status;

    private LocalDateTime firstContactTime;

    private LocalDateTime lastContactTime;

    private String description;

    private String remark;

    private Long parentCustomerId;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String ext1;

    private String ext2;

    private String ext3;
}
