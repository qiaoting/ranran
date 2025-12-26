## 一、项目说明

感谢您关注此开源项目。本开源项目使用SpringBoot4+MybatisPlus+Vue3+Element-Plus，
旨在通过简洁实用的方式整合最新技术栈，便于开发、调试与交付。
希望它能为你的学习和开发工作带来帮助与借鉴。 对于希望自己从零快速搭建项目框架的开发者
或者喜欢纯粹原生组件无过度封装的，特别适合本项目。克隆项目后可以快速配置并启动。
## 二、开发环境
### 后端
    后端的依赖非常精简，基本以spring相关为主
- Java 21  # 使用了当前最新版本的springboot，建议java版本为21，不过使用17也不会报错
- Spring Boot 4.0.0
- MyBatis Plus 3.5.15
- MySQL 8.0.41 # 服务器上安装的mysql版本为5.7
- Redis 5.0.14.1
- Maven 3.9.6
### 前端
- Node 22
- Vite 7.2.7
- Vue 3.5.22
- Element-Plus 2.11.5
- Pinia 3.0.3
- Router 4.6.3
- Axios 1.13.1

## 三、功能简介

### 系统登录
默认内置 [admin/123456] 账号，拥有所有权限
![输入图片说明](https://raw.gitcode.com/user-images/assets/8763983/e99a396e-e63e-4018-9662-e27cd09e4775/登录.png "登录.png")

### 系统首页
![输入图片说明](https://foruda.gitee.com/images/1766061622396058300/f40b286c_9519049.png "首页.png")

### 个人中心
![输入图片说明](https://foruda.gitee.com/images/1766061637376657401/55c25890_9519049.png "个人中心.png")

### 用户管理
管理员账号默认禁止删除和禁用，其他用户新增修改均可操作，删除需要注意有无在角色中绑定，需要解绑后才可删除
![输入图片说明](https://foruda.gitee.com/images/1766061654884046256/493f287b_9519049.png "用户管理.png")
添加新菜单，要求在前端代码中views目录下有对应的页面文件,比如添加图书管理菜单：
- 组件路径：system/book/index
- 权限字符串建议用组件路径斜杠转冒号：system:book:index
- 图标选择：点击输入框会弹窗供选择图标
- 菜单类型：目录/菜单/按钮，当前只用到了目录和菜单，按钮类型预留

### 菜单管理
![输入图片说明](https://foruda.gitee.com/images/1766061672363573563/a9e255d9_9519049.png "菜单管理.png")

### 角色管理
角色页面，基本的增删改查功能，注意选择对应菜单时，父级菜单不会自动勾选，需要手动勾选。
角色在删除的时候会校验是否有用户绑定，有绑定则不允许删除
![输入图片说明](https://foruda.gitee.com/images/1766061694499059874/d63967e7_9519049.png "角色管理.png")
其中分配用户点击后会跳转到用户列表，可以添加或者删除角色对应的用户
![输入图片说明](https://foruda.gitee.com/images/1766061708839346137/3f79afe2_9519049.png "角色分配用户.png")

### 公告管理
公告的新增目前简单用了输入框，后续可以改成富文本编辑器
![输入图片说明](https://foruda.gitee.com/images/1766061727716590678/cb200e23_9519049.png "公告管理.png")
公告发布后，登录系统时会在首页弹窗显示，点击关闭后不会再显示

### 定时任务
定时任务后台基于SpringTask实现
当前只实现了简单的cron表达式配置和启动停止功能
![输入图片说明](https://foruda.gitee.com/images/1766061772599075579/be1a506a_9519049.png "定时任务.png")

### 登录日志
略，后台在登录时简单记录了登录日志，可自行删去

## 四、本地开发环境搭建
### 1、数据库初始化
直接在mysql中执行sql目录下的 `ranran.sql` 脚本即可创建所需的数据库表和几行初始数据；保证redis可用
### 2、后端环境搭建
- 安装JDK 21，并配置好环境变量
- 安装Maven 3.9.6，也可以使用idea中自带的maven
- 使用IDEA打开后端代码，等待依赖下载完成，或者手动点击maven的更新依赖按钮
- 修改 `src/main/resources/application-dev.yml` 中的Mysql、Redis连接配置为你本地的配置
- 运行 `com.ranran.system.RanranSystemApplication` 启动后端服务，默认端口8080

### 3、前端环境搭建
- 安装Node 22，并配置好环境变量
- 使用VsCode打开前端代码，或者使用命令行进入前端代码目录
- 执行 `npm install` 安装依赖
- 前端代码中vite.config.js文件中配置了代理，默认指向本地的8080端口，如有需要可自行修改
  target: 'http://localhost:8080/'
- 执行 `npm run dev` 启动前端服务，默认端口为80
- 浏览器访问 `http://localhost:80` 即可看到系统登录页面

## 五、项目代码结构说明
### 1、后端代码结构
- ranran              # 项目根目录，可以在此目录下执行mvn打包命令等
  - ranran-api          # 接口模块，主要存放Controller层代码
  - ranran-common       # 通用模块，主要存放公共的工具类、常量类、枚举类等
  - ranran-persistence  # 系统模块，主要存放实体类、Service类、Mapper接口、XML映射文件等
  - ranran-framework    # 框架模块，主要存放spring和其他依赖的相关配置，如安全配置、缓存配置等

### 2、前端代码结构
- ranran-ui           # 前端代码根目录
  - public              # 静态资源目录
  - src                 # 源代码目录
    - api               # 存放所有请求后端接口相关代码
    - assets            # 存放静态资源，如图片、字体等
    - components        # 存放公共组件
    - views             # 存放各个页面组件，开发时主要在此目录下新增页面
    - store             # 存放Pinia状态管理相关代码
    - router            # 存放路由相关代码
    - utils             # 存放工具类函数
    - App.vue          # 根组件
    - main.js          # 入口文件
