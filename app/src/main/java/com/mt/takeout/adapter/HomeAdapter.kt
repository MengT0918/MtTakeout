package com.mt.takeout.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mt.takeout.model.bean.HomeSeller
import com.mt.takeout.widget.HomeSellerItemView
import com.mt.takeout.widget.HomeTitleItemView

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeHolder>() {
    private val mDatas = ArrayList<HomeSeller>()

    companion object {
        const val TYPE_TITLE = 0
        const val TYPE_SELLER = 1
    }

    class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun updateData(list: List<HomeSeller>) {
        mDatas.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
        return when (viewType) {
            TYPE_TITLE -> HomeHolder(HomeTitleItemView(parent.context))
            TYPE_SELLER -> HomeHolder(HomeSellerItemView(parent.context))
            else -> HomeHolder(HomeTitleItemView(parent.context))
        }
    }

    override fun getItemCount(): Int {
        return if (mDatas.size > 0) mDatas.size + 1 else 0
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_TITLE -> (holder.itemView as HomeTitleItemView).bindData()
            TYPE_SELLER -> (holder.itemView as HomeSellerItemView).bindData(mDatas[position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_TITLE else TYPE_SELLER
    }
}