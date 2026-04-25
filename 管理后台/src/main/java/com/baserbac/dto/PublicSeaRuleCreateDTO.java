package com.baserbac.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PublicSeaRuleCreateDTO {
    @NotBlank(message = "规则名称不能为空")
    private String ruleName;
    @NotNull(message = "规则类型不能为空")
    private Integer ruleType;
    private Integer rotateDays;
    private Integer claimLimitPerDay;
    private Integer claimLimitPerWeek;
    private Integer autoRecycleEnabled;
    private Integer autoRecycleDays;
    private Integer isEnabled;
    private String description;
    private Integer sortOrder;
}
