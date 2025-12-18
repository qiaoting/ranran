# 创建数据库
CREATE DATABASE IF NOT EXISTS `ranran` DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_unicode_ci;
# 使用数据库
USE `ranran`;
# 创建系统用户表
CREATE TABLE `sys_user`
(
    `user_id`     bigint       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `file_id`     bigint       NULL     DEFAULT NULL COMMENT '图片ID',
    `username`    varchar(50)  NOT NULL COMMENT '用户名',
    `password`    varchar(100) NOT NULL COMMENT '密码(加密存储)',
    `nickname`    varchar(50)  NULL     DEFAULT NULL COMMENT '昵称',
    `avatar`      varchar(255) NULL     DEFAULT NULL COMMENT '头像URL',
    `email`       varchar(100) NULL     DEFAULT NULL COMMENT '邮箱',
    `phone`       varchar(20)  NULL     DEFAULT NULL COMMENT '手机号',
    `is_admin`    char(1)      NOT NULL DEFAULT '1' COMMENT '是否管理员(0-否,1-是)',
    `status`      char(1)      NOT NULL DEFAULT '1' COMMENT '状态(0-禁用,1-正常)',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `idx_username` (`username`)
) ENGINE = InnoDB COMMENT = '系统用户表';
# 创建系统角色表
CREATE TABLE `sys_role`
(
    `role_id`     bigint       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name`   varchar(50)  NOT NULL COMMENT '角色名称',
    `role_code`   varchar(50)  NOT NULL COMMENT '角色编码',
    `description` varchar(255) NULL     DEFAULT NULL COMMENT '角色描述',
    `sort`        int          NULL DEFAULT 0 COMMENT '排序号',
    `status`      char(1)      NOT NULL DEFAULT '1' COMMENT '状态(0-禁用,1-正常)',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`role_id`)
) ENGINE = InnoDB COMMENT = '系统角色表';
# 创建系统菜单表
CREATE TABLE `sys_menu`
(
    `menu_id`     bigint       NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `parent_id`   bigint       NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    `menu_name`   varchar(50)  NOT NULL COMMENT '菜单名称',
    `menu_code`   varchar(100) NOT NULL COMMENT '菜单编码',
    `menu_type`   tinyint      NOT NULL DEFAULT 1 COMMENT '菜单类型(1-目录,2-菜单,3-按钮)',
    `path`        varchar(255) NULL     DEFAULT NULL COMMENT '路由路径',
    `view_path`   varchar(255) NULL     DEFAULT NULL COMMENT '组件路径',
    `permissions` varchar(255)          DEFAULT NULL COMMENT '权限字符串',
    `icon`        varchar(50)  NULL     DEFAULT NULL COMMENT '图标',
    `sort`        int          NOT NULL DEFAULT 0 COMMENT '排序号',
    `status`      char(1)      NOT NULL DEFAULT '1' COMMENT '状态(0-禁用,1-正常)',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`menu_id`)
) ENGINE = InnoDB COMMENT = '系统菜单表';
# 创建用户角色关联表
CREATE TABLE `sys_role_user`
(
    `user_role_id` bigint   NOT NULL AUTO_INCREMENT COMMENT '用户角色关联ID',
    `role_id`      bigint   NOT NULL COMMENT '角色ID',
    `user_id`      bigint   NOT NULL COMMENT '用户ID',
    `create_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`user_role_id`) USING BTREE,
    UNIQUE INDEX `uk_user_role` (`user_id` ASC, `role_id` ASC) COMMENT '用户角色关联唯一索引'
) ENGINE = InnoDB COMMENT = '用户角色关联表';
# 创建角色菜单关联表
CREATE TABLE `sys_role_menu`
(
    `role_menu_id` bigint   NOT NULL AUTO_INCREMENT COMMENT '角色菜单关联ID',
    `role_id`      bigint   NOT NULL COMMENT '角色ID',
    `menu_id`      bigint   NOT NULL COMMENT '菜单ID',
    `create_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`role_menu_id`)
) ENGINE = InnoDB COMMENT = '角色菜单关联表';
# 创建系统文件存储记录表
CREATE TABLE `sys_file`
(
    `file_id`     bigint        NOT NULL AUTO_INCREMENT COMMENT '文件ID',
    `file_name`   varchar(255)  NOT NULL COMMENT '原始文件名',
    `file_url`    varchar(512)  NOT NULL COMMENT '文件访问URL',
    `file_path`   varchar(1024) NOT NULL COMMENT '本地存储绝对路径',
    `file_type`   varchar(100)  NULL     DEFAULT '' COMMENT '文件MIME类型',
    `file_size`   bigint        NOT NULL DEFAULT 0 COMMENT '文件大小',
    `create_by`   varchar(64)   NOT NULL COMMENT '上传人',
    `create_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `update_time` datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`file_id`)
) ENGINE = InnoDB COMMENT = '系统文件存储记录表';
# 创建系统公告表
CREATE TABLE `sys_announcement`
(
    `announcement_id`   bigint       NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    `title`             varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
    `content`           TEXT         NOT NULL COMMENT '内容',
    `announcement_type` char(1)      NOT NULL DEFAULT '1' COMMENT '公告类型(1-普通公告)',
    `status`            char(1)      NOT NULL DEFAULT '1' COMMENT '状态(0-禁用,1-正常)',
    `create_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`announcement_id`),
    KEY `idx_title` (`title`),
    FULLTEXT KEY `idx_content` (`content`)
) ENGINE = InnoDB COMMENT ='系统公告';
# 创建定时任务表
CREATE TABLE `sys_task`
(
    `task_id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '任务ID',
    `task_name`       VARCHAR(60)  NOT NULL DEFAULT '' COMMENT '任务名称',
    `task_code`       VARCHAR(255) NOT NULL COMMENT '任务编码',
    `cron_expression` VARCHAR(255)          DEFAULT '' COMMENT 'cron表达式',
    `status`          CHAR(1)               DEFAULT '1' COMMENT '状态(0-禁用,1-正常)',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`task_id`)
) ENGINE = InnoDB COMMENT = '定时任务表';
# 创建登录日志
CREATE TABLE `sys_login_log`
(
    `log_id`      bigint       NOT NULL AUTO_INCREMENT COMMENT '访问ID',
    `username`    varchar(50)  NULL     DEFAULT '' COMMENT '用户账号',
    `msg`         varchar(255) NULL     DEFAULT '' COMMENT '提示消息',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    PRIMARY KEY (`log_id`)
) ENGINE = InnoDB COMMENT = '登录日志';
# 插入管理员账号
INSERT INTO `sys_user`
VALUES (1, 1, 'admin', '$2a$10$kJFZ8OaA5mhqpLTO7hIvL.Hb/9LRqARRw6QTHb7nGrUD/BP536tpS', '然然',
        '/upload/5467d5acbb6f4fc089616121331acfb0.png', 'admin@example.com', '13800001111', '1', '1',
        '2025-12-12 12:12:12', '2025-12-12 12:12:12');
# 插入测试账号张三
INSERT INTO `sys_user`
VALUES (2, 2, 'zhangsan', '$2a$10$kJFZ8OaA5mhqpLTO7hIvL.Hb/9LRqARRw6QTHb7nGrUD/BP536tpS', '张三', NULL, '', '', '0',
        '1', '2025-12-12 12:12:12', '2025-12-12 12:12:12');
# 插入菜单数据
INSERT INTO `sys_menu`
VALUES (1, 0, '系统管理', 'System', 1, 'system', 'ParentView', NULL, 'Setting', 1, '1', '2025-12-12 12:12:12',
        '2025-12-12 12:12:12');
INSERT INTO `sys_menu`
VALUES (2, 1, '用户管理', 'User', 2, 'user', 'system/user/index', 'system:user:index', 'User', 1, '1',
        '2025-12-12 12:12:12', '2025-12-12 12:12:12');
INSERT INTO `sys_menu`
VALUES (3, 1, '角色管理', 'Role', 2, 'role', 'system/role/index', 'system:role:index', 'Film', 2, '1',
        '2025-12-12 12:12:12', '2025-12-12 12:12:12');
INSERT INTO `sys_menu`
VALUES (4, 1, '菜单管理', 'Menu', 2, 'menu', 'system/menu/index', 'system:menu:index', 'Menu', 3, '1',
        '2025-12-12 12:12:12', '2025-12-12 12:12:12');
INSERT INTO `sys_menu`
VALUES (5, 1, '登录日志', 'Loginlog', 2, 'loginlog', 'system/log/index', 'system:loginlog:index', 'Document', 20, '1',
        '2025-12-12 12:12:12', '2025-12-12 12:12:12');
INSERT INTO `sys_menu`
VALUES (6, 1, '公告管理', 'Announcement', 2, 'announcement', 'system/announcement/index', 'system:announcement:index',
        'Timer', 4, '1', '2025-12-12 12:12:12', '2025-12-12 12:12:12');
INSERT INTO `sys_menu`
VALUES (7, 1, '定时任务', 'Task', 2, 'task', 'system/task/index', 'system:task:index', 'AlarmClock', 5, '1',
        '2025-12-12 12:12:12', '2025-12-12 12:12:12');
# 插入定时任务数据
INSERT INTO `sys_task`
VALUES (1, '健康检查', 'sysHealthTask', '0 0/5 * * * ?', '1', '2025-12-12 12:12:12', '2025-12-12 12:12:12');
