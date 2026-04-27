package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("crm_business_opportunity")
public class CrmBusinessOpportunity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String opportunityNo;

    private String opportunityName;

    private Long customerId;

    private String customerName;

    private Long leadId;

    private Long stageId;

    private String stageCode;

    private String stageName;

    private BigDecimal winProbability;

    private BigDecimal expectedAmount;

    private BigDecimal forecastedAmount;

    private LocalDate expectedDealDate;

    private Long assignUserId;

    private String assignUserName;

    private LocalDateTime assignTime;

    private Integer status;

    private String sourceName;

    private String industry;

    private String description;

    private String remark;

    private String tags;

    private LocalDateTime nextFollowTime;

    private Long followCount;

    private LocalDateTime lastFollowTime;

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
