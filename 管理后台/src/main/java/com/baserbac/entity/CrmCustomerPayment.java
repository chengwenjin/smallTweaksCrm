package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("crm_customer_payment")
public class CrmCustomerPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int METHOD_CASH = 1;
    public static final int METHOD_TRANSFER = 2;
    public static final int METHOD_CHECK = 3;
    public static final int METHOD_OTHER = 4;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_CONFIRMED = 1;

    public static final String[] METHOD_NAMES = {"", "现金", "银行转账", "支票", "其他"};

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long customerId;

    private Long contractId;

    private String paymentNo;

    private BigDecimal paymentAmount;

    private LocalDate paymentDate;

    private String paymentMethod;

    private Integer paymentStatus;

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
