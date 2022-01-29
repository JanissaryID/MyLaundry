package com.example.mylaundry.fragment

import android.view.View
import androidx.fragment.app.Fragment

//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ImageButton
//import android.widget.TextView
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.Navigation
//import androidx.navigation.fragment.navArgs
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.mylaundry.R
//import com.example.mylaundry.room.dryermachine.Dryer
//import com.example.mylaundry.room.dryermachine.DryerViewModel
//import com.example.mylaundry.room.washermachine.Washer
//import com.example.mylaundry.room.washermachine.WasherViewModel
//
class ListMachineAPI : Fragment() {
//
//    private val args : ListMachineArgs by navArgs()
//
//    private lateinit var BtnBack : ImageButton
//    private lateinit var titleList : TextView
//    private lateinit var emptyTextMachine : TextView
//
//    private lateinit var rvMachine: RecyclerView
//
//    private lateinit var mDryerViewModel : DryerViewModel
//    private lateinit var mWasherViewModel : WasherViewModel
//
//    private val listDryer = emptyList<Dryer>()
//    private val listWasher = emptyList<Washer>()
//
//    private var priceMachine : String = "0"
//
//    companion object{
//        var statButton: Boolean = false
//        lateinit var buttonCheckout : Button
//        var number : Int = 0
//        var idMachine : Int = 0
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_list_machine, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        BtnBack = view.findViewById(R.id.ButtonBackList)
//        titleList = view.findViewById(R.id.TitleList)
//        emptyTextMachine = view.findViewById(R.id.EmptyText)
//        buttonCheckout = view.findViewById(R.id.buttonCheckout)
//
//        rvMachine = view.findViewById(R.id.rvMachine)
//        rvMachine.setHasFixedSize(true)
//
//        BtnBack.setOnClickListener(this)
//        buttonCheckout.setOnClickListener(this)
//
//        titleList.text = args.titleBar
//
//        mDryerViewModel = ViewModelProvider(this).get(DryerViewModel::class.java)
//        mWasherViewModel = ViewModelProvider(this).get(WasherViewModel::class.java)
//
//        buttonCheckout.isEnabled =false
//        buttonCheckout.setBackgroundResource(R.drawable.gray_button)
//
//        showListMachine()
//        buttonStat(buttonCheckout,"0")
//    }
//
//    fun buttonStat(button: Button, price: String) {
//        if(statButton){
//            button.isEnabled =true
//            button.setBackgroundResource(R.drawable.header_layout)
//            button.text = "IDR $price"
//            priceMachine = price
//        }
//        else{
//            button.isEnabled =false
//            button.setBackgroundResource(R.drawable.gray_button)
//            button.text = "Checkout"
//        }
//    }
//
//    private fun showEmptyTextWasher() {
//        if(HomeFragment.washerMachine > 0){
//            emptyTextMachine.isVisible = false
//        }
//        else{
//            emptyTextMachine.text = "Washer machine is empty"
//            emptyTextMachine.isVisible = true
//        }
//    }
//
//    private fun showEmptyTextDryer() {
//        if(HomeFragment.dryerMachine > 0){
//            emptyTextMachine.isVisible = false
//        }
//        else{
//            emptyTextMachine.text = "Dryer machine is empty"
//            emptyTextMachine.isVisible = true
//        }
//    }
//
//    fun showListMachine() {
////        Log.d("checktitle", "Check Get title " + args.titleBar)
//        val title = args.titleBar
//        val separate = title!!.split(" ")[0]
////        Log.d("checktitle", "Check title " + separate)
////        Log.d("checktitle", "Check title " + separate.length)
//
//        if(separate == "Washer"){
//            val adapter = WasherAdapter(listWasher, args.price.toString())
//            rvMachine.layoutManager = GridLayoutManager(requireContext(), 3)
//            rvMachine.adapter = adapter
//
//            mWasherViewModel.readAllData.observe(viewLifecycleOwner, Observer { washer ->
//                adapter.setData(washer)
//            })
//            showEmptyTextWasher()
//        }
//        else if(separate == "Dryer"){
//            val adapter = DryerAdapter(listDryer, args.price.toString())
//            rvMachine.layoutManager = GridLayoutManager(requireContext(), 3)
//            rvMachine.adapter = adapter
//
//            mDryerViewModel.readAllData.observe(viewLifecycleOwner, Observer { dryer ->
//                adapter.setData(dryer)
//            })
//            showEmptyTextDryer()
//        }
//
//    }
//
//    private fun ContainsDot(price : String): Int {
//        var MachinePrice = 0
//        if(price.contains(".")) {
//            MachinePrice = price.replace(".","").toInt()
//
//        }
//        else{
//            MachinePrice = args.price!!.toInt()
//        }
//
//        return MachinePrice
//    }
//
////    private fun updateValueMachine(machine:String,id: Int, number : Int){
////        Log.d("checktitle", "Check title " + machine)
////        val separate = machine!!.split(" ")[0]
////        Log.d("checktitle", "Check title " + separate)
////        if(separate == "Washer"){
////            val updatedvalue = Washer(id,number,true)
////            mWasherViewModel.updateWasher(updatedvalue)
////        }
////        else{
////            val updatedvalue = Dryer(id,number,true)
////            mDryerViewModel.updateDryer(updatedvalue)
////        }
////    }
//
//    override fun onClick(p0: View?) {
//        when(p0!!.id){
//            R.id.ButtonBackList -> {
//                activity?.onBackPressed()
//            }
//            R.id.buttonCheckout -> {
//                statButton = false
////                updateValueMachine(args.titleBar!!, idMachine, number)
////                Log.d("checktitle", "Check title " + args.titleBar)
//
//                val separate1 = args.titleBar!!.split(" ")[0]
//                Log.d("checkrefid", "Check title " + args.titleBar)
//                Log.d("checkrefid", "Check title " + separate1)
////                Log.d("checkrefid", "Check Price Home " + HomeFragment.priceWasher)
//
//                if (separate1 == "Washer"){
//                    val pricemachineWasher = ContainsDot(HomeFragment.priceWasher)
//                    val action = ListMachineDirections.actionListMachineToQrisFragment(args.titleBar.toString(),number,pricemachineWasher)
//                    Navigation.findNavController(p0).navigate(action)
//                }
//                else if (separate1 == "Dryer"){
//                    val pricemachineDryer = ContainsDot(HomeFragment.priceDryer)
//                    val action = ListMachineDirections.actionListMachineToQrisFragment(args.titleBar.toString(),number,pricemachineDryer)
//                    Navigation.findNavController(p0).navigate(action)
//                }
//            }
//        }
//    }
}