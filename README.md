# Syntri ⚙️

> O essencial do seu servidor.

O **Syntri** é um plugin modular e poderoso para servidores Minecraft. Ele oferece uma base robusta, fácil de configurar, com suporte a múltiplos bancos de dados, mensagens customizadas, integração com PlaceholderAPI, MOTD animado e muito mais.

---

## 📸 Banner

<p align="center">
  <img src="assets/banner.png" alt="Banner Syntri" width="600"/>
</p>

---

## ⚙️ Funcionalidades

- ✅ Suporte a múltiplos bancos: MySQL, SQLite, PostgreSQL, Oracle, MongoDB, Firebird, SQL Server
- ✅ Mensagens de boas-vindas e saída com PlaceholderAPI
- ✅ Detecção de idioma por IP (fallback para `"br"`)
- ✅ Sistema de MOTD animado
- ✅ Proteções configuráveis: dano, interação, clima, fome, comandos, inventário, etc.
- ✅ Sistema de tradução via arquivos YAML
- ✅ Sistema de kits com cooldown, permissões e itens
- ✅ Modular e expansível

---

## 🔧 Configuração Principal (`config.yml`)

```yaml
backend:
  type: "mysql"
  host: "198.89.99.179"
  port: 3306
  database: "s20_fuzy_shaw"
  username: "u20_LoZOofh8zE"
  password: "Ck.DmYnEvjtKo^+L!9oj.RGT"
  file: "database.db"
  uri: "mongodb://localhost"
  service: "XE"

general:
  language: "us"
  welcome-message:
    enabled: true
    text: "&aNome: %syntri_name% | Vida: %syntri_health% | Mundo: %syntri_world% | Ping: %syntri_ping% | Online: %syntri_online% | Banco: %syntri_backend%"
  quit-message:
    enabled: true
    text: "&e[Syntri] O jogador %player_name% saiu do servidor."

motd:
  enabled: true
  messages:
    - "&eBem-vindodasdasda"
    - "&aConfira nossos eventos!"
    - "&bDivirta-se jogando!"
  interval: 1
  animation: true

scoreboard:
  enabled: true
  title: "&e&lSYNTRI"
  lines:
    - "&7&m----------------------"
    - "&aNick: &f%player_name%"
    - "&aVida: &f%syntri_health%"
    - "&aPing: &f%syntri_ping%ms"
    - "&aOnline: &f%syntri_online%"
    - "&aBanco: &f%syntri_backend%"
    - "&7&m----------------------"```

---

## 🧰 Kits (`kits.yml`)

```yaml
starter:
  permission: "syntri.kit.starter"
  time: "10s"
  items:
    - ==: org.bukkit.inventory.ItemStack
      type: STONE_SWORD
    - ==: org.bukkit.inventory.ItemStack
      type: BREAD
      amount: 16
    - ==: org.bukkit.inventory.ItemStack
      type: LEATHER_HELMET

vip:
  permission: "syntri.kit.vip"
  time: "1d"
  items:
    - ==: org.bukkit.inventory.ItemStack
      type: DIAMOND_SWORD
    - ==: org.bukkit.inventory.ItemStack
      type: GOLDEN_APPLE
      amount: 5
    - ==: org.bukkit.inventory.ItemStack
      type: DIAMOND_CHESTPLATE

warrior:
  permission: "syntri.kit.warrior"
  time: "2sa"
  items:
    - ==: org.bukkit.inventory.ItemStack
      type: IRON_SWORD
    - ==: org.bukkit.inventory.ItemStack
      type: COOKED_BEEF
      amount: 20
```

---

## 📦 Instalação

1. Baixe o arquivo `.jar` compilado
2. Coloque em `plugins/`
3. Configure os arquivos `config.yml` e `kits.yml`
4. Reinicie o servidor
5. Aproveite!

---

## 📄 Licença

Esse projeto é licenciado sob a [MIT License](LICENSE).

---