package ml.sky233.choseki.util

import com.google.gson.Gson
import ml.sky233.choseki.application.MainApplication.Companion.context
import ml.sky233.choseki.bean.PackageAudio
import ml.sky233.choseki.bean.PackageConfig
import ml.sky233.choseki.bean.PackageScene
import ml.sky233.choseki.sqlite.ChosekiDataGetter
import ml.sky233.choseki.sqlite.bean.*
import ml.sky233.choseki.sqlite.ChosekiDataPutter
import java.io.File

object PackageUtils {

    fun add(path: String, name: String) {
        val package_path = FileUtils.getPackagePath(name.split(".")[0])
        val putter = ChosekiDataPutter(context)

        ZipUtils.unZipFile(
            path, package_path
        )

        val config = Gson().fromJson(
            FileUtils.getFileText(package_path + File.separator + "config.json"),
            PackageConfig::class.java
        ).apply {
            putter.put(
                Package(
                    0, this.name, id, description, version, icon, true
                )
            )
        }

        ArrayList<PackageAudio>(
            Gson().fromJson(
                FileUtils.getFileText(package_path + File.separator + "audio" + File.separator + "audio.json"),
                Array<PackageAudio>::class.java
            ).toList()
        ).forEach { putter.put(Audio(0, it.name + "." + it.type, it.size, it.duration, config.id)) }

        val scene = File(package_path + File.separator + "scene").listFiles().apply {
            this?.forEach {
                Gson().fromJson(
                    FileUtils.getFileText(it.inputStream()), PackageScene::class.java
                ).apply {
                    putter.put(Scene(0, config.name, description, data.asJsonArray.toString(), config.id))
                }
            }
        }

        putter.close()
        ChosekiDataGetter.init(context)
    }


}