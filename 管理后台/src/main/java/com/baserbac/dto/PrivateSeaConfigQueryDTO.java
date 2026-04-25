package com.baserbac.dto;

import lombok.Data;

@Data
public class PrivateSeaConfigQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer configType;
    private Long roleId;
    private Long userId;
    private Integer isEnabled;
}
