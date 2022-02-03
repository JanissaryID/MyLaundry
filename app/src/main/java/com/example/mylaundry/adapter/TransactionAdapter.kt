package com.example.mylaundry.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mylaundry.R
import com.example.mylaundry.api.transactions.ResponseTransactions

class TransactionAdapter(private val listTransactions: ArrayList<ResponseTransactions>): RecyclerView.Adapter<TransactionAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDate: TextView = itemView.findViewById(R.id.dateTransaction)
        var tvTitle: TextView = itemView.findViewById(R.id.titleTransaction)
        var tvPrice: TextView = itemView.findViewById(R.id.priceTextTransaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_transactions, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val transactions = listTransactions[position]

        holder.tvTitle.text = transactions.type
        holder.tvPrice.text = "IDR ${transactions.price}"
        holder.tvDate.text = "${transactions.date}  ${transactions.time}"
    }

    override fun getItemCount(): Int = listTransactions.size
}