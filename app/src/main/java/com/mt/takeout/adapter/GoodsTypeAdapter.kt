package com.mt.takeout.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mt.takeout.model.bean.GoodsTypeInfo
import com.mt.takeout.ui.fragment.GoodsFragment
import com.mt.takeout.widget.GoodsTypeItemView

class GoodsTypeAdapter(private val goodsFragment: GoodsFragment) :
    RecyclerView.Adapter<GoodsTypeAdapter.GoodsTypeHolder>() {
    private val mGoodsTypes = ArrayList<GoodsTypeInfo>()
    var mSelectPosition = 0

    inner class GoodsTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun updateGoodsTypes(list: List<GoodsTypeInfo>?) {
        mGoodsTypes.clear()
        list?.let {
            mGoodsTypes.addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsTypeHolder {
        return GoodsTypeHolder(GoodsTypeItemView(parent.context))
    }

    override fun getItemCount(): Int {
        return mGoodsTypes.size
    }

    override fun onBindViewHolder(holder: GoodsTypeHolder, position: Int) {
        val itemView = (holder.itemView as GoodsTypeItemView)
        val goodsType = mGoodsTypes[position]
        itemView.bindData(goodsType, mSelectPosition, position)
        itemView.setOnClickListener {
            mSelectPosition = position
            val goodsPosition = goodsFragment.mPresenter.getGoodsPositionByTypeId(goodsType.id)
            goodsFragment.mStickyListView.setSelection(goodsPosition)
            notifyDataSetChanged()
        }
    }
}