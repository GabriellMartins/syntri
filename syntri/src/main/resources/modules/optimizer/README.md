# Optimizer Module - Syntri

The **Optimizer** module is responsible for applying automatic performance improvements to your Minecraft server, keeping TPS high even with many players online.

## 🔧 Configuration (`config.yml`)

```yaml
optimizer:
  enabled: true

  # Smart unloading of inactive chunks
  chunk: true

  # Automatically removes dropped items (entities)
  entity: true

  # Limits the number of living entities per chunk
  chunk-entity-limit: true

  # Disables redstone that is active outside player view (Paper/Purpur only)
  redstone: true
```
✅ All optimizers are modular and can be toggled individually.

## 🚀 Available Optimizations
- chunk	Unloads chunks that are far from players to reduce memory and CPU usage.
- entity	Periodically removes dropped items (e.g., laggy item stacks).
- chunk-entity-limit	Limits the amount of mobs and entities per chunk to avoid overpopulation.
- redstone	Disables always-on redstone outside the range of players (requires Paper).
---