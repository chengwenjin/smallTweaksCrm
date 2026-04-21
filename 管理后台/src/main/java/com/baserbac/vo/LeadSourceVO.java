package com.baserbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 线索来源 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "线索来源信息")
public class LeadSourceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "来源编码（如：manual, excel, website, campaign）")
    private String sourceCode;

    @Schema(description = "来源名称（如：手工录入、Excel导入、官网表单、市场活动）")
    private String sourceName;

    @Schema(description = "来源类型：1-手动录入 2-批量导入 3-API对接")
    private Integer sourceType;

    @Schema(description = "来源类型名称")
    private String sourceTypeName;

    @Schema(description = "是否系统内置：0-否 1-是")
    private Integer isSystem;

    @Schema(description = "是否启用：0-禁用 1-启用")
    private Integer isEnabled;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
