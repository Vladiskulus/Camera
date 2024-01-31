package com.iambulanva.camera

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iambulanva.camera.data.PhotoEntity
import com.iambulanva.camera.databinding.ItemPhotoBinding

class GalleryAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var photos: List<PhotoEntity> = listOf()

    fun setPhotos(photos: List<PhotoEntity>) {
        this.photos = photos
        notifyDataSetChanged()
    }

    class PhotoViewHolder(val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = PhotoViewHolder(
        ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with((holder as PhotoViewHolder).binding){
            iv.setImageBitmap(BitmapFactory.decodeByteArray(photos[position].bitmap, 0, photos[position].bitmap.size))
        }
    }
}