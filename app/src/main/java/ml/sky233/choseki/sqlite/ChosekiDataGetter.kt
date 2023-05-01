package ml.sky233.choseki.sqlite

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ml.sky233.choseki.sqlite.ChosekiDataGetter.initAudio
import ml.sky233.choseki.sqlite.ChosekiDataGetter.initLove
import ml.sky233.choseki.sqlite.ChosekiDataGetter.initPackage
import ml.sky233.choseki.sqlite.ChosekiDataGetter.initScene
import ml.sky233.choseki.sqlite.bean.Audio
import ml.sky233.choseki.sqlite.bean.Love
import ml.sky233.choseki.sqlite.bean.Package
import ml.sky233.choseki.sqlite.bean.Scene

object ChosekiDataGetter {


    @JvmStatic
    val packages: ArrayList<Package> = ArrayList()

    @JvmStatic
    val scenes: ArrayList<Scene> = ArrayList()

    @JvmStatic
    val audios: ArrayList<Audio> = ArrayList()

    @JvmStatic
    val loves: ArrayList<Love> = ArrayList()

    fun init(context: Context) {
        ChosekiDatabaseHelper(context).apply {
            this.readableDatabase.apply {
                initPackage()
                initAudio()
                initScene()
                initLove()
                this.close()
            }
            close()
        }
    }

    @SuppressLint("Recycle", "Range")
    fun SQLiteDatabase.initPackage() {
        val query = "SELECT * FROM package"
        val cursor = this.rawQuery(query, null)
        packages.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val packageId = cursor.getInt(cursor.getColumnIndex("package_id"))
            val version = cursor.getInt(cursor.getColumnIndex("version"))
            val icon = cursor.getString(cursor.getColumnIndex("icon"))
            val isEnable = cursor.getInt(cursor.getColumnIndex("isEnable")) != 0
            packages.add(Package(id, name, packageId, version, icon, isEnable))
        }
    }

    @SuppressLint("Recycle", "Range")
    fun SQLiteDatabase.initScene() {
        val query = "SELECT * FROM scene"
        val cursor = this.rawQuery(query, null)
        scenes.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val description = cursor.getString(cursor.getColumnIndex("description"))
            val data = cursor.getString(cursor.getColumnIndex("data"))
            val packageId = cursor.getInt(cursor.getColumnIndex("package_id"))
            scenes.add(Scene(id, name, description, data, packageId))
        }
    }

    @SuppressLint("Recycle", "Range")
    fun SQLiteDatabase.initAudio() {
        val query = "SELECT * FROM audio"
        val cursor = this.rawQuery(query, null)
        audios.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val size = cursor.getInt(cursor.getColumnIndex("size"))
            val duration = cursor.getInt(cursor.getColumnIndex("duration"))
            val packageId = cursor.getInt(cursor.getColumnIndex("package_id"))
            audios.add(Audio(id, name, size, duration, packageId))
        }
    }

    @SuppressLint("Recycle", "Range")
    fun SQLiteDatabase.initLove() {
        val query = "SELECT * FROM love"
        val cursor = this.rawQuery(query, null)
        loves.clear()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            val description = cursor.getString(cursor.getColumnIndex("description"))
            val data = cursor.getString(cursor.getColumnIndex("data"))
            val packageIds = ArrayList(cursor.getString(cursor.getColumnIndex("package_ids"))
                .split(",")
                .map { it.toInt() })
            loves.add(Love(id, name, description, data, packageIds))
        }
    }

}