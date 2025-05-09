# 📊 Module: Scoreboard

This module manages the **sidebar scoreboard** that appears for all players in-game.

You can fully customize the title, the information lines, and even enable animations with RGB gradients. It also supports [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) for dynamic values.

---

## ✅ Enable or Disable the Scoreboard

Inside the `config.yml`:

```yaml
scoreboard:
  enabled: true
```
---

true = scoreboard is shown to all players.

false = disables the scoreboard entirely.

---

# 🎯 Scoreboard Title
You can use:

Standard color codes: &a, &b, &c, etc.

RGB hex codes (MC 1.16+): #00ffff

Gradient effects:

yaml
Copiar
Editar


```yaml
title: "<gradient:#00ffff:#0000ff>&lSyntri Server</gradient>"
```
⚠️ Gradients only work in the title, not in the lines.
---

# 🧾 Scoreboard Lines
Each line represents a row in the sidebar.

You can use:

Static text (e.g., "Welcome!")

Dynamic placeholders from PlaceholderAPI (e.g., %player_name%, %server_online%)

Empty space for spacing (just use " ")

```exemple
lines:
  - " "
  - "&fPlayer: &a%player_name%"
  - "&fPing: &b%player_ping% ms"
  - "&fWorld: &e%player_world%"
  - "&fOnline: &d%server_online%"
  - "&fBalance: &6%vault_eco_balance%"
  - " "
  - "<gradient:#ff00cc:#3333ff>&lplay.syntri.net</gradient>"
```
---

# 🌈 Title Animation
You can enable title animation and choose from predefined styles.

Available styles:

RAINBOW

FLAME

OCEAN

CYBER

CHROME

PINK_PURPLE

interval_ticks: time between each animation frame (20 ticks = 1 second)

💡 Tips
Use " " to insert empty lines.

Placeholders like %player_name%, %player_ping%, %server_online% require PlaceholderAPI.

Gradients and RGB require Minecraft 1.16 or higher.

Balance requires Vault and an economy plugin (e.g., EssentialsX).

📁 File Location
This file: modulos/scoreboard/README.md
Main config: modulos/scoreboard/config.yml

You can edit both files to adjust your scoreboard display and learn how everything works.

---
