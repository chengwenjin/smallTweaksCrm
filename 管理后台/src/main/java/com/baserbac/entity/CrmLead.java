package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("crm_lead")
public class CrmLead implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Override
    public CrmLead clone() {
        try {
            return (CrmLead) super.clone();
        } catch (CloneNotSupportedException e) {
            CrmLead clone = new CrmLead();
            clone.setId(this.id);
            clone.setLeadNo(this.leadNo);
            clone.setLeadName(this.leadName);
            clone.setContactName(this.contactName);
            clone.setContactPhone(this.contactPhone);
            clone.setContactMobile(this.contactMobile);
            clone.setContactEmail(this.contactEmail);
            clone.setProvince(this.province);
            clone.setCity(this.city);
            clone.setDistrict(this.district);
            clone.setIndustry(this.industry);
            clone.setSourceId(this.sourceId);
            clone.setSourceName(this.sourceName);
            clone.setLevel(this.level);
            clone.setStatus(this.status);
            clone.setExpectedAmount(this.expectedAmount);
            clone.setAssignUserId(this.assignUserId);
            clone.setAssignUserName(this.assignUserName);
            clone.setAssignTime(this.assignTime);
            clone.setIsPublic(this.isPublic);
            clone.setPublicReason(this.publicReason);
            clone.setPublicTime(this.publicTime);
            clone.setLastFollowTime(this.lastFollowTime);
            clone.setFollowCount(this.followCount);
            clone.setSourceUserId(this.sourceUserId);
            clone.setSourceUserName(this.sourceUserName);
            clone.setEmployeeCount(this.employeeCount);
            clone.setAnnualRevenue(this.annualRevenue);
            clone.setCompanyScale(this.companyScale);
            return clone;
        }
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String leadNo;

    private String leadName;

    private String contactName;

    private String contactPhone;

    private String contactMobile;

    private String contactEmail;

    private String contactQq;

    private String contactWechat;

    private String province;

    private String city;

    private String district;

    private String address;

    private String industry;

    private Long sourceId;

    private String sourceCode;

    private String sourceName;

    private String sourceRemark;

    private Integer level;

    private Integer status;

    private Integer probability;

    private BigDecimal expectedAmount;

    private LocalDate expectedDealDate;

    private LocalDateTime nextFollowTime;

    private Long assignUserId;

    private String assignUserName;

    private LocalDateTime assignTime;

    private String description;

    private String remark;

    private String tags;

    private Integer isPublic;

    private String publicReason;

    private LocalDateTime publicTime;

    private LocalDateTime lastFollowTime;

    private Long followCount;

    private Long sourceUserId;

    private String sourceUserName;

    private Integer employeeCount;

    private BigDecimal annualRevenue;

    private String companyScale;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private String ext1;

    private String ext2;

    private String ext3;
}
