package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "销售阶段查询参数")
public class SalesStageQueryDTO extends PageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "阶段编码")
    private String stageCode;

    @Schema(description = "阶段名称（模糊查询）")
    private String stageName;

    @Schema(description = "是否启用：0-禁用 1-启用")
    private Integer isEnabled;

    @Schema(description = "是否系统预设：0-否 1-是")
    private Integer isSystem;
}
