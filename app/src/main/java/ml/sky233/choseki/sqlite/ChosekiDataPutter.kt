package ml.sky233.choseki.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ml.sky233.choseki.sqlite.bean.Audio
import ml.sky233.choseki.sqlite.bean.Love
import ml.sky233.choseki.sqlite.bean.Package
import ml.sky233.choseki.sqlite.bean.Scene

class ChosekiDataPutter(context: Context) {
    val helper: ChosekiDatabaseHelper = ChosekiDatabaseHelper(context)
    val database: SQLiteDatabase = helper.writableDatabase

    fun put(audio: Audio) {
        if(audio in ChosekiDataGetter.audios)
            return
        val value = ContentValues().apply {
            put("name", audio.name)
            put("size", audio.size)
            put("duration", audio.duration)
            put("package_id", audio.packageId)
        }
        database.insert("audio", null, value)
    }

    fun put(love: Love) {
        if(love in ChosekiDataGetter.loves)
            return
        val value = ContentValues().apply {
            put("name", love.name)
            put("description", love.description)
            put("data", love.data)
            put("package_ids", love.packageIds.joinToString(","))
        }
        database.insert("love", null, value)
    }

    fun put(package_:Package){
        if(package_ in ChosekiDataGetter.packages)
            return
        val value = ContentValues().apply {
            put("name", package_.name)
            put("version", package_.version)
            put("description",package_.description)
            put("icon", package_.icon)
            put("package_id", package_.packageId)
            put("isEnable", package_.isEnable)
        }
        database.insert("package", null, value)
    }

    fun put(scene: Scene) {
        if(scene in ChosekiDataGetter.scenes)
            return
        val value = ContentValues().apply {
            put("name", scene.name)
            put("description", scene.description)
            put("data", scene.data)
            put("package_id", scene.packageId)
        }
        database.insert("scene", null, value)
    }

    fun close() {
        database.close()
        helper.close()
    }
}