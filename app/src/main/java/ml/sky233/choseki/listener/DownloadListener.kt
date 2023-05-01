package ml.sky233.choseki.listener

import org.wlf.filedownloader.DownloadFileInfo
import org.wlf.filedownloader.file_download.base.HttpFailReason.*
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener

class DownloadListener : OnFileDownloadStatusListener {
    override fun onFileDownloadStatusWaiting(p0: DownloadFileInfo?) {}

    override fun onFileDownloadStatusPreparing(p0: DownloadFileInfo?) {}

    override fun onFileDownloadStatusPrepared(p0: DownloadFileInfo?) {}

    override fun onFileDownloadStatusPaused(p0: DownloadFileInfo?) {}

    override fun onFileDownloadStatusCompleted(downloadFileInfo: DownloadFileInfo) {

    }

    override fun onFileDownloadStatusDownloading(
        downloadFileInfo: DownloadFileInfo, downloadSpeed: Float, remainingTime: Long
    ) { // 正在下载，downloadSpeed为当前下载速度，单位KB/s，remainingTime为预估的剩余时间，单位秒

    }

    override fun onFileDownloadStatusFailed(
        url: String, //错误URL
        downloadFileInfo: DownloadFileInfo, failReason: OnFileDownloadStatusListener.FileDownloadStatusFailReason
    ) {
        when (failReason.type) {
            OnFileDownloadStatusListener.FileDownloadStatusFailReason.TYPE_URL_ILLEGAL -> {
                // 下载failUrl时出现url错误
            }

            OnFileDownloadStatusListener.FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL -> {
                // 下载failUrl时出现本地存储空间不足
            }

            TYPE_NETWORK_DENIED -> {
                // 下载failUrl时出现无法访问网络
            }

            TYPE_NETWORK_TIMEOUT -> {
                // 下载failUrl时出现连接超时
            }

            else -> {
                // 更多错误....
            }
        }

        val failCause = failReason.cause // 查看详细异常信息
        val failMsg = failReason.message // 查看异常描述信息
    }


}