package com.profplay.knowledgebox.helper

import android.content.Context
import android.media.SoundPool
import com.profplay.knowledgebox.R


class SoundEffectManager(context: Context) {

    private val soundPool = SoundPool.Builder().setMaxStreams(5).build()
    private val soundMap = mutableMapOf<String, Int>()

    init {
        soundMap["correct"] = soundPool.load(context, R.raw.correct_answer, 1)
        soundMap["wrong"] = soundPool.load(context, R.raw.wrong_answer, 1)
    }

    fun playSound(effect: String) {
        soundMap[effect]?.let { soundId ->
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun release() {
        soundPool.release()
    }
}
