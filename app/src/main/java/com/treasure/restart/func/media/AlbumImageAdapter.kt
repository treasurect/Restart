package com.treasure.restart.func.media

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treasure.restart.databinding.ItemAlbumImageBinding

class AlbumImageAdapter(
    private val onItemClick: (AlbumImage) -> Unit
) : RecyclerView.Adapter<AlbumImageAdapter.AlbumImageViewHolder>() {

    private val images = mutableListOf<AlbumImage>()
    private val selectedUris = mutableListOf<Uri>()

    fun submitData(newImages: List<AlbumImage>, newSelectedUris: List<Uri>) {
        images.clear()
        images.addAll(newImages)
        selectedUris.clear()
        selectedUris.addAll(newSelectedUris)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumImageViewHolder {
        val binding = ItemAlbumImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AlbumImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image, selectedUris.indexOf(image.uri))
        holder.itemView.setOnClickListener { onItemClick(image) }
    }

    override fun getItemCount(): Int = images.size

    class AlbumImageViewHolder(
        private val binding: ItemAlbumImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: AlbumImage, selectedIndex: Int) {
            Glide.with(binding.ivAlbumImage.context)
                .load(image.uri)
                .centerCrop()
                .into(binding.ivAlbumImage)

            if (selectedIndex >= 0) {
                binding.tvAlbumIndex.visibility = View.VISIBLE
                binding.tvAlbumIndex.text = "${selectedIndex + 1}"
            } else {
                binding.tvAlbumIndex.visibility = View.GONE
            }
        }
    }
}

data class AlbumImage(
    val uri: Uri
)
