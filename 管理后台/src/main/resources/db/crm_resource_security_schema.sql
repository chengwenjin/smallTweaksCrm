SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `crm_transfer_detail`;
DROP TABLE IF EXISTS `crm_transfer_record`;
DROP TABLE IF EXISTS `crm_public_sea_rule`;
DROP TABLE IF EXISTS `crm_private_sea_config`;

CREATE TABLE IF NOT EXISTS `crm_private_sea_config` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `config_type` tinyint NOT NULL DEFAULT 1 COMMENT '配置类型：1-全局配置, 2-角色配置, 3-用户配置',
    `role_id` bigint DEFAULT NULL COMMENT '角色ID',
    `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
    `user_id` bigint DEFAULT NULL COMMENT '用户ID',
    `user_name` varchar(100) DEFAULT NULL COMMENT '用户姓名',
    `max_customer_count` int NOT NULL DEFAULT 50 COMMENT '客户数量上限',
    `max_lead_count` int NOT NULL DEFAULT 100 COMMENT '线索数量上限',
    `auto_recycle_days` int NOT NULL DEFAULT 30 COMMENT '自动回收天数（超过该天数未跟进自动回收到公海）',
    `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用, 1-启用',
    `description` varchar(500) DEFAULT NULL COMMENT '配置说明',
    `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
    `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除, 1-已删除',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `ext1` varchar(500) DEFAULT NULL COMMENT '扩展字段1',
    `ext2` varchar(500) DEFAULT NULL COMMENT '扩展字段2',
    `ext3` varchar(500) DEFAULT NULL COMMENT '扩展字段3',
    PRIMARY KEY (`id`),
    KEY `idx_config_type` (`config_type`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='私海配置表';

CREATE TABLE IF NOT EXISTS `crm_public_sea_rule` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
    `rule_type` tinyint NOT NULL DEFAULT 1 COMMENT '规则类型：1-先抢先得, 2-定期轮换',
    `rotate_days` int DEFAULT 7 COMMENT '轮换天数（定期轮换时使用，每隔多少天轮换一次）',
    `claim_limit_per_day` int NOT NULL DEFAULT 10 COMMENT '每日认领上限',
    `claim_limit_per_week` int NOT NULL DEFAULT 50 COMMENT '每周认领上限',
    `auto_recycle_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用自动回收：0-禁用, 1-启用',
    `auto_recycle_days` int NOT NULL DEFAULT 30 COMMENT '自动回收天数（超过该天数未跟进自动回收到公海）',
    `is_enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用, 1-启用',
    `description` varchar(500) DEFAULT NULL COMMENT '规则说明',
    `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序',
    `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除, 1-已删除',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `ext1` varchar(500) DEFAULT NULL COMMENT '扩展字段1',
    `ext2` varchar(500) DEFAULT NULL COMMENT '扩展字段2',
    `ext3` varchar(500) DEFAULT NULL COMMENT '扩展字段3',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公海规则配置表';

CREATE TABLE IF NOT EXISTS `crm_transfer_record` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `transfer_no` varchar(50) NOT NULL COMMENT '转移单号',
    `transfer_type` tinyint NOT NULL DEFAULT 1 COMMENT '转移类型：1-离职回收, 2-手动转移, 3-自动回收, 4-调岗转移',
    `transfer_type_name` varchar(50) DEFAULT NULL COMMENT '转移类型名称',
    `from_user_id` bigint NOT NULL COMMENT '原负责人ID',
    `from_user_name` varchar(100) NOT NULL COMMENT '原负责人姓名',
    `from_department_id` bigint DEFAULT NULL COMMENT '原部门ID',
    `from_department_name` varchar(100) DEFAULT NULL COMMENT '原部门名称',
    `to_user_id` bigint DEFAULT NULL COMMENT '接手人ID',
    `to_user_name` varchar(100) DEFAULT NULL COMMENT '接手人姓名',
    `to_department_id` bigint DEFAULT NULL COMMENT '目标部门ID',
    `to_department_name` varchar(100) DEFAULT NULL COMMENT '目标部门名称',
    `transfer_method` tinyint NOT NULL DEFAULT 1 COMMENT '转移方式：1-全部转移, 2-部分转移, 3-移入公海',
    `customer_count` int NOT NULL DEFAULT 0 COMMENT '转移客户数量',
    `lead_count` int NOT NULL DEFAULT 0 COMMENT '转移线索数量',
    `contract_count` int NOT NULL DEFAULT 0 COMMENT '转移合同数量',
    `follow_count` int NOT NULL DEFAULT 0 COMMENT '转移跟进记录数量',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-待确认, 1-已完成, 2-已取消',
    `reason` varchar(500) DEFAULT NULL COMMENT '转移原因',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `operator_id` bigint NOT NULL COMMENT '操作人ID',
    `operator_name` varchar(100) NOT NULL COMMENT '操作人姓名',
    `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除, 1-已删除',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `ext1` varchar(500) DEFAULT NULL COMMENT '扩展字段1',
    `ext2` varchar(500) DEFAULT NULL COMMENT '扩展字段2',
    `ext3` varchar(500) DEFAULT NULL COMMENT '扩展字段3',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_transfer_no` (`transfer_no`),
    KEY `idx_from_user_id` (`from_user_id`),
    KEY `idx_to_user_id` (`to_user_id`),
    KEY `idx_transfer_type` (`transfer_type`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源转移记录表';

CREATE TABLE IF NOT EXISTS `crm_transfer_detail` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `record_id` bigint NOT NULL COMMENT '转移记录ID',
    `transfer_no` varchar(50) NOT NULL COMMENT '转移单号',
    `resource_type` tinyint NOT NULL COMMENT '资源类型：1-客户, 2-线索, 3-合同, 4-跟进记录',
    `resource_type_name` varchar(50) DEFAULT NULL COMMENT '资源类型名称',
    `resource_id` bigint NOT NULL COMMENT '资源ID',
    `resource_no` varchar(50) DEFAULT NULL COMMENT '资源编号',
    `resource_name` varchar(200) NOT NULL COMMENT '资源名称',
    `from_user_id` bigint NOT NULL COMMENT '原负责人ID',
    `from_user_name` varchar(100) NOT NULL COMMENT '原负责人姓名',
    `to_user_id` bigint DEFAULT NULL COMMENT '接手人ID',
    `to_user_name` varchar(100) DEFAULT NULL COMMENT '接手人姓名',
    `transfer_status` tinyint NOT NULL DEFAULT 1 COMMENT '转移状态：1-已转移, 2-已回收',
    `transfer_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '转移时间',
    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
    `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除, 1-已删除',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_record_id` (`record_id`),
    KEY `idx_transfer_no` (`transfer_no`),
    KEY `idx_resource_type` (`resource_type`),
    KEY `idx_resource_id` (`resource_id`),
    KEY `idx_from_user_id` (`from_user_id`),
    KEY `idx_to_user_id` (`to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源转移明细表';

INSERT INTO `crm_private_sea_config` (`config_type`, `role_id`, `role_name`, `max_customer_count`, `max_lead_count`, `auto_recycle_days`, `is_enabled`, `description`, `sort_order`) VALUES
(1, NULL, NULL, 100, 200, 30, 1, '全局默认配置：客户上限100，线索上限200，30天未跟进自动回收', 1),
(2, 1, '超级管理员', 500, 1000, 60, 1, '超级管理员配置：客户上限500，线索上限1000，60天未跟进自动回收', 2),
(2, 2, '销售经理', 200, 400, 30, 1, '销售经理配置：客户上限200，线索上限400，30天未跟进自动回收', 3),
(2, 3, '销售代表', 50, 100, 15, 1, '销售代表配置：客户上限50，线索上限100，15天未跟进自动回收', 4);

INSERT INTO `crm_public_sea_rule` (`rule_name`, `rule_type`, `rotate_days`, `claim_limit_per_day`, `claim_limit_per_week`, `auto_recycle_enabled`, `auto_recycle_days`, `is_enabled`, `description`, `sort_order`) VALUES
('默认公海规则', 1, 7, 10, 50, 1, 30, 1, '先抢先得模式：每日最多认领10条，每周最多50条，30天未跟进自动回收到公海', 1),
('定期轮换规则', 2, 7, 5, 30, 1, 15, 0, '定期轮换模式：每7天轮换一次，每日最多认领5条，每周最多30条，15天未跟进自动回收', 2);
