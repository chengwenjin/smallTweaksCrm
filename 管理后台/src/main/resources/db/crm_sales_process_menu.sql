-- ============================================================
-- 销售过程与商机管理 -> 过程跟进与外勤
-- 菜单数据初始化脚本
-- ============================================================

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- 1. 插入销售过程与商机管理顶级菜单（如果不存在）
INSERT IGNORE INTO `sys_menu` (`parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(0, '销售过程与商机管理', 1, NULL, '/crm/sales-process', 'Layout', 'TrendCharts', 70, 1, 1, 1);

-- 2. 插入过程跟进与外拍子菜单目录
-- 先获取销售过程与商机管理菜单ID
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
