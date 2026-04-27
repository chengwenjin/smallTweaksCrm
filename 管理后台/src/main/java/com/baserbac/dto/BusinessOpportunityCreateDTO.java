package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "商机创建参数")
public class BusinessOpportunityCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "商机名称不能为空")
    @Schema(description = "商机名称", example = "北京云端科技ERP项目")
    private String opportunityName;

    @Schema(description = "关联客户ID", example = "1")
    private Long customerId;

    @Schema(description = "客户名称", example = "北京云端科技有限公司")
    private String customerName;

    @Schema(description = "关联线索ID", example = "1")
    private Long leadId;

    @NotNull(message = "销售阶段不能为空")
    @Schema(description = "销售阶段ID", example = "1")
    private Long stageId;

    @Schema(description = "预计金额", example = "500000.00")
    private BigDecimal expectedAmount;

    @Schema(description = "预计成交日期", example = "2026-06-30")
    private LocalDate expectedDealDate;

    @Schema(description = "分配负责人ID", example = "1")
    private Long assignUserId;

    @Schema(description = "商机状态：0-进行中 1-已赢单 2-已输单", example = "0")
    private Integer status;

    @Schema(description = "来源", example = "线索转化")
    private String sourceName;

    @Schema(description = "所属行业", example = "IT互联网")
    private String industry;

    @Schema(description = "商机描述/需求描述", example = "客户需要企业管理系统")
    private String description;

    @Schema(description = "备注", example = "备注信息")
    private String remark;

    @Schema(description = "标签（多个用逗号分隔）", example = "重要客户,高价值")
    private String tags;

    @Schema(description = "下次跟进时间", example = "2026-04-30T10:00:00")
    private LocalDateTime nextFollowTime;
}
