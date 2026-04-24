package com.baserbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Schema(description = "分配规则VO")
public class AssignRuleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String ruleName;
    private Integer ruleType;
    private String ruleTypeName;
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
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}
