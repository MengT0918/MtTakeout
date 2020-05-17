package com.mt.takeout.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.mt.takeout.R
import kotlinx.android.synthetic.main.item_home_title.view.*

class HomeTitleItemView : LinearLayout {
    private val mUrlMaps = HashMap<String, String>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.item_home_title, this)
    }

    fun bindData() {
        if (mUrlMaps.size == 0) {
            mUrlMaps["Hannibal"] =
                "https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/31162/17/1128/101786/5c46ead8E22ee9740/f66061da227c1965.jpg!cr_1125x445_0_171!q70.jpg.dpg"
            mUrlMaps["Big Bang Theory"] =
                "https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/57267/26/14011/144429/5daffe93E40d0f031/a348a5c703a31657.jpg!cr_1125x445_0_171!q70.jpg.dpg"
            mUrlMaps["House of Cards"] =
                "https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/104640/28/366/119386/5dac4860E8d9f96df/0bbf771c2a26cf1d.jpg!cr_1125x445_0_171!q70.jpg.dpg"
            mUrlMaps["Game of Thrones"] =
                "https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/75964/2/13749/86827/5db14d08Ecab1f68f/6e4b81003e1e904f.jpg!cr_1125x445_0_171!q70.jpg.dpg"
            for ((key, value) in mUrlMaps) {
                val textSliderView = TextSliderView(context)
                textSliderView.description(key).image(value)
                item_home_title_slider.addSlider(textSliderView)
            }
        }
    }
}