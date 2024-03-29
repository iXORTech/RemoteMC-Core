**English** | [中文](README-zh.md)

> [!IMPORTANT]
> The development of this project (RemoteMC series in general) will be suspended
> due to QQ's policy going against bots with designs that RemoteMC uses and
> due to a lost of interest from the developer.

<h1 align="center">RemoteMC-Core</h1>

<p align="center"> 
  <b>Processing and API Core of RemoteMC Series </b>
</p>

<p align="center">
  <a href="LICENSE">
    <img src="https://img.shields.io/badge/License-AGPL--3.0--or--later-important?style=for-the-badge" />
  </a>
</p>

RemoteMC Series Software allows the users to use a RemoteMC-Core to connect multiple MC servers (RemoteMC-MCDR client), a QQ Client
(RemoteMC-QQ client), and eventually other chatbot modules. You can combine the modules you need to form the chat message
synchronization and MC server remote control network you need. The following picture shows a simple network configuration.

> **NOTE:** The features related to the MCDR component are done right now. But other parts related to other components (e.g. chatbots) are still in WIP.

<p align="center">
  <img src="https://img.cubik65536.top/file/Cubik-Image-Hosting-Storage/RemoteMC-Core-Architecture-Design-en.png" 
  height="750"/>
</p>

## 🔉 Announcement

This announcement is made due to the [disharmony event of Mirai](https://github.com/mamoe/mirai/issues/850).

  - This project has been developed for the hobby of developers and contributors. So,
    1. we might add/change some features or even remove some features just because we are interested in making these changes
    2. and posting any offensive speech/content/behavior that expresses the opposition to the points above will result in a **BAN FOREVER**
  - The fact that this project is open-sourced does not mean that the developer is obliged to provide you any services.
  - Your speech should not be offensive.
  - Read the [How To Ask Questions The Smart Way](http://www.catb.org/~esr/faqs/smart-questions.html) before you ask any questions.
  - If necessary, the developer reserves the right to stop any technical support for you.
  - If necessary, the developer reserves the right of **BANNING** you for any reason.
  - We will try **our best** to notify in beforehand about the modification/removal of the internal implementation of the program, but we cannot guarantee anything.
  - We will try **our best** to make updating the program easier (for example, no need to change anything other than executable binary) or clearly notify other changes needed. But this is not guaranteed either.

If you feel discomfort about the announcement above, you should consider stopping using this project.

## 👨🏻‍💻Contributors

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

## 🎊 Thanks

> <span style="font-size: 0.96em">**IntelliJ IDEA**</span><br/>Capable and Ergonomic IDE for JVM

Special Thanks to [JetBrains](https://www.jetbrains.com/?from=RemoteMC-Core) for providing us free Licenses for Open Source Development for IDEs such as [IntelliJ IDEA](https://www.jetbrains.com/idea/?from=RemoteMC-Core)

[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/jb_beam.png" height="96"/>](https://www.jetbrains.com/?from=RemoteMC-Core)
[<img src="https://resources.jetbrains.com/storage/products/company/brand/logos/IntelliJ_IDEA.png" height="96"/>](https://www.jetbrains.com/idea/?from=RemoteMC-Core)

<sup>Copyright © 2000-2023 JetBrains s.r.o. JetBrains and the JetBrains logo are registered trademarks of JetBrains s.r.o.</sup>
<br/>
<sup>Copyright © 2023 JetBrains s.r.o. IntelliJ IDEA and the IntelliJ IDEA logo are registered trademarks of JetBrains s.r.o.</sup>

## 📜 License

> **RemoteMC-Core uses [AGPL-3.0-or-later License](LICENSE) as software license.<br/>
Users can modify and redistribute the source code and use it for commercial proposes, but you should respect the content of [AGPL-3.0-or-later License](LICENSE)**

- Use this project for any proposes that violate the laws of **The United States of America**, **Canada**, **The People's Republic of China**, and the country where the user is at.


- When you deploy this project:
  - You should keep the `Powered by iXOR Technology with 💗.` text on the bottom of the webpages and in the command line prompt. And the `iXOR Technology` should always be linked to our website (https://ixor.tech).


- For derived projects of `RemoteMC-Core`:
  - Your project should also be open-sourced under [AGPL-3.0-or-later License](LICENSE). Whether it is a fork of our project or another project that use our code.
  - If you maintain a fork of `RemoteMC-Core`, you can use the `Powered by iXOR Technology with 💗, Modified by [your name].` text on the bottom of the webpages and in the command line prompt. And the `iXOR Technology` should always be linked to our website (https://ixor.tech). Otherwise, `Powered by iXOR Technology with 💗.` text should be kept.
  - If you use `RemoteMC`'s released source code for another project, whether the code have been changed or not, then you need to **clearly** mention the usage of our code and a URL to our repository in any part of the software description. The fact that our code is free and open-source must not be distorted or hidden.
      - URL to repositories：
          - https://github.com/iXORTech/RemoteMC-Core


``` text
RemoteMC-Core - Processing and API Core of RemoteMC Series
Copyright (C) 2022-2023 iXOR Technology, Cubik65536, and contributors.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
```
