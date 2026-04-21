-- ============================================
-- 线索管理模块菜单数据
-- 客户与线索管理 -> 线索全生命周期 -> 多渠道录入
-- ============================================

-- 1. 一级菜单：客户与线索管理
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(0, '客户与线索管理', 1, NULL, '/crm', 'Layout', 'User', 70, 1, 1, 0);

-- 获取刚插入的一级菜单ID
SET @crm_parent_id = LAST_INSERT_ID();

-- 2. 二级菜单：线索全生命周期
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(@crm_parent_id, '线索全生命周期', 1, NULL, 'lead-lifecycle', NULL, 'Connection', 1, 1, 1, 0);

-- 获取刚插入的二级菜单ID
SET @lead_lifecycle_parent_id = LAST_INSERT_ID();

-- 3. 三级菜单：多渠道录入（线索管理）
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(@lead_lifecycle_parent_id, '多渠道录入', 2, 'crm:lead:list', 'lead', 'crm/lead/index', 'Plus', 1, 1, 1, 0);

-- 获取刚插入的三级菜单ID
SET @lead_menu_id = LAST_INSERT_ID();

-- 4. 按钮权限
INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`) VALUES
(@lead_menu_id, '新增线索', 3, 'crm:lead:add', 1, 1, 0),
(@lead_menu_id, '编辑线索', 3, 'crm:lead:edit', 2, 1, 0),
(@lead_menu_id, '删除线索', 3, 'crm:lead:delete', 3, 1, 0),
(@lead_menu_id, '导入线索', 3, 'crm:lead:import', 4, 1, 0),
(@lead_menu_id, '分配线索', 3, 'crm:lead:assign', 5, 1, 0);

-- 5. 为管理员角色分配菜单权限
-- 获取管理员角色ID（假设为1）
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
