# TFIE の E2E 環境

製品の仕様と受け入れ条件は `PROJECT.md`、Harness の接続設定は `.harness-config.json` に置く。E2E は Minecraft 1.21.1、NeoForge 21.1.267、Java 21 で実行する。

## ローカル環境の準備

リポジトリのルートで以下の環境変数を設定する。

```sh
export TFIE_RUNTIME="$PWD/.harness-state/runtime"
export MCT_HOME="$TFIE_RUNTIME/mct-home"
export MCT_CACHE_DIR="$TFIE_RUNTIME/mct-cache"
export MCT_SKILL_TARGETS=none
```

1. `git submodule update --init .harness`、`npm --prefix .harness ci`、`npm --prefix .harness run build` で Harness を準備する。
2. `npm install --prefix "$TFIE_RUNTIME/pilot" @kzheart_/mc-pilot@0.20.2` で MC Pilot を入れる。インストール先の `node_modules/@kzheart_/mc-pilot/data/variants.json` で `1.21.1-neoforge` の `neoforgeVersion` を `21.1.267` に合わせる。
3. NeoForge 21.1.267 の公式 installer を Java 21 で実行し、`--installServer "$TFIE_RUNTIME/server"` にサーバーを作る。Minecraft EULA を確認し、同意した環境で `server/eula.txt` を設定する。`server/server.properties` に `server-ip=127.0.0.1`、`server/user_jvm_args.txt` に `-Xmx3G` と `-Dneoforge.readTimeout=120` を各1行で設定する。
4. `"$TFIE_RUNTIME/pilot/node_modules/.bin/mct" client create tfie-a --version 1.21.1 --loader neoforge --account tfie_a --width 1280 --height 720 --java "$JAVA_HOME/bin/java"` で、CPU に合う Java 21 のクライアントを作る。loader の準備が途中で止まった場合は、同じ installer の `--installClient "$MCT_CACHE_DIR/client/runtime/1.21.1"` でキャッシュを完成させ、クライアントの作成を再実行する。`$MCT_HOME/clients/tfie-a/instance.json` の `launchArgs` で最大 heap を `3072m`、`env.JAVA_TOOL_OPTIONS` を `-Dneoforge.readTimeout=120` にする。
5. `$TFIE_RUNTIME/server/mods/` と `$MCT_HOME/clients/tfie-a/minecraft/mods/` の両方に TFC 4.2.16、Immersive Engineering 12.4.2-194、TFCM 2.2.1、Patchouli 1.21.1-93-NEOFORGE の配布 JAR を置く。クライアントには MC Pilot の操作用 Mod も必要。

## 検証

```sh
node .harness/dist/cli/main.js doctor
node .harness/dist/cli/main.js validate
node tests/e2e/runtime-smoke.mjs
```

スモークテストは専用ワールド `harness-world` を作り直し、`tfie-a` が `127.0.0.1:25595` に参加できることを確認する。出力された `smoke-*` ディレクトリにスクリーンショット `joined.png` とログを保存し、サーバーとクライアントを停止する。TFIE の機能別 E2E は Harness がビルドした製品 JAR を配備し、`tests/acceptance.json` に登録したシナリオで検証する。
