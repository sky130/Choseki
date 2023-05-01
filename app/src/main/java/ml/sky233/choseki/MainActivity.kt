package ml.sky233.choseki

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ml.sky233.choseki.application.MainApplication.Companion.context
import ml.sky233.choseki.databinding.ActivityMainBinding
import ml.sky233.choseki.manager.DownloadManager
import ml.sky233.choseki.sqlite.ChosekiDataGetter
import ml.sky233.choseki.sqlite.ChosekiDataPutter
import ml.sky233.choseki.sqlite.ChosekiDatabaseHelper
import ml.sky233.choseki.util.FileUtils
import ml.sky233.choseki.util.PackageUtils
import ml.sky233.choseki.util.ZipUtils
import org.wlf.filedownloader.DownloadFileInfo
import org.wlf.filedownloader.base.Status.DOWNLOAD_STATUS_FILE_NOT_EXIST
import org.wlf.filedownloader.file_download.base.HttpFailReason
import org.wlf.filedownloader.file_download.http_downloader.HttpDownloader.HttpDownloadException.TYPE_ETAG_CHANGED
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener.FileDownloadStatusFailReason.TYPE_SAVE_FILE_NOT_EXIST
import java.io.File

class MainActivity : AppCompatActivity(), OnFileDownloadStatusListener {
    private lateinit var binding: ActivityMainBinding
    private val putter = ChosekiDataPutter(context)
    private val TAG = "MainActivity.Download"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        DownloadManager.instance.registerListener(this)
        binding.button.setOnClickListener {
//            ChosekiDataGetter.init(this)

            DownloadManager.instance.download("https://sky233.ml/update/Choseki/Choseki_package.zip")
        }
    }

    override fun onDestroy() {
        DownloadManager.instance.unregisterListener(this)
        super.onDestroy()
    }

    override fun onFileDownloadStatusWaiting(p0: DownloadFileInfo?) {}

    override fun onFileDownloadStatusPreparing(p0: DownloadFileInfo?) {}

    override fun onFileDownloadStatusPrepared(p0: DownloadFileInfo?) {}

    override fun onFileDownloadStatusPaused(p0: DownloadFileInfo?) {}

    override fun onFileDownloadStatusCompleted(downloadFileInfo: DownloadFileInfo) {
        if (downloadFileInfo.status == DOWNLOAD_STATUS_FILE_NOT_EXIST) {
            DownloadManager.instance.delete(downloadFileInfo.url)
            DownloadManager.instance.download(downloadFileInfo.url)
            Log.d(TAG, "重新下载")
            return
        }
        Log.d(TAG, "下载完成")
        PackageUtils.add(downloadFileInfo.filePath, downloadFileInfo.fileName)
    }

    override fun onFileDownloadStatusDownloading(
        downloadFileInfo: DownloadFileInfo, downloadSpeed: Float, remainingTime: Long
    ) { // 正在下载，downloadSpeed为当前下载速度，单位KB/s，remainingTime为预估的剩余时间，单位秒
        Log.d(TAG, downloadFileInfo.fileName)
        Log.d(TAG, downloadSpeed.toString())
        Log.d(TAG, remainingTime.toString())
    }

    override fun onFileDownloadStatusFailed(
        url: String, //错误URL
        downloadFileInfo: DownloadFileInfo,
        failReason: OnFileDownloadStatusListener.FileDownloadStatusFailReason
    ) {
        when (failReason.type) {
            OnFileDownloadStatusListener.FileDownloadStatusFailReason.TYPE_URL_ILLEGAL -> {
                // 下载failUrl时出现url错误
            }

            OnFileDownloadStatusListener.FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL -> {
                // 下载failUrl时出现本地存储空间不足
            }

            HttpFailReason.TYPE_NETWORK_DENIED -> {
                // 下载failUrl时出现无法访问网络
            }

            HttpFailReason.TYPE_NETWORK_TIMEOUT -> {
                // 下载failUrl时出现连接超时
            }

            TYPE_ETAG_CHANGED->{
                DownloadManager.instance.delete(downloadFileInfo.url)
                DownloadManager.instance.download(downloadFileInfo.url)
                Log.d(TAG, "重新下载")
            }


            else -> {
                // 更多错误....
            }
        }

        val failCause = failReason.cause // 查看详细异常信息
        val failMsg = failReason.message // 查看异常描述信息
        Log.d(TAG, failCause.toString())
        Log.d(TAG, failMsg.toString())
        Log.d(TAG, failReason.type)
    }

}