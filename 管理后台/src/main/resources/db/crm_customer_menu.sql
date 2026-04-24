-- ============================================================
-- 客户360°全景档案 - 菜单配置脚本
-- 客户与线索管理 -> 客户360°全景档案 -> 基础信息库
-- ============================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- 1. 先查找客户与线索管理的父菜单ID
SET @crm_parent_id = (SELECT id FROM `sys_menu` WHERE `menu_name` = '客户与线索管理' AND `parent_id` = 0);

-- 如果不存在，创建一级菜单：客户与线索管理
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`, `is_cached`, `is_external`)
SELECT 0, '客户与线索管理', 1, NULL, '/crm', 'Layout', 'UserFilled', 70, 1, 1, 0, 0, 0
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_name` = '客户与线索管理' AND `parent_id` = 0);

SET @crm_parent_id = COALESCE(@crm_parent_id, LAST_INSERT_ID());

-- 2. 创建二级菜单：客户360°全景档案
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`, `is_cached`, `is_external`)
SELECT @crm_parent_id, '客户360°全景档案', 1, NULL, 'customer-360', NULL, 'DataAnalysis', 2, 1, 1, 0, 0, 0
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_name` = '客户360°全景档案' AND `parent_id` = @crm_parent_id);

SET @customer_360_parent_id = COALESCE((SELECT id FROM `sys_menu` WHERE `menu_name` = '客户360°全景档案' AND `parent_id` = @crm_parent_id), LAST_INSERT_ID());

-- 3. 创建三级菜单：基础信息库（客户管理）
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`, `is_cached`, `is_external`)
SELECT @customer_360_parent_id, '基础信息库', 2, 'crm:customer:list', 'customer', 'crm/customer/index', 'Document', 1, 1, 1, 0, 0, 0
WHERE NOT EXISTS (SELECT 1 FROM `sys_menu` WHERE `menu_name` = '基础信息库' AND `parent_id` = @customer_360_parent_id);

SET @customer_menu_id = COALESCE((SELECT id FROM `sys_menu` WHERE `menu_name` = '基础信息库' AND `parent_id` = @customer_360_parent_id), LAST_INSERT_ID());

-- 4. 创建按钮权限
-- 先删除已有的按钮权限，避免重复
DELETE FROM `sys_menu` WHERE `parent_id` = @customer_menu_id AND `menu_type` = 3;

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`) VALUES
(@customer_menu_id, '新增客户', 3, 'crm:customer:add', 1, 1, 0),
(@customer_menu_id, '编辑客户', 3, 'crm:customer:edit', 2, 1, 0),
(@customer_menu_id, '删除客户', 3, 'crm:customer:delete', 3, 1, 0),
(@customer_menu_id, '查看客户', 3, 'crm:customer:view', 4, 1, 0),
(@customer_menu_id, '管理联系人', 3, 'crm:customer:contact', 5, 1, 0),
(@customer_menu_id, '管理跟进', 3, 'crm:customer:follow', 6, 1, 0);

-- 5. 为管理员角色分配菜单权限（role_id = 1 是超级管理员）
-- 先删除已有的关联，避免重复
DELETE FROM `sys_role_menu` WHERE `role_id` = 1 AND `menu_id` IN (
    SELECT id FROM `sys_menu` WHERE `menu_name` = '客户360°全景档案' 
    OR `menu_name` = '基础信息库'
    OR `permission_key` LIKE 'crm:customer:%'
);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) 
SELECT 1, id FROM `sys_menu` WHERE `permission_key` LIKE 'crm:customer:%';

-- 6. API权限配置
-- 先删除已有的API权限，避免重复
DELETE FROM `sys_api_permission` WHERE `permission_key` LIKE 'crm:customer:%';

INSERT INTO `sys_api_permission` (`permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`) VALUES
('crm:customer:list', '查询客户列表', '客户管理', 'GET', '/api/crm/customers', '分页查询客户列表', 1),
('crm:customer:view', '查看客户详情', '客户管理', 'GET', '/api/crm/customers/{id}', '查询客户详情（全景视图）', 1),
('crm:customer:add', '新增客户', '客户管理', 'POST', '/api/crm/customers', '创建新客户', 1),
('crm:customer:edit', '编辑客户', '客户管理', 'PUT', '/api/crm/customers/{id}', '更新客户信息', 1),
('crm:customer:delete', '删除客户', '客户管理', 'DELETE', '/api/crm/customers/{id}', '删除客户', 1),
('crm:customer:contact', '管理联系人', '客户管理', 'ALL', '/api/crm/customers/{id}/contacts', '客户联系人管理', 1),
('crm:customer:follow', '管理跟进', '客户管理', 'ALL', '/api/crm/customers/{id}/follows', '客户跟进记录管理', 1);

-- 7. 为管理员角色分配API权限
-- 先删除已有的关联，避免重复
DELETE FROM `sys_role_api` WHERE `role_id` = 1 AND `api_id` IN (
    SELECT id FROM `sys_api_permission` WHERE `permission_key` LIKE 'crm:customer:%'
);

INSERT INTO `sys_role_api` (`role_id`, `api_id`) 
SELECT 1, id FROM `sys_api_permission` WHERE `permission_key` LIKE 'crm:customer:%';

-- ============================================
-- 执行完成提示
-- ============================================
SELECT '客户360°全景档案菜单配置完成' AS message;
