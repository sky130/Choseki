package ml.sky233.choseki.manager

import ml.sky233.choseki.application.MainApplication
import ml.sky233.choseki.exception.WrongUrlException
import ml.sky233.choseki.util.FileUtils
import ml.sky233.choseki.util.TextUtils.isUrl
import org.wlf.filedownloader.FileDownloadConfiguration
import org.wlf.filedownloader.FileDownloader
import ml.sky233.choseki.listener.OnDetectBigUrlFileListener
import ml.sky233.choseki.listener.OnDeleteDownloadFileListener
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener.DetectBigUrlFileFailReason
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener


class DownloadManager {
    val TAG = "DownloadManager"

    companion object {
        val instance: DownloadManager = DownloadManager()
    }


    fun init(config: FileDownloadConfiguration) {
        FileDownloader.init(config)
    }

    fun download(url: String) {
        if (!url.isUrl()) {
            throw WrongUrlException(url)
        }
        FileDownloader.detect(url, OnDetectBigUrlFileListener())
    }

    fun delete(url:String){
        FileDownloader.delete(url, true, OnDeleteDownloadFileListener());
    }

    fun registerListener(listener: OnFileDownloadStatusListener) {
        FileDownloader.registerDownloadStatusListener(listener)
    }

    fun unregisterListener(listener: OnFileDownloadStatusListener) {
        FileDownloader.unregisterDownloadStatusListener(listener)
    }

    fun init() {
        FileDownloader.init(
            FileDownloadConfiguration.Builder(MainApplication.context).apply {
                configDebugMode(true)
                configFileDownloadDir(FileUtils.getDataPath())
                configDownloadTaskSize(1)
                configRetryDownloadTimes(0)
                configConnectTimeout(30000)
            }.build()
        )
    }
}