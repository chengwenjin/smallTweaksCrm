-- ============================================================
-- 客户360°全景档案 - 数据库表结构脚本
-- 客户与线索管理 -> 客户360°全景档案
-- ============================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- 1. 客户等级配置表
CREATE TABLE IF NOT EXISTS `crm_customer_level` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `level_code` VARCHAR(20) NOT NULL COMMENT '等级编码：A、B、C、D',
    `level_name` VARCHAR(50) NOT NULL COMMENT '等级名称',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '等级描述',
    `is_enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_level_code` (`level_code`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户等级配置表';

-- 2. 客户标签配置表
CREATE TABLE IF NOT EXISTS `crm_customer_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `tag_name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `tag_color` VARCHAR(20) DEFAULT NULL COMMENT '标签颜色',
    `tag_category` VARCHAR(50) DEFAULT NULL COMMENT '标签分类：高净值、价格敏感、行业属性等',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '标签描述',
    `is_enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tag_name` (`tag_name`),
    KEY `idx_tag_category` (`tag_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户标签配置表';

-- 3. 客户主表（基础信息库）
CREATE TABLE IF NOT EXISTS `crm_customer` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_no` VARCHAR(50) NOT NULL COMMENT '客户编号',
    `customer_name` VARCHAR(100) NOT NULL COMMENT '客户名称（企业名称）',
    `short_name` VARCHAR(50) DEFAULT NULL COMMENT '客户简称',
    `customer_type` VARCHAR(20) DEFAULT '1' COMMENT '客户类型：1-企业客户 2-个人客户',
    `credit_code` VARCHAR(50) DEFAULT NULL COMMENT '统一社会信用代码（工商信息）',
    `legal_person` VARCHAR(50) DEFAULT NULL COMMENT '法定代表人（工商信息）',
    `registered_capital` DECIMAL(18,2) DEFAULT NULL COMMENT '注册资本（万元，工商信息）',
    `establish_date` DATE DEFAULT NULL COMMENT '成立日期（工商信息）',
    `business_status` VARCHAR(20) DEFAULT NULL COMMENT '经营状态：存续、注销、吊销等',
    `business_scope` TEXT DEFAULT NULL COMMENT '经营范围（工商信息）',
    `registered_address` VARCHAR(255) DEFAULT NULL COMMENT '注册地址（工商信息）',
    `industry` VARCHAR(100) DEFAULT NULL COMMENT '所属行业',
    `province` VARCHAR(50) DEFAULT NULL COMMENT '省份',
    `city` VARCHAR(50) DEFAULT NULL COMMENT '城市',
    `district` VARCHAR(50) DEFAULT NULL COMMENT '区县',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
    `website` VARCHAR(100) DEFAULT NULL COMMENT '企业官网',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '企业邮箱',
    `phone` VARCHAR(50) DEFAULT NULL COMMENT '企业电话',
    `fax` VARCHAR(50) DEFAULT NULL COMMENT '企业传真',
    `employee_count` INT DEFAULT NULL COMMENT '员工人数',
    `annual_revenue` DECIMAL(18,2) DEFAULT NULL COMMENT '年营收（万元）',
    `company_scale` VARCHAR(50) DEFAULT NULL COMMENT '企业规模描述',
    `level_code` VARCHAR(20) DEFAULT NULL COMMENT '客户等级：A、B、C、D',
    `owner_user_id` BIGINT DEFAULT NULL COMMENT '负责人ID',
    `owner_user_name` VARCHAR(50) DEFAULT NULL COMMENT '负责人姓名',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签（多个用逗号分隔）',
    `source` VARCHAR(50) DEFAULT NULL COMMENT '客户来源',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '客户状态：0-潜在 1-合作中 2-已流失 3-休眠',
    `first_contact_time` DATETIME DEFAULT NULL COMMENT '首次接触时间',
    `last_contact_time` DATETIME DEFAULT NULL COMMENT '最后联系时间',
    `description` TEXT DEFAULT NULL COMMENT '客户描述',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `parent_customer_id` BIGINT DEFAULT NULL COMMENT '上级客户ID（组织架构）',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `ext1` VARCHAR(255) DEFAULT NULL,
    `ext2` VARCHAR(255) DEFAULT NULL,
    `ext3` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_customer_no` (`customer_no`),
    KEY `idx_customer_name` (`customer_name`),
    KEY `idx_credit_code` (`credit_code`),
    KEY `idx_level_code` (`level_code`),
    KEY `idx_owner_user_id` (`owner_user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_province` (`province`),
    KEY `idx_city` (`city`),
    KEY `idx_industry` (`industry`),
    KEY `idx_parent_customer_id` (`parent_customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户主表';

-- 4. 客户联系人表
CREATE TABLE IF NOT EXISTS `crm_customer_contact` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `contact_name` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
    `contact_position` VARCHAR(50) DEFAULT NULL COMMENT '联系人职位',
    `contact_department` VARCHAR(50) DEFAULT NULL COMMENT '联系人部门',
    `mobile` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `phone` VARCHAR(50) DEFAULT NULL COMMENT '办公电话',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `wechat` VARCHAR(50) DEFAULT NULL COMMENT '微信号',
    `qq` VARCHAR(20) DEFAULT NULL COMMENT 'QQ号',
    `is_key_contact` TINYINT NOT NULL DEFAULT 0 COMMENT '是否关键联系人：0-否 1-是',
    `is_primary` TINYINT NOT NULL DEFAULT 0 COMMENT '是否主联系人：0-否 1-是',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `gender` TINYINT DEFAULT NULL COMMENT '性别：0-未知 1-男 2-女',
    `birthday` DATE DEFAULT NULL COMMENT '生日',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '联系人描述',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_mobile` (`mobile`),
    KEY `idx_is_key_contact` (`is_key_contact`),
    KEY `idx_is_primary` (`is_primary`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户联系人表';

-- 5. 客户标签关联表
CREATE TABLE IF NOT EXISTS `crm_customer_tag_rel` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_customer_tag` (`customer_id`, `tag_id`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户标签关联表';

-- 6. 客户跟进记录表（全景视图使用）
CREATE TABLE IF NOT EXISTS `crm_customer_follow` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `follow_user_id` BIGINT NOT NULL COMMENT '跟进人ID',
    `follow_user_name` VARCHAR(50) DEFAULT NULL COMMENT '跟进人姓名',
    `follow_type` TINYINT NOT NULL DEFAULT 5 COMMENT '跟进方式：1-电话 2-微信 3-邮件 4-拜访 5-其他',
    `follow_content` TEXT NOT NULL COMMENT '跟进内容',
    `next_follow_time` DATETIME DEFAULT NULL COMMENT '下次跟进时间',
    `next_follow_remark` VARCHAR(500) DEFAULT NULL COMMENT '下次跟进备注',
    `is_last` TINYINT NOT NULL DEFAULT 1 COMMENT '是否最后一次跟进：1-是 0-否',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_follow_user_id` (`follow_user_id`),
    KEY `idx_follow_type` (`follow_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户跟进记录表';

-- 7. 客户报价记录表（全景视图使用）
CREATE TABLE IF NOT EXISTS `crm_customer_quote` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `quote_no` VARCHAR(50) NOT NULL COMMENT '报价单号',
    `quote_name` VARCHAR(100) NOT NULL COMMENT '报价名称',
    `quote_amount` DECIMAL(18,2) NOT NULL COMMENT '报价金额',
    `quote_date` DATE NOT NULL COMMENT '报价日期',
    `valid_date` DATE DEFAULT NULL COMMENT '有效截止日期',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '报价状态：0-待确认 1-已接受 2-已拒绝 3-已过期',
    `description` TEXT DEFAULT NULL COMMENT '报价说明',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_quote_no` (`quote_no`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_status` (`status`),
    KEY `idx_quote_date` (`quote_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户报价记录表';

-- 8. 客户合同订单表（全景视图使用）
CREATE TABLE IF NOT EXISTS `crm_customer_contract` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `contract_no` VARCHAR(50) NOT NULL COMMENT '合同编号',
    `contract_name` VARCHAR(100) NOT NULL COMMENT '合同名称',
    `contract_type` VARCHAR(20) DEFAULT NULL COMMENT '合同类型',
    `contract_amount` DECIMAL(18,2) NOT NULL COMMENT '合同金额',
    `signed_date` DATE DEFAULT NULL COMMENT '签订日期',
    `start_date` DATE DEFAULT NULL COMMENT '开始日期',
    `end_date` DATE DEFAULT NULL COMMENT '结束日期',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '合同状态：0-待执行 1-执行中 2-已完成 3-已终止',
    `description` TEXT DEFAULT NULL COMMENT '合同说明',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_contract_no` (`contract_no`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_status` (`status`),
    KEY `idx_signed_date` (`signed_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户合同订单表';

-- 9. 客户回款流水表（全景视图使用）
CREATE TABLE IF NOT EXISTS `crm_customer_payment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `contract_id` BIGINT DEFAULT NULL COMMENT '合同ID',
    `payment_no` VARCHAR(50) NOT NULL COMMENT '回款单号',
    `payment_amount` DECIMAL(18,2) NOT NULL COMMENT '回款金额',
    `payment_date` DATE NOT NULL COMMENT '回款日期',
    `payment_method` VARCHAR(20) DEFAULT NULL COMMENT '回款方式：1-现金 2-银行转账 3-支票 4-其他',
    `payment_status` TINYINT NOT NULL DEFAULT 1 COMMENT '回款状态：0-待确认 1-已到账',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_payment_no` (`payment_no`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_contract_id` (`contract_id`),
    KEY `idx_payment_date` (`payment_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户回款流水表';

-- 10. 客户售后工单表（全景视图使用）
CREATE TABLE IF NOT EXISTS `crm_customer_ticket` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `ticket_no` VARCHAR(50) NOT NULL COMMENT '工单编号',
    `ticket_title` VARCHAR(100) NOT NULL COMMENT '工单标题',
    `ticket_type` VARCHAR(20) DEFAULT NULL COMMENT '工单类型：1-咨询 2-故障 3-投诉 4-建议',
    `priority` TINYINT NOT NULL DEFAULT 2 COMMENT '优先级：1-高 2-中 3-低',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '工单状态：0-待处理 1-处理中 2-已解决 3-已关闭',
    `description` TEXT NOT NULL COMMENT '工单描述',
    `solution` TEXT DEFAULT NULL COMMENT '解决方案',
    `assignee_user_id` BIGINT DEFAULT NULL COMMENT '处理人ID',
    `assignee_user_name` VARCHAR(50) DEFAULT NULL COMMENT '处理人姓名',
    `create_user_id` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `create_user_name` VARCHAR(50) DEFAULT NULL COMMENT '创建人姓名',
    `resolved_time` DATETIME DEFAULT NULL COMMENT '解决时间',
    `closed_time` DATETIME DEFAULT NULL COMMENT '关闭时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ticket_no` (`ticket_no`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_status` (`status`),
    KEY `idx_priority` (`priority`),
    KEY `idx_assignee_user_id` (`assignee_user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户售后工单表';

-- ============================================================
-- 初始化数据
-- ============================================================

-- 客户等级初始化
INSERT IGNORE INTO `crm_customer_level` (`level_code`, `level_name`, `sort_order`, `description`, `is_enabled`) VALUES
('A', 'A级客户（战略客户）', 1, '战略级重要客户，高价值，长期合作', 1),
('B', 'B级客户（重要客户）', 2, '重要客户，中高价值，稳定合作', 1),
('C', 'C级客户（普通客户）', 3, '普通客户，中等价值，有合作', 1),
('D', 'D级客户（潜在客户）', 4, '潜在客户，低价值，待开发', 1);

-- 客户标签初始化
INSERT IGNORE INTO `crm_customer_tag` (`tag_name`, `tag_color`, `tag_category`, `sort_order`, `description`, `is_enabled`) VALUES
('高净值', '#1890ff', '价值属性', 1, '高价值客户', 1),
('价格敏感', '#ff4d4f', '价值属性', 2, '对价格敏感', 1),
('长期合作', '#52c41a', '合作属性', 3, '长期稳定合作', 1),
('新客户', '#13c2c2', '合作属性', 4, '新开发客户', 1),
('流失风险', '#faad14', '风险属性', 5, '有流失风险', 1),
('需要跟进', '#722ed1', '跟进属性', 6, '需要重点跟进', 1),
('国企', '#1890ff', '企业属性', 7, '国有企业', 1),
('外企', '#13c2c2', '企业属性', 8, '外资企业', 1),
('民企', '#52c41a', '企业属性', 9, '民营企业', 1),
('上市公司', '#faad14', '企业属性', 10, '上市公司', 1),
('集团公司', '#722ed1', '企业属性', 11, '集团型企业', 1),
('技术驱动', '#1890ff', '行业属性', 12, '技术驱动型企业', 1),
('快速增长', '#52c41a', '发展属性', 13, '快速增长期', 1),
('稳定发展', '#13c2c2', '发展属性', 14, '稳定发展期', 1);
