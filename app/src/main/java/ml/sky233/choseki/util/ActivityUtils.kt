package ml.sky233.choseki.util

import android.content.Context
import android.content.Intent

inline fun <reified T> startActivity(context: Context,block: Intent.()->Unit){
    val intent = Intent(context,T::class.java)
    intent.block()
    context.startActivity(intent)
}