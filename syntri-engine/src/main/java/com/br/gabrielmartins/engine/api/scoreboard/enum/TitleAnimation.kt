package com.br.gabrielmartins.engine.api.scoreboard.enum

enum class TitleAnimation(val generator: (String, Int) -> String) {

    RAINBOW({ text, index ->
        val colors = listOf("#FF0000", "#FF7F00", "#FFFF00", "#00FF00", "#0000FF", "#4B0082", "#8F00FF")
        val from = colors[index % colors.size]
        val to = colors[(index + 1) % colors.size]
        GradientUtil.generateGradient(text, from, to)
    }),

    FLAME({ text, index ->
        val colors = listOf("#FF3300", "#FF6600", "#FF9900", "#FFCC00")
        val from = colors[index % colors.size]
        val to = colors[(index + 1) % colors.size]
        GradientUtil.generateGradient(text, from, to)
    }),

    OCEAN({ text, index ->
        val colors = listOf("#0066FF", "#00CCFF", "#00FFCC", "#00FF99", "#00CCFF")
        val from = colors[index % colors.size]
        val to = colors[(index + 1) % colors.size]
        GradientUtil.generateGradient(text, from, to)
    }),

    CYBER({ text, index ->
        val colors = listOf("#00FF00", "#00CC99", "#00FFFF", "#00CCFF", "#0099FF")
        val from = colors[index % colors.size]
        val to = colors[(index + 1) % colors.size]
        GradientUtil.generateGradient(text, from, to)
    }),

    CHROME({ text, index ->
        val colors = listOf("#CCCCCC", "#AAAAAA", "#888888", "#AAAAAA")
        val from = colors[index % colors.size]
        val to = colors[(index + 1) % colors.size]
        GradientUtil.generateGradient(text, from, to)
    }),

    PINK_PURPLE({ text, index ->
        val colors = listOf("#FF66CC", "#CC66FF", "#9966FF", "#CC66FF")
        val from = colors[index % colors.size]
        val to = colors[(index + 1) % colors.size]
        GradientUtil.generateGradient(text, from, to)
    });

    companion object {
        fun fromName(name: String?): TitleAnimation = entries.firstOrNull {
            it.name.equals(name, ignoreCase = true)
        } ?: RAINBOW
    }

    fun generateAnimated(text: String, index: Int): String {
        return generator.invoke(text, index)
    }

    fun generateNext(text: String, step: Int): String {
        return generator.invoke(text, step)
    }
}
