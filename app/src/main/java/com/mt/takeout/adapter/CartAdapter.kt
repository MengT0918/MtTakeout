package com.mt.takeout.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.mt.takeout.model.bean.GoodsInfo
import com.mt.takeout.widget.CartItemView

class CartAdapter(fm: FragmentManager) : RecyclerView.Adapter<CartAdapter.CartHolder>() {
    private val mCarts = arrayListOf<GoodsInfo>()

    class CartHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun setCartList(carts: ArrayList<GoodsInfo>) {
        mCarts.clear()
        mCarts.addAll(carts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        return CartHolder(CartItemView(parent.context))
    }

    override fun getItemCount(): Int {
        return mCarts.size
    }

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        val view = (holder.itemView as CartItemView)
        view.bindData(mCarts[position])
        view.setRefreshListener() {
            notifyDataSetChanged()
        }
        view.setRemoveListener {
            mCarts.remove(it)
            mCarts.size
        }
    }
}
