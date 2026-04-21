package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 线索更新DTO
 */
@Data
@Schema(description = "线索更新参数")
public class LeadUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "线索ID", example = "1")
    private Long id;

    @Schema(description = "线索名称（客户名称/姓名）", example = "张三")
    private String leadName;

    @Schema(description = "联系人姓名", example = "张三")
    private String contactName;

    @Schema(description = "联系电话", example = "010-12345678")
    private String contactPhone;

    @Schema(description = "手机号", example = "13800138000")
    private String contactMobile;

    @Schema(description = "邮箱", example = "zhangsan@example.com")
    private String contactEmail;

    @Schema(description = "QQ号码", example = "123456")
    private String contactQq;

    @Schema(description = "微信号", example = "wx_zhangsan")
    private String contactWechat;

    @Schema(description = "省份", example = "北京市")
    private String province;

    @Schema(description = "城市", example = "北京市")
    private String city;

    @Schema(description = "区县", example = "朝阳区")
    private String district;

    @Schema(description = "详细地址", example = "某某街道某某号")
    private String address;

    @Schema(description = "所属行业", example = "IT互联网")
    private String industry;

    @Schema(description = "线索来源ID", example = "1")
    private Long sourceId;

    @Schema(description = "来源备注", example = "通过官网表单提交")
    private String sourceRemark;

    @Schema(description = "线索等级：1-高 2-中 3-低", example = "2")
    private Integer level;

    @Schema(description = "线索状态：0-新线索 1-跟进中 2-已转化 3-已无效 4-已回收", example = "1")
    private Integer status;

    @Schema(description = "转化概率（百分比：0-100）", example = "50")
    private Integer probability;

    @Schema(description = "预计金额", example = "10000.00")
    private BigDecimal expectedAmount;

    @Schema(description = "预计成交日期", example = "2026-05-01")
    private LocalDate expectedDealDate;

    @Schema(description = "下次跟进时间", example = "2026-04-22T10:00:00")
    private LocalDateTime nextFollowTime;

    @Schema(description = "分配负责人ID", example = "2")
    private Long assignUserId;

    @Schema(description = "线索描述/需求描述", example = "客户有软件开发需求")
    private String description;

    @Schema(description = "备注", example = "备注信息")
    private String remark;

    @Schema(description = "标签（多个用逗号分隔）", example = "重要客户,潜在客户")
    private String tags;
}
