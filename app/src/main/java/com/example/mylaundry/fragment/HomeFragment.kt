package com.example.mylaundry.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.mylaundry.R
import com.example.mylaundry.room.settings.SettingDatabaseGet
import com.example.mylaundry.room.settings.Settings
import com.example.mylaundry.room.transactions.Transactions
import com.example.mylaundry.room.transactions.TransactionsDatabaseGet
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception
import android.widget.Toast
import android.app.Activity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import com.example.mylaundry.api.machine.ResponseMachine
import com.example.mylaundry.api.machine.RetrofitClientMachine
import com.example.mylaundry.excel.CreateExcel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters

    private lateinit var uiScope : CoroutineScope

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

    private lateinit var buttonService : Button

    private lateinit var db: SettingDatabaseGet
    private lateinit var dbTransactions: TransactionsDatabaseGet

    private lateinit var dataExcel: List<Transactions>

    private var statOK : Boolean = false
    private var washOK : Boolean = false
    private var dryerOK : Boolean = false
    private var AvailableOK : Boolean = false
    private var okCreate : Boolean = true

    companion object{
        var washerMachine : Int = 0
        var dryerMachine : Int = 0
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

    private val filePath: File = File(Environment.getExternalStorageDirectory().toString() + "/Demo.xls")

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
        dbTransactions = Room.databaseBuilder(requireContext(), TransactionsDatabaseGet::class.java, "TransactionDatabase").build()

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

        buttonService = view.findViewById(R.id.btnSeeAllTransactions)
        buttonService.setOnClickListener(this)

        var viewModelJob = Job()
        uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        statOK = false
        washOK = false
        dryerOK = false
        AvailableOK = false

        askPermision()
        viewUI(false)
        getdataMachine()

    }

    private fun askPermision(){
        if (ActivityCompat.checkSelfPermission(
                requireContext()!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (context as Activity?)!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
            return
        }
    }

    private fun getdataMachine()
    {
        ListMachine.listWasher.clear()
        ListMachine.listDryer.clear()
        ListMachine.listMachine.clear()
        ListMachine.listDryerUse.clear()
        ListMachine.listWasherUse.clear()

        RetrofitClientMachine.instance.getMachine().enqueue(object : Callback<List<ResponseMachine>> {
            override fun onResponse(call: Call<List<ResponseMachine>>, response: Response<List<ResponseMachine>>) {
                Log.d("retrofit", "Code : ${response.code().toString()}")
//                Log.d("retrofit", "Code : ${response.body().toString()}")
                response.body()?.let {
                    ListMachine.listMachine.addAll(it)
                }

                for (a in ListMachine.listMachine){
                    if (a.machineType == 0){
                        ListMachine.listWasher.add(a)

                        if (a.machineStatus == true){
                            ListMachine.listWasherUse.add(a)
                        }
                    }
                    else{
                        ListMachine.listDryer.add(a)
                        if (a.machineStatus == true){
                            ListMachine.listDryerUse.add(a)
                        }
                    }
                    Log.d("retrofit", "Code : ${a.machineType}")
                    Log.d("retrofit", "Code : ${a}")
                }
                viewUI(true)

            }

            override fun onFailure(call: Call<List<ResponseMachine>>, t: Throwable) {
                Log.d("p2", t.message.toString())
                if (t.message == t.message){
                    Toast.makeText(requireContext(), "Tidak ada koneksi Internet" , Toast.LENGTH_SHORT).show()
                }
            }

        })
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
            WasherMachineAvailabe.text = "${ListMachine.listWasher.size-ListMachine.listWasherUse.size} Machine"
            DryerMachineAvailabe.text = "${ListMachine.listDryer.size-ListMachine.listDryerUse.size} Machine"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        loadData(true)
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
                        dataExcel = dbTransactions.dataDao().getAllData()
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
                      titleLaundryHomeTv.text = nameStorevar
                }
            }
        }
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

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.imageButton -> {
                findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
            }
            R.id.CardWasher -> {
                val action = HomeFragmentDirections.actionHomeFragmentToListMachine("Washer Machine", priceWasher,0)
                Navigation.findNavController(p0).navigate(action)
            }
            R.id.CardDryer -> {
                val action = HomeFragmentDirections.actionHomeFragmentToListMachine("Dryer Machine", priceDryer, 1)
                Navigation.findNavController(p0).navigate(action)
            }
            R.id.btnSeeAllTransactions -> {
                val excel = CreateExcel()
                excel.createExcelSheet()
//                Toast.makeText(requireContext(), "Tidak ada koneksi Internet" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}