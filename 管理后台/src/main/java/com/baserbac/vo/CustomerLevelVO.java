package com.baserbac.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLevelVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String levelCode;

    private String levelName;

    private Integer sortOrder;

    private String description;

    private Integer isEnabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
