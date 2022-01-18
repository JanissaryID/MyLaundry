package com.example.mylaundry.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mylaundry.R
import com.example.mylaundry.fragment.SettingFragmentDirections
import com.example.mylaundry.room.settings.Settings

class SettingAdapter(private var listSetting: List<Settings>): RecyclerView.Adapter<SettingAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {

        var tvTitle: TextView = itemview.findViewById(R.id.titleNamesText)
        var tvValue: TextView = itemview.findViewById(R.id.valueNamesText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_setting, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val setting = listSetting[position]
        holder.tvTitle.text = setting.nameSetting
        val titleNamesCheck = setting.nameSetting
        val separate1 = titleNamesCheck!!.split(" ")[1]
//        Log.d("setting", "TimeOut  $separate1")
        if(separate1 == "machine"){
            holder.tvValue.text = setting.valueSetting + " Unit"
        }
        else if(separate1 == "price"){
            holder.tvValue.text = "IDR"+setting.valueSetting
        }
        else if(separate1 == "laundry"){
            holder.tvValue.text = setting.valueSetting
        }
        else if(separate1 == "address"){
            holder.tvValue.text = setting.valueSetting
        }
        else if(separate1 == "port"){
            holder.tvValue.text = setting.valueSetting
        }
//        else if(separate1 == "bluetooth"){
//            holder.tvValue.text = setting.valueSetting
//        }
        else{
            holder.tvValue.text = "••••••••"
        }

        holder.itemView.setOnClickListener{
//            Toast.makeText(holder.itemView.context, "Kamu memilih " + listSetting[position].nameSetting +"   " + listSetting[position].valueSetting, Toast.LENGTH_SHORT).show()
            val action = SettingFragmentDirections.actionSettingFragmentToEditFragment(listSetting[position].nameSetting, listSetting[position].valueSetting,
                listSetting[position].idSettings!!
            )
            Navigation.findNavController(holder.itemView).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return listSetting.size
    }

    fun setData(settings: List<Settings>){
        this.listSetting = settings
        notifyDataSetChanged()
    }
}