-- ============================================
-- 销售过程与商机管理模块数据库表结构
-- 销售过程与商机管理 -> 标准化销售漏斗 -> 阶段管理
-- 支持：销售阶段配置、商机管理、销售漏斗统计
-- ============================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- 1. 销售阶段配置表
-- 预设标准阶段：初步接洽、需求确认、方案报价、商务谈判、赢单/输单
CREATE TABLE IF NOT EXISTS `crm_sales_stage` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `stage_code` VARCHAR(50) NOT NULL COMMENT '阶段编码（如：initial_contact, requirement_confirmed）',
    `stage_name` VARCHAR(100) NOT NULL COMMENT '阶段名称（如：初步接洽、需求确认）',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号（阶段顺序）',
    `win_probability` DECIMAL(5, 2) NOT NULL DEFAULT 0.00 COMMENT '赢率（百分比：0-100）',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '阶段描述',
    `is_system` TINYINT NOT NULL DEFAULT 0 COMMENT '是否系统预设：0-否 1-是',
    `is_enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
    `is_closed` TINYINT NOT NULL DEFAULT 0 COMMENT '是否结束阶段：0-否 1-是（赢单/输单属于结束阶段）',
    `close_type` TINYINT NOT NULL DEFAULT 0 COMMENT '结束类型：0-非结束阶段 1-赢单 2-输单',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `ext1` VARCHAR(255) DEFAULT NULL,
    `ext2` VARCHAR(255) DEFAULT NULL,
    `ext3` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_stage_code` (`stage_code`),
    KEY `idx_sort_order` (`sort_order`),
    KEY `idx_is_enabled` (`is_enabled`),
    KEY `idx_is_closed` (`is_closed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售阶段配置表';

-- 2. 商机主表
CREATE TABLE IF NOT EXISTS `crm_business_opportunity` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `opportunity_no` VARCHAR(50) NOT NULL COMMENT '商机编号',
    `opportunity_name` VARCHAR(200) NOT NULL COMMENT '商机名称',
    `customer_id` BIGINT DEFAULT NULL COMMENT '关联客户ID',
    `customer_name` VARCHAR(100) DEFAULT NULL COMMENT '客户名称',
    `lead_id` BIGINT DEFAULT NULL COMMENT '关联线索ID（从线索转化而来）',
    `stage_id` BIGINT NOT NULL COMMENT '当前销售阶段ID',
    `stage_code` VARCHAR(50) NOT NULL COMMENT '当前销售阶段编码',
    `stage_name` VARCHAR(100) DEFAULT NULL COMMENT '当前销售阶段名称',
    `win_probability` DECIMAL(5, 2) NOT NULL DEFAULT 0.00 COMMENT '当前阶段赢率（百分比：0-100）',
    `expected_amount` DECIMAL(12, 2) NOT NULL DEFAULT 0.00 COMMENT '预计金额（商机总金额）',
    `forecasted_amount` DECIMAL(12, 2) NOT NULL DEFAULT 0.00 COMMENT '预期成交金额（预计金额 × 赢率，自动计算）',
    `expected_deal_date` DATE DEFAULT NULL COMMENT '预计成交日期',
    `assign_user_id` BIGINT DEFAULT NULL COMMENT '分配负责人ID',
    `assign_user_name` VARCHAR(50) DEFAULT NULL COMMENT '分配负责人姓名',
    `assign_time` DATETIME DEFAULT NULL COMMENT '分配时间',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '商机状态：0-进行中 1-已赢单 2-已输单',
    `source_name` VARCHAR(100) DEFAULT NULL COMMENT '来源（如：线索转化、直接创建）',
    `industry` VARCHAR(100) DEFAULT NULL COMMENT '所属行业',
    `description` TEXT DEFAULT NULL COMMENT '商机描述/需求描述',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `tags` VARCHAR(500) DEFAULT NULL COMMENT '标签（多个用逗号分隔）',
    `next_follow_time` DATETIME DEFAULT NULL COMMENT '下次跟进时间',
    `follow_count` BIGINT NOT NULL DEFAULT 0 COMMENT '跟进次数',
    `last_follow_time` DATETIME DEFAULT NULL COMMENT '最后跟进时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0-未删除 1-已删除',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `ext1` VARCHAR(255) DEFAULT NULL,
    `ext2` VARCHAR(255) DEFAULT NULL,
    `ext3` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_opportunity_no` (`opportunity_no`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_customer_name` (`customer_name`),
    KEY `idx_lead_id` (`lead_id`),
    KEY `idx_stage_id` (`stage_id`),
    KEY `idx_stage_code` (`stage_code`),
    KEY `idx_assign_user_id` (`assign_user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_industry` (`industry`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商机主表';

-- 3. 初始化预设销售阶段数据
-- 标准销售漏斗阶段：初步接洽(10%) -> 需求确认(20%) -> 方案报价(40%) -> 商务谈判(70%) -> 赢单(100%)/输单(0%)
INSERT IGNORE INTO `crm_sales_stage` (`stage_code`, `stage_name`, `sort_order`, `win_probability`, `description`, `is_system`, `is_enabled`, `is_closed`, `close_type`) VALUES
('initial_contact', '初步接洽', 1, 10.00, '初次接触客户，了解基本信息和需求意向', 1, 1, 0, 0),
('requirement_confirmed', '需求确认', 2, 20.00, '深入沟通，确认客户具体需求和痛点', 1, 1, 0, 0),
('solution_quotation', '方案报价', 3, 40.00, '提供解决方案和报价，进行商务沟通', 1, 1, 0, 0),
('business_negotiation', '商务谈判', 4, 70.00, '商务条款谈判、合同细节确认', 1, 1, 0, 0),
('won', '赢单', 5, 100.00, '成功签约，赢取商机', 1, 1, 1, 1),
('lost', '输单', 6, 0.00, '商机失败，未签约', 1, 1, 1, 2);
