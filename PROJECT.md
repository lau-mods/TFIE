# Project

Status: active

Project ID: tfie-0.1.0
Mod ID: tfie

## Platform

Minecraft: 1.21.1
NeoForge: 21.1.267
Java: 21

## Purpose

TFIE 0.1.0を、TerraFirmaCraftの素材・加工・栽培体系とImmersive Engineeringの機械・電力体系を接続するNeoForge Modとして実装する。本書の仕様基準日は2026年11月23日とする。TFC Metallum Overhaulを必須依存に加え、Minecraft 1.21.1向けのReleaseとBeta、および同じMinecraft版の将来更新を保守対象とする。Minecraftの別バージョンとAlphaは対象に含めない。

本書を製品仕様の正本とする。Featuresは機能の担当範囲と観測する受入条件を定義し、Global Requirementsは37章の詳細仕様を全文収録する。Constraintsは実装と検証に適用する開発上の制約を定義する。Featureに記した具体例だけに実装を限定せず、担当する詳細仕様の全表行、数量、ID、状態遷移、例外、削除対象、非追加指定を満たすことを完成条件とする。

実装担当は、この文書と公開されているModのコードだけで作業を進める。別の会話、質問番号、選択肢の内容、ChatGPTのPage、外部の仕様書、作成時の作業ファイルは必要としない。旧TFIEとCrossoverの参照は既存動作の確認に使用し、旧版への一括委任によって本書の詳細を省略しない。参照する上流リポジトリと固定版は詳細仕様第37章に列挙する。

`Status: active`は仕様が確定し計画対象となることを意味する。Modの実装、ビルド、GameTest、E2Eの完了を表すものではない。機能の採否に関する未決事項はなく、実装方法は対象版のコードと標準APIを確認して決定する。既決の機能を追加の設計意図質問によって再選択しない。

納品ファイル名は`project.md`とする。harnessのWorkspaceへ配置する際は、ルートに`PROJECT.md`という大文字の名前で設置する。harnessはこのファイル名を固定で読むため、小文字のまま配置しない。設置後は`PROJECT.md`を唯一の正本とし、大小文字の異なる2つの編集対象を併存させない。[harnessのファイルパス定義](https://github.com/lau-mods/modding-harness/blob/2bf8738b61927767bc15bfa5859ad9e3a6f53a81/src/core/paths.ts)

## Features

Feature IDとAC IDは固定識別子とする。後から題名や本文を修正しても既存IDを再採番しない。下表の詳細仕様の章番号は、この文書のGlobal Requirements内を参照する。各ACに明記するGameTestまたはE2Eへ、実装時に`tests/acceptance.json`で対応付ける。

| Feature | 対象 | 主に担当する詳細仕様 |
|---|---|---|
| F-001 | 対応環境と登録を実装する。 | 第2・4章を適用する。 |
| F-002 | 金属素材と共通素材データを整備する。 | 第6・30章を適用する。 |
| F-003 | Metal Pressと金型を実装する。 | 第7章を適用する。 |
| F-004 | 加熱炉の出力温度と確定処理を実装する。 | 第5章を適用する。 |
| F-005 | BriquetのArc製錬を実装する。 | 第8章を適用する。 |
| F-006 | Arc配合と8mode Blueprintを実装する。 | 第9・10章を適用する。 |
| F-007 | Blast FurnaceとImproved Blast Furnaceを有効化する。 | 第11章を適用する。 |
| F-008 | Alloy Kilnを実装する。 | 第12章を適用する。 |
| F-009 | ドリルヘッドを実装する。 | 第13章を適用する。 |
| F-010 | TFC金床加工とHigh Carbon Steel副産物を実装する。 | 第14章を適用する。 |
| F-011 | 一般クラフトを実装する。 | 第15章を適用する。 |
| F-012 | マルチブロック構造を実装する。 | 第16章を適用する。 |
| F-013 | 木材加工と処理木材を実装する。 | 第17章と第23章のAFC互換境界を適用する。 |
| F-014 | 麻の栽培を実装する。 | 第18章を適用する。 |
| F-015 | Garden Clocheによる作物栽培を実装する。 | 第19・22章を適用する。 |
| F-016 | Squeezerによる種子の圧搾を実装する。 | 第20・22章を適用する。 |
| F-017 | FermenterによるEthanol製造を実装する。 | 第21・22章を適用する。 |
| F-018 | 非金属加工と薬液を実装する。 | 第23章を適用する。 |
| F-019 | External HeaterとCrucibleを接続する。 | 第24章を適用する。 |
| F-020 | 回転動力による発電を接続する。 | 第25章を適用する。 |
| F-021 | Crateの収納制限を実装する。 | 第26章を適用する。 |
| F-022 | 流体と容器の互換を実装する。 | 第27章を適用する。 |
| F-023 | 除草剤の作用を実装する。 | 第28章を適用する。 |
| F-024 | 肥料の互換を実装する。 | 第29章を適用する。 |
| F-025 | サーバー設定を実装する。 | 第31章を適用する。 |
| F-026 | ゲーム内ガイドと表示を整備する。 | 第32章を適用する。 |
| F-027 | 削除・置換台帳を全経路へ適用する。 | 第33章を適用する。 |
| F-028 | データ定義・保存・互換性を整備する。 | 第34章を適用する。 |
| F-029 | 構成と加工を統合して受入検証する。 | 第2・35・36章を適用する。 |

第1章の対象範囲、第3章の用語と単位、第34章の保存・サーバー・データパック条件、第36章の受入条件は、関連するすべてのFeatureへ適用する。第35章は旧版との差を定義し、第37章は固定した参照資料を示す。Featureの主担当が別でも、使用する材料、機械、レシピ、表示に関係する他章の条件を省略しない。

### F-001: 対応環境と登録

#### Description

Minecraft 1.21.1、NeoForge 21.1.267、Java 21を基準としてTFIE 0.1.0を構成し、必須・任意の依存関係と独自登録物を定義する。必須構成はTFC、IE、TFCM、Patchouliとし、AFCとOverdimsの互換は依存宣言を伴わない条件付きレシピとして実装する。

本Featureは詳細仕様第2、4章を担当する。該当章の全表行、本文の条件、例外および参照する共通要求を合否対象とする。次のACで取り上げる具体例だけへ実装範囲を縮小しない。

#### Acceptance Criteria

##### AC-F001-001: 必須構成の起動と識別

Preconditions:
詳細仕様第2章の基準版を用い、TFC・IE・TFCM・Patchouliを導入した専用サーバーとクライアントを用意する。

Action:
新規ワールドを起動し、Mod情報、TFIEのレジストリ、サーバーの通常tickを検査する。必須Mod不足の構成は別の起動検査で確認する。

Expected Result:
Mod IDと名前空間はtfie、版は0.1.0となる。必須構成でゲーム状態を読み込み、専用サーバーにクライアント専用クラスの読込障害を生じない。必須Mod不足は該当依存を明示して起動を拒否する。GameTestの基準環境でレジストリと稼働状態を確認し、不足構成の起動ログを補助証拠として保存する。 詳細仕様第2、4章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F001-002: 任意構成と条件付き木材互換

Preconditions:
FirmaLife、Advanced TFC Tech Unofficial、AFC、Overdimsの有無を個別に切り替える実行構成を用意する。

Action:
各構成で新規ワールドを起動し、該当素材、レシピ、形成機能の有効状態を照合する。

Expected Result:
FirmaLifeとAdvanced TFC Tech Unofficialの対象連携だけが導入時に有効となる。AFCとOverdimsは未導入でも起動し、導入した木材の実在するIDだけを参照する。AFCとOverdimsを本番ビルド依存、Modメタデータの必須・任意依存、配布依存宣言へ含めない。メタデータの照合を実行構成の証拠へ添付する。 詳細仕様第2、4章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F001-003: 登録物と既存IDの利用

Preconditions:
基準構成を起動し、詳細仕様第4章の登録表を期待集合として用意する。

Action:
アイテム、ブロック、BlockEntity Type、Creative Tab、Data Componentの各レジストリを列挙し、アイテムスタックを生成する。

Expected Result:
第4章の登録集合とID、スタック上限が一致する。Iron・Steel HeadとHemp Seeds・FiberはIEの既存IDを使用し、金属元素・鉱脈・旧Block Moldを重複登録しない。Arc用Blueprintは最大1個、初期mode 1を持つ。 詳細仕様第2、4章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F001-004: Creative Tabと基本資源の表示

Preconditions:
en_usを使用するクライアントでTFIEを起動する。

Action:
TFIE Creative Tabを開き、Treated Lumberと両工具HeadをInventory、手持ち、落下アイテムとして撮影する。

Expected Result:
Tab名はen_usの表示名となり、アイコンはTreated Lumberとなる。両工具HeadとTreated Lumberに未解決の翻訳キー、missing texture、欠損モデル、表示の重なりを生じない。個別Head、Blueprint、金型、Hempの固有描画は各FeatureのACでも確認する。 詳細仕様第2、4章の該当する全表行・条件も合否対象とする。検証方式はE2Eとする。

### F-002: 金属素材と共通素材データ

#### Description

素材の標準出力元、形状別金属量、入力タグ、TFC熱データを統合する。TFC既存形状、FirmaLife固有金属の既存形状、TFCM追加金属・追加形状の区別を維持する。未知の金属や存在しない形状を自動追加しない。

本Featureは詳細仕様第6、30章を担当する。該当章の全表行、本文の条件、例外および参照する共通要求を合否対象とする。次のACで取り上げる具体例だけへ実装範囲を縮小しない。

#### Acceptance Criteria

##### AC-F002-001: 金属集合と標準出力

Preconditions:
詳細仕様第6章の金属・形状が有効な構成と、FirmaLife不在の構成を用意する。

Action:
金属対応表から全入力タグと標準出力IDを解決し、実在する形状を列挙する。

Expected Result:
最大集合はTFC 19種、TFCM 42種、FirmaLife 2種の計63金属となる。TFCのIngot専用9種にはPlate・Sheet・Rod・Wireを新設しない。TFC既存形状はTFC、Chromium・Stainless Steelの既存形状はFirmaLife、追加形状はTFCMを標準出力とし、未知の金属へ加工を自動生成しない。 詳細仕様第6、30章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F002-002: 形状別金属量と圧縮例外

Preconditions:
Ingot、Plate、Sheet、Rod、Wire、Plated Block、TFCM Nuggetを、詳細仕様第6章の量で登録した環境を用意する。

Action:
金属データとTFC Heatingによる回収量を照合し、TFIEが追加する加工の入出力量を集計する。

Expected Result:
Ingot 100mB、Plate 100mB、Sheet 200mB、Rod 50mB、Wire 25mBとなる。100mBのPlated Blockを9Ingotとみなす経路を追加しない。TFCMの10mB Nuggetと既存9対1変換の例外は上流の定義として維持し、TFIEの別経路へ拡張しない。 詳細仕様第6、30章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F002-003: 金属と鉱石の無効化

Preconditions:
TFCMの金属または鉱石を個別に無効化した構成を用意する。

Action:
レシピを読み込み、当該素材を使うMetal Press、Arc、Alloy Kiln、Headその他の対応経路を列挙する。

Expected Result:
無効・未登録の入力または出力を参照する対象レシピは読み込まれず、Recipe IDと理由がログに記録される。別金属、空入力、代替製法へ置換せず、残ったArc配合のmode番号も変更しない。 詳細仕様第6、30章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F002-004: 素材タグとTFC熱データの適用

Preconditions:
詳細仕様第30章の共通素材、燃料、非金属熱容量の全表を期待集合として用意する。

Action:
タグへ属する実アイテムを使ってTFCの燃焼・加熱を実行し、登録データと回収物を照合する。

Expected Result:
第30章の全行について対象タグ、燃焼温度・時間、熱容量が一致する。金属形状の熱データは第6章の量と一致し、PlateとSheetを同一量として処理しない。無関係な共通タグへWrought Ironを通常Ironの代用として追加しない。 詳細仕様第6、30章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

### F-003: Metal Pressと金型

#### Description

最大63金属のPlate・Sheet・Rod・Wire加工と、Sheet金型およびMolds Blueprintの製法を提供する。4形状以外のIE機能へ削除範囲を広げない。

本Featureは詳細仕様第7章を担当する。該当章の全表行、本文の条件、例外および参照する共通要求を合否対象とする。次のACで取り上げる具体例だけへ実装範囲を縮小しない。

#### Acceptance Criteria

##### AC-F003-001: 全形状の加工数量と費用

Preconditions:
対応金属と形状が有効なMetal Pressへ、該当金型と常温Ingotを入れる。IEの時間・電力倍率は1とする。

Action:
第7章の4形状の加工を、全有効金属について各1回実行する。

Expected Result:
Ingot1個からPlate1個、Ingot2個からSheet1個、Ingot1個からRod2個、Ingot1個からWire4個を生成する。全加工は2400FE・60ticksで、材料温度を要求せず金型を消費しない。全形状が実在する最大構成では252レシピとなる。 詳細仕様第7章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F003-002: 競合する標準配合の置換

Preconditions:
TFIEとIEのレシピを読み込み、各対象金属の4形状と、変更対象外のGear等を用意する。

Action:
同じ入力と金型からの候補を検索し、加工結果を確認する。

Expected Result:
変更対象のPlate・Rod・Wireは指定するTFIE標準出力へ一意に置き換わる。4形状に関係しないGear・Blaze Rod等の有効なIE加工は一律に削除されない。正規IDはtfie:metalpress/F/Mとなる。 詳細仕様第7章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F003-003: Molds Blueprintと金型の製作

Preconditions:
Wrought Iron Plate1個、Blue Dye3個、Paper3個を用意する。各金型の独立試行ごとにSteel Plate3個とWirecutter1個を用意する。

Action:
指定配置でMolds Blueprintを作り、Engineers WorkbenchでSheet・Plate・Rod・Wireの各金型を作る。

Expected Result:
BlueprintはIE既存アイテムでComponent値moldsを持つ。各金型はSteel Plate3個を消費して1個生成し、Wirecutterは耐久1を消費して返却される。Sheetだけがtfie:mold_sheetとなり、旧Block Moldの登録・製法を生じない。 詳細仕様第7章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F003-004: 金型の分類と表示

Preconditions:
全IE金型とtfie:mold_sheetを登録した環境を用意する。

Action:
tfie:moldsタグとTFC Item Sizeを確認し、金型を機械へ装着する。

Expected Result:
第7章の金型タグの全要素が一致し、TFC sizeはnormal、weightはmediumとなる。Sheet金型は最大スタック数1となり、4形状の用途を混同せず指定金型だけで対応加工が成立する。 詳細仕様第7章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F003-005: 金型と加工結果の描画

Preconditions:
en_usでMetal PressとEngineers Workbenchを表示し、4種類の金型と各出力形状を用意する。

Action:
Sheet金型、Plate金型、Molds Blueprint、加工中および加工後の表示を撮影する。

Expected Result:
SheetとPlateの名前・外観・用途を区別し、Sheet金型にmissing textureや欠損モデルを生じない。採用金型と実出力の表示が一致し、Block金型の廃止資源を表示しない。 詳細仕様第7章の該当する全表行・条件も合否対象とする。検証方式はE2Eとする。

### F-004: 加熱炉の出力温度と確定処理

#### Description

Arc Furnace、Blast Furnace、Improved Blast Furnace、Alloy Kilnの金属出力へTFC Heatを付与し、出力容量と材料消費を整合させる。Sawmillの段階別排出とClocheの通常抽選をこの炉用規則で置換しない。

本Featureは詳細仕様第5章を担当する。該当章の全表行、本文の条件、例外および参照する共通要求を合否対象とする。次のACで取り上げる具体例だけへ実装範囲を縮小しない。

#### Acceptance Criteria

##### AC-F004-001: 融点の90パーセントと通常冷却

Preconditions:
各対象炉へ採用レシピを設定し、出力金属のTFC融点・熱容量を読み込む。

Action:
金属を生成した瞬間の温度を取得し、出力欄に残して時間を進める。

Expected Result:
生成温度は登録融点×0.9となり、整数へ丸めない。Raw Iron Bloomは基準融点1535℃に対し1381.5℃となる。TFCの通常Heat Component、熱容量、冷却を使用し、Slag・Insulating Glassへ同温度を付けず、Metal Pressにもこの加熱を適用しない。 詳細仕様第5章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F004-002: 異温度スタックと出力容量

Preconditions:
対象炉の出力欄へ同一アイテムの異温度スタックを置き、空きがある状態と全枠が埋まる状態を用意する。

Action:
新しい金属出力の確定処理を実行する。

Expected Result:
温度を含むComponentが異なるスタックは合流せず、空き枠を使用する。空きがなければ待機し、既存出力を再加熱・平均化しない。同一Componentのスタックだけが通常の上限内で合流する。 詳細仕様第5章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F004-003: 全生成物の一括確定と待機

Preconditions:
主出力または副産物の所定枠だけが不足する処理を、4種の炉で開始する。Arcは通常6枠とSlag専用1枠を用いる。

Action:
出力不足のまま完了条件まで進め、待機を継続した後で空き容量を確保する。

Expected Result:
全生成物が所定枠へ収まるまで進捗と未消費材料を保持し、待機中に追加材料・FEを消費しない。容量確保後は1処理分を一度だけ確定する。部分消失、二重消費、出力複製、副産物の再抽選、Slagの通常枠への迂回を生じない。他の出力が成立する処理は継続する。 詳細仕様第5章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F004-004: Arc処理の保存と読込

Preconditions:
Arcに実行中の処理、出力待機処理、予約した主入力と添加材、異なる温度の既存出力を用意する。

Action:
チャンクを再読込し、別の試行でサーバーを保存・再起動する。

Expected Result:
Recipe ID、開始時の配合、進捗、予約量、Inventory、待機状態、Heat情報を保持し、経過時間に応じた通常冷却だけを反映する。Blueprintを消費せず、予約や出力を増減させない。 詳細仕様第5章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F004-005: Arcのレシピ再読込による待機

Preconditions:
開始済みArc処理があり、未消費材料と進捗が保存されている。

Action:
同IDの入出力・数量・費用を変更するデータパック、またはそのレシピを無効化するデータパックを再読込する。次に元定義を復旧する場合と機械を解体する場合を試す。

Expected Result:
旧処理は別配合へ転換せず、進捗と未消費材料を保持して追加消費を停止する。定義復旧後は元処理を再開し、解体時は入力を回収する。Blueprintの交換・取り外しは妨げない。数量変更後のレシピを旧予約へ黙って適用しない。 詳細仕様第5章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

### F-005: BriquetのArc製錬

#### Description

TFCMの33鉱石系列について最終圧縮物Briquetから標準Ingotを得るArc製錬を提供する。鉄3系列のArc出力はCast Ironとし、高炉のRaw Iron Bloomと区別する。

本Featureは詳細仕様第8章を担当する。該当章の全表行、本文の条件、例外および参照する共通要求を合否対象とする。次のACで取り上げる具体例だけへ実装範囲を縮小しない。

#### Acceptance Criteria

##### AC-F005-001: 全33系列の数量と加工費

Preconditions:
第8章の33系列が有効な構成で、各Briquet1個と稼働条件を満たすArcを用意する。

Action:
各系列をBlueprint未装着、mode1〜8のそれぞれで加工する。

Expected Result:
第8章の各標準Ingotを1個生成し、1回51200FE・100ticksとなる。Flux・添加材・Slagを要求または生成せず、金属出力は融点の90%となる。鉄3系列はCast Ironとなり、ChromiteはFirmaLife導入時だけChromiumを出力する。 詳細仕様第8章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F005-002: 圧縮前素材と標準製錬の禁止

Preconditions:
Powder、Pellet、Chunks、Dirty Dust、Raw Ore、原鉱石、金属Dustと、正規Briquetを用意する。

Action:
各素材をArcへ投入し、レシピの検索と加工開始を試す。

Expected Result:
鉱石製錬はBriquetだけが開始する。Powder・Pellet等の迂回製錬とNetherite Scrap製造を開始しない。合金用Ingot入力は別機能として維持する。TFCMのPowder4対Pellet1、Pellet5対Briquet1の圧縮・逆変換とTFC Heatingは変更しない。 詳細仕様第8章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F005-003: 非採用系列と条件不成立

Preconditions:
Cinnabar、Cryolite、Graphite、Sulfur、Boraxおよび無効にした鉱石系列を用意する。

Action:
TFIEのBriquet製錬レシピを列挙して該当入力を照合する。

Expected Result:
Briquetのない4系列へ新しいBriquetや製錬を追加しない。BoraxはBoron回収の対象に含める。無効系列のレシピは省き、空入力や別金属へ置換しない。 詳細仕様第8章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

### F-006: Arc配合と8mode Blueprint

#### Description

合金18レシピとSteel1レシピを固定modeで選択し、非消費Blueprintを既存主入力へ置いて競合を除く。Blueprintの製法、操作、描画、予約、保存、JEI表示までをこのFeatureの一部とする。BriquetとInsulating GlassはBlueprintなしでも処理する。

本Featureは詳細仕様第9、10章を担当する。該当章の全表行、本文の条件、例外および参照する共通要求を合否対象とする。次のACで取り上げる具体例だけへ実装範囲を縮小しない。

#### Acceptance Criteria

##### AC-F006-001: 全合金配合とSteelの確定値

Preconditions:
第9章の主入力・添加材・必要modeを満たし、電極3本・FE・Redstone条件を備えるArcを用意する。IE倍率は1とする。

Action:
合金18件とSteel1件の全行を1回ずつ加工し、消費・出力・FE・進捗を測定する。

Expected Result:
全19件の主入力と添加材の役割、必要数、出力数、費用、時間が第9章の表と一致する。Wrought Iron1個と#c:dusts/coal_coke1個はmode1でSteel1個とSlag1個、204800FE・400ticksとなる。Charcoal Powderでは製鋼しない。他の18配合に追加Flux・Slagを設けず、HastelloyはFirmaLifeを要する。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-002: Blueprintの製作と8mode循環

Preconditions:
#c:paper3個と#c:dyes/blue3個を用意する。

Action:
不定形クラフトで生成したBlueprintをホットバーで選択し、メインハンドで空中を8回右クリックする。

Expected Result:
tfie:arc_furnace_blueprintを1個生成し、初期mode1から2、3、4、5、6、7、8、1へ順に切り替わる。tfie:arc_furnace_modeはサーバー側で確定し同期される。専用GUI、空白段階、逆順切替、材料・FE・耐久消費を追加しない。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-003: ブロック操作の優先

Preconditions:
main handにBlueprintを持ち、通常操作で開くArc等のブロックを用意する。

Action:
通常のブロック右クリックと空中への右クリックを行う。

Expected Result:
成立したブロック側操作を優先し、炉を開いた操作ではmodeが変わらない。空中右クリックでは1段階だけ変わり、クライアントとサーバーの二重実行による2段階移動を生じない。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-004: 既存主入力枠と非消費

Preconditions:
Arcの主入力0〜11、添加材12〜15、通常出力16〜21、Slag22、電極23〜25を用意する。

Action:
通常クリックとShiftクリックで主入力へBlueprint1枚を出し入れし、搬入Capabilityからの自動挿入と2枚目の挿入も試す。

Expected Result:
Blueprintは主入力の1枠だけを占め、装着時の材料枠は最大11、未装着時は12となる。専用枠・別保存領域を追加しない。自動搬入と2枚目は拒否し、既存1枚の同枠交換は認める。Blueprintを材料走査・予約・消費に含めず、電極3本等のIE稼働条件を維持する。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-005: mode別の一意な候補選択

Preconditions:
合金18件とSteel1件を読み込み、全171組の組合せと、異なる登録順・添加材搬入順を用意する。

Action:
各modeと主入力から候補を照合し、複数配合分の添加材を同居させて新規処理を試す。

Expected Result:
第10章の固定表へ一致し、同一mode・主入力の候補重複は0件となる。mode導入前の13競合組は分離される。Copperはmode1 Brass、2 Bronze、3 Bismuth Bronze、4 Black Bronze、5 Constantanとなり、主入力をNickel等へ入れ替えて競合を回避しない。登録順・JEI順・搬入順で選択が変わらない。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-006: 材料不足と余剰添加材

Preconditions:
Copper9個、Zinc1個を用意する。別試行ではBismuth1個またはNickel1個を添加材へ追加する。

Action:
mode3でBismuth不足のまま稼働させ、その後Bismuthを追加する。mode1とmode5では余剰添加材を同居させて加工する。

Expected Result:
mode3はBismuthを待ち、Brassへ切り替わらない。追加後Bismuth Bronze5個を生成しCopper6個を残す。mode1はBrass10個を生成してBismuthを残す。mode5はConstantan2個を生成してCopper8個とZinc1個を残す。必要量以外の添加材を消費しない。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-007: 未装着と未割当modeの基本加工

Preconditions:
Blueprintなし、mode1〜8のArcをそれぞれ用意し、Briquet、Glass2個とWrought Iron Dust1個、合金材を用意する。

Action:
基本加工と合金・Steelの開始を試す。

Expected Result:
BriquetとInsulating Glassは未装着および全8modeで処理する。Insulating Glassは2個、51200FE・100ticksで、熱とSlagを付けない。未装着時とmode6〜8では合金・Steelを開始しない。Wrought Iron Dustを作る追加Crusher経路とDustのArc再製錬を追加しない。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-008: 主入力ごとの並列処理と数量予約

Preconditions:
mode1でCopperとGoldを別主入力へ配置し、必要な添加材を供給する。別試行では複数の主入力が共有する添加材を1処理分だけ置く。

Action:
複数処理を開始し、処理キューと予約数量を調べる。

Expected Result:
BrassとRose Goldは主入力枠ごとに選択され、材料・出力条件の範囲内で並列処理する。開始済み処理の予約を控除して次の必要数を判定し、同じ材料数量を二重予約しない。Blueprintを予約しない。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-009: 交換と取り外し後の開始済み配合

Preconditions:
Blueprintで選択した配合を開始し、処理中の材料を残す。

Action:
Blueprintを取り出し、modeを変更して再投入する。別試行でBlueprintだけを取り除き、さらに別試行で実材料を除去する。

Expected Result:
開始済み処理は元Recipe IDと配合を保持し、mode変更と未装着の条件を新規開始だけへ適用する。Blueprintだけの除去で元処理を別レシピへ変換しない。実材料の除去にはIE通常の中断を適用する。レシピ無効化による待機はF-004の条件に従う。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-010: 不正Blueprintと配合重複

Preconditions:
外部データ操作で複数Blueprintまたは範囲外modeを置く。別試行ではデータパックで同一mode・主入力へ重複配合を追加する。

Action:
既存処理がある状態で新規処理の開始を試し、原因を取り除く。

Expected Result:
複数枚は同modeでも新規開始を停止する。範囲外modeも停止理由を表示する。重複配合は登録順で選ばず、不整合のある主入力の開始を停止する。開始済み処理を消失させず、原因解消後は正規の新規処理判定へ戻る。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-011: Blueprintの保存と解体回収

Preconditions:
各modeのBlueprintをInventoryまたはArc主入力へ置く。処理中と待機中の状態も用意する。

Action:
保存・再起動、チャンク再読込、機械解体を順に試す。

Expected Result:
mode Componentと主入力内の配置を維持し、通常処理、待機、保存のいずれでもBlueprintを消費しない。解体時には実在するBlueprint1枚を通常入力と同様に排出し、別領域の残骸や複製を生じない。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F006-012: BlueprintとArc画面の表示

Preconditions:
en_usでBlueprintのmode1、5、8と不正状態、Arcの既存画面を用意する。

Action:
手持ち、ツールチップ、切替時の短い表示、既存主入力に置いた状態と停止理由を撮影する。

Expected Result:
図面としての外観と現在modeを表示し、文字の欠けや翻訳キーの露出を生じない。設定用GUIと専用スロットは存在せず、材料用主入力にBlueprintが1枚表示される。停止理由の表示が観測状態と一致する。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はE2Eとする。

##### AC-F006-013: JEIの非消費mode表示

Preconditions:
JEIを任意導入し、全有効Arcレシピを読み込む。

Action:
合金、Steel、Briquet、Insulating Glassの各表示を開く。

Expected Result:
合金とSteelは対応modeのBlueprintを非消費触媒として示す。BriquetとInsulating GlassへBlueprint必須の表示を付けない。数量・入出力・カテゴリは実レシピと一致し、削除済みArcレシピを表示しない。 詳細仕様第9、10章の該当する全表行・条件も合否対象とする。検証方式はE2Eとする。

### F-007: Blast FurnaceとImproved Blast Furnace

#### Description

通常高炉と改良高炉を通常プレイで有効にし、鉄Briquet3系列からRaw Iron Bloomを製造する。単体CokeとCharcoalだけを燃料として採用する。TFIEが追加するWrought IronからSteelへの機械加工はArcの専用配合で扱い、両高炉には追加しない。

本Featureは詳細仕様第11章を担当する。該当章の全表行、本文の条件、例外および参照する共通要求を合否対象とする。次のACで取り上げる具体例だけへ実装範囲を縮小しない。

#### Acceptance Criteria

##### AC-F007-001: 鉄3系列のBloom製錬

Preconditions:
Hematite、Magnetite、LimoniteのBriquetを各1個用意し、通常高炉または改良高炉へ入れる。

Action:
各系列を許可燃料で1回加工する。

Expected Result:
各Briquet1個からtfc:raw_iron_bloom1個を生成し、Slag・Pig Ironを生成しない。基礎時間は1200ticks、金属量は100mB、基準生成温度は1381.5℃となる。改良高炉の予熱器による通常加速と搬入出は維持する。 詳細仕様第11章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F007-002: 燃料と非対応製錬の拒否

Preconditions:
単体Coke、単体Charcoal、各Block、通常石炭、Wrought Ironおよび鉄以外のBriquetを用意する。

Action:
両高炉で燃料判定と製錬開始を試す。

Expected Result:
燃料はCoke1個1200ticks、Charcoal1個300ticksだけを認める。Blockその他燃料と金属Block一括経路を追加せず、鉄3系列以外の製錬やWrought IronからSteelへの処理を行わない。 詳細仕様第11章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F007-003: 通常高炉と改良高炉の形成

Preconditions:
第15章の製法でBlast BrickとReinforced Blast Brickを得る。通常高炉は27Brick、改良高炉は27BrickとHopperを用意する。

Action:
第11章の全座標に配置し、形成起点からEngineers Hammerを使用する。改良高炉のHopperを誤位置または誤向きとする場合も試す。

Expected Result:
正規構造で形成し、通常高炉と改良高炉の起点(1,1,2)、マスター(1,1,1)を使用する。改良高炉のHopperは(1,3,1)、facing=down、enabled=trueとなる。構造不足や誤配置を正規形成として受理しない。 詳細仕様第11章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F007-004: BloomのTFC精製

Preconditions:
高炉出力のRaw Iron Bloomと、TFCの条件を満たす金床・Hammer・温度を用意する。

Action:
通常のTFC製法でRefined Iron Bloom、次にWrought Iron Ingotへ加工する。

Expected Result:
既存TFCの2段階精製を通り、ArcのCast Iron出力と混同しない。独自のSteel直結や精製省略レシピを追加しない。 詳細仕様第11章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F007-005: 高炉の形成表示

Preconditions:
en_usで通常高炉と改良高炉の形成前後とIE Manualの構造表示を用意する。

Action:
各構造、Hopper、稼働中の炉画面を撮影する。

Expected Result:
実形成とガイドの材料・位置・向きが一致する。モデル欠損、missing texture、画面の文字切れを生じず、Raw Iron Bloomを実出力として表示する。 詳細仕様第11章の該当する全表行・条件も合否対象とする。検証方式はE2Eとする。

### F-008: Alloy Kiln

#### Description

指定6種類・7経路の合金とInsulating GlassをAlloy Kilnへ実装する。2入力、通常かまど燃料、基礎200ticksの処理とし、Blueprintを要求しない。

本Featureは詳細仕様第12章を担当する。該当章の全表行、本文の条件、例外および参照する共通要求を合否対象とする。次のACで取り上げる具体例だけへ実装範囲を縮小しない。

#### Acceptance Criteria

##### AC-F008-001: 指定7合金経路の加工

Preconditions:
Alloy Kilnへ第12章の入力を用意し、通常かまど燃料を供給する。

Action:
Brass、Bronze、Constantan、Electrum、Invar、Andesite AlloyのZinc経路・Tin経路を各1回加工する。

Expected Result:
Copper9+Zinc1→Brass10、Copper9+Tin1→Bronze10、Copper1+Nickel1→Constantan2、Gold1+Silver1→Electrum2、Wrought Iron2+Nickel1→Invar3、Loose Andesite1+ZincまたはTin Nugget1→Andesite Alloy1となる。各200ticks、Blueprint不要、金属出力は融点×0.9となる。 詳細仕様第12章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F008-002: Insulating Glassと旧配合の排除

Preconditions:
Glass2個とTFCM Wrought Iron Dust1個を用意する。別試行ではRose Gold、Manyullynその他の非採用配合を用意する。

Action:
窯で連続加工し、直前レシピのキャッシュが存在する状態でも候補を照合する。

Expected Result:
Insulating Glass2個を200ticksで生成し、熱を付けない。同じ入力のIE旧比率を残さず、Rose Gold・Manyullynを無効化し、Sterling Silver・Osmiridium・3成分以上の合金を新設しない。キャッシュの有無で誤配合が成立しない。 詳細仕様第12章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F008-003: Alloy Kilnの形成と運用

Preconditions:
通常クラフトで得たAlloy Brick8個とEngineers Hammerを用意する。

Action:
X・Y・Z各0〜1の2×2×2に配置し、起点(1,1,1)から形成し、採用配合を運転する。

Expected Result:
マスター(0,0,1)の通常構造として形成する。通常プレイで製作と加工が成立し、燃料を高炉専用のCoke・Charcoalだけへ誤って制限しない。出力待機と熱の条件はF-004に従う。 詳細仕様第12章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F008-004: Alloy Kilnの描画

Preconditions:
en_usで形成済みAlloy Kilnと、ガイド・レシピ表示を用意する。

Action:
形成、稼働、合金とInsulating Glassの表示を撮影する。

Expected Result:
形成表示と実構造が一致し、主出力の種類・数量が採用配合と一致する。Blueprint専用表示や欠損モデルを生じない。 詳細仕様第12章の該当する全表行・条件も合否対象とする。検証方式はE2Eとする。

### F-009: ドリルヘッド

#### Description

IE既存2種とTFIE7種の計9種のHeadについて、性能、TFC金床製法、通常Minecraft金床の修理、モデル・名称を統合する。通常Minecraft金床の入手経路とTFC金床修理は追加しない。

本Featureは詳細仕様第13章を担当する。該当章の全表行、本文の条件、例外および参照する共通要求を合否対象とする。次のACで取り上げる具体例だけへ実装範囲を縮小しない。

#### Acceptance Criteria

##### AC-F009-001: 9種の性能と採掘範囲

Preconditions:
第13章のHead9種、IE Drill、採取条件を満たすブロックと満たさないブロックを用意する。

Action:
各Headを装着して各面の中央・端を採掘し、速度・攻撃値・最大耐久・採掘tierと候補範囲を取得する。

Expected Result:
第13章の性能表全9行と一致する。Iron/Invarは2×2×1、Steel/Black Steel/Titaniumは3×3×1、Blue Steel/Red Steel/Netherite/Tungsten Steelは5×5×1となる。追加採掘はプレイヤーの採取可否、IE有効対象、主対象の破壊進捗の0.4倍以上という条件を満たし、偶数幅は面内の命中位置に従う。 詳細仕様第13章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F009-002: 9種の金床製法

Preconditions:
各Headの対応Double Sheet1個と、金床tier3/4/5/6/6/3/5/6/7を材質順に用意する。材料はworkingTemperature以上とする。

Action:
TFC金床の目標位置と、最後の3操作bend、draw、hitを満たして完成させる。低tierと低温の試行も行う。

Expected Result:
対応Headを1個生成し、全9レシピはapply_bonus=falseとなる。IronはWrought Iron Double Sheetを使用する。低tier・低温では成立せず、IE標準のIron/Steel Headクラフトと独自溶接製法を追加しない。無効化したTFCM金属のHead製法は読み込まれない。 詳細仕様第13章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F009-003: 材料修理と回復量

Preconditions:
損傷した9種のHeadと対応Ingotを、通常Minecraft金床の左右欄へ置く。IronではWrought Iron、Cast Iron、通常Ironを個別に試す。

Action:
1個の修理と複数個の修理を行い、回復量と消費材数を測る。

Expected Result:
各Ingotの回復量は残損傷と最大耐久÷4の整数部の小さい方となる。Tungsten Steelでは最大10156、Netheriteでは8125となり、第13章の全9行に一致する。必要数だけ消費し、IronはWrought Ironだけを受け入れる。TFC金床修理とMinecraft金床の追加入手経路を設けない。 詳細仕様第13章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F009-004: 修理経験値と名称変更

Preconditions:
複数Ingotを要するHeadを用意し、元のカスタム名なし・ありの状態を分ける。

Action:
名称を維持する、空欄で削除する、新しい名称へ変更する修理を実行する。

Expected Result:
最後に消費するIngotによる回復量rだけからmax(1,floor(r/200))レベルを算出し、各材料分を合算しない。既存名の削除は基礎費用へ5レベルを加える。新しい名前への変更は5レベルを加え、変更前にカスタム名があった場合だけさらに2レベルを加える。同名維持には加算しない。 詳細仕様第13章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F009-005: Headの描画と材質表示

Preconditions:
en_usで9種のHeadと装着済みIE Drillを用意する。

Action:
Inventory、手持ち、ツールチップ、装着画面を撮影する。

Expected Result:
各材質を名前と既存資源に基づく外観で区別し、欠損モデル・missing texture・未解決翻訳キーを生じない。最大スタック数1と損傷表示が実状態に一致し、TFCMの追加4種もIE Drillの装着表示に反映される。 詳細仕様第13章の該当する全表行・条件も合否対象とする。検証方式はE2Eとする。

### F-010: TFC金床加工とHigh Carbon Steel副産物

#### Description

非Drill9件をTFC金床へ移行し、通常TFCのtier・温度・操作・品質条件を維持する。Pig IronからHigh Carbon Steelへのプレイヤー通常加工が成功した時だけ、IE Slag1個を副産物として追加する。

本Featureは詳細仕様第14章を担当する。該当章の全表行、本文の条件、例外および参照する共通要求を合否対象とする。次のACで取り上げる具体例だけへ実装範囲を縮小しない。

#### Acceptance Criteria

##### AC-F010-001: 非Drill9件の製法

Preconditions:
Faraday防具4部位、Empty Casing、Gun Barrel、Hammer Head、Wirecutter Head、Revolver Bayonetについて第14章の材料・tierを用意する。

Action:
TFC金床の選択製品、目標位置、各レシピの操作規則を満たして加工する。

Expected Result:
第14章の全9行の入力、出力数、tier、規則、apply_bonusへ一致する。Empty CasingだけはCopper Ingot1個から2個となり、他は各1個を生成する。Faraday4部位は各Steel Double Sheet1個を消費し、選択した部位を生成する。独自溶接を追加せず、対応する標準作業台製法を無効化する。 詳細仕様第14章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F010-002: TFC操作規則と温度の継承

Preconditions:
各レシピへ正しいHammer、誤ったHammer、workingTemperature境界の材料を用意する。

Action:
Hammer欄・主手・副手の優先順で操作し、目標位置と直近3操作を変えて完成を試す。

Expected Result:
#c:tools/hammerを通常の順で判定し、受理操作ごとに耐久を消費する。操作位置変化、any/not_last/last等の判定、0未満・150超の破壊、最初の負操作の例外、TFC設定の許容誤差が第14章と一致する。完成品にHeatがあれば通常のsetTemperatureIfWarmerで継承し、金床へ一律90%温度を付けない。 詳細仕様第14章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F010-003: 鍛造品質の対象と境界

Preconditions:
Faraday4種とapply_bonus=falseの非Drill5種・Head9種を用意し、TFC標準品質設定を使用する。

Action:
最小操作数に対する比rが1.5、2、5、10の前後となる通常プレイヤー加工を実行する。

Expected Result:
Faraday4種だけが新規品質評価を行う。perfectはr<1.5、expertは1.5以上2未満、wellは2以上5未満、modestは5以上10未満、noneは10以上となる。TFC設定を固定上書きせず、第14章のComponent・作者・補正の範囲を維持する。部品の品質をIE Drill本体へ自動反映する機能を追加しない。 詳細仕様第14章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F010-004: High Carbon Steel成功時のSlag

Preconditions:
tfc:anvil/metal/ingot/high_carbon_steelを選択し、Pig Iron1個、適正温度、Hammerを用意する。プレイヤーInventoryに空きがある場合と満杯の場合を分ける。

Action:
最後の3操作をhitとして目標位置を満たし、プレイヤーの通常金床加工を1回成功させる。

Expected Result:
TFC既存のHigh Carbon Steel主出力に加えてimmersiveengineering:slag1個を確率100%で生成する。先にプレイヤーInventoryへ入れ、入らない残数だけを金床位置へ排出する。加工1回で1個を超えて発生せず消失しない。 詳細仕様第14章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

##### AC-F010-005: Slagの対象外工程

Preconditions:
加工途中、失敗、取り出しだけ、High Carbon SteelからSteel、他の高炭素鋼、機械・遠隔金床の各試行を用意する。

Action:
それぞれの操作を行い、副産物を集計する。

Expected Result:
対象外工程からSlagを追加しない。判定IDに旧tfc:anvil/high_carbon_steel_ingotを使用せず、Pig IronからHigh Carbon Steelへの現行レシピの正常完了だけを検出する。Arc製鋼のSlagは別の機械出力として維持する。 詳細仕様第14章の該当する全表行・条件も合否対象とする。検証方式はGameTestとする。

### F-011: 一般クラフト

#### Description

Global Requirements第15章の一般クラフトを実装する。同章の全表行、レシピID、配置、入力の固定IDとタグの区別、数量、出力Component、例外条件、非追加指定、および関連する第33章の削除台帳も合否対象とする。通常クラフトはFEと加工時間を要求しない。第7章の金型、第14章の金床加工、第17章の処理木材製法はそれぞれのFeatureで扱い、同じ製法を別IDで重複追加しない。

#### Acceptance Criteria

##### AC-F011-001: 一般クラフトの配置と数量

Preconditions:
必須Modを導入し、第15章の「発電と電気設備」「搬送と建築設備」「工具と追加部品」「布と繊維を使う製品」の各行について、記載された入力数を作業台に用意する。

Action:
各行の配置でクラフトし、定形レシピの枠内移動と左右反転も試す。材料が1個不足する場合、別材料へ交換する場合、配置を崩す場合も照合する。

Expected Result:
全行が記載された材料条件、配置、消費数、出力数で成立する。例えばBasic EngineeringはWrought Iron Ingot4個とTreated Wood4個から4個、ConveyorはLeather3個・Wrought Iron Ingot2個・Redstone Dust1個から8個、CushionはHemp Fabric8個から3個となる。Minecraft標準の枠内移動と左右反転の扱いを維持し、必要な材料・数量・配置を満たさない入力を受け付けない。全表行の固定IDとタグを取り違えず、FE、待機時間、加熱条件を追加しない。検証方式はGameTestとする。

##### AC-F011-002: Wrought Ironによる材料置換

Preconditions:
第15章でWrought Ironを指定する一般クラフトについて、Wrought Iron、通常Iron、Cast IronのIngot・Rod・Plateを用意する。第15章でIE標準を維持するComponents BlueprintとJerrycanも用意する。

Action:
一般クラフトの対応する材料枠へ各金属を順に投入する。特にHatchのPlate2個とRod2個を個別に交換し、タグの登録内容も確認する。

Expected Result:
Wrought Iron指定のIngot・Rod・Plateはそれぞれ `#c:ingots/wrought_iron`、`#c:rods/wrought_iron`、`#c:plates/wrought_iron` を使用し、通常Iron・Cast Ironとの選択入力を設けない。HatchではPlateとRodの両方がWrought Ironである場合だけ成立する。この対応のためにWrought Ironを通常Ironの共通タグへ追加しない。Components BlueprintとJerrycanに定めた通常Ironの条件は維持する。検証方式はGameTestとする。

##### AC-F011-003: 作業台に用いる処理木材

Preconditions:
Treated Wood Lumber、Treated Slab、Treated Wood Rod、Crafting Table、Treated Fence、Wrought Iron Ingotを用意する。

Action:
IE Crafting Tableを `['sss','rcr','r r']`、Engineer's Workbenchを `['iss','c f']` の配置でクラフトする。それぞれのLumberとSlabを入れ替えて再照合する。

Expected Result:
IE Crafting Tableは上段の `tfie:treated_wood_lumber` 3個、Treated Wood Rod4個、`#c:player_workstations/crafting_tables` 1個から1個を生成し、上段にTreated Slabを使用しない。Engineer's WorkbenchはIE Crafting Table1個、Wrought Iron Ingot1個、Treated Fence1個、`#immersiveengineering:treated_wood_slab` 2個から1個を生成する。WorkbenchのSlabをLumberへ変更しない。検証方式はGameTestとする。

##### AC-F011-004: 支柱用Stone Bricksの限定

Preconditions:
Treated、Steel、Aluminumの各Fenceと、第15章のStone Bricksタグ表にある22アイテムを用意する。対応するSlab、Stairs、Wall、苔付き、ひび入りの派生形も比較入力として用意する。

Action:
Fence2個を縦に置き、その下へStone Bricks1個を置いて各Postをクラフトする。基礎材料を全22アイテムと派生形へ順に変更する。

Expected Result:
`#tfie:stone_bricks_no_variants` は第15章のTFC21岩種の通常Bricksと `minecraft:stone_bricks` の合計22アイテムを含む。各Postは対応Fence2個とタグ内の基礎1個から1個を生成する。Slab、Stairs、Wall、苔付き、ひび入りなどの派生形を自動追加しない。検証方式はGameTestとする。

##### AC-F011-005: 布と繊維の互換およびFirmaLifeの人工皮革

Preconditions:
FirmaLifeあり・なしの環境を用意する。IEのHemp FabricとHemp Fiber、TFCのBurlap ClothとJute Fiber、およびFirmaLife導入時のRaw Honeyを用意する。

Action:
布・繊維を要求する第15章の各レシピをIE素材とTFC素材で実行する。FirmaLife導入時にはRaw Honey1個を中央、Fabric8個を周囲へ置き、未導入時には読込済みレシピを調べる。

Expected Result:
`c:fabric_hemp` に `tfc:burlap_cloth`、`c:fiber_hemp` に `tfc:jute_fiber` を追加し、IE既存のタグ値を維持する。対応レシピが両Modの素材を受け付け、素材同士の相互変換レシピを追加しない。FirmaLife導入時だけ `tfie:crafting/ersatz_leather_firmalife` が存在し、固定アイテム `firmalife:food/raw_honey` 1個とFabric8個を消費してIE Ersatz Leather8個を生成する。Honey Fluidをこの入力へ代用しない。未導入時には当該レシピとTFIEによる代替配合を読み込まない。検証方式はGameTestとする。

##### AC-F011-006: Components BlueprintとFluorescent Tubeの標準材料

Preconditions:
Copper、Lead、通常Iron、Aluminum、Wrought IronのIngot、Blue Dye、Paper、Glass Block、Graphite Electrode、Glowstone Dust、Small Sphaleriteを用意する。

Action:
Components Blueprintを `['jkl','ddd','ppp']`、Fluorescent Tubeを `['GeG','GgG','GgG']` の配置でクラフトする。LeadをAluminumへ、通常IronをWrought Ironへ、Glowstone DustをSmall Sphaleriteへ変更した入力も照合する。

Expected Result:
Components BlueprintはCopper・Lead・通常IronのIngot各1個、Blue Dye3個、Paper3個を消費し、`immersiveengineering:blueprint` 1個へ `immersiveengineering:blueprint = components` を設定する。Molds Blueprintの材料と混同しない。Fluorescent TubeはGlass Block6個、Graphite Electrode1個、Glowstone Dust2個から1個となる。TFIEは上記のAluminum、Wrought Iron、Small Sphaleriteへの置換を行わず、通常Ironの入手条件を回避する追加レシピも設けない。検証方式はGameTestとする。

##### AC-F011-007: 固定Bucketと追加しない製法

Preconditions:
ChemthrowerとJerrycanの第15章に定める全材料、Minecraft Bucket、Blue Steel Bucket、Red Steel Bucketを用意する。

Action:
標準配置で両製品をクラフトし、固定Bucket枠をTFCの容器へ変更して照合する。関連するTFIE追加レシピIDとMinecraft Bucketの通常クラフトを調べる。

Expected Result:
Chemthrowerは固定 `minecraft:bucket` 1個、Jerrycanは同Bucket4個を要求する。Jerrycanの金属板は `#c:plates/iron` 4個を維持する。TFCのBlue Steel Bucket・Red Steel Bucketへ置換する経路を追加せず、流体Ingredientの容器互換を固定Bucketへ適用しない。TFCによるMinecraft Bucket通常クラフトの無効化を維持する。`tfie:crafting/chemthrower_tfc`、`tfie:crafting/jerrycan_tfc`、`tfie:crafting/blueprint_special_bullet` を出荷せず、入手条件を迂回する追加レシピを設けない。IE自身のSpecial Bullet Blueprint入手手段は、この非追加指定を理由に削除しない。検証方式はGameTestとする。

##### AC-F011-008: 炉の建材と手作業レシピの除外

Preconditions:
第15章のAlloy Brick、Blast Brick、Reinforced Blast Brickの材料と、同章の手作業レシピの除外範囲にある各標準クラフト材料を用意する。

Action:
炉の建材3製法を実行する。第15章および第33章の対象IDを列挙して通常クラフトを照合し、対象外のIEクラフトも比較する。

Expected Result:
Alloy BrickはBrick2個とSandstone Block2個から2個、Blast BrickはNether Brick4個・Brick4個・Magma Block1個から3個、Reinforced Blast BrickはBlast Brick1個・Steel Plate1個から1個を生成する。これらを無効化する定義を残さない。Faraday Armor4件、Empty Casing、Gun Barrel、Revolver Bayonet、Iron・Steel Drill Head2件の通常クラフトは、それぞれ指定された金床経路へ置換する。IE Steel防具4件、Steel工具5件、DustからのGunpowderクラフトを無効化し、TFIEの代替製法を追加しない。Nugget8件、Ingotと圧縮Block16件、手動Plate6件・Rod2件は第33章の限定ID集合に従って無効化し、対象外のIE一般クラフトを維持する。検証方式はGameTestとする。

##### AC-F011-009: 一般クラフトの表示

Preconditions:
JEIを使用する検証環境で、第15章の一般クラフトを読み込む。FirmaLifeあり・なしの両構成を用意する。

Action:
IE Crafting Table、Engineer's Workbench、Components Blueprint、Fluorescent Tube、Chemthrower、Jerrycan、各Post、Ersatz Leatherのレシピを表示する。

Expected Result:
材料アイコン、数量、配置、出力が第15章と一致し、Crafting TableのTreated LumberとWorkbenchのTreated Slabを区別して表示する。Components BlueprintはComponents用として表示し、固定BucketをTFC容器の選択入力として表示しない。無効化・非追加対象はTFIEの製造経路として表示せず、FirmaLife未導入時にその専用レシピを表示しない。missing texture、レシピ枠の崩れ、数量文字の欠落を生じない。検証方式はE2Eとする。

### F-012: マルチブロック構造

#### Description

Global Requirements第16章に定めるArc Furnace、Grist Mill、Power Loomの構造を実装する。同章の材料・数量・全相対座標・テンプレート状態・形成起点・マスター位置・回転・鏡映・厳密照合・導入条件の全表行と全条件も合否対象とする。通常Blast FurnaceとImproved Blast Furnaceの構造はF-007、Alloy Kilnの構造はF-008で扱う。3設備ともIEのTemplateMultiblock方式を維持する。

#### Acceptance Criteria

##### AC-F012-001: Arc Furnaceの形成

Preconditions:
第16章のArc Furnace表に従って、5×5×5の範囲へ全非空ブロックを配置する。テンプレート原点を `(0,0,0)` とし、未回転の `+X` を東、`+Y` を上、`+Z` を南とする。

Action:
`(2,0,4)` の `tfc:crucible` のSouth面へEngineer's Hammerで形成操作を行う。表の各必須座標を個別に欠落させた構造でも照合する。

Expected Result:
Steel Sheetmetal8個、TFC Black Steel Plated Block6個、Steel Sheetmetal Slab14個、Steel Scaffolding5個、Heavy Engineering5個、TFC Crucible1個、Redstone Engineering1個、Light Engineering10個、TFC Fire Bricks27個を、第16章の全座標どおり要求する。完全な構造が形成され、マスター位置は `(2,1,2)` となる。Slabは `type=bottom, waterlogged=false`、Scaffoldingは `waterlogged=false` をテンプレート状態とする。必須ブロックの欠落を認めない。検証方式はGameTestとする。

##### AC-F012-002: Grist Millの形成

Preconditions:
Advanced TFC Tech Unofficialを導入し、第16章のGrist Mill表の全座標に4×3×3の構造を配置する。

Action:
`(2,1,1)` の `tfc:metal/block/steel` へ正しい側面からEngineer's Hammerで形成操作を行い、各必須座標の欠落時も照合する。

Expected Result:
Steel Scaffolding9個、Steel Sheetmetal3個、Light Engineering4個、Redstone Engineering1個、Steel Sheetmetal Slab4個、TFC Steel Plated Block1個、Hopper1個を表どおりの座標へ要求し、マスター位置は `(1,0,1)` となる。Hopperのテンプレート状態は `facing=down, enabled=true`、Slabは `type=bottom, waterlogged=false`、Scaffoldingは `waterlogged=false` とする。必須座標を欠く構造は形成されない。検証方式はGameTestとする。

##### AC-F012-003: Power Loomの形成

Preconditions:
Advanced TFC Tech Unofficialを導入し、第16章のPower Loom表の全座標に3×3×5の構造を配置する。

Action:
`(1,1,4)` の `immersiveengineering:light_engineering` へ正しい側面からEngineer's Hammerで形成操作を行い、各必須座標の欠落時も照合する。

Expected Result:
Steel Scaffolding13個、Light Engineering3個、Heavy Engineering3個、TFC Steel Plated Block1個、Redstone Engineering1個、Steel Sheetmetal5個、Steel Fence1個を表どおりの座標へ要求し、マスター位置は `(1,0,2)` となる。Steel Fenceのテンプレート状態は `east=false, west=false, south=true, north=true, waterlogged=false`、Scaffoldingは `waterlogged=false` とする。必須座標を欠く構造は形成されない。検証方式はGameTestとする。

##### AC-F012-004: 指定ブロックIDの厳密照合

Preconditions:
3設備の完全な構造と、同じstorage blockタグに属するTFC以外のSteel・Black Steelのブロックを用意する。ArcのFire BricksとCrucibleについても別ブロックを比較対象とする。

Action:
TFC Steel Plated Block、TFC Black Steel Plated Block、TFC Fire Bricks、TFC Crucibleの指定位置を1か所ずつ別ブロックへ変更して形成操作を行う。

Expected Result:
Steel Plated Block指定箇所は `tfc:metal/block/steel`、Black Steel Plated Block指定箇所は `tfc:metal/block/black_steel` のブロックID一致を要求する。同一storage blockタグに属する別ブロックでは形成されない。Arcは `tfc:fire_bricks` と `tfc:crucible` を要求し、素材タグの一致だけで代用を認めない。検証方式はGameTestとする。

##### AC-F012-005: 方向変換と空欄の扱い

Preconditions:
3設備について、第16章の完全な構造を4方向と左右鏡映で用意する。各構造の表にないテンプレート座標にも比較用のブロックを置く。

Action:
形成起点のSouth、West、North、Eastの各面から対応方向の形成操作を行う。上下の面からの操作と、ブロック状態の向きを変換しない誤配置も試す。

Expected Result:
South面は無回転、West面は上から見て時計回り90度、North面は180度、East面は反時計回り90度として、全座標と方向を持つブロック状態へ同じ変換を適用する。左右鏡映を認め、同じ材料条件を維持する。上下の面は形成起点として扱わない。通常Airの空欄を構造照合・置換対象に追加せず、表にない座標へ独自の空気必須条件を設けない。検証方式はGameTestとする。

##### AC-F012-006: Advanced TFC Techの導入条件

Preconditions:
必須Modだけの環境と、Advanced TFC Tech Unofficialを追加した環境を用意する。

Action:
両環境で起動とデータ読込を行い、Arc Furnaceの構造を照合する。追加Mod導入時にはGrist MillとPower Loomを形成する。

Expected Result:
Arc Furnaceへの変更は必須構成で有効となる。Grist MillとPower Loomへの変更はMod ID `advancedtfctech` の導入時だけ適用する。未導入時には対象クラスや登録物への参照によってTFIEの起動・データ読込を妨げず、独立したGrist Mill・Power LoomをTFIE側へ新設しない。検証方式はGameTestとする。

##### AC-F012-007: 形成状態の保存

Preconditions:
3設備をそれぞれ形成し、第16章の指定位置にマスターが存在する。Advanced TFC Techの2設備には同Modを導入する。

Action:
形成済みの設備を含むチャンクを保存して再読込し、ワールドを保存してサーバーを再起動する。

Expected Result:
形成状態、方向・鏡映、マスター位置と各部位の接続関係が維持され、形成済み設備として動作する。未形成のテンプレート状態へ勝手に戻らず、マスターを重複生成しない。保存・同期には第34章の共通条件を適用する。検証方式はGameTestとする。

##### AC-F012-008: 構造と形成後の表示

Preconditions:
IE Manualの各構造表示と、実ワールド内の未形成・形成済みのArc Furnace、Grist Mill、Power Loomを用意する。

Action:
各構造を複数方向から表示し、Slab、Hopper、Fenceの方向が分かる視点と形成後のモデルを撮影する。

Expected Result:
表示する材料、数量、位置、テンプレート状態が第16章の全表行と一致する。ArcのTFC Crucible・Fire Bricks・Black Steel Plated Block、Grist MillとPower LoomのTFC Steel Plated Blockを表示し、別のstorage blockへ置換しない。形成後のモデルと向きが構造の方向に対応し、missing texture、モデル崩れ、z-fighting、意図しない欠落を生じない。検証方式はE2Eとする。

### F-013: 木材加工と処理木材

#### Description

Global Requirements第17章のSawmillとTreated Wood Lumberの製法を実装する。同章の対象36種、144レシピ、全表行、入出力、費用、進行段階、刃の判定、装着、耐久、排出、条件付き読込、依存非宣言、Beneath除外、密閉樽の数量・時間・保存条件も合否対象とする。ArborFirmaCraft連携の境界には第23章のLatex・Rubberの全表行と非追加指定も適用する。SawmillはIE標準の段階別副産物排出を維持し、炉の全出力同時確定の規則を転用しない。

#### Acceptance Criteria

##### AC-F013-001: 製材対象と条件付き読込

Preconditions:
TFCだけの木材構成、AFC追加、Overdims追加、両方追加の各環境を用意する。対象Modが存在する一方で、ある製材レシピが参照する入力・剥皮後形状・LumberのIDが欠ける構成も用意する。

Action:
各構成で起動とデータ再読込を行い、第17章の対象木材・レシピIDとmetadata・本番ビルド依存の宣言を照合する。

Expected Result:
TFC20種80件、AFC14種56件、OverdimsのCrimson・Warped2種8件を完全な対象集合とし、全対象導入時は36種144件となる。各種類のレシピ資源パスは `data/tfie/recipe/sawmill/{S}/{K}.json`、Kはlog・wood・stripped_log・planksの4値とする。stripped_logの1件がstripped_logとstripped_woodをOR入力として受け付け、5件目を追加しない。AFCは `afc`、Overdimsは `tfcoverdims` の導入と全参照Item IDの存在を読込条件とし、不足時は該当レシピを省略して起動を継続する。両Modを必須・任意・本番ビルド・配布ページの依存関係へ追加せず、旧AFC依存宣言を除去する。Beneath木材レシピを含めず、未導入IDをTFC・Vanillaへ置換しない。検証方式はGameTestとする。

##### AC-F013-002: 刃を用いる製材の入出力と費用

Preconditions:
IEのSawmill費用倍率と時間倍率を1にし、正常な刃、十分なFE、および全対象木材のLog、Wood、Stripped Log、Stripped Wood、Planksを用意する。

Action:
各入力を1個ずつ加工開始から完了まで刃を装着して処理し、材料・主出力・副産物・費用を計測する。

Expected Result:
全144レシピは入力1個につき1,600 FE、80 ticks、稼働中20 FE/tとなる。Log・Woodは対応Lumber12個とWood Dust計2個、Stripped Log・Stripped WoodはLumber12個とWood Dust1個、PlanksはLumber4個だけを生成する。Dustは `#c:dusts/wood` のTagOutputから解決する。Log・WoodのDustは剥皮時1個と切断時1個へ分け、剥皮済み入力へ剥皮Dustを再付与しない。Overdimsでは `tfcoverdims:wood/...` のIDを使用し、Vanilla Stemを代用にしない。Overdims自身の手鋸によるLumber8個とLumber4個からPlanks1個の製法は変更しない。IE設定変更時にはIE標準倍率だけを適用し、独自の温度・流体条件を追加しない。検証方式はGameTestとする。

##### AC-F013-003: 刃を装着しない製材

Preconditions:
刃を装着しないSawmill、十分なFE、各木材の5入力形状を用意する。IEの費用・時間倍率は1とする。

Action:
刃がない状態を加工開始から終了まで維持して各入力1個を処理する。

Expected Result:
LogはStripped Log1個と剥皮時のDust1個、WoodはStripped Wood1個と剥皮時のDust1個を排出する。Stripped Log、Stripped Wood、Planksは投入した同じアイテム1個をそのまま排出し、Dustを生成しない。いずれもLumberを生成しない。費用と所要時間は刃ありと同じ1,600 FE・80 ticksとし、搬送と剥皮を停止しない。刃あり加工でStripped形状をLumberへ加算する処理も設けない。検証方式はGameTestとする。

##### AC-F013-004: 段階別排出と加工途中の停止・刃変更

Preconditions:
標準設定のSawmillへ複数の原木・剥皮済み材・Planksを投入し、主出力・副産物の接続先を用意する。FE不足、Redstone停止、接続先満杯、および段階間の刃着脱を実施する状態も用意する。

Action:
25 tick目、69 tick目、80 tick目の前後で進行を観測する。FEを不足させ、Redstoneで停止し、正規の着脱操作で刃を交換・除去する。主出力と副産物の接続先を満杯にする。

Expected Result:
剥皮副産物は進捗31.25%の25 tick目、切断副産物は進捗86.25%の69 tick目に、その時点の条件に従って排出する。主出力は完了時点の刃の有無で決定する。69 tick目の切断副産物判定と完了時の主出力判定を統合せず、途中の着脱・他処理による刃の破損にもIE標準の段階別判定を適用する。FEが1 tick分に不足する間とRedstone停止中は進行しない。蓄電容量32,000 FE、処理列上限6を維持し、各処理は入力1個を保持する。同種の連続入力を合算配合へ変更しない。主出力と副産物をIE標準の別排出口へ送り、接続先に収納されない分は排出口からアイテムとして排出する。炉の全出力収納待ちへ変更しない。検証方式はGameTestとする。

##### AC-F013-005: 刃の装着・耐久と加工状態の保存

Preconditions:
刃なし・刃ありのSawmill、`#c:sawblades` の刃、標準最大耐久10,000のIE Sawblade、破損まで残り耐久5の刃を用意する。剥皮前・剥皮後・切断後の処理を保存対象とする。

Action:
空の機械へ刃を装着し、Redstone停止中に刃の交換と空手スニークによる取り外しを行う。FE残量だけをゼロにした稼働状態でも着脱を試す。処理を完了させ、各段階で保存・再読込し、機械の解体も行う。

Expected Result:
装着対象は `#c:sawblades` のアイテム1個とする。交換と取り外しはIEのRedstone停止条件を使用し、FE残量ゼロだけではその条件を代用しない。標準設定では各処理の主出力排出後に装着中の刃へ耐久5を消費し、原木・剥皮済み材・Planksで値を変更しない。未使用の標準刃は他用途と設定変更がなければ2,000処理を完了し、破損処理も主出力排出後に刃を失う。残る処理は刃なしの段階判定に従う。保存・再読込で保持入力、進捗、刃の耐久、既に排出した副産物の状態を失わず、同じ副産物を再排出しない。解体時は内部の刃をIE標準の解体処理で排出する。検証方式はGameTestとする。

##### AC-F013-006: 密閉樽によるTreated Wood Lumber

Preconditions:
TFCの密閉樽へ `#tfc:lumber` の入力n個と `immersiveengineering:creosote` の流体f mBを入れる。Lumber16個・Creosote800 mB、流体不足、余剰流体、余剰入力の各条件を用意する。

Action:
樽を封印して `tfie:barrel/treated_wood_lumber` を進行させる。途中の開封、チャンク再読込、保存・再読込も実行する。

Expected Result:
型は `tfc:barrel_sealed`、単位配合はLumber1個とCreosote50 mBから `tfie:treated_wood_lumber` 1個、durationは8,000とする。完了時の加工数は `min(n, floor(f/50))` となり、同数のLumberとその50倍のmBのCreosoteを消費する。16個と800 mBからは同じ8,000の処理時間で16個を生成し、処理時間を16倍にしない。新たな流体出力、FE、加熱条件を設けない。余剰材料と流体をTFC標準処理で保持し、封印・開封・暦時刻・チャンク再読込の時間処理を独自時計へ置換しない。検証方式はGameTestとする。

##### AC-F013-007: 処理木材ブロックの組立と油クラフトの削除

Preconditions:
Treated Wood Lumber4個、Treated Slab、通常Planks8個、Creosote1,000 mBを用意する。

Action:
Treated Wood Lumberを2×2へ置いてクラフトし、Slabへ置換した配置を照合する。IE標準のPlanksとCreosoteによる処理木材クラフト、および既存の向き変更・Slab・Stairs製法を照合する。

Expected Result:
`tfie:crafting/treated_wood_horizontal` は `tfie:treated_wood_lumber` 4個から `immersiveengineering:treated_wood_horizontal` 1個を生成し、Treated Slabを入力にしない。密閉樽から通した原料はLumber4個とCreosote200 mBとなる。別名前空間の `immersiveengineering:crafting/treated_wood_horizontal` を無効化し、Planks8個とCreosote1,000 mBから8ブロックを作る標準経路を残さない。処理木材の向き変更、Slab・Stairsへの既存加工は維持する。検証方式はGameTestとする。

##### AC-F013-008: AFCのLatexとRubberの維持範囲

Preconditions:
AFC導入時とAFC・FirmaLife併用時の構成を用意し、第23章のLatex・Rubber既存5経路、採液用Tap、Hevea、Rubber Figを用意する。

Action:
レシピ集合と材料タグを確認し、Pot2経路、FirmaLife Vat、Latex容器からGlue、RubberからSealed Bricks、および採液条件を照合する。

Expected Result:
IE Bottling Machine、Squeezer、Mixerその他の設備によるLatex→Rubberを追加しない。Rubberを使うConveyor・LV/MV絶縁Wire Coilも追加しない。`afc:rubber_bar` をLeather・Fabric・Plastic共通タグへ登録せず、LatexをPhenolic Resinと同一視しない。第23章の既存5経路を上書き・重複追加せず、Potの600℃・2,000 ticksと600℃・1,000 ticks、Vatの300℃・600 ticks、Latex1,000 mB→Glue4個、Stone Bricks8個とRubber1個→Sealed Bricks8個を維持する。Heveaの自然原木14〜50℃・1回2 mB、Rubber Figの自然原木6〜50℃・1回1 mBという採液条件と、春限定ではない条件を維持する。IE自身のAcetaldehydeとCreosoteによるResin製造を削除しない。検証方式はGameTestとする。

##### AC-F013-009: 製材と処理木材の表示

Preconditions:
TFC・AFC・Overdimsを導入したJEI環境と、Sawmillの刃あり・なしの実機、TFC密閉樽、Treated Wood Lumberを用意する。

Action:
Log、Wood、Stripped LogとStripped Woodの共通レシピ、Planks、Treated Wood Lumber、処理木材ブロックのレシピと実機表示を確認する。AFC・Overdims未導入の構成でもレシピ一覧を表示する。

Expected Result:
各提供元の素材、入力1個、Lumber12個または4個、段階別Dustを該当レシピで正しく表示し、剥皮済み2入力を1レシピの選択入力として扱う。TFIEはJEIの製材レシピ画面に消費FEを表示しない。消費FEはAC-F013-002のGameTestで確認する。刃なし加工と剥皮中間形状を、刃ありの追加Lumber出力として表示しない。密閉樽はLumber1個・Creosote50 mB・duration8,000、組立はTreated Lumber4個→処理木材1個を表示する。未導入Modの製材、Beneath製材、削除済み標準油クラフトをTFIEの有効経路として表示しない。Treated Wood Lumberのモデル・テクスチャ・名称、Sawmillの刃と加工物にmissing texture、表示崩れ、数量文字の欠落を生じない。検証方式はE2Eとする。

### F-014: 麻の栽培

#### Description

IEのHemp SeedsとHemp Fiberを使用し、TFCの栽培株・枯死株・野生株と生育処理を接続する。登録ID、気候、栄養、Loot Table、野生生成、季節、物理特性、タグは詳細仕様第18章の全表行・条件を適用する。

#### Acceptance Criteria

##### AC-F014-001: 植付けと作物登録

Preconditions:
TFC農地と、IEの`immersiveengineering:seed`を持つプレイヤーを用意する。

Action:
種を農地へ使用し、栽培株、枯死株、野生株、BlockEntityおよび関連タグの登録を検査する。

Expected Result:
`tfie:crop/hemp`が植わる。栽培株と枯死株には独立したBlockItemを登録せず、野生株`tfie:wild_crop/hemp`にはBlockItemを登録する。独自の種と繊維は登録しない。3種を`minecraft:crops`と`minecraft:mineable/hoe`へ追加し、雪のタグには野生株だけを追加する。既存タグを置換しない。詳細仕様第18章の登録・物理特性も合否対象とする。検証方式はGameTestとする。

##### AC-F014-002: 生育境界と栄養消費

Preconditions:
光量12以上の農地へHempを植え、温度10℃・31℃、水分30%・80%の境界値と、それぞれの範囲外を用意する。上段予定位置が空気の状態と障害物のある状態を用意する。

Action:
TFCの暦・作物更新を進め、生育、栄養吸収、上段生成および枯死を観測する。

Expected Result:
温度10〜31℃と水分30〜80%を両端含みで使用し、許容拡張を設けない。N=0.6、P=0.2、K=0.6を共通生育処理の吸収基準とする。下段だけが生育と栄養消費を処理し、age 0〜3では下段、age 4では上下段を形成する。上段の障害物による成長上限は0.75となる。独自の固定成長日数と毎tickの全量栄養消費を追加しない。光量・寿命・枯死等の詳細仕様第18章の条件も合否対象とする。検証方式はGameTestとする。

##### AC-F014-003: 収穫と上下段のドロップ

Preconditions:
栽培株のage 0〜4、成熟前後の枯死株、未成熟・成熟の野生株を用意する。成熟栽培株には複数の収量係数を設定する。

Action:
各状態の下段および上段を別々に破壊し、Loot Tableを制御した乱数で評価する。爆発時の条件も検査する。

Expected Result:
栽培株は種1個を出し、age 0〜3では繊維を出さない。成熟株の繊維は最小0、最大候補6〜10の一様分布と保存済み収量係数による補間を使用する。未成熟枯死株は種1個、成熟枯死株は種1〜3個、未成熟野生株は種1個、成熟野生株は種1個と繊維1〜3個となる。上段から同じ生成物を重複生成しない。全プールに`minecraft:survives_explosion`を適用する。詳細仕様第18章の収穫表の全行を合否対象とする。検証方式はGameTestとする。

##### AC-F014-004: 野生生成と季節

Preconditions:
野生Hempの個体・群落Featureを読み込み、温度と地下水値の境界、および半球補正済み暦月を設定したワールドを用意する。

Action:
配置定義と成立条件を評価し、6月・10月の境界、成熟期外、年をまたぐ成熟期設定、積雪状態を更新する。

Expected Result:
群落は試行6回、水平拡散5、垂直拡散1、rarity chance 90、温度10〜40℃、地下水140〜360を使用する。Rainfallへ読み替えず、`WORLD_SURFACE_WG`、replaceableおよびwould_surviveを要求する。基準設定では6〜10月に上下段が成熟し、冬季の積雪で成熟を解除する。`#tfc:feature/crops`の既存内容を維持する。詳細仕様第18章のFeature IDと全配置条件も合否対象とする。検証方式はGameTestとする。

##### AC-F014-005: 保存と上段操作

Preconditions:
成長度、収量係数、吸収済みN/P/K、寿命、最終更新時刻の異なるHempを用意する。

Action:
上段から農地情報の取得と施肥を行い、チャンク再読込およびサーバー再起動を実施する。

Expected Result:
上段の情報取得と施肥は対応する下段・農地へ作用する。BlockEntityの全保存項目と上下段状態が再読込後も一致し、二重成長・二重施肥消費を生じない。サーバー確定状態をクライアントへ同期する。詳細仕様第18章の保存・同期条件も合否対象とする。検証方式はGameTestとする。

##### AC-F014-006: 作物の描画

Preconditions:
栽培株の全age、成熟前後の枯死株、未成熟・成熟の野生株をクライアントの同一検証場へ配置する。

Action:
上下段と葉の透過領域が判別される視点から撮影する。

Expected Result:
3種をcutoutで表示し、段階に対応するモデル・テクスチャが欠落しない。age 0〜3に不要な上段を表示せず、成熟時の上下段に位置ずれ、重複面、透過不良を生じない。詳細仕様第18章の表示条件も合否対象とする。検証方式はE2Eとする。

### F-015: Garden Clocheによる作物栽培

#### Description

TFC作物29種のClocheレシピと8土壌への対応を実装する。成長目標値、稼働電力、養液、収穫、副産物、二段描画は詳細仕様第19章と第22章の全表行・条件を適用する。

#### Acceptance Criteria

##### AC-F015-001: 栽培対象と収穫量

Preconditions:
詳細仕様第19章に列挙した29種の種、8種の土壌、Waterと電力を用意し、IEの標準倍率を使用する。

Action:
全29レシピについて収穫まで処理し、土壌8種による差と種子副産物の条件を検査する。

Expected Result:
レシピ型は`immersiveengineering:cloche`、IDは`tfie:cloche/<作物識別子>`となる。MelonとPumpkinは果実ブロック1個、Red・Yellow Bell Pepperは対応色の食品4個、残り25種は指定アイテム6個を主出力とする。同種の種1個を25%で追加する。種と土壌を消費せず、土壌別の収量・時間差を設けない。食品の腐敗を無効化しない。詳細仕様第19章の全作物・土壌表と第22章の互換条件も合否対象とする。検証方式はGameTestとする。

##### AC-F015-002: 電力と成長目標

Preconditions:
有効な種と土壌を配置し、成長倍率1.0、肥料なし、Waterと十分な電力を持つClocheを用意する。

Action:
成長量とFEをtick単位で測定し、肥料倍率およびIEの`garden_cloche.growth_modifier`を変更して再評価する。

Expected Result:
全29レシピの成長目標は128,000となる。標準では成長中の1tickに成長量1を加え、8 FEを消費し、収穫確定tickも8 FEを消費する。肥料倍率とIE成長倍率は成長加算量へ作用する。128,000を固定FE総額として扱わない。内部電力容量16,000 FE、流体容量4,000mB、土壌・種・肥料各1枠、出力4枠を維持する。詳細仕様第19章の費用・進行条件も合否対象とする。検証方式はGameTestとする。

##### AC-F015-003: 水と肥料の補給

Preconditions:
Clocheに通常Water、Salt Water、Spring Water、Alcoholを個別に供給する。通常Waterの構成では有効肥料の有無を切り替える。

Action:
養液補給と6,000稼働ticks分の消費を観測する。

Expected Result:
`minecraft:water`だけを受け付け、補給1回に250mBを消費して6,000稼働ticks分の養液を得る。補給時の有効肥料は1個を消費し、その期間の倍率を設定する。肥料なしでも倍率1.0で栽培する。TFCの外気温・季節・降水・農地水分・農地N/P/K・支柱を追加条件としない。詳細仕様第19章の補給条件と第29章の肥料倍率も合否対象とする。検証方式はGameTestとする。

##### AC-F015-004: 条件喪失と出力待機

Preconditions:
成長途中のClocheと収穫完了直前のClocheを用意する。電力不足、無効な種・土壌、収穫物の全量が収まらない出力欄を個別に作る。

Action:
不足条件を発生・解消し、進捗、出力、FE、養液残量を観測する。

Expected Result:
電力不足または有効な栽培条件の喪失ではIEの処理に従って進捗を0へ戻す。出力容量だけが不足する場合は成長完了状態で待機し、成長用FEと養液残量を追加消費せず、全収穫物が収まると収穫を確定する。Arc用の予約・待機・副産物保持機構をClocheへ一律導入しない。詳細仕様第19章の待機条件も合否対象とする。検証方式はGameTestとする。

##### AC-F015-005: 二段作物の描画状態

Preconditions:
Green Bean、Jute、Maize、Papyrus、Sugarcane、Tomatoの各レシピについて、二段化境界の前後の成長率を用意する。

Action:
クライアントRendererを起動せず、両側共通のClocheRenderFunction実装が返すage、上下段のBlockState、Transformation、倍率を検査する。

Expected Result:
`tfie:doublecroptfc`が`min(maxAge, round(maxAge × g))`でageを決定し、全体倍率0.6875を使用する。Green Bean・Sugarcane・Tomatoはage 4、Jute・Maize・Papyrusはage 3から上段を1ブロック上へ表示し、上段にTOPを設定する。残り23種はIEの通常作物描画関数を使用する。専用サーバーでクライアント専用クラスを読み込まない。詳細仕様第19章の描画状態条件も合否対象とする。検証方式はGameTestとする。

##### AC-F015-006: 作物と土壌の描画

Preconditions:
29作物と8土壌を描画対象とし、二段作物には二段化の前後と成熟時のClocheを用意する。

Action:
Cloche内部を観察するE2Eシナリオで作物と土壌表面を撮影する。

Expected Result:
対応する作物モデルと土壌ごとの`tfc:block/farmland/<土壌識別子>`を表示する。入力はDirtであり、表示のためにFarmland入力へ変更しない。二段作物の上段・下段が同じageで表示され、Clocheとの不正な突き抜け、位置ずれ、missing textureを生じない。詳細仕様第19章の表示条件も合否対象とする。検証方式はE2Eとする。

### F-016: Squeezerによる種子の圧搾

#### Description

対象種子23種をSqueezerでPlant Oilへ加工する。旧版のFermenter型指定を是正し、対象集合と数量は詳細仕様第20章と第22章の全表行・条件を適用する。

#### Acceptance Criteria

##### AC-F016-001: 23種の圧搾

Preconditions:
詳細仕様第20章の種子23種と、IE標準倍率のSqueezerを用意する。

Action:
全種子を1個ずつ投入して加工を完了させる。

Expected Result:
各処理が種1個を消費し、`immersiveengineering:plantoil`80mBを6,400 FE・80ticksで生成する。固体出力、確率副産物、入力流体、加熱条件、肥料条件を追加しない。詳細仕様第20章の23行すべてを合否対象とする。検証方式はGameTestとする。

##### AC-F016-002: 機械種別と対象外種子

Preconditions:
SqueezerとFermenter、および対象23種とGreen Bean・Lentil・Garlic・Potato・Sugarcane・Papyrusの種を用意する。

Action:
Recipe Managerの型と機械での照合を確認する。

Expected Result:
対象のIDは`tfie:squeezer/<作物識別子>`、型は`immersiveengineering:squeezer`となる。同じ23件をFermenterへ残さず、対象外6種へTFIEのPlant Oil加工を追加しない。詳細仕様第20章と第22章の機械種別是正を合否対象とする。検証方式はGameTestとする。

##### AC-F016-003: 連携Mod不在と標準倍率

Preconditions:
FirmaLifeとAdvanced TFC Tech Unofficialのない基本構成、およびIEの費用・時間倍率を変更した構成を用意する。

Action:
種子Squeezerの読込と加工を実行する。

Expected Result:
23件はOptional Modを要求せずに有効となる。基礎値6,400 FE・80ticksへIE側の機械設定を適用し、TFIE独自の固定経過時間で上書きしない。詳細仕様第3章、第20章、第22章の共通条件を合否対象とする。検証方式はGameTestとする。

##### AC-F016-004: レシピ表示

Preconditions:
JEIを導入し、対象種子の用途画面とPlant Oilの製法画面を開く。

Action:
SqueezerカテゴリとFermenterカテゴリを撮影する。

Expected Result:
対象23種の加工がSqueezerカテゴリで種1個からPlant Oil80mBとして表示される。旧Fermenter経路と存在しない固体副産物を表示しない。詳細仕様第20章と第32章の表示条件も合否対象とする。検証方式はE2Eとする。

### F-017: FermenterによるEthanol製造

#### Description

TFC食品26種、穀粒タグ1レシピ、およびFirmaLife導入時のRaw HoneyからEthanolを製造する。詳細仕様第21章と第22章の全表行・条件を適用する。

#### Acceptance Criteria

##### AC-F017-001: 食品26種の発酵

Preconditions:
詳細仕様第21章の食品26種と、IE標準倍率のFermenterを用意する。

Action:
各食品1個を加工し、入出力と費用を測定する。

Expected Result:
各食品1個から`immersiveengineering:ethanol`80mBを6,400 FE・80ticksで生成する。型は`immersiveengineering:fermenter`、IDは`tfie:fermenter/<識別子>`となる。入力流体、固体出力、確率副産物を設けない。Melon SliceとPumpkin Chunksを使用し、果実ブロック、Peanut、Jamの独自経路を追加しない。詳細仕様第21章の全26行を合否対象とする。検証方式はGameTestとする。

##### AC-F017-002: 穀粒タグの処理

Preconditions:
`#c:foods/grain`へ属するTFCの6穀粒と、上位データパックから同タグへ追加した検証用素材を用意する。未脱穀作物も用意する。

Action:
`tfie:fermenter/grains`の照合と加工を実行する。

Expected Result:
タグに属する各1個からEthanol80mBを6,400 FE・80ticksで生成する。TFCの6アイテムだけへ条件を固定せず、同タグの追加素材を受け付ける。未脱穀作物を穀粒と同一扱いしない。詳細仕様第21章の穀粒条件を合否対象とする。検証方式はGameTestとする。

##### AC-F017-003: 食品の腐敗

Preconditions:
対象食品と、FirmaLife導入時のRaw Honeyについて腐敗前後のスタックを用意する。

Action:
同じ入力数でFermenterの照合と加工を実行する。

Expected Result:
通常のItem・タグ条件を使用し、対象食品とRaw Honeyを腐敗後も受け付ける。Caramel用Butterの非腐敗条件をFermenterへ適用しない。腐敗前後で指定出力量を変更しない。詳細仕様第21章の腐敗条件を合否対象とする。検証方式はGameTestとする。

##### AC-F017-004: Raw Honeyと連続加工

Preconditions:
FirmaLife導入・未導入の構成を用意する。導入構成ではRaw HoneyとWild Honeycombを用意する。

Action:
Raw Honeyの発酵、およびWild HoneycombのSqueezer加工に続く発酵を実行する。

Expected Result:
導入時だけ`tfie:fermenter/raw_honey`を1件登録し、Raw Honey1個からEthanol250mBを6,400 FE・80ticksで生成する。Wild Honeycombからの2工程は合計12,800 FEでEthanol250mBを得る。未導入時に当該レシピを読み込まず、蜂蜜連携による同じ発酵レシピの重複登録も行わない。詳細仕様第21章と第23章の該当条件を合否対象とする。検証方式はGameTestとする。

##### AC-F017-005: 発酵の表示

Preconditions:
JEIを導入し、FirmaLife導入・未導入の構成でFermenterカテゴリを開く。

Action:
通常食品、穀粒タグ、Raw Honeyの製法画面を撮影する。

Expected Result:
食品・穀粒の80mBとRaw Honeyの250mBを区別して表示する。Raw HoneyはFirmaLife導入時だけ表示し、種子からPlant Oilを得る旧Fermenterレシピを表示しない。詳細仕様第21章と第32章の表示条件も合否対象とする。検証方式はE2Eとする。

### F-018: 非金属加工と薬液

#### Description

Coke Oven、非金属Crusher、TFC Chisel・Heating、基礎薬液、およびFirmaLife追加6件を実装する。鉱石CrusherはTFCMの管理範囲を維持し、AFC LatexのIE代替加工を追加しない。詳細仕様第23章の全表行・条件を適用する。

#### Acceptance Criteria

##### AC-F018-001: Coke Ovenの数量と時間

Preconditions:
Bituminous CoalまたはLigniteを1個、16個、17個投入したCoke Ovenを用意し、固体・流体出力へ十分な容量を確保する。

Action:
各条件で1処理を完了させる。

Expected Result:
原料1個につきCoke1個とCreosote500mBを生成し、1処理の原料上限は16個、時間は6,000ticksとなる。原料16個ではCoke16個とCreosote8,000mBとなり、17個では1個を残す。原料1個でも時間は6,000ticksとなる。FE、温度、任意Mod条件を追加しない。詳細仕様第23章のCoke Oven2レシピを合否対象とする。検証方式はGameTestとする。

##### AC-F018-002: Coke Ovenの実残容量

Preconditions:
出力Cokeが0個・1個・60個・64個の状態、異種アイテムまたは異なるComponentsの状態、Creosote残容量が500mB未満の状態を用意する。

Action:
原料16個を投入して処理数を観測し、加工中の搬入出も実施する。

Expected Result:
処理数は`min(16, N_input, S_solid, floor(S_fluid / 500))`となる。上限64でCoke60個なら最大4個を追加し、Coke1個という既存個数を処理上限へ転用しない。異種出力・非互換Components・流体受入不能・処理数0では新規開始しない。完了時の実残容量を超える加算、部分消失、過剰消費を生じない。詳細仕様第23章の容量条件を合否対象とする。検証方式はGameTestとする。

##### AC-F018-003: 非金属Crusherの全24件

Preconditions:
Charcoal、Fluxstoneタグの素材、腐敗前後のOlive、およびRaw・Cut・Smooth各7色の砂岩を用意する。

Action:
全24レシピを加工し、砂岩の副産物判定を制御した乱数で検査する。

Expected Result:
Charcoal1個からCharcoal Powder4個を1,600 FE、Fluxstone1個からFlux2個を1,600 FE、Olive1個からOlive Paste2個を800 FEで生成する。砂岩21件は対応色のSand2個を3,200 FEで生成し、Saltpeter1個を50%で追加する。基礎処理長は全件50となり、実経過50ticksへ固定しない。Oliveを腐敗条件で除外しない。詳細仕様第23章の全24行・進行上限も合否対象とする。検証方式はGameTestとする。

##### AC-F018-004: Crusherの管理境界

Preconditions:
TFCM鉱石加工、羊毛、Wrought Iron Ingotを含むレシピ一覧を用意する。

Action:
初期読込とデータ再読込の後に、TFIEのレシピ集合と機械照合を検査する。

Expected Result:
鉱石加工はTFCMが提供する工程だけを使用し、TFIE独自の鉱石追加・数量変更・不足系列補完・Quern無効化の重複定義を行わない。Wrought Iron IngotからDustへのCrusherレシピも追加しない。`immersiveengineering:crusher/wool`を無効化し、代替糸レシピを追加しない。詳細仕様第23章と第33章の管理境界を合否対象とする。検証方式はGameTestとする。

##### AC-F018-005: ChiselとHeating

Preconditions:
Concrete、Concrete Brick、Leaded Concrete、Concrete Tile、レンガブロック`minecraft:bricks`、IE Slagを用意する。

Action:
4ブロックをSlab・Stairの各modeで加工し、Heating入力を指定温度の直前と到達時へ加熱する。

Expected Result:
Chisel8件ではSlab加工が設置1個と追加Item1個で合計2個、Stair加工が設置1個のみとなる。Heatingはレンガブロック1個からClinker Brick1個を1,499℃、Slag1個からSlag Glass1個を380℃で生成する。レンガアイテム`minecraft:brick`を入力に使用せず、固定処理時間・FE・独自温度条件をChiselへ追加しない。詳細仕様第23章の全10行を合否対象とする。検証方式はGameTestとする。

##### AC-F018-006: 基礎薬液と流体

Preconditions:
詳細仕様第23章のBarrel・Mixer・Refinery用材料と流体を用意する。

Action:
Redstone Acid、Herbicide、Limewater、Salt Water、Brine、FirmaLife不在時のMilk Vinegarを各1処理実行する。

Expected Result:
Redstone Dust1個とWater250mBはBarrelで即時にAcid250mBとなる。HerbicideはSulfur1個、指定銅鉱石Powderのいずれか1個、Ethanol500mBから500mBを3,200 FEで生成する。銅原料を100mBのCopper Dustへ置換しない。LimewaterはFresh Water500mBとFluxまたはLime1個から500mB、Salt WaterはFresh Water125mBとSalt1個から125mBを各800 FEで生成する。BrineとMilk VinegarはVinegar1mBと対応流体9mBから10mBを50 FE・1tickで生成する。Mixerの基礎処理長は出力mB数であり、同じ実経過tick数へ固定しない。詳細仕様第23章の全6行・条件を合否対象とする。検証方式はGameTestとする。

##### AC-F018-007: FirmaLifeのMixer4件

Preconditions:
FirmaLife導入・未導入の構成を用意する。導入時はOlive Paste、Blubber、Wood Ash、腐敗前後のButterと対応流体を用意する。

Action:
Olive Oil Water、Tallow、Lye、CaramelのMixer加工とレシピ読込を検査する。

Expected Result:
導入時だけ4件を有効にする。前三者は各固体1個とWater200mBから対応流体200mBを800 FE・基礎処理長200で生成する。Caramelは非腐敗Butter1個とSugar Water1,000mBからCaramel1,000mBを3,200 FE・基礎処理長1,000で生成し、腐敗Butterを拒否する。元のVatの300℃・600ticksと燃料条件をMixerへ追加せず、元レシピも維持する。詳細仕様第23章の全4行を合否対象とする。検証方式はGameTestとする。

##### AC-F018-008: FirmaLifeの蜂蜜と乳

Preconditions:
FirmaLife導入時にWild Honeycomb、Raw Honey、Milk・Goat Milk・Yak MilkとVinegarを用意し、未導入構成も用意する。

Action:
Wild HoneycombのSqueezer加工、Milk VinegarのRefinery加工、条件付き読込を検査する。

Expected Result:
Wild Honeycomb1個からRaw Honey1個だけを6,400 FE・80ticksで生成し、流体・蜂枠入力・追加副産物を設けない。乳は`#firmalife:milks`9mBとVinegar1mBからMilk Vinegar10mBを50 FE・1tickで生成する。FirmaLife導入用と未導入用のMilk Vinegarレシピを同時に有効にしない。元のCentrifuge・Barrel加工と既存Raw Honey発酵を維持する。詳細仕様第23章の2レシピと導入条件を合否対象とする。検証方式はGameTestとする。

##### AC-F018-009: AFC LatexとRubberの境界

Preconditions:
AFCを導入し、FirmaLifeの有無を切り替える。AFCのPot、Vat、Glue、Sealed BricksおよびTapデータを読み込む。

Action:
TFIE導入前後の対象レシピ・タグ・Tap条件を照合する。

Expected Result:
LatexからRubberへのIE機械経路、Rubber使用のConveyor・LV/MV Wire Coil、RubberのLeather・Fabric・Plasticタグ追加、LatexとPhenolic Resinの同一視を行わない。AFCの既存5経路を重複追加・上書きせず、Hevea14〜50℃で2mB、Rubber Fig6〜50℃で1mBという自然原木からの採液条件を維持する。IEのAcetaldehydeとCreosoteによるResin製造を維持する。詳細仕様第23章のAFC全表行・条件を合否対象とする。検証方式はGameTestとする。

##### AC-F018-010: 非金属レシピの表示

Preconditions:
JEIを導入し、基本構成とFirmaLife導入構成を用意する。

Action:
Coke Oven、Crusher、Chisel、Heating、Mixer、Squeezer、Refineryの採用レシピを撮影する。

Expected Result:
実際の機械カテゴリ、数量、流体、確率副産物、Heating温度を表示する。FirmaLifeの6件は導入構成だけに表示し、Caramelの非腐敗条件とHoneycombの固体のみの出力を識別する。羊毛Crusher、重複Milk Vinegar、AFCのIE代替経路を表示しない。詳細仕様第23章と第32章の表示条件も合否対象とする。検証方式はE2Eとする。

### F-019: External HeaterによるCrucible加熱

#### Description

IE External HeaterとTFC CrucibleをCapabilityで接続する。独立したElectric Crucibleを追加しない。詳細仕様第24章の全表行・条件を適用する。

#### Acceptance Criteria

##### AC-F019-001: 六方向の加熱と費用

Preconditions:
起動条件を満たす蓄電量のExternal HeaterへCrucibleを6方向から個別に隣接させ、Redstone入力をなくす。標準設定を使用する。

Action:
各方向の加熱呼出しを実行し、電力、目標温度、安定保持時間、同期を観測する。

Expected Result:
全6方向でCrucible1基につき20 FE/tを消費し、目標温度2,000℃、安定保持時間5ticksを設定する。Crucible内部が空でも、目標温度到達後でも、成立中は消費を継続する。実温度はTFCの通常変化で上昇し、瞬時に2,000℃へ変更しない。詳細仕様第24章の加熱条件を合否対象とする。検証方式はGameTestとする。

##### AC-F019-002: 始動と蓄電

Preconditions:
停止中のHeaterへ20 FE、3,200 FE、3,200 FEを超える電力を個別に保持させる。

Action:
隣接Crucibleの加熱呼出しと電力受入を観測する。

Expected Result:
停止中は蓄電量が3,200 FEを超えてから呼出しを開始し、20 FEだけで直ちに始動しない。蓄電容量32,000 FE、標準入力上限256 FE/t、およびIE消費設定がそれを上回る場合の上限処理を維持する。詳細仕様第24章のIE側始動・蓄電条件を合否対象とする。検証方式はGameTestとする。

##### AC-F019-003: 停止条件と冷却

Preconditions:
Crucibleを加熱中とし、電力不足、Redstone入力、Heater撤去を個別に発生させる。

Action:
加熱コールとその後のCrucible更新を観測する。

Expected Result:
加熱コール内の電力不足またはRedstone入力では目標温度0、当該コールの電力消費0となる。撤去等で呼出しが止まる場合は、Crucible自身の安定時間と通常減衰・冷却を適用する。実温度を瞬時に0へ変更しない。詳細仕様第24章の停止条件を合否対象とする。検証方式はGameTestとする。

##### AC-F019-004: 複数Crucibleと設定値

Preconditions:
同じHeaterへ複数のCrucibleを隣接させ、標準値とTFIE設定を変更した構成を用意する。

Action:
同一tickの呼出し回数、消費電力、各Crucibleの目標温度を測定する。

Expected Result:
Crucibleごとの呼出しに対して設定電力を消費し、標準2基なら成立した2呼出しで40 FEを消費する。温度を台数分加算しない。設定値は対象ごとに同じ規則で適用する。詳細仕様第24章と第31章の関連設定を合否対象とする。検証方式はGameTestとする。

### F-020: 回転動力による発電

#### Description

IE DynamoをTFC回転ネットワークのSinkとして接続する。回転速度・TFIE倍率・IE出力倍率を分離し、詳細仕様第25章の全条件を適用する。

#### Acceptance Criteria

##### AC-F020-001: 接続面とライフサイクル

Preconditions:
TFCの風車・水車から回転を伝達し、向きの異なるDynamoを配置する。

Action:
各面から接続し、読込、チャンクアンロード、再読込、ブロック撤去を実行する。

Expected Result:
Dynamoの向きと同じ面を回転入力とする。読込時にSinkを登録し、アンロードと撤去時に除去する。再読込による二重登録や撤去後の残存発電を生じない。詳細仕様第25章のネットワーク条件を合否対象とする。検証方式はGameTestとする。

##### AC-F020-002: 正逆回転と発電量

Preconditions:
標準TFIE倍率m=1.0、IE設定d=3で、回転速度ωを0.04、-0.04、0 rad/tickとする。別試行として回転ネットワークが未接続でRotationを取得しない状態を用意する。

Action:
各tickにIEへ渡す回転入力と受電先への供給候補を測定する。

Expected Result:
回転入力は`abs(ω × 500 × m)`、供給候補は`int((d / 3) × abs(ω × 500 × m))`となる。正逆0.04 rad/tickはいずれも20 FE/tとなり、非負値の端数を切り捨てる。有効なRotationがありω=0のtickでは回転入力0・供給候補0となる。Rotationを取得しないtickでは連携入力を呼び出さない。詳細仕様第25章の数式を合否対象とする。検証方式はGameTestとする。

##### AC-F020-003: 設定倍率と受電上限

Preconditions:
TFIE倍率mとIE設定dを別々に変更し、供給候補より小さい受入量の機器を隣接させる。

Action:
連続発電と受電先の交換を行う。

Expected Result:
TFIE倍率は回転入力へ作用し、IE設定を置換しない。複数受電先へIEの規則で配分し、受入量を超える電力をDynamo内部へ蓄積しない。詳細仕様第25章と第31章の関連設定を合否対象とする。検証方式はGameTestとする。

### F-021: Crateの収納制限

#### Description

Wooden CrateとReinforced CrateにTFCサイズ判定を適用し、IE固有の収納禁止も維持する。詳細仕様第26章の全条件を適用する。

#### Acceptance Criteria

##### AC-F021-001: サイズ境界

Preconditions:
両Crateと、TFCのTINYからHUGEまでの各サイズに属する通常収納許可アイテムを用意する。最大許容値を既定のVERY_LARGEとする。

Action:
各アイテムを収納する。

Expected Result:
ItemSizeManagerが返すサイズを使用し、VERY_LARGE以下を許可し、HUGEを拒否する。重量を追加制限にしない。詳細仕様第26章のサイズ順序を合否対象とする。検証方式はGameTestとする。

##### AC-F021-002: IEの収納禁止

Preconditions:
サイズ条件内でもコンテナ収納を許可しないアイテム、および`#immersiveengineering:forbidden_in_crates`のアイテムを用意する。

Action:
Wooden CrateとReinforced Crateへ搬入する。

Expected Result:
両アイテム群を拒否し、TFIEのサイズ条件でIE側の拒否を解除しない。詳細仕様第26章のIE条件を合否対象とする。検証方式はGameTestとする。

##### AC-F021-003: 操作方法と設定の一致

Preconditions:
サイズ境界上・境界外のアイテムと両Crateを用意し、許容サイズ設定も変更する。

Action:
通常GUI操作、Shift移動、自動搬入の各経路で収納を試みる。

Expected Result:
全搬入経路が同じサイズ・IE禁止条件を使用し、設定変更も同じ判定へ反映する。拒否されたアイテムを消費・複製しない。詳細仕様第26章と第31章の関連設定を合否対象とする。検証方式はGameTestとする。

### F-022: 流体と容器の互換

#### Description

IEの10流体をTFC容器へ対応させ、IEの流体Ingredient候補へTFC空容器を反映する。固定Bucketクラフトと区別し、詳細仕様第27章の全表行・条件を適用する。

#### Acceptance Criteria

##### AC-F022-001: 10流体の容器タグ

Preconditions:
詳細仕様第27章のIE流体10種、TFC Barrel、Wooden Bucket、Red Steel Bucketを用意する。

Action:
Fluidタグを検査し、各容器へ流体を充填・排出する。

Expected Result:
10流体すべてを`tfc:usable_in_barrel`、`tfc:usable_in_wooden_bucket`、`tfc:usable_in_red_steel_bucket`へ追加し、既存タグ内容・容器容量・通常入出力を維持する。対象はCreosote、Plant Oil、Ethanol、Biodiesel、High Power Biodiesel、Concrete、Herbicide、Redstone Acid、Acetaldehyde、Phenolic Resinとなる。詳細仕様第27章のID全行を合否対象とする。検証方式はGameTestとする。

##### AC-F022-002: Fluid Ingredientの判定と返却

Preconditions:
`#tfc:fluid_item_ingredient_empty_containers`の各容器から、対象流体を受け入れ、必要量以上を保持するものと、受入不可・容量不足のものを用意する。

Action:
IEのIngredientFluidStackの候補生成、材料照合、クラフト後の容器返却を実行する。

Expected Result:
候補は実際のFluid Capabilityと必要量で絞る。Item名だけで照合せず、流体の種類と保持量で成立を判定する。1容器分のコピーから必要量だけを排出し、残量を含む容器を返す。タグ所属だけを理由に全流体を許可しない。詳細仕様第27章の候補・排出条件を合否対象とする。検証方式はGameTestとする。

##### AC-F022-003: 固定Bucketとの区別

Preconditions:
Chemthrower、Jerrycanの一般クラフト、および各種TFC Bucketを用意する。

Action:
固定`minecraft:bucket`入力をTFC Bucketで置き換えてレシピ照合する。

Expected Result:
IngredientFluidStack互換を固定アイテム材料へ拡張せず、固定Bucket指定を維持する。TFC Bucketによる代替経路をTFIEから追加しない。詳細仕様第15章と第27章の固定Bucket条件を合否対象とする。検証方式はGameTestとする。

##### AC-F022-004: 流体材料の表示候補

Preconditions:
JEIを導入し、必要流体量の異なるIE流体Ingredientレシピを開く。

Action:
容器候補の切替を撮影する。

Expected Result:
TFC空容器タグから、実際に対象流体と必要量を満たす容器だけを候補へ表示する。受入不可・容量不足の容器を表示せず、流体量と容器モデルが一致する。固定Bucket入力は固定Bucketとして表示する。詳細仕様第27章と第32章の表示条件も合否対象とする。検証方式はE2Eとする。

### F-023: 除草剤の作用

#### Description

`#c:herbicide`にChemthrower効果を登録し、命中地表と直上植物を処理する。詳細仕様第28章の全表行・条件を適用する。

#### Acceptance Criteria

##### AC-F023-001: TFC地表と作物

Preconditions:
同じ土壌VariantのTFC Grass、Farmland、Clay Grassを用意し、各直上へ未成熟・成熟の栽培株、枯死株、野生株を配置する。

Action:
地表へHerbicideを命中させる。

Expected Result:
GrassとFarmlandは同じVariantのDirtへ、Clay Grassは同じVariantのClayへ変わる。Grass・Farmland上の栽培株は成長度1以上かで成熟状態を判定して枯死へ変換し、ドロップを伴って破壊する。Clay Grassでは栽培株を枯死へ変換する専用分岐を使用しない。枯死・野生作物の扱いは詳細仕様第28章の各行に一致する。検証方式はGameTestとする。

##### AC-F023-002: 葉と通常植物

Preconditions:
Leavesタグのブロック、SnowyDirtBlock、FarmBlock、通常BushBlock、SeasonalPlantBlockと対象外ブロックを用意する。

Action:
各地表・葉へHerbicideを命中させる。

Expected Result:
葉は命中ブロックだけをドロップなしで除去する。TFC地表の通常BushBlockはSeasonalPlantBlockを除いてドロップなしで除去する。その他のSnowyDirtBlock・FarmBlockはMinecraft Dirtへ変わり、直上BushBlockをドロップなしで除去する。対象外ブロックへ独自の地表変更・植物破壊を追加しない。詳細仕様第28章の全分岐を合否対象とする。検証方式はGameTestとする。

##### AC-F023-003: 命中範囲とエンティティ

Preconditions:
地表の周囲に作物を配置し、作物本体への直接命中とLivingEntityへの命中を用意する。命中AABBの0.25ブロック拡張範囲内外へChemthrowerShotEntityを置く。

Action:
Herbicideの命中処理を実行する。

Expected Result:
地表と直上以外の広域作物破壊を追加せず、作物本体への直接命中だけで同じ地表処理を起動しない。LivingEntityへTFIE独自の追加効果を与えない。ブロック効果後、命中AABBを各方向へ0.25拡張した範囲のChemthrowerShotEntityを除去する。詳細仕様第28章の作用範囲を合否対象とする。検証方式はGameTestとする。

### F-024: 肥料の互換

#### Description

IE素材3種のTFC農地用肥料と、TFC素材8種のCloche用肥料を登録する。両者のN/P/Kと成長倍率を区別し、詳細仕様第29章の全表行・条件を適用する。

#### Acceptance Criteria

##### AC-F024-001: 農地への栄養量

Preconditions:
栄養に十分な空きのある通常TFC農地と、IE Saltpeter Dust、Fertilizer、Slagを用意する。

Action:
各素材1個を通常使用する。

Expected Result:
SaltpeterはN/P/K=0.2/0/0、Fertilizerは0.1/0.2/0.5、Slagは0/0.2/0.1を与える。各FertilizerデータIDは`tfie:dust_saltpeter`、`tfie:fertilizer`、`tfie:slag`となる。詳細仕様第29章の全3行を合否対象とする。検証方式はGameTestとする。

##### AC-F024-002: 土壌補正と消費

Preconditions:
非常に肥沃・肥沃・通常・貧栄養・非常に貧栄養の農地、対象栄養が満量の農地、通常・Creativeプレイヤーを用意する。

Action:
通常使用とShift使用を行い、栄養量と消費数を観測する。

Expected Result:
土壌倍率1.2・1.1・1.0・0.9・0.8をそれぞれ適用する。通常使用は1個を消費し、Shift使用は対象栄養のいずれかを満たす必要数と所持数に従う。全対象栄養が満量なら消費せず、Creativeでも消費しない。ゲージ上限を維持する。詳細仕様第29章のTFC消費条件を合否対象とする。検証方式はGameTestとする。

##### AC-F024-003: Clocheの8肥料

Preconditions:
Clocheに必要な種・土壌・Water・電力を用意し、8種のTFC肥料を個別に供給する。

Action:
養液補給時に肥料を投入し、成長加算量を測定する。

Expected Result:
Compost1.2、Guano1.3、Pure Nitrogen1.2、Pure Phosphorus1.2、Pure Potassium1.2、Shellfish1.1、Sylvite1.2、Wood Ash1.1の成長倍率となる。レシピ型は`immersiveengineering:fertilizer`とし、N/P/K量として適用しない。詳細仕様第29章のID全8行と第19章の施肥周期・消費を合否対象とする。検証方式はGameTestとする。

##### AC-F024-004: Shellfishと既存施肥処理

Preconditions:
腐敗前後のShellfishとCloche、上段を持つHempと農地を用意する。

Action:
ShellfishによるCloche施肥と、IE肥料によるTFCの通常施肥を実行する。

Expected Result:
ShellfishのレシピはItem IDで照合し、腐敗後も倍率1.1を適用する。Clocheの水・電力・投与期間は通常処理を維持し、農地施肥と混同しない。Hempの上段操作は対応農地へ作用する。詳細仕様第18章、第19章、第29章の関連条件を合否対象とする。検証方式はGameTestとする。

### F-025: サーバー設定

#### Description

Global Requirementsに全文収録する確定仕様第31章の全行を合否対象とする。TFIEの4設定をSERVER区分へ登録し、`crucibleExternalHeater.fePerTick`は整数20・範囲0〜32,000、`crucibleExternalHeater.temperature`は整数2,000・範囲0〜2,147,483,647、`compatibility.crateMaximumItemSize`はTFC Size列挙値`VERY_LARGE`、`compatibility.tfcRotationalEnergyModifier`は倍精度浮動小数1.0・範囲`Double.MIN_NORMAL`〜`Double.MAX_VALUE`とする。SizeはTINY、VERY_SMALL、SMALL、NORMAL、LARGE、VERY_LARGE、HUGEの7値を認める。各設定が実際のゲーム処理へ適用されることを確認し、設定宣言の静的確認だけで完了しない。レシピ数量・FE・時間・温度条件の調整は、個別章で定義したデータパック方式を維持する。

#### Acceptance Criteria

##### AC-F025-001: 既定値のゲーム処理への適用

Preconditions:
TFIEのSERVER設定を未変更とし、IEのDynamo倍率を3とする。十分なFEを持つExternal HeaterへCrucibleを隣接させ、回転速度0.04rad/tickのDynamoと、IE固有の収納拒否条件に該当しないサイズ別アイテムを用意する。

Action:
Heaterを稼働させ、Dynamoへ回転を伝達し、Wooden CrateとReinforced CrateへVERY_LARGEとHUGEの各アイテムを通常操作と自動搬入で投入する。

Expected Result:
SERVER設定の有効値は20、2,000、VERY_LARGE、1.0となる。HeaterはCrucible1基につき20FE/tを消費して目標温度2,000℃を設定する。Dynamoの供給候補は20FE/tとなる。両CrateはVERY_LARGEを受け入れ、HUGEを拒否する。検証方式: GameTest。

##### AC-F025-002: Heater設定の変更と零消費

Preconditions:
SERVER設定を順に消費40FE/t・目標1,500℃、消費0FE/t・目標1,500℃へ変更したワールドを用意する。Heaterの32,000FE容量と起動前3,200FE超の判定はIE標準のままとする。

Action:
各設定で、十分に蓄電したHeaterからCrucibleを加熱する。零消費設定では、停止中で蓄電量が起動閾値以下のHeaterも別に確認する。稼働中にRedstone入力を与える。

Expected Result:
40FE/t設定では各Crucible呼出しが40FEを消費し、目標温度1,500℃を設定する。零消費設定では設定上の加熱費用が0となるが、停止中のIE起動判定を解除しない。Redstone入力を加熱コール内で検出すると目標温度0・当該コールの消費0となる。実温度を目標温度へ瞬時に置換しない。実行中の設定宣言も照合し、消費の型・範囲を整数0〜32,000、目標温度を整数0〜2,147,483,647とする。検証方式: GameTest。

##### AC-F025-003: Crateサイズ上限の変更

Preconditions:
両Crateのサイズ上限をNORMAL、続いてHUGEへ変更する。各TFC Sizeのアイテムに加え、サイズは条件内だがIEのコンテナ内収納禁止または`#immersiveengineering:forbidden_in_crates`に該当するアイテムを用意する。

Action:
通常操作と自動搬入を使って各アイテムを投入し、収納結果を確認する。

Expected Result:
NORMAL設定ではTINY〜NORMALをサイズ条件上受け入れ、LARGE以上を拒否する。HUGE設定では全7サイズをサイズ条件上受け入れる。どちらの設定でもIE固有の拒否を解除せず、重量による新しい拒否条件を追加しない。通常操作と自動搬入の結果が一致し、有効な設定値は定義した7列挙値のいずれかとなる。検証方式: GameTest。

##### AC-F025-004: 回転倍率の適用と範囲

Preconditions:
SERVER設定の回転倍率を2.0、IEの`dynamo_output`を3とする。受電先が供給候補全量を受け入れるDynamoを用意する。

Action:
0.04rad/tickと−0.04rad/tickをそれぞれ与え、回転を切断する。ゲーム内で読み込まれた設定定義の型・既定値・許容範囲も照合する。

Expected Result:
正転と逆転のどちらも`int((3/3)×abs(0.04×500×2))`に従って40FE/tを供給候補とする。切断後はTFIEの回転入力を行わない。倍率の型は倍精度浮動小数、既定値は1.0、許容範囲は`Double.MIN_NORMAL`以上`Double.MAX_VALUE`以下となり、0と負数を有効な設定値として採用しない。検証方式: GameTest。

### F-026: ゲーム内ガイドと表示

#### Description

Global Requirementsに全文収録する確定仕様第32章の全表行と、第34章の「表示と資源」にあるガイド・JEI・表示要件を合否対象とする。`en_us`、IE Engineer’s Manual、TFC Field Guide、Arc Blueprintの表示、JEIの機械カテゴリと非消費触媒を実際の処理条件へ一致させる。カテゴリとEntry ID、並び順の設定、動的参照キーを維持する。作物・アイテム・機械そのものの描画は各対象Featureで扱い、本Featureはガイド・JEI・表示情報を扱う。

#### Acceptance Criteria

##### AC-F026-001: 両ガイドの項目と内容

Preconditions:
`en_us`を選択したクライアントで、新規ワールドへ参加し、IE ManualとTFC Field Guideを入手する。

Action:
両ガイドのTFIEカテゴリを開き、金型、薬液、マルチブロック、Blueprint、Jute互換、Hemp、Herbicide、External Heater、Drill Head、Kinetic Dynamoの全10項目を順に表示する。

Expected Result:
IEカテゴリは`tfie:tfc_ie_changes`、TFCカテゴリは`tfc:tfc_ie_changes`となり、第32章の全Entry IDで対応する項目が開く。IEのautoload priority4・category weight8、TFCのsortnum10を使用する。説明は8mode、主入力配置・非消費、Briquet限定、Wrought Iron→Steel、90%温度、4形状、Hemp栽培条件と野生地下水条件を反映する。高炉・Alloy Kilnの製作や形成を禁止する旧説明を残さない。検証方式: E2E。

##### AC-F026-002: 動的な設定値とレシピ値の表示

Preconditions:
HeaterのSERVER設定を35FE/t・1,900℃とする。上位データパックで一つの採用レシピのFE・処理時間を既定値から変更し、データ再読込を完了する。

Action:
実際にHeaterを稼働させ、変更したレシピを機械で開始する。IE Manualの該当説明と、当該レシピを表示するJEI・ガイドの動的表示を開く。

Expected Result:
`crucibleExternalHeaterFEPerTick`と`crucibleExternalHeaterTemperature`は実際のサーバー値35と1,900を表示し、ゲーム処理と一致する。動的なレシピ表示には再読込後のFE・時間を反映し、標準出荷値を変更後の実数値として表示しない。未知の設定参照キーを実数値として表示しない。検証方式: GameTestおよびE2E。

##### AC-F026-003: BlueprintのmodeとArcレシピ表示

Preconditions:
JEIを導入し、全対象金属とFirmaLifeを有効にする。Arc用BlueprintとArcの全19合金・Steelレシピ、BriquetとInsulating Glassの基本加工を用意する。

Action:
Blueprintをメインハンドで選択し、空中への右クリックで1〜8を一巡する。各状態のツールチップと切替表示を確認し、JEIで全19レシピと基本加工を開く。

Expected Result:
表示するmodeはサーバー上の整数Data Componentと一致し、8の次は1を表示する。合金・Steelには第9・10章の固定modeを持つBlueprintを非消費触媒として表示する。CopperのBrass/Bronze/Bismuth Bronze/Black Bronze/Constantanは順にmode1/2/3/4/5となる。BriquetとInsulating GlassにはBlueprint必須条件を表示せず、mode6〜8へ架空の合金を割り当てない。設定用GUIを開かない。検証方式: E2E。

##### AC-F026-004: JEIの機械カテゴリと削除済みレシピ

Preconditions:
JEIを導入し、採用するSqueezer、Fermenter、Arc、Crusherと各削除対象の材料を用意する。

Action:
種子23種の用途、食品とRaw Honeyの発酵、WoolのCrusher用途、旧ArcのOre・Dust・Recycling用途をJEIで調べ、対応する有効レシピを機械で処理する。

Expected Result:
種子23種はSqueezerカテゴリでPlant Oil80mBを表示し、旧誤登録のFermenterレシピとして表示しない。食品とRaw HoneyはFermenterでEthanol80mB・250mBの該当量を表示し、処理結果と一致する。TFIEが削除したWool→String、旧鉱石・Dust製錬、Arc RecyclingをJEIに残さない。表示から既存の別用途まで一括除去しない。検証方式: GameTestおよびE2E。

##### AC-F026-005: 連携ModとJEIの有無

Preconditions:
FirmaLife、Advanced TFC Tech Unofficial、AFC、Overdimsをそれぞれ導入・除外した構成と、JEI未導入の構成を用意する。

Action:
各構成でクライアントを起動してワールドへ入り、両ガイドの関連項目とBlueprintの通常表示を開く。

Expected Result:
導入状態に一致した素材・製材・構造・食品連携を説明する。未登録Item・Fluidの参照によって画面が停止しない。JEI未導入でもTFIE、両ガイド、Blueprintの通常表示が動作し、JEIをTFIEの必須依存として要求しない。検証方式: E2E。

### F-027: 削除・置換台帳

#### Description

Global Requirementsに全文収録する確定仕様第33章の全表行、テンプレート展開集合、固定Recipe ID、例外、初回起動・再読込条件を合否対象とする。基準IE 12.4.2-194の固定commitに対する台帳を用い、「削除」と「標準経路を除去して採用配合へ置換する処置」を区別する。件数だけを照合して対象IDの誤りを見逃さない。元レシピ一覧・機械検索・JEI・実処理のすべてへ適用し、対象外の上流機能を名前の類似だけで巻き込まない。

#### Acceptance Criteria

##### AC-F027-001: Crusher54件の削除と対象外の維持

Preconditions:
第33章に定義した22種の`ore_{M}`、15種の`raw_ore_{M}`、15種の`raw_block_{M}`、`nether_gold`、`wool`を照合集合とする。TFCMの正規鉱石加工と、IEの対象外Ingot粉砕、TFIEの非金属加工も有効にする。

Action:
サーバーのレシピ検索結果を全54件で照合し、該当原料をCrusherへ投入する。対照として有効なTFCM加工、Charcoal・Fluxstone・Olive・Sandstone加工を実行する。

Expected Result:
標準54件の削除対象から加工が開始せず、Wool1個→String4個の経路も成立しない。TFCMの定義と採用非金属加工は残る。`ingot_{M}`を鉱石削除へ巻き込まず、Wrought IronのCrusher経路をTFIEから追加しない。Nether Quartzは`ore_quartz`で除去し、存在しない`crusher/nether_quartz`を実在レシピとして数えない。検証方式: GameTest。

##### AC-F027-002: Arc静的78件と動的Recyclingの除去

Preconditions:
第33章の金属鉱石15種×3形態、Dust23種、標準合金7件、Netherite Scrap、Steel、Insulating Glassを照合集合とする。IEの動的レシピ生成を通常どおり通した新規ワールドを使用する。

Action:
起動直後とデータ再読込後に全静的78件と動的Arc Recyclingの機械検索を確認し、標準Ore・Raw Ore・Dust・Ancient Debris・リサイクル用金属製品をArcへ投入する。対照として採用Briquet・Steel・Glass加工を行い、MixerとBottling MachineのPotion生成器も確認する。

Expected Result:
削除対象の標準加工は開始せず、置換対象はTFIEの採用配合だけで処理する。`immersiveengineering:arc_recycling_list`入口と、生成器が作る全`ArcRecyclingRecipe`がArcの利用対象から外れる。初回起動・再読込後とも復活しない。Potion用のMixer・Bottling生成器と、TFCMのTFC Heatingを巻き込まない。検証方式: GameTest。

##### AC-F027-003: 高炉とAlloy Kilnの置換範囲

Preconditions:
通常高炉、改良高炉、Alloy Kilnを形成し、鉄3Briquet、Iron Ingot/Block、単体Coke・Charcoalと各Block、Alloy Kilnの全採用入力を用意する。

Action:
各高炉の製錬と燃料判定を実行し、Alloy KilnのBrass、Bronze、Constantan、Electrum、Invar、Andesite Alloy2経路、Insulating Glassを処理する。Rose GoldとManyullynの標準Alloy Kiln配合も照合する。

Expected Result:
高炉の標準Steel2件とBlock燃料2件は存在せず、単体Coke1,200ticks・Charcoal300ticksを維持する。鉄3BriquetはRaw Iron Bloomだけを出力し、Slagを出さない。Alloy Kilnの採用8配合は全て200ticksとなり、Rose Gold・Manyullynの代替を追加しない。Arcの採用Rose Goldまで削除しない。Blast Brick、Reinforced Blast Brick、Alloy Brickは採用クラフトで製作し、炉を形成する。検証方式: GameTest。

##### AC-F027-004: 形状加工とクラフトの個別削除

Preconditions:
Metal Press標準64件、手加工Plate/Rod8件、Nugget8件、金属圧縮Block16件、粉末火薬1件、旧TFIEのBlock金型等3件を、台帳の完全ID集合から展開する。

Action:
各旧入力を対応する機械・クラフトへ与え、旧Recipe IDの残存と処理結果を照合する。対照としてTFIEのCopper Wire、Sheet金型、TFCMの既存Nugget変換を処理する。

Expected Result:
全削除・置換元が旧配合のまま成立せず、旧Block金型とBlockプレスを出荷しない。TFIEのWireはIngot1個→4個となり、IE標準の2個出力を残さない。Sheet金型とその加工を維持する。TFCMのIngot1個対Nugget9個とNugget10mBの既存例外を変更せず、IE圧縮Blockの9Ingot経路を代替追加しない。検証方式: GameTest。

##### AC-F027-005: 名前空間と除外対象の維持

Preconditions:
削除対象と同じパスを持ち別名前空間に属する対照レシピをテスト用データパックで追加する。基準構成で成立する対象外Metal Press加工、Raw OreとRaw Ore Blockの保管変換、Block/Slab加工、Beneath以外の製材を用意する。

Action:
対象外の対照レシピを実処理し、対象外IDと登録条件を確認する。TFIE製材の全入力・出力名前空間もゲーム内の有効レシピから照合する。

Expected Result:
別名前空間の同名パスを削除しない。Metal PressのArdite・Manyullyn、Gear、Blaze Rod、Bullet Casing、Melonを今回の金属置換へ追加せず、それぞれの上流登録条件に従う。Raw Ore保管変換と既存Block/Slab加工を維持する。TFIE製材に`beneath:`木材を含めず、Overdimsは`tfcoverdims:`を使用する。旧TFIE0.0.3に存在しないBeneath製材IDを削除対象として捏造しない。検証方式: GameTest。

##### AC-F027-006: 再読込と素材無効化後の一貫性

Preconditions:
台帳全件の起動時検証を通した構成から、TFCMの対象金属または鉱石を無効化した構成へ切り替える。JEI導入クライアントも接続する。

Action:
条件を反映したサーバーを起動し、データ再読込を実行する。レシピ一覧、機械検索、JEIと実処理を全台帳集合で再照合する。

Expected Result:
対象素材のTFIE置換配合が省略されても、IE標準の置換元を救済経路として復活させない。起動時と再読込後で削除集合が一致し、JEIに存在する削除済み加工や、機械でだけ動作する削除済み加工を残さない。全名前空間や全機械を巻き込む一括削除を行わない。検証方式: GameTestおよびE2E。

### F-028: データ定義・保存・互換性

#### Description

Global Requirementsに全文収録する確定仕様第34章の全記述と、第5・10章のArc予約・保存・変更規則を合否対象とする。レシピ、タグ、気候、栄養、燃料、Item Heat、構造を各提供元のMinecraft1.21.1向けデータ形式で定義する。上位データパック、条件付き読込、サーバー側確定、専用サーバー、保存・再読込を実ゲーム状態で検証する。新規ワールドを前提とし、0.0.xのID・設定・機械保存状態の移行処理を設けない。描画とガイドの内容は該当Featureで検証する。

#### Acceptance Criteria

##### AC-F028-001: データパック上書きと新規処理

Preconditions:
上位データパックで、採用ArcレシピのFEと時間、別の採用機械レシピの数量または条件を変更する。標準出荷値を使用した対照ワールドを保持する。

Action:
データ再読込後に新規加工を開始し、必要材料・出力・消費FE・進行時間を測定する。同一経路の旧定義と新定義が重複するかも検索する。

Expected Result:
新規処理はそのワールドで有効なデータ値を用い、元の標準値をコードで強制しない。単一経路を上書きと追加の双方で重複登録しない。modeを要求するArc配合は上書き後も固定mode条件を照合する。利用者が追加した独自レシピはTFIE標準件数へ算入しない。検証方式: GameTest。

##### AC-F028-002: 条件不成立時の省略と記録

Preconditions:
Optional Mod不在、TFCM金属・鉱石無効化、参照Item・Fluidの不在という各条件を独立した構成で用意する。必須Modを備えた対照構成を保持する。

Action:
各サーバーを起動して対象レシピを検索し、関連機械へ材料を投入する。データ再読込も実施し、対象Recipe IDと読込結果を照合する。

Expected Result:
成立しない連携だけを読み込まず、対象Recipe IDと不成立理由をログへ記録する。存在しない入力を空の材料へ置換せず、Missing Registryエラーや無材料出力を発生させない。代替製法とmode番号の詰め直しを行わない。必須Mod自体の不在をOptional連携の省略として扱わない。検証方式: GameTest。

##### AC-F028-003: 専用サーバーでの状態確定

Preconditions:
描画環境を持たない専用サーバーを起動し、クライアントを接続する。Blueprint、合金・Briquet加工、確率副産物を持つ採用レシピを用意する。

Action:
クライアントからBlueprintを切り替えて加工を開始し、材料数・mode・進捗・温度・副産物をサーバー状態と表示で照合する。接続を切り、再接続する。

Expected Result:
Recipe選択、材料消費、乱数、副産物、温度、mode変更はサーバー側で確定する。再接続後もサーバーの状態へ一致し、クライアント側の二重処理による材料消失や生成物複製を生じさせない。共通処理からクライアント専用クラスを読み込むことによる専用サーバー停止がない。検証方式: GameTestおよびE2E。

##### AC-F028-004: Arc状態の保存と再開

Preconditions:
ArcにBlueprint1枚、複数の主入力、共有添加材、開始済み処理を用意する。通常進行、出力満杯、異温度の既存出力との非合流という各状態を記録する。

Action:
チャンクを再読込し、別試行ではサーバーを停止・再起動する。保存前後のInventory、mode、Recipe ID、配合、予約量、進捗、待機状態、Heat Componentを照合し、必要な出力空間を空けて処理を再開する。

Expected Result:
保存した各状態を保持し、再開後は通常の冷却と処理規則を適用する。開始済み材料を二重予約・二重消費せず、出力を複製・消失させない。温度を常温へ一律初期化せず、既存出力の再加熱・温度平均を行わない。Blueprintを消費しない。Arc固有の予約キューを他機械へ一律に新設しない。検証方式: GameTestおよびE2E。

##### AC-F028-005: Blueprint交換とレシピ定義変更の区別

Preconditions:
Arcで予約済みの合金処理を開始する。Blueprintを交換する試行、同一Recipe IDの入出力・数量・費用を変更する試行、当該Recipe IDを削除する試行を分けて用意する。

Action:
Blueprintを交換・取り外しして新規加工を投入する。別試行では処理中にデータ再読込を行い、無効化した定義を復旧する。待機中の機械解体による入力回収も確認する。

Expected Result:
Blueprint変更だけなら開始済み処理は元の選択を保持し、変更を新規開始分へ適用する。再読込で定義が変化・消失した処理は元の進捗と未消費材料を保持して待機し、追加材料・FEを消費しない。別配合へ転換せず、定義復旧または機械解体まで保持する。Blueprint着脱自体を禁止せず、原料を抜いた場合のIE通常中断とは区別する。検証方式: GameTest。

##### AC-F028-006: 新規ワールドと廃止登録

Preconditions:
TFIE0.1.0を新規ワールドへ導入する。0.0.xの設定・機械状態の移行を要求する前提を置かない。

Action:
ワールドを起動し、独自登録物、全有効レシピ、Blueprint Component、Sheet金型をゲーム内で取得・利用する。登録レジストリと導入説明も照合する。

Expected Result:
0.1.0の正規IDとデータだけで各機能が成立する。旧`tfie:mold_block`を移行目的で登録せず、旧Block金型・Blockプレスの3レシピも存在しない。0.0.xのItem ID、Recipe ID、設定、機械保存状態の変換機能を追加せず、導入説明は新規ワールドを前提とする。検証方式: GameTestおよびE2E。

### F-029: 統合受入

#### Description

Global Requirementsに全文収録する確定仕様第2・35・36章の全表行・全例外を合否対象とする。基準版はMinecraft1.21.1、NeoForge21.1.267、Java21、TFC4.2.16、IE12.4.2-194、TFCM2.2.1、Patchouli1.21.1-93-NEOFORGEとし、指定Optional・条件付き連携の基準版も使用する。Minecraft1.21.1のRelease/Betaおよび同Minecraft版の将来公開版を保守対象とし、Alpha・別Minecraft版を範囲へ含めない。未公開・未検証の将来版での動作保証を合格実績として記載しない。新規ワールドのクライアント・専用サーバーを実行し、旧版から維持する機能と変更する機能を区別して統合検証する。

#### Acceptance Criteria

##### AC-F029-001: 基本構成とRequired不足

Preconditions:
基準版のTFC・IE・TFCM・Patchouliを備えた最小構成、および各Required Modを一つずつ外した構成を用意する。

Action:
完全構成のGameTestでTFIEの機械加工とアイテム操作を実行する。E2Eではクライアントと専用サーバーを起動し、新規ワールドへ参加した後のTFIE機械・アイテム・表示を撮影する。Required不足の各構成は別の起動検査で起動拒否とログを確認し、E2Eシナリオ用runtimeは完全構成を使用する。

Expected Result:
完全構成ではワールド参加とTFIE処理が成立する。Required不足では該当依存を明示して起動を拒否し、部分動作したTFIEを利用させない。依存不足の起動ログは補助証拠として保持し、harnessのE2E起動失敗を成功扱いする実装を行わない。実行成果物のMod IDは`tfie`、バージョンは`0.1.0`となり、Java21・NeoForge・Minecraft1.21.1向けのメタデータと実行環境が一致する。正常構成の画像に未解決翻訳キー、missing texture、欠損モデルを生じない。検証方式: GameTestおよびE2E。

##### AC-F029-002: Optionalと依存宣言なしの連携

Preconditions:
最小構成、FirmaLife単独追加、Advanced TFC Tech Unofficial単独追加、AFC単独追加、Overdims単独追加、全4Mod追加の6構成を用意する。

Action:
各構成のクライアントと専用サーバーを起動し、FirmaLifeの素材・追加6加工、Grist MillとPower Loomの構造、AFC14樹種、Overdims2樹種の製材を該当構成で処理する。

Expected Result:
未導入連携が起動を妨げず、導入した対象だけが有効となる。FirmaLifeと`advancedtfctech`はOptionalとする。AFCとOverdimsは実行時の存在・参照Item確認だけで製材を有効にし、本番用ビルド、ModメタデータのRequired/Optional、配布依存のいずれにも依存宣言を持たない。Beneath木材を追加しない。検証方式: GameTestおよびE2E。

##### AC-F029-003: 全対象集合と既定値の一致

Preconditions:
TFCMの全対象金属・鉱石とFirmaLife、AFC、Overdimsを有効にした標準出荷構成を使用し、利用者による独自レシピを加えない。

Action:
有効なレシピ集合を仕様の全表行から生成した期待集合と照合する。各表行の入力を実際のRecipe検索と機械処理へ与え、結果・数量・費用・時間・温度・mode条件を確認する。

Expected Result:
最大63金属のMetal Press4形状252件、Briquet33系列、Arc合金18件とSteel1件、高炉3件、Alloy Kiln合金7件とGlass1件、Head9種、Cloche29種、種子Squeezer23種、製材36樹種144件が一致する。ID違い・余分な対象・欠けた表行を総件数の一致で許容しない。金属・鉱石を無効化した別構成では該当集合だけを省略し、代替とmode詰め直しを行わない。検証方式: GameTest。

##### AC-F029-004: Arcの競合・温度・出力待機の統合

Preconditions:
Arcの19配合と全8modeを有効にし、共有添加材を使う複数主入力、材料不足、余剰添加材、出力満杯、異温度出力という試験状態を用意する。

Action:
全171組の候補照合、レシピ登録順・添加材搬入順の変更、Blueprint交換、並列加工、出力欄の開放、チャンク再読込を実施する。

Expected Result:
同一mode・主入力の候補重複は0件となる。mode3のCopper＋ZincはBismuthを待ち、mode1ではBismuthが同居してもBrassだけを作る。Blueprint未装着とmode6〜8ではBriquet・Glassだけを処理する。予約を二重使用せず、Blueprint変更で開始済み配合を変えない。金属出力は登録融点の90%となり、異温度非合流、全量が収まるまでの待機、追加FE・二重消費・消失なしを満たす。検証方式: GameTestおよびE2E。

##### AC-F029-005: 旧版との差分と非追加方針

Preconditions:
基準の全連携構成において、Coke Ovenの出力欄を60個のCokeで満たし、入力16個と十分なCreosote容量を用意する。Squeezer種子、High Carbon Steel金床加工、Treated Lumber、Components Blueprint、固定Bucket材料、AFC Latex関連を用意する。

Action:
Coke Oven、種子Squeezer、High Carbon Steel鍛造、IE Crafting Table製作を実行し、その他の採用・維持・非追加経路をRecipe検索と実クラフトで照合する。

Expected Result:
Coke Ovenは6,000ticksで最大4原料を処理してCoke4個とCreosote2,000mBを追加する。種子はSqueezerでPlant Oilを製造する。対象High Carbon Steel鍛造成功時だけSlag1個を生成し、満杯Inventoryでは金床位置へ排出する。IE Crafting TableはTreated Lumberを用いる。Components Blueprintの標準金属、Fluorescent Tube、固定Minecraft Bucketを維持し、Minecraft金床入手経路・TFC金床修理・AFC Rubber製造利用・鉱石不足の救済・Wool→Stringを追加しない。第35章の全差分と第36章の全受入行は、それぞれ対応するゲーム検証を通過する。検証方式: GameTestおよびE2E。

## Global Requirements

以下にTFIE 0.1.0の詳細仕様全37章を収録する。各Featureの担当範囲には、対応章のすべての表と本文を適用する。本節の見出し階層はharnessの構文に合わせて配置しているが、章番号、入出力、対象集合、数量、費用、時間、温度、操作、例外、削除・維持・非追加指定、参照先は変更しない。

表に記載する値は標準出荷値とする。個別章が認めるサーバー設定・データパックによる変更を維持し、検証では標準値の確認と変更後の実効値の確認を区別する。未導入Modと無効金属の条件、所定のサーバー処理・保存・同期、および第36章の受入条件を、関連する各Featureの完成条件に含める。

<!-- BEGIN TFIE_010_COMPLETE_SPEC -->

### TFIE 0.1.0 完全仕様書

対象ModはTFIE 0.1.0とし、Minecraft 1.21.1向けの確定仕様を定義する。仕様策定日は2026年11月23日とする。本文には対象素材、全採用レシピの入出力、数量、費用、時間、温度、操作、例外条件、削除対象、保存・再読込、表示、受入条件を記載する。

過去の質問番号、選択肢、会話、旧版への一括委任を仕様定義の前提としない。上流の既存動作を維持する箇所は、TFIEが変更する範囲との境界を本文で明記する。参照URLは一次資料の照合先であり、本文の対象一覧や数値の代わりに用いない。

### 目次

| 章 | 対象 |
|---:|---|
| 1 | 仕様の位置付け |
| 2 | 対応環境と依存関係 |
| 3 | 用語と共通条件 |
| 4 | TFIEの登録物 |
| 5 | 共通の出力温度と状態管理 |
| 6 | 金属素材と形状 |
| 7 | Metal Press |
| 8 | Briquet製錬の全系列 |
| 9 | Arc Furnaceの加工 |
| 10 | Arc用Blueprint |
| 11 | Blast FurnaceとImproved Blast Furnace |
| 12 | Alloy Kiln |
| 13 | ドリルヘッド |
| 14 | 手作業による金属加工 |
| 15 | 一般クラフト |
| 16 | マルチブロックの構造 |
| 17 | 木材加工と処理木材 |
| 18 | 麻の栽培 |
| 19 | Garden Clocheによる作物栽培 |
| 20 | Squeezerによる種子の圧搾 |
| 21 | FermenterによるEthanol製造 |
| 22 | 栽培・圧搾・発酵の互換性確認 |
| 23 | 非金属加工と薬液 |
| 24 | External HeaterとCrucible |
| 25 | 回転動力による発電 |
| 26 | Crateの収納制限 |
| 27 | 流体と容器 |
| 28 | 除草剤の作用 |
| 29 | 肥料の互換 |
| 30 | 共通素材タグとTFC熱データ |
| 31 | サーバー設定 |
| 32 | ゲーム内ガイドと表示 |
| 33 | 削除・置換するレシピの台帳 |
| 34 | データ定義と互換性 |
| 35 | 旧TFIEとCrossoverからの変更 |
| 36 | 実装の受入条件 |
| 37 | 参照版 |

### 1. 仕様の位置付け

TFIE 0.1.0は、Minecraft 1.21.1上でTerraFirmaCraftの素材・加工・栽培体系とImmersive Engineeringの機械・電力体系を接続する互換Modである。本書はTFIEが追加、置換、無効化する内容を定義する。実装者は本書の対象集合、数量、条件、入出力、状態遷移および受入条件を使用し、過去の会話や質問への回答を参照する必要はない。

本書に明示する変更以外のMinecraft、TFC、IEおよび連携Modの機能は、それぞれの提供元の機能として維持する。例えばIEの送電線網、発電機内部の一般的な蓄電処理、TFC共通作物の暦進行は、TFIE独自の置換対象に含めない。TFIEとの接続点に影響する条件は本書へ明記する。参照URLは定義の根拠を示し、数量表や対象一覧の代わりには用いない。

本書内の「追加しない」「無効化する」は異なる操作である。「追加しない」はTFIE独自の経路を作成しないことを意味し、他Modの既存機能を一律に削除する指示ではない。「無効化する」は対象レシピを利用不能にし、JEIや機械の実処理にも残さないことを意味する。

### 2. 対応環境と依存関係

TFIEのバージョンは`0.1.0`、Mod IDとデータ名前空間は`tfie`とする。ローダーはNeoForge、Javaは21とし、クライアントと専用サーバーの両方へ導入する。Minecraftの対象は1.21.1に限定する。

対応版の基準は次表とする。Minecraft 1.21.1向けのReleaseとBetaを対象とし、Alphaを対象に含めない。同じMinecraft 1.21.1向けの将来公開版を保守対象に含める。

| 構成要素 | Mod ID | 基準版 | 依存関係と動作 |
|---|---|---|---|
| NeoForge | `neoforge` | 21.1.267 | 必須のローダーとする。 |
| TerraFirmaCraft | `tfc` | 4.2.16 | Required依存とする。 |
| Immersive Engineering | `immersiveengineering` | 1.21.1-12.4.2-194 | Required依存とする。 |
| TFC Metallum Overhaul | `tfcm` | 2.2.1 | Required依存へ追加する。 |
| Patchouli | `patchouli` | 1.21.1-93-NEOFORGE | TFC系の必須構成として扱う。 |
| Advanced TFC Tech Unofficial | `advancedtfctech` | 3.3 | Optional依存とし、導入時だけGrist MillとPower Loomの連携を有効にする。 |
| FirmaLife | `firmalife` | 3.0.16 | Optional依存とし、導入時だけ対応素材とレシピを有効にする。 |
| ArborFirmaCraft | `afc` | 2.1.3 | 依存宣言を削除し、製材の条件付き互換だけを提供する。 |
| TFC Overdims | `tfcoverdims` | 0.1.5 Beta | 依存宣言を追加せず、CrimsonとWarpedの条件付き製材を提供する。 |
| Beneath | `beneath` | 対応対象に含めない。 | TFIEが提供するBeneath木材の製材経路を含めない。 |

ArborFirmaCraftとTFC Overdimsは、本番用ビルドの依存、Modメタデータの必須・任意依存、配布ページの依存宣言のいずれにも含めない。対応レシピの読込時だけModの存在と参照アイテムの存在を確認する。どちらも未導入でTFIEが起動することを要求する。

起動時の版判定は、検証した最低対応版以上を許容し、判明した不適合版を明示的に除外する方式とする。将来版の内部API、登録ID、データ形式、標準レシピの変更はTFIEの更新で対応する。同じMinecraft版であることだけを根拠に、未検証の全将来版の動作を保証する表現は用いない。新しい金属・鉱石・樹種は、TFIEの対応表を更新したときに追加する。タグへ現れた未知の金属や樹種へ、TFIEの新規加工を自動生成しない。

必須Modの不足は依存関係エラーとして明示する。Optional Mod不在と、TFCMで金属・鉱石を無効化した場合は、その対象を参照する連携だけを読込対象から除外する。欠落した処理をTFIE側の代替レシピで補完しない。

基準版の出典は[NeoForge Maven](https://maven.neoforged.net/releases/net/neoforged/neoforge/)、[TFC](https://www.curseforge.com/minecraft/mc-mods/terrafirmacraft/files/all)、[IE](https://www.curseforge.com/minecraft/mc-mods/immersive-engineering/files/all)、[TFCM](https://www.curseforge.com/minecraft/mc-mods/tfc-metallum-overhaul/files/9712557)、[Patchouli](https://www.curseforge.com/minecraft/mc-mods/patchouli)、[Advanced TFC Tech Unofficial](https://www.curseforge.com/minecraft/mc-mods/advanced-tfc-tech-unofficial)、[FirmaLife](https://www.curseforge.com/minecraft/mc-mods/firmalife/files/all)、[ArborFirmaCraft](https://www.curseforge.com/minecraft/mc-mods/arborfirmacraft/files/all)、[TFC Overdims](https://www.curseforge.com/minecraft/mc-mods/tfc-overdims/files/9719778)である。条件付き読込は[NeoForge 1.21.1の条件機構](https://docs.neoforged.net/docs/1.21.1/resources/server/conditions/)を使用する。

### 3. 用語と共通条件

| 用語 | 本書での意味 |
|---|---|
| TFC | TerraFirmaCraftを指す。 |
| IE | Immersive Engineeringを指す。 |
| TFCM | TFC Metallum Overhaulを指す。 |
| AFC | ArborFirmaCraftを指す。 |
| FE | IEが利用する電力量の単位を指す。1処理分の総量と、1tick当たりの量を区別する。 |
| tick | サーバーのゲームtickを指す。標準進行では20ticksが1秒に相当する。TFCの暦時間とは区別する。 |
| mB | 流体量と金属量の単位を指す。1,000mBを1バケツ分とする。 |
| Ingot | 本書の金属配合では100mBの金属を持つインゴットを指す。 |
| Plate | 100mBの薄板を指す。 |
| Sheet | 200mBの板材を指し、Plateとは別形状とする。 |
| Lumber | TFC系の製材アイテムを指し、Planksの板材ブロックとは区別する。 |
| Briquet | TFCMの最終圧縮鉱石加工物を指す。1個は回収金属100mBに相当する。 |
| 主入力 | Arc Furnaceの材料入力12枠のいずれかを指す。各処理は主入力1枠を使用する。 |
| 添加材 | Arc Furnaceの4枠の添加材エリアへ入れる材料を指す。 |
| Recipe ID | `namespace:path`形式のレシピ登録名を指す。 |
| Item ID・Fluid ID | アイテムまたは流体の登録名を指す。Recipe IDと同じ文字列でも、所属レジストリは別とする。 |
| `#namespace:path` | 同名のタグへ属する入力を認める条件を指す。固定Item IDとは区別する。 |
| mode | Arc用Blueprintが保持する1〜8の整数を指す。 |

表に示すFEと時間は、特記のない限り機械のレシピ1処理分の基礎値とする。IEが提供する機械別の電力・時間倍率は維持する。基礎時間、Clocheの成長目標値、Mixerの処理長、実際の経過ticksを混同しない。

数量は入力の必要最小数として扱う。余剰入力の存在を理由にレシピを変更しない。Coke Ovenの一括処理上限だけは、必要消費数と区別して別途定義する。確率副産物の抽選時期は対象機械の処理に従い、機械ごとに定めた出力判定を一律に置き換えない。

金属や鉱石そのものと鉱脈の生成は、TFC、TFCMおよび各素材Modに委ねる。TFIEが新しい金属元素・合金登録・鉱脈を重複して登録することはない。TFIEの独自登録物は連携用アイテム、作物、金型、ドリルヘッドおよびBlueprintとする。

### 4. TFIEの登録物

| レジストリ | ID | 役割と数量制約 |
|---|---|---|
| Item | `tfie:treated_wood_lumber` | Creosote処理済みLumberとする。通常の最大スタック数64とする。 |
| Item | `tfie:tool_head/ie_hammer` | Engineer's Hammerの頭部とする。通常の最大スタック数64とする。 |
| Item | `tfie:tool_head/wirecutter` | Wirecutterの頭部とする。通常の最大スタック数64とする。 |
| Item | `tfie:mold_sheet` | Metal PressのSheet金型とし、最大スタック数1とする。 |
| Item | `tfie:arc_furnace_blueprint` | Arcのmode指定用Blueprintとし、最大スタック数1とする。 |
| Data Component | `tfie:arc_furnace_mode` | Arc用Blueprintの整数modeを保持し、永続化とクライアント同期へ対応する。既定値は1とする。 |
| Item | `tfie:drillhead_black_steel` | Black Steel Headとし、最大スタック数1とする。 |
| Item | `tfie:drillhead_blue_steel` | Blue Steel Headとし、最大スタック数1とする。 |
| Item | `tfie:drillhead_red_steel` | Red Steel Headとし、最大スタック数1とする。 |
| Item | `tfie:drillhead_invar` | Invar Headとし、最大スタック数1とする。 |
| Item | `tfie:drillhead_titanium` | Titanium Headとし、最大スタック数1とする。 |
| Item | `tfie:drillhead_netherite` | Netherite Headとし、最大スタック数1とする。 |
| Item | `tfie:drillhead_tungsten_steel` | Tungsten Steel Headとし、最大スタック数1とする。 |
| Block | `tfie:crop/hemp` | 栽培Hempとする。 |
| Block | `tfie:dead_crop/hemp` | 枯死Hempとする。 |
| BlockとBlockItem | `tfie:wild_crop/hemp` | 野生Hempとする。 |
| BlockEntity Type | `tfie:crop` | Hempの作物状態を保持する。 |
| Creative Tab | `tfie:main` | 翻訳キーを`tfie.creative_tab.main`、アイコンを`tfie:treated_wood_lumber`とする。 |

IronとSteelのHeadはIEの既存IDを使用する。Hemp SeedsとHemp FiberもIEの既存IDを使用する。TFIE独自のBlock Moldを登録せず、そのBlueprint製法とMetal Press加工も含めない。標準出力の金属素材は、本書の金属ID対応表から求める。

Arc用Blueprintとmode ComponentのIDはTFIE 0.1.0の新規定義とする。その他の既存登録の根拠は[ModItems](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/ModItems.java)と[ModBlocks](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/ModBlocks.java)である。

Creative Tabの定義は[ModCreativeTabs](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/ModCreativeTabs.java)を継承する。

### 5. 共通の出力温度と状態管理

#### 金属出力の温度

Arc Furnace、Blast Furnace、Improved Blast Furnace、Alloy Kilnが生成する、TFCの温度を保持する金属主出力を対象とする。生成温度を次式で定める。

```text
生成温度[℃] = 出力金属に登録された融点[℃] × 0.9
```

例えば融点1,535℃のRaw Iron Bloomは1,381.5℃で生成する。温度を整数へ丸めず、TFCの通常のHeat Componentへ設定する。熱容量と冷却は出力素材のTFC定義を使用し、出力欄にある間も冷却する。金属ではないInsulating Glass、Slag、木材、食材および確率副産物に、この規則による温度を付けない。常温加工であるMetal Pressにも一律の90%加熱を適用しない。

同一アイテムでも温度を含むComponentが異なるスタックは合流しない。空き出力枠を使用し、空きがなければ待機する。既存スタックを再加熱したり、既存温度と新規温度を平均したりしない。採用した90%は、Ice and Fire TFC compatの融点2,000℃・生成温度1,800℃という関係を各金属へ一般化したものである。[温度値](https://github.com/lau-mods/IaFTFC/blob/c6bee8e29734515379e788620e6ec91401f71204/src/main/java/net/claustra01/iaftfc/metal/Dragonsteel.java)、[Heat Component処理](https://github.com/lau-mods/IaFTFC/blob/c6bee8e29734515379e788620e6ec91401f71204/src/main/java/net/claustra01/iaftfc/metal/DragonForgeHeat.java)

#### 出力容量と確定処理

本節の一括確定と出力待機は、Arc Furnace、Blast Furnace、Improved Blast Furnace、Alloy Kilnの内部出力へ適用する。Sawmillの剥皮・切断時の副産物排出と外部へのアイテム排出は、木材加工の章で定義するIEの段階別処理を維持する。

対象炉では、1処理分の主出力と副産物を所定の出力欄へすべて配置することを、材料消費と排出を確定する条件とする。部分的な生成物だけを排出して残りを消失させない。所定枠が不足する場合は処理の進捗と未消費材料を保持して待機し、待機中に追加の材料・FEを消費しない。他の出力可能な処理は継続する。待機中の副産物再抽選、入力や出力の複製、Blueprintの消費を認めない。

Arcでは通常出力6枠とSlag専用1枠を維持する。Slagを通常出力枠へ迂回させる独自機能は追加しない。Recipe ID、開始時の配合、進捗、予約数量をサーバー側の状態として保存し、再起動・チャンク再読込で維持する。

Arcのレシピ再読込で開始済み処理のレシピが無効になった場合、または同じIDの入出力・数量・費用が変更された場合は、旧処理を別配合へ変換しない。進捗と未消費材料を保持して待機し、追加消費を停止する。定義を復旧するか、機械を解体して入力を回収するまで待機を維持する。Blueprintの交換・取り外し自体は妨げない。これらはTFIEの整合性要件であり、取得済みレシピのキャッシュだけに判定を委ねない。[IE処理キュー](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/process/MultiblockProcess.java)、[入力予約](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/process/MultiblockProcessInMachine.java)

### 6. 金属素材と形状

#### 対応金属の集合

TFIE 0.1.0の金属加工は、下表に列挙する金属と、その金属に実在する形状だけを対象とする。TFCが登録する形状はTFCのアイテムを、FirmaLife固有金属の既存形状はFirmaLifeのアイテムを、TFCM追加金属と追加形状はTFCMのアイテムを標準出力とする。IE固有の機械部品を金属素材の同義物として置換しない。

TFCの基本形状を持つ金属は次の19種である。融点は基準データの値を示す。出力温度の計算には、そのワールドで出力金属に登録されている融点を用いる。

| 金属名 | 材料IDの末尾 | 基準融点℃ |
|---|---|---:|
| Bismuth | `bismuth` | 270 |
| Bismuth Bronze | `bismuth_bronze` | 985 |
| Black Bronze | `black_bronze` | 1070 |
| Black Steel | `black_steel` | 1485 |
| Blue Steel | `blue_steel` | 1540 |
| Brass | `brass` | 930 |
| Bronze | `bronze` | 950 |
| Cast Iron | `cast_iron` | 1535 |
| Copper | `copper` | 1080 |
| Gold | `gold` | 1060 |
| Nickel | `nickel` | 1453 |
| Red Steel | `red_steel` | 1540 |
| Rose Gold | `rose_gold` | 960 |
| Silver | `silver` | 961 |
| Steel | `steel` | 1540 |
| Sterling Silver | `sterling_silver` | 950 |
| Tin | `tin` | 230 |
| Wrought Iron | `wrought_iron` | 1535 |
| Zinc | `zinc` | 420 |

TFCには上記以外に `pig_iron`、`weak_steel`、`weak_blue_steel`、`weak_red_steel`、`high_carbon_steel`、`high_carbon_black_steel`、`high_carbon_blue_steel`、`high_carbon_red_steel`、`unknown` の9種がある。これらはIngot専用であり、TFCMも本仕様のPlate・Sheet・Rod・Wireを追加していない。Arcの配合入力・出力として採用したものは使用するが、Metal Pressに不足形状を新設しない。

TFCMの追加金属は次の42種である。各金属は設定で有効な場合だけ対応する。鍛造tierはTFCM金床加工の基準値であり、ドリルヘッドの採掘tierとは区別する。

| 金属名 | 材料IDの末尾 | 基準融点℃ | 基準鍛造tier |
|---|---|---:|---:|
| Compressed Iron | `compressed_iron` | 1535 | 3 |
| Platinum | `platinum` | 1730 | 3 |
| Naquadah | `naquadah` | 2000 | 7 |
| Iridium | `iridium` | 1535 | 3 |
| Osmium | `osmium` | 1535 | 3 |
| Osmiridium | `osmiridium` | 1500 | 3 |
| Mithril | `mithril` | 940 | 2 |
| Arcane | `arcane` | 940 | 2 |
| Refined Glowstone | `refined_glowstone` | 940 | 2 |
| Refined Obsidian | `refined_obsidian` | 940 | 2 |
| Antimony | `antimony` | 630 | 1 |
| Titanium | `titanium` | 1700 | 5 |
| Cobalt | `cobalt` | 1700 | 3 |
| Lithium | `lithium` | 181 | 3 |
| Aluminum | `aluminum` | 660 | 3 |
| Constantan | `constantan` | 1250 | 1 |
| Invar | `invar` | 1250 | 3 |
| Electrum | `electrum` | 1000 | 1 |
| Lead | `lead` | 330 | 1 |
| Uranium | `uranium` | 1130 | 3 |
| Tungsten | `tungsten` | 1535 | 3 |
| Solder | `solder` | 400 | 3 |
| Tungsten Steel | `tungsten_steel` | 1535 | 7 |
| Netherite | `netherite` | 1535 | 6 |
| Dawnstone | `dawnstone` | 1000 | 1 |
| Andesite Alloy | `andesite_alloy` | 520 | 2 |
| Draconium | `draconium` | 2000 | 7 |
| Awakened Draconium | `awakened_draconium` | 2000 | 7 |
| Boron | `boron` | 2076 | 3 |
| Thorium | `thorium` | 1750 | 3 |
| Magnesium | `magnesium` | 650 | 2 |
| Beryllium | `beryllium` | 1287 | 3 |
| Zirconium | `zirconium` | 1855 | 2 |
| Manganese | `manganese` | 1246 | 3 |
| Molybdenum | `molybdenum` | 1535 | 3 |
| Neodymium | `neodymium` | 1024 | 3 |
| Vanadium | `vanadium` | 1700 | 3 |
| Hastelloy | `hastelloy` | 1370 | 4 |
| High Speed Steel | `high_speed_steel` | 1450 | 5 |
| Signalum | `signalum` | 1500 | 4 |
| Lumium | `lumium` | 1500 | 4 |
| Enderium | `enderium` | 1700 | 5 |

TFCMは上表とは別に中間素材 `tfcm:metal/ingot/high_carbon_tungsten_steel` を登録する。このアイテムは `tungsten_steel` が有効な場合に存在するIngot専用素材であり、上表の42種やMetal Pressの対応集合へ加えない。

FirmaLife導入時には `chromium` と `stainless_steel` を追加する。基準融点はそれぞれ1,250℃と1,540℃、基準鍛造tierはともに4とする。

形状を持つ対応集合は、TFC 19種、TFCM 42種、FirmaLife 2種の最大63種となる。TFCMの全金属を有効にし、FirmaLifeを導入した構成では、Metal Pressの4形状について最大252レシピとなる。未登録の将来金属はこの集合へ自動追加せず、TFIEの更新で個別に対応表へ加える。

根拠は[TFCの金属とPartType](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/util/Metal.java)、[TFCMのTFC・FirmaLife金属対応表](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/common/metal/TfcMetal.java)、[TFCM追加金属42種](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/common/metal/Metal.java)、[形状の登録](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/registry/TFCMItems.java)である。

#### アイテムIDと材料タグ

以下の `M` は上表の材料ID末尾を示し、`F` は形状IDを示す。

| 金属群と形状 | 標準アイテムID |
|---|---|
| TFCのIngot、Double Ingot、Sheet、Double Sheet、Rod | `tfc:metal/F/M` とする。 |
| FirmaLifeのIngot、Double Ingot、Sheet、Double Sheet、Rod | `firmalife:metal/F/M` とする。 |
| TFC・FirmaLife金属にTFCMが追加するPlate、Wire、Dust、Nuggetその他の追加形状 | `tfcm:metal/F/M` とする。 |
| TFCM追加金属の全アイテム形状 | `tfcm:metal/F/M` とする。 |
| TFCM追加金属のBlock | `tfcm:metal/block/M` とする。 |
| TFCM追加金属のSlabとStairs | `tfcm:metal/block/M_slab`、`tfcm:metal/block/M_stairs` とする。 |

金属入力には、対象が一致する `c:ingots/M`、`c:double_ingots/M`、`c:sheets/M`、`c:double_sheets/M`、`c:rods/M`、`c:plates/M`、`c:wires/M` のタグを用いる。出力は上表の標準アイテムへ固定する。Cast IronとWrought Ironを同じ鉄素材へ統合せず、Wrought Ironを一般の `c:ingots/iron` タグへ追加しない。Ironの置換を採用したクラフトとIronドリルヘッドの修理は、対象レシピ・対象判定をWrought Ironへ限定して変更する。

TFCMの形状IDと金属量を次表とする。数量は流体量mBである。

| 形状ID | 1個の金属量mB | TFIEのMetal Press |
|---|---:|---|
| `ingot` | 100 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `double_ingot` | 200 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `sheet` | 200 | Metal Pressの出力形状とする。 |
| `double_sheet` | 400 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `rod` | 50 | Metal Pressの出力形状とする。 |
| `block` | 100 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `slab` | 50 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `stairs` | 75 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `foil` | 100 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `gear` | 400 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `heavy_sheet` | 600 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `nail` | 5 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `ring` | 12 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `rivet` | 5 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `screw` | 5 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `stamen` | 25 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `wire` | 25 | Metal Pressの出力形状とする。 |
| `plate` | 100 | Metal Pressの出力形状とする。 |
| `nugget` | 10 | TFIEはこの形状へのMetal Press加工を追加しない。 |
| `dust` | 100 | TFIEはこの形状へのMetal Press加工を追加しない。 |

Plate 100 mBとSheet 200 mBを区別する。Metal PressではIngot 1個からPlate 1個、Ingot 2個からSheet 1個、Ingot 1個からRod 2個、Ingot 1個からWire 4個とする。いずれも2,400 FEを消費し、入力温度を要求しない。

TFCMのBlockは100 mBであり、Ingot 9個分の圧縮Blockとして扱わない。TFCMが提供するNuggetは1個10 mBである一方、既存クラフトはIngot 1個とNugget 9個を相互変換する。この既存例外をTFIEで補正しない。TFIEの追加経路にはこの例外を拡張しない。

根拠は[MetalForm](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/common/metal/MetalForm.java)と[MetalDataProvider](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/datagen/MetalDataProvider.java)である。

#### 無効化条件

`tfcm-common.toml` の `disabledMetals` に指定した追加金属、および `disabledOres` に指定した鉱石系列を参照するTFIEレシピは読み込まない。TFCMが登録自体を除外したアイテムへの直接参照で読込を失敗させず、Mod導入状態、該当する金属・鉱石の有効状態、参照アイテムの存在を確認する。

合金ではすべての入力金属と出力金属の有効性を要求する。Briquet製錬では鉱石系列と回収金属の双方を要求する。FirmaLifeのChromium・Stainless Steel、Chromite、およびChromiumを材料とする採用レシピにはFirmaLife導入条件を付ける。無効化した対象・レシピ・理由をログへ記録し、他の金属や別機械による代替レシピを追加しない。mode番号を詰め直さず、残る配合の固定番号を維持する。

TFCMの無効化設定はゲーム再起動時に登録内容へ反映される。存在する設定条件のIDは `tfcm:metal_enabled` と `tfcm:ore_enabled` であり、FirmaLife導入条件は `neoforge:mod_loaded` の `modid: firmalife` とする。根拠は[TFCMConfig](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/config/TFCMConfig.java)、[TFCMItems](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/registry/TFCMItems.java)、[ContentConditions](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/datagen/ContentConditions.java)である。

### 7. Metal Press

#### 加工と数量

対象は金属素材の章で定義した最大63金属とし、実在するPlate、Sheet、Rod、Wireの4形状を加工する。出力の名前空間は金属素材の標準出力規則に従う。

| 出力形状 | 入力 | 主出力 | 金型 | 基礎FE | 基礎時間 |
|---|---|---|---|---:|---:|
| Plate | 同金属Ingot 1個を使用する。 | Plate 1個を生成する。 | `immersiveengineering:mold_plate`を使用する。 | 2,400 | 60 ticks |
| Sheet | 同金属Ingot 2個を使用する。 | Sheet 1個を生成する。 | `tfie:mold_sheet`を使用する。 | 2,400 | 60 ticks |
| Rod | 同金属Ingot 1個を使用する。 | Rod 2個を生成する。 | `immersiveengineering:mold_rod`を使用する。 | 2,400 | 60 ticks |
| Wire | 同金属Ingot 1個を使用する。 | Wire 4個を生成する。 | `immersiveengineering:mold_wire`を使用する。 | 2,400 | 60 ticks |

材料温度を要求せず、金型は消費しない。金属量はPlateが100mB、Sheetが200mB、Rodが50mB、Wireが25mBであり、各経路の金属量を保存する。既存IEの対象金属のPlate・Rod・Wire経路は対応するTFIE経路へ置き換え、同じ入力と金型から異なる出力が競合する状態を残さない。GearやBlaze Rodなど、今回変更する4形状と無関係なIE機能へ一律の削除を広げない。

正規レシピIDは`tfie:metalpress/F/M`とし、`F`へ`plate`、`sheet`、`rod`、`wire`、`M`へ対応する金属の末尾IDを代入する。入力・出力のどちらかの金属または形状が無効・未登録なら、その1レシピを読み込まない。

基礎時間60ticksは[IE MetalPressRecipe](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/MetalPressRecipe.java)、形状量は[TFCM MetalForm](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/common/metal/MetalForm.java)を根拠とする。

#### 金型とMolds Blueprint

Sheet金型は、Molds Blueprintを置いたEngineer's WorkbenchでSteel Plate 3個とWirecutter 1個から1個を製作する。Wirecutterは耐久1を消費して返却する。Sheet金型の正規レシピIDは`tfie:blueprint/mold_sheet`とする。

Plate・Rod・WireのIE金型も、Molds Blueprintと`#c:plates/steel`3個、`immersiveengineering:wirecutter`1個から各1個を製作する。Recipe IDは`immersiveengineering:blueprint/mold_plate`、`immersiveengineering:blueprint/mold_rod`、`immersiveengineering:blueprint/mold_wire`とし、IE既存の3レシピを維持する。各製作でWirecutterの耐久を1消費する。[Plate金型](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/blueprint/mold_plate.json)、[Rod金型](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/blueprint/mold_rod.json)、[Wire金型](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/blueprint/mold_wire.json)

Itemタグ`tfie:molds`には`tfie:mold_sheet`と、IEの`mold_plate`、`mold_gear`、`mold_rod`、`mold_bullet_casing`、`mold_wire`、`mold_packing_4`、`mold_packing_9`、`mold_unpacking`を含める。IEの各IDには`immersiveengineering:`を前置する。このタグをTFC Item Sizeの入力とし、size=`normal`、weight=`medium`を定義する。廃止した`tfie:mold_block`を含めない。[金型タグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tags/item/molds.json)、[Item Size](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tfc/item_size/molds.json)

Molds Blueprintのクラフトは、Wrought Iron Plate 1個、Blue Dye 3個、Paper 3個から1個とする。3×3の配置は上段中央にPlate、中段にBlue Dye 3個、下段にPaper 3個を置く。完成品は`immersiveengineering:blueprint`であり、`immersiveengineering:blueprint` Componentを`molds`とする。対象レシピIDは`immersiveengineering:crafting/blueprint_molds`とする。Iron PlateをWrought Iron Plateへ置き換える範囲をこのレシピへ明示して適用し、一般のIronタグへWrought Ironを登録しない。

根拠は[Sheet金型](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/blueprint/mold_sheet.json)と[IE Molds Blueprint](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/blueprint_molds.json)である。

### 8. Briquet製錬の全系列

BriquetはTFCMの鉱石加工物であり、1個が回収金属100 mBに相当する。Arc Furnaceは次表のBriquet 1個から、対応する標準Ingot 1個を51,200 FE・100 ticksで生成する。添加材、Flux、Slagその他の副産物を要求・生成しない。Blueprint未装着時およびmode 1〜8で処理する。出力温度は出力金属の融点の90%とする。

| 系列ID | 入力Briquet ID | 出力Ingot ID | 追加条件 |
|---|---|---|---|
| `native_copper` | `tfcm:ore_processing/briquet/native_copper` | `tfc:metal/ingot/copper` | 共通の有効条件を適用する。 |
| `native_gold` | `tfcm:ore_processing/briquet/native_gold` | `tfc:metal/ingot/gold` | 共通の有効条件を適用する。 |
| `hematite` | `tfcm:ore_processing/briquet/hematite` | `tfc:metal/ingot/cast_iron` | 共通の有効条件を適用する。 |
| `native_silver` | `tfcm:ore_processing/briquet/native_silver` | `tfc:metal/ingot/silver` | 共通の有効条件を適用する。 |
| `cassiterite` | `tfcm:ore_processing/briquet/cassiterite` | `tfc:metal/ingot/tin` | 共通の有効条件を適用する。 |
| `bismuthinite` | `tfcm:ore_processing/briquet/bismuthinite` | `tfc:metal/ingot/bismuth` | 共通の有効条件を適用する。 |
| `garnierite` | `tfcm:ore_processing/briquet/garnierite` | `tfc:metal/ingot/nickel` | 共通の有効条件を適用する。 |
| `malachite` | `tfcm:ore_processing/briquet/malachite` | `tfc:metal/ingot/copper` | 共通の有効条件を適用する。 |
| `magnetite` | `tfcm:ore_processing/briquet/magnetite` | `tfc:metal/ingot/cast_iron` | 共通の有効条件を適用する。 |
| `limonite` | `tfcm:ore_processing/briquet/limonite` | `tfc:metal/ingot/cast_iron` | 共通の有効条件を適用する。 |
| `sphalerite` | `tfcm:ore_processing/briquet/sphalerite` | `tfc:metal/ingot/zinc` | 共通の有効条件を適用する。 |
| `tetrahedrite` | `tfcm:ore_processing/briquet/tetrahedrite` | `tfc:metal/ingot/copper` | 共通の有効条件を適用する。 |
| `galena` | `tfcm:ore_processing/briquet/galena` | `tfcm:metal/ingot/lead` | 共通の有効条件を適用する。 |
| `magnesite` | `tfcm:ore_processing/briquet/magnesite` | `tfcm:metal/ingot/magnesium` | 共通の有効条件を適用する。 |
| `bauxite` | `tfcm:ore_processing/briquet/bauxite` | `tfcm:metal/ingot/aluminum` | 共通の有効条件を適用する。 |
| `uraninite` | `tfcm:ore_processing/briquet/uraninite` | `tfcm:metal/ingot/uranium` | 共通の有効条件を適用する。 |
| `chromite` | `tfcm:ore_processing/briquet/chromite` | `firmalife:metal/ingot/chromium` | FirmaLifeを要する。 |
| `rutile` | `tfcm:ore_processing/briquet/rutile` | `tfcm:metal/ingot/titanium` | 共通の有効条件を適用する。 |
| `zircon` | `tfcm:ore_processing/briquet/zircon` | `tfcm:metal/ingot/zirconium` | 共通の有効条件を適用する。 |
| `cobaltite` | `tfcm:ore_processing/briquet/cobaltite` | `tfcm:metal/ingot/cobalt` | 共通の有効条件を適用する。 |
| `stibnite` | `tfcm:ore_processing/briquet/stibnite` | `tfcm:metal/ingot/antimony` | 共通の有効条件を適用する。 |
| `native_platinum` | `tfcm:ore_processing/briquet/native_platinum` | `tfcm:metal/ingot/platinum` | 共通の有効条件を適用する。 |
| `wolframite` | `tfcm:ore_processing/briquet/wolframite` | `tfcm:metal/ingot/tungsten` | 共通の有効条件を適用する。 |
| `pyrolusite` | `tfcm:ore_processing/briquet/pyrolusite` | `tfcm:metal/ingot/manganese` | 共通の有効条件を適用する。 |
| `monazite` | `tfcm:ore_processing/briquet/monazite` | `tfcm:metal/ingot/neodymium` | 共通の有効条件を適用する。 |
| `thorianite` | `tfcm:ore_processing/briquet/thorianite` | `tfcm:metal/ingot/thorium` | 共通の有効条件を適用する。 |
| `native_iridium` | `tfcm:ore_processing/briquet/native_iridium` | `tfcm:metal/ingot/iridium` | 共通の有効条件を適用する。 |
| `native_osmium` | `tfcm:ore_processing/briquet/native_osmium` | `tfcm:metal/ingot/osmium` | 共通の有効条件を適用する。 |
| `spodumene` | `tfcm:ore_processing/briquet/spodumene` | `tfcm:metal/ingot/lithium` | 共通の有効条件を適用する。 |
| `carobbiite` | `tfcm:ore_processing/briquet/carobbiite` | `tfcm:metal/ingot/beryllium` | 共通の有効条件を適用する。 |
| `native_naquadah` | `tfcm:ore_processing/briquet/native_naquadah` | `tfcm:metal/ingot/naquadah` | 共通の有効条件を適用する。 |
| `mithril_matrix` | `tfcm:ore_processing/briquet/mithril_matrix` | `tfcm:metal/ingot/mithril` | 共通の有効条件を適用する。 |
| `borax` | `tfcm:ore_processing/briquet/borax` | `tfcm:metal/ingot/boron` | 共通の有効条件を適用する。 |

全33系列のうちHematite・Magnetite・LimoniteはCast Ironを出力する。Blast FurnaceとImproved Blast Furnaceでは同じ鉄3系列だけを受け入れ、Briquet 1個から `tfc:raw_iron_bloom` 1個を生成する。ArcのCast Iron出力と高炉のRaw Iron Bloom出力を区別する。

Cinnabar、Cryolite、Graphite、Sulfurには金属回収流体がなく、Briquetが存在しない。これらの系列にTFIEがBriquetや製錬レシピを新設しない。Boraxは非金属鉱石としての加工属性を持つが、回収金属Boronが定義されているため上表に含める。

TFCMのPowder 4個からPellet 1個、Pellet 5個からBriquet 1個という既存圧縮、および逆変換は変更しない。金属量はPowder 5 mB、Pellet 20 mB、Briquet 100 mBである。TFIEが許可するArcの鉱石製錬入力はBriquetだけとし、Powder、Pellet、Chunks、Dirty Dust、Raw Ore、原鉱石、金属DustからのArc製錬を設けない。TFCM自身のTFC HeatingによるPowder・Pellet・Briquetの加熱溶融は維持する。

根拠は[ProcessingSeries](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/common/processing/ProcessingSeries.java)と[ProcessingDataProvider](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/datagen/ProcessingDataProvider.java)である。

### 9. Arc Furnaceの加工

#### 入力範囲

鉱石製錬の入力はBriquetだけとする。原鉱石、Raw Ore、Raw Ore Block、Rocky Chunks、Chunks、Dirty Dust、Powder、Pellet、100mBの金属Dust、Netherite Scrapの製造用入力を使うIE標準製錬経路は無効化する。金属製品のArc Recyclingも、起動時とデータ再読込時の動的生成を含めて無効化する。

金属Ingotは下表の合金とSteel製造に使用する。Briquet限定という規則を、合金のIngot入力の禁止へ拡張しない。TFCM自身のTFC Heatingによる鉱石加工物の溶融は変更しない。Briquetの全33系列と数量はBriquet製錬の章に定義する。

#### 合金の全配合

以下の金属名は、金属素材の章で定義したIngotとタグを指す。数量は1処理分とし、主入力は表の主入力欄にある1種類だけとする。Andesite Alloyの主入力だけは`tfc:rock/loose/andesite`であり、Ingotではない。添加材の順序は問わず、指定数量を消費する。

| 出力合金 | mode | 主入力 | 添加材 | 出力数 | FE | ticks |
|---|---:|---|---|---:|---:|---:|
| Bismuth Bronze | 3 | Copper 3個 | Zinc 1個、Bismuth 1個 | 5 | 128,000 | 250 |
| Black Bronze | 4 | Copper 2個 | Silver 1個、Gold 1個 | 4 | 102,400 | 200 |
| Brass | 1 | Copper 9個 | Zinc 1個 | 10 | 256,000 | 500 |
| Bronze | 2 | Copper 9個 | Tin 1個 | 10 | 128,000 | 250 |
| Rose Gold | 1 | Gold 3個 | Copper 1個 | 4 | 51,200 | 100 |
| Sterling Silver | 1 | Silver 3個 | Copper 2個 | 5 | 128,000 | 250 |
| Weak Steel | 1 | Steel 6個 | Nickel 2個、Black Bronze 2個 | 10 | 256,000 | 500 |
| Weak Blue Steel | 1 | Black Steel 10個 | Steel 5個、Bismuth Bronze 2個、Sterling Silver 3個 | 20 | 512,000 | 1,000 |
| Weak Red Steel | 2 | Black Steel 10個 | Steel 5個、Brass 2個、Rose Gold 3個 | 20 | 512,000 | 1,000 |
| Constantan | 5 | Copper 1個 | Nickel 1個 | 2 | 51,200 | 100 |
| Electrum | 2 | Gold 1個 | Silver 1個 | 2 | 51,200 | 100 |
| Invar | 2 | Wrought Iron 2個 | Nickel 1個 | 3 | 51,200 | 100 |
| Osmiridium | 1 | Osmium 2個 | Iridium 1個 | 3 | 76,800 | 150 |
| Solder | 1 | Antimony 2個 | Bismuth 1個、Tin 1個 | 4 | 102,400 | 200 |
| Hastelloy | 2 | Nickel 7個 | Chromium 3個、Molybdenum 2個、Wrought Iron 1個、Tungsten 1個 | 14 | 358,400 | 700 |
| High Speed Steel | 2 | Steel 10個 | Tungsten 1個、Vanadium 1個、Molybdenum 1個、Manganese 1個 | 14 | 358,400 | 700 |
| Andesite AlloyのZinc経路 | 1 | Loose Andesite 1個 | Zinc Nugget 1個 | 1 | 25,600 | 50 |
| Andesite AlloyのTin経路 | 2 | Loose Andesite 1個 | Tin Nugget 1個 | 1 | 25,600 | 50 |

金属合金は16種類、Andesite Alloyは2経路とする。HastelloyはFirmaLifeのChromiumを使用するため、FirmaLife導入時だけ有効にする。各配合に追加のFlux、炭素材、Slag、確率副産物を設けない。出力には共通の90%温度を付与する。

Andesite AlloyはLoose Andesite 1個とZincまたはTin Nugget 1個からIngot 1個を生成する。Tungsten Steel、Netherite、Signalum等について、上表にない合金製法をTFIEから追加しない。

正規レシピIDは`tfie:arcfurnace/alloy/M`とする。`M`は各出力合金の末尾IDであり、Andesite Alloyだけは`andesite_alloy_from_zinc`と`andesite_alloy_from_tin`を使用する。Briquetは`tfie:arcfurnace/briquet/S`とし、`S`へ全33系列の系列IDを代入する。

Constantan、Electrum、Brass、Bronze、Rose Gold、Invarの費用は、IEに存在する同名合金のIngot1個当たり費用へ合わせる。他の金属合金とAndesite Alloyは出力Ingot1個当たり25,600FE・50ticksとする。この計算規則よりも上表の確定値を実装の基準とし、IEの他の製錬種別へ同じ単価を拡張しない。[IE合金生成](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/datagen/java/blusunrize/immersiveengineering/data/recipes/OreRecipes.java)、[IE出力数量](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/datagen/java/blusunrize/immersiveengineering/data/resources/RecipeMetals.java)、[TFCM配合範囲](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/datagen/MetalDataProvider.java)

#### Steel製造

| 項目 | 値 |
|---|---|
| 正規Recipe ID | `tfie:arcfurnace/steel`とする。 |
| mode | 1とする。 |
| 主入力 | `#c:ingots/wrought_iron`1個を使用する。 |
| 添加材 | `#c:dusts/coal_coke`1個を使用する。 |
| 主出力 | `tfc:metal/ingot/steel`1個を生成する。 |
| 副産物 | `immersiveengineering:slag`1個を確定で生成する。 |
| 費用 | 204,800FE・400ticksとする。 |
| 出力温度 | Steelの融点の90%とする。Slagへ同じ温度を付けない。 |

IE標準のIronからSteelへの経路をこの配合へ置き換える。Charcoal PowderをArc製鋼の添加材として認めない。高炉燃料としてのCharcoal許可とは別の条件である。[IE標準Steel](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/arcfurnace/steel.json)

#### Insulating Glass

`#c:glass_blocks`2個を主入力、`tfcm:metal/dust/wrought_iron`1個を添加材として、`immersiveengineering:insulating_glass`2個を51,200FE・100ticksで生成する。正規Recipe IDは`tfie:arcfurnace/insulating_glass`とする。非金属加工として扱い、Blueprint未装着時と全mode 1〜8で許可する。温度付与とSlag生成を行わない。

Wrought Iron DustはTFCMのQuernでWrought Iron Ingot1個から1個を得る。TFIEはWrought Iron IngotからDustへのCrusherレシピを追加せず、DustからIngotへのArc製錬も追加しない。[Quern製法](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/generated/resources/data/tfcm/recipe/quern/metal/dust/wrought_iron.json)、[IE Insulating Glass](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/arcfurnace/insulating_glass.json)

### 10. Arc用Blueprint

#### 製作と設定操作

`#c:paper`3個と`#c:dyes/blue`3個から、`tfie:arc_furnace_blueprint`1個を不定形クラフトで製作する。正規Recipe IDは`tfie:crafting/arc_furnace_blueprint`とする。初期modeは1とし、空白・未設定の段階を設けない。

Blueprintをホットバーで選択し、メインハンドの右クリックでmodeを1→2→3→4→5→6→7→8→1の順に切り替える。設定GUI、Shiftによる逆順切替、追加材料、FE、耐久消費を設けない。空中への右クリックを標準操作として案内し、ブロック側の通常操作が成立した場合はその操作を優先する。炉を開く操作によってmodeを変更しない。

ツールチップと切替時の短い表示で現在のmodeを示す。JEIには各合金・Steelレシピが要求するmodeのBlueprintを非消費の触媒として表示する。BriquetとInsulating GlassにBlueprint必須という表示を付けない。IEのEngineer's Workbench用Blueprintとは別アイテムとし、Workbench用カテゴリをArcの設定値に読み替えない。

modeは`tfie:arc_furnace_mode`の整数Data Componentとしてサーバー側で更新し、保存と通信の双方へ対応する。根拠となる上流機構は[NeoForge Data Components](https://docs.neoforged.net/docs/1.21.1/items/datacomponents/)と[右クリックの処理順](https://docs.neoforged.net/docs/1.21.1/items/interactionpipeline/)である。

#### 機械内の配置

Arc Furnaceの主入力0〜11の任意の1枠へBlueprintを置く。専用スロットと、Inventoryとは別のBlueprint保存領域は設けない。Blueprintの枠を材料走査、材料予約、消費の対象から除外する。添加材12〜15の4枠は維持する。

通常出力は16〜21の6枠、Slag出力は22の1枠、電極は23〜25の3枠を維持する。電極3本の装着、FE供給、Redstone制御というIEの稼働条件をBlueprintで代替しない。稼働tickに対する電極の通常の耐久消費も維持する。

1台につきBlueprint1枚、1スタック1個に限定する。2枚目の投入を拒否し、既存の1枚を同じ枠で交換する操作は認める。外部操作で複数枚が存在した場合は、同じmodeであっても炉の新規処理を停止し、開始済み処理を維持する。正規範囲外のmodeも新規処理の停止と状態表示の対象とする。

着脱は既存の機械画面の通常操作とShiftクリックで行う。材料・添加材の自動搬入口ではBlueprintを拒否する。機械解体時には、主入力内の通常アイテムと同じ扱いでBlueprintを排出する。

装着中の材料用主入力は最大11枠とし、未装着時の基本加工は12枠を使用する。新しいBlueprintの挿入には空き枠を要する。処理中の原料を抜き取った場合にはIEの通常の処理中断を適用し、Blueprintだけの交換・取り外しによる継続保証と混同しない。[IEの入力枠と予約処理](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/logic/arcfurnace/ArcFurnaceLogic.java)

#### modeと配合の対応

| 主入力 | mode 1 | mode 2 | mode 3 | mode 4 | mode 5 |
|---|---|---|---|---|---|
| Copper | Brass | Bronze | Bismuth Bronze | Black Bronze | Constantan |
| Gold | Rose Gold | Electrum | 対象なし | 対象なし | 対象なし |
| Silver | Sterling Silver | 対象なし | 対象なし | 対象なし | 対象なし |
| Steel | Weak Steel | High Speed Steel | 対象なし | 対象なし | 対象なし |
| Black Steel | Weak Blue Steel | Weak Red Steel | 対象なし | 対象なし | 対象なし |
| Nickel | 対象なし | Hastelloy | 対象なし | 対象なし | 対象なし |
| Wrought Iron | Steel | Invar | 対象なし | 対象なし | 対象なし |
| Osmium | Osmiridium | 対象なし | 対象なし | 対象なし | 対象なし |
| Antimony | Solder | 対象なし | 対象なし | 対象なし | 対象なし |
| Loose Andesite | Zinc経路のAndesite Alloy | Tin経路のAndesite Alloy | 対象なし | 対象なし | 対象なし |

mode 6〜8には合金・Steelレシピを割り当てない。いずれも正規のmodeとして循環に含め、Briquet製錬とInsulating Glassを処理する。BriquetとInsulating Glassはmode 1〜5でも同様に処理する。Blueprintがない場合はこの2系統だけを自動処理し、合金・Steel製造を開始しない。

modeは機械全体へ適用するが、配合の照合は主入力枠ごとに行う。例えばmode 1でCopperとGoldを別の主入力枠へ置けば、BrassとRose Goldを必要な材料予約・出力容量の範囲で並列処理する。

#### 競合と設定変更

同じmode・同じ主入力材料から候補を1件へ定めた後、必要な主入力数と添加材数を確認する。材料不足なら待機し、別の配合へ移行しない。レシピに不要な添加材が同居しても、候補を変更せず、対象配合の必要量だけを消費する。投入後に一致したレシピを並べ、その順位をmodeに対応させる処理は採用しない。

新規処理の成立判定では、開始済み処理が予約した主入力と添加材の数量を在庫から控除する。複数の主入力枠が同じ添加材を使用する場合にも、同じ数量を二重に予約しない。

全19レシピの171組に対し、modeで分離する前の競合13組は上表で別modeへ分離される。同一mode・同一主入力へ複数候補を置く組合せも0件となる。データパックによってこの一意性が崩れた場合は、登録順で選ばず、不整合のある主入力の新規処理を停止して理由を表示する。

Copperを主入力とする5配合は、任意の2配合の添加材を4枠以内へ同居させると双方が成立する。このため、主入力割当を維持したまま分離するには最低5modeを要する。採用した8modeはこの条件を満たし、mode 6〜8を未割当として保持する。

| 入力状態 | mode | 要求する結果 |
|---|---:|---|
| Copper 9個、Zinc 1個、Bismuth 1個 | 1 | Brass 10個を生成し、Bismuth1個を残す。 |
| Copper 9個、Zinc 1個、Bismuth 1個 | 3 | Bismuth Bronze5個を生成し、Copper6個を残す。 |
| Copper 9個、Zinc 1個、Bismuthなし | 3 | Bismuthを待機し、Brassを開始しない。 |
| Copper 9個、Zinc 1個、Nickel 1個 | 5 | Constantan2個を生成し、Copper8個とZinc1個を残す。 |
| Wrought Iron 2個、Nickel 1個、Coke Dust 1個 | 1 | Steel1個とSlag1個を生成し、Wrought Iron1個とNickel1個を残す。 |
| Wrought Iron 2個、Nickel 1個、Coke Dust 1個 | 2 | Invar3個を生成し、Coke Dust1個を残す。 |

Blueprintの挿入・交換・取り外しは随時認める。変更を新規開始分へ適用し、開始済み処理は選択済みRecipe IDと配合を保持する。取り出してmodeを変更した後に再投入する操作でも同じ規則を用いる。Mod不在や素材無効化でレシピが減っても、mode番号を詰め直さない。

IE標準のArc JSONには非消費Blueprint入力の概念がないため、通常の材料としてBlueprintを登録して消費させる方式を採用しない。添加材の個数0を指定して存在条件の代用にもしない。TFIEでBlueprintの状態確認とmodeによる限定照合を実装する。[ArcFurnaceRecipe](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/ArcFurnaceRecipe.java)

### 11. Blast FurnaceとImproved Blast Furnace

両設備を通常プレイで製作・形成・運用する。受け入れる製錬材料は、Hematite、Magnetite、LimoniteのBriquetの3種類だけとする。各1個から`tfc:raw_iron_bloom`1個を生成し、SlagやPig Ironを出力しない。入力IDはBriquet全系列表に示す。正規Recipe IDは`tfie:blastfurnace/briquet/hematite`、`tfie:blastfurnace/briquet/magnetite`、`tfie:blastfurnace/briquet/limonite`とする。

Raw Iron Bloomは1個100mB、基準融点1,535℃、生成温度1,381.5℃とする。Raw Iron BloomからRefined Iron Bloom、続いてWrought Iron Ingotへ至るTFC金床加工を維持する。BriquetをArcへ入れた場合のCast Iron出力と区別する。[Raw Iron Bloomの金属量と融点](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/generated/resources/data/tfc/recipe/heating/raw_iron_bloom.json)、[Refined Iron Bloom](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/generated/resources/data/tfc/recipe/anvil/refined_iron_bloom.json)、[Wrought Iron](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/generated/resources/data/tfc/recipe/anvil/metal/ingot/wrought_iron.json)

基礎時間は1個当たり1,200ticksとする。Improved Blast Furnaceの予熱器による加速と、IEの通常の搬入出・進行規則を維持する。通常高炉の1,200ticksを改良高炉でも必ず同じ実経過時間とする制約は設けない。

| 許可する燃料 | 1個の燃焼ticks |
|---|---:|
| Coke | 1,200 |
| Charcoal | 300 |

Coke Block、Charcoal Blockおよびその他の燃料をIE高炉へ追加しない。金属Blockを入出力する一括製錬経路も無効化する。Wrought IronからSteelへの製造はArcの専用配合で行う。[IE高炉データ](https://github.com/BluSunrize/ImmersiveEngineering/tree/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe)

建材のAlloy Brick、Blast Brick、Reinforced Blast Brickは、一般クラフトの章に定める製法で入手する。通常高炉はBlast Brick27個の3×3×3、改良高炉はReinforced Blast Brick27個の3×3×3と上部中央のHopper、Alloy KilnはAlloy Brick8個の2×2×2というIEの基本構造を使用する。形成にはIEのEngineer's Hammerを用いる。Arc、Grist Mill、Power Loomの材料置換は、構造の章で別に定義する。

未回転の通常高炉と改良高炉では、BrickをX・Y・Zそれぞれ0〜2の全27座標へ置く。改良高炉のHopperは`(1,3,1)`、状態は`facing=down, enabled=true`とする。両高炉の形成起点は`(1,1,2)`、マスター位置は`(1,1,1)`とする。Alloy KilnではX・Y・Zそれぞれ0〜1の全8座標へAlloy Brickを置き、形成起点を`(1,1,1)`、マスター位置を`(0,0,1)`とする。[通常高炉構造](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/resources/data/immersiveengineering/structure/multiblocks/blast_furnace.nbt)、[改良高炉構造](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/resources/data/immersiveengineering/structure/multiblocks/improved_blast_furnace.nbt)、[合金窯構造](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/resources/data/immersiveengineering/structure/multiblocks/alloy_smelter.nbt)

### 12. Alloy Kiln

2種類の入力を使用する合金窯として有効にする。次表の6種類・7合金レシピを採用し、各1バッチの基礎時間を200ticksとする。燃料は通常のかまど燃料判定を使用する。Blueprintを要求しない。

| 出力 | 入力1 | 入力2 | 出力数 |
|---|---|---|---:|
| Brass | Copper Ingot9個 | Zinc Ingot1個 | 10 |
| Bronze | Copper Ingot9個 | Tin Ingot1個 | 10 |
| Constantan | Copper Ingot1個 | Nickel Ingot1個 | 2 |
| Electrum | Gold Ingot1個 | Silver Ingot1個 | 2 |
| Invar | Wrought Iron Ingot2個 | Nickel Ingot1個 | 3 |
| Andesite Alloy | Loose Andesite1個 | Zinc Nugget1個 | 1 |
| Andesite Alloy | Loose Andesite1個 | Tin Nugget1個 | 1 |

正規Recipe IDは`tfie:alloysmelter/M`とする。Andesite Alloyの末尾は`andesite_alloy_from_zinc`と`andesite_alloy_from_tin`に分ける。金属主出力へ融点の90%を付与し、通常冷却と異温度非合流を適用する。

非金属加工として、`#c:glass_blocks`2個と`tfcm:metal/dust/wrought_iron`1個から`immersiveengineering:insulating_glass`2個を200ticksで生成する。正規Recipe IDは`tfie:alloysmelter/insulating_glass`とする。この出力は加熱しない。

同じ入力に対するIE標準の別比率を残さず、上表へ置き換える。Rose GoldとManyullynの標準経路を無効化し、Sterling Silver、Osmiridiumおよび3成分以上の合金は追加しない。IEが直前の一致レシピを再利用する場合も、異なる配合を選ぶ競合が残らないことを要求する。[IE AlloyRecipe](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/AlloyRecipe.java)、[時間の既定値](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/crafting/serializers/AlloyRecipeSerializer.java)

### 13. ドリルヘッド

#### 登録と性能

全9種を対象とする。IronとSteelはIE既存アイテムを使用し、他の7種はTFIEのアイテムとする。最大スタック数は1、初期損傷値は0とする。表の範囲は採掘面に対する横幅×縦幅×深さの最大値であり、採掘対象ブロックの採掘可否とIEドリルの有効対象判定を満たす部分だけを掘削する。

| 材質 | HeadアイテムID | 最大範囲 | 速度 | 攻撃値 | 最大耐久 | 採掘tier |
|---|---|---|---:|---:|---:|---|
| Iron | `immersiveengineering:drillhead_iron` | 2×2×1 | 9 | 6 | 6,000 | MinecraftのIron tierを使用する。 |
| Steel | `immersiveengineering:drillhead_steel` | 3×3×1 | 10 | 7 | 10,000 | MinecraftのDiamond tierを使用する。 |
| Black Steel | `tfie:drillhead_black_steel` | 3×3×1 | 12 | 8 | 16,000 | TFC Black Steelのlevel 5を使用する。 |
| Blue Steel | `tfie:drillhead_blue_steel` | 5×5×1 | 15 | 10 | 20,000 | TFC Blue Steelのlevel 6を使用する。 |
| Red Steel | `tfie:drillhead_red_steel` | 5×5×1 | 15 | 10 | 20,000 | TFC Red Steelのlevel 6を使用する。 |
| Invar | `tfie:drillhead_invar` | 2×2×1 | 10 | 7 | 11,000 | TFCM Invarのlevel 3を使用する。 |
| Titanium | `tfie:drillhead_titanium` | 3×3×1 | 13 | 9 | 16,500 | TFCM Titaniumのlevel 5を使用する。 |
| Netherite | `tfie:drillhead_netherite` | 5×5×1 | 16 | 11 | 32,500 | TFCM Netheriteのlevel 6を使用する。 |
| Tungsten Steel | `tfie:drillhead_tungsten_steel` | 5×5×1 | 18 | 12 | 40,625 | TFCM Tungsten Steelのlevel 7を使用する。 |

各HeadはIEのドリルへ装着し、掘削面と命中位置から採掘範囲を決める。奇数幅は対象位置を中心に配置し、2×2の偶数幅は命中した面内の位置で範囲をずらす。追加掘削の候補は、空気でなく、プレイヤーが採取可能で、IEドリルの有効対象であり、主対象の破壊進捗の0.4倍以上の破壊進捗を持つブロックに限定する。

既存5種の性能の根拠は[IE DrillheadItem](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/items/DrillheadItem.java)、[TFIEDrillHeadItem](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/TFIEDrillHeadItem.java)、[TFCTiers](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/TFCTiers.java)である。追加4種の速度・攻撃値・範囲はTFIE 0.1.0の採用値であり、耐久だけを[TFCM ToolMetal](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/common/tool/ToolMetal.java)の通常工具耐久の5倍とする。

#### 金床製法

各Headは対応金属のDouble Sheet 1個から1個を鍛造する。最後の3操作は時間順にbend、draw、hitとする。レシピの規則IDは `bend_third_last`、`draw_second_last`、`hit_last` とする。材料はTFCの鍛造温度条件を満たす必要がある。

正規Recipe IDは`tfie:anvil/drillhead_M`とし、`M`には`iron`、`steel`、`black_steel`、`blue_steel`、`red_steel`、`invar`、`titanium`、`netherite`、`tungsten_steel`を用いる。全9件の`apply_bonus`は`false`とし、完成時の新しい鍛造品質評価を行わない。

| Head | 入力タグ | 金床tier |
|---|---|---:|
| Iron | `c:double_sheets/wrought_iron` | 3 |
| Steel | `c:double_sheets/steel` | 4 |
| Black Steel | `c:double_sheets/black_steel` | 5 |
| Blue Steel | `c:double_sheets/blue_steel` | 6 |
| Red Steel | `c:double_sheets/red_steel` | 6 |
| Invar | `c:double_sheets/invar` | 3 |
| Titanium | `c:double_sheets/titanium` | 5 |
| Netherite | `c:double_sheets/netherite` | 6 |
| Tungsten Steel | `c:double_sheets/tungsten_steel` | 7 |

IE標準の `immersiveengineering:crafting/drillhead_iron` と `immersiveengineering:crafting/drillhead_steel` は無効化し、この金床製法を用いる。無効化したTFCM金属のHead製造レシピは読み込まず、代替製法を追加しない。

基準となる5製法は[Iron](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/drillhead_iron.json)、[Steel](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/drillhead_steel.json)、[Black Steel](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/drillhead_black_steel.json)、[Blue Steel](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/drillhead_blue_steel.json)、[Red Steel](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/drillhead_red_steel.json)である。

#### 修理

通常のMinecraft金床で、損傷したHeadを左欄、対応するIngotを右欄へ置いて修理する。TFIEはIEの `AnvilUpdateEvent` による修理処理を継承する。Iron HeadだけはWrought Ironを修理材とし、Cast Ironと通常Ironによる代替を認めない。

| Head | 修理材タグ | Ingot 1個の最大回復量 |
|---|---|---:|
| Iron | `c:ingots/wrought_iron` | 1,500 |
| Steel | `c:ingots/steel` | 2,500 |
| Black Steel | `c:ingots/black_steel` | 4,000 |
| Blue Steel | `c:ingots/blue_steel` | 5,000 |
| Red Steel | `c:ingots/red_steel` | 5,000 |
| Invar | `c:ingots/invar` | 2,750 |
| Titanium | `c:ingots/titanium` | 4,125 |
| Netherite | `c:ingots/netherite` | 8,125 |
| Tungsten Steel | `c:ingots/tungsten_steel` | 10,156 |

Ingot 1個ごとの回復量は残損傷と最大耐久÷4の整数部の小さい方とする。残損傷が0になるか、右欄のIngotを使い切るまで反復し、実際に使った個数だけを消費する。

IEの経験値費用をそのまま定義すると、最後に消費するIngot 1個による回復量を `r` として基礎費用を `max(1, floor(r / 200))` レベルとする。各材料の費用を合算しない。名前を変更しない場合はこの費用だけを用いる。既存のカスタム名を空欄にして削除する場合は5レベルを加える。新しい名前へ変更する場合は5レベルを加え、修理前にカスタム名が付いていた場合はさらに2レベルを加える。

TFIEはMinecraft金床の入手・製作経路を追加せず、TFC金床の修理レシピも追加しない。TFCがMinecraft金床の通常クラフトを無効化する状態を維持する。修理判定への対応と、その設備の通常プレイでの入手経路を区別する。根拠は[IE修理イベント](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/EventHandler.java)である。

### 14. 手作業による金属加工

#### TFC金床への移行

IEのFaraday防具、Empty Casing、Gun Barrel、Hammer Head、Wirecutter Head、Revolver Bayonetを、以下のTFC金床レシピで製作する。表の入力数量はすべて1個であり、完成時に消費する。`#`はアイテムタグを示す。タグに属する材料を受け付け、出力は表のアイテムIDへ固定する。レシピ型はすべて`tfc:anvil`とする。

Drill Headは金属とドリルの章で定義する9種類を使用する。本節の非Drill 9件と合わせ、TFIE 0.1.0が追加・置換する金床製法は最大18件とする。無効なTFCM金属に対応するHead製法は読み込まない。High Carbon Steelの副産物対応は、TFC既存レシピに対する追加処理であり、この18件へ重複計上しない。

| レシピID | 入力1個 | 出力と数量 | 最低金床tier | 加工規則 | `apply_bonus` |
|---|---|---|---:|---|---|
| [tfie:anvil/armor_faraday_boots](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/armor_faraday_boots.json) | `#c:double_sheets/steel` | `immersiveengineering:armor_faraday_boots` ×1 | 4 | `["punch_not_last","hit_any","draw_any"]` | `true` |
| [tfie:anvil/armor_faraday_chestplate](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/armor_faraday_chestplate.json) | `#c:double_sheets/steel` | `immersiveengineering:armor_faraday_chestplate` ×1 | 4 | `["punch_not_last","hit_any","draw_any"]` | `true` |
| [tfie:anvil/armor_faraday_helmet](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/armor_faraday_helmet.json) | `#c:double_sheets/steel` | `immersiveengineering:armor_faraday_helmet` ×1 | 4 | `["punch_not_last","hit_any","draw_any"]` | `true` |
| [tfie:anvil/armor_faraday_leggings](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/armor_faraday_leggings.json) | `#c:double_sheets/steel` | `immersiveengineering:armor_faraday_leggings` ×1 | 4 | `["punch_not_last","hit_any","draw_any"]` | `true` |
| [tfie:anvil/empty_casing](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/empty_casing.json) | `#c:ingots/copper` | `immersiveengineering:empty_casing` ×2 | 1 | `["punch_any","upset_any","shrink_last"]` | `false` |
| [tfie:anvil/gunpart_barrel](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/gunpart_barrel.json) | `#c:ingots/steel` | `immersiveengineering:gunpart_barrel` ×1 | 4 | `["shrink_any","hit_any","draw_last"]` | `false` |
| [tfie:anvil/tool_head/ie_hammer](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/tool_head/ie_hammer.json) | `#c:ingots/wrought_iron` | `tfie:tool_head/ie_hammer` ×1 | 3 | `["hit_third_last","draw_second_last","upset_last"]` | `false` |
| [tfie:anvil/tool_head/wirecutter](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/tool_head/wirecutter.json) | `#c:ingots/wrought_iron` | `tfie:tool_head/wirecutter` ×1 | 3 | `["bend_not_last","shrink_not_last","hit_last"]` | `false` |
| [tfie:anvil/toolupgrade_revolver_bayonet](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/anvil/toolupgrade_revolver_bayonet.json) | `#c:ingots/steel` | `immersiveengineering:toolupgrade_revolver_bayonet` ×1 | 4 | `["draw_any","bend_any","hit_last"]` | `false` |

Faraday防具は部位ごとに1個のSteel Double Sheetを消費する。同じ入力でも金床で選択した製品に応じてHelmet、Chestplate、Leggings、Bootsを区別する。通常の作業台によるFaraday防具4種、Empty Casing、Gun Barrel、Revolver Bayonetの製法を無効化する。HammerとWirecutterは本節のHeadを使う組立レシピへ置き換える。

TFIE独自の溶接レシピは追加しない。Double IngotやDouble Sheetの事前製作にはTFC/TFCMの材料別製法を使用する。金床での加工と溶接を同一レシピとして扱わない。

#### 金床の操作と完成条件

金床tierは表の最低値以上を要求する。プレイヤーの通常加工では、金床のHammer欄、主手、副手の順に`#c:tools/hammer`に該当する工具を調べる。該当するHammerがなければ加工しない。受理した1操作ごとにHammerの耐久を消費する。

各操作は金床の加工位置を次の量だけ移動させる。

| 操作 | 位置の変化量 |
|---|---:|
| `hit_light` | −3 |
| `hit_medium` | −6 |
| `hit_hard` | −9 |
| `draw` | −15 |
| `punch` | ＋2 |
| `bend` | ＋7 |
| `upset` | ＋13 |
| `shrink` | ＋16 |

加工規則の配列は、列挙した条件をすべて満たすことを要求する。配列の左から右を、そのまま実行順として扱わない。規則名の後半は次の範囲を示す。`hit`はLight、Medium、Hardのいずれでも満たす。

| 規則の接尾辞 | 判定する位置 |
|---|---|
| `any` | 直近の1番目、2番目、3番目のいずれかを調べる。4回以上前の操作は含めない。 |
| `not_last` | 直近の2番目または3番目を調べる。最後の操作は含めない。 |
| `last` | 最後の操作を調べる。 |
| `second_last` | 最後から2番目の操作を調べる。 |
| `third_last` | 最後から3番目の操作を調べる。 |

例えばHammer Headでは、直近3操作の時間順が`hit`、`draw`、`upset`となる。Wirecutter Headでは、最後を`hit`とし、その直前2操作に`bend`と`shrink`を1回ずつ置く。Faraday防具では、直近3操作に`punch`、`hit`、`draw`を1回ずつ置き、`punch`を最後にしない。

完成には、すべての加工規則と加工位置の目標が同時に成立する必要がある。TFC標準の目標位置はワールドのSeedと出力アイテムIDから決まり、40〜113の範囲となる。許容誤差はTFC設定`anvilAcceptableWorkRange`に従い、既定値0では目標位置との一致を要求する。加工位置が0未満または150を超えると入力を破壊する。未加工の位置0で最初に負方向の操作を行った場合だけは、その操作を無視して直ちに材料を破壊することを防ぐ。

根拠は[ForgeStep](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/component/forge/ForgeStep.java)、[ForgeRule](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/component/forge/ForgeRule.java)、[AnvilRecipe](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/recipes/AnvilRecipe.java)、[AnvilBlockEntity](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/blockentities/AnvilBlockEntity.java)である。

#### 金床加工の温度

入力にTFC Heatが登録されている場合、操作時の温度がその材料の`workingTemperature`以上であることを要求する。材料が冷えて条件を満たさなくなった場合は、その操作を受け付けず、再加熱を要する。TFIEが独立した共通温度を設定することはなく、TFC/TFCMの材料別熱データを使用する。入力にHeatがないレシピへ、TFIEから新たな温度条件を加えない。

完成品にもHeatがある場合、TFCの`setTemperatureIfWarmer`により入力温度を引き継ぐ。完成品が既にそれ以上の温度を持つ場合は下げず、完成品にHeatがなければこの転記を行わない。Arc Furnace、高炉、Alloy Kilnの金属出力に適用する「融点の90%」を、金床出力には適用しない。

根拠は[温度条件](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/component/heat/IHeatView.java)と[金床の操作・熱の引継ぎ](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/blockentities/AnvilBlockEntity.java)である。

#### 鍛造品質

`apply_bonus=true`のレシピでは、プレイヤーが通常の金床加工を完了した際にTFCの鍛造品質を評価し、完成品へ`tfc:forging_bonus` Componentを付与する。品質が`none`の場合は新しいComponentを付与しない。品質の評価には、実際の総操作回数を、その目標位置と加工規則に対してTFCが算出した最小操作回数で割った値`r`を使用する。作者欄には加工したプレイヤー名を記録する。

TFCの既定設定における品質と補正値は次のとおりとする。境界値には厳密な「未満」を用いるため、例えば`r=1.5`は`perfect`ではなく`expert`となる。

| 品質ID | 操作回数比`r` | 採掘速度倍率 | 耐久消費抑制の基準確率 | 攻撃ダメージ倍率 |
|---|---|---:|---:|---:|
| `perfect` | `r < 1.5` | 1.8 | 50% | 1.5 |
| `expert` | `1.5 ≤ r < 2` | 1.6 | 37.5% | 1.375 |
| `well` | `2 ≤ r < 5` | 1.4 | 25% | 1.25 |
| `modest` | `5 ≤ r < 10` | 1.2 | 12.5% | 1.125 |
| `none` | `r ≥ 10` | 1.0 | 0% | 1.0 |

これらはTFC設定の`anvilPerfectlyForgedThreshold=1.5`、`anvilExpertForgedThreshold=2.0`、`anvilWellForgedThreshold=5.0`、`anvilModestlyForgedThreshold=10.0`、`anvilMaxEfficiencyMultiplier=1.8`、`anvilMaxDurabilityMultiplier=0.5`、`anvilMaxDamageMultiplier=1.5`による値であり、TFIEは設定を固定・上書きしない。

採掘速度の補正はプレイヤーの主手スタック、攻撃の補正は攻撃者の主手スタックに付いた品質を参照する。耐久補正はダメージを受けるアイテム自体の品質を参照し、通常の耐久消費処理へ作用する。最大耐久値を直接増やす処理ではない。表の耐久確率はTFCの抑制判定へ渡す値であり、複数点をまとめて消費する処理について、常にその割合だけ消費量が減るという保証を表さない。

本節ではFaraday防具4件だけに新しい品質評価を適用する。ほかの5件と本書で定義するDrill Head製法の`apply_bonus`は`false`とする。`false`は完成時の新しい品質評価を行わないことを意味し、別の出力処理が既存Componentを明示的に複製する場合まで一律に削除する指定ではない。工具部品から組立品への品質の継承には、TFCの`tfc:copy_forging_bonus`などの明示的な転記が必要となる。IE Drillに装着したHeadの品質が、Drill本体の採掘速度や独自の耐久処理へ自動的に反映されるという仕様は設けない。TFCの遠隔加工処理から新たな品質評価を行う対応も追加しない。

根拠は[ForgingBonus](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/component/forge/ForgingBonus.java)、[ForgingBonusComponent](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/component/forge/ForgingBonusComponent.java)、[TFCサーバー設定](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/config/ServerConfig.java)、[採掘と攻撃の補正](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/ForgeEventHandler.java)、[耐久処理](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/mixin/ItemStackMixin.java)、[品質の転記](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/recipes/outputs/CopyForgingBonusModifier.java)である。

#### High Carbon SteelのSlag副産物

TFC既存レシピ`tfc:anvil/metal/ingot/high_carbon_steel`が、プレイヤーの通常金床加工によって正常に完了した際、`immersiveengineering:slag`1個を確率100%で追加する。対象の既存レシピは`#c:ingots/pig_iron`1個から`tfc:metal/ingot/high_carbon_steel`1個を生成し、加工規則は`["hit_third_last","hit_second_last","hit_last"]`とする。最低tierはTFCレシピで省略されているため0であり、材料の鍛造温度条件は通常どおり適用する。TFIEはこの主出力・入力・規則を変更しない。

副産物は完成した加工1回につき1個だけ生成する。まず加工したプレイヤーのInventoryへ追加し、収容されなかった残数だけを金床の位置へアイテムとして排出する。加工途中、失敗、完成条件未達、単なる完成品の取り出しでは生成しない。

対象はPig IronからHigh Carbon Steelへの工程だけとする。High Carbon SteelからSteelへの精製、高炭素Black Steel・Blue Steel・Red Steelの加工、機械・遠隔処理による金床加工へ対象を拡張しない。Arc FurnaceのWrought IronからSteelへの配合に含まれるSlagは、本項とは別に定義した機械出力として維持する。

この挙動は[Crossoverの金床副産物処理](https://github.com/NMagpie/TFC-IE-Crossover/blob/41eb5d40bdd9e97152cd400decce4c04f746b902/src/main/java/com/nmagpie/tfc_ie_addon/mixin/AnvilBlockEntityMixin.java)を、[現行TFCレシピID](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/generated/resources/data/tfc/recipe/anvil/metal/ingot/high_carbon_steel.json)へ対応させる仕様である。Crossoverが参照する旧ID`tfc:anvil/high_carbon_steel_ingot`のまま判定しない。

### 15. 一般クラフト

#### 入力と配置の共通規則

以下の表は1回のクラフトに必要な材料数と出力数を示す。配置は文字列配列とし、配列の先頭から上段、中段、下段の順に読む。各文字列は左から右へ読む。空白は空欄、同じ記号は同じ材料条件を表し、その出現回数が消費数量となる。配置ありのレシピ型は`minecraft:crafting_shaped`とし、Minecraft標準の枠内移動と左右反転の扱いを使用する。配置を問わないレシピは`minecraft:crafting_shapeless`とする。

`#`で始まる入力はアイテムタグ、それ以外は固定アイテムIDとする。表の記号別数量は配置全体の合計である。すべて通常の作業台クラフトであり、FEと加工時間を要求しない。各行のレシピIDへのリンクは、配合を確認した一次資料を示す。TFIE 0.1.0で明示変更した材料条件は、本書の表を正とする。

採用した一般クラフトのIron系Ingot、Rod、Plateには、それぞれ`#c:ingots/wrought_iron`、`#c:rods/wrought_iron`、`#c:plates/wrought_iron`を使用する。通常IronまたはCast Ironとの選択入力を設けない。この対応は表に定める個別レシピへ適用し、Wrought Ironを一般のIronタグへ登録する方法を採らない。Components BlueprintとJerrycanなど、標準製法を維持する個別指定は本節の材料置換より優先する。

#### 発電と電気設備

| レシピID | 配置 | 記号別の入力と合計数量 | 出力と数量 |
|---|---|---|---|
| [immersiveengineering:crafting/basic_engineering](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/basic_engineering.json) | `["iwi","w w","iwi"]` | `i`=`#c:ingots/wrought_iron` ×4、`w`=`#immersiveengineering:treated_wood` ×4 | `immersiveengineering:basic_engineering` ×4 |
| [immersiveengineering:crafting/coil_lv](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/coil_lv.json) | `["www","wiw","www"]` | `i`=`#c:ingots/wrought_iron` ×1、`w`=`immersiveengineering:wirecoil_copper` ×8 | `immersiveengineering:coil_lv` ×1 |
| [immersiveengineering:crafting/coil_mv](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/coil_mv.json) | `["www","wiw","www"]` | `i`=`#c:ingots/wrought_iron` ×1、`w`=`immersiveengineering:wirecoil_electrum` ×8 | `immersiveengineering:coil_mv` ×1 |
| [immersiveengineering:crafting/coil_hv](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/coil_hv.json) | `["www","wiw","www"]` | `i`=`#c:ingots/wrought_iron` ×1、`w`=`immersiveengineering:wirecoil_steel` ×8 | `immersiveengineering:coil_hv` ×1 |
| [immersiveengineering:crafting/dynamo](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/dynamo.json) | `["rcr","ili"]` | `c`=`immersiveengineering:component_iron` ×1、`i`=`#c:ingots/wrought_iron` ×2、`l`=`immersiveengineering:coil_lv` ×1、`r`=`#c:dusts/redstone` ×2 | `immersiveengineering:dynamo` ×1 |
| [immersiveengineering:crafting/windmill](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/windmill.json) | `["ppp","pip","ppp"]` | `i`=`#c:ingots/wrought_iron` ×1、`p`=`immersiveengineering:windmill_blade` ×8 | `immersiveengineering:windmill` ×1 |
| [immersiveengineering:crafting/current_transformer](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/current_transformer.json) | `[" m "," b ","iei"]` | `b`=`#immersiveengineering:connector_insulator` ×1、`e`=`immersiveengineering:component_electronic` ×1、`i`=`#c:ingots/wrought_iron` ×2、`m`=`immersiveengineering:voltmeter` ×1 | `immersiveengineering:current_transformer` ×1 |
| [immersiveengineering:crafting/electromagnet](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/electromagnet.json) | `["pcp","wiw","pwp"]` | `c`=`immersiveengineering:component_electronic` ×1、`i`=`#c:ingots/wrought_iron` ×1、`p`=`#c:rods/steel` ×4、`w`=`immersiveengineering:wirecoil_copper` ×3 | `immersiveengineering:electromagnet` ×1 |
| [immersiveengineering:crafting/floodlight](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/floodlight.json) | `["sii","pes","sci"]` | `c`=`immersiveengineering:component_iron` ×1、`e`=`immersiveengineering:light_bulb` ×1、`i`=`#c:ingots/wrought_iron` ×3、`p`=`#c:glass_panes` ×1、`s`=`#c:plates/silver` ×3 | `immersiveengineering:floodlight` ×1 |
| [immersiveengineering:crafting/transformer](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/transformer.json) | `["lm","eb","ii"]` | `b`=`immersiveengineering:coil_mv` ×1、`e`=`immersiveengineering:component_electronic` ×1、`i`=`#c:ingots/wrought_iron` ×2、`l`=`immersiveengineering:connector_lv` ×1、`m`=`immersiveengineering:connector_mv` ×1 | `immersiveengineering:transformer` ×1 |
| [immersiveengineering:crafting/transformer_hv](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/transformer_hv.json) | `["mh","eb","ii"]` | `b`=`immersiveengineering:coil_hv` ×1、`e`=`immersiveengineering:component_electronic` ×1、`h`=`immersiveengineering:connector_hv` ×1、`i`=`#c:ingots/wrought_iron` ×2、`m`=`immersiveengineering:connector_mv` ×1 | `immersiveengineering:transformer_hv` ×1 |

#### 搬送と建築設備

| レシピID | 配置 | 記号別の入力と合計数量 | 出力と数量 |
|---|---|---|---|
| [immersiveengineering:crafting/conveyor_basic](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/conveyor_basic.json) | `["lll","iri"]` | `i`=`#c:ingots/wrought_iron` ×2、`l`=`#c:leathers` ×3、`r`=`#c:dusts/redstone` ×1 | `immersiveengineering:conveyor_basic` ×8 |
| [immersiveengineering:crafting/conveyor_splitter](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/conveyor_splitter.json) | `["cic"," c "]` | `c`=`immersiveengineering:conveyor_basic` ×3、`i`=`#c:ingots/wrought_iron` ×1 | `immersiveengineering:conveyor_splitter` ×3 |
| [immersiveengineering:crafting/pipe_valve](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/pipe_valve.json) | `["pc","sr"]` | `c`=`immersiveengineering:component_iron` ×1、`p`=`immersiveengineering:fluid_pipe` ×1、`r`=`#c:dusts/redstone` ×1、`s`=`#c:rods/wrought_iron` ×1 | `immersiveengineering:pipe_valve` ×1 |
| [immersiveengineering:crafting/hatch](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/hatch.json) | `["rr","pp"]` | `p`=`#c:plates/wrought_iron` ×2、`r`=`#c:rods/wrought_iron` ×2 | `immersiveengineering:hatch` ×1 |
| [immersiveengineering:crafting/treated_post](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/treated_post.json) | `["f","f","s"]` | `f`=`immersiveengineering:treated_fence` ×2、`s`=`#tfie:stone_bricks_no_variants` ×1 | `immersiveengineering:treated_post` ×1 |
| [immersiveengineering:crafting/steel_post](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/steel_post.json) | `["f","f","s"]` | `f`=`immersiveengineering:steel_fence` ×2、`s`=`#tfie:stone_bricks_no_variants` ×1 | `immersiveengineering:steel_post` ×1 |
| [immersiveengineering:crafting/alu_post](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/alu_post.json) | `["f","f","s"]` | `f`=`immersiveengineering:alu_fence` ×2、`s`=`#tfie:stone_bricks_no_variants` ×1 | `immersiveengineering:alu_post` ×1 |
| [immersiveengineering:crafting/craftingtable](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/craftingtable.json) | `["sss","rcr","r r"]` | `c`=`#c:player_workstations/crafting_tables` ×1、`r`=`#c:rods/treated_wood` ×4、`s`=`tfie:treated_wood_lumber` ×3 | `immersiveengineering:craftingtable` ×1 |
| [immersiveengineering:crafting/workbench](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/workbench.json) | `["iss","c f"]` | `c`=`immersiveengineering:craftingtable` ×1、`f`=`immersiveengineering:treated_fence` ×1、`i`=`#c:ingots/wrought_iron` ×1、`s`=`#immersiveengineering:treated_wood_slab` ×2 | `immersiveengineering:workbench` ×1 |

#### 工具と追加部品

| レシピID | 配置 | 記号別の入力と合計数量 | 出力と数量 |
|---|---|---|---|
| [immersiveengineering:crafting/hammer](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/hammer.json) | `["h","s"]` | `h`=`tfie:tool_head/ie_hammer` ×1、`s`=`#c:rods/wooden` ×1 | `immersiveengineering:hammer` ×1 |
| [immersiveengineering:crafting/wirecutter](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/wirecutter.json) | `["si"," s"]` | `i`=`tfie:tool_head/wirecutter` ×1、`s`=`#c:rods/wooden` ×2 | `immersiveengineering:wirecutter` ×1 |
| [immersiveengineering:crafting/screwdriver](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/screwdriver.json) | `[" i","s "]` | `i`=`#c:rods/wrought_iron` ×1、`s`=`#c:rods/wooden` ×1 | `immersiveengineering:screwdriver` ×1 |
| [immersiveengineering:crafting/earmuffs](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/earmuffs.json) | `[" S ","S S","W W"]` | `S`=`#c:rods/wrought_iron` ×3、`W`=`#minecraft:wool` ×2 | `immersiveengineering:earmuffs` ×1 |
| [immersiveengineering:crafting/speedloader](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/speedloader.json) | `["sd","dc"]` | `c`=`immersiveengineering:component_iron` ×1、`d`=`#c:plates/plastic` ×2、`s`=`#c:rods/wrought_iron` ×1 | `immersiveengineering:speedloader` ×1 |
| [immersiveengineering:crafting/toolupgrade_buzzsaw_spareblades](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/toolupgrade_buzzsaw_spareblades.json) | `["rht","rt "]` | `h`=`#c:fiber_hemp` ×1、`r`=`#c:rods/wrought_iron` ×2、`t`=`#immersiveengineering:treated_wood` ×2 | `immersiveengineering:toolupgrade_buzzsaw_spareblades` ×1 |
| [immersiveengineering:crafting/toolupgrade_powerpack_magnet](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/toolupgrade_powerpack_magnet.json) | `["rer","wiw"," w "]` | `e`=`immersiveengineering:component_electronic_adv` ×1、`i`=`#c:ingots/wrought_iron` ×1、`r`=`#c:rods/steel` ×2、`w`=`immersiveengineering:wirecoil_copper` ×3 | `immersiveengineering:toolupgrade_powerpack_magnet` ×1 |
| [immersiveengineering:crafting/toolupgrade_shield_magnet](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/toolupgrade_shield_magnet.json) | `["  l","lc ","lil"]` | `c`=`immersiveengineering:coil_lv` ×1、`i`=`#c:ingots/wrought_iron` ×1、`l`=`#c:leathers` ×4 | `immersiveengineering:toolupgrade_shield_magnet` ×1 |
| [immersiveengineering:crafting/toolupgrade_shield_shock](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/toolupgrade_shield_shock.json) | `["crc","crc","crc"]` | `c`=`immersiveengineering:connector_lv` ×6、`r`=`#c:rods/wrought_iron` ×3 | `immersiveengineering:toolupgrade_shield_shock` ×1 |

#### 布と繊維を使う製品

| レシピID | 配置 | 記号別の入力と合計数量 | 出力と数量 |
|---|---|---|---|
| [immersiveengineering:crafting/balloon](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/balloon.json) | `[" f ","ftf"," s "]` | `f`=`#c:fabric_hemp` ×3、`s`=`#immersiveengineering:treated_wood_slab` ×1、`t`=`minecraft:torch` ×1 | `immersiveengineering:balloon` ×2 |
| [immersiveengineering:crafting/cushion](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/cushion.json) | `["fff","f f","fff"]` | `f`=`#c:fabric_hemp` ×8 | `immersiveengineering:cushion` ×3 |
| [immersiveengineering:crafting/strip_curtain](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/strip_curtain.json) | `["sss","fff","fff"]` | `f`=`#c:fabric_hemp` ×6、`s`=`#c:rods/all_metal` ×3 | `immersiveengineering:strip_curtain` ×3 |
| [immersiveengineering:crafting/wirecoil_structure_rope](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/wirecoil_structure_rope.json) | `[" w ","wsw"," w "]` | `s`=`#c:rods/wooden` ×1、`w`=`#c:fiber_hemp` ×4 | `immersiveengineering:wirecoil_structure_rope` ×4 |
| [immersiveengineering:crafting/gunpowder_barrel](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/gunpowder_barrel.json) | `["gfg","gbg"]` | `b`=`immersiveengineering:wooden_barrel` ×1、`f`=`#c:fiber_hemp` ×1、`g`=`#c:gunpowders` ×4 | `immersiveengineering:gunpowder_barrel` ×1 |

IE Crafting Tableの上段はTreated Lumber3個とし、Treated Slabを使用しない。Engineer's Workbenchは上表どおりTreated Slab2個を使用する。Treated、Steel、Aluminumの各Postは、対応するFence2個を縦に置き、その下へ`#tfie:stone_bricks_no_variants`1個を置く。HatchのPlateとRodは両方ともWrought Ironへ限定する。

Treated Lumber4個を2×2に置いて`immersiveengineering:treated_wood_horizontal`1個を得る製法、およびLumberのCreosote処理と製材は、処理木材の章で定義する。Sheet MoldとMolds Blueprintの製法はMetal Pressの章で定義する。Molds BlueprintではWrought Iron Plate1個を使用し、Components Blueprintとは個別に扱う。

#### 炉の建材

| レシピID | 配置 | 記号別の入力と合計数量 | 出力と数量 |
|---|---|---|---|
| [immersiveengineering:crafting/alloybrick](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/alloybrick.json) | `["sb","bs"]` | `b`=`#c:bricks` ×2、`s`=`#c:sandstone/blocks` ×2 | `immersiveengineering:alloybrick` ×2 |
| [immersiveengineering:crafting/blastbrick](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/blastbrick.json) | `["csc","sms","csc"]` | `c`=`#c:bricks/nether` ×4、`m`=`minecraft:magma_block` ×1、`s`=`#c:bricks` ×4 | `immersiveengineering:blastbrick` ×3 |
| [immersiveengineering:crafting/blastbrick_reinforced](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/blastbrick_reinforced.json) | 配置を問わない。 | `immersiveengineering:blastbrick` ×1、`#c:plates/steel` ×1 | `immersiveengineering:blastbrick_reinforced` ×1 |

Alloy Brick、Blast Brick、Reinforced Blast Brickの製作を有効にする。これらのレシピに対する無効化定義を残さず、Alloy Kiln、Blast Furnace、Improved Blast Furnaceの形成を妨げない。各設備の構造と加工対象は、それぞれの設備の章に定義する。

#### 個別に維持するIE標準製法

| レシピID | 配置 | 記号別の入力と合計数量 | 出力と数量 |
|---|---|---|---|
| [immersiveengineering:crafting/blueprint_components](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/blueprint_components.json) | `["jkl","ddd","ppp"]` | `d`=`#c:dyes/blue` ×3、`j`=`#c:ingots/copper` ×1、`k`=`#c:ingots/lead` ×1、`l`=`#c:ingots/iron` ×1、`p`=`#c:paper` ×3 | `immersiveengineering:blueprint` ×1。Component `{"immersiveengineering:blueprint":"components"}` |
| [immersiveengineering:crafting/fluorescent_tube](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/fluorescent_tube.json) | `["GeG","GgG","GgG"]` | `G`=`#c:glass_blocks` ×6、`e`=`immersiveengineering:graphite_electrode` ×1、`g`=`#c:dusts/glowstone` ×2 | `immersiveengineering:fluorescent_tube` ×1 |
| [immersiveengineering:crafting/chemthrower](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/chemthrower.json) | `[" tg"," hg","pb "]` | `b`=`minecraft:bucket` ×1、`g`=`immersiveengineering:wooden_grip` ×2、`h`=`immersiveengineering:heavy_engineering` ×1、`p`=`immersiveengineering:fluid_pipe` ×1、`t`=`immersiveengineering:toolupgrade_drill_waterproof` ×1 | `immersiveengineering:chemthrower` ×1 |
| [immersiveengineering:crafting/jerrycan](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/jerrycan.json) | `[" ii","ibb","ibb"]` | `b`=`minecraft:bucket` ×4、`i`=`#c:plates/iron` ×4 | `immersiveengineering:jerrycan` ×1 |

Components BlueprintはCopper、Lead、通常IronのIngot各1個を使用する。LeadをAluminumへ変更せず、通常IronをWrought Ironへ変更しない。Fluorescent TubeのGlowstone Dust2個をSmall Sphaleriteへ変更しない。

ChemthrowerにはMinecraft Bucket1個、JerrycanにはMinecraft Bucket4個を要求する。TFIEはBlue Steel Bucket・Red Steel BucketなどのTFC容器で代用する追加経路を設けない。JerrycanのPlate条件もIE標準の`#c:plates/iron`を維持する。流体Ingredientの容器対応は、ここで指定した固定アイテム`minecraft:bucket`の置換には作用しない。

TFCがMinecraft Bucketの通常クラフトを無効化する状態は維持する。Components Blueprintの通常Iron、Chemthrower・JerrycanのMinecraft Bucketが、採用したTFIE製造工程だけでは調達条件を満たさない場合でも、TFIEはその条件を回避する追加製法を設けない。標準レシピの維持は、すべての構成環境での材料入手を保証する指定ではない。

TFIE独自のSpecial Bullet Blueprintクラフトは追加しない。出荷しない旧追加レシピIDは`tfie:crafting/blueprint_special_bullet`、`tfie:crafting/chemthrower_tfc`、`tfie:crafting/jerrycan_tfc`とする。Special Bullet Blueprintに関係するIE自身の入手手段を、この不追加指定を理由に削除しない。

固定Bucketの条件は上表のIE各レシピ、通常クラフトの無効化は[TFCのBucketレシピ](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/generated/resources/data/minecraft/recipe/bucket.json)、旧追加レシピの識別は[Special Bullet Blueprint](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/crafting/blueprint_special_bullet.json)、[Chemthrower](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/crafting/chemthrower_tfc.json)、[Jerrycan](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/crafting/jerrycan_tfc.json)を根拠とする。

#### FirmaLifeのErsatz Leather

| レシピID | 配置 | 記号別の入力と合計数量 | 出力と数量 |
|---|---|---|---|
| [tfie:crafting/ersatz_leather_firmalife](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/crafting/ersatz_leather_firmalife.json) | `["fff","fcf","fff"]` | `c`=`firmalife:food/raw_honey` ×1、`f`=`#c:fabric_hemp` ×8 | `immersiveengineering:ersatz_leather` ×8 |

この1件はMod ID`firmalife`の導入時だけ読み込む。Raw Honeyの入力は固定アイテム`firmalife:food/raw_honey`であり、Honey Fluidを使うレシピではない。FirmaLife未導入時には参照先が存在しないレシピを残さず、TFIEによる代替材料の配合も追加しない。`#c:fabric_hemp`には後述のTFC Burlap Clothを含める。

#### 布と繊維の共通タグ

以下の値を既存のアイテムタグへ追加する。タグ全体を置き換えず、IE自身のHemp FabricとHemp Fiberなど既存の値を維持する。

| タグID | TFIEが追加する固定アイテムID | 用途 |
|---|---|---|
| `c:fabric_hemp` | `tfc:burlap_cloth` | Hemp Fabricを材料とするクッション、気球、カーテン、FirmaLifeの人工皮革などへ対応する。 |
| `c:fiber_hemp` | `tfc:jute_fiber` | Hemp Fiberを材料とするロープ、Gunpowder Barrel、Buzzsaw用追加部品などへ対応する。 |

この対応は素材間の相互変換レシピを追加する指定ではない。タグを入力として参照するレシピが、追加したTFCアイテムを受け付けるようにする。

根拠は[Fabricタグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/c/tags/item/fabric_hemp.json)と[Fiberタグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/c/tags/item/fiber_hemp.json)である。

#### 支柱用Stone Bricksタグ

`tfie:stone_bricks_no_variants`は、次の22アイテムを持つアイテムタグとする。Postの基礎にはこのタグを使用し、Slab、Stairs、Wall、苔付き、ひび入りなどの派生形を自動で含めない。Minecraftの通常Stone Bricks1種を明示的に含める。

| 番号 | アイテムID |
|---:|---|
| 1 | `tfc:rock/bricks/granite` |
| 2 | `tfc:rock/bricks/diorite` |
| 3 | `tfc:rock/bricks/gabbro` |
| 4 | `tfc:rock/bricks/shale` |
| 5 | `tfc:rock/bricks/claystone` |
| 6 | `tfc:rock/bricks/limestone` |
| 7 | `tfc:rock/bricks/conglomerate` |
| 8 | `tfc:rock/bricks/dolomite` |
| 9 | `tfc:rock/bricks/chert` |
| 10 | `tfc:rock/bricks/chalk` |
| 11 | `tfc:rock/bricks/tuff` |
| 12 | `tfc:rock/bricks/rhyolite` |
| 13 | `tfc:rock/bricks/basalt` |
| 14 | `tfc:rock/bricks/andesite` |
| 15 | `tfc:rock/bricks/dacite` |
| 16 | `tfc:rock/bricks/quartzite` |
| 17 | `tfc:rock/bricks/slate` |
| 18 | `tfc:rock/bricks/phyllite` |
| 19 | `tfc:rock/bricks/schist` |
| 20 | `tfc:rock/bricks/gneiss` |
| 21 | `tfc:rock/bricks/marble` |
| 22 | `minecraft:stone_bricks` |

根拠は[Stone Bricksタグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tags/item/stone_bricks_no_variants.json)である。

#### 手作業レシピの除外範囲

次のクラフトを削除・置換する。ここで示す`S`、`T`、`M`は、対象集合に列挙した値だけを代入する文字列である。任意の金属や防具へ削除を広げない。

| レシピIDまたはIDテンプレート | 対象集合 | 処置 |
|---|---|---|
| `immersiveengineering:crafting/armor_faraday_S` | `boots`、`chestplate`、`helmet`、`leggings` | 通常クラフト4件を削除し、本章の金床4件へ移す。 |
| `immersiveengineering:crafting/empty_casing` | 固定ID1件とする。 | 通常クラフトを削除し、本章の金床製法を使用する。 |
| `immersiveengineering:crafting/gunpart_barrel` | 固定ID1件とする。 | 通常クラフトを削除し、本章の金床製法を使用する。 |
| `immersiveengineering:crafting/toolupgrade_revolver_bayonet` | 固定ID1件とする。 | 通常クラフトを削除し、本章の金床製法を使用する。 |
| `immersiveengineering:crafting/drillhead_M` | `iron`、`steel` | 通常クラフト2件を削除し、Drill Headの金床製法を使用する。 |
| `immersiveengineering:crafting/armor_steel_S` | `boots`、`chestplate`、`helmet`、`leggings` | IE Steel防具4件を削除する。TFIEによるIE Steel防具の代替製法を追加しない。 |
| `immersiveengineering:crafting/T_steel` | `axe`、`hoe`、`pickaxe`、`shovel`、`sword` | IE Steel工具5件を削除する。TFIEによるIE Steel工具の代替製法を追加しない。 |
| `immersiveengineering:crafting/gunpowder_from_dusts` | 固定ID1件とする。 | 粉末からの火薬クラフトを削除し、TFIEの代替製法を追加しない。 |

Copper、Nickel、Silver、SteelのIE Nugget相互変換8件と、同じIDに置かれたTFIEの上書きは無効化し、TFCM既存の変換を使用する。IE独自金属8種のIngotと圧縮Blockの相互変換16件を無効化し、TFC/TFCMのPlated Block製法を使用する。Copper、Gold、Iron、Nickel、Silver、Steelの手動Plate加工6件とIron・Steelの手動Rod加工2件を無効化し、TFC/TFCM金床とMetal Pressを使用する。完全な削除ID一覧と判定範囲は、レシピの削除・置換の章で定義する。

本章の入力と出力に変更を加えていない、上記範囲外のIE一般クラフトは維持する。TFC/TFCM側が素材や設定により制限する既存経路を、TFIEの代替レシピで補完しない。

除外元の根拠は[TFIEのIEクラフト定義一覧](https://github.com/lau-mods/TFIE/tree/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting)である。Steel防具・工具の個別例は[Steel Helmet](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/armor_steel_helmet.json)と[Steel Pickaxe](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/pickaxe_steel.json)を参照する。

### 16. マルチブロックの構造

相対座標はテンプレートの原点を `(0,0,0)` とし、`(X,Y,Z)` の順で記載する。`+Y` は上、未回転テンプレートの `+X` は東、`+Z` は南とする。回転・鏡映する場合はすべての座標とブロック状態へ同じ変換を適用する。

3設備ともIEのTemplateMultiblock方式を使用する。Engineer's Hammerによる形成操作の起点と、形成後のマスターブロック位置を次表とする。

| 設備 | 形成起点 | 起点ブロック | マスター位置 |
|---|---|---|---|
| Arc Furnace | `(2,0,4)` | `tfc:crucible` とする。 | `(2,1,2)` とする。 |
| Grist Mill | `(2,1,1)` | `tfc:metal/block/steel` とする。 | `(1,0,1)` とする。 |
| Power Loom | `(1,1,4)` | `immersiveengineering:light_engineering` とする。 | `(1,0,2)` とする。 |

IEの形成処理ではクリック面の反対方向をNorthからの回転先とする。South面への操作は無回転、West面は上から見て時計回り90度、North面は180度、East面は反時計回り90度となる。上下の面を形成起点として扱わない。鏡映も認め、左右を入れ替えた配置へ同じ材料条件を適用する。通常のAirは構造の照合・置換対象から除外する。したがって、下表にない座標はテンプレート上の空欄であり、空気必須の判定を新設しない。

Advanced TFC Tech 3.3の形成位置は[GristMillMultiblock](https://github.com/HermitOwO/AdvancedTFCTech/blob/911df3cb4821324c3565d0d0cc59ea14dadd2c52/src/main/java/com/hermitowo/advancedtfctech/common/multiblocks/GristMillMultiblock.java)と[PowerLoomMultiblock](https://github.com/HermitOwO/AdvancedTFCTech/blob/911df3cb4821324c3565d0d0cc59ea14dadd2c52/src/main/java/com/hermitowo/advancedtfctech/common/multiblocks/PowerLoomMultiblock.java)に基づく。

Steel Plated BlockとBlack Steel Plated Blockの指定箇所では、それぞれ `tfc:metal/block/steel` と `tfc:metal/block/black_steel` とのブロックID一致を要求する。同じstorage blockタグを持つ他のブロックで代用しない。ArcのFire BricksとCrucibleにも対応するTFCブロックを使用する。Grist MillとPower Loomへの変更はAdvanced TFC Tech Unofficial導入時だけ適用する。

#### Arc Furnace

構造寸法は 5×5×5 とする。寸法の順序はX・Y・Zとする。次表で全非空ブロックの材料・数量・座標を定義する。

| ブロックID | 数量 | 相対座標 |
|---|---:|---|
| `immersiveengineering:sheetmetal_steel` | 8 | (0,0,0)、(0,0,1)、(1,0,0)、(3,0,0)、(4,0,0)、(4,0,1)、(0,2,0)、(4,2,0) |
| `tfc:metal/block/black_steel` | 6 | (0,0,2)、(4,0,2)、(0,1,2)、(4,1,2)、(0,2,2)、(4,2,2) |
| `immersiveengineering:slab_sheetmetal_steel` | 14 | (0,0,3)、(1,0,1)、(1,0,2)、(1,0,3)、(1,0,4)、(2,0,1)、(2,0,2)、(2,0,3)、(3,0,1)、(3,0,2)、(3,0,3)、(3,0,4)、(4,0,3)、(4,0,4) |
| `immersiveengineering:steel_scaffolding_standard` | 5 | (0,0,4)、(1,3,0)、(3,3,0)、(1,4,0)、(3,4,0) |
| `immersiveengineering:heavy_engineering` | 5 | (2,0,0)、(0,1,0)、(0,1,1)、(4,1,0)、(4,1,1) |
| `tfc:crucible` | 1 | (2,0,4) |
| `immersiveengineering:rs_engineering` | 1 | (0,1,4) |
| `immersiveengineering:light_engineering` | 10 | (1,1,0)、(2,1,0)、(3,1,0)、(1,2,0)、(2,2,0)、(3,2,0)、(2,3,0)、(2,4,0)、(2,4,1)、(2,4,2) |
| `tfc:fire_bricks` | 27 | (1,1,1)、(1,1,2)、(2,1,1)、(2,1,2)、(3,1,1)、(3,1,2)、(1,2,1)、(1,2,2)、(1,2,3)、(1,2,4)、(2,2,1)、(2,2,2)、(2,2,3)、(2,2,4)、(3,2,1)、(3,2,2)、(3,2,3)、(3,2,4)、(1,3,1)、(1,3,2)、(1,3,3)、(2,3,1)、(2,3,2)、(2,3,3)、(3,3,1)、(3,3,2)、(3,3,3) |

テンプレートの表示状態は次のとおりとする。状態値を持たないブロックは各ブロックの既定状態を用いる。

| 対象ブロック | テンプレート状態 |
|---|---|
| `immersiveengineering:slab_sheetmetal_steel` | `waterlogged=false`、`type=bottom` |
| `immersiveengineering:steel_scaffolding_standard` | `waterlogged=false` |

根拠は[Arc Furnaceの構造定義](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/structure/multiblocks/arcfurnace.nbt)である。

#### Grist Mill

構造寸法は 4×3×3 とする。寸法の順序はX・Y・Zとする。次表で全非空ブロックの材料・数量・座標を定義する。

| ブロックID | 数量 | 相対座標 |
|---|---:|---|
| `immersiveengineering:steel_scaffolding_standard` | 9 | (0,0,0)、(0,0,2)、(1,0,0)、(1,0,1)、(2,0,0)、(2,0,1)、(2,0,2)、(3,0,0)、(3,0,2) |
| `immersiveengineering:sheetmetal_steel` | 3 | (0,0,1)、(3,0,1)、(1,1,2) |
| `immersiveengineering:light_engineering` | 4 | (1,0,2)、(0,1,1)、(1,1,1)、(3,1,1) |
| `immersiveengineering:rs_engineering` | 1 | (1,1,0) |
| `immersiveengineering:slab_sheetmetal_steel` | 4 | (2,1,0)、(2,1,2)、(3,1,0)、(3,1,2) |
| `tfc:metal/block/steel` | 1 | (2,1,1) |
| `minecraft:hopper` | 1 | (1,2,1) |

テンプレートの表示状態は次のとおりとする。状態値を持たないブロックは各ブロックの既定状態を用いる。

| 対象ブロック | テンプレート状態 |
|---|---|
| `immersiveengineering:steel_scaffolding_standard` | `waterlogged=false` |
| `immersiveengineering:slab_sheetmetal_steel` | `waterlogged=false`、`type=bottom` |
| `minecraft:hopper` | `facing=down`、`enabled=true` |

根拠は[Grist Millの構造定義](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/advancedtfctech/structure/multiblocks/grist_mill.nbt)である。

#### Power Loom

構造寸法は 3×3×5 とする。寸法の順序はX・Y・Zとする。次表で全非空ブロックの材料・数量・座標を定義する。

| ブロックID | 数量 | 相対座標 |
|---|---:|---|
| `immersiveengineering:steel_scaffolding_standard` | 13 | (0,0,1)、(0,0,2)、(0,0,3)、(2,0,1)、(2,0,2)、(2,0,3)、(0,1,1)、(0,1,3)、(2,1,1)、(2,1,3)、(2,2,1)、(2,2,2)、(2,2,3) |
| `immersiveengineering:light_engineering` | 3 | (1,0,0)、(1,1,0)、(1,1,4) |
| `immersiveengineering:heavy_engineering` | 3 | (1,0,1)、(1,0,2)、(1,0,3) |
| `tfc:metal/block/steel` | 1 | (1,0,4) |
| `immersiveengineering:rs_engineering` | 1 | (2,0,4) |
| `immersiveengineering:sheetmetal_steel` | 5 | (1,1,1)、(1,1,3)、(1,2,1)、(1,2,2)、(1,2,3) |
| `immersiveengineering:steel_fence` | 1 | (1,1,2) |

テンプレートの表示状態は次のとおりとする。状態値を持たないブロックは各ブロックの既定状態を用いる。

| 対象ブロック | テンプレート状態 |
|---|---|
| `immersiveengineering:steel_scaffolding_standard` | `waterlogged=false` |
| `immersiveengineering:steel_fence` | `east=false`、`waterlogged=false`、`south=true`、`north=true`、`west=false` |

根拠は[Power Loomの構造定義](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/advancedtfctech/structure/multiblocks/power_loom.nbt)である。

Arc Furnaceの起点と方向変換は[ArcFurnaceMultiblock](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/ArcFurnaceMultiblock.java)と[TemplateMultiblock](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/multiblocks/TemplateMultiblock.java)、材料の厳密照合は[MultiblockCompatibility](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/MultiblockCompatibility.java)を根拠とする。

### 17. 木材加工と処理木材

#### 製材対象

TFIE 0.1.0はIE Sawmillへ、TFCの20樹種、ArborFirmaCraftの14樹種、TFC OverdimsのCrimson・Warpedの2種を追加する。合計36種を対象とし、各種類に4レシピを定義する。以下の表が対象木材の完全な集合である。

| 木材の提供元 | Item IDの名前空間 `N` | 木材識別子 `S` の完全な集合 | 木材数 | 製材レシピ数 |
|---|---|---|---:|---:|
| TerraFirmaCraft | `tfc` とする。 | `acacia`、`ash`、`aspen`、`birch`、`blackwood`、`chestnut`、`douglas_fir`、`hickory`、`kapok`、`mangrove`、`maple`、`oak`、`palm`、`pine`、`rosewood`、`sequoia`、`spruce`、`sycamore`、`white_cedar`、`willow` とする。 | 20 | 80 |
| ArborFirmaCraft | `afc` とする。 | `araucaria`、`baobab`、`beech`、`cypress`、`eucalyptus`、`fig`、`ginkgo`、`hevea`、`ipe`、`ironwood`、`mahoe`、`mahogany`、`teak`、`tualang` とする。 | 14 | 56 |
| TFC Overdims | `tfcoverdims` とする。 | `crimson`、`warped` とする。 | 2 | 8 |
| 合計 | 上記3名前空間を使用する。 | 上記36種を対象とする。 | 36 | 144 |

`N` と `S` は同じ行から組み合わせる。例えばOakでは `N=tfc`、`S=oak`、Heveaでは `N=afc`、`S=hevea`、Crimsonでは `N=tfcoverdims`、`S=crimson` とする。TFCとAFCの136件は、旧TFIEの全製材JSONの入力・出力・副産物・FEと照合した集合である。Overdimsの8件は、以下の同じ数量と処理構造で追加する。[旧TFIEのTFC製材](https://github.com/lau-mods/TFIE/tree/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/sawmill)、[Overdimsの木材定義](https://github.com/lau-mods/TFC-Overdims/blob/d01a4b088802a7dcac9f81b5d601ecf07a15ac62/src/main/java/net/claustra01/tfcoverdims/common/wood/OverdimsWood.java)

#### 製材する形状とレシピID

木材のItem IDは `{N}:wood/{形状}/{S}` とする。`log` は樹皮付き原木、`wood` は全面に樹皮を持つ木材ブロック、`stripped_log` と `stripped_wood` はそれぞれの剥皮済み形状、`planks` は板材ブロックを表す。`lumber` は板材アイテムであり、`planks` のブロックとは区別する。

各木材について次の4レシピを登録する。レシピの名前空間は入力元のModにかかわらず `tfie` とし、型はすべて `immersiveengineering:sawmill` とする。表中の個数は1回の加工量であり、入力は常に1個とする。

| レシピID | 入力 | `stripped` に設定する剥皮後の形状 | 刃を使用する場合の主出力 `result` | 剥皮時の副産物 `strippingSecondaries` | 切断時の副産物 `secondaryOutputs` |
|---|---|---|---|---|---|
| `tfie:sawmill/{S}/log` | `{N}:wood/log/{S}` 1個を使用する。 | `{N}:wood/stripped_log/{S}` 1個とする。 | `{N}:wood/lumber/{S}` 12個とする。 | Wood Dust 1個とする。 | Wood Dust 1個とする。 |
| `tfie:sawmill/{S}/wood` | `{N}:wood/wood/{S}` 1個を使用する。 | `{N}:wood/stripped_wood/{S}` 1個とする。 | `{N}:wood/lumber/{S}` 12個とする。 | Wood Dust 1個とする。 | Wood Dust 1個とする。 |
| `tfie:sawmill/{S}/stripped_log` | `{N}:wood/stripped_log/{S}` または `{N}:wood/stripped_wood/{S}` のいずれか1個を使用する。 | 指定しない。 | `{N}:wood/lumber/{S}` 12個とする。 | 生成しない。 | Wood Dust 1個とする。 |
| `tfie:sawmill/{S}/planks` | `{N}:wood/planks/{S}` 1個を使用する。 | 指定しない。 | `{N}:wood/lumber/{S}` 4個とする。 | 生成しない。 | 生成しない。 |

`stripped_log` の1レシピが2種類の剥皮済み入力をOR条件で扱う。したがって、各木材に入力形状が5種類あっても、JSONは4件とする。`stripped_wood` 専用の5件目のJSONを追加しない。レシピの資源パスは `data/tfie/recipe/sawmill/{S}/{K}.json` とし、`K` の集合は `log`、`wood`、`stripped_log`、`planks` とする。木材識別子の集合に重複がないため、全144件のレシピIDも重複しない。

Wood Dustは出力タグ `#c:dusts/wood` からIEのTagOutput処理で解決する1個とする。基準IEのタグに属するアイテムは `immersiveengineering:dust_wood` である。原木の2個分のDustは、剥皮時の1個と切断時の1個を別々の副産物欄へ定義する。剥皮済み原木に剥皮時のDustを再付与しない。[TFC Log](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/sawmill/oak/log.json)、[TFC Wood](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/sawmill/oak/wood.json)、[AFCの剥皮済み入力](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/sawmill/hevea/stripped_log.json)、[Planks](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/sawmill/oak/planks.json)、[Wood Dustタグ](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/c/tags/item/dusts/wood.json)

Overdimsでも同じItem ID規則を適用する。CrimsonとWarpedの4種類の原木IDは、それぞれ `tfcoverdims:wood/log/{S}`、`tfcoverdims:wood/wood/{S}`、`tfcoverdims:wood/stripped_log/{S}`、`tfcoverdims:wood/stripped_wood/{S}` とする。板材ブロックは `tfcoverdims:wood/planks/{S}`、出力Lumberは `tfcoverdims:wood/lumber/{S}` とする。Vanillaの `minecraft:crimson_stem` や `minecraft:warped_stem` を、この入力IDの代用にしない。[Overdimsの木材登録](https://github.com/lau-mods/TFC-Overdims/blob/d01a4b088802a7dcac9f81b5d601ecf07a15ac62/src/main/java/net/claustra01/tfcoverdims/common/wood/OverdimsWoodBlocks.java)、[Crimson原木タグ](https://github.com/lau-mods/TFC-Overdims/blob/d01a4b088802a7dcac9f81b5d601ecf07a15ac62/src/main/resources/data/tfcoverdims/tags/item/crimson_logs.json)、[Warped原木タグ](https://github.com/lau-mods/TFC-Overdims/blob/d01a4b088802a7dcac9f81b5d601ecf07a15ac62/src/main/resources/data/tfcoverdims/tags/item/warped_logs.json)

Overdims本体の手鋸加工は原木系1個からLumber 8個、通常クラフトはLumber 4個からPlanks 1個である。TFIEのSawmillは原木系の出力を12個とし、Planksから4個を回収する。手鋸や通常クラフト自体の数量は変更しない。[Crimsonの手鋸加工](https://github.com/lau-mods/TFC-Overdims/blob/d01a4b088802a7dcac9f81b5d601ecf07a15ac62/src/main/resources/data/tfcoverdims/recipe/wood/lumber/crimson_from_logs.json)、[Warpedの手鋸加工](https://github.com/lau-mods/TFC-Overdims/blob/d01a4b088802a7dcac9f81b5d601ecf07a15ac62/src/main/resources/data/tfcoverdims/recipe/wood/lumber/warped_from_logs.json)、[Crimsonの板材クラフト](https://github.com/lau-mods/TFC-Overdims/blob/d01a4b088802a7dcac9f81b5d601ecf07a15ac62/src/main/resources/data/tfcoverdims/recipe/wood/planks/crimson.json)、[Warpedの板材クラフト](https://github.com/lau-mods/TFC-Overdims/blob/d01a4b088802a7dcac9f81b5d601ecf07a15ac62/src/main/resources/data/tfcoverdims/recipe/wood/planks/warped.json)

#### 製材の費用と進行

144レシピすべての基準消費量を1回1,600 FEとする。加工時間はIE Sawmillの標準値である80 ticksとし、標準設定では加工中の1処理当たり20 FE/tを消費する。剥皮済み原木やPlanksも同じ費用・時間を使用する。これらはIEの `sawmill.energyModifier` と `sawmill.timeModifier` が両方1の場合の値であり、管理者がIE設定を変更した場合はIEの標準倍率を適用する。TFIE独自の倍率、温度条件、流体入力を追加しない。[SawmillRecipeの80 ticks](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/SawmillRecipe.java)、[Sawmillレシピの読込形式](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/crafting/serializers/SawmillRecipeSerializer.java)、[IEの機械設定](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/config/IEServerConfig.java)

FEが1 tick分に不足する間、またはRedstone制御で停止している間は、該当処理を進めない。機械の標準蓄電容量は32,000 FE、内部処理列の上限は6処理とする。各処理は入力1個を保持し、同一種類の入力を近接して搬入する場合にも、個数・FE・出力を合算した別配合へ変更しない。主出力と副産物はIE標準の別々の排出口へ送る。接続先へ収納されない分はIEの標準処理で排出口からアイテムとして排出する。[SawmillLogicの処理列と出力](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/logic/sawmill/SawmillLogic.java)

#### 刃を装着しない場合

Sawmillの刃を装着しなくても搬送と剥皮は進行する。全144レシピは、刃の有無によってFEと所要時間を減らさない。刃を装着しない状態を加工開始から終了まで維持した場合、出力は次のとおりとする。

| 入力1個 | 刃なしの主出力 | 刃なしの副産物 |
|---|---|---|
| `{N}:wood/log/{S}` を投入する。 | `{N}:wood/stripped_log/{S}` 1個を排出する。 | Wood Dust 1個を剥皮時に生成する。 |
| `{N}:wood/wood/{S}` を投入する。 | `{N}:wood/stripped_wood/{S}` 1個を排出する。 | Wood Dust 1個を剥皮時に生成する。 |
| `{N}:wood/stripped_log/{S}` を投入する。 | 投入した剥皮済みLog 1個をそのまま排出する。 | 生成しない。 |
| `{N}:wood/stripped_wood/{S}` を投入する。 | 投入した剥皮済みWood 1個をそのまま排出する。 | 生成しない。 |
| `{N}:wood/planks/{S}` を投入する。 | 投入したPlanks 1個をそのまま排出する。 | 生成しない。 |

刃なしの加工ではLumberを得ない。LogとWoodの `stripped` 出力は、刃を使用する通常加工でLumberへ追加される副産物ではなく、剥皮工程後の中間形状である。刃を使用して最後まで加工した場合、主出力はLumberだけとする。

基準IEの `SawmillProcess` は、進捗31.25%で剥皮副産物を出力し、進捗86.25%でその時点の刃の有無に応じて切断副産物を出力する。標準80 ticksでは、それぞれ25 tick目と69 tick目に相当する。完了時の主出力は、その時点で刃が装着されているかにより決まる。加工途中の停止、刃の着脱、他の処理による刃の破損が生じた場合も、このIE標準の段階別判定を維持する。切断副産物の判定時点と主出力の判定時点をTFIE独自に統合しない。[SawmillProcessの剥皮・切断・出力判定](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/logic/sawmill/SawmillProcess.java)

#### 刃の装着と耐久

Sawmillへ装着する刃は `#c:sawblades` に属するアイテム1個とする。基準IEでは `immersiveengineering:sawblade` がこのタグへ登録され、最大耐久は10,000である。TFIEはこのタグ、刃の最大耐久、機械への装着規則を変更しない。[装着用タグ](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/c/tags/item/sawblades.json)、[標準刃の登録値](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/register/IEItems.java)

基準IEの `sawmill_bladeDamage` は5とし、主出力を排出した各処理の完了時に、装着中の刃へこの耐久消費を適用する。原木、剥皮済み材、Planksで耐久消費を変更しない。未使用の標準刃を他用途に使用せず、設定も変更しない場合、5ずつの耐久消費により2,000処理を完了する。破損が生じる処理では主出力の排出後に刃を破損させ、残る処理は刃なし時の段階別判定に従う。[耐久設定](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/config/IEServerConfig.java)、[完了後の耐久消費](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/logic/sawmill/SawmillLogic.java)、[ItemStackの耐久適用](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/utils/ItemUtils.java)

刃が空の機械には、対象の刃を持って操作して1個を装着する。装着済みの刃を交換または取り外す際は、IEのRedstone制御で機械を停止する。停止中に対象の刃を持って操作すると交換し、空の手でスニーク操作すると取り外す。機械を解体した場合は、内部の刃をIE標準の解体処理で排出する。電源のFE残量だけをゼロにする操作は、Redstoneによる停止の代用にしない。基準IEの着脱判定はRedstoneの有効状態を用いる。[SawmillLogicの着脱処理](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/logic/sawmill/SawmillLogic.java)

#### 条件付き互換とBeneathの除外

ArborFirmaCraftとTFC Overdimsは、TFIEの必須依存、任意依存、本番用ビルド依存、配布ページの依存宣言へ追加しない。ArborFirmaCraftの旧依存宣言は削除する。両Modの木材製材は、該当Modの導入と、各レシピが参照する入力・剥皮後の形状・LumberのItem IDの存在を条件として読み込む。未導入または参照アイテム不在の場合は、その互換レシピを読み込まず、TFIE本体の起動を妨げない。

AFCの導入判定にはMod ID `afc`、Overdimsの導入判定にはMod ID `tfcoverdims` を用いる。剥皮済み材を扱うレシピでは、OR条件に記載した2種類の入力IDを参照対象とする。未導入Modの木材IDをTFCやVanillaの木材へ置き換える代替処理は設けない。旧AFC製材も `neoforge:mod_loaded` 条件を使用しており、0.1.0では参照アイテムの存在確認を含む条件付き読込を採用する。[旧AFCの条件付き製材](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/sawmill/hevea/log.json)、[NeoForge 1.21.1のデータ読込条件](https://docs.neoforged.net/docs/1.21.1/resources/server/conditions/)

TFIE 0.1.0にはBeneathの木材を参照する製材レシピを含めない。`beneath:` の木材を `tfcoverdims:` の別名として受け入れず、Beneath自体が持つ他用途のレシピは変更しない。固定基準の旧TFIE製材136件には `beneath:` の参照がないため、この基準から削除する既存Beneath製材ID集合は空とする。Overdimsの追加8件とBeneath向け製材の不採用を区別する。

#### Treated Wood Lumber

処理済みの板材アイテムとして `tfie:treated_wood_lumber` を登録する。TFCの密閉樽で通常のLumberをCreosoteに浸し、このアイテムへ加工する。レシピIDは `tfie:barrel/treated_wood_lumber`、型は `tfc:barrel_sealed` とし、次表の値を定義する。

| 項目 | 定義 |
|---|---|
| アイテム入力 | `#tfc:lumber` に属するアイテム1個を使用する。 |
| 流体入力 | `immersiveengineering:creosote` 50 mBを使用する。流体IDを直接指定する。 |
| アイテム出力 | `tfie:treated_wood_lumber` 1個を生成する。 |
| 流体出力 | 新たな出力流体を定義しない。 |
| 処理時間 | `duration = 8000` とする。 |
| 電力・熱 | FEと加熱を要求しない。 |

材料と流体の消費はTFCの密閉樽の単位配合として扱う。樽に同じ入力アイテムが `n` 個、Creosoteが `f` mBある場合、1回の完了時の加工数は `min(n, floor(f / 50))` とする。その個数のTreated Wood Lumberを生成し、同数のLumberと、その50倍のmBのCreosoteを消費する。例えばLumber 16個とCreosote 800 mBでは、同じ8,000 ticksの配合時間でTreated Wood Lumber 16個を生成する。加工数に応じて処理時間を16倍にしない。余った未加工アイテムとCreosoteは、TFCの通常処理で保持する。

時間管理はTFC密閉樽の標準処理を用いる。レシピ進行の基準時刻、封印・開封、チャンク再読込時の経過処理をTFIE独自の時計へ置換しない。8,000はレシピの `duration` の値であり、IE機械のFE消費を伴う80 ticksとは別の処理体系である。[旧TFIEの密閉樽レシピ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/barrel/treated_wood_lumber.json)、[TFCの樽の数量計算](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/recipes/BarrelRecipe.java)、[密閉樽レシピ](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/recipes/SealedBarrelRecipe.java)、[TFCの樽の時間処理](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/blockentities/BarrelBlockEntity.java)

#### 処理木材ブロックへの組立

レシピID `tfie:crafting/treated_wood_horizontal` を通常の定形クラフトとして登録する。型は `minecraft:crafting_shaped` とし、`tfie:treated_wood_lumber` 4個を2行2列へ配置して `immersiveengineering:treated_wood_horizontal` 1個を生成する。パターンは `["XX", "XX"]`、`X` は `tfie:treated_wood_lumber` とする。Treated Slabをこの入力に使用しない。密閉樽から通して製作する場合、処理木材ブロック1個につき通常Lumber 4個とCreosote 200 mBを消費する。[Treated Lumberの組立レシピ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/crafting/treated_wood_horizontal.json)

IE標準の `immersiveengineering:crafting/treated_wood_horizontal` は無効化する。この標準レシピはPlanks 8個と `#c:creosote` 1,000 mBから処理木材ブロック8個を作る流体クラフトであり、TFIEの密閉樽とLumber組立を通らないためである。標準レシピとTFIEレシピは名前空間が異なるため、TFIE側に組立を追加するだけで標準側の削除を省略しない。処理木材ブロックの既存の向き変更、Slab・Stairsへの加工をこの1件の無効化に含めない。[IE標準の油クラフト](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/treated_wood_horizontal.json)、[旧TFIEの同ID無効化](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/treated_wood_horizontal.json)

### 18. 麻の栽培

IEのHempをTFCの作物として栽培する。種は `immersiveengineering:seed`、繊維は `immersiveengineering:hemp_fiber` を使用し、TFIE独自の種や繊維は登録しない。IEの種をTFC農地へ使用すると、TFIEの栽培株を植える。

| 種別 | 登録ID | 仕様 |
|---|---|---|
| 栽培株 | `tfie:crop/hemp` | TFCの二段作物として扱う。独立したBlockItemは登録しない。 |
| 枯死株 | `tfie:dead_crop/hemp` | 成熟状態と上下段を保持する枯死作物とする。独立したBlockItemは登録しない。 |
| 野生株 | `tfie:wild_crop/hemp` | TFCの二段野生作物として扱い、対応するBlockItemも登録する。 |
| 作物BlockEntity | `tfie:crop` | 栽培株の成長度、収量係数、吸収済みN/P/K、寿命、最終更新時刻を保存・同期する。 |
| 気候定義 | `tfie:crop/hemp` | TFCのClimate Rangeデータとして読み込む。 |

根拠は[HempCrop](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/HempCrop.java)、[ブロック登録](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/ModBlocks.java)、[種の植付け登録](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/ModInteractions.java)、[作物BlockEntity登録](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/ModBlockEntities.java)である。

#### 栽培条件と生育

| 項目 | 値または動作 |
|---|---|
| 生育温度 | 10℃以上31℃以下とする。 |
| 農地水分 | 30%以上80%以下とする。 |
| 温度・水分の許容拡張 | 両方0とし、生育範囲の外に別の生存許容幅を設けない。 |
| 窒素要求量 | N=0.6とする。 |
| リン要求量 | P=0.2とする。 |
| カリウム要求量 | K=0.6とする。 |
| 成長段階 | `age=0,1,2,3,4` の5段階とする。 |
| 上下段 | `part=bottom` と `part=top` を使用する。age 0〜3は下段、age 4で上段を形成する。 |
| 上段予定位置の障害物 | TFC二段作物の規則により成長上限を0.75へ制限する。上段予定位置が空気または同種作物であれば、通常の成長上限を適用する。 |
| 農地 | 下段は `#tfc:farmlands` 上で生存する。 |
| 生育処理 | TFCの共通作物処理を下段だけで実行し、上下段による二重成長・二重栄養消費を生じさせない。 |
| 描画 | 生株・枯死株・野生株はcutout描画とする。 |
| 物理特性 | 当たり判定による衝突を設けず、硬度0.4、作物の音、可燃性60・延焼速度30、ピストン移動時の破壊を設定する。 |

N/P/Kは作物が吸収する栄養の基準量であり、各tickにその全量を消費する値ではない。TFCの共通処理が、経過時間、農地の栄養量、作物の吸収済み量、気温、水分、寿命、成長・寿命設定から生育と収量を求める。通常の光量条件は12以上とする。TFIE独自の固定成長日数は設けない。気候不適合と寿命到達時は、TFCの枯死判定により枯死株へ移行する。上段を操作した場合の農地情報・施肥先は下段に対応させる。

`minecraft:crops` と `minecraft:mineable/hoe` のブロックタグへ生株・枯死株・野生株を追加する。`tfc:can_be_snow_piled` には野生株だけを追加する。既存のタグ内容は置換しない。

根拠は[Hemp気候データ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tfc/climate_range/crop/hemp.json)、[TFCの二段作物](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/blocks/crop/DoubleCropBlock.java)、[TFCの生育計算](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/blocks/crop/CropHelpers.java)、[TFCの成長上限](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/blocks/crop/ICropBlock.java)である。

#### 収穫と枯死

生成物は下段のLoot Tableで定義し、上段から同じ生成物を重複して出力しない。全ドロッププールへ `minecraft:survives_explosion` 条件を付ける。

| 状態 | Hemp Fiber | Hemp Seeds |
|---|---|---|
| 栽培株のage 0〜3 | 出力しない。 | 1個を出力する。 |
| 栽培株のage 4 | TFCの収量係数を用いて数量を決める。 | 1個を出力する。 |
| 成熟前の枯死株 | 出力しない。 | 1個を出力する。 |
| 成熟後の枯死株 | 出力しない。 | 1〜3個を一様に抽選する。 |
| 未成熟の野生株 | 出力しない。 | 1個を出力する。 |
| 成熟した野生株 | 1〜3個を一様に抽選する。 | 1個を出力する。 |

成熟した栽培株の繊維数には `tfc:crop_yield_uniform` を使用する。最小値0、最大値を6〜10の一様分布とし、TFCが保存する収量係数で最小値と抽選した最大値の間を補間して個数を得る。したがって、成熟した全株へ無条件に6〜10個を与える仕様ではない。種の数を繊維数へ連動させない。

根拠は[栽培株のLoot Table](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/loot_table/blocks/crop/hemp.json)、[枯死株のLoot Table](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/loot_table/blocks/dead_crop/hemp.json)、[野生株のLoot Table](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/loot_table/blocks/wild_crop/hemp.json)、[収量補間](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/util/loot/CropYieldProvider.java)である。

#### 野生生成

| 項目 | 仕様 |
|---|---|
| 個体Feature ID | Configured Feature・Placed Featureとも `tfie:plant/wild_crop/hemp` とする。 |
| 群落Feature ID | Configured Feature・Placed Featureとも `tfie:plant/wild_crop/hemp_patch` とする。 |
| 個体Feature種別 | `tfc:tall_wild_crop` とし、対象ブロックを `tfie:wild_crop/hemp` とする。 |
| 群落Feature種別 | `minecraft:random_patch` とする。 |
| 群落内試行数 | 6回とする。 |
| 群落の水平拡散 | `xz_spread=5` とする。 |
| 群落の垂直拡散 | `y_spread=1` とする。 |
| 頻度フィルター | `minecraft:rarity_filter` の `chance=90` とする。 |
| 水平配置 | `minecraft:in_square` を適用する。 |
| 生成時の気候 | 温度10〜40℃、地下水値140〜360を要求する。栽培時の温度・農地水分とは区別する。 |
| 高さ | `WORLD_SURFACE_WG` Heightmapを使用する。 |
| 配置条件 | 配置場所がTFCのreplaceable判定を満たし、下段のwould_survive判定が成立することを要求する。 |
| TFCワールド生成への登録 | `#tfc:feature/crops` のPlaced Featureタグへ群落を追加し、既存内容を置換しない。 |

野生株の成熟期はTFCの半球補正済みの暦月と `wildCropMaturityStartMonth`・`wildCropMaturityEndMonth` に従う。基準設定では6月から10月までを両端含みで成熟期とし、設定が年をまたぐ範囲も扱う。成熟期の変更は上下段へ反映する。冬季に雪へ覆われた状態は成熟状態を解除する。

地下水条件は実データの `min_groundwater`・`max_groundwater` を指す。Rainfallという別の条件へ読み替えない。根拠は[個体配置](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/worldgen/placed_feature/plant/wild_crop/hemp.json)、[群落定義](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/worldgen/configured_feature/plant/wild_crop/hemp_patch.json)、[群落配置と気候](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/worldgen/placed_feature/plant/wild_crop/hemp_patch.json)、[TFC野生作物の季節処理](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/blocks/crop/WildCropBlock.java)、[TFC成熟期設定](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/config/ServerConfig.java)である。

### 19. Garden Clocheによる作物栽培

#### 共通の加工条件

Garden ClocheにTFC作物29種類の栽培レシピを追加する。レシピ型は `immersiveengineering:cloche` とし、レシピIDは `tfie:cloche/<作物識別子>` とする。下表の作物識別子を `<作物識別子>` に代入する。種スロットは `tfc:seeds/<作物識別子>`、描画ブロックは `tfc:crop/<作物識別子>` とする。栽培中に配置した種と土壌を消費しない。

全29レシピの `time` は128,000とする。この値は成長の目標量であり、固定のFE総額を表す値ではない。IE標準設定では、成長中の1サーバーtickに成長量1を加算し、8 FEを消費する。肥料使用時は成長加算量へ肥料倍率を乗じる。収穫を確定するtickも8 FEを消費する。IEの `garden_cloche.growth_modifier` を変更した場合は、成長加算量にその値も乗じる。既定値は1.0とする。

液体は `minecraft:water` とする。通常のWaterを使用し、TFCのSalt Water、Spring Water、Alcoholなどを同じ入力として扱わない。1回の補給でWater 250 mBを消費して6,000稼働ticks分の養液残量を得る。補給時に有効な肥料が肥料スロットにある場合は1個消費し、その投与期間へ肥料倍率を適用する。肥料がない場合も倍率1.0で栽培する。肥料アイテムの種類と倍率は本仕様の肥料の章で定義する。これらはTFC農地の窒素・リン・カリウム量とは別のCloche用成長倍率である。

Clocheのタンク容量は4,000 mB、内部電力容量は16,000 FEとする。入力は土壌1枠、種1枠、肥料1枠、出力は4枠とする。成長と収穫の条件には種・土壌の一致、Water、養液残量、電力を用い、TFIEはTFCの外気温、季節、降水、農地水分、農地栄養、支柱を追加条件としない。電力不足または有効な栽培条件の喪失時は、IEの処理に従って成長進捗を0へ戻す。収穫出力が全量収まらない場合は収穫を確定せず、成長完了状態で待機する。この待機中に成長用の追加FE・養液残量を消費しない。機械の設定値を利用者が変更する場合はIE側の設定を適用する。

収穫1回につき下表の主出力を必ず生成し、同じ作物の種 `tfc:seeds/<作物識別子>` 1個を25%で追加する。食品の出力を保存食へ変換する処理や、TFCの腐敗を無効化する処理を追加しない。

根拠は[29件の栽培定義](https://github.com/lau-mods/TFIE/tree/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/cloche)、[IEのCloche処理](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/metal/ClocheBlockEntity.java)、[IEのCloche設定](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/config/IEServerConfig.java)、[既定流体の定義](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/crafting/serializers/ClocheRecipeSerializer.java)とする。

#### 対応する土壌

全29レシピについて、次の8種類のいずれかを土壌スロットへ置く。土壌の種類による出力個数、時間、消費FEの差を設けない。

| 土壌 | 入力アイテムID |
|---|---|
| Entisol | `tfc:dirt/entisol` |
| Aridisol | `tfc:dirt/aridisol` |
| Oxisol | `tfc:dirt/oxisol` |
| Fluvisol | `tfc:dirt/fluvisol` |
| Andisol | `tfc:dirt/andisol` |
| Podzol | `tfc:dirt/podzol` |
| Alfisol | `tfc:dirt/alfisol` |
| Mollisol | `tfc:dirt/mollisol` |

Cloche内の土壌表面は、各入力 `tfc:dirt/<土壌識別子>` に対して `tfc:block/farmland/<土壌識別子>` のテクスチャを使用する。上表は入力する土ブロックの一覧であり、Farmlandブロックを要求するという意味ではない。[土壌テクスチャ登録](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/ClocheRenderFunctions.java)

#### 栽培対象と収穫量

| 作物識別子 | 主出力アイテムID | 主出力個数 | 描画 |
|---|---|---:|---|
| `alfalfa` | `tfc:alfalfa` | 6 | 通常の作物描画を使用する。 |
| `barley` | `tfc:food/barley` | 6 | 通常の作物描画を使用する。 |
| `beet` | `tfc:food/beet` | 6 | 通常の作物描画を使用する。 |
| `cabbage` | `tfc:food/cabbage` | 6 | 通常の作物描画を使用する。 |
| `canola` | `tfc:canola` | 6 | 通常の作物描画を使用する。 |
| `carrot` | `tfc:food/carrot` | 6 | 通常の作物描画を使用する。 |
| `cassava` | `tfc:food/cassava` | 6 | 通常の作物描画を使用する。 |
| `garlic` | `tfc:food/garlic` | 6 | 通常の作物描画を使用する。 |
| `green_bean` | `tfc:food/green_bean` | 6 | 二段描画とし、age 4から上段を表示する。 |
| `jute` | `tfc:jute` | 6 | 二段描画とし、age 3から上段を表示する。 |
| `lentil` | `tfc:food/lentil` | 6 | 通常の作物描画を使用する。 |
| `maize` | `tfc:food/maize` | 6 | 二段描画とし、age 3から上段を表示する。 |
| `melon` | `tfc:melon` | 1 | 通常の作物描画を使用する。 |
| `oat` | `tfc:food/oat` | 6 | 通常の作物描画を使用する。 |
| `onion` | `tfc:food/onion` | 6 | 通常の作物描画を使用する。 |
| `papyrus` | `tfc:papyrus` | 6 | 二段描画とし、age 3から上段を表示する。 |
| `peanut` | `tfc:food/peanut` | 6 | 通常の作物描画を使用する。 |
| `potato` | `tfc:food/potato` | 6 | 通常の作物描画を使用する。 |
| `pumpkin` | `tfc:pumpkin` | 1 | 通常の作物描画を使用する。 |
| `radish` | `tfc:food/radish` | 6 | 通常の作物描画を使用する。 |
| `red_bell_pepper` | `tfc:food/red_bell_pepper` | 4 | 通常の作物描画を使用する。 |
| `rice` | `tfc:food/rice` | 6 | 通常の作物描画を使用する。 |
| `rye` | `tfc:food/rye` | 6 | 通常の作物描画を使用する。 |
| `soybean` | `tfc:food/soybean` | 6 | 通常の作物描画を使用する。 |
| `squash` | `tfc:food/squash` | 6 | 通常の作物描画を使用する。 |
| `sugarcane` | `tfc:food/sugarcane` | 6 | 二段描画とし、age 4から上段を表示する。 |
| `tomato` | `tfc:food/tomato` | 6 | 二段描画とし、age 4から上段を表示する。 |
| `wheat` | `tfc:food/wheat` | 6 | 通常の作物描画を使用する。 |
| `yellow_bell_pepper` | `tfc:food/yellow_bell_pepper` | 4 | 通常の作物描画を使用する。 |

MelonとPumpkinの主出力は果実ブロック1個とし、切り分けた食品6個へ変更しない。Red Bell PepperとYellow Bell Pepperは対応する成熟色の食品4個とし、Green Bell Pepperを追加生成しない。Alfalfa、Canola、Jute、Papyrusは `tfc:food/` ではなく表に示した素材アイテムを出力する。[Melon定義](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/cloche/melon.json)、[Red Bell Pepper定義](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/cloche/red_bell_pepper.json)、[Alfalfa定義](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/cloche/alfalfa.json)

#### 二段作物の描画

Green Bean、Jute、Maize、Papyrus、Sugarcane、Tomatoは、描画関数 `tfie:doublecroptfc` を使用する。全体の描画倍率は0.6875とする。正規化した成長率を `g`、対象ブロックのageプロパティの最大値を `maxAge` とし、描画ageを `min(maxAge, round(maxAge × g))` とする。表のageに達する前は下段だけを描画し、達した後は同じageの上段も1ブロック上へ描画する。上段には `DoubleCropBlock.Part.TOP` を設定する。Green Bean、Sugarcane、Tomatoはage 4、Jute、Maize、Papyrusはage 3を二段化の境界とする。

残る23作物は `immersiveengineering:crop` の描画関数を使用する。描画の登録と土壌テクスチャの初期化を、専用サーバーでクライアント専用クラスを参照せずに行う。[二段作物の描画定義](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/ClocheRenderFunctions.java)

### 20. Squeezerによる種子の圧搾

#### 加工条件と出力

対象の種1個を消費し、Plant Oil `immersiveengineering:plantoil` 80 mBを生成する。固体出力と確率副産物は設けない。1処理は6,400 FE、IE標準倍率で80 ticksとする。入力流体、加熱温度、肥料を要求しない。

レシピ型は `immersiveengineering:squeezer`、レシピIDは `tfie:squeezer/<作物識別子>` とする。対象は次表の23種類に限定する。Green Bean、Lentil、Garlic、Potato、Sugarcane、Papyrusの種にはTFIEのPlant Oil加工を追加しない。

| 作物識別子 | 入力アイテムID |
|---|---|
| `alfalfa` | `tfc:seeds/alfalfa` |
| `barley` | `tfc:seeds/barley` |
| `beet` | `tfc:seeds/beet` |
| `cabbage` | `tfc:seeds/cabbage` |
| `canola` | `tfc:seeds/canola` |
| `carrot` | `tfc:seeds/carrot` |
| `cassava` | `tfc:seeds/cassava` |
| `jute` | `tfc:seeds/jute` |
| `maize` | `tfc:seeds/maize` |
| `melon` | `tfc:seeds/melon` |
| `oat` | `tfc:seeds/oat` |
| `onion` | `tfc:seeds/onion` |
| `peanut` | `tfc:seeds/peanut` |
| `pumpkin` | `tfc:seeds/pumpkin` |
| `radish` | `tfc:seeds/radish` |
| `red_bell_pepper` | `tfc:seeds/red_bell_pepper` |
| `rice` | `tfc:seeds/rice` |
| `rye` | `tfc:seeds/rye` |
| `soybean` | `tfc:seeds/soybean` |
| `squash` | `tfc:seeds/squash` |
| `tomato` | `tfc:seeds/tomato` |
| `wheat` | `tfc:seeds/wheat` |
| `yellow_bell_pepper` | `tfc:seeds/yellow_bell_pepper` |

[対象・数量・費用の定義](https://github.com/lau-mods/TFIE/tree/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/squeezer)、[IE Squeezerの80 ticks定義](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/SqueezerRecipe.java)

#### 旧版から是正する機械種別

TFC-IE Crossover 2.1.3の `SqueezerRecipes` は `FermenterRecipeBuilder` を使用し、旧TFIE 0.0.3の対応23件も `type: immersiveengineering:fermenter` を保持する。そのため両旧版の実挙動はFermenterによるPlant Oil製造となる。TFIE 0.1.0では採用済みのSqueezer加工という要件に合わせてレシピ型を `immersiveengineering:squeezer` へ是正する。対象種子、80 mB、6,400 FE、80 ticksは維持し、Fermenter側へ同じPlant Oilレシピを残さない。[Crossoverの生成処理](https://github.com/NMagpie/TFC-IE-Crossover/blob/41eb5d40bdd9e97152cd400decce4c04f746b902/src/data/java/com/nmagpie/tfc_ie_addon/data/recipes/SqueezerRecipes.java)、[旧TFIEのAlfalfaレシピ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/squeezer/alfalfa.json)

### 21. FermenterによるEthanol製造

#### 共通の加工条件

レシピ型は `immersiveengineering:fermenter` とし、入力アイテムまたは対象タグのアイテム1個を消費する。流体出力はEthanol `immersiveengineering:ethanol` とし、入力流体、固体出力、確率副産物を設けない。全レシピの消費FEは6,400、IE標準倍率における時間は80 ticksとする。レシピIDは `tfie:fermenter/<識別子>` とし、下表の識別子を用いる。

食品の入力には通常のアイテム・タグ照合を使用し、腐敗していないことを要求する `tfc:not_rotten` 条件を追加しない。このため、対象食品は腐敗後もFermenterの原料として受け付ける。FirmaLifeのRaw Honeyにも同じ規則を適用する。ButterからCaramelへのMixer加工に定めた非腐敗条件を、このFermenterへ拡張しない。[個別食品レシピ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/fermenter/potato.json)、[Raw Honeyレシピ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/fermenter/raw_honey.json)、[IE Fermenterの時間定義](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/FermenterRecipe.java)

#### TFC食品の個別加工

次の26種類は入力1個につきEthanol 80 mBを生成する。

| レシピ識別子 | 入力アイテムID | Ethanol量 |
|---|---|---:|
| `banana` | `tfc:food/banana` | 80 |
| `beet` | `tfc:food/beet` | 80 |
| `blackberry` | `tfc:food/blackberry` | 80 |
| `blueberry` | `tfc:food/blueberry` | 80 |
| `bunchberry` | `tfc:food/bunchberry` | 80 |
| `cassava` | `tfc:food/cassava` | 80 |
| `cherry` | `tfc:food/cherry` | 80 |
| `cloudberry` | `tfc:food/cloudberry` | 80 |
| `cranberry` | `tfc:food/cranberry` | 80 |
| `elderberry` | `tfc:food/elderberry` | 80 |
| `gooseberry` | `tfc:food/gooseberry` | 80 |
| `green_apple` | `tfc:food/green_apple` | 80 |
| `lemon` | `tfc:food/lemon` | 80 |
| `melon_slice` | `tfc:food/melon_slice` | 80 |
| `olive` | `tfc:food/olive` | 80 |
| `orange` | `tfc:food/orange` | 80 |
| `peach` | `tfc:food/peach` | 80 |
| `plum` | `tfc:food/plum` | 80 |
| `potato` | `tfc:food/potato` | 80 |
| `pumpkin_chunks` | `tfc:food/pumpkin_chunks` | 80 |
| `raspberry` | `tfc:food/raspberry` | 80 |
| `red_apple` | `tfc:food/red_apple` | 80 |
| `snowberry` | `tfc:food/snowberry` | 80 |
| `strawberry` | `tfc:food/strawberry` | 80 |
| `sugarcane` | `tfc:food/sugarcane` | 80 |
| `wintergreen_berry` | `tfc:food/wintergreen_berry` | 80 |

個別食品の対応集合にはPeanut、各種Jam、果実ブロックそのものを含めない。Melonは `tfc:food/melon_slice`、Pumpkinは `tfc:food/pumpkin_chunks` を入力とする。[全個別レシピ](https://github.com/lau-mods/TFIE/tree/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/fermenter)

#### 穀粒の加工

レシピID `tfie:fermenter/grains` は `#c:foods/grain` 1個からEthanol 80 mBを生成する。TFC 4.2.16の当該タグに属するアイテムは次の6種類とする。

| 穀粒 | 入力アイテムID |
|---|---|
| Barley Grain | `tfc:food/barley_grain` |
| Maize Grain | `tfc:food/maize_grain` |
| Oat Grain | `tfc:food/oat_grain` |
| Rice Grain | `tfc:food/rice_grain` |
| Rye Grain | `tfc:food/rye_grain` |
| Wheat Grain | `tfc:food/wheat_grain` |

Clocheの収穫物である `tfc:food/barley` などの未脱穀作物と、`tfc:food/barley_grain` などの穀粒を区別する。レシピ条件は上記6アイテムの固定列挙ではなく `#c:foods/grain` とするため、他Modまたは利用者のデータパックがこのタグへ追加したアイテムも同じレシピ条件に従う。[穀粒レシピ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/fermenter/grains.json)、[TFC 4.2.16のGrainタグ](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/generated/resources/data/c/tags/item/foods/grain.json)

#### FirmaLifeのRaw Honey

FirmaLife導入時に限り、レシピID `tfie:fermenter/raw_honey` を有効にする。入力は `firmalife:food/raw_honey` 1個、出力はEthanol 250 mB、消費FEは6,400、時間は80 ticksとする。FirmaLifeが未導入の場合は、このレシピを読み込まない。

別途採用するWild HoneycombのSqueezer加工は、Wild Honeycomb 1個からRaw Honey 1個を6,400 FE・80 ticksで生成する。両加工を連続して行う場合は、Wild Honeycomb 1個からEthanol 250 mBを得て、合計12,800 FEを消費する。Raw HoneyのFermenter加工は1件だけ登録し、Wild Honeycomb対応によって同じレシピを重複追加しない。[Raw Honeyの既存加工](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/fermenter/raw_honey.json)

### 22. 栽培・圧搾・発酵の互換性確認

Clocheの29対象、主出力数量、種子副産物25%、成長目標128,000、8種類の土壌、二段作物の描画条件、およびFermenterの対象と出力・費用は、TFC-IE Crossover 2.1.3と旧TFIE 0.0.3の定義を継承する。圧搾の機械種別だけは前述のとおりSqueezerへ是正する。FirmaLife Raw Honeyの加工は導入時限定とし、通常のTFC食品・作物にはFirmaLifeを要求しない。[CrossoverのCloche生成定義](https://github.com/NMagpie/TFC-IE-Crossover/blob/41eb5d40bdd9e97152cd400decce4c04f746b902/src/data/java/com/nmagpie/tfc_ie_addon/data/recipes/ClocheRecipes.java)、[CrossoverのFermenter生成定義](https://github.com/NMagpie/TFC-IE-Crossover/blob/41eb5d40bdd9e97152cd400decce4c04f746b902/src/data/java/com/nmagpie/tfc_ie_addon/data/recipes/FermenterRecipes.java)

### 23. 非金属加工と薬液

#### 共通規則

Crusherの鉱石加工はTFCMだけが管理する。TFIEはTFCMの工程を迂回するIE標準レシピの削除だけを行い、独自の鉱石処理、数量変更、重複定義、不足系列の補完を追加しない。原鉱石から直接Powderを得る旧Quernレシピの無効化もTFCMの管理範囲とし、TFIEで重複定義しない。以下のCrusher追加表は非金属加工を対象とする。

本章の数量とFEはレシピ1回分の基礎値とする。`#`を付したIDはタグ、それ以外は個別のItem ID、Fluid IDまたはBlock IDを示す。入力数量を別に記載していない固体材料は各1個を消費する。採用するレシピごとに、以下の表へRecipe ID、入出力、数量、処理値、条件を示す。

IEの設定倍率を変更した構成では、機械が計算する費用・進行速度を用いる。基礎処理長はIE内部の進行量であり、機械が1回のサーバーtickで複数段階を処理する場合、同じ数値の実経過tick数を意味しない。Crusherの基礎処理長は50、追加進行の上限は4であり、通常の1段階と合わせて1サーバーtick当たり最大5段階となる。Mixerの基礎処理長は出力流体量のmB数と同じ値であり、追加進行の上限は7、合計の上限は8段階となる。実際の追加進行は、直近の電力搬入量・搬出量、蓄電量、残りの処理長に制約される。Squeezerは基礎80ticks、Refineryは基礎1tickとし、両機械の追加進行は0とする。

温度欄を「要求なし」とするIE加工は、TFCの加熱機構による入力温度を要求せず、出力へ独自の加熱温度を設定しない。Coke Oven、TFC Chisel、TFC Heating、TFC BarrelはFEを消費しない。TFC Heatingの到達温度と加工所要時間を同一視せず、固定処理時間を追加しない。

時間と進行の根拠は[CrusherRecipe](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/CrusherRecipe.java)、[MixerRecipe](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/MixerRecipe.java)、[MultiblockProcess](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/process/MultiblockProcess.java)、[MixingProcess](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/logic/mixer/MixingProcess.java)、[SqueezerRecipe](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/SqueezerRecipe.java)、[RefineryRecipe](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/RefineryRecipe.java)である。

#### Coke Oven

Bituminous CoalとLigniteの2レシピを採用する。出力の指定は`#c:coal_coke`を維持し、基準となるIEの登録アイテムは`immersiveengineering:coal_coke`とする。出力流体は`immersiveengineering:creosote`とする。

| Recipe ID | 入力 | 原料1個当たりの固体出力 | 原料1個当たりの流体出力 | 一括上限 | 1回の時間 | FE | 温度条件 |
|---|---|---|---|---:|---:|---:|---|
| `tfie:cokeoven/bituminous_coal` | `tfc:ore/bituminous_coal` | `#c:coal_coke` 1個 | `immersiveengineering:creosote` 500mB | 16個 | 6,000ticks | 0 | 要求なし |
| `tfie:cokeoven/lignite` | `tfc:ore/lignite` | `#c:coal_coke` 1個 | `immersiveengineering:creosote` 500mB | 16個 | 6,000ticks | 0 | 要求なし |

任意Modの導入条件や腐敗条件を設けない。入力数16は一括処理数の上限であり、1個のCokeを得るために原料16個を消費する比率ではない。原料が1個だけでも処理し、その場合も1回の時間は6,000ticksとする。原料16個と十分な出力容量がある場合、1回でCoke16個とCreosote8,000mBを生成する。

1回の処理数量を次式で定める。

```text
n = min(16, N_input, S_solid, floor(S_fluid / 500))
```

`N_input`は同一レシピに一致する投入原料の個数、`S_solid`は出力先に追加するCokeの実残容量、`S_fluid`はCreosoteタンクの実残容量をmBで表した値とする。空の固体出力欄では出力アイテムとスロットのスタック上限を用いる。出力欄に同じアイテムと同じComponentsのCokeがある場合は、上限から既存個数を引く。異なるアイテムまたは異なるComponentsがある場合は`S_solid=0`とする。流体を受け入れないタンク状態では`S_fluid=0`とする。`n=0`なら新規加工を開始しない。

完了時には`n`個の原料を消費してCokeを`n`個、Creosoteを`500n`mB追加する。処理中の搬入出があっても、出力時の実残容量を超えて加算しない。IEの基準版に存在する「非空の出力欄で、既存個数を残容量として使用する」計算を修正する。例えば出力欄にCoke60個があり、スタック上限64、原料と液体容量が十分なら、加工数量は4個となる。Coke1個がある場合に加工上限を1個へ制限しない。

入出力と一括上限は[Bituminous Coal](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/cokeoven/bituminous_coal.json)と[Lignite](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/cokeoven/lignite.json)、照合時の数量の扱いは[CokeOvenRecipe](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/CokeOvenRecipe.java)、既存の容量計算は[CokeOvenLogic](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/logic/CokeOvenLogic.java)、Cokeの基準IDは[coal_cokeタグ](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/c/tags/item/coal_coke.json)を根拠とする。

#### Crusher

以下の24件を全対象とする。全レシピの固体入力は1個、基礎処理長は50、温度条件は要求なしとする。任意Modの導入条件と腐敗条件を設けない。Oliveへ独自の非腐敗判定を追加しない。

| Recipe ID | 入力 | 主出力 | 副産物 | 基礎FE | 基礎処理長 |
| --- | --- | --- | --- | --- | --- |
| `tfie:crusher/charcoal` | `minecraft:charcoal` 1個 | `tfc:powder/charcoal` 4個 | なし | 1,600 | 50 |
| `tfie:crusher/fluxstone` | `#tfc:fluxstone` 1個 | `tfc:powder/flux` 2個 | なし | 1,600 | 50 |
| `tfie:crusher/olive` | `tfc:food/olive` 1個 | `tfc:olive_paste` 2個 | なし | 800 | 50 |
| `tfie:crusher/sandstone/cutblack` | `tfc:cut_sandstone/black` 1個 | `tfc:sand/black` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/cutbrown` | `tfc:cut_sandstone/brown` 1個 | `tfc:sand/brown` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/cutgreen` | `tfc:cut_sandstone/green` 1個 | `tfc:sand/green` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/cutpink` | `tfc:cut_sandstone/pink` 1個 | `tfc:sand/pink` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/cutred` | `tfc:cut_sandstone/red` 1個 | `tfc:sand/red` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/cutwhite` | `tfc:cut_sandstone/white` 1個 | `tfc:sand/white` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/cutyellow` | `tfc:cut_sandstone/yellow` 1個 | `tfc:sand/yellow` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/rawblack` | `tfc:raw_sandstone/black` 1個 | `tfc:sand/black` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/rawbrown` | `tfc:raw_sandstone/brown` 1個 | `tfc:sand/brown` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/rawgreen` | `tfc:raw_sandstone/green` 1個 | `tfc:sand/green` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/rawpink` | `tfc:raw_sandstone/pink` 1個 | `tfc:sand/pink` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/rawred` | `tfc:raw_sandstone/red` 1個 | `tfc:sand/red` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/rawwhite` | `tfc:raw_sandstone/white` 1個 | `tfc:sand/white` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/rawyellow` | `tfc:raw_sandstone/yellow` 1個 | `tfc:sand/yellow` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/smoothblack` | `tfc:smooth_sandstone/black` 1個 | `tfc:sand/black` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/smoothbrown` | `tfc:smooth_sandstone/brown` 1個 | `tfc:sand/brown` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/smoothgreen` | `tfc:smooth_sandstone/green` 1個 | `tfc:sand/green` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/smoothpink` | `tfc:smooth_sandstone/pink` 1個 | `tfc:sand/pink` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/smoothred` | `tfc:smooth_sandstone/red` 1個 | `tfc:sand/red` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/smoothwhite` | `tfc:smooth_sandstone/white` 1個 | `tfc:sand/white` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |
| `tfie:crusher/sandstone/smoothyellow` | `tfc:smooth_sandstone/yellow` 1個 | `tfc:sand/yellow` 2個 | `tfc:powder/saltpeter` 1個を50%で生成する。 | 3,200 | 50 |

砂岩の副産物判定は各加工1回につき1回とする。Raw・Cut・Smoothの3形状と、Black・Brown・Green・Pink・Red・White・Yellowの7色を全て含む。砂岩以外の3件に副産物を設けない。主出力は常に表の個数を生成する。

根拠は[Charcoal](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/crusher/charcoal.json)、[Fluxstone](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/crusher/fluxstone.json)、[Olive](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/crusher/olive.json)、[砂岩21件のJSON](https://github.com/lau-mods/TFIE/tree/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/crusher/sandstone)である。

鉱石のCrusher処理はTFCMが提供する工程だけを使用する。TFIEから鉱石加工の追加、数量変更、重複レシピ、不足系列への補完を行わず、TFCMの工程を迂回するIE標準レシピを無効化する。Wrought Iron Ingotから`tfcm:metal/dust/wrought_iron`へのCrusher加工も追加しない。

羊毛から糸への`immersiveengineering:crusher/wool`を無効化する。対象は`#minecraft:wool` 1個から`minecraft:string` 4個を3,200FEで生成する単一レシピであり、色別レシピではない。代替レシピを追加せず、データ再読込後も無効状態を維持する。

鉱石加工の管理範囲は[TFCM MachineProcessingDataProvider](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/datagen/MachineProcessingDataProvider.java)、羊毛の削除対象は[IE wool](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crusher/wool.json)で確認する。

#### TFC Chisel

以下の8件を採用する。入力は設置したブロック1個であり、加工後のブロック状態を`result`で指定する。Slabでは設置ブロックをSlab1個へ変更し、同じSlabのアイテムをさらに1個渡すため、合計2個となる。Stairでは設置ブロックを階段1個へ変更し、追加アイテムを渡さない。

| Recipe ID | 設置入力 | Chisel mode | 設置出力 | 追加のItem出力 |
| --- | --- | --- | --- | --- |
| `tfie:chisel/slab_concrete` | `immersiveengineering:concrete` 1個 | `tfc:slab` | `immersiveengineering:slab_concrete` 1個 | `immersiveengineering:slab_concrete` 1個 |
| `tfie:chisel/slab_concrete_brick` | `immersiveengineering:concrete_brick` 1個 | `tfc:slab` | `immersiveengineering:slab_concrete_brick` 1個 | `immersiveengineering:slab_concrete_brick` 1個 |
| `tfie:chisel/slab_concrete_leaded` | `immersiveengineering:concrete_leaded` 1個 | `tfc:slab` | `immersiveengineering:slab_concrete_leaded` 1個 | `immersiveengineering:slab_concrete_leaded` 1個 |
| `tfie:chisel/slab_concrete_tile` | `immersiveengineering:concrete_tile` 1個 | `tfc:slab` | `immersiveengineering:slab_concrete_tile` 1個 | `immersiveengineering:slab_concrete_tile` 1個 |
| `tfie:chisel/stairs_concrete` | `immersiveengineering:concrete` 1個 | `tfc:stair` | `immersiveengineering:stairs_concrete` 1個 | なし |
| `tfie:chisel/stairs_concrete_brick` | `immersiveengineering:concrete_brick` 1個 | `tfc:stair` | `immersiveengineering:stairs_concrete_brick` 1個 | なし |
| `tfie:chisel/stairs_concrete_leaded` | `immersiveengineering:concrete_leaded` 1個 | `tfc:stair` | `immersiveengineering:stairs_concrete_leaded` 1個 | なし |
| `tfie:chisel/stairs_concrete_tile` | `immersiveengineering:concrete_tile` 1個 | `tfc:stair` | `immersiveengineering:stairs_concrete_tile` 1個 | なし |

全8件のFEは0とし、固定時間、温度、確率副産物、任意Modの導入条件を追加しない。TFC Chiselの通常の道具判定、操作、耐久消費、設置向きの処理を用いる。根拠は[Chisel全8件](https://github.com/lau-mods/TFIE/tree/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/chisel)である。

#### TFC Heating

以下の2件を採用する。FE、固定処理時間、任意Modの導入条件、確率副産物を設けない。指定温度へ到達した入力1個をTFCの通常加熱処理で変換する。流体出力は設けない。

| Recipe ID | 入力 | 固体出力 | 要求温度 |
| --- | --- | --- | --- |
| `tfie:heating/clinker_brick` | `minecraft:bricks` 1個 | `immersiveengineering:clinker_brick` 1個 | 1,499℃ |
| `tfie:heating/slag_glass` | `immersiveengineering:slag` 1個 | `immersiveengineering:slag_glass` 1個 | 380℃ |

Clinker Brickの入力はレンガのアイテム`minecraft:brick`ではなく、レンガブロック`minecraft:bricks`とする。根拠は[Clinker Brick](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/heating/clinker_brick.json)と[Slag Glass](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/heating/slag_glass.json)である。

#### 薬液と基礎流体

以下の6件を採用する。Item入力の選択肢は記載したいずれか1個を意味し、選択肢を同時に全て消費しない。全レシピで固体副産物と温度条件を設けない。腐敗条件も追加しない。

| Recipe ID | 設備とRecipe type | Item入力 | Fluid入力 | Fluid出力 | 基礎FE | 基礎処理長または時間 | 導入条件 |
|---|---|---|---|---|---:|---|---|
| `tfie:barrel/redstone_acid` | TFC Barrel、`tfc:barrel_instant` | `#c:dusts/redstone` 1個 | `minecraft:water` 250mB | `immersiveengineering:redstone_acid` 250mB | 0 | 即時反応とし、密封待機を要求しない。 | なし |
| `tfie:mixer/herbicide` | IE Mixer、`immersiveengineering:mixer` | `#c:dusts/sulfur` 1個と、`tfc:powder/native_copper`、`tfc:powder/malachite`、`tfc:powder/tetrahedrite`のいずれか1個 | `#c:ethanol` 500mB | `immersiveengineering:herbicide` 500mB | 3,200 | 500 | なし |
| `tfie:mixer/limewater` | IE Mixer、`immersiveengineering:mixer` | `tfc:powder/flux`または`tfc:powder/lime` 1個 | `#tfc:fresh_water` 500mB | `tfc:limewater` 500mB | 800 | 500 | なし |
| `tfie:mixer/salt_water` | IE Mixer、`immersiveengineering:mixer` | `tfc:powder/salt` 1個 | `#tfc:fresh_water` 125mB | `tfc:salt_water` 125mB | 800 | 125 | なし |
| `tfie:refinery/brine` | IE Refinery、`immersiveengineering:refinery` | なし | `tfc:vinegar` 1mBと`tfc:salt_water` 9mB | `tfc:brine` 10mB | 50 | 1tick | なし |
| `tfie:refinery/milk_vinegar` | IE Refinery、`immersiveengineering:refinery` | なし | `tfc:vinegar` 1mBと`#c:milk` 9mB | `tfc:milk_vinegar` 10mB | 50 | 1tick | FirmaLife未導入時だけ有効とする。 |

Herbicideの銅原料は洗浄後の鉱石Powderであり、各1個は金属5mB相当となる。100mBの`tfcm:metal/dust/copper`へ置き換えない。原鉱石から直接Powderを得る旧Quern経路の無効化と、TFCMでの洗浄工程はTFCMが管理する。

Milk Vinegarの不在条件は`neoforge:not`の内側へ`neoforge:mod_loaded`、`modid=firmalife`を指定する。FirmaLife導入時は後述の`#firmalife:milks`を使う別レシピへ切り替え、両方を同時に有効化しない。

`immersiveengineering:crafting/gunpowder_from_dusts`を無効化する。TFIEはその代替レシピを追加しない。

流体レシピの根拠は[Redstone Acid](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/barrel/redstone_acid.json)、[Herbicide](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/mixer/herbicide.json)、[Limewater](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/mixer/limewater.json)、[Salt Water](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/mixer/salt_water.json)、[Brine](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/refinery/brine.json)、[Milk Vinegar](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/refinery/milk_vinegar.json)である。鉱石PowderのIDと量は[TFCM ProcessingSeries](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/common/processing/ProcessingSeries.java)と[ProcessingDataProvider](https://github.com/lau-mods/TFC-metallum-overhaul/blob/v2.2.1/src/main/java/net/claustra01/tfcm/datagen/ProcessingDataProvider.java)に従う。

#### FirmaLifeの追加加工

以下の6件は全てFirmaLife導入時だけ有効とする。ロード条件は`neoforge:mod_loaded`、`modid=firmalife`とする。元のFirmaLifeのVat、Centrifuge、Barrelレシピは維持する。TFIEのIE機械経路では温度条件を要求せず、加熱用燃料を消費しない。

| Recipe ID | 設備とRecipe type | Item入力 | Fluid入力 | Item出力 | Fluid出力 | 基礎FE | 基礎処理長または時間 |
|---|---|---|---|---|---|---:|---|
| `tfie:mixer/firmalife/olive_oil_water` | IE Mixer、`immersiveengineering:mixer` | `tfc:olive_paste` 1個 | `minecraft:water` 200mB | なし | `tfc:olive_oil_water` 200mB | 800 | 200 |
| `tfie:mixer/firmalife/tallow` | IE Mixer、`immersiveengineering:mixer` | `tfc:blubber` 1個 | `minecraft:water` 200mB | なし | `tfc:tallow` 200mB | 800 | 200 |
| `tfie:mixer/firmalife/lye` | IE Mixer、`immersiveengineering:mixer` | `tfc:powder/wood_ash` 1個 | `minecraft:water` 200mB | なし | `tfc:lye` 200mB | 800 | 200 |
| `tfie:mixer/firmalife/caramel` | IE Mixer、`immersiveengineering:mixer` | 腐敗していない`firmalife:food/butter` 1個 | `firmalife:sugar_water` 1,000mB | なし | `firmalife:caramel` 1,000mB | 3,200 | 1,000 |
| `tfie:squeezer/firmalife/wild_honeycomb` | IE Squeezer、`immersiveengineering:squeezer` | `firmalife:wild_honeycomb` 1個 | なし | `firmalife:food/raw_honey` 1個 | なし | 6,400 | 80ticks |
| `tfie:refinery/firmalife/milk_vinegar` | IE Refinery、`immersiveengineering:refinery` | なし | `tfc:vinegar` 1mBと`#firmalife:milks` 9mB | なし | `tfc:milk_vinegar` 10mB | 50 | 1tick |

CaramelのButterだけに非腐敗条件を付ける。Ingredientは`firmalife:food/butter`との一致と`tfc:not_rotten`相当の判定を両方満たすものとし、腐敗済みButterを受け入れない。他の5件へ同じ条件を追加しない。原料と出力の個数・流体量を上表から変更しない。

Olive Oil Water、Tallow、Lye、Caramelの元のFirmaLife Vatは300℃・600ticksを要求する。新しいMixer経路はこの加熱工程を上表の電力処理へ置き換えるものであり、元の温度・時間をMixerへ併設しない。Mixerの200、1,000という値は実経過時間ではなく基礎処理長とする。

Wild HoneycombのSqueezer経路は固体のRaw Honeyだけを生成する。蜂枠を入力に追加せず、Honey等の流体や追加副産物も生成しない。Raw HoneyからEthanol250mBを得る既存のFermenter経路は別レシピとして維持する。

`#firmalife:milks`の基準構成要素は`minecraft:milk`、`firmalife:goat_milk`、`firmalife:yak_milk`とする。Milk Vinegarの生成量は入力流体の合計と同じ10mBとし、触媒アイテムを要求しない。

入出力の根拠は[FirmaLife Olive Oil Water](https://github.com/eerussianguy/firmalife/blob/v3.0.16/src/generated/resources/data/firmalife/recipe/vat/olive_oil_water.json)、[Tallow](https://github.com/eerussianguy/firmalife/blob/v3.0.16/src/generated/resources/data/firmalife/recipe/vat/tallow.json)、[Lye](https://github.com/eerussianguy/firmalife/blob/v3.0.16/src/generated/resources/data/firmalife/recipe/vat/lye.json)、[Caramel](https://github.com/eerussianguy/firmalife/blob/v3.0.16/src/generated/resources/data/firmalife/recipe/vat/caramel.json)、[Wild Honeycomb](https://github.com/eerussianguy/firmalife/blob/v3.0.16/src/generated/resources/data/firmalife/recipe/centrifuge/wild_honeycomb.json)、[Milk Vinegar](https://github.com/eerussianguy/firmalife/blob/v3.0.16/src/generated/resources/data/firmalife/recipe/barrel_instant_fluid/milk_vinegar.json)、[milksタグ](https://github.com/eerussianguy/firmalife/blob/v3.0.16/src/generated/resources/data/firmalife/tags/fluid/milks.json)である。IE向けRecipe IDとFEは本仕様の採用値とする。

#### ArborFirmaCraftのLatexとRubber

TFIEはArborFirmaCraftを依存関係へ追加しない。ArborFirmaCraft導入時の既存のLatex・Rubber製法を維持し、LatexからRubberへのIE機械レシピを追加しない。Bottling Machine、Squeezer、Mixerその他のIE設備を用いた代替製法も追加しない。

TFIEはRubberを使うConveyor、LV絶縁Wire Coil、MV絶縁Wire Coilのレシピを追加しない。`afc:rubber_bar`を共通Leather、Fabric、Plasticタグへ追加せず、`afc:latex`をIEのPhenolic Resinと同じ流体として扱わない。IE自身のAcetaldehydeとCreosoteによるResin製造は維持する。

ArborFirmaCraft 2.1.3に存在する次の5経路を上書きせず、TFIEから同じレシピを重複追加しない。

| 所有ModのRecipe ID | 設備と形式 | 入力 | 出力 | 固有の条件 |
|---|---|---|---|---|
| `afc:pot/rubber` | TFC Pot、`tfc:pot` | `afc:latex` 1,000mBと`#c:dusts/sulfur` 1個 | `afc:rubber_bar` 1個 | 600℃・2,000ticks |
| `afc:pot/rubber_temp` | TFC Pot、`tfc:pot` | `afc:latex` 1,000mBと`tfc:powder/sulfur` 1個 | `afc:rubber_bar` 1個 | 600℃・1,000ticks |
| `afc:vat/rubber` | FirmaLife Vat、`firmalife:vat` | `afc:latex` 1,000mBと`tfc:powder/sulfur` 1個 | `afc:rubber_bar` 1個 | FirmaLife導入時の互換データを使用し、300℃・600ticksを要求する。 |
| `afc:crafting/latex_bucket` | 不定形クラフト、`minecraft:crafting_shapeless` | `afc:latex` 1,000mBを含む容器1個 | `tfc:glue` 4個 | AFCの`tfc:fluid_content`判定を使用する。 |
| `afc:crafting/sealed_bricks_rubber` | 定形クラフト、`minecraft:crafting_shaped` | `#minecraft:stone_bricks` 8個と`afc:rubber_bar` 1個 | `firmalife:sealed_bricks` 8個 | FirmaLife導入時の互換データを使用する。3×3中央へRubber、周囲8枠へStone Bricksを置く。 |

この5件は全てFEを消費しない。クラフト2件には固定時間や温度を追加しない。採液用Tap、幹、採液容器、温度判定、Latexの取得量はAFCの処理を維持する。上表をTFIE独自レシピの追加指示として扱わない。

AFCの採液は自然状態の原木を要求する。Heveaは14〜50℃で1回2mB、Rubber Figは6〜50℃で1回1mBのLatexを生成し、どちらも春限定とはしない。これらは採液レシピの値を用い、ガイドの旧記述である22℃・12℃へ置き換えない。TFIEは採液量、温度範囲、Tapと幹・容器の構成を変更しない。[Hevea](https://github.com/Therighthon/ArborFirmaCraft/blob/d8d7ca315e6f3fda1ee9a740541c56db92b69cb3/src/main/resources/data/afc/recipe/tree_tapping/hevea_latex.json)、[Rubber Fig](https://github.com/Therighthon/ArborFirmaCraft/blob/d8d7ca315e6f3fda1ee9a740541c56db92b69cb3/src/main/resources/data/afc/recipe/tree_tapping/rubber_fig_latex.json)、[Tapの処理](https://github.com/Therighthon/ArborFirmaCraft/blob/d8d7ca315e6f3fda1ee9a740541c56db92b69cb3/src/main/java/com/therighthon/afc/common/blockentities/TapBlockEntity.java)

根拠は[AFC Pot](https://github.com/Therighthon/ArborFirmaCraft/blob/d8d7ca315e6f3fda1ee9a740541c56db92b69cb3/src/main/resources/data/afc/recipe/pot/rubber.json)、[TFC Powder版Pot](https://github.com/Therighthon/ArborFirmaCraft/blob/d8d7ca315e6f3fda1ee9a740541c56db92b69cb3/src/main/resources/data/afc/recipe/pot/rubber_temp.json)、[FirmaLife Vat](https://github.com/Therighthon/ArborFirmaCraft/blob/d8d7ca315e6f3fda1ee9a740541c56db92b69cb3/src/main/resources/firmalife_compat_data/data/afc/recipe/vat/rubber.json)、[Glue](https://github.com/Therighthon/ArborFirmaCraft/blob/d8d7ca315e6f3fda1ee9a740541c56db92b69cb3/src/main/resources/data/afc/recipe/crafting/latex_bucket.json)、[Sealed Bricks](https://github.com/Therighthon/ArborFirmaCraft/blob/d8d7ca315e6f3fda1ee9a740541c56db92b69cb3/src/main/resources/firmalife_compat_data/data/afc/recipe/crafting/sealed_bricks_rubber.json)である。

### 24. External HeaterとCrucible

IEの `immersiveengineering:furnace_heater` をTFCの `tfc:crucible` に隣接させると、電力を用いてCrucibleを加熱する。独立したElectric Crucibleは追加しない。CrucibleにIEのExternal Heater Capabilityを登録し、隣接6方向を対象とする。TFIE側ではCrucibleの面による加熱可否を制限しない。

| 項目 | 仕様 |
|---|---|
| 標準消費電力 | 加熱対象のCrucible 1基につき20 FE/tとする。 |
| 標準目標温度 | 2,000℃とする。 |
| 加熱条件 | そのtickに必要な電力があり、HeaterにRedstone入力がないことを要求する。 |
| 加熱成立時 | Crucibleの目標温度を設定温度へ更新し、目標温度の安定保持時間を5 ticksへ更新し、状態を同期する。 |
| 消費の継続 | 目標温度到達後も、加熱成立中は毎tickの電力を消費する。Crucible内部の材料の有無を追加条件にしない。 |
| 加熱不成立時 | 加熱コール内で電力不足またはRedstone入力を検出した場合、目標温度を0へ設定し、そのコールによる電力消費を0とする。 |
| 実温度 | 目標温度に向けてTFCの通常の温度変化を適用する。設定操作で実温度を瞬時に2,000℃へ置き換えない。 |
| 更新停止時 | Heaterの停止・撤去等で目標更新が止まった場合、Crucible自身の安定時間と通常の目標温度減衰・冷却を適用する。 |

IE側のHeaterは32,000 FEを保持し、起動前は蓄電量が3,200 FEを超えると隣接対象への加熱処理を呼び出す。既に動作している場合も加熱処理を呼び出す。したがって、停止中のHeaterへ20 FEだけを供給した状態で直ちに始動する仕様ではない。電力入力面と蓄電処理はIEのHeaterに従う。標準電力入力上限は256 FE/tであり、IEのHeater関連消費設定がそれを上回る場合は大きい値を用いる。

複数のCrucibleを同じHeaterへ隣接させた場合は、各Crucibleへの呼出しごとに電力を消費する。温度を複数台分加算する機能は設けない。

根拠は[TFIEのCrucibleHeater](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/CrucibleHeater.java)、[IEのFurnaceHeaterBlockEntity](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/metal/FurnaceHeaterBlockEntity.java)、[TFCのCrucible温度処理](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/blockentities/CrucibleBlockEntity.java)である。

### 25. 回転動力による発電

IEの `immersiveengineering:dynamo` をTFC回転ネットワークのSinkとして扱う。Dynamoの向きと同じ面を回転入力の接続面とし、TFCの風車・水車から伝達された回転を受ける。読込時にネットワークへ追加し、チャンクのアンロードとブロック撤去時に除去する。

TFC回転速度を `ω`、TFIE設定の倍率を `m` とすると、毎サーバーtickにIEへ渡す入力は次式とする。TFCの速度の単位はrad/tickである。

```text
IEへ渡す回転入力 = abs(ω × 500 × m)
標準のm = 1.0
```

正転と逆転は絶対値によって同じ発電量とする。回転ネットワークから有効な回転を取得していないtickには、この連携による入力を行わない。

IE側が隣接機器へ供給する電力の上限は、IEの `dynamo_output` 設定を `d` とすると次式になる。非負値を整数へ切り捨てる。

```text
供給候補FE/t = int((d / 3) × abs(ω × 500 × m))
IE標準設定のd = 3
```

Dynamoはこの電力を隣接する受電先へ配分する。受電先の受入量を超えた分をDynamo内部へ蓄積する機能は持たない。TFIEの倍率はIE設定を置換せず、その前段の回転入力へ作用する。例えば標準設定で `ω=0.04 rad/tick` のときは20 FE/tが供給候補となる。

根拠は[TFIEの回転接続](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/mixin/DynamoBlockEntityMixin.java)、[TFC回転速度の単位](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/util/rotation/Rotation.java)、[IE側の電力変換と配分](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/metal/DynamoBlockEntity.java)、[IE標準倍率](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/config/IEServerConfig.java)である。

### 26. Crateの収納制限

IE Wooden CrateとReinforced CrateへTFCのアイテムサイズ制限を適用する。対象アイテムは `ItemSizeManager` が返すTFCサイズで判定し、標準の最大許容値を `VERY_LARGE` とする。

サイズ順序は `TINY < VERY_SMALL < SMALL < NORMAL < LARGE < VERY_LARGE < HUGE` とする。標準設定ではVERY_LARGE以下をサイズ条件上許可し、HUGEを拒否する。重量を新しい制限条件として追加しない。

サイズ条件を満たす場合も、IE本体の「コンテナ内収納を許可しないアイテム」と `#immersiveengineering:forbidden_in_crates` による拒否を維持する。TFIEのサイズ条件によってIE側の拒否を解除しない。通常操作と機械搬入で、同じ収納可否を使用する。

根拠は[サイズ制限](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/mixin/AllowInCrateMixin.java)、[TFCサイズ順序](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/component/size/Size.java)、[IEの追加収納条件](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/IEApi.java)である。

### 27. 流体と容器

#### TFC容器で扱うIE流体

次の10流体を、TFCの `tfc:usable_in_barrel`、`tfc:usable_in_wooden_bucket`、`tfc:usable_in_red_steel_bucket` の各Fluidタグへ追加する。各容器の既存容量と通常の流体入出力を維持する。

| 流体 | 流体ID |
|---|---|
| Creosote | `immersiveengineering:creosote` |
| Plant Oil | `immersiveengineering:plantoil` |
| Ethanol | `immersiveengineering:ethanol` |
| Biodiesel | `immersiveengineering:biodiesel` |
| High Power Biodiesel | `immersiveengineering:high_power_biodiesel` |
| Concrete | `immersiveengineering:concrete` |
| Herbicide | `immersiveengineering:herbicide` |
| Redstone Acid | `immersiveengineering:redstone_acid` |
| Acetaldehyde | `immersiveengineering:acetaldehyde` |
| Phenolic Resin | `immersiveengineering:phenolic_resin` |

根拠は[Barrel用Fluidタグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfc/tags/fluid/usable_in_barrel.json)、[Wooden Bucket用Fluidタグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfc/tags/fluid/usable_in_wooden_bucket.json)、[Red Steel Bucket用Fluidタグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfc/tags/fluid/usable_in_red_steel_bucket.json)である。

#### IEレシピの流体材料

IEの流体材料 `IngredientFluidStack` に表示する候補は、`#tfc:fluid_item_ingredient_empty_containers` の空容器から生成する。基準となるタグにはGlass Bottle系、Minecraft Bucket、TFC Wooden Bucket、Blue Steel Bucket、Red Steel Bucket、Ceramic Jugが含まれる。ただし、各候補の流体Capabilityが対象流体を実際に受け入れ、レシピ必要量以上を保持する場合だけ候補へ含める。タグに含まれるだけで、すべての容器に全流体を許可する仕様ではない。

材料の成立判定は容器のアイテム名ではなく、保持する流体の種類と必要量によって行う。クラフト時は1容器分のコピーから必要量を排出し、その残量を含む容器を返す。TFIEはこのIEの流体材料判定・排出規則を維持し、表示候補をTFC容器へ対応させる。

この対応は、材料を固定アイテム `minecraft:bucket` として指定した一般クラフトへ適用しない。したがって、ChemthrowerやJerrycanの固定Bucket材料をTFC Bucketへ置き換える機能にはならない。

根拠は[TFIEの候補列挙](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/mixin/IngredientFluidStackMixin.java)、[TFC空容器タグ](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/generated/resources/data/tfc/tags/item/fluid_item_ingredient_empty_containers.json)、[IEの流体判定と容器返却](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/crafting/fluidaware/IngredientFluidStack.java)である。

### 28. 除草剤の作用

`#c:herbicide` の流体にTFIEのChemthrower効果を登録する。基準となる流体はIEのHerbicideである。対象は命中したブロックと、その直上の植物とする。広い範囲の全作物を一括破壊する効果は追加しない。

| 命中対象 | 地表の変更 | 直上の植物の扱い |
|---|---|---|
| TFCのGrassまたはFarmland | 同じ土壌VariantのDirtへ変更する。 | TFC栽培作物は成長度1以上かを用いて枯死状態へ変え、その後にドロップを伴って破壊する。枯死作物と野生作物もドロップを伴って破壊する。通常のBushBlockは、SeasonalPlantBlockを除き、ドロップなしで除去する。 |
| TFCのClay Grass | 同じ土壌VariantのClayへ変更する。 | 栽培作物を枯死へ変換する専用分岐は使用しない。枯死・野生作物はドロップを伴って破壊し、SeasonalPlantBlock以外の通常BushBlockはドロップなしで除去する。 |
| `#minecraft:leaves` | 命中した葉をドロップなしで除去する。 | 別の直上植物処理は追加しない。 |
| その他のSnowyDirtBlockまたはFarmBlock | `minecraft:dirt` へ変更する。 | 直上がBushBlockならドロップなしで除去する。 |
| 上記に該当しないブロック | 独自の地表変更を行わない。 | 独自の植物破壊を行わない。 |

土壌への命中に伴う処理を定義し、作物本体への直接命中だけで同じ地表処理を起動する条件は追加しない。LivingEntityに対しては、TFIEが登録する除草剤効果から追加の効果を与えない。

ブロックへの効果を適用した後、命中ブロックのAABBを各方向へ0.25ブロック拡張した範囲の `ChemthrowerShotEntity` を除去する。同じ噴射群の残存弾による重複作用を抑える。根拠は[HerbicideEffects](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/common/HerbicideEffects.java)である。

### 29. 肥料の互換

#### IE素材をTFC農地へ使用する場合

以下のTFC Fertilizerデータを登録する。数値は通常土壌で1個を使用した際の栄養基準値とし、1.0を栄養ゲージの100%とする。TFCの土壌補正、ゲージ上限、通常・Shift使用の数量処理を維持する。

| FertilizerデータID | 材料 | 窒素N | リンP | カリウムK |
|---|---|---:|---:|---:|
| `tfie:dust_saltpeter` | `immersiveengineering:dust_saltpeter` | 0.2 | 0 | 0 |
| `tfie:fertilizer` | `immersiveengineering:fertilizer` | 0.1 | 0.2 | 0.5 |
| `tfie:slag` | `immersiveengineering:slag` | 0 | 0.2 | 0.1 |

土壌補正倍率は非常に肥沃な農地で1.2、肥沃な農地で1.1、通常で1.0、貧栄養の農地で0.9、非常に貧栄養の農地で0.8とする。通常使用は1個を消費する。Shift使用は、補充対象の栄養ゲージのいずれかを満たすまでの必要数と所持数に従う。補充する全栄養が既に満量であれば、肥料を消費しない。Creativeでは消費しない。

根拠は[Saltpeter](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tfc/fertilizer/dust_saltpeter.json)、[Fertilizer](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tfc/fertilizer/fertilizer.json)、[Slag](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tfc/fertilizer/slag.json)、[TFCの施肥と土壌倍率](https://github.com/TerraFirmaCraft/TerraFirmaCraft/blob/v4.2.16/src/main/java/net/dries007/tfc/common/blocks/crop/CropHelpers.java)である。

#### TFC素材をGarden Clocheへ使用する場合

以下の `immersiveengineering:fertilizer` レシピを登録する。倍率はGarden Clocheの成長倍率であり、TFC農地へ与えるN/P/Kの値ではない。

| レシピID | 材料ID | 成長倍率 |
|---|---|---:|
| `tfie:fertilizer/compost` | `tfc:compost` | 1.2 |
| `tfie:fertilizer/guano` | `tfc:groundcover/guano` | 1.3 |
| `tfie:fertilizer/pure_nitrogen` | `tfc:pure_nitrogen` | 1.2 |
| `tfie:fertilizer/pure_phosphorus` | `tfc:pure_phosphorus` | 1.2 |
| `tfie:fertilizer/pure_potassium` | `tfc:pure_potassium` | 1.2 |
| `tfie:fertilizer/shellfish` | `tfc:food/shellfish` | 1.1 |
| `tfie:fertilizer/sylvite` | `tfc:powder/sylvite` | 1.2 |
| `tfie:fertilizer/wood_ash` | `tfc:powder/wood_ash` | 1.1 |

各登録は材料のItem IDを条件とする。Shellfishの肥料レシピへ腐敗除外条件を追加しない。Garden Clocheの水・電力・施肥周期と肥料消費はClocheの共通動作に従う。根拠は[全8肥料レシピ](https://github.com/lau-mods/TFIE/tree/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/fertilizer)である。

### 30. 共通素材タグとTFC熱データ

#### 素材タグ

以下のタグへ値を追加し、上流の既存内容を置換しない。

| Itemタグ | 追加するItem ID | 作用 |
|---|---|---|
| `c:dusts/saltpeter` | `tfc:powder/saltpeter` | 共通Saltpeter Dustを入力とするレシピでTFC Saltpeter Powderを認める。 |
| `c:dusts/sulfur` | `tfc:powder/sulfur` | 共通Sulfur Dustを入力とするレシピでTFC Sulfur Powderを認める。 |
| `tfc:forge_fuel` | `immersiveengineering:coal_coke` | IEの単体CokeをTFC Charcoal Forge用燃料として認める。 |
| `tfc:leather_knapping` | `immersiveengineering:ersatz_leather` | IE Ersatz LeatherをTFCのLeather Knapping入力として認める。 |

根拠は[Saltpeterタグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/c/tags/item/dusts/saltpeter.json)、[Sulfurタグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/c/tags/item/dusts/sulfur.json)、[Forge Fuelタグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfc/tags/item/forge_fuel.json)、[Leather Knappingタグ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfc/tags/item/leather_knapping.json)である。

#### TFCの燃料データ

| TFC FuelデータID | 燃料Item ID | 燃焼時間 | 燃焼温度 |
|---|---|---:|---:|
| `tfie:coal_coke` | `immersiveengineering:coal_coke` | 3,200 ticks | 1,550℃ |
| `tfie:coke` | `immersiveengineering:coke` | 32,000 ticks | 1,550℃ |

この表はTFCの燃料データを定義する。IE Blast Furnaceの燃料レシピとは別の登録であり、IE高炉でCoke Blockを燃料として認める規則を意味しない。Forge Fuelタグへは単体Cokeだけを追加するため、TFC Fuelデータの存在だけを根拠としてCoke BlockをCharcoal Forgeへ許可しない。各設備の受入タグと燃料データを両方満たす必要がある。

根拠は[単体CokeのTFC Fuel](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tfc/fuel/coal_coke.json)と[Coke BlockのTFC Fuel](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tfc/fuel/coke.json)である。

#### 非金属の熱容量

| TFC Item HeatデータID | 対象Item ID | 熱容量 |
|---|---|---:|
| `tfie:bricks` | `minecraft:bricks` | 1.0 |
| `tfie:slag` | `immersiveengineering:slag` | 0.6 |

この熱容量はTFC Heatingの加熱・冷却対象とするための値である。金属融点・鍛造温度を付与せず、Clinker BrickおよびSlag Glassへの加熱レシピで要求する温度は別に定義する。根拠は[Bricksの熱データ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tfc/item_heat/bricks.json)と[Slagの熱データ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/tfc/item_heat/slag.json)である。

### 31. サーバー設定

TFIEの設定はSERVER区分へ登録する。次の4項目を提供し、レシピの数量・FE・時間・温度条件は原則としてデータパックで定義する。

| 設定キー | 型 | 標準値 | 許容範囲 |
|---|---|---|---|
| `crucibleExternalHeater.fePerTick` | 整数 | 20 | 0〜32,000とする。 |
| `crucibleExternalHeater.temperature` | 整数 | 2,000 | 0〜2,147,483,647とする。 |
| `compatibility.crateMaximumItemSize` | TFC Size列挙値 | `VERY_LARGE` | `TINY`、`VERY_SMALL`、`SMALL`、`NORMAL`、`LARGE`、`VERY_LARGE`、`HUGE` のいずれかとする。 |
| `compatibility.tfcRotationalEnergyModifier` | 倍精度浮動小数 | 1.0 | `Double.MIN_NORMAL` 以上 `Double.MAX_VALUE` 以下とし、0と負数を認めない。 |

回転倍率の数値上の下限は約 `2.2250738585072014×10^-308`、上限は約 `1.7976931348623157×10^308` となる。Heaterの消費を0へ設定することは設定上認めるが、IE側の起動判定と電力保持の仕組みを解除する設定ではない。

根拠は[TFIEConfig](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/config/TFIEConfig.java)と[SERVER区分の登録](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/TFIE.java)である。

### 32. ゲーム内ガイドと表示

表示言語として `en_us` を提供する。IE Engineer’s ManualとTFC Field Guideの両方にTFIEの機能を記載し、実際のレシピ・材料・形成条件・数値と一致させる。

IE ManualのカテゴリIDを `tfie:tfc_ie_changes`、TFC Field GuideのカテゴリIDを `tfc:tfc_ie_changes` とする。IE側はautoload priority 4・category weight 8、TFC側はsortnum 10とする。基礎となる項目の対応を次表に示す。

| 内容 | IE ManualのEntry ID | TFC Field GuideのEntry ID |
|---|---|---|
| 金型 | `tfie:tfc_ie_mold` | `tfc:tfc_ie_changes/mold` |
| 薬液 | `tfie:tfc_ie_chemicals` | `tfc:tfc_ie_changes/chemicals` |
| マルチブロック | `tfie:tfc_ie_multiblocks` | `tfc:tfc_ie_changes/multiblocks` |
| Blueprint | `tfie:tfc_ie_blueprint` | `tfc:tfc_ie_changes/blueprint` |
| Jute互換 | `tfie:tfc_ie_jute` | `tfc:tfc_ie_changes/jute` |
| Hemp | `tfie:tfc_ie_hemp` | `tfc:tfc_ie_changes/hemp` |
| Herbicide | `tfie:tfc_ie_herbicide` | `tfc:tfc_ie_changes/herbicide` |
| External Heater | `tfie:tfc_ie_external_heater` | `tfc:tfc_ie_changes/external_heater` |
| Drill Head | `tfie:tfc_ie_drill` | `tfc:tfc_ie_changes/drill` |
| Kinetic Dynamo | `tfie:tfc_ie_dynamo` | `tfc:tfc_ie_changes/dynamo` |

IE Manualから設定値を参照するキー `crucibleExternalHeaterFEPerTick` と `crucibleExternalHeaterTemperature` は、実際のサーバー設定値を返す。未知の参照キーを実数値として表示しない。Hempの説明では、栽培時の気温・水分と、野生生成時の気温・地下水を区別する。

TFIE 0.1.0のガイドには、Arc用Blueprintの8mode・主入力への配置・非消費・配合表・未装着時の基本加工、Briquetだけを使う製錬、Wrought IronからSteelへの製造、出力温度90%、高炉とAlloy Kilnの有効化、Metal Pressの4形状を反映する。製作・形成を禁止していた高炉・合金窯の説明は残さない。Optional連携の説明は導入状態と一致させる。根拠は[IE Manual登録](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/assets/immersiveengineering/manual/autoload.json)、[TFCカテゴリ](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/assets/tfc/patchouli_books/field_guide/en_us/categories/tfc_ie_changes.json)、[設定値の表示連携](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/java/net/claustra01/tfie/client/ClientEvents.java)である。

### 33. 削除・置換するレシピの台帳

#### 適用方法

本章はTFIEが削除または置換するIE標準レシピと、TFIEが0.1.0へ持ち越さない旧レシピを定義する。基準はImmersive Engineering 1.21.1-12.4.2-194の固定commit `583a182549d284a35c813b55ac2a0d1fddcf945a` とする。

レシピIDは `namespace:path` の形式で記載し、リソースファイルの拡張子 `.json` を含めない。表の `{M}` には直後に定義する集合の各文字列を1つずつ代入する。「削除」は当該標準レシピを読込対象から除外すること、「置換」は当該標準レシピを除外したうえで、本書に定義した材料・数量・出力・温度・mode条件のレシピを採用することを意味する。旧レシピと新レシピを同時に有効にしない。

対象は各表の完全ID集合と、その役割に相当する基準IEの動的生成処理へ限定する。外部Modの同名パスを含めた全名前空間への一括削除を行わない。対象素材の無効化によりTFIEの置換後レシピを読み込まない場合も、IE標準の置換元レシピを代替として復活させない。初回起動とデータ再読込の両方でこの状態を維持する。

#### Crusher

次表の54件を削除する。鉱石、Raw Ore、Raw Ore Blockから直接Dustまたは資源を得る標準処理を除去し、鉱石加工はTFCM側の定義を使用する。羊毛の粉砕は独立した削除対象とする。

| レシピIDの完全形またはテンプレート | 集合 | 件数 | 処置 |
|---|---|---:|---|
| `immersiveengineering:crusher/ore_{M}` | 下記の鉱石22種を用いる。 | 22 | 削除する。 |
| `immersiveengineering:crusher/raw_ore_{M}` | 下記の金属鉱石15種を用いる。 | 15 | 削除する。 |
| `immersiveengineering:crusher/raw_block_{M}` | 下記の金属鉱石15種を用いる。 | 15 | 削除する。 |
| `immersiveengineering:crusher/nether_gold` | 固定IDとする。 | 1 | Nether Gold Oreの直接粉砕を削除する。 |
| `immersiveengineering:crusher/wool` | 固定IDとする。 | 1 | 羊毛1個から糸4個を得る加工を削除し、代替を追加しない。 |

鉱石22種の集合は、`aluminum`、`ardite`、`coal`、`cobalt`、`copper`、`diamond`、`emerald`、`fluorite`、`gold`、`iron`、`lapis`、`lead`、`nickel`、`osmium`、`platinum`、`quartz`、`redstone`、`silver`、`tin`、`tungsten`、`uranium`、`zinc` とする。

金属鉱石15種の集合は、`aluminum`、`ardite`、`cobalt`、`copper`、`gold`、`iron`、`lead`、`nickel`、`osmium`、`platinum`、`silver`、`tin`、`tungsten`、`uranium`、`zinc` とする。

Nether Quartz Oreは `ore_quartz` の入力タグに含まれるため、このIDの削除で対象とする。Nether Gold Oreは個別ID `nether_gold` を削除する。存在しない `crusher/nether_quartz` というIDを追加の削除対象として作成しない。

`immersiveengineering:crusher/ingot_{M}` は精錬済みIngotの粉砕であり、本章の鉱石処理削除へ含めない。TFIEはWrought IronのCrusher経路を追加せず、Wrought Iron Dustの入手はTFCMのQuern経路を使用する。Charcoal、Fluxstone、Olive、TFC Sandstoneの採用レシピは本書の非金属加工仕様に従う。鉱石と羊毛に関係しないCrusherの全レシピを一括削除しない。

根拠は[IEの鉱石レシピ生成](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/datagen/java/blusunrize/immersiveengineering/data/recipes/OreRecipes.java)、[Nether Gold](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crusher/nether_gold.json)、[Quartz Ore](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crusher/ore_quartz.json)、[Wool](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crusher/wool.json)とする。

#### Arc Furnace

基準IEの静的Arcレシピ78件を、次表に従って削除または置換する。表は将来の未知の非金属レシピまで削除するワイルドカードではない。

| レシピIDの完全形またはテンプレート | 集合 | 件数 | 処置 |
|---|---|---:|---|
| `immersiveengineering:arcfurnace/ore_{M}` | 前節の金属鉱石15種を用いる。 | 15 | 削除し、Briquet以外の鉱石製錬を追加しない。 |
| `immersiveengineering:arcfurnace/raw_ore_{M}` | 前節の金属鉱石15種を用いる。 | 15 | 削除する。 |
| `immersiveengineering:arcfurnace/raw_block_{M}` | 前節の金属鉱石15種を用いる。 | 15 | 削除する。 |
| `immersiveengineering:arcfurnace/dust_{M}` | 下記のDust23種を用いる。 | 23 | 削除し、金属Dustの直接製錬を追加しない。 |
| `immersiveengineering:arcfurnace/alloy_{M}` | `brass`、`bronze`、`constantan`、`electrum`、`invar`、`manyullyn`、`rose_gold` とする。 | 7 | IE標準配合をすべて除去する。採用した合金だけを本書の固定配合とBlueprint条件で追加する。Manyullynを追加しない。 |
| `immersiveengineering:arcfurnace/netherite_scrap` | 固定IDとする。 | 1 | Ancient DebrisからNetherite Scrapを得るArc加工を削除する。 |
| `immersiveengineering:arcfurnace/steel` | 固定IDとする。 | 1 | Wrought Iron 1個とCoke Dust 1個からSteel 1個とSlag 1個を得る204,800 FE・400 ticksのmode 1配合へ置換する。 |
| `immersiveengineering:arcfurnace/insulating_glass` | 固定IDとする。 | 1 | Glass Block 2個とTFCM Wrought Iron Dust 1個からInsulating Glass 2個を得る51,200 FE・100 ticksの基本加工へ置換する。 |

Dust23種の集合は、`aluminum`、`ardite`、`brass`、`bronze`、`cobalt`、`constantan`、`copper`、`electrum`、`gold`、`invar`、`iron`、`lead`、`manyullyn`、`nickel`、`osmium`、`platinum`、`rose_gold`、`silver`、`steel`、`tin`、`tungsten`、`uranium`、`zinc` とする。

採用したBriquet製錬33系列と合金・Steel製造19レシピを、この削除後の標準配合へ重複して重ねない。Briquetの出力は各系列の対応Ingotとし、鉄3系列はCast Ironとする。Insulating GlassはBlueprint未装着時および全modeで許可する。TFIEによるArcの鉱石製錬制限は、TFCMが提供するTFC Heatingの削除を意味しない。

根拠は[IEのArc鉱石・Dust・合金生成](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/datagen/java/blusunrize/immersiveengineering/data/recipes/OreRecipes.java)、[Netherite Scrapと非金属Arc加工](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/datagen/java/blusunrize/immersiveengineering/data/recipes/MultiblockRecipes.java)、[Steel](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/arcfurnace/steel.json)、[Insulating Glass](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/arcfurnace/insulating_glass.json)とする。

#### Arc Recycling

静的な登録入口 `immersiveengineering:arc_recycling_list` を無効化する。このレシピの型は `immersiveengineering:generated_list`、生成器IDは `immersiveengineering:arc_recycling_list` である。

個々のリサイクル対象を固定JSONのID一覧だけで削除する方式に依存しない。IEのArc Recycling生成器が作る全 `ArcRecyclingRecipe` をArcの利用対象から除外し、起動後およびデータ再読込後も金属製品からのArcリサイクルを無効にする。上流の識別値 `specialRecipeType = recycling` と生成器の関係を維持して判定する。Recipe一覧生成器全体を停止せず、MixerのPotion生成器とBottling MachineのPotion生成器を巻き込まない。

この決定の対象はArc Recyclingであり、別の機械や通常のSmelting・Blastingに存在する全リサイクルレシピを追加で禁止するものではない。[動的レシピの登録入口](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/arc_recycling_list.json)、[GeneratedListRecipe](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/crafting/GeneratedListRecipe.java)、[ArcRecyclingCalculator](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/crafting/ArcRecyclingCalculator.java)、[ArcRecyclingRecipe](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/ArcRecyclingRecipe.java)

#### Blast FurnaceとImproved Blast Furnace

通常高炉と改良高炉は同じレシピ集合を使用する。標準のSteel製造2件とBlock燃料2件を削除し、CokeとCharcoalの単体燃料2件を維持する。

| レシピID | 処置 |
|---|---|
| `immersiveengineering:blastfurnace/steel` | Iron IngotからSteelを製造する標準レシピを削除する。 |
| `immersiveengineering:blastfurnace/steel_block` | Iron BlockからSteel Blockを製造する標準レシピを削除する。 |
| `immersiveengineering:blastfurnace/fuel_coke` | `#c:coal_coke` の単体燃料と1,200 ticksを維持する。 |
| `immersiveengineering:blastfurnace/fuel_charcoal` | `#c:charcoal` の単体燃料と300 ticksを維持する。 |
| `immersiveengineering:blastfurnace/fuel_coke_block` | 削除する。 |
| `immersiveengineering:blastfurnace/fuel_charcoal_block` | 削除する。 |

高炉の製錬はHematite、Magnetite、LimoniteのBriquet 1個からRaw Iron Bloom 1個を得る1,200 ticksの3配合へ限定する。Slagを生成しない。CokeとCharcoalの燃料タグへBlockを追加してBlock燃料の禁止を迂回しない。IE標準の基準ファイルには、この6件以外の高炉加工・燃料IDは存在しない。[高炉レシピ集合](https://github.com/BluSunrize/ImmersiveEngineering/tree/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/blastfurnace)、[Coke燃料](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/blastfurnace/fuel_coke.json)、[Charcoal燃料](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/blastfurnace/fuel_charcoal.json)

高炉とAlloy Kilnを有効にするため、旧TFIEが行っていた `immersiveengineering:crafting/blastbrick`、`immersiveengineering:crafting/blastbrick_reinforced`、`immersiveengineering:crafting/alloybrick` の無効化を継承しない。これらの建材は本書の採用した製作条件に従って有効にする。

#### Alloy Kiln

Alloy Kilnのレシピ型は `immersiveengineering:alloy`、IE標準レシピIDのディレクトリは `alloysmelter` とする。次の8件を処理する。

| レシピID | 処置 |
|---|---|
| `immersiveengineering:alloysmelter/brass` | Copper 9個とZinc 1個からBrass 10個を得る配合へ置換する。 |
| `immersiveengineering:alloysmelter/bronze` | Copper 9個とTin 1個からBronze 10個を得る配合へ置換する。 |
| `immersiveengineering:alloysmelter/constantan` | Copper 1個とNickel 1個からConstantan 2個を得る配合へ置換する。 |
| `immersiveengineering:alloysmelter/electrum` | Gold 1個とSilver 1個からElectrum 2個を得る配合へ置換する。 |
| `immersiveengineering:alloysmelter/invar` | Wrought Iron 2個とNickel 1個からInvar 3個を得る配合へ置換する。 |
| `immersiveengineering:alloysmelter/rose_gold` | 削除する。Alloy Kiln用の代替レシピを追加しない。 |
| `immersiveengineering:alloysmelter/manyullyn` | 削除する。代替レシピを追加しない。 |
| `immersiveengineering:alloysmelter/insulating_glass` | Glass Block 2個とTFCM Wrought Iron Dust 1個からInsulating Glass 2個を得る配合へ置換する。 |

上記の採用6件に、Loose Andesite 1個とZinc Nugget 1個からAndesite Alloy 1個を得る配合、およびLoose Andesite 1個とTin Nugget 1個から同量を得る配合を追加する。採用8配合はすべて200 ticksとする。Sterling Silver、Osmiridium、3成分以上の合金を追加しない。Rose GoldのAlloy Kilnでの禁止は、採用済みArc配合の禁止へ拡張しない。[IE標準の合金生成](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/datagen/java/blusunrize/immersiveengineering/data/recipes/OreRecipes.java)、[Rose Gold](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/alloysmelter/rose_gold.json)、[Manyullyn](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/alloysmelter/manyullyn.json)

#### Metal Pressの標準配合との競合除去

TFIEが扱う金属のPlate、Rod、Wireは、同じ材料に対するIE標準レシピを除外して本書の数量・標準出力へ置換する。IE標準Wireの出力2個を残さず、TFIEのWire 4個へ統一する。IE標準の `iron` はWrought Ironの個別配合へ置換し、Cast Ironとの共通化を行わない。

| レシピIDの完全形またはテンプレート | 集合 | 件数 |
|---|---|---:|
| `immersiveengineering:metalpress/plate_{M}` | 下記の21種を用いる。 | 21 |
| `immersiveengineering:metalpress/rod_{M}` | 下記の21種を用いる。 | 21 |
| `immersiveengineering:metalpress/wire_{M}` | 下記の21種を用いる。 | 21 |
| `immersiveengineering:metalpress/rod_netherite` | 固定IDとする。 | 1 |

21種の集合は、`aluminum`、`brass`、`bronze`、`cobalt`、`constantan`、`copper`、`electrum`、`gold`、`invar`、`iron`、`lead`、`nickel`、`osmium`、`platinum`、`rose_gold`、`silver`、`steel`、`tin`、`tungsten`、`uranium`、`zinc` とする。

TFIE対応外の `ardite` と `manyullyn` を、このMetal Press置換集合へ追加しない。標準の `gear_{M}`、`blaze_rod`、`bullet_casing`、`melon` の各加工もこの置換へ含めない。SheetはTFIEの独立した金型・レシピで追加する。金属設定により対象金属のTFIE配合が無効となった場合も、その金属のIE標準経路を代替として有効化しない。[IEのPlate・Rod・Wire生成](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/datagen/java/blusunrize/immersiveengineering/data/recipes/OreRecipes.java)、[IE標準Wire出力](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/metalpress/wire_copper.json)、[Netherite Rod](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/metalpress/rod_netherite.json)

#### 手作業のPlateとRod

次の8件だけを手作業の形状加工に関する削除対象とする。対象金属はTFC/TFCM金床と採用済みMetal Pressで加工する。

| レシピIDの完全形またはテンプレート | 対象集合 | 件数 |
|---|---|---:|
| `immersiveengineering:crafting/plate_{M}_hammering` | `copper`、`gold`、`iron`、`nickel`、`silver`、`steel` とする。 | 6 |
| `immersiveengineering:crafting/stick_{M}` | `iron`、`steel` とする。 | 2 |

この個別指定を、全金属の手作業加工禁止へ拡張しない。旧TFIEの同じ `immersiveengineering` 名前空間に置かれた無効化定義と同じ対象を維持する。[旧版の手作業Plate無効化](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/plate_copper_hammering.json)、[旧版のIron Rod無効化](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/immersiveengineering/recipe/crafting/stick_iron.json)

#### Nuggetの相互変換

Copper、Nickel、Silver、Steelについて、次の8件を削除する。TFCMが既に提供する変換を使用し、TFIEによる数量の再定義を行わない。

| レシピID | 処置 |
|---|---|
| `immersiveengineering:crafting/copper_ingot_to_nugget_copper` | 削除する。 |
| `immersiveengineering:crafting/nugget_copper_to_copper_ingot` | 削除する。 |
| `immersiveengineering:crafting/ingot_nickel_to_nugget_nickel` | 削除する。 |
| `immersiveengineering:crafting/nugget_nickel_to_ingot_nickel` | 削除する。 |
| `immersiveengineering:crafting/ingot_silver_to_nugget_silver` | 削除する。 |
| `immersiveengineering:crafting/nugget_silver_to_ingot_silver` | 削除する。 |
| `immersiveengineering:crafting/ingot_steel_to_nugget_steel` | 削除する。 |
| `immersiveengineering:crafting/nugget_steel_to_ingot_steel` | 削除する。 |

旧TFIEが同じIDに置いた上書きレシピも採用しない。上書きファイルだけを除去してIE標準の変換を復活させる処置は行わない。TFCMのIngot 1個とNugget 9個の既存変換、およびNugget 1個10 mBという既存数量規則は変更しない。[IEのCopper分解](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/copper_ingot_to_nugget_copper.json)、[IEのCopper組立](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/nugget_copper_to_copper_ingot.json)

#### Ingotと金属圧縮Blockの相互変換

IE独自の金属圧縮Blockについて、次の16件を削除する。Block製法はTFC/TFCMのPlated Block仕様を使用し、Ingot 9個からIE圧縮Blockを作る代替をTFIEに追加しない。

| レシピIDテンプレート | 対象集合 | 件数 |
|---|---|---:|
| `immersiveengineering:crafting/ingot_{M}_to_storage_{M}` | 下記の8種を用いる。 | 8 |
| `immersiveengineering:crafting/storage_{M}_to_ingot_{M}` | 下記の8種を用いる。 | 8 |

対象集合は、`aluminum`、`constantan`、`electrum`、`lead`、`nickel`、`silver`、`steel`、`uranium` とする。

この削除はIngotと圧縮Blockの相互変換に限定する。Raw OreとRaw Ore Blockの保管用相互変換、およびIEの既存BlockとそのSlabを加工するレシピを同じ名前の一括検索で削除しない。Vanilla名前空間のIron・Gold・CopperのBlockレシピを、本表のIE独自Block16件として数えない。[IEの圧縮・解体生成](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/datagen/java/blusunrize/immersiveengineering/data/recipes/OreRecipes.java)、[Slab加工の入力指定](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/storage_aluminum_to_slab.json)

#### その他の個別削除

`immersiveengineering:crafting/gunpowder_from_dusts` を無効化し、TFIEによる代替レシピを追加しない。一般クラフト・工具・防具のうち、本書の個別製法で置換したものは、それぞれの置換元IDに従う。Components BlueprintのIron入力、Fluorescent Tubeの標準製法、ChemthrowerとJerrycanのMinecraft Bucket入力を、一般的なTFC素材置換規則から暗黙に変更しない。[IEの粉末火薬](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/generated/resources/data/immersiveengineering/recipe/crafting/gunpowder_from_dusts.json)

#### TFIEのBlock金型とBlock加工

次の旧TFIEレシピ3件を0.1.0の出荷内容へ含めない。

| レシピID | 旧用途 |
|---|---|
| `tfie:blueprint/mold_block` | Block金型を製作する。 |
| `tfie:metalpress/block_steel` | Steel Ingot 9個をSteel Plated Blockへ加工する。 |
| `tfie:metalpress/block_uranium` | Uranium Ingot 9個をIE Uranium圧縮Blockへ加工する。 |

TFIEのMetal Press用追加形状はPlate、Sheet、Rod、Wireに限定し、旧Block金型 `tfie:mold_block` をこの加工体系へ含めない。Sheet金型 `tfie:mold_sheet` とその製法は維持する。[旧Block金型](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/blueprint/mold_block.json)、[旧Steel Block加工](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/metalpress/block_steel.json)、[旧Uranium Block加工](https://github.com/lau-mods/TFIE/blob/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/metalpress/block_uranium.json)

#### Beneath向け製材の除外

TFIE 0.1.0は、入力または出力に `beneath:` の木材アイテムを参照するTFIE製材レシピを出荷しない。OverdimsのCrimson・Warpedには `tfcoverdims:` の木材IDを使用し、BeneathのIDを別名として許容しない。

固定基準の旧TFIE 0.0.3には製材JSONが136件あり、参照する木材名前空間は `tfc` と `afc` だけである。したがって、この固定基準から削除するBeneath製材の既存ID集合は空とする。存在しない `tfie:sawmill/crimson/...` を旧版の削除IDとして記載しない。Beneath自体が提供する他用途のレシピを、このTFIE製材除外によって削除しない。[旧TFIEの製材集合](https://github.com/lau-mods/TFIE/tree/83644ef428f483b200a5403d559e37f8311c529e/src/main/resources/data/tfie/recipe/sawmill)

### 34. データ定義と互換性

#### データパックによる調整

本書のレシピ、材料タグ、気候、栄養、燃料、Item Heatおよび構造は、Minecraft 1.21.1と各提供Modのデータ形式を使用する。利用者の上位優先度データパックから数量・費用・時間・条件を調整する手段を維持する。TFIEの追加レシピは`tfie`名前空間へ登録し、IEの既存レシピを置換するものは削除・置換台帳に示すIDで管理する。同一経路を上書きレシピと新規レシピの両方で重複登録しない。

データパックによって値を変更した場合、そのワールドの値を機械の新規処理、JEI、ガイドの動的な表示へ反映する。本書の数量表はTFIE 0.1.0の標準出荷値とする。利用者が独自に追加したレシピを、TFIEが提供する標準レシピとして数えない。Blueprintのmodeを要求するArc配合には固定modeの条件を保持し、データ再読込後にもその条件を照合する。Arcの開始済み処理には、本書で定める配合の保持と待機を適用する。

条件付きレシピを読み込む前に、関連Mod、金属・鉱石の有効設定、入力と出力の登録を確認する。条件不成立のレシピは読み込まず、対象Recipe IDと不成立理由を記録する。存在しないItem・Fluidを空入力として残さず、Missing Registryエラーを起こすデータも出荷しない。必須Modそのものの不在は、Optional連携の省略として処理せず依存関係エラーとする。

削除対象は起動時のレシピ一覧、JEI表示、機械の検索対象、再読込後の一覧で一致させる。特にArc Recyclingは、静的JSONだけを削除して動的な生成を残す状態を認めない。上流更新で削除対象のIDや生成方法が変わった場合は、同じ迂回経路が再出現しないよう互換性を更新する。

これらのデータ読込の基礎は[NeoForgeのレシピ](https://docs.neoforged.net/docs/1.21.1/resources/server/recipes/)、[条件付き読込](https://docs.neoforged.net/docs/1.21.1/resources/server/conditions/)、[IEのレシピキャッシュ](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/api/java/blusunrize/immersiveengineering/api/crafting/cache/CachedRecipeList.java)による。modeの固定割当、無効化範囲、温度と予約の保持は、本書が定義するTFIEの追加要件である。

#### 保存とサーバー処理

レシピ選択、材料消費、乱数、副産物、加熱温度、Blueprintのmode変更はサーバー側で確定する。表示と入力操作だけをクライアント側へ分離し、専用サーバーの共通処理からクライアント専用クラスを読み込まない。

Blueprintのmodeと、ArcのInventory、進捗、予約した材料、待機状態、生成済みの温度情報を、再起動とチャンク再読込を経ても保持する。Arcの開始済み処理の配合をBlueprintの交換によって変更せず、再読込で別レシピへ転換しない。入力材料を通常操作で除去したときのIEの処理中断と、Blueprintだけを除去したときの処理継続を区別する。他の機械には各章で定める保存・進行規則を適用し、Arcの処理キューを全機械へ新設しない。

新規ワールドを前提とし、TFIE 0.0.xのItem ID、レシピID、設定ファイル、機械の保存状態を移行する処理は設けない。旧Block Moldなどの廃止IDを移行のためだけに登録しない。0.1.0の導入説明には新規ワールドを前提とすることを記載する。

#### 表示と資源

追加アイテム・作物には対応するモデル、テクスチャ、`en_us`の表示名を用意する。各Headの材質、Arc用Blueprintの現在mode、Sheet金型とPlate金型の用途を表示上でも区別する。Blueprintは製図用の図面を表す外観とし、専用の設定画面を作成しない。

JEIがある場合は、採用した機械カテゴリと実際のレシピ型を一致させる。Squeezer用種子をFermenterのレシピとして表示しない。Arcの合金・Steelには必要modeを持つ非消費Blueprintを表示し、BriquetとInsulating Glassには不要なBlueprint条件を表示しない。機械で処理しない削除済みレシピをJEIへ残さない。JEI自体はTFIEの必須依存へ追加しない。

IE ManualとTFC Field Guideには、本書の対象素材、形成構造、製法、数値および制約を記載する。AFC・Overdimsの未導入状態とFirmaLife・Advanced TFC Tech Unofficialの未導入状態を扱い、未登録アイテムの表示参照で画面を停止させない。実際のサーバー設定値を参照する項目は、その値を表示する。

### 35. 旧TFIEとCrossoverからの変更

比較対象をTFIE 0.0.3とTFC-IE Crossover 2.1.3とする。この表は仕様の差を説明するためのものであり、各機能の入出力や数値は本書の対応章を定義とする。

| 対象 | TFIE 0.1.0の差 |
|---|---|
| 依存関係 | TFCMをRequiredへ追加する。AFCの依存宣言を削除し、AFCとOverdimsは条件付き製材だけで対応する。 |
| 金属加工の対象 | TFCMの有効金属と実在形状へ対象を広げる。Plate 100mBとSheet 200mBを別に扱い、旧Block金型とBlock加工を含めない。 |
| Crusherの鉱石処理 | TFCM側だけへ委ね、IEの迂回レシピを削除する。TFIEから鉱石処理を重複追加しない。 |
| Arcの鉱石製錬 | Briquetだけを入力とし、鉄3系列はCast Ironを出力する。原鉱石・Raw Ore・Dust等の標準処理とArc Recyclingを無効化する。 |
| Arcの合金とSteel | 固定8modeの非消費Blueprintを主入力へ置く。主入力と添加材の割当を維持したまま競合を解消する。Wrought IronからSteelへの専用配合を用いる。 |
| 炉の出力温度 | 出力金属の融点の90%を付与し、異温度スタックを合流しない。容量不足時に生成物を失わず、待機中の追加消費を行わない。 |
| 高炉とAlloy Kiln | 建材の通常クラフト、形成、通常プレイでの運用を有効にする。高炉は鉄BriquetからRaw Iron Bloomだけを出力し、Alloy Kilnは指定7合金レシピとInsulating Glassを扱う。 |
| ドリルヘッド | Invar、Titanium、Netherite、Tungsten Steelの4種を追加する。Iron Headの修理材をWrought Ironへ限定する。 |
| 一般クラフト | 指定するIron入力をWrought Ironへ限定し、IE Crafting TableをTreated Lumberで製作する。Components Blueprint・Fluorescent Tubeの標準材料は維持し、TFC Bucketの追加経路を設けない。 |
| Coke Oven | 1原料につきCoke1個とCreosote500mB、最大16原料・6,000ticksという比率を維持する。IE 194の非空出力欄の容量計算を実残容量へ修正する。 |
| 非金属Crusher・Heating・Chisel | 木炭、Fluxstone、Olive、砂岩21件、Clinker Brick、Slag Glass、Concrete系8件の入出力・数量・温度・確率を継承する。 |
| Insulating Glass | IE標準のIron DustをTFCM Wrought Iron Dustへ置き換える。出力数とArc・Alloy Kilnの費用は維持する。 |
| 薬液と基礎流体 | Redstone Acid、Herbicide、Limewater、Salt Water、Brine、FirmaLife不在時のMilk Vinegarの数量・費用を継承する。Herbicideの銅鉱石Powderの入手工程はTFCMへ移る。 |
| FirmaLifeの加工 | Mixer4件、Wild HoneycombのSqueezer1件、FirmaLife乳を対象とするRefinery1件を追加する。既存のRaw Honey発酵を重複追加しない。 |
| High Carbon SteelのSlag | Crossoverの金床副産物を現行TFCのRecipe IDで復元する。旧TFIEにはこの処理がない。 |
| 羊毛のCrusher加工 | 羊毛1個からString4個を得るIEレシピを無効化する。 |
| 種子の機械種別 | 旧TFIEとCrossoverのSqueezer用23レシピにある`immersiveengineering:fermenter`指定を是正し、採用したSqueezerでPlant Oilを製造する。入出力、6,400FE、基礎80ticksは維持する。 |
| 製材 | TFC20種とAFC14種を維持し、OverdimsのCrimson・Warped2種を追加する。Beneath木材をTFIEの出荷レシピへ含めない。 |
| AFC Latex | IEによるRubber製造と、Rubberを使うConveyor・LV/MV Wire Coilの追加経路を設けない。AFCとFirmaLifeが提供する既存処理を維持する。 |

Coke Ovenの容量差は[IE CokeOvenLogic](https://github.com/BluSunrize/ImmersiveEngineering/blob/583a182549d284a35c813b55ac2a0d1fddcf945a/src/main/java/blusunrize/immersiveengineering/common/blocks/multiblocks/logic/CokeOvenLogic.java)、CrossoverのSlag判定は[AnvilBlockEntityMixin](https://github.com/NMagpie/TFC-IE-Crossover/blob/41eb5d40bdd9e97152cd400decce4c04f746b902/src/main/java/com/nmagpie/tfc_ie_addon/mixin/AnvilBlockEntityMixin.java)を根拠とする。その他の差は各機能章に示す固定版のJSON・コードと、本書の採用仕様の比較による。

FirmaLifeの採用6件は、3.0.16の直前の更新で追加された機能だけを対象とするものではない。3.0.15から3.0.16にはレシピ定義の追加・変更がなく、3.0.11から3.0.16の間に追加されたCaramel等と、既存の未対応加工を含めて採用した。出典は[FirmaLife 3.0.16の変更履歴](https://github.com/eerussianguy/firmalife/blob/v3.0.16/CHANGELOG.md)、[3.0.15からの差分](https://github.com/eerussianguy/firmalife/compare/v3.0.15...v3.0.16)、[3.0.11からの差分](https://github.com/eerussianguy/firmalife/compare/v3.0.11...v3.0.16)である。

### 36. 実装の受入条件

本節は、TFIE 0.1.0の実装が本書を満たすかを判定する条件である。仕様書の作成完了と、Modの実装・ゲーム内検証の完了を同一の状態として扱わない。

#### 構成とデータ

| 対象 | 合格条件 |
|---|---|
| 基本構成 | TFC・IE・TFCM・Patchouliを備えたMinecraft 1.21.1で、クライアントと専用サーバーが起動する。Required不足は該当依存を明示して起動を拒否する。 |
| Optional構成 | FirmaLifeとAdvanced TFC Tech Unofficialを個別に導入・除外した場合、対応する連携だけが切り替わる。AFCとOverdimsの未導入でも起動し、導入した木材だけが追加される。 |
| TFCM設定 | 金属または鉱石を無効化した場合、該当入力・出力を参照する全レシピを読み込まず、理由をログへ記録する。代替レシピとmodeの詰め直しを行わない。 |
| レシピの件数と対象 | 最大63金属の4形状、Briquet33系列、Arc合金18件とSteel1件、高炉3件、Alloy Kiln合金7件、Head9種、Cloche29種、種子Squeezer23種、製材36樹種の対象集合が各表と一致する。 |
| 削除 | 台帳の対象レシピが通常一覧、JEI、機械処理、再読込後に残らない。Arc Recyclingの動的生成も再出現しない。 |
| データ上書き | 上位データパックによる数量・FE・時間・条件の変更を再読込後の新規処理へ反映する。変更されたArcの開始済み処理は、本書の待機規則で保持する。 |

#### 配合と機械状態

| 対象 | 合格条件 |
|---|---|
| Arcの競合 | 合金18件とSteel1件の全171組を照合し、同一mode・同一主入力の候補重複を0件とする。登録順、JEI表示順、添加材の搬入順を変えても対象配合が変わらない。 |
| 材料不足 | mode 3のCopperにZincだけを与えてもBrassを開始しない。Bismuthの追加後にBismuth Bronzeだけを開始する。 |
| 余剰添加材 | mode 1のBrassにBismuthを同居させてもBrassを作り、Bismuthを消費しない。mode 5のConstantanにZincを同居させてもConstantanを作る。 |
| 未装着と全mode | Blueprint未装着時とmode 1〜8のすべてでBriquet・Insulating Glassを処理する。未装着時とmode 6〜8では合金・Steelを開始しない。 |
| Blueprintの操作 | 右クリックで1から8を順に循環し、8の次は1へ戻る。炉を開く操作ではmodeを変更しない。専用GUI・専用スロットを作らない。 |
| Blueprintの非消費 | 通常処理、並列処理、出力待機、再起動、解体の各状態でBlueprintを材料消費しない。自動搬入と2枚目の投入を拒否する。外部操作による複数枚・不正modeでは新規処理を停止する。 |
| 予約と切替 | 同じ材料数量を複数処理へ予約しない。Blueprint変更後も開始済み処理を元の配合で扱い、新規開始分だけへ変更を適用する。 |
| 熱 | 金属出力が登録融点の90%を持ち、時間経過で冷却する。温度の異なる既存出力へ合流せず、再加熱・平均化しない。 |
| 加熱炉の出力待機 | 主出力と副産物がすべて収まるまで待機する。出力の一部消失、二重消費、追加FE、副産物の再抽選を生じさせない。Sawmillには段階別排出を適用する。 |
| Arcの保存 | チャンク再読込とサーバー再起動の前後でmode、進捗、予約、入力、出力、温度、待機状態が一致する。 |
| Coke Oven | 空出力・原料16個からCoke16個とCreosote8,000mBを6,000ticksで得る。Coke60個の欄には最大4個だけを追加する。液体残量500mB未満では開始しない。 |
| 高炉 | 鉄Briquet3系列以外を製錬しない。単体CokeとCharcoalだけを燃料とし、Raw Iron Bloomだけを出力する。 |
| 金属量 | TFIEのMetal Pressと採用する合金配合が本書の金属量を保ち、100mBのPlated Blockを900mBとして圧縮・解体しない。TFCM既存のNugget例外をTFIEへ拡張しない。 |

#### 栽培、加工、表示

| 対象 | 合格条件 |
|---|---|
| Hemp | 気候・水分・光・N/P/K・上下段・枯死・収量が本書と一致する。上段から繊維と種を二重に出力しない。野生生成では地下水条件を使用する。 |
| Cloche | 29作物が指定土壌と数量で処理され、二段作物の描画が崩れない。種と土壌を通常加工で消費しない。 |
| SqueezerとFermenter | 種子23種はSqueezerでPlant Oil80mBを出力する。食品とRaw HoneyはFermenterで指定量のEthanolを出力する。機械カテゴリを取り違えない。 |
| 腐敗条件 | 既存食品加工へ一律の腐敗除外を追加しない。Caramel用Butterの非腐敗条件だけを、そのレシピの定義どおり要求する。 |
| 木材 | 36樹種の実在入力から対応するLumberを出力する。刃なしではIEの剥皮・搬送だけを行い、Lumberを生成しない。Beneath木材を追加しない。 |
| 非金属と流体 | 全入力ID、流体タグ、数量、FE、温度、確率、FirmaLife条件が表と一致する。Milk Vinegarの導入時用と未導入時用を同時有効にしない。 |
| Slag | 対象のHigh Carbon Steel金床加工成功時だけ1個を出力する。加工途中、失敗、他の鋼種、別工程で追加しない。Inventoryが満杯でも消失させない。 |
| 構造 | 表の材料・数量・座標で形成し、指定Plated Blockを共通storage blockで代用しない。ガイドの形成表示と実際の判定を一致させる。 |
| 表示と制約 | `en_us`、JEI、両ガイドに採用数値と入手条件を反映する。Components Blueprint、固定Bucket、通常金床の非追加方針を、別の救済レシピで変更しない。 |

### 37. 参照版

本文の参照URLは次の固定版を基礎とする。上流の現在の既定ブランチが変化しても、数値の採用元を追跡するための識別子として保持する。表の版は実装検証の基準であり、過去の会話を参照するための識別子ではない。

| 対象 | 版または固定commit | 主な参照目的 |
|---|---|---|
| [TFIE](https://github.com/lau-mods/TFIE/tree/83644ef428f483b200a5403d559e37f8311c529e) | 0.0.3、`83644ef428f483b200a5403d559e37f8311c529e` | 栽培、既存連携、レシピ、Head、構造、設定の基準とする。 |
| [Immersive Engineering](https://github.com/BluSunrize/ImmersiveEngineering/tree/583a182549d284a35c813b55ac2a0d1fddcf945a) | 12.4.2-194、`583a182549d284a35c813b55ac2a0d1fddcf945a` | 機械の数量・費用・入力・処理・削除対象を確認する。 |
| [TerraFirmaCraft](https://github.com/TerraFirmaCraft/TerraFirmaCraft/tree/v4.2.16) | `v4.2.16` | 金属、温度、作物、金床、データ形式を確認する。 |
| [TFC Metallum Overhaul](https://github.com/lau-mods/TFC-metallum-overhaul/tree/v2.2.1) | `v2.2.1` | 金属42種、形状、Briquet、設定、工具用金属を確認する。 |
| [FirmaLife](https://github.com/eerussianguy/firmalife/tree/v3.0.16) | `v3.0.16` | Chromium・Stainless Steel、食材・乳・Vat・蜂蜜加工を確認する。 |
| [ArborFirmaCraft](https://github.com/Therighthon/ArborFirmaCraft/tree/d8d7ca315e6f3fda1ee9a740541c56db92b69cb3) | 2.1.3、`d8d7ca315e6f3fda1ee9a740541c56db92b69cb3` | 14樹種と、変更しないLatex加工の境界を確認する。 |
| [TFC Overdims](https://github.com/lau-mods/TFC-Overdims/tree/d01a4b088802a7dcac9f81b5d601ecf07a15ac62) | 0.1.5 Beta、`d01a4b088802a7dcac9f81b5d601ecf07a15ac62` | Crimson・WarpedのIDとLumber数量を確認する。 |
| [Advanced TFC Tech Unofficial](https://github.com/HermitOwO/AdvancedTFCTech/tree/911df3cb4821324c3565d0d0cc59ea14dadd2c52) | 3.3、`911df3cb4821324c3565d0d0cc59ea14dadd2c52` | Grist MillとPower Loomの構造・形成起点を確認する。 |
| [TFC-IE Crossover](https://github.com/NMagpie/TFC-IE-Crossover/tree/41eb5d40bdd9e97152cd400decce4c04f746b902) | 2.1.3、`41eb5d40bdd9e97152cd400decce4c04f746b902` | 継承レシピとの比較と、High Carbon SteelのSlag復元を確認する。 |
| [Ice and Fire TFC compat](https://github.com/lau-mods/IaFTFC/tree/c6bee8e29734515379e788620e6ec91401f71204) | 0.1.4、`c6bee8e29734515379e788620e6ec91401f71204` | 90%温度と通常Heat Componentの採用元を確認する。 |

本書をTFIE 0.1.0の仕様定義とする。機能の採否に関する未決事項は残さない。今後の仕様変更は、対象機能、変更前後の入出力・数量・条件、既存ワールドへの影響を明示した変更として扱う。

<!-- END TFIE_010_COMPLETE_SPEC -->

## Constraints

### harnessの適用版

本書は[modding-harness](https://github.com/lau-mods/modding-harness/tree/2bf8738b61927767bc15bfa5859ad9e3a6f53a81)のversion 0.2.0、commit `2bf8738b61927767bc15bfa5859ad9e3a6f53a81`にある[PROJECT.mdテンプレート](https://github.com/lau-mods/modding-harness/blob/2bf8738b61927767bc15bfa5859ad9e3a6f53a81/templates/PROJECT.md)と[仕様](https://github.com/lau-mods/modding-harness/blob/2bf8738b61927767bc15bfa5859ad9e3a6f53a81/docs/harness-impl.md)を基準とする。harness自体をTFIEの製品機能として改変しない。harnessの将来版へ移す場合は構文・工程の差分を確認し、TFIEの採用仕様を暗黙に変更しない。

Featureは機能単位で完成させ、ゲーム処理、GUIに関する処理、モデル、テクスチャを同一FeatureのACへ含める。1つのFeatureを複数milestoneへ分割しない。具体的なmilestone、依存関係、順序はharnessのplanで作成する。本書に架空のcheckpoint、Git commit、テスト結果、進捗を記録しない。

### 実装資料と判断の範囲

TFIEの公開リポジトリは`https://github.com/lau-mods/TFIE`とする。仕様比較の基準は0.0.3のcommit `83644ef428f483b200a5403d559e37f8311c529e`とし、新しい製品版を0.1.0とする。既存コードを参照・再利用する場合も、0.0.3に残る仕様と本書の変更を区別し、古い依存宣言、削除対象、機械種別の誤りを継承しない。Minecraft 1.21.1のソースとAPIを使用し、別Minecraft版の実装例を無検証で移植しない。

第37章の固定参照版は数値と挙動の照合に使用する。上流の既定ブランチのHEADを、この固定参照版と同一とみなさない。将来版への対応では実際に使用した版と差分を記録する。数値や対象集合は本書を採用値とし、上流の標準値と違うことだけを理由に元へ戻さない。通常API、公開拡張点、Mixin等の選択は対象版のコードで確認し、必要な範囲だけへ変更を限定する。

本書で定義したレシピとデータは原則としてJSON資源を使用し、既存のMinecraft・NeoForge・TFC・IEの機構を優先する。非消費Blueprint、予約、熱出力、動的Recycling無効化など、通常JSONだけでは仕様を満たさない接続点には必要なコードを実装する。Blueprintを通常の消費Ingredientにする、添加材数0で代用する、候補の登録順をmodeとみなす実装を行わない。

既存ソース、テクスチャ等を使用する場合は、その配布条件と必要な権利表示を維持する。参照用リポジトリのコードをTFIEの依存関係として追加する必要はない。とくにAFCとOverdimsの資料参照は、本番依存宣言への追加を意味しない。

### Code Rules

harnessが実装と修正へ適用する次の規則を維持する。以下は基準commitの[Code Rules](https://github.com/lau-mods/modding-harness/blob/2bf8738b61927767bc15bfa5859ad9e3a6f53a81/docs/harness-impl.md)に対応する。

| 対象 | 実装上の要求 |
|---|---|
| 実装範囲 | 対象ACと適用する共通要求を満たす。別Featureの未計画機能や新規製法を追加しない。 |
| 標準機構 | 独自コードより先にMinecraftとNeoForgeのレジストリ、JSON資源、既存基底クラスを使用する。 |
| JSON資源 | レシピ、Loot Table、タグ、モデル、言語ファイル等のJSONは`runData`で実行するData Providerから生成し、生成済み資源もcommitへ含める。手書きJSONだけを最終生成経路にしない。 |
| テクスチャ | `runData`でテクスチャを生成しない。テクスチャを新たに作成する場合は新しい形状を描かず、TFIE本体または依存Modの既存アイテムテクスチャの再利用、色変更、切り抜き、それらの組合せで作成する。 |
| 処理経路 | 1つの挙動に1つの直接的な経路を置き、小さいクラスと直接呼出しを使用する。2か所目の利用が生じるまでは値をその場へ記す。 |
| 抽象化 | 抽象化、helper、追加configは、実際に2つの呼出し箇所で必要とする場合だけ導入する。本書の4つの既存サーバー設定は製品仕様として維持する。 |
| 状態検証 | ゲームで生じる状態だけを検証し、MinecraftとNeoForgeの保証に依存する。データパック・外部操作により生じる複数Blueprint、不正mode、条件不成立等は本書の明示する実状態として扱う。 |
| 不要物 | 現在の挙動で使用しないコード、資源、コメントを除去する。廃止したBlock Mold、旧Beneath向け製材、重複レシピを残さない。 |
| 命名とコメント | 名前は処理内容を表し、コメントは現在の挙動の理由を説明する。実装しない将来機能のための抽象化や仮の資源を追加しない。 |

### 保存とデータの制約

サーバーをゲーム状態の決定主体とし、クライアントは表示と入力に必要な処理だけを担う。保存、同期、再読込の条件は詳細仕様第5・10・18・19・25・31・34章および各機械章を適用する。専用サーバーからクライアント専用クラスを読み込まない。

データパックの値を新規処理へ反映し、条件付きレシピの不存在を代替レシピで埋めない。登録IDやレシピ型の誤りをレシピ表示側だけで隠さない。削除と置換はRecipe Manager、機械の実処理、JEI、再読込、動的生成で整合させる。Arcの保持規則をClocheの乱数処理やSawmillの段階別排出へ広げない。

新規ワールドを導入条件とし、0.0.xの保存データ移行、旧登録IDの救済、Minecraft金床やBucket等の追加入手経路を設けない。製品の仕様変更を必要とする具体的な上流不適合を検出した場合は、対象版・コード位置・再現手順・影響を記録し、仕様を無断で緩めた状態を合格としない。

### 検証と受入

ゲーム内の状態、入出力、数量、FE、処理時間、温度、予約、保存、同期はNeoForge GameTestで判定する。描画、モデル、テクスチャ、GUIの文字、Tooltip、JEI、ガイドの画面はE2Eで撮影して判定する。E2Eのためにクライアント・サーバー起動処理をプロジェクト固有シナリオへ重複実装せず、harnessの共通lifecycleを使用する。クライアント専用の描画関数を専用サーバーGameTestへ読み込む方法を採らない。

すべてのACを`tests/acceptance.json`のGameTestまたはE2Eへ少なくとも1件対応付ける。1つのACに複数のtestcaseを対応付けてよい。各ACに指定した3欄の前提・操作・結果を検証する。全表行の検証は、本書から作成した独立の期待値と実行結果を比較する。実装側のレシピから期待値をそのまま読み出し、その値自身との一致だけを判定する検査を、入出力検証の代用にしない。

GameTestは指定のJUnit XML reportにtestcaseを出力し、failure・error・skippedが0であることを要求する。任意Modなしの構成で未実行になった試験を、その機能の検証成功として扱わない。必要な実行構成ごとに対応する試験を実施し、構成間の結果を保持する。必須依存不足による起動拒否、ビルド設定、Modメタデータ、配布依存の宣言は、ゲーム内判定に加えて起動ログ・成果物の検査を補助証拠として用いる。

基本構成、FirmaLifeのみ追加、Advanced TFC Tech Unofficialのみ追加、AFCのみ追加、Overdimsのみ追加、採用する任意連携を併用した構成、TFCMの金属・鉱石を無効化した構成を区別して確認する。全構成で無関係な全テストを繰り返す必要はないが、各条件分岐と相互作用を実際の有無に対応する構成で判定する。全数の最大構成における63金属、252形状レシピ、33Briquet系列、19Arc配合、7Alloy Kiln合金レシピ、9Head、29Cloche作物、23種子圧搾、36樹種144製材という対象集合を、該当章の有効条件と合わせて照合する。

描画ACは対象機能と同じmilestoneで完成させる。未登録アイテム、翻訳キーの露出、missing texture、数量・modeの誤表示、構造表示と形成判定の不一致を残さない。第32章のJEI、IE Manual、TFC Field Guideの確認も、実際のサーバー設定と読み込まれたレシピへ対応させる。

各milestoneは、実装、Compile、Build、Code Review、GameTest、対象がある場合のE2EとE2E Reviewを完了した後でharnessのcheckpointへ進める。実行中に`PROJECT.md`、plan、harness本体を変更しない。Code Rulesへの明確な違反とAC不適合を、理由のないaccepted判定で通過させない。

プロジェクトの完成には、全Featureの計画への割当、全milestoneのcheckpoint、全ACの実行済み成功、review issueの完了、`progress.json`のcomplete、最終checkpointがGit HEADであること、cleanな作業ツリーを要求する。仕様文書の検証成功だけをMod完成の根拠としない。[harnessの完成条件](https://github.com/lau-mods/modding-harness/blob/2bf8738b61927767bc15bfa5859ad9e3a6f53a81/docs/harness-impl.md)

## Open Questions

None.
