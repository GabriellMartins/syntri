package com.br.gabrielmartins.syntri.scoreboard.enum.gradiant

object GradientAnimationUtil {

    fun getAnimatedGradientTitle(
        text: String,
        colors: List<String>,
        step: Int
    ): String {
        if (colors.size < 2) return text

        val startIndex = step % colors.size
        val endIndex = (startIndex + 1) % colors.size

        val startColor = colors[startIndex]
        val endColor = colors[endIndex]

        return GradientUtil.generateGradient(text, startColor, endColor)
    }
}
