package com.baserbac.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TransferCreateDTO {
    @NotNull(message = "转移类型不能为空")
    private Integer transferType;
    @NotNull(message = "原负责人ID不能为空")
    private Long fromUserId;
    private String fromUserName;
    private Long toUserId;
    private String toUserName;
    @NotNull(message = "转移方式不能为空")
    private Integer transferMethod;
    private String reason;
    private String remark;
    private List<Long> customerIds;
    private List<Long> leadIds;
}
