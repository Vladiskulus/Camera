package com.vn.iambulance.prototype_20.ui.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseViewHolder<T : ViewBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)