package ml.sky233.choseki.service

import android.content.res.AssetManager
import android.media.MediaPlayer
import android.media.SoundPool
import com.google.gson.Gson
import ml.sky233.choseki.MainApplication.Companion.context
import ml.sky233.choseki.manager.ThreadPoolManager

class ChosekiPlayer(json: String) {
    private val mMediaPlayerList = ArrayList<MediaPlayer>()
    private val mSoundPool: SoundPool = SoundPool.Builder().build()
    private val mSoundPoolNames = ArrayList<PointAudioObject>()
    private val audioObjects = Gson().fromJson(json, Array<AudioObject>::class.java)
    private val assetManager: AssetManager = context.assets
    private var isStartPlaying = false
    private var isPlaying = false

    init {
        for (obj in audioObjects) {
            if (obj.isLineAudio) {
                mMediaPlayerList.add(MediaPlayer().apply {
                    val afd = assetManager.openFd("audio/${obj.audioName}.mp3")
                    this.setDataSource(
                        afd.fileDescriptor, afd.startOffset, afd.length
                    )
                    this.prepare()
                })
            } else if (obj.isPointAudio) {
                mSoundPoolNames.add(
                    PointAudioObject(
                        ArrayList<Int>().apply {
                            for (name in obj.names) {
                                val afd = assetManager.openFd("audio/${name}.mp3")
                                add(mSoundPool.load(afd, 1))
                            }
                        }, obj.frequency, obj.duration, obj.names[0]
                    )
                )
            }
        }
    }

    fun startPlay() {
        isPlaying = true
        if (!isStartPlaying) {
            mMediaPlayerList.edit {
                isLooping = true
                start()
            }
            for (obj in mSoundPoolNames) {
                ThreadPoolManager.getInstance().addTask(obj.TAG) {
                    soundPoolPlay(obj)
                }
            }
            isStartPlaying = true
        } else {
            mMediaPlayerList.edit {
                setVolume(1f, 1f)
                start()
            }
            for (obj in mSoundPoolNames) {
                for (id in obj.ids) {
                    mSoundPool.setVolume(id, 1f, 1f)
                }
            }
        }
    }

    private fun soundPoolPlay(obj: PointAudioObject) {
        while (true)
            for (o in obj.ids) {
                if (isPlaying)
                    mSoundPool.play(o, 1.0f, 1.0f, 0, 0, 1.0f)
                Thread.sleep(obj.duration.toLong())
            }
    }

    fun stopPlay() {
        isPlaying = false
        mMediaPlayerList.edit {
            setVolume(0f, 0f)
            pause()
        }
        for (obj in mSoundPoolNames) {
            for (id in obj.ids) {
                mSoundPool.setVolume(id, 0f, 0f)
            }
        }
    }

    fun release() {
        ThreadPoolManager.getInstance().exitThreadPool()
        mMediaPlayerList.edit {
            release()
        }//释放资源
        mSoundPool.release()
    }

    private infix fun ArrayList<MediaPlayer>.edit(block: MediaPlayer.() -> Unit) {
        for (mediaPlayer in this) {
            mediaPlayer.block()
        }
    }

}

data class PointAudioObject(
    val ids: ArrayList<Int>,
    val frequency: Int,
    val duration: Int,
    val TAG: String,//线程池标签
)

data class AudioObject(
    val audioName: String?,//音频名,当为线音频的时候才存在
    val duration: Int,//播放间隔,仅在点音频时使用,线音频为0
    val isLineAudio: Boolean,//判断是否是线音频
    val isPointAudio: Boolean,//判断是否是点音频
    val isRawRes: Boolean,//判断是不是Raw里面的音频
    val startPlayTime: Int,//开始播放时间
    val totalEndTime: Int,//结束播放时间
    val frequency: Int,//频率
    val names: List<String>,//仅在点音频时使用
    val volume: Double,//声音大小
)