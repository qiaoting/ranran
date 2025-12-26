# 创建数据库，编字符集等参数自行调整
create database if not exists `ranran` default character set utf8mb4 default collate utf8mb4_unicode_ci;
# 使用数据库
use `ranran`;
# 创建系统用户表
create table `sys_user`
(
    `user_id`     bigint       not null auto_increment comment '用户id',
    `file_id`     bigint       comment '图片id',
    `username`    varchar(50)  not null comment '用户名',
    `password`    varchar(100) not null comment '密码(加密存储)',
    `nickname`    varchar(50)  comment '昵称',
    `avatar`      varchar(255) comment '头像url',
    `email`       varchar(100) comment '邮箱',
    `phone`       varchar(20)  comment '手机号',
    `is_admin`    char(1)      default '1' comment '是否管理员(0-否,1-是)',
    `status`      char(1)      default '1' comment '状态(0-禁用,1-正常)',
    `create_time` datetime     default current_timestamp comment '创建时间',
    `update_time` datetime     default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`user_id`),
    unique key `idx_username` (`username`)
) engine = innodb comment = '系统用户表';
# 创建系统角色表
create table `sys_role`
(
    `role_id`     bigint       not null auto_increment comment '角色id',
    `role_name`   varchar(50)  not null comment '角色名称',
    `role_code`   varchar(50)  not null comment '角色编码',
    `description` varchar(255) comment '角色描述',
    `sort`        int          default 0 comment '排序号',
    `status`      char(1)      not null default '1' comment '状态(0-禁用,1-正常)',
    `create_time` datetime     default current_timestamp comment '创建时间',
    `update_time` datetime     default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`role_id`)
) engine = innodb comment = '系统角色表';
# 创建系统菜单表
create table `sys_menu`
(
    `menu_id`     bigint       not null auto_increment comment '菜单id',
    `parent_id`   bigint       not null default 0 comment '父菜单id',
    `menu_name`   varchar(50)  not null comment '菜单名称',
    `menu_code`   varchar(100) not null comment '菜单编码',
    `menu_type`   tinyint      not null default 1 comment '菜单类型(1-目录,2-菜单,3-按钮)',
    `path`        varchar(255) comment '路由路径',
    `view_path`   varchar(255) comment '组件路径',
    `permissions` varchar(255) comment '权限字符串',
    `icon`        varchar(50)  comment '图标',
    `sort`        int          default 0 comment '排序号',
    `status`      char(1)      not null default '1' comment '状态(0-禁用,1-正常)',
    `create_time` datetime     default current_timestamp comment '创建时间',
    `update_time` datetime     default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`menu_id`)
) engine = innodb comment = '系统菜单表';
# 创建用户角色关联表
create table `sys_role_user`
(
    `user_role_id` bigint   not null auto_increment comment '用户角色关联id',
    `role_id`      bigint   not null comment '角色id',
    `user_id`      bigint   not null comment '用户id',
    `create_time`  datetime default current_timestamp comment '创建时间',
    primary key (`user_role_id`) using btree,
    unique index `uk_user_role` (`user_id` asc, `role_id` asc) comment '用户角色关联唯一索引'
) engine = innodb comment = '用户角色关联表';
# 创建角色菜单关联表
create table `sys_role_menu`
(
    `role_menu_id` bigint   not null auto_increment comment '角色菜单关联id',
    `role_id`      bigint   not null comment '角色id',
    `menu_id`      bigint   not null comment '菜单id',
    `create_time`  datetime default current_timestamp comment '创建时间',
    primary key (`role_menu_id`)
) engine = innodb comment = '角色菜单关联表';
# 创建系统文件存储记录表
create table `sys_file`
(
    `file_id`     bigint        not null auto_increment comment '文件id',
    `file_name`   varchar(255)  comment '原始文件名',
    `file_url`    varchar(512)  comment '文件访问url',
    `file_path`   varchar(1024) comment '本地存储绝对路径',
    `file_type`   varchar(100)  default '' comment '文件mime类型',
    `file_size`   bigint        default 0 comment '文件大小',
    `create_by`   varchar(64)   comment '上传人',
    `create_time` datetime      default current_timestamp comment '上传时间',
    `update_time` datetime      default current_timestamp on update current_timestamp,
    primary key (`file_id`)
) engine = innodb comment = '系统文件存储记录表';
# 创建系统公告表
create table `sys_announcement`
(
    `announcement_id`   bigint       not null auto_increment comment '公告id',
    `title`             varchar(255) not null default '' comment '标题',
    `content`           text         comment '内容',
    `announcement_type` char(1)      not null default '1' comment '公告类型(1-普通公告)',
    `status`            char(1)      not null default '1' comment '状态(0-禁用,1-正常)',
    `create_time`       datetime     default current_timestamp comment '创建时间',
    `update_time`       datetime     default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`announcement_id`),
    key `idx_title` (`title`),
    fulltext key `idx_content` (`content`)
) engine = innodb comment ='系统公告';
# 创建定时任务表
create table `sys_task`
(
    `task_id`         bigint       not null auto_increment comment '任务id',
    `task_name`       varchar(60)  not null default '' comment '任务名称',
    `task_code`       varchar(255) not null comment '任务编码',
    `cron_expression` varchar(255) default '' comment 'cron表达式',
    `status`          char(1)      default '1' comment '状态(0-禁用,1-正常)',
    `create_time`     datetime     default current_timestamp comment '创建时间',
    `update_time`     datetime     default current_timestamp on update current_timestamp comment '更新时间',
    primary key (`task_id`)
) engine = innodb comment = '定时任务表';
# 创建登录日志
create table `sys_login_log`
(
    `log_id`      bigint       not null auto_increment comment '访问id',
    `username`    varchar(50)  default '' comment '用户账号',
    `msg`         varchar(255) default '' comment '提示消息',
    `create_time` datetime     default current_timestamp comment '访问时间',
    primary key (`log_id`)
) engine = innodb comment = '登录日志';
# 插入管理员账号
insert into `sys_user`
values (1, 1, 'admin', '$2a$10$iZpfrngh7mCxPxg5HkWT9eUucea4J5ywKFzK3aWbKU2jVmAsSDb/.',
        '然然',null, 'admin@example.com', '13800001111', '1', '1',sysdate(), sysdate());
# 插入测试账号张三
insert into `sys_user`
values (2, 2, 'zhangsan', '$2a$10$iZpfrngh7mCxPxg5HkWT9eUucea4J5ywKFzK3aWbKU2jVmAsSDb/.',
        '张三', null, '', '', '0','1', sysdate(), sysdate());
# 插入菜单数据
insert into `sys_menu`
values (1, 0, '系统管理', 'system', 1, 'system', 'parentview', null,
        'Setting', 1, '1', sysdate(),sysdate());
insert into `sys_menu`
values (2, 1, '用户管理', 'user', 2, 'user', 'system/user/index', 'system:user:index',
        'User', 1, '1',sysdate(), sysdate());
insert into `sys_menu`
values (3, 1, '角色管理', 'role', 2, 'role', 'system/role/index', 'system:role:index',
        'Film', 2, '1',sysdate(), sysdate());
insert into `sys_menu`
values (4, 1, '菜单管理', 'menu', 2, 'menu', 'system/menu/index', 'system:menu:index',
        'Menu', 3, '1',sysdate(), sysdate());
insert into `sys_menu`
values (5, 1, '公告管理', 'announcement', 2, 'announcement', 'system/announcement/index', 'system:announcement:index',
        'Timer', 4, '1', sysdate(), sysdate());
insert into `sys_menu`
values (6, 1, '定时任务', 'task', 2, 'task', 'system/task/index', 'system:task:index',
        'AlarmClock', 5, '1',sysdate(), sysdate());
insert into `sys_menu`
values (7, 0, '监控运维', 'monitor', 1, 'monitor', '', '',
        'DataAnalysis', 2, '1', sysdate(),sysdate());
insert into `sys_menu`
values (8, 7, '登录日志', 'loginlog', 2, 'loginlog', 'monitor/loginlog/index', 'monitor:loginlog:index',
        'Document', 20, '1',sysdate(), sysdate());

# 插入定时任务数据
insert into `sys_task`
values (1, '健康检查', 'sysHealthTask', '0 0/5 * * * ?', '1', sysdate(), sysdate());
