<!--
Template for changes, <mandatory> [optional]
- <New feature/Improvements/Bug fix> - [(Pull Request Number) \[@GitHubUsername\]]
-->

**:warning: THIS IS IN EXPERIMENTAL STAGE, DO NOT USE IN PRODUCTION ENVIRONMENT!**
**:warning: 该软件仍在实验阶段，请不要用于生产环境！**

------

# Universal Chat and Broadcast API

:bangbang: This version **MIGHT NOT** represent all the features in the final production/official version! :bangbang:

## :warning: BREAKING CHANGES :warning:

- The dedicated messaging APIs for QQ Bots were removed
- The dedicated messaging/broadcasting APIs for Minecraft Servers were removed
- The `say` function of Minecraft Servers was replaced by `sendMessage`

## New features

- LOGs will be written to a file named `RemoteMC-Core.log`
- LOGs of every day will be archived in a file named `RemoteMC-Core.YYYY-MM-DD.log.gz`. An archive of more than 30 days or 3GB will be deleted
- Function for sending broadcast to QQ Groups was added

## Improvements

- LOGs outputting to `STDOUT` will shown in a simplified format
- Functions for sending messages to QQ Groups were moved from `QQBotEntity` to `QQGroupEntity`

## Bug fixes

`There is nothing to display yet.`

## Known Bugs

`There is nothing to display yet.`

------

# 通用聊天和广播 API

:bangbang: 该版本 **可能不会** 代表在生产/正式版本中的所有功能！ :bangbang:

## :warning: 破坏性变更 :warning:

- QQ 机器人专有的消息发送 API 已被移除
- Minecraft 服务器专有的消息/广播发送 API 已被移除
- Minecraft 服务器的 `say` 函数已被替换为 `sendMessage`

## 新增功能

- 日志将会被写入文件 `RemoteMC-Core.log`
- 每日日志将会被存档为文件 `RemoteMC-Core.YYYY-MM-DD.log.gz`。超过 30 天或 3GB 的存档将会被删除
- 向 QQ 群聊发送广播的函数已被添加

## 优化与改进

- 输出到 `STDOUT` 的日志内容将会以简化格式显示
- 将向 QQ 群聊发送消息的函数从 `QQBotEntity` 移动到 `QQGroupEntity` 中

## 错误修复

`暂时没有可以显示的内容`

## 已知错误

`暂时没有可以显示的内容`

------

**Full Changelog**: https://github.com/iXORTech/RemoteMC-Core/compare/v0.1.1-beta...v0.2.0-beta
**完整的更新日志**: https://github.com/iXORTech/RemoteMC-Core/compare/v0.1.1-beta...v0.2.0-beta
