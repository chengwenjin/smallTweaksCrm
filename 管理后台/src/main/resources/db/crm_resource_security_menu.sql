SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

SET @crm_parent_id = (SELECT id FROM sys_menu WHERE path = '/crm' AND menu_type = 2 LIMIT 1);

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `sort_order`, `path`, `component`, `is_visible`, `is_cache`, `is_external`, `icon`, `create_time`, `update_time`, `is_deleted`)
VALUES (@crm_parent_id, '资源安全与流转', 2, 2, '/crm/resource-security', NULL, 1, 0, 0, 'Lock', NOW(), NOW(), 0);

SET @resource_parent_id = LAST_INSERT_ID();

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `sort_order`, `path`, `component`, `is_visible`, `is_cache`, `is_external`, `icon`, `create_time`, `update_time`, `is_deleted`)
VALUES 
(@resource_parent_id, '私海配置', 3, 1, '/crm/resource-security/private-sea', 'crm/resource-security/private-sea', 1, 0, 0, 'User', NOW(), NOW(), 0),
(@resource_parent_id, '公海规则', 3, 2, '/crm/resource-security/public-sea-rule', 'crm/resource-security/public-sea-rule', 1, 0, 0, 'Share', NOW(), NOW(), 0),
(@resource_parent_id, '离职一键回收', 3, 3, '/crm/resource-security/transfer', 'crm/resource-security/transfer', 1, 0, 0, 'Switch', NOW(), NOW(), 0);

SET @private_sea_menu_id = (SELECT id FROM sys_menu WHERE path = '/crm/resource-security/private-sea' AND menu_type = 3 LIMIT 1);
SET @public_sea_rule_menu_id = (SELECT id FROM sys_menu WHERE path = '/crm/resource-security/public-sea-rule' AND menu_type = 3 LIMIT 1);
SET @transfer_menu_id = (SELECT id FROM sys_menu WHERE path = '/crm/resource-security/transfer' AND menu_type = 3 LIMIT 1);

INSERT INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `sort_order`, `path`, `component`, `is_visible`, `is_cache`, `is_external`, `icon`, `create_time`, `update_time`, `is_deleted`)
VALUES 
(@private_sea_menu_id, '新增私海配置', 4, 1, '', '', 1, 0, 0, 'Plus', NOW(), NOW(), 0),
(@private_sea_menu_id, '编辑私海配置', 4, 2, '', '', 1, 0, 0, 'Edit', NOW(), NOW(), 0),
(@private_sea_menu_id, '删除私海配置', 4, 3, '', '', 1, 0, 0, 'Delete', NOW(), NOW(), 0),
(@public_sea_rule_menu_id, '新增公海规则', 4, 1, '', '', 1, 0, 0, 'Plus', NOW(), NOW(), 0),
(@public_sea_rule_menu_id, '编辑公海规则', 4, 2, '', '', 1, 0, 0, 'Edit', NOW(), NOW(), 0),
(@public_sea_rule_menu_id, '删除公海规则', 4, 3, '', '', 1, 0, 0, 'Delete', NOW(), NOW(), 0),
(@transfer_menu_id, '执行转移', 4, 1, '', '', 1, 0, 0, 'Switch', NOW(), NOW(), 0),
(@transfer_menu_id, '查看详情', 4, 2, '', '', 1, 0, 0, 'View', NOW(), NOW(), 0);

SET @admin_role_id = 1;

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_time`, `update_time`, `is_deleted`)
SELECT @admin_role_id, id, NOW(), NOW(), 0 FROM sys_menu WHERE path IN (
  '/crm/resource-security',
  '/crm/resource-security/private-sea',
  '/crm/resource-security/public-sea-rule',
  '/crm/resource-security/transfer'
) OR parent_id IN (SELECT id FROM sys_menu WHERE path IN (
  '/crm/resource-security',
  '/crm/resource-security/private-sea',
  '/crm/resource-security/public-sea-rule',
  '/crm/resource-security/transfer'
));

INSERT INTO `sys_api_permission` (`api_name`, `api_url`, `api_method`, `api_group`, `create_time`, `update_time`, `is_deleted`)
VALUES 
('私海配置列表', '/crm/private-sea', 'GET', '资源安全', NOW(), NOW(), 0),
('私海配置详情', '/crm/private-sea/{id}', 'GET', '资源安全', NOW(), NOW(), 0),
('私海使用情况', '/crm/private-sea/usage', 'GET', '资源安全', NOW(), NOW(), 0),
('新增私海配置', '/crm/private-sea', 'POST', '资源安全', NOW(), NOW(), 0),
('更新私海配置', '/crm/private-sea', 'PUT', '资源安全', NOW(), NOW(), 0),
('删除私海配置', '/crm/private-sea/{id}', 'DELETE', '资源安全', NOW(), NOW(), 0),
('公海规则列表', '/crm/public-sea-rule', 'GET', '资源安全', NOW(), NOW(), 0),
('公海规则详情', '/crm/public-sea-rule/{id}', 'GET', '资源安全', NOW(), NOW(), 0),
('新增公海规则', '/crm/public-sea-rule', 'POST', '资源安全', NOW(), NOW(), 0),
('更新公海规则', '/crm/public-sea-rule', 'PUT', '资源安全', NOW(), NOW(), 0),
('删除公海规则', '/crm/public-sea-rule/{id}', 'DELETE', '资源安全', NOW(), NOW(), 0),
('转移记录列表', '/crm/transfer', 'GET', '资源安全', NOW(), NOW(), 0),
('转移记录详情', '/crm/transfer/{id}', 'GET', '资源安全', NOW(), NOW(), 0),
('获取用户客户', '/crm/transfer/user/customers/{userId}', 'GET', '资源安全', NOW(), NOW(), 0),
('获取用户线索', '/crm/transfer/user/leads/{userId}', 'GET', '资源安全', NOW(), NOW(), 0),
('创建转移记录', '/crm/transfer', 'POST', '资源安全', NOW(), NOW(), 0),
('离职一键回收', '/crm/transfer/resign', 'POST', '资源安全', NOW(), NOW(), 0);
