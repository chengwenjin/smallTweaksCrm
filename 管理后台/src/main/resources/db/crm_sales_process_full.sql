-- ============================================================
-- 销售过程与商机管理 -> 过程跟进与外勤
-- 完整数据库脚本（表结构 + 菜单数据 + 测试数据）
-- ============================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- 1. 跟进记录表（支持多类型内容：文本、语音、图片等）
CREATE TABLE IF NOT EXISTS `crm_follow_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `business_type` VARCHAR(20) NOT NULL DEFAULT 'customer' COMMENT '关联业务类型：customer-客户, lead-线索, opportunity-商机',
    `business_id` BIGINT NOT NULL COMMENT '关联业务ID',
    `business_name` VARCHAR(100) DEFAULT NULL COMMENT '关联业务名称',
    `follow_user_id` BIGINT NOT NULL COMMENT '跟进人ID',
    `follow_user_name` VARCHAR(50) DEFAULT NULL COMMENT '跟进人姓名',
    `follow_type` TINYINT NOT NULL DEFAULT 5 COMMENT '跟进方式：1-电话 2-微信 3-邮件 4-拜访 5-其他',
    `content_type` TINYINT NOT NULL DEFAULT 1 COMMENT '内容类型：1-文本 2-语音 3-图片 4-视频 5-文件',
    `text_content` TEXT DEFAULT NULL COMMENT '文本内容',
    `voice_url` VARCHAR(500) DEFAULT NULL COMMENT '语音文件URL',
    `voice_duration` INT DEFAULT NULL COMMENT '语音时长（秒）',
    `image_urls` TEXT DEFAULT NULL COMMENT '图片URL列表（JSON数组）',
    `video_url` VARCHAR(500) DEFAULT NULL COMMENT '视频文件URL',
    `video_duration` INT DEFAULT NULL COMMENT '视频时长（秒）',
    `file_url` VARCHAR(500) DEFAULT NULL COMMENT '附件文件URL',
    `file_name` VARCHAR(200) DEFAULT NULL COMMENT '附件文件名',
    `file_size` BIGINT DEFAULT NULL COMMENT '附件文件大小（字节）',
    `location_latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '位置纬度',
    `location_longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '位置经度',
    `location_address` VARCHAR(200) DEFAULT NULL COMMENT '位置地址',
    `next_follow_time` DATETIME DEFAULT NULL COMMENT '下次跟进时间',
    `next_follow_remark` VARCHAR(500) DEFAULT NULL COMMENT '下次跟进备注',
    `todo_id` BIGINT DEFAULT NULL COMMENT '关联待办ID',
    `is_last` TINYINT NOT NULL DEFAULT 1 COMMENT '是否最后一次跟进：1-是 0-否',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_business` (`business_type`, `business_id`),
    KEY `idx_follow_user_id` (`follow_user_id`),
    KEY `idx_follow_type` (`follow_type`),
    KEY `idx_content_type` (`content_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='跟进记录表';

-- 2. 外勤签到表
CREATE TABLE IF NOT EXISTS `crm_field_checkin` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `checkin_no` VARCHAR(50) NOT NULL COMMENT '签到编号',
    `checkin_user_id` BIGINT NOT NULL COMMENT '签到人ID',
    `checkin_user_name` VARCHAR(50) DEFAULT NULL COMMENT '签到人姓名',
    `checkin_type` TINYINT NOT NULL DEFAULT 1 COMMENT '签到类型：1-签到 2-签退',
    `checkin_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    `latitude` DECIMAL(10,7) NOT NULL COMMENT '纬度',
    `longitude` DECIMAL(10,7) NOT NULL COMMENT '经度',
    `location_address` VARCHAR(200) DEFAULT NULL COMMENT '位置地址',
    `location_province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
    `location_city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
    `location_district` VARCHAR(50) DEFAULT NULL COMMENT '区县',
    `photo_urls` TEXT DEFAULT NULL COMMENT '现场照片URL列表（JSON数组）',
    `business_type` VARCHAR(20) DEFAULT NULL COMMENT '关联业务类型：customer-客户, lead-线索, opportunity-商机',
    `business_id` BIGINT DEFAULT NULL COMMENT '关联业务ID',
    `business_name` VARCHAR(100) DEFAULT NULL COMMENT '关联业务名称',
    `purpose` VARCHAR(200) DEFAULT NULL COMMENT '拜访目的',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `device_info` TEXT DEFAULT NULL COMMENT '设备信息（JSON）',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `network_type` VARCHAR(20) DEFAULT NULL COMMENT '网络类型：wifi, 4g, 5g',
    `battery_level` INT DEFAULT NULL COMMENT '电量百分比',
    `is_abnormal` TINYINT NOT NULL DEFAULT 0 COMMENT '是否异常：0-正常 1-异常',
    `abnormal_reason` VARCHAR(200) DEFAULT NULL COMMENT '异常原因',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_checkin_no` (`checkin_no`),
    KEY `idx_checkin_user_id` (`checkin_user_id`),
    KEY `idx_checkin_time` (`checkin_time`),
    KEY `idx_business` (`business_type`, `business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外勤签到表';

-- 3. 外勤签到轨迹记录表
CREATE TABLE IF NOT EXISTS `crm_field_track` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `track_no` VARCHAR(50) NOT NULL COMMENT '轨迹编号',
    `track_user_id` BIGINT NOT NULL COMMENT '轨迹用户ID',
    `track_user_name` VARCHAR(50) DEFAULT NULL COMMENT '轨迹用户姓名',
    `track_date` DATE NOT NULL COMMENT '轨迹日期',
    `latitude` DECIMAL(10,7) NOT NULL COMMENT '纬度',
    `longitude` DECIMAL(10,7) NOT NULL COMMENT '经度',
    `location_address` VARCHAR(200) DEFAULT NULL COMMENT '位置地址',
    `location_province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
    `location_city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
    `location_district` VARCHAR(50) DEFAULT NULL COMMENT '区县',
    `accuracy` DECIMAL(10,2) DEFAULT NULL COMMENT '定位精度（米）',
    `speed` DECIMAL(10,2) DEFAULT NULL COMMENT '移动速度（km/h）',
    `direction` INT DEFAULT NULL COMMENT '方向角度（0-360）',
    `record_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_track_user` (`track_user_id`, `track_date`),
    KEY `idx_record_time` (`record_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='外勤签到轨迹记录表';

-- 4. 待办提醒表
CREATE TABLE IF NOT EXISTS `crm_todo_reminder` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `todo_no` VARCHAR(50) NOT NULL COMMENT '待办编号',
    `user_id` BIGINT NOT NULL COMMENT '所属用户ID',
    `user_name` VARCHAR(50) DEFAULT NULL COMMENT '所属用户姓名',
    `title` VARCHAR(100) NOT NULL COMMENT '待办标题',
    `content` TEXT DEFAULT NULL COMMENT '待办内容',
    `priority` TINYINT NOT NULL DEFAULT 2 COMMENT '优先级：1-高 2-中 3-低',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待处理 1-已完成 2-已取消 3-已过期',
    `remind_time` DATETIME DEFAULT NULL COMMENT '提醒时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '截止时间',
    `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
    `complete_remark` VARCHAR(500) DEFAULT NULL COMMENT '完成备注',
    `business_type` VARCHAR(20) DEFAULT NULL COMMENT '关联业务类型：customer-客户, lead-线索, opportunity-商机, follow-跟进',
    `business_id` BIGINT DEFAULT NULL COMMENT '关联业务ID',
    `business_name` VARCHAR(100) DEFAULT NULL COMMENT '关联业务名称',
    `follow_record_id` BIGINT DEFAULT NULL COMMENT '关联跟进记录ID',
    `remind_type` TINYINT NOT NULL DEFAULT 1 COMMENT '提醒方式：1-系统通知 2-短信 3-邮件',
    `remind_count` INT NOT NULL DEFAULT 0 COMMENT '已提醒次数',
    `last_remind_time` DATETIME DEFAULT NULL COMMENT '最后提醒时间',
    `is_recurring` TINYINT NOT NULL DEFAULT 0 COMMENT '是否重复：0-否 1-是',
    `recurring_type` VARCHAR(20) DEFAULT NULL COMMENT '重复类型：daily-每日, weekly-每周, monthly-每月',
    `recurring_config` TEXT DEFAULT NULL COMMENT '重复配置（JSON）',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_todo_no` (`todo_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`),
    KEY `idx_remind_time` (`remind_time`),
    KEY `idx_end_time` (`end_time`),
    KEY `idx_business` (`business_type`, `business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='待办提醒表';

-- ============================================================
-- 菜单数据初始化
-- ============================================================

-- 1. 插入销售过程与商机管理顶级菜单（如果不存在）
INSERT IGNORE INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(0, '销售过程与商机管理', 1, NULL, '/crm/sales-process', 'Layout', 'TrendCharts', 70, 1, 1, 1);

-- 2. 插入过程跟进与外拍子菜单目录
SET @sales_process_menu_id = (SELECT id FROM `sys_menu` WHERE `menu_name` = '销售过程与商机管理' AND `parent_id` = 0);

INSERT IGNORE INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(@sales_process_menu_id, '过程跟进与外勤', 1, NULL, 'field-work', 'Layout', 'UserFilled', 1, 1, 1, 1);

-- 3. 插入过程跟进与外拍子菜单
SET @field_work_menu_id = (SELECT id FROM `sys_menu` WHERE `menu_name` = '过程跟进与外勤' AND `parent_id` = @sales_process_menu_id);

INSERT IGNORE INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(@field_work_menu_id, '跟进记录', 2, 'crm:follow:list', 'follow-record', 'crm/sales-process/follow-record', 'Document', 1, 1, 1, 1),
(@field_work_menu_id, '外勤签到', 2, 'crm:checkin:list', 'field-checkin', 'crm/sales-process/field-checkin', 'Location', 2, 1, 1, 1),
(@field_work_menu_id, '待办提醒', 2, 'crm:todo:list', 'todo-reminder', 'crm/sales-process/todo-reminder', 'Bell', 3, 1, 1, 1);

-- 4. 插入按钮权限
SET @follow_menu_id = (SELECT id FROM `sys_menu` WHERE `menu_name` = '跟进记录' AND `parent_id` = @field_work_menu_id);
SET @checkin_menu_id = (SELECT id FROM `sys_menu` WHERE `menu_name` = '外勤签到' AND `parent_id` = @field_work_menu_id);
SET @todo_menu_id = (SELECT id FROM `sys_menu` WHERE `menu_name` = '待办提醒' AND `parent_id` = @field_work_menu_id);

-- 跟进记录按钮
INSERT IGNORE INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`) VALUES
(@follow_menu_id, '新增跟进', 3, 'crm:follow:add', 1, 1, 1),
(@follow_menu_id, '删除跟进', 3, 'crm:follow:delete', 2, 1, 1);

-- 外勤签到按钮
INSERT IGNORE INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`) VALUES
(@checkin_menu_id, '新增签到', 3, 'crm:checkin:add', 1, 1, 1),
(@checkin_menu_id, '查看轨迹', 3, 'crm:checkin:track', 2, 1, 1);

-- 待办提醒按钮
INSERT IGNORE INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`) VALUES
(@todo_menu_id, '新增待办', 3, 'crm:todo:add', 1, 1, 1),
(@todo_menu_id, '编辑待办', 3, 'crm:todo:edit', 2, 1, 1),
(@todo_menu_id, '删除待办', 3, 'crm:todo:delete', 3, 1, 1),
(@todo_menu_id, '完成待办', 3, 'crm:todo:complete', 4, 1, 1),
(@todo_menu_id, '取消待办', 3, 'crm:todo:cancel', 5, 1, 1);

-- 5. 插入接口权限
INSERT IGNORE INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`) VALUES
('crm:follow:list', '查询跟进记录列表', '跟进记录管理', 'GET', '/api/crm/follow-records', '分页查询跟进记录', 1),
('crm:follow:add', '新增跟进记录', '跟进记录管理', 'POST', '/api/crm/follow-records', '创建跟进记录', 1),
('crm:follow:delete', '删除跟进记录', '跟进记录管理', 'DELETE', '/api/crm/follow-records/{id}', '删除跟进记录', 1),
('crm:checkin:list', '查询签到记录列表', '外勤签到管理', 'GET', '/api/crm/field-checkins', '分页查询签到记录', 1),
('crm:checkin:add', '新增签到记录', '外勤签到管理', 'POST', '/api/crm/field-checkins', '创建签到记录', 1),
('crm:checkin:track', '查看轨迹', '外勤签到管理', 'GET', '/api/crm/field-checkins/tracks', '查询轨迹记录', 1),
('crm:todo:list', '查询待办列表', '待办提醒管理', 'GET', '/api/crm/todo-reminders', '分页查询待办提醒', 1),
('crm:todo:add', '新增待办', '待办提醒管理', 'POST', '/api/crm/todo-reminders', '创建待办提醒', 1),
('crm:todo:edit', '编辑待办', '待办提醒管理', 'PUT', '/api/crm/todo-reminders/{id}', '更新待办提醒', 1),
('crm:todo:delete', '删除待办', '待办提醒管理', 'DELETE', '/api/crm/todo-reminders/{id}', '删除待办', 1),
('crm:todo:complete', '完成待办', '待办提醒管理', 'POST', '/api/crm/todo-reminders/{id}/complete', '完成待办', 1),
('crm:todo:cancel', '取消待办', '待办提醒管理', 'POST', '/api/crm/todo-reminders/{id}/cancel', '取消待办', 1);

-- 6. 给超级管理员分配新菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `sys_menu` WHERE `parent_id` = @sales_process_menu_id 
OR `parent_id` = @field_work_menu_id
OR `parent_id` = @follow_menu_id
OR `parent_id` = @checkin_menu_id
OR `parent_id` = @todo_menu_id;

-- 7. 给超级管理员分配新接口权限
INSERT IGNORE INTO `sys_role_api` (`role_id`, `api_id`)
SELECT 1, id FROM `sys_api_permission` WHERE `permission_key` LIKE 'crm:%';
