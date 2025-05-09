object GradientUtil {
    fun generateGradient(text: String, startHex: String, endHex: String): String {
        if (text.isEmpty()) return ""
        val startColor = hexToRgb(startHex)
        val endColor = hexToRgb(endHex)
        val length = text.length
        val sb = StringBuilder()
        for (i in text.indices) {
            val ratio = i.toDouble() / (length - 1).coerceAtLeast(1)
            val r = interpolate(startColor[0], endColor[0], ratio)
            val g = interpolate(startColor[1], endColor[1], ratio)
            val b = interpolate(startColor[2], endColor[2], ratio)
            val hex = String.format("#%02X%02X%02X", r, g, b)
            sb.append(toLegacyHex(hex)).append(text[i])
        }
        return sb.toString()
    }

    private fun interpolate(start: Int, end: Int, ratio: Double): Int {
        return (start + (end - start) * ratio).toInt()
    }

    private fun hexToRgb(hex: String): IntArray {
        val clean = hex.replace("#", "")
        return intArrayOf(
            Integer.parseInt(clean.substring(0, 2), 16),
            Integer.parseInt(clean.substring(2, 4), 16),
            Integer.parseInt(clean.substring(4, 6), 16)
        )
    }

    private fun toLegacyHex(hex: String): String {
        val cleaned = hex.replace("#", "")
        return "§x" + cleaned.map { "§$it" }.joinToString("")
    }
}
