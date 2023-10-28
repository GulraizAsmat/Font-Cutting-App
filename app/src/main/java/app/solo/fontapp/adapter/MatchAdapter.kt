package app.solo.fontapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.solo.fontapp.databinding.ItemColorssBinding
import app.solo.fontapp.listeners.AdapterListener
import app.solo.fontapp.models.ColorData


class MatchAdapter(
    val context: Context,
    private val listener: AdapterListener,
    private val propertyList: ArrayList<ColorData>
) :
    RecyclerView.Adapter<MatchAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemColorssBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemColorssBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val propertyItem = propertyList[position]


        holder.binding.profileImage.setImageResource(propertyItem.color)



        holder.binding.root.setOnClickListener {
            listener.onAdapterItemClicked("chat_detail", position)
        }
        holder.binding.executePendingBindings()
    }


    override fun getItemCount(): Int {
        return propertyList.size // Return the number of items in your data list
    }

}
