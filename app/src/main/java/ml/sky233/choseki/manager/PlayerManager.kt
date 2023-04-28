package ml.sky233.choseki.manager

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import ml.sky233.choseki.service.ChosekiPlayer
import ml.sky233.choseki.service.PlayerService
import java.io.InputStream

class PlayerManager {

    lateinit var binder: PlayerService.PlayerIBinder
    lateinit var mServiceConnection: ServiceConnection
    var isBinder = false

    companion object {
        val instance: PlayerManager = PlayerManager()
        var service: PlayerService? = null
    }

    fun init(str: String) {
        service?.init(str)
    }

    fun init(stream: InputStream) {
        service?.init(stream)
    }

    fun play() {
        service?.play()
    }

    fun pause() {
        service?.pause()
    }

    fun resume() {
        service?.resume()
    }

    fun release() {
        service?.release()
    }


    init {
        mServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, mBinder: IBinder) {
                binder = mBinder as PlayerService.PlayerIBinder
                service = binder.getService()
                isBinder = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                service = null
            }
        }
    }

}