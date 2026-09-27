Desert Stormfront 复刻工程
把商业 RTS《Desert Stormfront》（NobleMaster，Java/libGDX）的混淆字节码逆向还原为可读、可编译、可运行、可扩展的源码工程，并配套 26 份工程文档。

当前状态：编译 exit=0 errors=0 classes=408；run\dev.ps1 test 回归门禁 20/20 通过（含编译退出码断言、画面绘制、Mod 数据格式契约、本地化解码、玩法事件、注释覆盖率与脚本引导一致性检查）。

1. 快速开始
powershell -File run\dev.ps1 build     :: 编译 -> appbuild\classes
powershell -File run\dev.ps1 test      :: 一键回归门禁（8 项，含画面检查）
powershell -File run\dev.ps1 run       :: 启动游戏
powershell -File run\dev.ps1 package   :: 打包 jar + 依赖 + data
powershell -File run\dev.ps1 exe       :: 打包 Windows exe（自带运行时）
powershell -File run\dev.ps1 env       :: 环境自检
启动市场参数必须用 -mNobleMaster（或 -lite）。-mReview 会选中 ExpiringLicense(构建日期, 90天)，构建日期是 2013 年、试用早已过期，游戏将永远停在只做黑色清屏的 BootScreen —— 表现为「窗口正常但全黑、日志无任何异常」。根因见 24_黑屏根因与修复.md。

2. 技术栈
层面	结论
引擎	libGDX + LWJGL 2.9.2 桌面后端（已实测：OpenGL 4.6 正常）
运行时	工程自带 JDK 17（Temurin 17.0.19）；原游戏自带 JRE 1.6
构建	无 Gradle/Maven/Ant，离线手工 classpath（run\*.ps1）
平台	Windows（PowerShell 脚本体系；脚本一律 ASCII-only，规避 GBK 解码坑）
3. 目录
src/com/desertstormfront/   业务层 335 类（ai 82 / ui 55 / game 48 / screen 35 / command 25 / session 17 / world 14 / desktop 14 / mod 10 / 其他 35）
src/com/noblemaster/lib/    基础层 61 类（io / log / data / net.match / license / market）
run/                        构建、运行、诊断、回归脚本
mod/                        数据外置：units.json / new_units.json / ruleset.json / plugins.txt / data/
*.md                        工程文档（00~25，见下）
源码 396 个 .java（380 原始还原类 + 10 个 mod/ 扩展类 + 6 个 test/ 测试类）→ 408 个 .class（含内部类）。

4. 文档索引
类别	文档
任务与范围	00 任务书与执行计划 · 01 技术调研 · 02 复刻范围 · 03 资产清单
逆向分析	04 架构分析 · 05 核心流程图 · 06 关键算法 · 07 模块函数映射 · 13 AI 函数映射 · 17 UnitType 字段语义
设计与规范	08 架构设计 · 09 开发规范 · 14 AI 模块架构
验证与交付	10 测试用例集 · 11 功能对齐 · 12 编译运行指南 · 22 事件流验证
扩展能力	15 自定义开发可行性 · 16 数据外置 v1~v3 · 18 自定义规则集 v4 · 19 资源 Mod v5 + 事件总线 v6 · 20 玩法事件 · 21 command 映射与热重载 v7
问题与计划	23 黑屏问题诊断（已被 24 结案） · 24 黑屏根因与修复 · 25 方法级去混淆缺口报告
标准化与二开	26 完全标准化与二次开发改造方案（达标定义 · 根因记录 · 批次 SOP · 二开能力矩阵 · 第 7 节：验收结论与后续非命名缺口执行顺序）
5. 已具备的扩展能力
能力	状态
v1 数值覆盖 / v2 通用字段覆盖 / v3 新增单位	✅（mod/units.json、mod/new_units.json）
v4 自定义规则集（阵营/地形/单位/槽位/造价）	✅（mod/ruleset.json）
v5 资源 Mod（贴图/音频/地图/本地化整文件覆盖）	✅（mod/data/，单点打在 GameFile）
v6 事件总线 + 插件钩子 + 玩法级事件	✅（mod/plugins.txt）
v7 热重载（幂等，ModLoader.reload()）	✅
v8 语义 JSON 键（health/speed/damage…，旧混淆键仍兼容）	✅（ModLoader.ALIASES，门禁 mod.fieldAliases）
5.1 去混淆工具链（2026-09 新增）
工具	用途
tools\deobf\RenameAst.java	符号级（编译器解析）批量改名：方法（继承族 + 全接收者 + 真作用域）与字段（遮蔽校验 → 必要时限定为 this.x），描述符感知，默认 dry-run
run\deobf-rename.ps1	一键包装：-Map <tsv> [-Owner <fqn>] [-Apply]，产出 build\rename-report.tsv 供审计
run\deobf-map-skeleton.ps1	从 javap 产出描述符精确的映射骨架；支持 -Package（整包合并成一份映射）与 -Fields（字段行，kind F），省去手写 JVM 描述符（批次提速关键）
run\deobf-audit.ps1	混淆率量化（口径：1–2 个字母，大小写都算）
run\comment-audit.ps1	注释覆盖率度量（口径：行首为行注释/块注释标记，或处于块注释内；内联块注释不计）。已纳入 dev.ps1 test 作 docs.comments 棘轮门禁（地板 3.5%）
git 基线 / tag	每批改名独立提交，可整体回滚
6. 待办
方法级去混淆：2696 个方法中 385 个仍为混淆名（14.3%）；口径与批次记录见 26。screen 包 91% → 2%、command 100% → 2%、map/config/mod/patch 已归零。已完成 UserConfig（53）、GameConfig（42）、AbstractGameInfo（30）、Unit（40 声明 / 1075 处 / 90 文件）、UnitType（26 / 286 处）、GameCommand（5 条映射 → 68 声明，覆盖全部 21 个指令子类）、UnitCommander（79 条映射 → 143 声明 / 486 处，command 包 100% → 10%）+ NetworkedUnitCommander（13）、TerrainGrid（42 条映射 → 46 声明 / 304 处，world 包 88% → 53%）、World（39 条映射 → 348 处 / 47 文件，game 包 78% → 71%）、AIContext（31 → 233 处 / 34 文件，ai 包 96% → 85%）、MapDefinition（18 → 57 声明，含 Tsf/Dsf/ModRuleset，map 包 100% → 6%）、Player（27 → 270 处 / 51 文件）、FogOfWar（18 → 102 处 / 12 文件）、UnitTypeSlots（14 → 122 处 / 18 文件，槽位语义由两个规则集交叉比对得出）、UnitType 剩余 15 项（→ 92 处 / 28 文件，矩阵方向由四个调用点互证）、config 包字段 115 项（run\map-config-fields.tsv，逐字段对应 user.config 键 + 语义 getter）、command 包字段 59 项（run\map-command-fields.tsv）等批次；2026-09-19 起新增方法批次 ui.Widget+ui.Label（62）、ui.hud.GuiAssets+TutorialHintBox+VectorTextRenderer（33）、ai.intent.IntentGroup+session 契约（79），与字段批次 ui.VectorTextRenderer+GuiAssets（28）、screen.OptionsScreen（21）— 详见 26 批次记录表。678 处 / 52 文件，ui 包 86% → 68%）。最近批次（见 27 §5.38/§5.39）：world 剩余方法 + Neighbor 枚举常量（42 方法 + 10 常量 → 346 处 / 35 文件，方向名以 game.model.Direction 为轴约定证据，world 包方法 34% → 2%）、graphics.table 全包归零（20 + 23 → 159 处）、lib.i18n + lib.data（43 + 10 → 652 处）、lib.io + lib.util + lib.log（52 + 8 → 1069 处 / 106 文件）、lib.net.match（46 + 22 → 324 处 / 19 文件，包 92% → 0%，字段语义由 MatchMakingClientTest 日志与 GameClient 的连接选择双证据定名）、ui 渲染基元（SpriteBatch/GlBuffer/FrameBuffer/ClipRect，30 + 22 → 376 处 / 15 文件）、ai 全包（190 + 25 → 864 处 / 75 文件，HTN 规划与行为树两套同构节点一次定名，67% → 0%）、game 可确认子集（39 → 167 处 / 26 文件）、lib.license + lib.script（26 → 52 处 / 13 文件）。每批均过 run\dev.ps1 test（当前 20/20）。 字段级改名已落地：Unit/World/Player/TerrainGrid/AIContext 共 63 个字段 + 长尾 TsfRuleset/DsfRuleset 共 58 个字段（规则集的名字直接写在内联构造里，如 new UnitType(0, "General[i18n]: General", …)），再经 command（59）+ config（115）+ ui.VectorTextRenderer/GuiAssets（28）+ screen.OptionsScreen（21）+ world（Neighbor 10 个常量）+ graphics.table（23）+ lib.i18n/lib.data（10）+ lib.io/lib.util（8）+ lib.net.match（22）+ ui 渲染基元（22）+ ai（25）字段批次后，字段混淆率 87.2% → 47.1%（849 / 1802）；UnitType 为试点批次，并原子同步了 ModLoader.ALIASES 与 ModFieldAliasTest（由门禁 mod.fieldAliases 守护）。command/config/map/mod 字段已归零，剩余集中在无访问器的 UI 控件引用（screen / ui 大部分类）。 Unit 批次已于 2026-09-18 重新落地，无需外部 IDE/LSP：本仓自带 tools\deobf\RenameAst.java（基于 javac 符号解析，描述符感知，自动处理实例接收者 / 继承族 / 作用域），配套 run\deobf-rename.ps1 一键执行且默认 dry-run。原回退原因见 25 第 7 节，结论已被该工具取代。下一批队列见 26 第 3 节 P2。
对局内人工确认：玩法事件在真实对战中的触发时机与顺序（冒烟只到主菜单）。
地图加载偶发错误：Trying to host within a unit that cannot host（已被捕获，非致命），见 24 第 6 节。
原版/复刻版同场景同输入的逐帧数值比对。
