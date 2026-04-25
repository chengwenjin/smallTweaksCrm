package com.baserbac.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrivateSeaConfigUpdateDTO {
    @NotNull(message = "ID不能为空")
    private Long id;
    private Integer configType;
    private Long roleId;
    private String roleName;
    private Long userId;
    private String userName;
    private Integer maxCustomerCount;
    private Integer maxLeadCount;
    private Integer autoRecycleDays;
    private Integer isEnabled;
    private String description;
    private Integer sortOrder;
}
