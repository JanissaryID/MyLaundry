package com.example.mylaundry.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mylaundry.R
import com.example.mylaundry.fragment.HomeFragment
import com.example.mylaundry.fragment.ListMachine
import com.example.mylaundry.room.washermachine.Washer
import com.google.android.material.internal.ContextUtils.getActivity
import java.security.AccessController.getContext

class WasherAdapter(private var listWasher: List<Washer>, price: String): RecyclerView.Adapter<WasherAdapter.ListViewHolder>() {

    private var selectedItem = -1

    private var priceMachine = price

    inner class ListViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {

        var tvNumber: TextView = itemview.findViewById(R.id.NumberMachine)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_machine, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val washer = listWasher[position]

        if(washer.isActive == true){
            holder.tvNumber.text = "On"
            holder.tvNumber.setTextColor(Color.WHITE)
            holder.tvNumber.setBackgroundResource(R.drawable.machine_running)
            holder.tvNumber.isEnabled = false
        }
        else{
            holder.tvNumber.text = washer.numberMachine.toString()

            ListMachine.statButton = false
            holder.tvNumber.setBackgroundResource(R.drawable.header_layout)
            holder.tvNumber.setTextColor(Color.WHITE)

            if(selectedItem == position){
                holder.tvNumber.setBackgroundResource(R.drawable.machine)
                holder.tvNumber.setTextColor(Color.BLACK)
            }
        }

        holder.itemView.setOnClickListener{
//            Toast.makeText(holder.itemView.context, "Kamu memilih Washer machine nomor " + listWasher[position].numberMachine, Toast.LENGTH_SHORT).show()
            if(washer.isActive == false){
                val previousItem = selectedItem
                selectedItem = position

                notifyItemChanged(previousItem)
                notifyItemChanged(position)
                ListMachine.statButton = true

                val home = ListMachine()
                home.buttonStat(ListMachine.buttonCheckout, priceMachine)

                ListMachine.idMachine = listWasher[position].idWasher!!
                ListMachine.number = listWasher[position].numberMachine!!

                Log.d("checkButton", ListMachine.statButton.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        Log.d("checkcount", listWasher.size.toString())
        return listWasher.size
    }

    fun setData(washer: List<Washer>){
        this.listWasher = washer
        notifyDataSetChanged()
    }
}