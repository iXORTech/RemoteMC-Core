[English](README.md) | **中文**

> [!IMPORTANT]
> 由于开发者的兴趣的缺失以及对非官方 QQ 机器人来说非常不友好的 QQ 机器人政策，
> 本项目（RemoteMC 系列）的开发将会暂停。

<h1 align="center">RemoteMC-Core</h1>

<p align="center">
  <b>RemoteMC 系列处理与API核心</b>
</p>

<p align="center">
  <a href="LICENSE">
    <img src="https://img.shields.io/badge/License-AGPL--3.0--or--later-important?style=for-the-badge" />
  </a>
</p>

RemoteMC 系列软件允许用户使用一个 RemoteMC-Core 连接多个 MC 服务器（RemoteMC-MCDR 客户端），一个 QQ 群（RemoteMC-QQ 客户端），以及以后可能出现的其他聊天机器人模块。
您可以按需要组合这些模块，组成您需要的信息同步与 MC 服务器远程控制网络。下方的图片展示了一个简单的网路配置。

> **注意：** 本程序与 MCDR 组件相关的部分已经可以正常工作。但与其他组件（例如聊天机器人）配合的部分还未完成。

<p align="center">
  <img src="https://img.cubik65536.top/file/Cubik-Image-Hosting-Storage/RemoteMC-Core-Architecture-Design-zh.png" 
  height="750"/>
</p>

## 🔉 声明

由于 [Mirai 处出现的不和谐事件](https://github.com/mamoe/mirai/issues/850)，我们做出如下声明：

- 本项目是出于开发者和贡献者们的爱好而开发的。因此，
  1. 我们可能会仅仅因为我们有兴趣而增加，更改，甚至删除一些功能
  2. 任何反对上述一点的攻击/冒犯性言论/内容/行为都会导致您被**永久封禁**
- 本项目开源不代表开发者有义务为您提供服务。
- 您的发言不应该具有攻击/冒犯性。
- 在提问前请先阅读[《提问的智慧》](https://github.com/ryanhanwu/How-To-Ask-Questions-The-Smart-Way/blob/main/README-zh_CN.md)。
- 若有必要，开发者有权对您停止任何技术支持。
- 若有必要，开发者有权基于任何理由将您封禁。
- 我们将**尽可能**保证提前通知程序内部实现的程序内部的修改/删除，但无法做出任何保证。
- 我们将**尽可能**使更新程序更容易（例如在仅仅替换可执行文件的情况下即可完成升级）或明确的通知需要进行的更改。但我们也不能对此做出保证。

如果您对以上条目感到不适，您应该考虑停止使用本项目。

## 👨🏻‍💻贡献者

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://cubik65536.top"><img src="https://avatars.githubusercontent.com/u/72877496?v=4?s=100" width="100px;" alt="Qian Qian "Cubik""/><br /><sub><b>Qian Qian "Cubik"</b></sub></a><br /><a href="https://github.com/iXORTech/RemoteMC-Core/issues?q=author%3ACubik65536" title="Bug reports">🐛</a> <a href="https://github.com/iXORTech/RemoteMC-Core/commits?author=Cubik65536" title="Code">💻</a> <a href="https://github.com/iXORTech/RemoteMC-Core/commits?author=Cubik65536" title="Documentation">📖</a> <a href="#ideas-Cubik65536" title="Ideas, Planning, & Feedback">🤔</a> <a href="#infra-Cubik65536" title="Infrastructure (Hosting, Build-Tools, etc)">🚇</a> <a href="#maintenance-Cubik65536" title="Maintenance">🚧</a> <a href="#question-Cubik65536" title="Answering Questions">💬</a> <a href="https://github.com/iXORTech/RemoteMC-Core/pulls?q=is%3Apr+reviewed-by%3ACubik65536" title="Reviewed Pull Requests">👀</a> <a href="#tool-Cubik65536" title="Tools">🔧</a> <a href="#translation-Cubik65536" title="Translation">🌍</a> <a href="https://github.com/iXORTech/RemoteMC-Core/commits?author=Cubik65536" title="Tests">⚠️</a></td>
    </tr>
  </tbody>
  <tfoot>
    <tr>
      <td align="center" size="13px" colspan="7">
        <img src="https://raw.githubusercontent.com/all-contributors/all-contributors-cli/1b8533af435da9854653492b1327a23a4dbd0a10/assets/logo-small.svg">
          <a href="https://all-contributors.js.org/docs/en/bot/usage">Add your contributions</a>
        </img>
      </td>
    </tr>
  </tfoot>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

## 🎊 鸣谢

> <span style="font-size: 0.96em">**IntelliJ IDEA**</span><br/>功能强大，符合人体工程学的 JVM IDE

特别感谢 [JetBrains](https://www.jetbrains.com/?from=RemoteMC-Core) 为我们提供免费的，[IntelliJ IDEA](https://www.jetbrains.com/idea/?from=RemoteMC-Core) 等 IDE 的开源开发许可证

[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" height="96"/>](https://www.jetbrains.com/?from=RemoteMC-Core)
[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA.png" height="96"/>](https://www.jetbrains.com/idea/?from=RemoteMC-Core)

<sup>Copyright © 2000-2023 JetBrains s.r.o. JetBrains and the JetBrains logo are registered trademarks of JetBrains s.r.o.</sup>
<br/>
<sup>Copyright © 2023 JetBrains s.r.o. IntelliJ IDEA and the IntelliJ IDEA logo are registered trademarks of JetBrains s.r.o.</sup>

## 📜 协议&许可证

> **RemoteMC-Core 使用 [AGPL-3.0-or-later 协议](license-translations/LICENSE-zh.md)（[原文](LICENSE)）作为软件分发许可证。<br/>
用户可以更改，再分发本开源代码，进行商业使用，但必须遵守 [AGPL-3.0-or-later 协议](license-translations/LICENSE-zh.md)（[原文](LICENSE)）内容**

但同时，用户不得：
- 将该项目用于违反**美国**，**加拿大**，**中华人民共和国**以及用户所处国家的法律的用途


- 当您在部署本项目时：
  - 您应当保留在网页底部或者命令行提示处的 `由 iXOR Technology 强力驱动。` 字样，并且 `iXOR Technology` 应当始终链接到我们的网站（<https://ixor.tech>）


- 对于 `RemoteMC-Core` 的衍生项目：
  - 不管您是在维护一个我们项目的分支抑或是直接使用了我们的代码，您的项目应当也使用 [AGPL-3.0-or-later 协议](license-translations/LICENSE-zh.md)（[原文](LICENSE)）作为软件分发许可证。
  - 如果您在维护一个 `RemoteMC-Core` 的分支，您可以在网页底部或者命令行提示处使用 `由 iXOR Technology 强力驱动，由 [您的名字] 更改。` 字样。否则必须保留 `由 iXOR Technology 强力驱动。` 字样。
  - 如果你在使用 `RemoteMC` 已发布的代码，不管您是否更改了代码，您都需要在您的程序描述中**明确**提及使用了本项目代码，并附上本项目的链接。您不得扭曲或隐藏本项目免费且开源的事实。
    - 本项目链接
      - https://github.com/iXORTech/RemoteMC-Core


``` text
RemoteMC-Core - RemoteMC 系列处理与API核心
版权所有 (C) 2022-2023 iXOR Technology, Cubik65536 以及 所有贡献者。

本程序为自由软件：在自由软件基金会发布的 GNU Affero 通用公共许可证的约束下，
你可以对其进行再分发及修改。许可证版本为第三版或（你可选的）后续版本。

我们希望发布的这款程序有用，但其不带任何担保；甚至不默认保证它有经济价值和适
合特定用途。详情参见 GNU Affero 通用公共许可证。

你理当已收到一份 GNU Affero 通用公共许可证的副本。如果你没有收到它，请查阅
<http://www.gnu.org/licenses/>。
```
