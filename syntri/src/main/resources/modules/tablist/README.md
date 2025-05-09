# 🧾 Module: Tablist

This module controls the **TAB player list header and footer** shown when players press the `Tab` key in-game.

You can fully customize the top and bottom messages, include RGB colors, gradients, and even dynamic placeholders using [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/).

---

## ✅ Enable or Disable Tablist

Inside the `config.yml`:

```yaml
tablist:
  enabled: true
```
true = shows the custom tablist for all players.

false = disables the tablist feature.

#  Tablist Header & Footer
You can customize both sections using:

Standard color codes (e.g., &b, &e)

RGB hex codes (e.g., #00ffcc)

Gradients (in Minecraft 1.16+)

Placeholders (e.g., %player_name%, %server_tps%, %vault_eco_balance%)

````exemple
header: |
  <gradient:#00ffff:#0000ff>&lSyntri Network</gradient>
  &7Welcome, &a%player_name%&7!

footer: |
  &7There are &b%server_online% &7players online.
  &fPing: &a%player_ping% ms
  &fTPS: &2%server_tps%
  <gradient:#ff00cc:#3333ff>play.syntri.net</gradient>
````

ℹ️ You can use | after header: or footer: to write multiple lines (YAML multiline format).

💡 Tips
PlaceholderAPI is required to use %placeholders%.

Gradients and RGB colors only work on Minecraft 1.16+.

Use \n for line breaks if not using the | multiline format.

Use color codes: &a, &b, or #HEX / <gradient:...> for visual effects.

🔧 File Location
This file: modulos/tablist/README.md
Main config: modulos/tablist/config.yml

Modify the config to change the tablist appearance, and refer to this file anytime for help!

