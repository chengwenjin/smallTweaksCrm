-- ============================================
-- 销售过程与商机管理模块菜单数据
-- 销售过程与商机管理 -> 标准化销售漏斗 -> 阶段管理
-- 销售过程与商机管理 -> 标准化销售漏斗 -> 商机管理
-- ============================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- 1. 一级菜单：销售过程与商机管理
-- 检查是否已存在，如果不存在则插入
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`)
SELECT 0, '销售过程与商机管理', 1, NULL, '/sales', 'Layout', 'TrendCharts', 80, 1, 1, 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_name` = '销售过程与商机管理' AND `parent_id` = 0);

-- 获取刚插入的一级菜单ID
SET @sales_parent_id = (SELECT id FROM `sys_menu` WHERE `menu_name` = '销售过程与商机管理' AND `parent_id` = 0 LIMIT 1);

-- 2. 二级菜单：标准化销售漏斗
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`)
SELECT @sales_parent_id, '标准化销售漏斗', 1, NULL, 'sales-funnel', NULL, 'DataAnalysis', 1, 1, 1, 0
FROM DUAL
WHERE @sales_parent_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_name` = '标准化销售漏斗' AND `parent_id` = @sales_parent_id);

-- 获取刚插入的二级菜单ID
SET @sales_funnel_parent_id = (SELECT id FROM `sys_menu` WHERE `menu_name` = '标准化销售漏斗' AND `parent_id` = @sales_parent_id LIMIT 1);

-- 3. 三级菜单：阶段管理
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`)
SELECT @sales_funnel_parent_id, '阶段管理', 2, 'crm:stage:list', 'stage', 'crm/sales-funnel/stage', 'Setting', 1, 1, 1, 0
FROM DUAL
WHERE @sales_funnel_parent_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_name` = '阶段管理' AND `parent_id` = @sales_funnel_parent_id);

-- 获取刚插入的阶段管理菜单ID
SET @stage_menu_id = (SELECT id FROM `sys_menu` WHERE `menu_name` = '阶段管理' AND `parent_id` = @sales_funnel_parent_id LIMIT 1);

-- 4. 阶段管理按钮权限
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`)
SELECT @stage_menu_id, '新增阶段', 3, 'crm:stage:add', 1, 1, 0
FROM DUAL WHERE @stage_menu_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `parent_id` = @stage_menu_id AND `permission_key` = 'crm:stage:add');

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`)
SELECT @stage_menu_id, '编辑阶段', 3, 'crm:stage:edit', 2, 1, 0
FROM DUAL WHERE @stage_menu_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `parent_id` = @stage_menu_id AND `permission_key` = 'crm:stage:edit');

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`)
SELECT @stage_menu_id, '删除阶段', 3, 'crm:stage:delete', 3, 1, 0
FROM DUAL WHERE @stage_menu_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `parent_id` = @stage_menu_id AND `permission_key` = 'crm:stage:delete');

-- 5. 三级菜单：商机管理
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`)
SELECT @sales_funnel_parent_id, '商机管理', 2, 'crm:opportunity:list', 'opportunity', 'crm/sales-funnel/opportunity', 'Document', 2, 1, 1, 0
FROM DUAL
WHERE @sales_funnel_parent_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_name` = '商机管理' AND `parent_id` = @sales_funnel_parent_id);

-- 获取刚插入的商机管理菜单ID
SET @opportunity_menu_id = (SELECT id FROM `sys_menu` WHERE `menu_name` = '商机管理' AND `parent_id` = @sales_funnel_parent_id LIMIT 1);

-- 6. 商机管理按钮权限
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`)
SELECT @opportunity_menu_id, '新增商机', 3, 'crm:opportunity:add', 1, 1, 0
FROM DUAL WHERE @opportunity_menu_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `parent_id` = @opportunity_menu_id AND `permission_key` = 'crm:opportunity:add');

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`)
SELECT @opportunity_menu_id, '编辑商机', 3, 'crm:opportunity:edit', 2, 1, 0
FROM DUAL WHERE @opportunity_menu_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `parent_id` = @opportunity_menu_id AND `permission_key` = 'crm:opportunity:edit');

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`)
SELECT @opportunity_menu_id, '删除商机', 3, 'crm:opportunity:delete', 3, 1, 0
FROM DUAL WHERE @opportunity_menu_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `parent_id` = @opportunity_menu_id AND `permission_key` = 'crm:opportunity:delete');

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`)
SELECT @opportunity_menu_id, '转移阶段', 3, 'crm:opportunity:move', 4, 1, 0
FROM DUAL WHERE @opportunity_menu_id IS NOT NULL AND NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `parent_id` = @opportunity_menu_id AND `permission_key` = 'crm:opportunity:move');

-- 7. 为管理员角色分配菜单权限
-- 获取管理员角色ID（假设为1）
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `sys_menu` WHERE `permission_key` LIKE 'crm:stage:%' OR `permission_key` LIKE 'crm:opportunity:%'
ON DUPLICATE KEY UPDATE `role_id` = VALUES(`role_id`);

-- 8. API权限配置
INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`)
SELECT 'crm:stage:list', '查询销售阶段列表', '销售阶段管理', 'GET', '/api/crm/sales-stages', '分页查询销售阶段列表', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_api_permission` WHERE `permission_key` = 'crm:stage:list');

INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`)
SELECT 'crm:stage:add', '新增销售阶段', '销售阶段管理', 'POST', '/api/crm/sales-stages', '创建销售阶段', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_api_permission` WHERE `permission_key` = 'crm:stage:add');

INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`)
SELECT 'crm:stage:edit', '编辑销售阶段', '销售阶段管理', 'PUT', '/api/crm/sales-stages', '更新销售阶段', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_api_permission` WHERE `permission_key` = 'crm:stage:edit');

INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`)
SELECT 'crm:stage:delete', '删除销售阶段', '销售阶段管理', 'DELETE', '/api/crm/sales-stages/{id}', '删除销售阶段', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_api_permission` WHERE `permission_key` = 'crm:stage:delete');

INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`)
SELECT 'crm:opportunity:list', '查询商机列表', '商机管理', 'GET', '/api/crm/opportunities', '分页查询商机列表', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_api_permission` WHERE `permission_key` = 'crm:opportunity:list');

INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`)
SELECT 'crm:opportunity:add', '新增商机', '商机管理', 'POST', '/api/crm/opportunities', '创建商机', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_api_permission` WHERE `permission_key` = 'crm:opportunity:add');

INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`)
SELECT 'crm:opportunity:edit', '编辑商机', '商机管理', 'PUT', '/api/crm/opportunities/{id}', '更新商机', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_api_permission` WHERE `permission_key` = 'crm:opportunity:edit');

INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`)
SELECT 'crm:opportunity:delete', '删除商机', '商机管理', 'DELETE', '/api/crm/opportunities/{id}', '删除商机', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_api_permission` WHERE `permission_key` = 'crm:opportunity:delete');

INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`)
SELECT 'crm:opportunity:move', '转移商机阶段', '商机管理', 'PUT', '/api/crm/opportunities/{id}/move-stage', '转移商机到指定阶段', 1
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `sys_api_permission` WHERE `permission_key` = 'crm:opportunity:move');

-- 9. 为管理员角色分配API权限
INSERT INTO `sys_role_api` (`role_id`, `api_id`)
SELECT 1, id FROM `sys_api_permission` WHERE `permission_key` LIKE 'crm:stage:%' OR `permission_key` LIKE 'crm:opportunity:%'
ON DUPLICATE KEY UPDATE `role_id` = VALUES(`role_id`);
