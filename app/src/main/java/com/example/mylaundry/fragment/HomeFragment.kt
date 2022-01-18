package com.example.mylaundry.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.mylaundry.R
import com.example.mylaundry.room.dryermachine.Dryer
import com.example.mylaundry.room.dryermachine.DryerDatabaseGet
import com.example.mylaundry.room.dryermachine.DryerViewModel
import com.example.mylaundry.room.settings.SettingDatabaseGet
import com.example.mylaundry.room.settings.Settings
import com.example.mylaundry.room.washermachine.Washer
import com.example.mylaundry.room.washermachine.WasherDatabaseGet
import com.example.mylaundry.room.washermachine.WasherViewModel
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters

    private lateinit var BtnSetting : ImageButton
    private lateinit var cardWasher : CardView
    private lateinit var cardDryer : CardView

    private lateinit var WasherMachineAvailabe : TextView
    private lateinit var DryerMachineAvailabe : TextView
    private lateinit var titleLaundryHomeTv : TextView

    private lateinit var titleAvailableWash : TextView
    private lateinit var titleAvailableDryer : TextView

    private lateinit var progressWasher : ProgressBar
    private lateinit var progressDryer : ProgressBar

    lateinit var db: SettingDatabaseGet
    lateinit var washerdb: WasherDatabaseGet
    lateinit var dryerdb: DryerDatabaseGet

    private var statOK : Boolean = false
    private var washOK : Boolean = false
    private var dryerOK : Boolean = false
    private var AvailableOK : Boolean = false
//    private var whileOFF : Boolean = true

    companion object{
        var washerMachine : Int = 0
        var dryerMachine : Int = 0
        var washerMachineUsed : Int = 0
        var dryerMachineUsed : Int = 0
        var priceWasher : String = ""
        var priceDryer : String = ""
        var clientIDvar : String = ""
        var clientSKvar : String = ""
        var merchantIDvar : String = ""
        var nameStorevar : String = ""
        var passwordvar : String = ""
        var ipvar : String = ""
        var portvar : String = ""
    }

    val numberMachine = listOf<Int>()



    private lateinit var mDryerViewModel : DryerViewModel
    private lateinit var mWasherViewModel : WasherViewModel
//
//    private val listSettings = emptyList<Settings>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Room.databaseBuilder(requireContext(), SettingDatabaseGet::class.java, "LaundryDatabase").build()
        washerdb = Room.databaseBuilder(requireContext(), WasherDatabaseGet::class.java, "WasherDatabase").build()
        dryerdb = Room.databaseBuilder(requireContext(), DryerDatabaseGet::class.java, "DryerDatabase").build()

        mDryerViewModel = ViewModelProvider(this).get(DryerViewModel::class.java)
        mWasherViewModel = ViewModelProvider(this).get(WasherViewModel::class.java)

        BtnSetting = view.findViewById(R.id.imageButton)
        BtnSetting.setOnClickListener(this)

        cardWasher = view.findViewById(R.id.CardWasher)
        cardWasher.setOnClickListener(this)
        cardDryer = view.findViewById(R.id.CardDryer)
        cardDryer.setOnClickListener(this)

        WasherMachineAvailabe = view.findViewById(R.id.textView5)
        DryerMachineAvailabe = view.findViewById(R.id.textView3)
        titleLaundryHomeTv = view.findViewById(R.id.titleLaundryHome)

        titleAvailableWash = view.findViewById(R.id.textView4)
        titleAvailableDryer = view.findViewById(R.id.textView2)

        progressWasher = view.findViewById(R.id.progressbar_washer)
        progressDryer = view.findViewById(R.id.progressbar_dryer)

        statOK = false
        washOK = false
        dryerOK = false
        AvailableOK = false

//        loadData(true)

    }

    private fun viewUI(stat:Boolean){
        titleLaundryHomeTv.text = nameStorevar
        if(!stat){
            titleAvailableWash.isVisible = false
            titleAvailableDryer.isVisible = false
            WasherMachineAvailabe.isVisible = false
            DryerMachineAvailabe.isVisible = false
            progressWasher.isVisible = true
            progressDryer.isVisible = true
        }
        else{
            titleAvailableWash.isVisible = true
            titleAvailableDryer.isVisible = true
            WasherMachineAvailabe.isVisible = true
            DryerMachineAvailabe.isVisible = true
            progressWasher.isVisible = false
            progressDryer.isVisible = false
            WasherMachineAvailabe.text = "${washerMachine-washerMachineUsed} Machine"
            DryerMachineAvailabe.text = "${dryerMachine-dryerMachineUsed} Machine"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
//        getDataMachine()
//        loadData2()
        loadData(true)
//        loadData3()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData(statInsert : Boolean){
        var viewModelJob = Job()
        var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        uiScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    if (statInsert){
                        getDataMachine()
                        insertDataDryer()
                        insertDataWasher()
                        getDatamachineAvailable()
                        Log.d("run", "Routine")
                    }
                    else{
                        getDataMachine()
                        Log.d("run", "Routine")
                    }
                }
                catch (e: Exception){
                    Log.d("check", e.toString())
                }
                withContext(Dispatchers.Main){

                    Log.d("check", "While ON")
                    Log.d("check", "statOK $statOK")
                    Log.d("check", "washOK $washOK")
                    Log.d("check", "dryerOK $dryerOK")
                    Log.d("check", "Available $AvailableOK")
                    if (statOK && washOK && dryerOK && AvailableOK){
                        viewUI(true)
                    }
                    else{
                        viewUI(false)
                    }
                    Log.d("check", "While OFF")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData2(){
        GlobalScope.launch {
            try {
                getDataMachine()
//                insertDataDryer()
//                insertDataWasher()
                Log.d("run", "Routine")
            }
            catch (e: Exception){
                Log.d("check", e.toString())
            }
        }
        viewUI(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadData3(){
        try {
            getDataMachine()
            insertDataDryer()
            insertDataWasher()
            Log.d("run", "Routine")
        }
        catch (e: Exception){
            Log.d("check", e.toString())
        }
    }

    private fun getDatamachineAvailable(){
        washerMachineUsed = 0
        dryerMachineUsed = 0

        val dataWasherAvailable: List<Washer> = washerdb.dataDao().getAllData()
        val dataDryerAvailable: List<Dryer> = dryerdb.dataDao().getAllData()

        for (valuewasher in dataWasherAvailable){
            if(valuewasher.isActive == true){
                washerMachineUsed++
            }
        }

        for (valuedryer in dataDryerAvailable){
            if(valuedryer.isActive == true){
                dryerMachineUsed++
            }
        }

        Log.d("checkget", "Check Get Washer " + washerMachine)
        Log.d("checkget", "Check Get Washer Used " + washerMachineUsed)

        Log.d("checkget", "Check Get Dryer " + dryerMachine)
        Log.d("checkget", "Check Get Dryer Used " + dryerMachineUsed)


        AvailableOK = true

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDataMachine() {

        val data: List<Settings> = db.dataDao().getAllData()

        for (value in data){
            val separate1 = value.nameSetting!!.split(" ")[1]
            if(separate1 == "machine"){
                val separate = value.nameSetting!!.split(" ")[0]
                if (separate == "Washer"){

                    washerMachine = value.valueSetting!!.toInt()
                }
                else if (separate == "Dryer"){
                    dryerMachine = value.valueSetting!!.toInt()
                }
            }
            else if(separate1 == "price"){
                val separate = value.nameSetting!!.split(" ")[0]
                if (separate == "Washer"){
                    priceWasher = value.valueSetting!!
                    Log.d("checkget", "Check Get Washer " + washerMachine)
                }
                else if (separate == "Dryer"){
                    priceDryer = value.valueSetting!!
                    Log.d("checkget", "Check Get Dryer " + dryerMachine)
                }
            }
            else if(separate1 == "laundry"){
//                val decodedBytes = Base64.getDecoder().decode(value.valueSetting.toString())
//                val decodedString = String(decodedBytes)

//                nameStorevar = decodedString
                nameStorevar = value.valueSetting.toString()
            }
            else if(separate1 == "settings"){
                passwordvar = value.valueSetting.toString()
            }
            else if(separate1 == "address"){
                ipvar = value.valueSetting.toString()
            }
            else if(separate1 == "port"){
                portvar = value.valueSetting.toString()
            }
            else{
                val separate = value.nameSetting!!.split(" ")[0]
//                Log.d("checksplit", "separate : $separate")
                if(separate == "Client"){
                    clientIDvar = value.valueSetting.toString()
//                    Log.d("checksplit", "separate : $clientIDvar")
                }
                else if(separate == "Key"){
                    clientSKvar = value.valueSetting.toString()
//                    Log.d("checksplit", "separate : $clientSKvar")
                }
                else{
                    merchantIDvar = value.valueSetting.toString()
//                    Log.d("checksplit", "separate : $merchantIDvar")
                }
            }
        }
        statOK = true

    }

    fun insertDataDryer() {
        Log.d("check", "Show DryerMachine " + dryerMachine.toString())
        if(dryerMachine > 0){
            for (i in 1..dryerMachine){
                val dryer = Dryer(i-1, i, false)
//                Log.d("check", i.toString() + "     " + (i+1).toString())
                mDryerViewModel.addDryer(dryer)
            }
        }
        dryerOK = true
    }

    fun insertDataWasher() {
        Log.d("check", "Show WasherMachine " + washerMachine.toString())
        if(washerMachine > 0){
            for (i in 1..washerMachine){
                val washer = Washer(i-1, i, false)
//                Log.d("check", i.toString() + "     " + (i+1).toString())
                mWasherViewModel.addWasher(washer)
            }
        }
        washOK = true
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imageButton -> {
                findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
            }
            R.id.CardWasher -> {
                val action = HomeFragmentDirections.actionHomeFragmentToListMachine("Washer Machine", priceWasher)
                Navigation.findNavController(p0).navigate(action)
            }
            R.id.CardDryer -> {
                val action = HomeFragmentDirections.actionHomeFragmentToListMachine("Dryer Machine", priceDryer)
                Navigation.findNavController(p0).navigate(action)
            }
        }
    }
}