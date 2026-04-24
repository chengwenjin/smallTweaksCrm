package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "客户跟进记录创建参数")
public class CustomerFollowCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "客户ID不能为空")
    @Schema(description = "客户ID", required = true)
    private Long customerId;

    @Schema(description = "跟进方式：1-电话 2-微信 3-邮件 4-拜访 5-其他")
    private Integer followType;

    @NotBlank(message = "跟进内容不能为空")
    @Schema(description = "跟进内容", required = true)
    private String followContent;

    @Schema(description = "下次跟进时间")
    private LocalDateTime nextFollowTime;

    @Schema(description = "下次跟进备注")
    private String nextFollowRemark;
}
