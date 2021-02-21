package com.dsk.themoviedb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dsk.themoviedb.databinding.ItemLoadingStateBinding

class LoadStateAdapterView(
    private val retryToFetchData: () -> Unit
) : LoadStateAdapter<LoadStateAdapterView.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.itemView.apply {
            holder.viewBinding.buttonRetry.isVisible = loadState !is LoadState.Loading
            holder.viewBinding.textViewError.isVisible = loadState !is LoadState.Loading
            holder.viewBinding.progressbar.isVisible = loadState is LoadState.Loading

            if (loadState is LoadState.Error) {
                holder.viewBinding.textViewError.text = loadState.error.localizedMessage
            }

            holder.viewBinding.buttonRetry.setOnClickListener {
                retryToFetchData.invoke()
            }
        }
    }

    inner class LoadStateViewHolder(var viewBinding: ItemLoadingStateBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            ItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

}