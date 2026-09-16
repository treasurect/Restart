package com.treasure.restart.func.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.treasure.restart.databinding.ItemProfileActionBinding
import com.treasure.restart.databinding.ItemProfileHeaderBinding
import com.treasure.restart.func.main.profile.model.ProfileData

class ProfileListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ProfileListItem>()

    fun setProfile(profile: ProfileData) {
        items.clear()
        items.add(ProfileListItem.Header(profile))
        profile.actions.forEach { action ->
            items.add(ProfileListItem.Action(action.first, action.second))
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ProfileListItem.Header -> TYPE_HEADER
            is ProfileListItem.Action -> TYPE_ACTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(
                ItemProfileHeaderBinding.inflate(inflater, parent, false)
            )
            else -> ActionViewHolder(
                ItemProfileActionBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ProfileListItem.Header -> (holder as HeaderViewHolder).bind(item.profile)
            is ProfileListItem.Action -> (holder as ActionViewHolder).bind(item.title, item.value)
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderViewHolder(private val binding: ItemProfileHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(profile: ProfileData) {
            binding.tvProfileAvatar.text = profile.avatarText
            binding.tvProfileNickname.text = profile.nickname
            binding.tvProfileUserId.text = profile.userId
            binding.tvProfileBio.text = profile.bio
            binding.tvFollowCount.text = profile.stats[0].first
            binding.tvFansCount.text = profile.stats[1].first
            binding.tvLikeCount.text = profile.stats[2].first
        }
    }

    class ActionViewHolder(private val binding: ItemProfileActionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, value: String) {
            binding.tvProfileActionTitle.text = title
            binding.tvProfileActionValue.text = value
        }
    }

    private sealed class ProfileListItem {
        data class Header(val profile: ProfileData) : ProfileListItem()
        data class Action(val title: String, val value: String) : ProfileListItem()
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ACTION = 1
    }
}
