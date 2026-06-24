# Maritime Library Management System

## 📚 项目简介

Maritime 图书管理系统是一个基于 Spring Boot 构建的 RESTful 风格后端项目，提供图书管理、借阅管理、读者证管理、用户管理等核心功能，支持文件上传下载、JWT 身份认证、统一异常处理等基础设施。

## 🏗️ 技术栈

| 技术 | 说明 | 版本 |
|------|------|------|
| **Spring Boot** | 应用框架 | 2.4.5 |
| **Java** | 编程语言 | 8/17 (编译目标17) |
| **MyBatis-Plus** | ORM 框架 | 3.5.3.1 |
| **MySQL** | 数据库 | 8.0.33 |
| **JWT (jjwt)** | 身份认证 | 0.9.0 |
| **Hutool** | 工具包 | 5.3.10 |
| **Lombok** | 代码简化 | - |
| **EasyExcel** | Excel 处理 | 3.1.1 |
| **Fastjson** | JSON 处理 | 1.2.62 |
| **UserAgentUtils** | 设备信息解析 | 1.21 |
| **Maven** | 构建工具 | - |

## 📁 项目结构

```
com.hs.maritime
├── MaritimeBackApplication.java          # 启动类
├── common/
│   ├── Result.java                       # 统一响应结果
│   ├── PageResult.java                   # 分页结果封装
│   └── SystemConstant.java               # 系统常量
├── config/
│   ├── GlobalCorsConfig.java             # 全局跨域配置
│   ├── JacksonConfig.java                # JSON 序列化配置
│   ├── MybatisPlusConfig.java            # MyBatis-Plus 分页插件
│   └── WebMvcConfig.java                 # Web MVC 配置（拦截器、格式化）
├── controller/
│   ├── AuthController.java               # 认证接口（登录/注册）
│   ├── BookController.java               # 图书管理接口
│   ├── BookCopyController.java           # 图书副本管理接口
│   ├── BorrowRecordController.java       # 借阅记录接口
│   ├── CategoryController.java           # 图书分类接口
│   ├── LibraryCardController.java        # 读者证接口
│   ├── LibraryCardTypeController.java    # 读者证类型接口
│   ├── UserController.java               # 用户管理接口
│   └── WebController.java                # 文件下载接口
├── dto/
│   ├── BookQueryDTO.java                 # 图书查询条件
│   ├── BookCopyQueryDTO.java             # 副本查询条件
│   ├── CategoryQueryDTO.java             # 分类查询条件
│   └── RecordQueryDTO.java               # 借阅记录查询条件
├── entity/
│   ├── Book.java                         # 图书实体
│   ├── BookCopy.java                     # 图书副本实体
│   ├── BorrowRecord.java                 # 借阅记录实体
│   ├── Category.java                     # 图书分类实体
│   ├── LibraryCard.java                  # 读者证实体
│   ├── LibraryCardType.java              # 读者证类型实体
│   ├── Role.java                         # 角色实体
│   ├── User.java                         # 用户实体
│   ├── UserLoginLog.java                 # 登录日志实体
│   └── UserProfile.java                  # 用户资料实体
├── enums/
│   ├── ResultEnum.java                   # 响应状态枚举
│   └── UserStatusEnum.java               # 用户状态枚举
├── exceptions/
│   ├── MaritimeException.java            # 自定义业务异常
│   └── MaritimeExceptionHandler.java     # 全局异常处理器
├── interceptor/
│   └── JWTInterceptor.java               # JWT 鉴权拦截器
├── mapper/
│   ├── BookCopyMapper.java
│   ├── BookMapper.java
│   ├── BorrowRecordMapper.java
│   ├── CategoryMapper.java
│   ├── LibraryCardMapper.java
│   ├── LibraryCardTypeMapper.java
│   ├── RoleMapper.java
│   ├── UserLoginLogMapper.java
│   ├── UserMapper.java
│   └── UserProfileMapper.java
├── service/
│   ├── impl/                             # Service 实现类
│   │   ├── BookServiceImpl.java
│   │   ├── BookCopyServiceImpl.java
│   │   ├── BorrowRecordServiceImpl.java
│   │   ├── CategoryServiceImpl.java
│   │   ├── FileServiceImpl.java
│   │   ├── LibraryCardServiceImpl.java
│   │   ├── LibraryCardTypeServiceImpl.java
│   │   ├── RoleServiceImpl.java
│   │   ├── UserLoginLogServiceImpl.java
│   │   ├── UserProfileServiceImpl.java
│   │   └── UserServiceImpl.java
│   └── ...                               # Service 接口
├── utils/
│   ├── DeviceLogUtils.java               # 设备信息解析工具
│   ├── JWTUtils.java                     # JWT 令牌工具
│   └── MD5Utils.java                     # MD5 加密工具
└── vo/
    ├── BookVO.java
    ├── BookCopyVO.java
    ├── BorrowRecordVO.java
    ├── BorrowUserVO.java
    ├── CategoryVO.java
    ├── LibraryCardTypeVO.java
    ├── LibraryCardVO.java
    └── UserVO.java
```

## 🗄️ 数据库设计

数据库名：`maritime_db`，共 10 张表：

| 表名 | 说明 |
|------|------|
| `users` | 用户主表 |
| `user_profiles` | 用户资料表（通过触发器自动创建） |
| `roles` | 角色表（admin / librarian / reader） |
| `user_login_logs` | 用户登录日志表 |
| `books` | 图书主表 |
| `book_copies` | 图书副本表 |
| `categories` | 图书分类表 |
| `library_card_types` | 读者证类型表 |
| `library_cards` | 读者证表（一人一证） |
| `borrow_records` | 借阅记录表 |

### 核心实体关系

- **User** 1:1 **UserProfile**（用户注册时触发器自动创建）
- **User** N:1 **Role**（角色编码：admin / librarian / reader）
- **User** 1:1 **LibraryCard**（一人一证）
- **LibraryCard** N:1 **LibraryCardType**（普通/学生/教师/VIP）
- **Book** 1:N **BookCopy**（每种书有多个副本）
- **LibraryCard** 1:N **BorrowRecord**（借阅记录）
- **BookCopy** 1:N **BorrowRecord**（每个副本对应一条借阅记录）

## 🔑 API 接口文档

所有接口统一前缀：`/api`（在 `application.yml` 中通过 `server.servlet.path` 配置）

### 认证模块（无需 Token）

| 请求方式 | URL | 说明 |
|---------|-----|------|
| POST | `/login` | 用户登录 |
| POST | `/register` | 用户注册 |

### 图书管理

| 请求方式 | URL | 说明 |
|---------|-----|------|
| GET | `/book/bookPage` | 分页查询图书列表（后台管理） |
| GET | `/book/displayPage` | 分页查询图书（前台查阅，支持关键词搜索） |
| POST | `/book/add` | 新增图书 |
| PUT | `/book/update` | 修改图书 |
| DELETE | `/book/delete/{id}` | 删除图书 |
| DELETE | `/book/deleteBatch` | 批量删除图书 |
| POST | `/book/uploadCover` | 上传图书封面 |

### 图书副本管理

| 请求方式 | URL | 说明 |
|---------|-----|------|
| GET | `/copy/selectPage` | 分页查询副本列表 |
| POST | `/copy/add` | 新增副本（自动更新图书总/可借数量） |
| PUT | `/copy/update` | 修改副本信息 |
| DELETE | `/copy/delete/{id}` | 删除副本 |

### 借阅记录管理

| 请求方式 | URL | 说明 |
|---------|-----|------|
| POST | `/borrow/add` | 新增借阅记录（支持批量借阅） |
| GET | `/borrow/page` | 分页查询借阅记录 |
| PUT | `/borrow/update` | 更新借阅记录 |

### 图书分类管理

| 请求方式 | URL | 说明 |
|---------|-----|------|
| GET | `/category/list` | 分类列表（全部） |
| GET | `/category/page` | 分页查询分类 |
| POST | `/category/add` | 新增分类 |
| PUT | `/category/update` | 修改分类 |
| DELETE | `/category/delete/{id}` | 删除分类（有关联图书时阻止删除） |
| DELETE | `/category/deleteBatch` | 批量删除分类 |

### 读者证类型管理

| 请求方式 | URL | 说明 |
|---------|-----|------|
| GET | `/cardType/list` | 类型列表（全部） |
| GET | `/cardType/page` | 分页查询类型 |
| POST | `/cardType/add` | 新增类型 |
| PUT | `/cardType/update` | 修改类型 |
| DELETE | `/cardType/delete/{id}` | 删除类型（有关联读者证时阻止删除） |
| DELETE | `/cardType/deleteBatch` | 批量删除类型 |

### 读者证管理

| 请求方式 | URL | 说明 |
|---------|-----|------|
| GET | `/card/list` | 读者证列表（全部） |
| GET | `/card/page` | 分页查询读者证 |
| POST | `/card/add` | 新增读者证（校验押金） |
| PUT | `/card/update` | 修改读者证 |
| DELETE | `/card/delete/{id}` | 删除读者证 |
| DELETE | `/card/deleteBatch` | 批量删除读者证 |

### 用户管理

| 请求方式 | URL | 说明 |
|---------|-----|------|
| PUT | `/user/update` | 修改用户信息 |
| POST | `/user/uploadAvatar` | 上传头像 |
| POST | `/user/changePassword` | 修改密码 |
| POST | `/user/updateNotification` | 通知设置 |
| POST | `/user/updatePrivacy` | 隐私设置 |
| DELETE | `/user/off` | 注销账号 |
| GET | `/user/withoutCardUsers` | 获取无读者证的用户列表 |
| GET | `/user/borrowUser` | 根据关键词查询借阅用户详情 |

### 文件下载（无需 Token）

| 请求方式 | URL | 说明 |
|---------|-----|------|
| GET | `/download/avatar/{fileName}` | 下载头像文件 |
| GET | `/download/cover/{fileName}` | 下载图书封面文件 |

### 响应格式

**成功响应：**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

**分页响应：**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 100,
    "pages": 10,
    "list": []
  }
}
```

**失败响应：**
```json
{
  "code": 500,
  "msg": "错误信息",
  "data": null
}
```

## ⚙️ 配置说明

### 应用配置（application.yml）

```yaml
server:
  port: 8080                          # 服务端口
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/maritime_db
    username: root
    password: 123456
  mvc:
    servlet:
      path: /api                      # 接口统一前缀
  servlet:
    multipart:
      max-file-size: 5MB              # 单文件最大
      max-request-size: 20MB          # 请求最大
file:
  avatar-dir: .../avatars/            # 头像存储路径
  cover-dir: .../covers/              # 封面存储路径
```

### JWT 配置（JWTUtils.java 常量）

| 参数 | 值 |
|------|-----|
| 令牌有效期 | 3600000ms（1小时） |
| 签名密钥 | jwt123 |
| 请求头名称 | Auth-Token |

### 跨域配置

支持所有来源、所有请求头、GET/POST/PUT/DELETE 方法、允许携带 Cookie，暴露 `Auth-Token` 响应头。

## 🛡️ 安全机制

1. **密码加密**：使用 MD5 + 16位随机盐值加密存储
2. **JWT 鉴权**：除 `/login`、`/register`、`/download/**` 外所有请求需携带 `Auth-Token`
3. **全局异常处理**：统一返回标准错误格式
4. **一人一证**：读者证表通过唯一索引 `uk_user_id` 约束

## 🚀 快速启动

### 前置条件

- JDK 8+（推荐 17）
- Maven 3.6+
- MySQL 8.0+
- IDE（推荐 IntelliJ IDEA）

### 启动步骤

1. **初始化数据库**
   ```sql
   source src/main/resources/db/maritime_db.sql
   ```

2. **修改配置**
   编辑 `src/main/resources/application.yml`，修改数据库连接信息（用户名、密码）和文件存储路径。

3. **启动应用**
   ```bash
   mvn spring-boot:run
   ```
   或在 IDE 中运行 `MaritimeBackApplication.java`。

4. **验证**
   访问 `http://localhost:8080/api/login`，服务正常启动即可。

### 初始数据

SQL 脚本中已预置：
- 三种角色：admin（系统管理员）、librarian（图书管理员）、reader（普通读者）
- 10 个图书分类（文学、科技、历史、哲学等）
- 100 本图书数据
- 4 种读者证类型（普通/学生/教师/VIP）
- 1 张测试读者证

## 📝 开发说明

### 代码规范

- 使用 Lombok 简化实体/DTO/VO 的 Getter/Setter
- Controller 层统一返回 `Result<T>` 或 `PageResult<T>`
- 业务异常统一抛出 `MaritimeException`
- 所有 Service 接口继承 `IService` 获得 MyBatis-Plus 通用 CRUD

### 分页查询流程

1. Controller 接收 `pageNum`、`pageSize` 参数
2. 构建 `QueryWrapper` 条件
3. 调用 `Service.page(new Page<>(), queryWrapper)`
4. 将 `IPage` 通过 `PageResult.of()` 转换为统一格式
5. 使用 `page.convert()` 将 Entity 转为 VO

### 借阅业务流程

1. 查询用户 → 校验读者证 → 获取类型规则
2. 自动分配可借副本
3. 创建借阅记录（生成编号 `JY + 时间 + 图书ID + 用户ID`）
4. 更新副本状态为"已借出"
5. 更新图书的可借/已借数量
