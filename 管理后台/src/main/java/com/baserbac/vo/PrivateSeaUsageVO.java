package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateSeaUsageVO {
    private Long userId;
    private String userName;
    private Integer customerCount;
    private Integer maxCustomerCount;
    private Integer customerRemaining;
    private Integer leadCount;
    private Integer maxLeadCount;
    private Integer leadRemaining;
    private Integer autoRecycleDays;
    private Double customerUsageRate;
    private Double leadUsageRate;
}
