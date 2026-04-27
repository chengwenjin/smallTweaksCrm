package com.baserbac.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商机信息")
public class BusinessOpportunityVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商机ID")
    private Long id;

    @Schema(description = "商机编号")
    private String opportunityNo;

    @Schema(description = "商机名称")
    private String opportunityName;

    @Schema(description = "关联客户ID")
    private Long customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "关联线索ID")
    private Long leadId;

    @Schema(description = "销售阶段ID")
    private Long stageId;

    @Schema(description = "销售阶段编码")
    private String stageCode;

    @Schema(description = "销售阶段名称")
    private String stageName;

    @Schema(description = "当前阶段赢率（百分比：0-100）")
    private BigDecimal winProbability;

    @Schema(description = "预计金额")
    private BigDecimal expectedAmount;

    @Schema(description = "预期成交金额（预计金额 × 赢率）")
    private BigDecimal forecastedAmount;

    @Schema(description = "预计成交日期")
    private LocalDate expectedDealDate;

    @Schema(description = "分配负责人ID")
    private Long assignUserId;

    @Schema(description = "分配负责人姓名")
    private String assignUserName;

    @Schema(description = "分配时间")
    private LocalDateTime assignTime;

    @Schema(description = "商机状态：0-进行中 1-已赢单 2-已输单")
    private Integer status;

    @Schema(description = "商机状态名称")
    private String statusName;

    @Schema(description = "来源")
    private String sourceName;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "商机描述/需求描述")
    private String description;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "标签（多个用逗号分隔）")
    private String tags;

    @Schema(description = "标签列表")
    private List<String> tagList;

    @Schema(description = "下次跟进时间")
    private LocalDateTime nextFollowTime;

    @Schema(description = "跟进次数")
    private Long followCount;

    @Schema(description = "最后跟进时间")
    private LocalDateTime lastFollowTime;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
