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
public class PrivateSeaConfigVO {
    private Long id;
    private Integer configType;
    private String configTypeName;
    private Long roleId;
    private String roleName;
    private Long userId;
    private String userName;
    private Integer maxCustomerCount;
    private Integer maxLeadCount;
    private Integer autoRecycleDays;
    private Integer isEnabled;
    private String isEnabledName;
    private String description;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
