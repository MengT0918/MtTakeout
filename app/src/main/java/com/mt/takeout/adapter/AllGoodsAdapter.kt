package com.mt.takeout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mt.takeout.R
import com.mt.takeout.model.bean.GoodsInfo
import com.mt.takeout.ui.fragment.GoodsFragment
import com.mt.takeout.widget.AllGoodsItemView
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter

class AllGoodsAdapter(private val goodsFragment: GoodsFragment) : BaseAdapter(), StickyListHeadersAdapter {
    private val allGoods = ArrayList<GoodsInfo>()

    fun updateAllGoods(list: List<GoodsInfo>?) {
        allGoods.clear()
        list?.let {
            allGoods.addAll(list)
            notifyDataSetChanged()
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = AllGoodsItemView(parent?.context)
        }
        val view = (itemView as AllGoodsItemView)
        view.setFragment(goodsFragment)
        view.bindData(allGoods[position])
        view.setRefreshListener {
            notifyDataSetChanged()
        }
        return itemView
    }

    override fun getItem(position: Int): Any {
        return allGoods[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return allGoods.size
    }

    override fun getHeaderId(position: Int): Long {
        return allGoods[position].typeId.toLong()
    }

    override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val tv = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_type_header, parent, false) as TextView
        tv.text = allGoods[position].typeName
        return tv
    }
}