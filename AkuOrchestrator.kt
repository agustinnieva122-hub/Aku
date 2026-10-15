package com.tupaquete.aku

import android.content.Context
import android.content.Intent

object AkuOrchestrator {
    private var uiLogger: ((String) -> Unit)? = null
    private var activeAppTarget: AppTarget? = null

    fun setUiLogger(logger: ((String) -> Unit)?) { this.uiLogger = logger }
    fun log(message: String) { uiLogger?.invoke(message) }

    fun notifyAppOpened(packageName: String) {
        val matched = AppTarget.values().firstOrNull { it.packages.contains(packageName) }
        if (matched != null) {
            activeAppTarget = matched
            log("App activa detectada: ${matched.canonicalName}")
        }
    }

    fun processVoiceCommand(context: Context, rawText: String) {
        log("Voz capturada: \"$rawText\"")
        val command = CommandParser.parse(rawText, activeAppTarget)
        if (command == null) {
            log("❌ Comando no reconocido o sin hotword ('Aku').")
            return
        }
        log("✅ Parseado: App=${command.app.canonicalName}, Acción=${command.action}, Speed=${command.speed}x, Param=${command.parameter ?: "N/A"}")
        when (command.action) {
            ActionType.LAUNCH -> launchApp(context, command.app)
            ActionType.SCROLL -> performScroll(command.speed)
            ActionType.SEARCH -> performSearch(context, command.app, command.parameter)
        }
    }

    private fun launchApp(context: Context, target: AppTarget) {
        val pm = context.packageManager
        for (pkg in target.packages) {
            val intent = pm.getLaunchIntentForPackage(pkg)
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                log("🚀 Abriendo ${target.canonicalName} ($pkg)...")
                activeAppTarget = target
                return
            }
        }
        log("⚠️ No se encontró la app ${target.canonicalName} instalada.")
    }

    private fun performScroll(speed: Float) {
        val service = AkuAccessibilityService.instance
        if (service != null) {
            log("📜 Ejecutando scroll a ${speed}x...")
            service.performDynamicScroll(speed)
        } else log("⚠️ Servicio de Accesibilidad desactivado.")
    }

    private fun performSearch(context: Context, target: AppTarget, query: String?) {
        if (query.isNullOrEmpty()) {
            log("⚠️ Término de búsqueda vacío.")
            return
        }

        launchApp(context, target)
        log("🔍 Buscando \"$query\" en ${target.canonicalName}...")

        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            val service = AkuAccessibilityService.instance
            if (service != null) {
                service.performSearchInCurrentApp(query)
            } else {
                log("⚠️ Accesibilidad no activa para escribir la búsqueda.")
            }
        }, 1500L)
    }
}
