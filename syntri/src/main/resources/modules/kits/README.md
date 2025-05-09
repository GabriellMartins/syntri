# 🎁 Kit Module

This module manages server kits — predefined sets of items and armor that players can receive using commands.

Kits can have:
- Items and armor
- Permissions
- Cooldowns (daily, hourly, etc.)

---

## ✅ Enabling Kits

Kits are automatically loaded from:

modulos/kits/config.yml

## 🎒 Example Kit
````yaml
Starter:
  permission: syntri.kit.starter
  time: 1d
  items:
    item0:
      data: <base64>
    item1:
      data: <base64>
  armor:
    armor0:
      data: <base64>
````
- permission: required permission to use the kit (syntri.kit.starter)
- time: cooldown (ex: 1d = 1 day)
- items: inventory items
- armor: equipped armor (helmet, chestplate, etc.)

## ⏱ Cooldown Formats
You can use:

````yaml
10s = 10 seconds

5min = 5 minutes

1h = 1 hour

2d = 2 days

1m = 1 month

1sa = 1 week
````
## 🔑 Permissions
Each kit requires a permission.
````yaml
Example:
permission: syntri.kit.vip
````
Only players with this permission can use the kit.

## 🛠 Creating Kits In-Game
You can use commands (if implemented) like:
````yaml
/kit create <name> <permission> <cooldown>
````
This will:

Save your inventory and armor

Add the kit to kits/config.yml

Automatically serialize the items into base64

## ⚠️ Important Notes
Do not manually edit base64 item data unless you know what you're doing.

Items are saved from your current inventory using KitManager.

Make sure Vault and a permission plugin are installed to manage access.

---

