package com.example.mylaundry.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylaundry.R
import com.example.mylaundry.adapter.SettingAdapter
import com.example.mylaundry.adapter.TransactionAdapter
import com.example.mylaundry.api.machine.ResponseMachine
import com.example.mylaundry.api.machine.RetrofitClientMachine
import com.example.mylaundry.api.transactions.ResponseTransactions
import com.example.mylaundry.api.transactions.RetrofitClientTransactions
import com.example.mylaundry.room.settings.SettingViewModel
import kotlinx.android.synthetic.main.dialog_datepicker.view.*
import kotlinx.android.synthetic.main.dialog_login.view.*
import kotlinx.android.synthetic.main.dialog_login.view.buttonEnter
import kotlinx.android.synthetic.main.fragment_transaction.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class TransactionFragment : Fragment(), View.OnClickListener {

    private lateinit var rvTransactions: RecyclerView

    private val listTrans = ArrayList<ResponseTransactions>()

    private var msg = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTransactions = view.findViewById(R.id.rvTransactions)
        ButtonBackTransactions.setOnClickListener(this)
        FilterButton.setOnClickListener(this)
        btnSeeAllTransactions.setOnClickListener(this)

        getDataTransactions()
    }

    private fun showEmptyText() {
        if(listTrans.size != 0){
            EmptyTextTransactions.isVisible = false
        }
        else{
            EmptyTextTransactions.isVisible = true
        }
    }

    private fun showRecyclerList() {
        Log.d("retrofit", "show ${listTrans.toString()}")
        val adapter = TransactionAdapter(listTrans)
        rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        rvTransactions.adapter = adapter
        rvTransactions.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }

    private fun getDataTransactions(){
        try {
            RetrofitClientTransactions.instance.getTransactions().enqueue(object :
                Callback<List<ResponseTransactions>> {
                override fun onResponse(call: Call<List<ResponseTransactions>>, response: Response<List<ResponseTransactions>>) {
                    Log.d("retrofit", "Code : ${response.code().toString()}")
                    response.body()?.let {
                        listTrans.addAll(it)
                    }
                    Log.d("retrofit", "Body : ${listTrans.size.toString()}")
                    showEmptyText()
                    showRecyclerList()
                }

                override fun onFailure(call: Call<List<ResponseTransactions>>, t: Throwable) {
                    Log.d("p2", t.message.toString())
                    if (t.message == t.message){
                        Toast.makeText(requireContext(), "Tidak ada koneksi Internet" , Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
        catch (e : Exception){
            Toast.makeText(requireContext(), "Error $e" , Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataTransactionsFilter(){
        listTrans.clear()
        try {
            RetrofitClientTransactions.instance.getFilterTransactions(msg).enqueue(object :
                Callback<List<ResponseTransactions>> {
                override fun onResponse(call: Call<List<ResponseTransactions>>, response: Response<List<ResponseTransactions>>) {
                    Log.d("retrofit", "Code : ${response.code().toString()}")
                    response.body()?.let {
                        listTrans.addAll(it)
                    }
                    Log.d("retrofit", "Body : ${listTrans.size.toString()}")
                    showEmptyText()
                    showRecyclerList()
                }

                override fun onFailure(call: Call<List<ResponseTransactions>>, t: Throwable) {
                    Log.d("p2", t.message.toString())
                    if (t.message == t.message){
                        Toast.makeText(requireContext(), "Tidak ada koneksi Internet" , Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
        catch (e : Exception){
            Toast.makeText(requireContext(), "Error $e" , Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCustomDatePicker() {



        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_datepicker, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)

        val mAlertDialog = mBuilder.show()

        mDialogView.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            msg = "${"%02d".format(dayOfMonth)}-${"%02d".format(month + 1)}-${year}"
        }

        mDialogView.buttonEnter.setOnClickListener {
            if(msg == ""){
                Toast.makeText(requireContext(), "Pick the date" , Toast.LENGTH_SHORT).show()
            }
            else{
                getDataTransactionsFilter()
                Toast.makeText(requireContext(), "$msg" , Toast.LENGTH_SHORT).show()
                mAlertDialog.dismiss()
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.ButtonBackTransactions -> {
                activity?.onBackPressed()
            }
            R.id.btnSeeAllTransactions -> {
                activity?.onBackPressed()
            }
            R.id.FilterButton -> {
                showCustomDatePicker()
            }
        }
    }
}