package com.baserbac.dto;

import lombok.Data;

@Data
public class PublicSeaRuleQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer ruleType;
    private Integer isEnabled;
}
