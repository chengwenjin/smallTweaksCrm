package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicSeaRuleVO {
    private Long id;
    private String ruleName;
    private Integer ruleType;
    private String ruleTypeName;
    private Integer rotateDays;
    private Integer claimLimitPerDay;
    private Integer claimLimitPerWeek;
    private Integer autoRecycleEnabled;
    private String autoRecycleEnabledName;
    private Integer autoRecycleDays;
    private Integer isEnabled;
    private String isEnabledName;
    private String description;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
