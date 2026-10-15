package com.tupaquete.aku

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AkuAccessibilityService : AccessibilityService() {
    companion object {
        @Volatile var instance: AkuAccessibilityService? = null
            private set
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        AkuOrchestrator.log("Servicio de Accesibilidad CONECTADO.")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            event.packageName?.toString()?.let { AkuOrchestrator.notifyAppOpened(it) }
        }
    }

    override fun onInterrupt() { AkuOrchestrator.log("Servicio de Accesibilidad interrumpido.") }

    override fun onDestroy() {
        instance = null
        super.onDestroy()
    }

    fun performDynamicScroll(speed: Float) {
        val metrics = resources.displayMetrics
        val path = Path().apply {
            moveTo((metrics.widthPixels / 2).toFloat(), metrics.heightPixels * 0.8f)
            lineTo((metrics.widthPixels / 2).toFloat(), metrics.heightPixels * 0.2f)
        }
        val duration = (500L / speed.coerceIn(0.25f, 3.0f)).toLong().coerceIn(100L, 2000L)
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, duration))
            .build()
        dispatchGesture(gesture, null, null)
    }

    fun performSearchInCurrentApp(query: String) {
        val root = rootInActiveWindow ?: return
        val editable = findEditableNode(root)
        if (editable != null) {
            val args = Bundle().apply {
                putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_ARGUMENT, query)
            }
            editable.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, args)
            editable.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
        } else AkuOrchestrator.log("⚠️ No se encontró campo de texto interactivo.")
    }

    private fun findEditableNode(node: AccessibilityNodeInfo, depth: Int = 0): AccessibilityNodeInfo? {
        if (depth > 10) return null
        if (node.isEditable || node.className?.contains("EditText", ignoreCase = true) == true) return node
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            val result = findEditableNode(child, depth + 1)
            if (result != null) return result
        }
        return null
    }
}
