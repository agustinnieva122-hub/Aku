package com.tupaquete.aku

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnAccessibility: Button
    private lateinit var btnListen: Button
    private lateinit var tvLogs: TextView
    private var speechRecognizer: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Pedir permiso de micrófono si no está concedido
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.RECORD_AUDIO
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            registerForActivityResult(
                androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
            ) { granted ->
                AkuOrchestrator.log(
                    if (granted) "✅ Permiso de micrófono concedido."
                    else "⚠️ Permiso de micrófono DENEGADO."
                )
            }.launch(android.Manifest.permission.RECORD_AUDIO)
        }

        btnAccessibility = findViewById(R.id.btnAccessibility)
        btnListen = findViewById(R.id.btnListen)
        tvLogs = findViewById(R.id.tvLogs)

        AkuOrchestrator.setUiLogger { message ->
            runOnUiThread { tvLogs.append("\\n$message") }
        }
        btnAccessibility.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
        setupSpeechRecognizer()
        btnListen.setOnClickListener { startListening() }
    }

    private fun setupSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) { AkuOrchestrator.log("🎤 Escuchando...") }
                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {}
                    override fun onError(error: Int) { AkuOrchestrator.log("❌ Error de micrófono: $error") }
                    override fun onResults(results: Bundle?) {
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) AkuOrchestrator.processVoiceCommand(this@MainActivity, matches[0])
                    }
                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }
        } else AkuOrchestrator.log("⚠️ SpeechRecognizer no disponible en este dispositivo.")
    }

    private fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-AR")
        }
        speechRecognizer?.startListening(intent)
    }

    override fun onDestroy() {
        AkuOrchestrator.setUiLogger(null)
        speechRecognizer?.destroy()
        super.onDestroy()
    }
}
