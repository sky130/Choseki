package ml.sky233.choseki.listener

import ml.sky233.choseki.util.FileUtils
import org.wlf.filedownloader.FileDownloader
import org.wlf.filedownloader.listener.OnDetectBigUrlFileListener

class OnDetectBigUrlFileListener : OnDetectBigUrlFileListener {

    //我是傻逼,没看文档
    override fun onDetectNewDownloadFile(
        url: String, fileName: String, saveDir: String, fileSize: Long
    ) {
        // 如果有必要，可以改变文件名称fileName和下载保存的目录saveDir
        FileDownloader.createAndStart(url, FileUtils.getDataPath(), fileName,null)
    }

    override fun onDetectUrlFileExist(url: String) {
        // 继续下载，自动会断点续传（如果服务器无法支持断点续传将从头开始下载）
        FileDownloader.start(url)
    }

    override fun onDetectUrlFileFailed(
        url: String, failReason: OnDetectBigUrlFileListener.DetectBigUrlFileFailReason
    ) {
        // 探测一个网络文件失败了，具体查看failReason
    }
}
