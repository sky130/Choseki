package ml.sky233.choseki.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import ml.sky233.choseki.handler.CrashHandler
import ml.sky233.choseki.manager.DownloadManager
import ml.sky233.choseki.manager.PlayerManager
import ml.sky233.choseki.service.PlayerService


@SuppressLint("StaticFieldLeak")
class MainApplication : Application() {

    companion object {
        lateinit var context: Context
        lateinit var application: MainApplication
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        application = this
        initService()
    }

    private fun initService() {
        CrashHandler.instance.init(this)
        DownloadManager.instance.init()
        val intent = Intent(application, PlayerService::class.java)
        application.bindService(intent, PlayerManager.instance.mServiceConnection, BIND_AUTO_CREATE)
    }

}