package ml.sky233.choseki.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import ml.sky233.choseki.application.MainApplication
import ml.sky233.choseki.manager.PlayerManager
import java.io.IOException
import java.io.InputStream


class PlayerService : Service() {
    lateinit var mChosekiPlayer: ChosekiPlayer
    private val mBinder: PlayerIBinder = PlayerIBinder()

    override fun onCreate() {
        super.onCreate()
    }
    override fun onDestroy() {
        unbindService(PlayerManager.instance.mServiceConnection)
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return mBinder
    }

    fun init(str: String) {
        if (this@PlayerService::mChosekiPlayer.isInitialized)
            mChosekiPlayer.release()
        mChosekiPlayer = ChosekiPlayer(str)
    }

    fun init(stream: InputStream) {
        if (this@PlayerService::mChosekiPlayer.isInitialized)
            mChosekiPlayer.release()
        mChosekiPlayer = ChosekiPlayer(stream)
    }

    fun play() {
        mChosekiPlayer.startPlay()
    }

    fun pause() {
        mChosekiPlayer.stopPlay()
    }

    fun resume() {
        mChosekiPlayer.startPlay()
    }

    fun release() {
        mChosekiPlayer.release()
    }

    inner class PlayerIBinder : Binder() {

        fun getService(): PlayerService {
            return this@PlayerService
        }
    }

}

