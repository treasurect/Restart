package com.treasure.basic.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.treasure.basic.R
import com.treasure.basic.bean.SearchSortBean

/**
val options = listOf(
"引入外资额由大到小",
"引入外资额由小到大",
"投资意愿由高到低",
"投资意愿由低到高",
"成立日期从早到晚",
"成立日期从晚到早",
"注册资本从大到小",
"注册资本从小到大"
)

sortView.setOnClickListener {
DropPopupWindow(this, it, options, selectedIndex) { index, value ->
selectedIndex = index
sortView.text = value
// TODO: 数据刷新逻辑
}.show()
}
 */
class DropWrapPopupWindow<T>(
    private val context: Context,
    private val anchor: View,
    private val options: List<T>,
    private var selectedIndex: Int = -1,
    private val onSelect: (index: Int, value: String) -> Unit
) {

    private var popupWindow: PopupWindow? = null

    fun show() {
        val contentView = LayoutInflater.from(context).inflate(R.layout.layout_popup_drop_text_wrap, null)
        val recyclerView = contentView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = OptionsAdapter()

        popupWindow = PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
            isOutsideTouchable = true
            isFocusable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            elevation = 10f
            showAsDropDown(anchor)
        }
    }

    fun dismiss() {
        popupWindow?.dismiss()
    }

    private inner class OptionsAdapter : RecyclerView.Adapter<OptionsAdapter.SortViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.item_option_text_wrap, parent, false)
            return SortViewHolder(view)
        }

        override fun onBindViewHolder(holder: SortViewHolder, position: Int) {
            val text = when (val item = options[position]) {
                is SearchSortBean -> item.sortName
                is String -> item
                else -> ""
            }
            holder.tvOption.text = text
            holder.itemView.setOnClickListener {
                selectedIndex = position
                onSelect(position, text)
                popupWindow?.dismiss()
            }
        }

        override fun getItemCount(): Int = options.size

        inner class SortViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvOption: TextView = view.findViewById(R.id.tv_options)
        }
    }
}
