### 前端依赖
- Node 22
- Vite 7.2.7
- Vue 3.5.22
- Element-Plus 2.11.5
- Pinia 3.0.3
- Router 4.6.3
- Axios 1.13.1

### 前端开发环境搭建
- 安装Node 22，并配置好环境变量
- 使用VsCode打开前端代码，或者使用命令行进入前端代码目录
- 执行 `npm install` 安装依赖
- 前端代码中vite.config.js文件中配置了代理，默认指向本地的8080端口，如有需要可自行修改
  target: 'http://localhost:8080/'
- 执行 `npm run dev` 启动前端服务，默认端口为3000
- 浏览器访问 `http://localhost:3000` 即可看到系统登录页面

### 前端目录说明

- api，存放后台接口相关
- assets，存放静态资源
- components，存放可复用的组件
- layout，存放布局相关
- router，路由配置
- store，状态管理
- utils，工具函数
- views，视图组件