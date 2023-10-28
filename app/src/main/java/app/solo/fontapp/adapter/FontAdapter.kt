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


class FontAdapter(
    val context: Context,
    private val listener: AdapterListener,
    private val propertyList: ArrayList<FontData>
) :
    RecyclerView.Adapter<FontAdapter.ViewHolder>() {

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

        if(propertyItem.selected){
            holder.binding.favouritesIcon.setImageResource(R.drawable.aa_selected_fav_filled_icon)
        }else {
            holder.binding.favouritesIcon.setImageResource(R.drawable.aa_selected_fav_filled_gray_icon)
        }

        fontStyle(holder,position)



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

        holder.binding.executePendingBindings()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fontStyle(holder: ViewHolder, position: Int ){

        holder.binding.font.text = propertyList[position].name
        when(position){
            0->{
                val customFont = context.resources.getFont(R.font.roboto_regular)

                holder.binding.font.typeface = customFont

            }
            1->{
                val customFont = context.resources.getFont(R.font.coco)

                holder.binding.font.typeface = customFont
            }
            2->{
                val customFont = context.resources.getFont(R.font.glad)

                holder.binding.font.typeface = customFont
            }
            3->{
                val customFont = context.resources.getFont(R.font.body_grotesque)

                holder.binding.font.typeface = customFont
            }
            4->{
                val customFont = context.resources.getFont(R.font.trislan)

                holder.binding.font.typeface = customFont
            }
            5->{
                val customFont = context.resources.getFont(R.font.agendaking)

                holder.binding.font.typeface = customFont
            }
            6->{
                val customFont = context.resources.getFont(R.font.atipastopromedium)

                holder.binding.font.typeface = customFont
            }
             7->{
                val customFont = context.resources.getFont(R.font.ballerina)

                holder.binding.font.typeface = customFont
            }
             8->{
                val customFont = context.resources.getFont(R.font.cafe_francoise)

                holder.binding.font.typeface = customFont
            }
            9->{
                val customFont = context.resources.getFont(R.font.beatrix)

                holder.binding.font.typeface = customFont
            }
            10->{
                val customFont = context.resources.getFont(R.font.astaghfirulloh)

                holder.binding.font.typeface = customFont
            }
            11->{
                val customFont = context.resources.getFont(R.font.buljirya)

                holder.binding.font.typeface = customFont
            }
            12->{
                val customFont = context.resources.getFont(R.font.blacknote)

                holder.binding.font.typeface = customFont
            }
            13->{
                val customFont = context.resources.getFont(R.font.candy_qelling)

                holder.binding.font.typeface = customFont
            }
            14->{
                val customFont = context.resources.getFont(R.font.catterpillar)

                holder.binding.font.typeface = customFont
            }
            15->{
                val customFont = context.resources.getFont(R.font.eleganodisplay)

                holder.binding.font.typeface = customFont
            }
            16->{
                val customFont = context.resources.getFont(R.font.godlike_personal_use)

                holder.binding.font.typeface = customFont
            }
            17->{
                val customFont = context.resources.getFont(R.font.heal_the_world)

                holder.binding.font.typeface = customFont
            }
            18->{
                val customFont = context.resources.getFont(R.font.honuzimaregular)

                holder.binding.font.typeface = customFont
            }
            19->{
                val customFont = context.resources.getFont(R.font.lovely_girl)

                holder.binding.font.typeface = customFont
            }
            20->{
                val customFont = context.resources.getFont(R.font.midway)

                holder.binding.font.typeface = customFont
            }
            21->{
                val customFont = context.resources.getFont(R.font.milchella)

                holder.binding.font.typeface = customFont
            }
            22->{
                val customFont = context.resources.getFont(R.font.nozomi)

                holder.binding.font.typeface = customFont
            }
        }
        holder.binding.font

    }


}
