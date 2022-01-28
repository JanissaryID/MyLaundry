package com.example.mylaundry.fragment

import android.Manifest
import android.content.Context
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.mylaundry.R
import com.example.mylaundry.excel.ExportToExcel
import com.example.mylaundry.room.dryermachine.Dryer
import com.example.mylaundry.room.dryermachine.DryerDatabaseGet
import com.example.mylaundry.room.dryermachine.DryerViewModel
import com.example.mylaundry.room.settings.SettingDatabaseGet
import com.example.mylaundry.room.settings.Settings
import com.example.mylaundry.room.transactions.Transactions
import com.example.mylaundry.room.transactions.TransactionsDatabaseGet
import com.example.mylaundry.room.washermachine.Washer
import com.example.mylaundry.room.washermachine.WasherDatabaseGet
import com.example.mylaundry.room.washermachine.WasherViewModel
import com.example.mylaundry.socket.SocketPrograming
import kotlinx.coroutines.*
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*
import android.widget.Toast

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import android.app.Activity

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.os.Build.VERSION_CODES.Q
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mylaundry.services.ForegroundServices
import com.google.ar.core.Config
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.runner.RunWith
import java.io.FileNotFoundException
import org.apache.poi.hssf.usermodel.HSSFCell

import org.apache.poi.hssf.usermodel.HSSFRow

import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.xssf.usermodel.XSSFSheet







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
    private lateinit var washerdb: WasherDatabaseGet
    private lateinit var dryerdb: DryerDatabaseGet
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
//    val dataExcel: List<Transactions> =

    private val filePath: File = File(Environment.getExternalStorageDirectory().toString() + "/Demo.xls")

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

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");

        db = Room.databaseBuilder(requireContext(), SettingDatabaseGet::class.java, "LaundryDatabase").build()
        washerdb = Room.databaseBuilder(requireContext(), WasherDatabaseGet::class.java, "WasherDatabase").build()
        dryerdb = Room.databaseBuilder(requireContext(), DryerDatabaseGet::class.java, "DryerDatabase").build()
        dbTransactions = Room.databaseBuilder(requireContext(), TransactionsDatabaseGet::class.java, "TransactionDatabase").build()

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

        buttonService = view.findViewById(R.id.btnService)
        buttonService.setOnClickListener(this)

        var viewModelJob = Job()
        uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        statOK = false
        washOK = false
        dryerOK = false
        AvailableOK = false

        askPermision()



//        loadData(true)

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
        ForegroundServices.startService(requireContext(), "Laundry service is running")
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

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun loadData2(){
//        GlobalScope.launch {
//            try {
//                if (SocketPrograming.statGetData){
//                    Log.d("getdata", "Get Data Service � : ${SocketPrograming.getData}")
//                    Toast.makeText(requireContext(), "Received Data : ${SocketPrograming.getData}" , Toast.LENGTH_SHORT).show()
//                }
//            }
//            catch (e: Exception){
//                Log.d("check", e.toString())
//            }
//        }
//        viewUI(true)
//    }

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

    fun createXlFile() {

        // File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
        val wb: Workbook = HSSFWorkbook()
        var cell: Cell? = null
        var sheet: Sheet? = null
        sheet = wb.createSheet("Demo Excel Sheet")
        //Now column and row
        val row: Row = sheet.createRow(0)
        cell = row.createCell(0)
        cell.setCellValue("Type Machine")
        cell = row.createCell(1)
        cell.setCellValue("No Machine")
        cell = row.createCell(2)
        cell.setCellValue("Date")
        cell = row.createCell(3)
        cell.setCellValue("Time")
        cell = row.createCell(4)
        cell.setCellValue("Price")

        //column width
        sheet.setColumnWidth(0, 20 * 200)
        sheet.setColumnWidth(1, 30 * 200)
        sheet.setColumnWidth(2, 30 * 200)
        sheet.setColumnWidth(3, 30 * 200)
        sheet.setColumnWidth(4, 30 * 200)

        var a = 0
        for (value in dataExcel){
            val row1: Row = sheet.createRow(a + 1)
            cell = row1.createCell(0)
            cell.setCellValue(value.typeMachine)
            Log.d("ok", value.typeMachine.toString())
            cell = row1.createCell(1)
            cell.setCellValue(value.noMachine.toString())
            Log.d("ok", value.noMachine.toString())
            cell = row1.createCell(2)
            cell.setCellValue(value.date.toString())
            Log.d("ok", value.date.toString())
            cell = row1.createCell(3)
            cell.setCellValue(value.timeMachine.toString())
            Log.d("ok", value.timeMachine.toString())
            cell = row1.createCell(4)
            cell.setCellValue(value.priceMachine.toString())
            Log.d("ok", value.priceMachine.toString())

            sheet.setColumnWidth(0, 20 * 200)
            sheet.setColumnWidth(1, 30 * 200)
            sheet.setColumnWidth(2, 30 * 200)
            sheet.setColumnWidth(3, 30 * 200)
            sheet.setColumnWidth(4, 30 * 200)
        }
        a = 0
        Log.d("ok", "OK EXCEL")

        val folderName = "Import Excel"
        val fileName = folderName + System.currentTimeMillis() + ".xls"
//        val path = Environment.getExternalStorageDirectory().path + ""
//        val file = File(Environment.getExternalStorageDirectory()
        val path: String = Environment.getExternalStorageDirectory().path + File.separator.toString() + folderName + File.separator.toString() + fileName
        val file = File(Environment.getExternalStorageDirectory().path + File.separator.toString() + folderName)
        if (!file.exists()) {
            file.mkdirs()
        }
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(path)
            wb.write(outputStream)
            // ShareViaEmail(file.getParentFile().getName(),file.getName());
            Toast.makeText(ApplicationProvider.getApplicationContext<Context>(), "Excel Created in $path", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(
                ApplicationProvider.getApplicationContext<Context>(),
                "Not OK",
                Toast.LENGTH_LONG
            ).show()
            try {
                outputStream!!.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun exsel(){
        // Blank workbook
        // Blank workbook
        val workbook = XSSFWorkbook()

        // Creating a blank Excel sheet

        // Creating a blank Excel sheet
        val sheet = workbook.createSheet("student Details")

        // Creating an empty TreeMap of string and Object][]
        // type

        // Creating an empty TreeMap of string and Object][]
        // type
        val data: MutableMap<String, Array<Any>> = TreeMap()

        // Writing data to Object[]
        // using put() method

        // Writing data to Object[]
        // using put() method
        data["1"] = arrayOf("ID", "NAME", "LASTNAME")
        data["2"] = arrayOf(1, "Pankaj", "Kumar")
        data["3"] = arrayOf(2, "Prakashni", "Yadav")
        data["4"] = arrayOf(3, "Ayan", "Mondal")
        data["5"] = arrayOf(4, "Virat", "kohli")

        // Iterating over data and writing it to sheet

        // Iterating over data and writing it to sheet
        val keyset: Set<String> = data.keys

        var rownum = 0

        for (key in keyset) {

            // Creating a new row in the sheet
            val row: Row = sheet.createRow(rownum++)
            val objArr = data[key]!!
            var cellnum = 0
            for (obj in objArr) {

                // This line creates a cell in the next
                //  column of that row
                val cell = row.createCell(cellnum++)
                if (obj is String) cell.setCellValue(obj as String) else if (obj is Int) cell.setCellValue(
                    (obj as Int?)!!.toDouble()
                )
            }
        }

        // Try block to check for exceptions

        // Try block to check for exceptions
        try {

            // Writing the workbook
            val out = FileOutputStream(
                File("gfgcontribute.xlsx")
            )
            workbook.write(out)

            // Closing file output connections
            out.close()

            // Console message for successful execution of
            // program
            println(
                "gfgcontribute.xlsx written successfully on disk."
            )
        } // Catch block to handle exceptions
        catch (e: Exception) {

            // Display exceptions along with line number
            // using printStackTrace() method
            e.printStackTrace()
        }
    }

    private fun backDoorTransactions(){
        uiScope.launch {
            withContext(Dispatchers.IO) {

                createXlFile()
                withContext(Dispatchers.Main){
                    if (okCreate){
                        Toast.makeText(requireContext(), "Excel Created in $filePath", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(), "Not OK", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
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
            R.id.btnService -> {
                exsel()
//                backDoorTransactions()
//                createXlFile()
//                Toast.makeText(requireContext(), "Tidak ada koneksi Internet" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}