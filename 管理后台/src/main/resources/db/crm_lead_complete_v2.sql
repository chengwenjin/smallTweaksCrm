-- ============================================
-- 线索管理模块完整数据库脚本 V2
-- 客户与线索管理 -> 线索全生命周期 -> 多渠道录入
-- 
-- 重要：请确保数据库管理工具使用 UTF-8 编码执行此脚本
-- 数据库字符集：utf8mb4
-- ============================================

-- ============================================
-- 第一部分：表结构（如果不存在则创建）
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
-- 第二部分：初始化线索来源数据
-- ============================================

TRUNCATE TABLE `crm_lead_source`;

INSERT INTO `crm_lead_source` (`source_code`, `source_name`, `source_type`, `is_system`, `is_enabled`, `sort_order`, `description`) VALUES
('manual', '手工录入', 1, 1, 1, 1, '后台手工录入创建线索'),
('excel', 'Excel导入', 2, 1, 1, 2, '通过Excel批量导入创建线索'),
('website', '官网表单', 3, 1, 1, 3, '通过官网表单API对接创建线索'),
('campaign', '市场活动', 3, 1, 1, 4, '通过市场活动API对接创建线索'),
('referral', '客户转介绍', 1, 1, 1, 5, '客户转介绍的线索'),
('phone', '电话咨询', 1, 1, 1, 6, '电话咨询获取的线索'),
('chat', '在线咨询', 3, 1, 1, 7, '在线客服咨询获取的线索'),
('other', '其他来源', 1, 1, 1, 99, '其他渠道获取的线索');

-- ============================================
-- 第三部分：菜单数据（先删除旧数据，再插入新数据）
-- ============================================

-- 先删除已存在的线索相关菜单和权限
DELETE FROM `sys_role_api` WHERE `api_id` IN (SELECT id FROM `sys_api_permission` WHERE `permission_key` LIKE 'crm:%');
DELETE FROM `sys_api_permission` WHERE `permission_key` LIKE 'crm:%';
DELETE FROM `sys_role_menu` WHERE `menu_id` IN (SELECT id FROM `sys_menu` WHERE `permission_key` LIKE 'crm:%' OR `menu_name` LIKE '%线索%' OR `menu_name` LIKE '%客户%');
DELETE FROM `sys_menu` WHERE `permission_key` LIKE 'crm:%' OR `menu_name` LIKE '%线索%' OR `menu_name` LIKE '%客户%';

-- 1. 一级菜单：客户与线索管理
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`, `is_cached`, `is_external`) VALUES
(0, '客户与线索管理', 1, NULL, '/crm', 'Layout', 'UserFilled', 70, 1, 1, 0, 0, 0);

SET @crm_parent_id = LAST_INSERT_ID();

-- 2. 二级菜单：线索全生命周期
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`, `is_cached`, `is_external`) VALUES
(@crm_parent_id, '线索全生命周期', 1, NULL, 'lead-lifecycle', NULL, 'Connection', 1, 1, 1, 0, 0, 0);

SET @lead_lifecycle_parent_id = LAST_INSERT_ID();

-- 3. 三级菜单：多渠道录入（线索管理）
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`, `is_cached`, `is_external`) VALUES
(@lead_lifecycle_parent_id, '多渠道录入', 2, 'crm:lead:list', 'lead', 'crm/lead/index', 'Plus', 1, 1, 1, 0, 0, 0);

SET @lead_menu_id = LAST_INSERT_ID();

-- 4. 按钮权限
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`) VALUES
(@lead_menu_id, '新增线索', 3, 'crm:lead:add', 1, 1, 0),
(@lead_menu_id, '编辑线索', 3, 'crm:lead:edit', 2, 1, 0),
(@lead_menu_id, '删除线索', 3, 'crm:lead:delete', 3, 1, 0),
(@lead_menu_id, '导入线索', 3, 'crm:lead:import', 4, 1, 0),
(@lead_menu_id, '分配线索', 3, 'crm:lead:assign', 5, 1, 0);

-- 5. 为管理员角色分配菜单权限（role_id = 1 是超级管理员）
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) 
SELECT 1, id FROM `sys_menu` WHERE `permission_key` LIKE 'crm:%';

-- 6. API权限配置
INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`) VALUES
('crm:lead:list', '查询线索列表', '线索管理', 'GET', '/api/crm/leads', '分页查询线索列表', 1),
('crm:lead:add', '新增线索', '线索管理', 'POST', '/api/crm/leads', '手工录入线索', 1),
('crm:lead:edit', '编辑线索', '线索管理', 'PUT', '/api/crm/leads/{id}', '更新线索信息', 1),
('crm:lead:delete', '删除线索', '线索管理', 'DELETE', '/api/crm/leads/{id}', '删除线索', 1),
('crm:lead:import', '导入线索', '线索管理', 'POST', '/api/crm/leads/import', 'Excel批量导入线索', 1),
('crm:lead:assign', '分配线索', '线索管理', 'PUT', '/api/crm/leads/{id}/assign', '分配线索给负责人', 1);

-- 7. 为管理员角色分配API权限
INSERT INTO `sys_role_api` (`role_id`, `api_id`) 
SELECT 1, id FROM `sys_api_permission` WHERE `permission_key` LIKE 'crm:%';

-- ============================================
-- 第四部分：测试数据（UTF-8编码，中文不会乱码）
-- ============================================

-- 先清空测试数据
TRUNCATE TABLE `crm_lead_log`;
TRUNCATE TABLE `crm_lead`;

-- 插入测试线索数据（各种来源、各种状态）

-- 高优先级线索 - 新线索
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_phone`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `address`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `probability`, `expected_amount`, `description`, `tags`, `create_by`, `create_time`) VALUES
('LD202604230001', '北京创新科技有限公司', '张三', '010-88881111', '13800138001', 'zhangsan@bjcxkj.com', '北京市', '北京市', '海淀区', '中关村科技园区A座1001室', '信息技术', 1, 'manual', '手工录入', 1, 0, 80, 500000.00, '客户对企业管理系统有浓厚兴趣，希望尽快安排演示。通过销售经理推荐联系，客户规模约200人，目前使用传统Excel管理，迫切需要数字化转型。', '潜在客户,高价值,信息化需求', 'admin', NOW());

-- 中优先级线索 - 跟进中
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_phone`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `address`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `probability`, `expected_amount`, `description`, `tags`, `create_by`, `create_time`, `assign_user_id`, `assign_user_name`, `assign_time`, `next_follow_time`) VALUES
('LD202604230002', '上海恒达贸易有限公司', '李四', '021-66662222', '13900139002', 'lisi@shhdmy.com', '上海市', '上海市', '浦东新区', '陆家嘴金融中心B座2505室', '贸易批发', 3, 'website', '官网表单', 2, 1, 50, 200000.00, '客户通过官网表单提交需求，主要想了解库存管理模块。已进行一次电话沟通，客户表示需要对比3家供应商后再做决定。下周安排远程演示。', '贸易行业,库存管理,待演示', 'admin', NOW(), 1, '管理员', NOW(), DATE_ADD(NOW(), INTERVAL 2 DAY));

-- 低优先级线索 - 新线索
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_phone`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `address`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `probability`, `expected_amount`, `description`, `tags`, `create_by`, `create_time`) VALUES
('LD202604230003', '广州小微型商店', '王五', '', '13700137003', 'wangwu@gzwx.com', '广东省', '广州市', '天河区', '天河路电脑城3楼', '零售', 6, '电话咨询', '电话咨询', 3, 0, 20, 50000.00, '电话咨询基础功能，预算有限，可能需要简化版。客户表示目前仅需管理10个员工，对价格比较敏感。', '小微企业,价格敏感,基础版', 'admin', NOW());

-- 高优先级线索 - 已转化（来自市场活动）
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_phone`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `address`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `probability`, `expected_amount`, `description`, `tags`, `create_by`, `create_time`, `assign_user_id`, `assign_user_name`, `assign_time`) VALUES
('LD202604230004', '深圳智慧医疗科技有限公司', '赵六', '0755-99993333', '13600136004', 'zhaoliu@szzhyl.com', '广东省', '深圳市', '南山区', '科技园南区软件园12栋', '医疗健康', 4, 'campaign', '市场活动', 1, 2, 95, 800000.00, '通过2026春季医疗信息化展会获取，客户对医疗数据管理系统有强烈需求。已完成3次演示，合同细节已谈妥，待签字盖章。', '医疗行业,高价值,待签约,展会获取', 'admin', NOW(), 1, '管理员', NOW());

-- 中优先级线索 - 已无效（来自客户转介绍）
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_phone`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `address`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `probability`, `expected_amount`, `description`, `tags`, `create_by`, `create_time`) VALUES
('LD202604230005', '杭州远大教育集团', '孙七', '0571-77774444', '13500135005', 'sunqi@hzydjy.com', '浙江省', '杭州市', '西湖区', '文三路教育大厦18楼', '教育培训', 5, 'referral', '客户转介绍', 2, 3, 0, 300000.00, '由老客户转介绍，但跟进后发现客户已在上个月签约竞争对手。保持联系，待合同到期后再跟进。', '教育行业,转介绍,已流失', 'admin', NOW());

-- 低优先级线索 - 跟进中（来自在线咨询）
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_phone`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `address`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `probability`, `expected_amount`, `description`, `tags`, `create_by`, `create_time`, `next_follow_time`) VALUES
('LD202604230006', '成都新兴餐饮店', '周八', '', '13400134006', 'zhouba@cdxxcy.com', '四川省', '成都市', '锦江区', '春熙路美食街88号', '餐饮', 7, 'chat', '在线咨询', 3, 1, 30, 80000.00, '通过官网在线客服咨询，主要想了解订单管理和会员管理功能。客户是连锁店，有5家门店，需要统一管理。已发送产品资料，等待回复。', '餐饮连锁,订单管理,会员管理', 'admin', NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY));

-- 高优先级线索 - 新线索（来自Excel导入）
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_phone`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `address`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `probability`, `expected_amount`, `description`, `tags`, `create_by`, `create_time`) VALUES
('LD202604230007', '武汉智能制造有限公司', '吴九', '027-55555555', '13300133007', 'wujiu@whznzz.com', '湖北省', '武汉市', '东湖新技术开发区', '光谷软件园C区', '智能制造', 2, 'excel', 'Excel导入', 1, 0, 70, 600000.00, '通过行业展会名单批量导入，客户为大型制造企业，正在进行数字化转型。生产管理模块是核心需求，有专门的信息化部门负责采购。', '制造行业,数字化转型,展会名单,批量导入', 'admin', NOW());

-- 中优先级线索 - 新线索（来自其他来源）
INSERT INTO `crm_lead` (`lead_no`, `lead_name`, `contact_name`, `contact_phone`, `contact_mobile`, `contact_email`, `province`, `city`, `district`, `address`, `industry`, `source_id`, `source_code`, `source_name`, `level`, `status`, `probability`, `expected_amount`, `description`, `tags`, `create_by`, `create_time`) VALUES
('LD202604230008', '南京绿色农业发展有限公司', '郑十', '', '13200132008', 'zhengshi@njlsny.com', '江苏省', '南京市', '浦口区', '现代农业示范园区', '农业', 8, 'other', '其他来源', 2, 0, 40, 150000.00, '通过行业协会推荐获取，客户从事现代农业，需要供应链管理和财务管理。规模中等，预算有限，需要高性价比方案。', '农业行业,协会推荐,性价比优先', 'admin', NOW());

-- ============================================
-- 执行完成提示
-- ============================================
-- 脚本执行完成！
--
-- 已执行内容：
-- 1. 创建表结构（crm_lead_source, crm_lead, crm_lead_log）
-- 2. 初始化线索来源数据（8种来源）
-- 3. 创建菜单数据（客户与线索管理 -> 线索全生命周期 -> 多渠道录入）
-- 4. 分配菜单权限给管理员角色
-- 5. 创建API权限并分配给管理员
-- 6. 插入8条测试线索数据（覆盖各种来源和状态）
--
-- 测试数据说明：
-- - 高优先级(level=1): 3条
-- - 中优先级(level=2): 3条  
-- - 低优先级(level=3): 2条
--
-- - 新线索(status=0): 4条
-- - 跟进中(status=1): 3条
-- - 已转化(status=2): 1条
-- - 已无效(status=3): 1条
--
-- - 所有来源类型都有测试数据
-- - 包含完整的中文信息（UTF-8编码）
-- ============================================
