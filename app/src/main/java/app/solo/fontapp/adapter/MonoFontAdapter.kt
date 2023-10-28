package app.solo.fontapp.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import app.solo.fontapp.R
import app.solo.fontapp.databinding.ItemFontBinding


import app.solo.fontapp.listeners.AdapterListener
import app.solo.fontapp.models.FontData


class MonoFontAdapter(
    val context: Context,
    private val listener: AdapterListener,
    private val propertyList: ArrayList<FontData>
) :
    RecyclerView.Adapter<MonoFontAdapter.ViewHolder>() {

    inner class ViewHolder(val binding:app.solo.fontapp.databinding.ItemFontBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFontBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
//        return propertyList.size // Return the number of items in your data list
        return propertyList.size // Return the number of items in your data list
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val propertyItem = propertyList[position]
        holder.binding.fontName.text=propertyItem.name

        holder.binding.favouritesIcon.setOnClickListener {

            listener.onAdapterItemClicked("font_selected",position)

        }
        holder.binding.downloadIcon.setOnClickListener {

            listener.onAdapterItemClicked("font_download",position)

        }

        holder.binding.root.setOnClickListener {

            listener.onAdapterItemClicked("edit",position)

        }
        // Bind your data to the layout using data binding
        fontStyle(holder,position)
        holder.binding.executePendingBindings()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fontStyle(holder: ViewHolder, position: Int ){

        holder.binding.font.text = propertyList[position].name
        when(position){
            0->{
                val customFont = context.resources.getFont(R.font.adorefree)

                holder.binding.font.typeface = customFont

            }
            1->{
                val customFont = context.resources.getFont(R.font.honeybear)

                holder.binding.font.typeface = customFont
            }
            2->{
                val customFont = context.resources.getFont(R.font.keyzha)

                holder.binding.font.typeface = customFont
            }
            3->{
                val customFont = context.resources.getFont(R.font.margarethy)

                holder.binding.font.typeface = customFont
            }
            4->{
                val customFont = context.resources.getFont(R.font.tendrils)

                holder.binding.font.typeface = customFont
            }
            5->{
                val customFont = context.resources.getFont(R.font.yumeira_monogram)

                holder.binding.font.typeface = customFont
            }
            6->{
                val customFont = context.resources.getFont(R.font.mady_risaw)

                holder.binding.font.typeface = customFont
            }
            7->{
                val customFont = context.resources.getFont(R.font.sun_flower)

                holder.binding.font.typeface = customFont
            }
            8->{
                val customFont = context.resources.getFont(R.font.coffeemocca)

                holder.binding.font.typeface = customFont
            }
            8->{
                val customFont = context.resources.getFont(R.font.spring)

                holder.binding.font.typeface = customFont
            }
            9->{
                val customFont = context.resources.getFont(R.font.ziviliammonogram)

                holder.binding.font.typeface = customFont
            }






        }
        holder.binding.font

    }


}
