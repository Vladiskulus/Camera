package com.iambulanva.camera


import android.graphics.BitmapFactory
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.iambulanva.camera.databinding.ItemPhotoBinding

class GalleryAdapter(private val photoPaths: List<String> = emptyList(), val callback: (String) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = PhotoViewHolder(
        ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = photoPaths.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with((holder as PhotoViewHolder).binding){
            // Завантаження фотографії за шляхом
            iv.setImageBitmap(BitmapFactory.decodeFile(photoPaths[position]))
            iv.setOnClickListener {
                callback(photoPaths[position])
            }
        }
    }
}
