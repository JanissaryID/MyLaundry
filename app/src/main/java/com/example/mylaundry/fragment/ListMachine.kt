package com.example.mylaundry.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylaundry.R
import com.example.mylaundry.adapter.MachineAdapter
import com.example.mylaundry.api.machine.ResponseMachine
import com.example.mylaundry.api.machine.RetrofitClient
import kotlinx.android.synthetic.main.fragment_list_machine.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMachine : Fragment(), View.OnClickListener {

    private val args : ListMachineArgs by navArgs()

    private lateinit var BtnBack : ImageButton
    private lateinit var titleList : TextView
    private lateinit var emptyTextMachine : TextView

//    lateinit var buttonCheckout : Button

//    private lateinit var rvMachine: RecyclerView



    private var priceMachine : String = "0"

    companion object{
        var statButton: Boolean = false
        lateinit var buttonCheckoutMachine : Button
        var number : Int = 0
        var idMachine : Int = 0

        val listMachine = ArrayList<ResponseMachine>()
        val listDryer = ArrayList<ResponseMachine>()
        val listWasher = ArrayList<ResponseMachine>()
        val listDryerUse = ArrayList<ResponseMachine>()
        val listWasherUse = ArrayList<ResponseMachine>()

        lateinit var rvMachineall: RecyclerView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_machine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BtnBack = view.findViewById(R.id.ButtonBackList)
        titleList = view.findViewById(R.id.TitleList)
        emptyTextMachine = view.findViewById(R.id.EmptyText)
        buttonCheckoutMachine = view.findViewById(R.id.buttonCheckout)

        rvMachineall = view.findViewById(R.id.rvMachine)


        BtnBack.setOnClickListener(this)
        buttonCheckout.setOnClickListener(this)

        titleList.text = args.titleBar

        buttonCheckout.isEnabled =false
        buttonCheckout.setBackgroundResource(R.drawable.gray_button)

        showLoading(true)

        getdataMachine()
        buttonStat(buttonCheckout,"0")

        emptyTextMachine.isVisible = false
    }

    fun buttonStat(button: Button, price: String) {
        if(statButton){
            button.isEnabled =true
            button.setBackgroundResource(R.drawable.header_layout)
            button.text = "IDR $price"
            priceMachine = price
        }
        else{
            button.isEnabled =false
            button.setBackgroundResource(R.drawable.gray_button)
            button.text = "Checkout"
        }
    }

    private fun showEmptyTextWasher() {
        if(listWasher != null){
            emptyTextMachine.isVisible = false
        }
        else{
            emptyTextMachine.text = "Dryer machine is empty"
            emptyTextMachine.isVisible = true
        }
    }

    private fun showEmptyTextDryer() {
        if(listDryer != null){
            emptyTextMachine.isVisible = false
        }
        else{
            emptyTextMachine.text = "Dryer machine is empty"
            emptyTextMachine.isVisible = true
        }
    }

    private fun getdataMachine()
    {
        rvMachineall.setHasFixedSize(true)
        rvMachineall.layoutManager = GridLayoutManager(requireContext(), 3)
//        showLoading(true)
        if(args.typeMachine == 0){
            val adapter = MachineAdapter(listWasher, args.price.toString())
            rvMachine.adapter = adapter

            if (listWasher != null) {
                showEmptyTextWasher()
                showLoading(false)
            } else {
                showEmptyTextDryer()
                showLoading(true)
            }
        }
        else{
//            showLoading(true)
            val adapter = MachineAdapter(listDryer, args.price.toString())
            rvMachine.adapter = adapter

            if (listDryer != null) {
                showEmptyTextDryer()
                showLoading(false)
            } else {
                showLoading(true)
            }
        }
//        rvMachine.layoutManager = GridLayoutManager(requireContext(), 3)
//        rvMachine.adapter = adapter
//        rvMachine.setHasFixedSize(true)
    }

    private fun ContainsDot(price : String): Int {
        var MachinePrice = 0
        if(price.contains(".")) {
            MachinePrice = price.replace(".","").toInt()

        }
        else{
            MachinePrice = args.price!!.toInt()
        }

        return MachinePrice
    }

//    private fun updateValueMachine(machine:String,id: Int, number : Int){
//        Log.d("checktitle", "Check title " + machine)
//        val separate = machine!!.split(" ")[0]
//        Log.d("checktitle", "Check title " + separate)
//        if(separate == "Washer"){
//            val updatedvalue = Washer(id,number,true)
//            mWasherViewModel.updateWasher(updatedvalue)
//        }
//        else{
//            val updatedvalue = Dryer(id,number,true)
//            mDryerViewModel.updateDryer(updatedvalue)
//        }
//    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.ButtonBackList -> {
//                listWasher.clear()
//                listDryer.clear()
                activity?.onBackPressed()
            }
            R.id.buttonCheckout -> {
                statButton = false
//                updateValueMachine(args.titleBar!!, idMachine, number)
//                Log.d("checktitle", "Check title " + args.titleBar)

                val separate1 = args.titleBar!!.split(" ")[0]
                Log.d("checkrefid", "Check title " + args.titleBar)
                Log.d("checkrefid", "Check title " + separate1)
//                Log.d("checkrefid", "Check Price Home " + HomeFragment.priceWasher)

                if (separate1 == "Washer"){

                    val adapterWasher = MachineAdapter(listWasher, args.price.toString())
                    rvMachine.adapter = adapterWasher

                    val pricemachineWasher = ContainsDot(HomeFragment.priceWasher)
                    val action = ListMachineDirections.actionListMachineToQrisFragment(args.titleBar.toString(),number,pricemachineWasher)
                    Navigation.findNavController(p0).navigate(action)
                }
                else if (separate1 == "Dryer"){

                    val adapterDryer = MachineAdapter(listDryer, args.price.toString())
                    rvMachine.adapter = adapterDryer

                    val pricemachineDryer = ContainsDot(HomeFragment.priceDryer)
                    val action = ListMachineDirections.actionListMachineToQrisFragment(args.titleBar.toString(),number,pricemachineDryer)
                    Navigation.findNavController(p0).navigate(action)
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressbar.visibility = View.VISIBLE
        } else {
            progressbar.visibility = View.GONE
        }
    }
}