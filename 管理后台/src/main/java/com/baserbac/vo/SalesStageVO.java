package com.baserbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "销售阶段信息")
public class SalesStageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "阶段ID")
    private Long id;

    @Schema(description = "阶段编码")
    private String stageCode;

    @Schema(description = "阶段名称")
    private String stageName;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @Schema(description = "赢率（百分比：0-100）")
    private BigDecimal winProbability;

    @Schema(description = "阶段描述")
    private String description;

    @Schema(description = "是否系统预设：0-否 1-是")
    private Integer isSystem;

    @Schema(description = "是否启用：0-禁用 1-启用")
    private Integer isEnabled;

    @Schema(description = "是否结束阶段：0-否 1-是")
    private Integer isClosed;

    @Schema(description = "结束类型：1-赢单 2-输单 0-非结束阶段")
    private Integer closeType;

    @Schema(description = "结束类型名称")
    private String closeTypeName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
