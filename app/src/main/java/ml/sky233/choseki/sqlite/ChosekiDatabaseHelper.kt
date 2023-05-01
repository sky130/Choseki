package ml.sky233.choseki.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ChosekiDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Choseki.db"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS package (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, package_id INTEGER, description TEXT,version INTEGER, icon TEXT,isEnable INTEGER)")
        //创建名字为音源包的表,用于存储包名,特征码,版本,是否启用
        db?.execSQL("CREATE TABLE IF NOT EXISTS scene (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT,data TEXT, package_id INTEGER)")
        //创建名字为情景的表,用于存储情景名,描述,播放数据,归属音源包
        db?.execSQL("CREATE TABLE IF NOT EXISTS audio (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, size INTEGER,duration INTEGER, package_id INTEGER)")
        //创建名字为音源的表,用于存储音源名,音源数量(用于点状音源),音频频率(用于点状音源,值为0时为点状音源,可用于判断),归属包
        db?.execSQL("CREATE TABLE IF NOT EXISTS love (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,description TEXT,data TEXT,package_ids TEXT)")
        //创建名字为自建情景的表,用于情景名,描述,播放数据,涉及归属包
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO()
    }
}