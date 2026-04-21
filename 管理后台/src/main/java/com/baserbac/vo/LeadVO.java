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

/**
 * 线索信息 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "线索信息")
public class LeadVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "线索ID")
    private Long id;

    @Schema(description = "线索编号")
    private String leadNo;

    @Schema(description = "线索名称（客户名称/姓名）")
    private String leadName;

    @Schema(description = "联系人姓名")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "手机号")
    private String contactMobile;

    @Schema(description = "邮箱")
    private String contactEmail;

    @Schema(description = "QQ号码")
    private String contactQq;

    @Schema(description = "微信号")
    private String contactWechat;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区县")
    private String district;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "线索来源ID")
    private Long sourceId;

    @Schema(description = "线索来源编码")
    private String sourceCode;

    @Schema(description = "线索来源名称")
    private String sourceName;

    @Schema(description = "来源备注")
    private String sourceRemark;

    @Schema(description = "线索等级：1-高 2-中 3-低")
    private Integer level;

    @Schema(description = "线索等级名称")
    private String levelName;

    @Schema(description = "线索状态：0-新线索 1-跟进中 2-已转化 3-已无效 4-已回收")
    private Integer status;

    @Schema(description = "线索状态名称")
    private String statusName;

    @Schema(description = "转化概率（百分比：0-100）")
    private Integer probability;

    @Schema(description = "预计金额")
    private BigDecimal expectedAmount;

    @Schema(description = "预计成交日期")
    private LocalDate expectedDealDate;

    @Schema(description = "下次跟进时间")
    private LocalDateTime nextFollowTime;

    @Schema(description = "分配负责人ID")
    private Long assignUserId;

    @Schema(description = "分配负责人姓名")
    private String assignUserName;

    @Schema(description = "分配时间")
    private LocalDateTime assignTime;

    @Schema(description = "线索描述/需求描述")
    private String description;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "标签（多个用逗号分隔）")
    private String tags;

    @Schema(description = "标签列表")
    private List<String> tagList;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
