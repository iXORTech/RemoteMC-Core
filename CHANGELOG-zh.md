# RemoteMC-Core 的更新日志

## Version 0.2.0-beta (590AA40) "通用聊天和广播 API" - 2022年8月7日

**完整的更新日志**: https://github.com/iXORTech/RemoteMC-Core/compare/v0.1.1-beta...v0.2.0-beta

### :warning: 破坏性变更 :warning:

- QQ 机器人专有的消息发送 API 已被移除
- Minecraft 服务器专有的消息/广播发送 API 已被移除
- Minecraft 服务器的 `say` 函数已被替换为 `sendMessage`

### 新增功能

- 日志将会被写入文件 `RemoteMC-Core.log`
- 每日日志将会被存档为文件 `RemoteMC-Core.YYYY-MM-DD.log.gz`。超过 30 天或 3GB 的存档将会被删除
- 向 QQ 群聊发送广播的函数已被添加

### 优化与改进

- 输出到 `STDOUT` 的日志内容将会以简化格式显示
- 将向 QQ 群聊发送消息的函数从 `QQBotEntity` 移动到 `QQGroupEntity` 中

### 错误修复

`暂时没有可以显示的内容`

### 已知错误

`暂时没有可以显示的内容`

## Version 0.1.1-beta (3CB9B5C) "用户友好的版本号展示" - 2022年7月25日

**完整的更新日志**: https://github.com/iXORTech/RemoteMC-Core/compare/v0.1.0-beta.1...v0.1.1-beta

### :warning: 破坏性变更 :warning:

- 编译完成的 jar 文件的名称不再包含修订号和 `-all` 后缀。

### 新增功能

- 版本号在软件内的显示方案更加用户友好。

### 优化与改进

`暂时没有可以显示的内容`

### 错误修复

`暂时没有可以显示的内容`

### 已知错误

`暂时没有可以显示的内容`

## Version 0.1.0-beta (4052EB2) "The first beta version" - 2022年7月16日

**完整的更新日志**: https://github.com/iXORTech/RemoteMC-Core/commits/v0.1.0-beta

### :warning: 破坏性变更 :warning:

`暂时没有可以显示的内容`

### 新增功能

- 通过文件进行配置
- I18N（本地化），目前支持 `en`（英语）以及 `zh_CN`（简体中文）
- 欢迎页面
- 状态页面
- 通过 [`RemoteMC MCDR 组件`](https://github.com/iXORTech/RemoteMC-MCDR) 连接 Minecraft 服务器的功能（已通过一些基础功能测试）
- 通过 [`RemoteMC MCDR 组件`](https://github.com/iXORTech/RemoteMC-MCDR) 控制 Minecraft 服务器的相关 API（已通过一些基础功能测试）
- QQ 机器人的连接功能（未经测试）
- QQ 机器人的控制 API（未经测试）

### 优化与改进

`暂时没有可以显示的内容`

### 错误修复

`暂时没有可以显示的内容`

### 已知错误

`暂时没有可以显示的内容`
