package ml.sky233.choseki.application

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Environment
import ml.sky233.choseki.handler.CrashHandler
import ml.sky233.choseki.manager.PlayerManager
import ml.sky233.choseki.service.PlayerService
import org.wlf.filedownloader.FileDownloadConfiguration.Builder
import org.wlf.filedownloader.FileDownloader
import java.io.File


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
        initBuilder()
        initService()
    }

    private fun initBuilder() {
        FileDownloader.init(
            Builder(this).apply {
                configFileDownloadDir(Environment.getExternalStorageDirectory().absolutePath + File.separator + "download")
                configDownloadTaskSize(3)
                configRetryDownloadTimes(0)
                configDebugMode(false)
                configConnectTimeout(30000)
            }.build()
        )
    }

    private fun initService() {
        CrashHandler.instance.init(this)
        val intent = Intent(application, PlayerService::class.java)
        application.bindService(intent, PlayerManager.instance.mServiceConnection, BIND_AUTO_CREATE)
    }

}