CREATE TABLE IF NOT EXISTS `sys_department` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父级部门ID，0表示根节点',
    `dept_code` VARCHAR(64) NOT NULL COMMENT '部门编码',
    `dept_name` VARCHAR(100) NOT NULL COMMENT '部门名称',
    `dept_type` VARCHAR(32) DEFAULT NULL COMMENT '部门类型',
    `leader_name` VARCHAR(50) DEFAULT NULL COMMENT '负责人姓名',
    `leader_phone` VARCHAR(20) DEFAULT NULL COMMENT '负责人电话',
    `sort_no` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '修改人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_department_code` (`dept_code`, `deleted`),
    KEY `idx_sys_department_parent_id` (`parent_id`),
    KEY `idx_sys_department_name` (`dept_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_code` VARCHAR(64) NOT NULL COMMENT '角色编码',
    `role_name` VARCHAR(100) NOT NULL COMMENT '角色名称',
    `data_scope` TINYINT NOT NULL DEFAULT 4 COMMENT '数据范围',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '修改人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_role_code` (`role_code`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '登录账号',
    `password_hash` VARCHAR(255) NOT NULL COMMENT '密码摘要',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `user_type` TINYINT NOT NULL COMMENT '用户类型：1管理员 2教师 3学生 4员工',
    `dept_id` BIGINT DEFAULT NULL COMMENT '所属部门ID',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
    `gender` TINYINT DEFAULT NULL COMMENT '性别：1男 2女 0未知',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
    `last_login_at` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '修改人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_user_username` (`username`, `deleted`),
    KEY `idx_sys_user_dept_id` (`dept_id`),
    KEY `idx_sys_user_real_name` (`real_name`),
    KEY `idx_sys_user_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_user_role` (`user_id`, `role_id`),
    KEY `idx_sys_user_role_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父级菜单ID',
    `menu_name` VARCHAR(100) NOT NULL COMMENT '菜单名称',
    `menu_type` CHAR(1) NOT NULL COMMENT '菜单类型：M目录 C菜单 B按钮',
    `route_path` VARCHAR(200) DEFAULT NULL COMMENT '前端路由路径',
    `component_path` VARCHAR(200) DEFAULT NULL COMMENT '前端组件路径',
    `permission_code` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    `icon` VARCHAR(100) DEFAULT NULL COMMENT '图标',
    `sort_no` INT NOT NULL DEFAULT 0 COMMENT '排序号',
    `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否可见：1是 0否',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1启用 0停用',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '修改人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_role_menu` (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

CREATE TABLE IF NOT EXISTS `biz_student` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '关联用户ID',
    `student_no` VARCHAR(32) NOT NULL COMMENT '学号',
    `student_name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` TINYINT DEFAULT NULL COMMENT '性别：1男 2女 0未知',
    `dept_id` BIGINT NOT NULL COMMENT '所属院系或班级ID',
    `major_name` VARCHAR(100) DEFAULT NULL COMMENT '专业名称',
    `grade_year` INT DEFAULT NULL COMMENT '入学年级',
    `class_name` VARCHAR(100) DEFAULT NULL COMMENT '班级名称',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1在读 0离校',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '修改人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_biz_student_no` (`student_no`, `deleted`),
    KEY `idx_biz_student_user_id` (`user_id`),
    KEY `idx_biz_student_dept_id` (`dept_id`),
    KEY `idx_biz_student_name` (`student_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学生表';

CREATE TABLE IF NOT EXISTS `biz_teacher` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '关联用户ID',
    `teacher_no` VARCHAR(32) NOT NULL COMMENT '工号',
    `teacher_name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` TINYINT DEFAULT NULL COMMENT '性别：1男 2女 0未知',
    `dept_id` BIGINT NOT NULL COMMENT '所属部门ID',
    `title_name` VARCHAR(100) DEFAULT NULL COMMENT '职称',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1在职 0离职',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
    `created_by` BIGINT DEFAULT NULL COMMENT '创建人ID',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_by` BIGINT DEFAULT NULL COMMENT '修改人ID',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    `version` INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_biz_teacher_no` (`teacher_no`, `deleted`),
    KEY `idx_biz_teacher_user_id` (`user_id`),
    KEY `idx_biz_teacher_dept_id` (`dept_id`),
    KEY `idx_biz_teacher_name` (`teacher_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教师表';

INSERT INTO `sys_department` (
    `id`, `parent_id`, `dept_code`, `dept_name`, `dept_type`, `leader_name`, `leader_phone`, `sort_no`, `status`, `remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (1001, 0, 'SYS', '系统管理部', 'office', '平台管理员', '13800000000', 1, 1, '系统默认部门', 1, 1, 0, 0),
    (2001, 0, 'INFO', '信息工程学院', 'college', '张院长', '13900000000', 2, 1, '演示部门', 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `dept_name` = VALUES(`dept_name`),
    `dept_type` = VALUES(`dept_type`),
    `leader_name` = VALUES(`leader_name`),
    `leader_phone` = VALUES(`leader_phone`),
    `status` = VALUES(`status`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `sys_role` (
    `id`, `role_code`, `role_name`, `data_scope`, `status`, `remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (1, 'admin', '系统管理员', 1, 1, '拥有全部管理权限', 1, 1, 0, 0),
    (2, 'teacher', '教师', 4, 1, '教师演示角色', 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `role_code` = VALUES(`role_code`),
    `role_name` = VALUES(`role_name`),
    `data_scope` = VALUES(`data_scope`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `sys_user` (
    `id`, `username`, `password_hash`, `real_name`, `user_type`, `dept_id`, `phone`, `email`, `gender`, `status`, `remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (1, 'admin', '{noop}admin123', '系统管理员', 1, 1001, '13800000000', 'admin@xtu.edu.cn', 1, 1, '初始化管理员账号', 1, 1, 0, 0),
    (2, 'teacher01', '{noop}teacher123', '张老师', 2, 2001, '13900000000', 'teacher01@xtu.edu.cn', 1, 1, '初始化教师账号', 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `real_name` = VALUES(`real_name`),
    `user_type` = VALUES(`user_type`),
    `dept_id` = VALUES(`dept_id`),
    `phone` = VALUES(`phone`),
    `email` = VALUES(`email`),
    `gender` = VALUES(`gender`),
    `status` = VALUES(`status`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `sys_user_role` (`user_id`, `role_id`, `created_by`) VALUES
    (1, 1, 1),
    (2, 2, 1)
ON DUPLICATE KEY UPDATE
    `created_by` = VALUES(`created_by`);

INSERT INTO `biz_student` (
    `id`, `user_id`, `student_no`, `student_name`, `gender`, `dept_id`, `major_name`, `grade_year`, `class_name`, `phone`, `email`, `status`, `remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (10001, NULL, '20230001', '李同学', 1, 2001, '软件工程', 2023, '软件工程1班', '13700000000', 'student01@xtu.edu.cn', 1, '初始化学生档案', 1, 1, 0, 0),
    (10002, NULL, '20220015', '王同学', 2, 2001, '计算机科学与技术', 2022, '计科2班', '13600000000', 'student02@xtu.edu.cn', 1, '初始化学生档案', 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `user_id` = VALUES(`user_id`),
    `student_name` = VALUES(`student_name`),
    `gender` = VALUES(`gender`),
    `dept_id` = VALUES(`dept_id`),
    `major_name` = VALUES(`major_name`),
    `grade_year` = VALUES(`grade_year`),
    `class_name` = VALUES(`class_name`),
    `phone` = VALUES(`phone`),
    `email` = VALUES(`email`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `biz_teacher` (
    `id`, `user_id`, `teacher_no`, `teacher_name`, `gender`, `dept_id`, `title_name`, `phone`, `email`, `status`, `remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (20001, 2, 'T2024001', '张老师', 1, 2001, '副教授', '13900000000', 'teacher01@xtu.edu.cn', 1, '初始化教师档案', 1, 1, 0, 0),
    (20002, NULL, 'T2024002', '李老师', 2, 2001, '讲师', '13811112222', 'teacher02@xtu.edu.cn', 1, '初始化教师档案', 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `user_id` = VALUES(`user_id`),
    `teacher_name` = VALUES(`teacher_name`),
    `gender` = VALUES(`gender`),
    `dept_id` = VALUES(`dept_id`),
    `title_name` = VALUES(`title_name`),
    `phone` = VALUES(`phone`),
    `email` = VALUES(`email`),
    `status` = VALUES(`status`),
    `remark` = VALUES(`remark`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `sys_menu` (
    `id`, `parent_id`, `menu_name`, `menu_type`, `route_path`, `component_path`, `permission_code`, `icon`, `sort_no`, `visible`, `status`, `remark`, `created_by`, `updated_by`, `deleted`, `version`
) VALUES
    (10, 0, '工作台', 'C', '/dashboard', 'views/dashboard/index.vue', 'dashboard:view', 'Odometer', 1, 1, 1, '首页工作台', 1, 1, 0, 0),
    (100, 0, '系统管理', 'M', '/system', NULL, NULL, 'Setting', 10, 1, 1, '系统管理目录', 1, 1, 0, 0),
    (110, 100, '用户管理', 'C', '/system/users', 'views/system/user/index.vue', 'system:user:view', 'User', 11, 1, 1, '用户管理菜单', 1, 1, 0, 0),
    (111, 110, '用户新增', 'B', NULL, NULL, 'system:user:create', NULL, 1, 0, 1, '用户新增按钮', 1, 1, 0, 0),
    (112, 110, '用户编辑', 'B', NULL, NULL, 'system:user:update', NULL, 2, 0, 1, '用户编辑按钮', 1, 1, 0, 0),
    (113, 110, '用户删除', 'B', NULL, NULL, 'system:user:delete', NULL, 3, 0, 1, '用户删除按钮', 1, 1, 0, 0),
    (120, 100, '角色管理', 'C', '/system/roles', 'views/system/role/index.vue', 'system:role:view', 'Avatar', 12, 1, 1, '角色管理菜单', 1, 1, 0, 0),
    (121, 120, '角色新增', 'B', NULL, NULL, 'system:role:create', NULL, 1, 0, 1, '角色新增按钮', 1, 1, 0, 0),
    (122, 120, '角色编辑', 'B', NULL, NULL, 'system:role:update', NULL, 2, 0, 1, '角色编辑按钮', 1, 1, 0, 0),
    (123, 120, '角色删除', 'B', NULL, NULL, 'system:role:delete', NULL, 3, 0, 1, '角色删除按钮', 1, 1, 0, 0),
    (130, 100, '菜单管理', 'C', '/system/menus', 'views/system/menu/index.vue', 'system:menu:view', 'Menu', 13, 1, 1, '菜单管理菜单', 1, 1, 0, 0),
    (131, 130, '菜单新增', 'B', NULL, NULL, 'system:menu:create', NULL, 1, 0, 1, '菜单新增按钮', 1, 1, 0, 0),
    (132, 130, '菜单编辑', 'B', NULL, NULL, 'system:menu:update', NULL, 2, 0, 1, '菜单编辑按钮', 1, 1, 0, 0),
    (133, 130, '菜单删除', 'B', NULL, NULL, 'system:menu:delete', NULL, 3, 0, 1, '菜单删除按钮', 1, 1, 0, 0),
    (200, 0, '组织管理', 'M', '/organization', NULL, NULL, 'OfficeBuilding', 20, 1, 1, '组织管理目录', 1, 1, 0, 0),
    (210, 200, '部门管理', 'C', '/organization/departments', 'views/organization/department/index.vue', 'organization:department:view', 'OfficeBuilding', 21, 1, 1, '部门管理菜单', 1, 1, 0, 0),
    (211, 210, '部门新增', 'B', NULL, NULL, 'organization:department:create', NULL, 1, 0, 1, '部门新增按钮', 1, 1, 0, 0),
    (212, 210, '部门编辑', 'B', NULL, NULL, 'organization:department:update', NULL, 2, 0, 1, '部门编辑按钮', 1, 1, 0, 0),
    (213, 210, '部门删除', 'B', NULL, NULL, 'organization:department:delete', NULL, 3, 0, 1, '部门删除按钮', 1, 1, 0, 0),
    (300, 0, '人员管理', 'M', '/personnel', NULL, NULL, 'UserFilled', 30, 1, 1, '人员管理目录', 1, 1, 0, 0),
    (310, 300, '学生管理', 'C', '/personnel/students', 'views/personnel/student/index.vue', 'personnel:student:view', 'Reading', 31, 1, 1, '学生管理菜单', 1, 1, 0, 0),
    (311, 310, '学生新增', 'B', NULL, NULL, 'personnel:student:create', NULL, 1, 0, 1, '学生新增按钮', 1, 1, 0, 0),
    (312, 310, '学生编辑', 'B', NULL, NULL, 'personnel:student:update', NULL, 2, 0, 1, '学生编辑按钮', 1, 1, 0, 0),
    (313, 310, '学生删除', 'B', NULL, NULL, 'personnel:student:delete', NULL, 3, 0, 1, '学生删除按钮', 1, 1, 0, 0),
    (320, 300, '教师管理', 'C', '/personnel/teachers', 'views/personnel/teacher/index.vue', 'personnel:teacher:view', 'User', 32, 1, 1, '教师管理菜单', 1, 1, 0, 0),
    (321, 320, '教师新增', 'B', NULL, NULL, 'personnel:teacher:create', NULL, 1, 0, 1, '教师新增按钮', 1, 1, 0, 0),
    (322, 320, '教师编辑', 'B', NULL, NULL, 'personnel:teacher:update', NULL, 2, 0, 1, '教师编辑按钮', 1, 1, 0, 0),
    (323, 320, '教师删除', 'B', NULL, NULL, 'personnel:teacher:delete', NULL, 3, 0, 1, '教师删除按钮', 1, 1, 0, 0)
ON DUPLICATE KEY UPDATE
    `parent_id` = VALUES(`parent_id`),
    `menu_name` = VALUES(`menu_name`),
    `menu_type` = VALUES(`menu_type`),
    `route_path` = VALUES(`route_path`),
    `component_path` = VALUES(`component_path`),
    `permission_code` = VALUES(`permission_code`),
    `icon` = VALUES(`icon`),
    `sort_no` = VALUES(`sort_no`),
    `visible` = VALUES(`visible`),
    `status` = VALUES(`status`),
    `updated_by` = VALUES(`updated_by`),
    `deleted` = 0;

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `created_by`) VALUES
    (1, 10, 1),
    (1, 100, 1),
    (1, 110, 1),
    (1, 111, 1),
    (1, 112, 1),
    (1, 113, 1),
    (1, 120, 1),
    (1, 121, 1),
    (1, 122, 1),
    (1, 123, 1),
    (1, 130, 1),
    (1, 131, 1),
    (1, 132, 1),
    (1, 133, 1),
    (1, 200, 1),
    (1, 210, 1),
    (1, 211, 1),
    (1, 212, 1),
    (1, 213, 1),
    (1, 300, 1),
    (1, 310, 1),
    (1, 311, 1),
    (1, 312, 1),
    (1, 313, 1),
    (1, 320, 1),
    (1, 321, 1),
    (1, 322, 1),
    (1, 323, 1),
    (2, 10, 1),
    (2, 300, 1),
    (2, 320, 1)
ON DUPLICATE KEY UPDATE
    `created_by` = VALUES(`created_by`);
