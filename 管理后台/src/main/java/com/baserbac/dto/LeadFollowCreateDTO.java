package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "线索跟进创建参数")
public class LeadFollowCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "线索ID不能为空")
    @Schema(description = "线索ID", example = "1")
    private Long leadId;

    @Schema(description = "跟进方式：1-电话 2-微信 3-邮件 4-拜访 5-其他", example = "1")
    private Integer followType;

    @NotBlank(message = "跟进内容不能为空")
    @Schema(description = "跟进内容", example = "与客户电话沟通，客户对产品有兴趣，约定下次拜访时间")
    private String followContent;

    @Schema(description = "下次跟进时间", example = "2026-04-25T10:00:00")
    private String nextFollowTime;

    @Schema(description = "下次跟进备注", example = "带产品演示PPT")
    private String nextFollowRemark;
}
