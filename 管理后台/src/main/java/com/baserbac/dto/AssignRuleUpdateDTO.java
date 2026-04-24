package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "分配规则更新参数")
public class AssignRuleUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "规则名称")
    private String ruleName;

    @Schema(description = "规则类型：1-地域规则 2-行业规则 3-规模规则")
    private Integer ruleType;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区县")
    private String district;

    @Schema(description = "行业")
    private String industry;

    @Schema(description = "最小员工数")
    private Integer minEmployeeCount;

    @Schema(description = "最大员工数")
    private Integer maxEmployeeCount;

    @Schema(description = "最小年营收(万元)")
    private BigDecimal minAnnualRevenue;

    @Schema(description = "最大年营收(万元)")
    private BigDecimal maxAnnualRevenue;

    @Schema(description = "分配目标用户ID")
    private Long assignUserId;

    @Schema(description = "优先级(数字越小优先级越高)")
    private Integer priority;

    @Schema(description = "是否启用：1-启用 0-禁用")
    private Integer isEnabled;

    @Schema(description = "规则描述")
    private String description;
}
