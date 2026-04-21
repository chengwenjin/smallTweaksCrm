package com.baserbac.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * API 对接创建线索 DTO
 * 用于官网表单、市场活动等外部系统对接
 */
@Data
@Schema(description = "API对接创建线索参数")
public class LeadApiCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "线索名称不能为空")
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

    @NotBlank(message = "来源编码不能为空")
    @Schema(description = "来源编码：website-官网表单 campaign-市场活动", example = "website")
    private String sourceCode;

    @Schema(description = "来源名称（如具体活动名称）", example = "2024春季促销活动")
    private String sourceName;

    @Schema(description = "来源备注", example = "通过官网联系我们表单提交")
    private String sourceRemark;

    @Schema(description = "线索描述/需求描述", example = "客户有软件开发需求")
    private String description;

    @Schema(description = "备注", example = "备注信息")
    private String remark;

    @Schema(description = "标签（多个用逗号分隔）", example = "官网客户,潜在客户")
    private String tags;
}
