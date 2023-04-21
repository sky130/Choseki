package ml.sky233.choseki.util

import android.content.Context
import android.net.Uri
import android.util.Log
import ml.sky233.suiteki.MainApplication.TAG
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.zip.ZipInputStream

object FileUtils {
    var mi_health_path =
        "/storage/emulated/0/Android/data/com.mi.health/files/log/XiaomiFit.device.log"
    var mi_wearable_path =
        "/storage/emulated/0/Android/data/com.xiaomi.wearable/files/log/Wearable.log"
    var uri_1 =
        "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3AAndroid%2Fdata%2Fcom.mi.health%2F"

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun startFor(activity: Activity, uri: Uri?) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.setFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or
                    Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        )
            .putExtra("android.provider.extra.SHOW_ADVANCED", true)
            .putExtra("android.content.extra.SHOW_ADVANCED", true)
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
        activity.startActivityForResult(intent, 1) //开始授权
    }

    fun buildUri(path: String): Uri {
        return DocumentsContract.buildDocumentUri(
            "com.android.externalstorage.documents",
            "primary:Android/data/$path"
        )
    }

    fun buildUri2(path: String): Uri {
        return DocumentsContract.buildDocumentUri(
            "com.android.externalstorage.documents",
            "primary:Android/data/" + path.replace("/storage/emulated/0/Android/data/", "")
        )
    }

    fun deleteDirWihtFile(name: String?) {
        val dir = File(name)
        if (dir == null || !dir.exists() || !dir.isDirectory) return
        for (file in dir.listFiles()) if (file.isFile) file.delete() // 删除所有文件
        else if (file.isDirectory) deleteDirWihtFile(file.path) // 递规的方式删除文件夹
        dir.delete() // 删除目录本身
    }

    fun getDocumentFile(context: Context?, path: String): DocumentFile {
        var path = path
        if (path.endsWith("/")) path = path.substring(0, path.length - 1)
        val path2 = path.replace("/storage/emulated/0/", "").replace("/", "%2F")
        return DocumentFile.fromSingleUri(
            context,
            Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A$path2")
        )
    }

    //    public static Uri changeToUri(String path) {
    //        path = path.replace("/storage/emulated/0/", "").replace("/", "%2F");
    ////        return Uri.parse("content://com.android.externalstorage.documents/tree/primary%3A" + path);
    ////        content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fcom.mi.health%2Ffiles%2Flog%2FXiaomiFit.device.log
    ////        content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fcom.mi.health
    ////        content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata%2Fcom.mi.health%2Ffiles%2Flog%2FXiaomiFit.device.log
    ////        content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fcom.mi.health%2Ffiles%2Flog%2FXiaomiFit.device.log
    ////        content://com.android.externalstorage.documents/document/primary%3AAndroid%2Fdata%2Fcom.mi.health%2Ffiles%2Flog%2FXiaomiFit.device.log
    //        return Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A" + path);
    //    }
    fun changeToUri(path: String): Uri {
        var path = path
        path = path.replace("/storage/emulated/0/Android/data/", "")
        return DocumentsContract.buildDocumentUri(
            "com.android.externalstorage.documents",
            "primary:Android/data/$path"
        )
    }

    fun changeToUri2(path: String): Uri {
        var path = path
        path = path.replace("/storage/emulated/0/", "")
        return DocumentsContract.buildDocumentUri(
            "com.android.externalstorage.documents",
            "primary:$path"
        )
    }

    fun getLogText(context: Context): String {
        return try {
            if (SettingUtils.getValue("read_mode")) {
                getInputText(context, chooseUri(context))
            } else getFileText(choosePath(context))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    val logTextZip: String
        get() = try {
            if (File("/data/data/ml.sky233.suiteki/log_data/Wearable.log").exists()) getFileText("/data/data/ml.sky233.suiteki/log_data/Wearable.log") else if (File(
                    "/data/data/ml.sky233.suiteki/log_data/XiaomiFit.device.log"
                ).exists()
            ) getFileText("/data/data/ml.sky233.suiteki/log_data/XiaomiFit.device.log") else ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }

    @SuppressLint("Recycle")
    fun getInputText(context: Context, uri: Uri?): String {
        var res = ""
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri!!)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            val fin = inputStream as FileInputStream?
            val length = fin!!.available()
            val buffer = ByteArray(length)
            fin.read(buffer)
            res = String(buffer, 0, length, StandardCharsets.UTF_8)
            fin.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return res
    }

    fun isDocumentFile(path: String, context: Context?): Boolean {
        var path = path
        path = path.replace("/storage/emulated/0/", "").replace("/", "%2F")
        val documentFile: DocumentFile = DocumentFile.fromSingleUri(
            context, Uri.parse(
                "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A$path"
            )
        )
        return documentFile.exists()
    }

    fun isFile(path: String?): Boolean {
        return File(path).exists()
    }

    fun deleteDocumentFile(path: String, context: Context?): Boolean {
        var path = path
        path = path.replace("/storage/emulated/0/", "").replace("/", "%2F")
        val documentFile: DocumentFile = DocumentFile.fromSingleUri(
            context, Uri.parse(
                "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata/document/primary%3A$path"
            )
        )
        return if (documentFile.exists()) documentFile.delete() else true
    }

    fun fileIsExist(fileName: String?): Boolean {
        //传入指定的路径，然后判断路径是否存在
        val file = File(fileName)
        return if (file.exists()) true else {
            //file.mkdirs() 创建文件夹的意思
            file.mkdirs()
        }
    }

    fun saveBitmap(name: String?, bm: Bitmap, mContext: Context?) {
        //判断指定文件夹的路径是否存在
        val TargetPath = "/sdcard/" + "/images/"
        if (!fileIsExist(TargetPath)) {
            Log.d("Save Bitmap", "TargetPath isn't exist")
        } else {
            //如果指定文件夹创建成功，那么我们则需要进行图片存储操作
            val saveFile = File(TargetPath, name)
            try {
                val saveImgOut = FileOutputStream(saveFile)
                // compress - 压缩的意思
                bm.compress(Bitmap.CompressFormat.JPEG, 80, saveImgOut)
                //存储完成后需要清除相关的进程
                saveImgOut.flush()
                saveImgOut.close()
                Log.d("Save Bitmap", "The picture is save to your phone!")
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }

    fun getFileBytes(fileName: String?): ByteArray? {
        if (File(fileName).exists()) try {
            val fin = FileInputStream(fileName)
            val length = fin.available()
            val buffer = ByteArray(length)
            fin.read(buffer)
            fin.close()
            return buffer
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getFileBytes(inputStream: InputStream): ByteArray? {
        try {
            val length = inputStream.available()
            val buffer = ByteArray(length)
            inputStream.read(buffer)
            inputStream.close()
            return buffer
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun writeFileBytes(filename: String?, bytes: ByteArray): ByteArray {
        val file = File(filename)
        try {
            if (!file.isFile) file.createNewFile()
            val fos = FileOutputStream(file)
            fos.write(bytes)
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bytes
        //        return null;
    }

    fun getFileText(filename: String?): String {
        var res = ""
        val file = File(filename)
        Log.d(TAG, filename!!)
        try {
            if (!file.exists()) {
                file.createNewFile()
                return res
            }
            val fin = FileInputStream(filename)
            val length = fin.available()
            val buffer = ByteArray(length)
            fin.read(buffer)
            res = String(buffer, 0, length, "UTF-8")
            fin.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return res
    }

    fun writeFileText(filename: String?, text: String): String {
        val file = File(filename)
        try {
            if (!file.exists()) file.createNewFile()
            val fos = FileOutputStream(filename)
            fos.write(text.toByteArray())
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return text
    }

    fun getFileText(file: InputStream): String {
        var res = ""
        try {
            val length = file.available()
            val buffer = ByteArray(length)
            file.read(buffer)
            res = String(buffer, "UTF-8")
            file.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return res
        }
        return res
    }

    val newLogZip: String
        get() {
            val num: LongArray
            val file = File("/storage/emulated/0/Download/wearablelog/")
            val files = file.listFiles { file1: File -> file1.name.endsWith("log.zip") }
                ?: return ""
            num = LongArray(files.size)
            for (i in files.indices) num[i] =
                TextUtils.getTheText("Suiteki:" + files[i].name, "Suiteki:", "log.zip").get(0)
                    .toLong()
            var name: Long = 0
            for (i in num.indices) if (name < num[i]) name = num[i]
            return if (name != 0L) file.path + "/" + name.toString() + "log.zip" else ""
        }

    fun isPath(path: String): Boolean {
        return if (Build.VERSION.SDK_INT >= 30) {
            setValue("read_mode", true)
            isDocumentFile(path, MainApplication.application)
        } else isFile(path)
    }

    fun choosePath(context: Context): String {
        return when (SettingUtils.getString("app_mode")) {
            "auto" -> {
                if (isInstalled(
                        "com.mi.health",
                        context
                    ) && isPath(mi_health_path)
                ) return mi_health_path
                if (isInstalled(
                        "com.xiaomi.wearable",
                        context
                    ) && isPath(mi_wearable_path)
                ) mi_wearable_path else ""
            }

            "mi_health" -> mi_health_path
            "mi_wearable" -> mi_wearable_path
            else -> ""
        }
    }

    fun isGetPermission(context: Context, pn: String?): Boolean {
        val pList: List<UriPermission> = context.contentResolver.persistedUriPermissions
        for (uriPermission in pList) {
            if (TextUtils.lookFor(uriPermission.getUri().getPath(), pn)) return true
        }
        return false
    }

    fun chooseUri(context: Context): Uri? {
        var mi_health: DocumentFile? = null
        var mi_wearable: DocumentFile? = null
        val pList: List<UriPermission> = context.contentResolver.persistedUriPermissions
        if (Build.VERSION.SDK_INT < 33) {
            for (i in pList.indices) {
                val up: UriPermission = pList[i]
                if (TextUtils.lookFor(up.getUri().getPath(), "Android/data")) {
                    try { //小米运动健康
                        val df: DocumentFile =
                            DocumentFile.fromTreeUri(context, up.getUri()).findFile("com.mi.health")
                                .findFile("files").findFile("log").findFile("XiaomiFit.device.log")
                        if (df.isFile()) mi_health = df
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                    try {
                        val df: DocumentFile = DocumentFile.fromTreeUri(context, up.getUri())
                            .findFile("com.xiaomi.wearable").findFile("files").findFile("log")
                            .findFile("Wearable.log")
                        if (df.isFile()) mi_wearable = df
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }
        } else if (Build.VERSION.SDK_INT >= 33) {
            for (i in pList.indices) {
                val up: UriPermission = pList[i]
                if (TextUtils.lookFor(up.getUri().getPath(), "com.mi.health")) {
                    try {
                        val df: DocumentFile =
                            DocumentFile.fromTreeUri(context, up.getUri()).findFile("files")
                                .findFile("log").findFile("XiaomiFit.device.log")
                        if (df.isFile()) mi_health = df
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
                if (TextUtils.lookFor(up.getUri().getPath(), "com.xiaomi.wearable")) {
                    try {
                        val df: DocumentFile =
                            DocumentFile.fromTreeUri(context, up.getUri()).findFile("files")
                                .findFile("log").findFile("Wearable.log")
                        if (df.isFile()) mi_wearable = df
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return when (SettingUtils.getString("app_mode")) {
            "auto" -> {
                if (mi_health != null) if (isInstalled(
                        "com.mi.health",
                        context
                    ) && mi_health.isFile()
                ) return mi_health.getUri()
                if (mi_wearable != null) if (isInstalled(
                        "com.xiaomi.wearable",
                        context
                    ) && mi_wearable.isFile()
                ) return mi_wearable.getUri()
                null
            }

            "mi_health" -> mi_health.getUri()
            "mi_wearable" -> mi_wearable.getUri()
            else -> null
        }
    }

    fun getInstallStatus(context: Context): Map<String, Boolean> {
        val map: MutableMap<String, Boolean> = HashMap()
        map["MiWearable"] = isInstalled("com.xiaom.wearable", context)
        map["MiHealth"] = isInstalled("com.mi.health", context)
        return map
    }

    fun isInstalled(packageName: String?, context: Context): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName!!, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    fun unzipFile(zipPath: String?): Boolean {
        val tempFileName: String = data_path + "/log_data/"
        return try {
            var file = File(tempFileName)
            if (!file.exists()) file.mkdirs()
            val inputStream: InputStream = FileInputStream(zipPath)
            val zipInputStream = ZipInputStream(inputStream)
            var zipEntry = zipInputStream.nextEntry
            val buffer = ByteArray(1024 * 1024)
            var count = 0
            while (zipEntry != null) {
                if (!zipEntry.isDirectory) {
                    var fileName = zipEntry.name
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1)
                    file = File(tempFileName + File.separator + fileName)
                    file.createNewFile()
                    val fileOutputStream = FileOutputStream(file)
                    while (zipInputStream.read(buffer)
                            .also { count = it } > 0
                    ) fileOutputStream.write(buffer, 0, count)
                    fileOutputStream.close()
                }
                zipEntry = zipInputStream.nextEntry
            }
            zipInputStream.close()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}