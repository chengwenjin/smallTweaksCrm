package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("crm_assign_rule")
public class CrmAssignRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String ruleName;

    private Integer ruleType;

    private String province;

    private String city;

    private String district;

    private String industry;

    private Integer minEmployeeCount;

    private Integer maxEmployeeCount;

    private BigDecimal minAnnualRevenue;

    private BigDecimal maxAnnualRevenue;

    private Long assignUserId;

    private String assignUserName;

    private Integer priority;

    private Integer isEnabled;

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
