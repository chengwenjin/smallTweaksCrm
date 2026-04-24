package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "客户标签创建参数")
public class CustomerTagCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "标签名称不能为空")
    @Schema(description = "标签名称", required = true)
    private String tagName;

    @Schema(description = "标签颜色")
    private String tagColor;

    @Schema(description = "标签分类")
    private String tagCategory;

    @Schema(description = "排序号")
    private Integer sortOrder;

    @Schema(description = "标签描述")
    private String description;
}
