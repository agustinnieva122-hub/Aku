package com.tupaquete.aku

data class ParsedCommand(
    val app: AppTarget,
    val action: ActionType,
    val parameter: String? = null,
    val speed: Float = 1.0f
)

enum class ActionType { LAUNCH, SCROLL, SEARCH }

object CommandParser {
    private val HOTWORDS = listOf("aku", "acu", "akú")
    private val LAUNCH_VERBS = listOf("abre", "abrí", "abrime", "entra", "entrá", "entrate", "andá", "anda", "ve", "venite", "metete", "meté", "pone", "poneme", "lanzá", "lanzar", "dale", "abrir")
    private val SCROLL_VERBS = listOf("desliza", "deslizá", "desplaza", "desplazate", "desplazá", "scroll", "scrollea", "baja", "bajá", "sube", "subí", "mové", "move", "movete", "corre", "corré")
    private val SEARCH_VERBS = listOf("busca", "buscá", "buscame", "búscame", "buscar", "encontrá", "encontra", "encontrame")
    private val VERY_SLOW = listOf("muy despacio", "muy lento", "re lento", "re despacio")
    private val SLOW = listOf("despacio", "lento", "lentito", "suave", "tranquilo", "pausado", "tranqui", "sin apuro")
    private val FAST = listOf("rápido", "rapido", "rápidito", "rapidito", "veloz", "apura")
    private val VERY_FAST = listOf("muy rápido", "muy rapido", "re rápido", "apurado")
    private val NUMERIC_SPEED_REGEX = Regex("""(\d+(?:[.,]\d+)?)\s*x""")
    private val SEARCH_QUERY_REGEX = Regex("""(?:busca|buscá|buscame|búscame|buscar|encontrá|encontra|encontrame)\s+(?:a\s+)?(.*?)(?:\s+en\s+|\s+y\s+|\s+luego\s+|$)""", RegexOption.IGNORE_CASE)

    fun parse(rawText: String, context: AppTarget? = null): ParsedCommand? {
        val text = rawText.lowercase().trim()
        if (!text.containsAnyOf(HOTWORDS)) return null
        val explicitApp = AppTarget.findInText(text)
        val app = explicitApp ?: context ?: return null
        val action = when {
            text.containsAnyOf(SCROLL_VERBS) -> ActionType.SCROLL
            text.containsAnyOf(SEARCH_VERBS) -> ActionType.SEARCH
            text.containsAnyOf(LAUNCH_VERBS) -> ActionType.LAUNCH
            explicitApp != null -> ActionType.LAUNCH
            else -> return null
        }
        val parameter = if (action == ActionType.SEARCH) extractSearchQuery(text) else null
        val speed = if (action == ActionType.SCROLL) extractSpeed(text) else 1.0f
        return ParsedCommand(app, action, parameter, speed)
    }

    private fun String.containsAnyOf(words: List<String>) = words.any { contains(it) }

    private fun extractSpeed(text: String): Float {
        NUMERIC_SPEED_REGEX.find(text)?.groupValues?.getOrNull(1)?.replace(',', '.')?.toFloatOrNull()
            ?.let { return it.coerceIn(0.25f, 3.0f) }
        return when {
            text.containsAnyOf(VERY_SLOW) -> 0.25f
            text.containsAnyOf(SLOW) -> 0.5f
            text.containsAnyOf(VERY_FAST) -> 3.0f
            text.containsAnyOf(FAST) -> 2.0f
            else -> 1.0f
        }
    }

    private fun extractSearchQuery(text: String): String? =
        SEARCH_QUERY_REGEX.find(text)?.groupValues?.getOrNull(1)?.trim()?.takeIf { it.isNotBlank() }
}
