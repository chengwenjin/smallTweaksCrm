package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("crm_customer_contract")
public class CrmCustomerContract implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_EXECUTING = 1;
    public static final int STATUS_COMPLETED = 2;
    public static final int STATUS_TERMINATED = 3;

    public static final String[] STATUS_NAMES = {"待执行", "执行中", "已完成", "已终止"};

    @TableId(value = "id", type = IdType.AUTO)
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

    private String description;

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
}
