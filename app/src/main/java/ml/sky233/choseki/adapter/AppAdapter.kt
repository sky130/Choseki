package ml.sky233.choseki.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ml.sky233.choseki.R
import ml.sky233.choseki.bean.AppObject
import ml.sky233.choseki.util.ViewUtils
import ml.sky233.choseki.widget.RecyclerExtras

class AppAdapter(var mContext: Context, private val mPublicArray: ArrayList<AppObject>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), RecyclerExtras.OnItemClickListener,
    RecyclerExtras.OnItemLongClickListener, RecyclerExtras.OnItemTouchListener {
    // 获取列表项的个数
    override fun getItemCount(): Int {
        return mPublicArray.size
    }

    // 创建列表项的视图持有者
    override fun onCreateViewHolder(vg: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // 根据布局文件item_linear.xml生成视图对象
        val v = LayoutInflater.from(mContext).inflate(R.layout.item_app, vg, false)
        return ItemHolder(v)
    }

    // 绑定列表项的视图持有者
    @SuppressLint("RecyclerView", "ClickableViewAccessibility")
    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        val holder = vh as ItemHolder
        ViewUtils.addTouchScale(holder.itemView)
        holder.tv_title.text = mPublicArray[position].title
        holder.iv_img.setImageResource(mPublicArray[position].icon)
        holder.itemView.setOnClickListener { v: View? ->
            if (mOnItemClickListener != null) {
                mOnItemClickListener!!.onItemClick(v, position)
            }
        }
        holder.itemView.setOnLongClickListener { v: View? ->
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener!!.onItemLongClick(v, position)
            }
            true
        }
    }

    // 获取列表项的类型
    override fun getItemViewType(position: Int): Int {
        // 这里返回每项的类型，开发者可自定义头部类型与一般类型，
        // 然后在onCreateViewHolder方法中根据类型加载不同的布局，从而实现带头部的网格布局
        return 0
    }

    // 获取列表项的编号
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // 定义列表项的视图持有者
    inner class ItemHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tv_title // 声明列表项标题的文本视图
                : TextView
        var iv_img // 声明列表项描述的文本视图
                : ImageView

        init {
            tv_title = v.findViewById(R.id.item_app_title)
            iv_img = v.findViewById(R.id.item_app_img)
        }
    }

    // 声明列表项的点击监听器对象
    private var mOnItemClickListener: RecyclerExtras.OnItemClickListener? = null
    fun setOnItemClickListener(listener: RecyclerExtras.OnItemClickListener?) {
        mOnItemClickListener = listener
    }

    // 声明列表项的长按监听器对象
    private var mOnItemLongClickListener: RecyclerExtras.OnItemLongClickListener? = null
    fun setOnItemLongClickListener(listener: RecyclerExtras.OnItemLongClickListener?) {
        mOnItemLongClickListener = listener
    }

    // 声明列表项的触摸监听器对象
    private var mOnItemTouchListener: RecyclerExtras.OnItemTouchListener? = null
    fun setOnItemTouchListener(listener: RecyclerExtras.OnItemTouchListener?) {
        mOnItemTouchListener = listener
    }

    // 处理列表项的点击事件
    @SuppressLint("DefaultLocale")
    override fun onItemClick(view: View?, position: Int) {
    }

    // 处理列表项的长按事件
    @SuppressLint("DefaultLocale")
    override fun onItemLongClick(view: View?, position: Int) {

    }

    // 处理列表项的触摸事件
    override fun onItemTouch(view: View?, event: MotionEvent?): Boolean {
        val desc = "列表被触摸"
        Toast.makeText(mContext, desc, Toast.LENGTH_SHORT).show()
        return false
    }
}