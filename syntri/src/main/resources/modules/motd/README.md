# 🖥 MOTD Module

This module controls the **MOTD (Message of the Day)** that appears in the Minecraft server list when players see your server.

---

## ✅ Enable or Disable

```yaml
motd:
  enabled: true
```

true = shows the MOTD when players see your server

false = disables the MOTD entirely

## 📝 MOTD Messages
yaml
Copiar
Editar
messages:
- "&eWelcome!"
- "&aCheck out our events!"
- "&bHave fun playing!"

You can write multiple messages. If animation is enabled, they will rotate one by one every few seconds.

## ⚙️ Other Settings
````yaml
interval: 1      # Time in seconds between each message
animation: true  # true = animated (cycle messages), false = static (first only)
````
interval: how often (in seconds) the MOTD changes

animation: if true, cycles through all messages

🎨 Colors
Use Minecraft color codes:

&a, &b, &c, etc.

Works in all Minecraft versions (no RGB or gradients needed)