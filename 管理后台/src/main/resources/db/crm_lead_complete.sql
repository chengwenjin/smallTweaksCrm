-- ============================================
-- 线索管理模块完整数据库脚本
-- 客户与线索管理 -> 线索全生命周期 -> 多渠道录入
-- 支持：手工录入、Excel批量导入、API对接
-- 
-- 执行说明：
-- 1. 请使用支持 UTF-8 编码的数据库管理工具执行此脚本
-- 2. 确保当前数据库连接使用 UTF-8 编码
-- 3. 字符集：utf8mb4
-- ============================================

-- ============================================
-- 第一部分：表结构
-- ============================================

-- 1. 线索来源配置表
CREATE TABLE IF NOT EXISTS `crm_lead_source` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `source_code` VARCHAR(50) NOT NULL COMMENT '来源编码',
    `source_name` VARCHAR(100) NOT NULL COMMENT '来源名称',
    `source_type` TINYINT NOT NULL DEFAULT 1 COMMENT '来源类型：1-手动录入 2-批量导入 3-API对接',
    `is_system` TINYINT NOT NULL DEFAULT 0 COMMENT '是否系统内置：0-否 1-是',
    `is_enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `ext1` VARCHAR(255) DEFAULT NULL,
    `ext2` VARCHAR(255) DEFAULT NULL,
    `ext3` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_source_code` (`source_code`),
    KEY `idx_is_enabled` (`is_enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索来源配置表';

-- 2. 线索主表
CREATE TABLE IF NOT EXISTS `crm_lead` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `lead_no` VARCHAR(50) NOT NULL COMMENT '线索编号',
    `lead_name` VARCHAR(100) NOT NULL COMMENT '线索名称',
    `contact_name` VARCHAR(50) DEFAULT NULL COMMENT '联系人姓名',
    `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    `contact_mobile` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `contact_email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `contact_qq` VARCHAR(20) DEFAULT NULL COMMENT 'QQ号码',
    `contact_wechat` VARCHAR(50) DEFAULT NULL COMMENT '微信号',
    `province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
    `city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
    `district` VARCHAR(50) DEFAULT NULL COMMENT '区县',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
    `industry` VARCHAR(100) DEFAULT NULL COMMENT '所属行业',
    `source_id` BIGINT NOT NULL COMMENT '线索来源ID',
    `source_code` VARCHAR(50) NOT NULL COMMENT '线索来源编码',
    `source_name` VARCHAR(100) DEFAULT NULL COMMENT '线索来源名称',
    `source_remark` VARCHAR(255) DEFAULT NULL COMMENT '来源备注',
    `level` TINYINT NOT NULL DEFAULT 3 COMMENT '线索等级：1-高 2-中 3-低',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '线索状态：0-新线索 1-跟进中 2-已转化 3-已无效 4-已回收',
    `probability` INT DEFAULT NULL COMMENT '转化概率',
    `expected_amount` DECIMAL(12, 2) DEFAULT NULL COMMENT '预计金额',
    `expected_deal_date` DATE DEFAULT NULL COMMENT '预计成交日期',
    `next_follow_time` DATETIME DEFAULT NULL COMMENT '下次跟进时间',
    `assign_user_id` BIGINT DEFAULT NULL COMMENT '分配负责人ID',
    `assign_user_name` VARCHAR(50) DEFAULT NULL COMMENT '分配负责人姓名',
    `assign_time` DATETIME DEFAULT NULL COMMENT '分配时间',
    `description` TEXT DEFAULT NULL COMMENT '线索描述',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `ext1` VARCHAR(255) DEFAULT NULL,
    `ext2` VARCHAR(255) DEFAULT NULL,
    `ext3` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_lead_no` (`lead_no`),
    KEY `idx_contact_phone` (`contact_phone`),
    KEY `idx_contact_mobile` (`contact_mobile`),
    KEY `idx_source_id` (`source_id`),
    KEY `idx_source_code` (`source_code`),
    KEY `idx_level` (`level`),
    KEY `idx_status` (`status`),
    KEY `idx_assign_user_id` (`assign_user_id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索主表';

-- 3. 线索操作日志表
CREATE TABLE IF NOT EXISTS `crm_lead_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `lead_id` BIGINT NOT NULL COMMENT '线索ID',
    `lead_no` VARCHAR(50) NOT NULL COMMENT '线索编号',
    `operate_type` TINYINT NOT NULL COMMENT '操作类型',
    `operate_name` VARCHAR(50) NOT NULL COMMENT '操作名称',
    `operate_content` TEXT DEFAULT NULL COMMENT '操作内容',
    `operate_user_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `operate_user_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名',
    `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_lead_id` (`lead_id`),
    KEY `idx_lead_no` (`lead_no`),
    KEY `idx_operate_type` (`operate_type`),
    KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='线索操作日志表';

-- ============================================
-- 第二部分：初始化数据
-- ============================================

-- 4. 初始化线索来源数据
INSERT IGNORE INTO `crm_lead_source` (`source_code`, `source_name`, `source_type`, `is_system`, `is_enabled`, `sort_order`, `description`) VALUES
('manual', '手工录入', 1, 1, 1, 1, '后台手工录入创建线索'),
('excel', 'Excel导入', 2, 1, 1, 2, '通过Excel批量导入创建线索'),
('website', '官网表单', 3, 1, 1, 3, '通过官网表单API对接创建线索'),
('campaign', '市场活动', 3, 1, 1, 4, '通过市场活动API对接创建线索'),
('referral', '客户转介绍', 1, 1, 1, 5, '客户转介绍的线索'),
('phone', '电话咨询', 1, 1, 1, 6, '电话咨询获取的线索'),
('chat', '在线咨询', 3, 1, 1, 7, '在线客服咨询获取的线索'),
('other', '其他来源', 1, 1, 1, 99, '其他渠道获取的线索');

-- ============================================
-- 第三部分：菜单数据
-- ============================================

-- 5. 一级菜单：客户与线索管理
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(0, '客户与线索管理', 1, NULL, '/crm', 'Layout', 'User', 70, 1, 1, 0);

-- 获取刚插入的一级菜单ID
SET @crm_parent_id = LAST_INSERT_ID();

-- 6. 二级菜单：线索全生命周期
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(@crm_parent_id, '线索全生命周期', 1, NULL, 'lead-lifecycle', NULL, 'Connection', 1, 1, 1, 0);

-- 获取刚插入的二级菜单ID
SET @lead_lifecycle_parent_id = LAST_INSERT_ID();

-- 7. 三级菜单：多渠道录入（线索管理）
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(@lead_lifecycle_parent_id, '多渠道录入', 2, 'crm:lead:list', 'lead', 'crm/lead/index', 'Plus', 1, 1, 1, 0);

-- 获取刚插入的三级菜单ID
SET @lead_menu_id = LAST_INSERT_ID();

-- 8. 按钮权限
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`) VALUES
(@lead_menu_id, '新增线索', 3, 'crm:lead:add', 1, 1, 0),
(@lead_menu_id, '编辑线索', 3, 'crm:lead:edit', 2, 1, 0),
(@lead_menu_id, '删除线索', 3, 'crm:lead:delete', 3, 1, 0),
(@lead_menu_id, '导入线索', 3, 'crm:lead:import', 4, 1, 0),
(@lead_menu_id, '分配线索', 3, 'crm:lead:assign', 5, 1, 0);

-- 9. 为管理员角色分配菜单权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) 
SELECT 1, id FROM `sys_menu` WHERE `permission_key` LIKE 'crm:%';

-- 10. API权限配置
INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`) VALUES
('crm:lead:list', '查询线索列表', '线索管理', 'GET', '/api/crm/leads', '分页查询线索列表', 1),
('crm:lead:add', '新增线索', '线索管理', 'POST', '/api/crm/leads', '手工录入线索', 1),
('crm:lead:edit', '编辑线索', '线索管理', 'PUT', '/api/crm/leads/{id}', '更新线索信息', 1),
('crm:lead:delete', '删除线索', '线索管理', 'DELETE', '/api/crm/leads/{id}', '删除线索', 1),
('crm:lead:import', '导入线索', '线索管理', 'POST', '/api/crm/leads/import', 'Excel批量导入线索', 1),
('crm:lead:assign', '分配线索', '线索管理', 'PUT', '/api/crm/leads/{id}/assign', '分配线索给负责人', 1);

-- 11. 为管理员角色分配API权限
INSERT INTO `sys_role_api` (`role_id`, `api_id`) 
SELECT 1, id FROM `sys_api_permission` WHERE `permission_key` LIKE 'crm:%';

-- ============================================
-- 执行完成提示
-- ============================================
-- 脚本执行完成！
-- 请确认以下内容：
-- 1. 表是否创建成功：crm_lead_source, crm_lead, crm_lead_log
-- 2. 来源数据是否初始化：8条来源数据
-- 3. 菜单是否创建成功：一级菜单"客户与线索管理"
-- 4. 字符集确认：所有表使用 utf8mb4 编码
-- ============================================
