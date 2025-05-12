# 🌐 Syntri

**Syntri** é um plugin modular e extensível para servidores Minecraft (1.8 até 1.21.x), desenvolvido em **Kotlin + Java**, com foco em desempenho, proteção e organização de funcionalidades.

---

## 🚀 Recursos Principais

- 🎯 **Sistema Anti-Dupe** inteligente para evitar duplicações via baús, shift-click e exploits comuns
- ✅ **Modularização por pacotes** (kits, scoreboard, restrições, utilidades, etc.)
- 💬 **Scoreboard RGB animado**, com suporte a PlaceholderAPI
- 🎒 **Kits com cooldown, permissões, armaduras, efeitos e meta NBT**
- 🔒 Bloqueios de: comandos, crafting, containers, ender pearls no border, camas, nether roof e mais
- 🌍 Comandos essenciais: `/kit`, `/tp`, `/warp`, `/home`, `/spawn`, `/ping`, etc.
- 📜 Código limpo, comentado e orientado a boas práticas

---

## 🛡️ Sistema Anti-Dupe (Proteção Contra Duplicações)

Syntri inclui um sistema anti-duplicação interno, que:

- Bloqueia uso indevido de **shift+click** em containers configurados
- Impede craftings de itens que causam bug em versões antigas (como `/minecraft:enchanted_golden_apple`)
- Cancela interações com inventários instáveis (como bigornas infinitas ou duplicação com mobs)
- Protege contra glitches com blocos como **baús**, **funis**, **droppers**, **baldes**, e containers em geral

O sistema funciona por eventos como:

- `InventoryClickEvent`
- `InventoryOpenEvent`
- `PrepareItemCraftEvent`
- `PlayerDropItemEvent`

Você pode personalizar tudo via `modules/general/config.yml`.

---

## 🔧 Instalação

1. Baixe o `Syntri.jar`
2. Coloque na pasta `/plugins`
3. Inicie o servidor
4. Configure os módulos em `/plugins/Syntri/modules/`

---

## ✅ Permissões

| Permissão            | Descrição                                                        |
|----------------------|------------------------------------------------------------------|
| `syntri.kit`         | Usar `/kit`                                                      |
| `syntri.createkit`   | Criar kits                                                       |
| `syntri.tp`          | Usar `/tp`                                                       |
| `syntri.spawn`       | Usar `/spawn`                                                    |
| `syntri.setspawn`    | Definir o spawn                                                  |
| `syntri.home`        | Usar `/home`                                                     |
| `syntri.sethome`     | Definir `/sethome`                                               |
| `syntri.warp`        | Usar `/warp`                                                     |
| `syntri.setwarp`     | Definir `/setwarp`                                               |
| `syntri.gm`          | Mudar o modo de jogo                                             |
| `syntri.reload`      | Recarregar configurações                                         |
| `syntri.chat`        | Usar o chat global (se houver restrições)                        |
| `syntri.dev`         | Acesso a ferramentas de debug                                    |

---

## 📂 Estrutura de Pastas

````yaml
/plugins/Syntri/
├── modules/
│ ├── general/
│ │ ├── config.yml
│ │ └── README.md
│ ├── kits/
│ │ ├── config.yml
│ │ └── README.md
│ ├── scoreboard/
│ │ └── config.yml
└── ...
````


---

## 🔗 Requisitos

- Java 17+
- Spigot ou Paper 1.8 até 1.21.x
- PlaceholderAPI (opcional, mas recomendado)

---

## 👨‍💻 Projeto

Desenvolvido com ❤️ por **Gabriel Martins**  
🔗 GitHub: [github.com/GabriellMartins](https://github.com/GabriellMartins)

---

## 📄 Licença

Distribuído sob a licença MIT.
