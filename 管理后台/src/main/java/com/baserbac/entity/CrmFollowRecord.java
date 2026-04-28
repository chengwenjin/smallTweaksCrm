package com.baserbac.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("crm_follow_record")
public class CrmFollowRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String BUSINESS_TYPE_CUSTOMER = "customer";
    public static final String BUSINESS_TYPE_LEAD = "lead";
    public static final String BUSINESS_TYPE_OPPORTUNITY = "opportunity";

    public static final int FOLLOW_TYPE_PHONE = 1;
    public static final int FOLLOW_TYPE_WECHAT = 2;
    public static final int FOLLOW_TYPE_EMAIL = 3;
    public static final int FOLLOW_TYPE_VISIT = 4;
    public static final int FOLLOW_TYPE_OTHER = 5;

    public static final String[] FOLLOW_TYPE_NAMES = {"", "电话", "微信", "邮件", "拜访", "其他"};

    public static final int CONTENT_TYPE_TEXT = 1;
    public static final int CONTENT_TYPE_VOICE = 2;
    public static final int CONTENT_TYPE_IMAGE = 3;
    public static final int CONTENT_TYPE_VIDEO = 4;
    public static final int CONTENT_TYPE_FILE = 5;

    public static final String[] CONTENT_TYPE_NAMES = {"", "文本", "语音", "图片", "视频", "文件"};

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String businessType;

    private Long businessId;

    private String businessName;

    private Long followUserId;

    private String followUserName;

    private Integer followType;

    private Integer contentType;

    private String textContent;

    private String voiceUrl;

    private Integer voiceDuration;

    private String imageUrls;

    private String videoUrl;

    private Integer videoDuration;

    private String fileUrl;

    private String fileName;

    private Long fileSize;

    private BigDecimal locationLatitude;

    private BigDecimal locationLongitude;

    private String locationAddress;

    private LocalDateTime nextFollowTime;

    private String nextFollowRemark;

    private Long todoId;

    private Integer isLast;

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
}
