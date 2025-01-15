package com.profplay.knowledgebox.helper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.Locale
import android.Manifest
import android.speech.tts.UtteranceProgressListener

class TextToSpeechHelper(private val context: Context) {
    private var tts: TextToSpeech? = null
    private val messageQueue: MutableList<String> = mutableListOf()
    private var isSpeaking = false

    fun init(onInit: (Boolean) -> Unit) {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                val result = tts?.setLanguage(Locale("tr", "TR")) // Türkçe dil ayarı
                Log.e("TextToSpeechHelper", "Türkçe dili ayarlandı.")
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TextToSpeechHelper", "Türkçe dili desteklenmiyor veya veri eksik.")
                    tts?.language = Locale.getDefault()
                }
            } else {
                Log.e("TextToSpeechHelper", "TextToSpeech başlatılamadı.")
            }
        }
    }

    fun initialize() {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale.getDefault()
            }
        }
    }

    /*fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }*/

    fun speak(message: String, onComplete: () -> Unit) {
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) {
                onComplete() // TTS tamamlandığında callback çağrılır
            }
            override fun onError(utteranceId: String?) {}
        })
    }

    fun speakQueue(message: String) {
        messageQueue.add(message)
        if (!isSpeaking) {
            processQueue()
        }
    }

    private fun processQueue() {
        if (messageQueue.isNotEmpty()) {
            isSpeaking = true
            val message = messageQueue.removeAt(0)
            tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, message.hashCode().toString())

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    isSpeaking = false
                    processQueue()
                }

                override fun onError(utteranceId: String?) {
                    isSpeaking = false
                    processQueue()
                }
            })
        }
    }

    fun startSpeechRecognition(context: Context, onResult: (String) -> Unit, onError: () -> Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Mikrofon izni gerekli.", Toast.LENGTH_SHORT).show()
            return
        }

        Handler(Looper.getMainLooper()).post {
                if (SpeechRecognizer.isRecognitionAvailable(context)) {
                    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
                    val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR") // Türkçe dil desteği
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Cevabınızı söyleyin")
                    }

                    speechRecognizer.setRecognitionListener(object : RecognitionListener {
                        override fun onReadyForSpeech(params: Bundle?) {}
                        override fun onBeginningOfSpeech() {}
                        override fun onRmsChanged(rmsdB: Float) {}
                        override fun onBufferReceived(buffer: ByteArray?) {}
                        override fun onEndOfSpeech() {}

                        override fun onError(error: Int) {
                            Log.e("SpeechRecognizer", "Hata kodu: $error")
                            when (error) {
                                SpeechRecognizer.ERROR_NO_MATCH -> {
                                    Toast.makeText(context, "Anlaşılamadı, lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show()
                                }
                                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                                    Toast.makeText(context, "Mikrofon izni gerekli.", Toast.LENGTH_SHORT).show()
                                }
                                // Diğer hata kodları için benzer işlemler
                            }
                            onError()
                            onError()
                        }
                        override fun onResults(results: Bundle?) {
                            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                            if (!matches.isNullOrEmpty()) {
                                onResult(matches[0])
                            } else {
                                onError()
                            }
                        }
                        override fun onPartialResults(partialResults: Bundle?) {}
                        override fun onEvent(eventType: Int, params: Bundle?) {}
                    })
                    speechRecognizer.startListening(recognizerIntent)
                }
            }
    }

    fun stop() {
        tts?.stop()
    }

    fun release() {
        tts?.shutdown()
    }
}
