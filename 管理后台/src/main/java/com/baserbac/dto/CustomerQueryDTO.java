package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "客户查询参数")
public class CustomerQueryDTO extends PageQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "客户简称")
    private String shortName;

    @Schema(description = "统一社会信用代码")
    private String creditCode;

    @Schema(description = "客户等级：A、B、C、D")
    private String levelCode;

    @Schema(description = "客户状态：0-潜在 1-合作中 2-已流失 3-休眠")
    private Integer status;

    @Schema(description = "负责人ID")
    private Long ownerUserId;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "标签（多个用逗号分隔）")
    private String tags;

    @Schema(description = "客户来源")
    private String source;
}
