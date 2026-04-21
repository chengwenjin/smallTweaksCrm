package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 线索主表实体
 */
@Data
@TableName("crm_lead")
public class CrmLead implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 线索编号
     */
    private String leadNo;

    /**
     * 线索名称（客户名称/姓名）
     */
    private String leadName;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 手机号
     */
    private String contactMobile;

    /**
     * 邮箱
     */
    private String contactEmail;

    /**
     * QQ号码
     */
    private String contactQq;

    /**
     * 微信号
     */
    private String contactWechat;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 线索来源ID
     */
    private Long sourceId;

    /**
     * 线索来源编码
     */
    private String sourceCode;

    /**
     * 线索来源名称
     */
    private String sourceName;

    /**
     * 来源备注
     */
    private String sourceRemark;

    /**
     * 线索等级：1-高 2-中 3-低
     */
    private Integer level;

    /**
     * 线索状态：0-新线索 1-跟进中 2-已转化 3-已无效 4-已回收
     */
    private Integer status;

    /**
     * 转化概率（百分比：0-100）
     */
    private Integer probability;

    /**
     * 预计金额
     */
    private BigDecimal expectedAmount;

    /**
     * 预计成交日期
     */
    private LocalDate expectedDealDate;

    /**
     * 下次跟进时间
     */
    private LocalDateTime nextFollowTime;

    /**
     * 分配负责人ID
     */
    private Long assignUserId;

    /**
     * 分配负责人姓名
     */
    private String assignUserName;

    /**
     * 分配时间
     */
    private LocalDateTime assignTime;

    /**
     * 线索描述/需求描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;

    /**
     * 标签（多个用逗号分隔）
     */
    private String tags;

    /**
     * 软删除：0-未删除 1-已删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 预留扩展字段1
     */
    private String ext1;

    /**
     * 预留扩展字段2
     */
    private String ext2;

    /**
     * 预留扩展字段3
     */
    private String ext3;
}
