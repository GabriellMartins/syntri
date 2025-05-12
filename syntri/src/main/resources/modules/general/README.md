# 🛡️ Syntri Protection System - Full README

A comprehensive protection and utility system for Minecraft servers. This plugin includes **blockers**, **disablers**, and **general utilities** to increase control, security, and performance.

> ✅ Fully configurable via `config.yml`
> ✅ Supports Minecraft **1.8.9 to 1.21.4**
> ✅ Includes **permission-based overrides**

---

## 🔒 BLOCKERS MODULE

### 📦 Container Access Blocker
Prevents players from opening specific container types (chests, hoppers, etc.). Useful for spawn zones or restricted areas.
- **Permission:** `syntri.bypass.containerbloqueado`

### ⛏️ Shift-Click in Containers Blocker
Prevents shift-clicking items into certain container types, helping to avoid item duplication or rapid transfers.
- **Permission:** `syntri.bypass.shiftemcontainer`

### 🛏️ Bed Use Blocker
Blocks players from using beds to sleep. Useful on PvP servers or during special events.
- **Permission:** `syntri.bypass.entrarnacama`

### 💬 Blocked Commands
Blocks execution of commands listed in configuration. Helps prevent abuse of commands like `/pl`, `/kill`, etc.
- **Permission:** `syntri.bypass.comandobloqueado`

### ⚒️ Crafting Blocker
Disables crafting of specific items by ID. Can be used to block things like TNT or enchanted golden apples.
- **Permission:** `syntri.bypass.craftbloqueado`

### 🏷️ Name Tag Usage Blocker
Blocks players from applying name tags to entities unless permitted.
- **Permission:** `syntri.bypass.usarnametag`

### 🚗 Vehicle Entry Blocker
Prevents entering minecarts, boats, and other vehicles. Great for arenas or regions with movement restrictions.
- **Permission:** `syntri.bypass.entraremveiculos`

### 🌍 World Border Pearl Blocker
Prevents Ender Pearls from bypassing the world border. Helps enforce world limits.
- **Permission:** *(Not applicable)*

### 🪧 Sign Text Filter
Blocks or filters sign text containing blacklisted words. Optionally disables all sign editing.
- **Permission:** `syntri.bypass.blockedsign`

### 📛 Banned Nicknames
Prevents players from joining if their name contains blocked words (e.g., inappropriate terms).
- **Permission:** *(Not applicable)*

### 🔁 Dual Login Prevention
Blocks login if the username is already online. Protects against duplicate connections and account abuse.
- **Permission:** *(Not applicable)*

### ☀️ Daylight Burn Protection
Prevents mobs like zombies and skeletons from burning under the sun. Useful for mob arena or special events.
- **Permission:** *(Not applicable)*

### 💥 Item Explosion Immunity
Prevents items on the ground from being destroyed by TNT or other explosions.
- **Permission:** *(Not applicable)*

### 🌀 Portal Teleport Blocker
Blocks automatic teleportation via Nether or End portals.
- **Permission:** `syntri.bypass.teleportarporportal`

---

## 🔧 DISABLERS MODULE

### ☀️ Weather Disabler
Cancels rain and thunderstorms on all worlds. Keeps the server sunny.

### ⏰ Daylight Cycle Disabler
Freezes the time and optionally sets a fixed time of day.

### 🔥 Blaze & Ghast Fireball Disabler
Cancels fireball projectiles launched by Blazes or Ghasts. Prevents griefing or spam.

### 🌵 Cactus Damage Disabler
Disables damage from cacti. Prevents mob farms or item destruction via cactus.

### 🐉 Dragon & Wither Block Breaking
Prevents Ender Dragons and Withers from breaking blocks.

### 🔥 Fire Spread Disabler
Prevents blocks from catching fire or spreading due to lava, lightning, or fire.

### 🌊 Fluid Vertical Flow Blocker
Disables water/lava vertical flow down cliffs. Good for anti-grief in builds.

### 🍗 Hunger Disabler
Keeps player hunger bar full. Ideal for hubs, minigames, or lobbies.

### ⚰️ Death Message Disabler
Hides death messages from chat.

### 🐾 Natural Mob Spawn Disabler
Disables mob spawning from natural causes (but not spawners).

### 🍂 Leaf Decay Disabler
Prevents leaf blocks from disappearing naturally.

### 🪨 Falling Blocks Disabler
Stops sand, gravel, and anvils from falling. Good for creative zones.

### 🚪 Dead Entity Portal Fix
Automatically removes dead entities that try to use a portal.

---

## ⚙️ GENERAL UTILITIES MODULE

### 🧠 Anvil Color Rename
Allows players to use `&` color codes in anvil renaming.
- **Permission:** `syntri.cornabigorna`

### 🪧 Sign Color Usage
Allows colored text using `&` codes when placing signs.
- **Permission:** `syntri.cornaplaca`

### 📍 Spawn on Join / Respawn
Teleports players to spawn (or VIP spawn) when they login or die.
- **Permission:** `syntri.spawn.vip` *(for VIP spawn)*

### 🕳️ Void Fall Protection
If a player falls into the void, they’re teleported back to spawn instead of dying.

### 🧱 Infinite Anvil Use
When interacting with an anvil, it opens a virtual menu instead. The anvil doesn't break.

### 🎯 EnderPearl Cooldown
Prevents players from spamming Ender Pearls by adding a cooldown.

### 🛑 Invalid Money Command Blocker
Blocks buggy economy commands like `/money pay -NaN`, `/market sell null`, etc.

### 🎮 First Join Commands
Executes a custom list of commands the first time a player joins the server.

### 💾 Keep XP on Death
Allows players to keep XP levels when they die.
- **Permission:** `syntri.manterxp`

### ⏳ Teleport Invincibility
Gives players short invincibility after teleporting via plugin/command.

### 👥 Player Limit Handler
Sets a max number of players on the server. If full, kicks a random non-VIP.
- **Permission:** `syntri.lotado.entrar`

---

## 🧩 Permissions Summary
| Feature | Permission |
|--------|------------|
| Bypass container open | `syntri.bypass.containerbloqueado` |
| Bypass shift move | `syntri.bypass.shiftemcontainer` |
| Sleep in beds | `syntri.bypass.entrarnacama` |
| Use blocked commands | `syntri.bypass.comandobloqueado` |
| Use name tags | `syntri.bypass.usarnametag` |
| Enter vehicles | `syntri.bypass.entraremveiculos` |
| Use anvil colors | `syntri.cornabigorna` |
| Use sign colors | `syntri.cornaplaca` |
| Enter full server | `syntri.lotado.entrar` |
| Use VIP spawn | `syntri.spawn.vip` |
| Keep XP | `syntri.manterxp` |
| Use portal when blocked | `syntri.bypass.teleportarporportal` |
| Bypass blocked signs | `syntri.bypass.blockedsign` |

---

Developed with ❤️ to give your server total control, protection and functionality.

> **Credits:** Most core concepts and event logic were inspired or adapted from the open-source repository by [rush](https://github.com/eduardo-mior/System). Huge thanks for the solid foundation and ideas!

Need help or customization? Open an issue or contact the author.