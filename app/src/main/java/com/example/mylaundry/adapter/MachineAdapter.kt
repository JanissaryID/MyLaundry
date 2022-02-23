package com.example.mylaundry.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mylaundry.R
import com.example.mylaundry.api.machine.ResponseMachine
import com.example.mylaundry.fragment.ListMachine

class MachineAdapter(private val listMachine: ArrayList<ResponseMachine>, price: String): RecyclerView.Adapter<MachineAdapter.ListViewHolder>() {

    private var selectedItem = -1

//    private var priceMachine = price

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNumber: TextView = itemView.findViewById(R.id.NumberMachine)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MachineAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_machine, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MachineAdapter.ListViewHolder, position: Int) {
        val machineLaundry = listMachine[position]

        if(machineLaundry.machineStatus == true){
            holder.tvNumber.text = "On"
            holder.tvNumber.setTextColor(Color.WHITE)
            holder.tvNumber.setBackgroundResource(R.drawable.machine_running)
            holder.tvNumber.isEnabled = false
        }
        else{
            holder.tvNumber.text = machineLaundry.machineNumber.toString()

            ListMachine.statButton = false
            holder.tvNumber.setBackgroundResource(R.drawable.header_layout)
            holder.tvNumber.setTextColor(Color.WHITE)

            if(selectedItem == position){
                holder.tvNumber.setBackgroundResource(R.drawable.machine)
                holder.tvNumber.setTextColor(Color.BLACK)

            }
        }


        holder.itemView.setOnClickListener{
            if(machineLaundry.machineStatus == false){
                val previousItem = selectedItem
                selectedItem = position

                notifyItemChanged(previousItem)
                notifyItemChanged(position)
                ListMachine.statButton = true

                val home = ListMachine()
//                home.buttonStat(ListMachine.buttonCheckoutMachine, priceMachine)
                home.buttonStat(ListMachine.buttonCheckoutMachine, machineLaundry.machinePrice!!)

                ListMachine.idMachine = listMachine[position].id!!
                ListMachine.number = listMachine[position].machineNumber!!

                Log.d("checkButton", ListMachine.statButton.toString())
            }
        }
    }

    override fun getItemCount(): Int = listMachine.size
}