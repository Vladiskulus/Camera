package com.iambulanva.camera

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iambulanva.camera.data.PhotoEntity
import com.iambulanva.camera.databinding.ItemPhotoBinding

class GalleryAdapter(private var photoPaths: List<String> = emptyList()): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setPhotos(photos: List<PhotoEntity>) {
        // Оновлюємо адаптер із списком PhotoEntity
        this.photoPaths = photos.mapNotNull { it.filePath }
        notifyDataSetChanged()
    }

    fun addPhoto(photoPath: String) {
        // Додаємо новий шлях до фотографії та оновлюємо адаптер
        photoPaths = photoPaths.toMutableList().apply { add(photoPath) }
        notifyDataSetChanged()
    }

    class PhotoViewHolder(val binding: ItemPhotoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = PhotoViewHolder(
        ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = photoPaths.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with((holder as PhotoViewHolder).binding){
            // Завантаження фотографії за шляхом
            iv.setImageBitmap(BitmapFactory.decodeFile(photoPaths[position]))
        }
    }
}
