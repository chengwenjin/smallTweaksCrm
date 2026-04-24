package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "分配规则查询参数")
public class AssignRuleQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer pageNum = 1;
    private Integer pageSize = 10;

    @Schema(description = "规则名称")
    private String ruleName;

    @Schema(description = "规则类型")
    private Integer ruleType;

    @Schema(description = "是否启用")
    private Integer isEnabled;

    @Schema(description = "分配目标用户ID")
    private Long assignUserId;
}
