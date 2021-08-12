package com.example.smarteat.utils

import com.example.smarteat.models.Achievement
import com.example.smarteat.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

object AchievementChecker {
    interface AchievementListener {
        fun userGetAchievement(achievement: Achievement)
    }

    internal var user: User? = null
    internal var userDataFile: File? = null
    internal val listeners = ArrayList<AchievementListener>()
    internal val achievements = ArrayList<Achievement>()

    fun addListener(listener: AchievementListener) {
        listeners.add(listener)
    }

    private fun invokeAllListeners(achievement: Achievement) {
        for (listener in listeners)
            listener.userGetAchievement(achievement)
        if (userDataFile != null && user != null)
            CoroutineScope(Dispatchers.IO).launch {
                DataUpdater.updateAchievements(userDataFile!!, user!!)
            }
    }

    fun checkAchievements() {
        for (achieve in achievements)
            if (user?.achievements?.contains(achieve) == false && achieve.isComplete())
                invokeAllListeners(achieve)
    }
}