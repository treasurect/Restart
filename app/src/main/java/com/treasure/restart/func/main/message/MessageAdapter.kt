package com.treasure.restart.func.main.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.treasure.restart.databinding.ItemMessageBinding
import com.treasure.restart.databinding.ItemMessageSectionBinding
import com.treasure.restart.databinding.ItemSuggestUserBinding
import com.treasure.restart.func.main.message.model.MessageListItem

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<com.treasure.restart.func.main.message.model.MessageListItem>()

    fun submitList(newItems: List<com.treasure.restart.func.main.message.model.MessageListItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is MessageListItem.Section -> TYPE_SECTION
            is MessageListItem.Activity -> TYPE_ACTIVITY
            is MessageListItem.Suggestion -> TYPE_SUGGESTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SECTION -> SectionViewHolder(
                ItemMessageSectionBinding.inflate(inflater, parent, false)
            )
            TYPE_ACTIVITY -> ActivityViewHolder(
                ItemMessageBinding.inflate(inflater, parent, false)
            )
            else -> SuggestionViewHolder(
                ItemSuggestUserBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is MessageListItem.Section -> (holder as SectionViewHolder).bind(item)
            is MessageListItem.Activity -> (holder as ActivityViewHolder).bind(item)
            is MessageListItem.Suggestion -> (holder as SuggestionViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class SectionViewHolder(private val binding: ItemMessageSectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageListItem.Section) {
            binding.tvSectionTitle.text = item.title
        }
    }

    class ActivityViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageListItem.Activity) {
            binding.tvMessageIcon.text = item.iconLabel
            binding.tvMessageTitle.text = item.title
            binding.tvMessageContent.text = item.content
            binding.tvMessageTime.text = item.time
        }
    }

    class SuggestionViewHolder(private val binding: ItemSuggestUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MessageListItem.Suggestion) {
            binding.tvSuggestionAvatar.text = item.avatarLabel
            binding.tvSuggestionName.text = item.nickname
            binding.tvSuggestionDesc.text = item.description
            binding.btnFollow.text = "关注"
        }
    }

    companion object {
        private const val TYPE_SECTION = 0
        private const val TYPE_ACTIVITY = 1
        private const val TYPE_SUGGESTION = 2
    }
}
