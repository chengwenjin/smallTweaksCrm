-- 创建 small_tweaks_crm 数据库
CREATE DATABASE IF NOT EXISTS `small_tweaks_crm` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `small_tweaks_crm`;

-- 1. sys_user 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名，全局唯一',
    `password` VARCHAR(100) NOT NULL COMMENT 'BCrypt加密密码',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '软删除：0未删除 1已删除',
    `must_change_pwd` TINYINT NOT NULL DEFAULT 0 COMMENT '是否强制修改密码',
    `login_fail_count` INT NOT NULL DEFAULT 0 COMMENT '连续登录失败次数',
    `lock_time` DATETIME DEFAULT NULL COMMENT '账号锁定截止时间',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by` VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `ext1` VARCHAR(255) DEFAULT NULL,
    `ext2` VARCHAR(255) DEFAULT NULL,
    `ext3` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_phone` (`phone`),
    KEY `idx_status` (`status`, `is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. sys_role 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_key` VARCHAR(50) NOT NULL COMMENT '角色标识',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
    `is_system` TINYINT NOT NULL DEFAULT 0 COMMENT '是否系统内置',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `ext1` VARCHAR(255) DEFAULT NULL,
    `ext2` VARCHAR(255) DEFAULT NULL,
    `ext3` VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_key` (`role_key`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 3. sys_menu 菜单表
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父级ID',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `menu_type` TINYINT NOT NULL COMMENT '类型：1目录 2菜单 3按钮',
    `permission_key` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    `path` VARCHAR(200) DEFAULT NULL COMMENT '路由地址',
    `component` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
    `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
    `sort_order` INT NOT NULL DEFAULT 0,
    `is_visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示',
    `is_cached` TINYINT NOT NULL DEFAULT 0 COMMENT '是否缓存',
    `is_external` TINYINT NOT NULL DEFAULT 0 COMMENT '是否外链',
    `status` TINYINT NOT NULL DEFAULT 1,
    `is_system` TINYINT NOT NULL DEFAULT 0,
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_key` (`permission_key`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 4. sys_api_permission 接口权限表
CREATE TABLE IF NOT EXISTS `sys_api_permission` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `permission_key` VARCHAR(100) NOT NULL COMMENT '权限标识',
    `api_name` VARCHAR(100) NOT NULL COMMENT '接口名称',
    `module` VARCHAR(50) NOT NULL COMMENT '所属模块',
    `request_method` VARCHAR(10) NOT NULL COMMENT '请求方式',
    `api_path` VARCHAR(200) NOT NULL COMMENT '接口路径',
    `description` VARCHAR(255) DEFAULT NULL,
    `status` TINYINT NOT NULL DEFAULT 1,
    `create_by` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_by` VARCHAR(50) DEFAULT NULL,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_key` (`permission_key`),
    KEY `idx_module` (`module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口权限表';

-- 5. sys_operation_log 操作日志表
CREATE TABLE IF NOT EXISTS `sys_operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `log_type` TINYINT NOT NULL COMMENT '1登录 2操作 3异常',
    `operator_id` BIGINT NOT NULL,
    `operator_name` VARCHAR(50) NOT NULL,
    `module` VARCHAR(50) NOT NULL,
    `description` VARCHAR(255) DEFAULT NULL,
    `request_method` VARCHAR(10) DEFAULT NULL,
    `request_url` VARCHAR(200) DEFAULT NULL,
    `request_params` TEXT,
    `request_headers` TEXT,
    `response_status` INT DEFAULT NULL,
    `response_result` TEXT,
    `ip` VARCHAR(50) DEFAULT NULL,
    `user_agent` VARCHAR(500) DEFAULT NULL,
    `status` TINYINT NOT NULL COMMENT '1成功 0失败',
    `error_msg` TEXT,
    `duration` INT DEFAULT NULL,
    `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_operator` (`operator_id`),
    KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 6. sys_login_log 登录日志表
CREATE TABLE IF NOT EXISTS `sys_login_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL,
    `login_status` TINYINT NOT NULL COMMENT '1成功 0失败',
    `fail_reason` VARCHAR(200) DEFAULT NULL,
    `ip` VARCHAR(50) DEFAULT NULL,
    `location` VARCHAR(100) DEFAULT NULL,
    `browser` VARCHAR(100) DEFAULT NULL,
    `os` VARCHAR(100) DEFAULT NULL,
    `user_agent` VARCHAR(500) DEFAULT NULL,
    `login_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_username` (`username`),
    KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- 7. sys_user_role 用户-角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色关联表';

-- 8. sys_role_menu 角色-菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `role_id` BIGINT NOT NULL,
    `menu_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单关联表';

-- 9. sys_role_api 角色-接口权限关联表
CREATE TABLE IF NOT EXISTS `sys_role_api` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `role_id` BIGINT NOT NULL,
    `api_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_api` (`role_id`, `api_id`),
    KEY `idx_api_id` (`api_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-接口权限关联表';

-- 插入初始化数据
-- 1. 插入默认角色
INSERT IGNORE INTO `sys_role` (`id`, `role_name`, `role_key`, `sort_order`, `status`, `is_system`, `remark`) VALUES
(1, '超级管理员', 'admin', 1, 1, 1, '系统内置超级管理员'),
(2, '普通用户', 'user', 2, 1, 0, '普通用户角色');

-- 2. 插入默认管理员用户 (密码: admin123, BCrypt加密)
INSERT IGNORE INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `email`, `status`, `must_change_pwd`) VALUES
(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', '13800138000', 'admin@example.com', 1, 0);

-- 3. 关联管理员角色
INSERT IGNORE INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 4. 插入顶级菜单
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(1, 0, '系统管理', 1, NULL, '/system', 'Layout', 'Setting', 100, 1, 1, 1),
(2, 0, '权限管理', 1, NULL, '/permission', 'Layout', 'Lock', 90, 1, 1, 1),
(3, 0, '日志管理', 1, NULL, '/log', 'Layout', 'Document', 80, 1, 1, 1);

-- 5. 插入系统管理子菜单
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `permission_key`, `path`, `component`, `icon`, `sort_order`, `is_visible`, `status`, `is_system`) VALUES
(4, 1, '用户管理', 2, 'system:user:list', 'user', 'system/user/index', 'User', 1, 1, 1, 1),
(5, 1, '角色管理', 2, 'system:role:list', 'role', 'system/role/index', 'UserFilled', 2, 1, 1, 1),
(6, 1, '菜单管理', 2, 'system:menu:list', 'menu', 'system/menu/index', 'Menu', 3, 1, 1, 1);

-- 6. 插入用户管理按钮权限
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `permission_key`, `sort_order`, `status`, `is_system`) VALUES
(7, 4, '新增用户', 3, 'system:user:add', 1, 1, 1),
(8, 4, '编辑用户', 3, 'system:user:edit', 2, 1, 1),
(9, 4, '删除用户', 3, 'system:user:delete', 3, 1, 1),
(10, 4, '重置密码', 3, 'system:user:resetPwd', 4, 1, 1);

-- 7. 给超级管理员分配所有菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) 
SELECT 1, id FROM `sys_menu`;

-- 8. 插入示例接口权限
INSERT IGNORE INTO `sys_api_permission` (`id`, `permission_key`, `api_name`, `module`, `request_method`, `api_path`, `description`, `status`) VALUES
(1, 'system:user:list', '查询用户列表', '用户管理', 'GET', '/api/system/users', '分页查询用户列表', 1),
(2, 'system:user:add', '新增用户', '用户管理', 'POST', '/api/system/users', '创建新用户', 1),
(3, 'system:user:edit', '编辑用户', '用户管理', 'PUT', '/api/system/users/{id}', '更新用户信息', 1),
(4, 'system:user:delete', '删除用户', '用户管理', 'DELETE', '/api/system/users/{id}', '删除指定用户', 1),
(5, 'system:role:list', '查询角色列表', '角色管理', 'GET', '/api/system/roles', '分页查询角色列表', 1);

-- 9. 给超级管理员分配所有接口权限
INSERT IGNORE INTO `sys_role_api` (`role_id`, `api_id`) 
SELECT 1, id FROM `sys_api_permission`;
