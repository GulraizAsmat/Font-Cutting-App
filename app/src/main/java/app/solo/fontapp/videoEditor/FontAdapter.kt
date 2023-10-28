//package com.employee.employeejobbie.activites.videoEditor
//
//import android.content.Context
//import android.graphics.Typeface
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.core.content.res.ResourcesCompat
//import androidx.recyclerview.widget.RecyclerView
//import app.solo.fontapp.R
//import app.solo.fontapp.listeners.AdapterListener
//
//import app.solo.fontapp.videoEditor.model.FontStyle
//
//
//
//class FontAdapter(
//    val context: Context,
//    private val listener: AdapterListener,
//    private val fontList: ArrayList<FontStyle>,
//) :
//    RecyclerView.Adapter<FontAdapter.MyHolder>(), View.OnClickListener {
//
//
//    var tvFontName: TextView? = null;
//
//    inner class MyHolder(v: View) : RecyclerView.ViewHolder(v) {
//        var view: View = v
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.item_font_select, parent, false)
//        return MyHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MyHolder, position: Int) {
//
//        tvFontName = holder.view.findViewById(R.id.tvFontName);
//
//
////        tvFontName!!.text = fontList[position]
//
//
//        if (fontList[position].fontSelected) {
//            holder.view.cl_font_bg.setBackgroundResource(R.drawable.font_bg_shape_selected)
//        }
//        else {
//            holder.view.cl_font_bg.setBackgroundResource(R.drawable.font_bg_shape)
//        }
//
//
//        showFontFamily(fontList[position].fontName, holder)
//        tvFontName!!.setOnClickListener {
//
//            listener.onAdapterItemClicked("selected_font", position)
//        }
//
//    }
//
//
//    fun showFontFamily(fontName: String, holder: MyHolder) {
//
//        when (fontName) {
//            "Wonderland" -> {
//
//                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.wonderland)
//                tvFontName!!.typeface = typeface
//                tvFontName!!.text = "Aa"
//            }
//            "Cinzel" -> {
//                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.cinzel)
//                tvFontName!!.typeface = typeface
//                tvFontName!!.text = "Aa"
//            }
//            "Emojione" -> {
//                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.emojione)
//                tvFontName!!.typeface = typeface
//                tvFontName!!.text = "Aa"
//            }
//            "Josefinsans" -> {
//                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.josefinsans)
//                tvFontName!!.typeface = typeface
//                tvFontName!!.text = "Aa"
//            }
//            "Merriweather" -> {
//                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.merriweather)
//                tvFontName!!.typeface = typeface
//                tvFontName!!.text = "Aa"
//            }
//            "Raleway" -> {
//                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.raleway)
//                tvFontName!!.typeface = typeface
//                tvFontName!!.text = "Aa"
//            }
//            "Roboto" -> {
//                val typeface: Typeface? = ResourcesCompat.getFont(context, R.font.roboto)
//                tvFontName!!.typeface = typeface
//                tvFontName!!.text = "Aa"
//            }
//
//        }
//
//    }
//
//
//    override fun getItemCount(): Int {
//        return fontList.size
//    }
//
//    override fun onClick(p0: View?) {
//
//    }
//
//}