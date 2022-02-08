package com.example.mylaundry.fragment

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.system.ErrnoException
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mylaundry.R
import com.example.mylaundry.api.machine.ResponseMachine
import com.example.mylaundry.api.machine.ResponseUpdateMachine
import com.example.mylaundry.api.machine.RetrofitClientMachine
import com.example.mylaundry.api.qris.generate.GetResponseAPI
import com.example.mylaundry.api.qris.payment.ResponsePaymentAPI
import com.example.mylaundry.api.qris.generate.ResponseAPI
import com.example.mylaundry.api.qris.RetrofitClient
import com.example.mylaundry.api.payment.GetResponsePaymentAPI
import com.example.mylaundry.api.transactions.ResponseTransactions
import com.example.mylaundry.api.transactions.RetrofitClientTransactions
import com.example.mylaundry.room.transactions.Transactions
import com.example.mylaundry.room.transactions.TransactionsViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import java.net.*
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class QrisFragment : Fragment(), View.OnClickListener {

    private val args : QrisFragmentArgs by navArgs()

    private lateinit var BtnBack : ImageButton
    private lateinit var BtnOnMachine : Button
    private lateinit var BtnCheck : Button

    private lateinit var titleMachine : TextView

    private lateinit var QRImage : ImageView

    private lateinit var progressbar : ProgressBar

    private lateinit var CheckImage : ImageView

//    private lateinit var mDryerViewModel : DryerViewModel
//    private lateinit var mWasherViewModel : WasherViewModel
    private lateinit var mTransactionsViewModel : TransactionsViewModel

    private lateinit var uiScope : CoroutineScope

//    private lateinit var socket: Socket
//    private lateinit var socket: MulticastSocket
//    private lateinit var group: InetAddress
//    private lateinit var datagramPacket: DatagramPacket

    private var reffID : Long = 0
    private var successPayment : Boolean = false
    private var GenerateOK : Boolean = false

    private var getData = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qris, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BtnBack = view.findViewById(R.id.ButtonBackQR)
        BtnBack.setOnClickListener(this)

        BtnOnMachine = view.findViewById(R.id.ButtonOn)
        BtnOnMachine.setOnClickListener(this)
        buttonStat(false)

        BtnCheck = view.findViewById(R.id.ButtonCheck)
        BtnCheck.setOnClickListener(this)

        QRImage = view.findViewById(R.id.QRcodeImage)

        progressbar = view.findViewById(R.id.progressbar)

        CheckImage = view.findViewById(R.id.CheckImage)
        CheckImage.isVisible = false

        titleMachine = view.findViewById(R.id.TitleMachineLaundry)
        titleMachine.text = "${args.machineType} : ${args.machineNumber}"

//        mDryerViewModel = ViewModelProvider(this).get(DryerViewModel::class.java)
//        mWasherViewModel = ViewModelProvider(this).get(WasherViewModel::class.java)
        mTransactionsViewModel = ViewModelProvider(this).get(TransactionsViewModel::class.java)


        coroutineResponseAPI()
//        doBackDoor()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun coroutineResponseAPI(){
        var viewModelJob = Job()
        uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        uiScope.launch {
            withContext(Dispatchers.IO) {
//                getQRcode()
                while(!successPayment){
                    if(GenerateOK){
                        try {
                            getResponsePayment(reffID)
                        }
                        catch (e: Exception){
                            Log.d("checkrefid", e.toString())
                        }
                    }
                    delay(1000L)
                }
                withContext(Dispatchers.Main){
                    if (successPayment){
                        CheckImage.isVisible = true
                        buttonStat(true)
                    }
                    else{
                        CheckImage.isVisible = false
                        buttonStat(false)
                    }
                }
            }
        }
    }

    fun getRandomString(length: Int) : String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    fun buttonStat(button: Boolean) {
        if(button){
            BtnOnMachine.isEnabled =true
            BtnOnMachine.setBackgroundResource(R.drawable.header_layout)
        }
        else{
            BtnOnMachine.isEnabled =false
            BtnOnMachine.setBackgroundResource(R.drawable.gray_button)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun responsePaymentAPI(refidPayment : Long?){
        val Parameter = ResponsePaymentAPI(refidPayment)

        var cID = HomeFragment.clientIDvar
        var cSK = HomeFragment.clientSKvar
        var mID = HomeFragment.merchantIDvar

        var deccID = Base64.getDecoder().decode(cID)
        var deccSK = Base64.getDecoder().decode(cSK)
        var decmID = Base64.getDecoder().decode(mID)

        var cIDstring = String(deccID)
        var cSKstring = String(deccSK)
        var mIDstring = String(decmID)

        val code = "${cIDstring}:${cSKstring}:${mIDstring}"
//        val code = "6a7ba6b1a2e6eaf211bfc87c2ba7b6dc:1acb950632a327bd45638e16c6766bef:210910003000000"
        val encodedString: String = Base64.getEncoder().encodeToString(code.toByteArray())

//        Log.d("checkrefid", "On Get Response : $Parameter" )
//        Log.d("checkrefid", "On Get Response")
//        Log.d("checkrefid", "On Get Response $successPayment")
        RetrofitClient.instance.CheckPayment("Bearer "+ encodedString,Parameter).enqueue(object : Callback<GetResponsePaymentAPI> {
            override fun onResponse(
                call: Call<GetResponsePaymentAPI>,
                response: Response<GetResponsePaymentAPI>
            ) {
                Log.d("checksplit", "code Encode Response : $encodedString")
                if (response.code() == 200) {
//                    Log.d("checkrefid", "Response 200 OK")
                    if (response.body()?.status == "success"){
                        successPayment = true
                        Log.d("checkrefid", "Payment Success")
                    }
                    else{
                        successPayment = false
                        Log.d("checkrefid", "Payment Active : $Parameter")
                    }
                }
                else {
                    successPayment = false
                    Log.d("checkrefid", "Respon code : "+response.code().toString())
                }
            }

            override fun onFailure(call: Call<GetResponsePaymentAPI>, t: Throwable) {
                if (t.message == t.message){
                    Log.d("checkrefid", "On Get Response ERROR : ${t.message}")
                    Toast.makeText(requireContext(), "Error" , Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getResponsePayment(refidPayment : Long?){
        try {
            responsePaymentAPI(refidPayment)
        }
        catch (e: Exception){
            Log.d("check", e.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getQRcode(){
        BtnOnMachine.isEnabled = false
        val length = 20
        val randomReff = getRandomString(length)

        var cID = HomeFragment.clientIDvar
        var cSK = HomeFragment.clientSKvar
        var mID = HomeFragment.merchantIDvar

        var deccID = Base64.getDecoder().decode(cID)
        var deccSK = Base64.getDecoder().decode(cSK)
        var decmID = Base64.getDecoder().decode(mID)

        var cIDstring = String(deccID)
        var cSKstring = String(deccSK)
        var mIDstring = String(decmID)

        val QRParameter = ResponseAPI(mIDstring,args.price,0,randomReff,"https://webhook.site/73121a4e-5dd9-423d-980b-0ace6c719b90",5)

        val code = "${cIDstring}:${cSKstring}:${mIDstring}"
//        val code = "6a7ba6b1a2e6eaf211bfc87c2ba7b6dc:1acb950632a327bd45638e16c6766bef:210910003000000"
        val encodedString: String = Base64.getEncoder().encodeToString(code.toByteArray())

        try {
            RetrofitClient.instance.Qris("Bearer "+ encodedString,QRParameter).enqueue(object : Callback<GetResponseAPI> {
                override fun onResponse(
                    call: Call<GetResponseAPI>,
                    response: Response<GetResponseAPI>
                ) {
                    Log.d("checksplit", "code Encode : $encodedString")
                    showLoading(true)
                    if (response.code() == 200) {
                        if (response.body()?.status == "success"){
                            showLoading(false)
                            GenerateOK = true
                            QRGenerator(response.body()?.rawqr.toString())
                            reffID = response.body()?.refid!!
//                            Log.d("checkrefid", "Check Get RefID " + reffID)
                        }
                        else{
                            getQRcode()
                        }
                    } else {
                        showLoading(true)
                    }
                }

                override fun onFailure(call: Call<GetResponseAPI>, t: Throwable) {
                    if (t.message == t.message){
                        Log.d("checkrefid", "On Get Response Generate ERROR : ${t.message}")
                        Toast.makeText(requireContext(), "Tidak ada koneksi Internet" , Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        catch (e: Exception){
            Log.d("check", e.toString())
        }

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressbar.visibility = View.VISIBLE
        } else {
            progressbar.visibility = View.GONE
        }
    }

    private fun QRGenerator(code :String){
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(code, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        QRImage.setImageBitmap(bitmap)
    }

//    private fun updateValueMachine(machine:String,id: Int, number : Int){
////        Log.d("checktitle", "Check title " + machine)
//        val separate = machine.split(" ")[0]
////        Log.d("checktitle", "Check title " + separate)
//        if(separate == "Washer"){
//            val updatedvalue = Washer(id,number,true)
//            mWasherViewModel.updateWasher(updatedvalue)
//        }
//        else{
//            val updatedvalue = Dryer(id,number,true)
//            mDryerViewModel.updateDryer(updatedvalue)
//        }
//    }

    override fun onStop() {
        super.onStop()
        successPayment = false
        GenerateOK = false
        uiScope.cancel()
    }

    private fun addTransactions(){
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val simpleTimeFormat = SimpleDateFormat("HH:mm:ss")
        val currentDate: String = simpleDateFormat.format(Date())
        val currentTime: String = simpleTimeFormat.format(Date())
        val transactions = Transactions(
            typeMachine = args.machineType.toString(),
            noMachine = args.machineNumber,
            date =currentDate,
            timeMachine = currentTime,
            priceMachine = args.price
        )
        mTransactionsViewModel.addTransactions(transactions)
    }

    private fun rentMachine(){

        val bodyUpdate = ResponseUpdateMachine(true)

        RetrofitClientMachine.instance.putMachine(args.machineId, bodyUpdate).enqueue(object : Callback<ResponseUpdateMachine> {
            override fun onResponse(call: Call<ResponseUpdateMachine>, response: Response<ResponseUpdateMachine>) {
                Log.d("retrofitput", args.machineId.toString())
                Log.d("retrofitput", response.code().toString())
                Log.d("retrofitput", response.body().toString())

//                BtnOnMachine.isEnabled =false

                insertTransactions()

            }

            override fun onFailure(call: Call<ResponseUpdateMachine>, t: Throwable) {
                Log.d("error", t.message.toString())
                if (t.message == t.message){

//                    BtnOnMachine.isEnabled =true

                    Toast.makeText(requireContext(), "Tidak ada koneksi Internet" , Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun insertTransactions(){

        val current = LocalDateTime.now()

        val formatDay = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = current.format(formatDay)

        val formatTime = DateTimeFormatter.ofPattern("HH:mm")
        val time = current.format(formatTime)

        val bodyUpdate = ResponseTransactions(
            date,
            args.machineNumber,
            args.price.toString(),
            time,
            args.machineType
        )

        RetrofitClientTransactions.instance.insertTransactions(bodyUpdate).enqueue(object : Callback<ResponseTransactions> {
            override fun onResponse(call: Call<ResponseTransactions>, response: Response<ResponseTransactions>) {
                Log.d("retrofitinsert", args.machineId.toString())
//                Log.d("retrofitinsert", response.)
                Log.d("retrofitinsert", response.code().toString())
                Log.d("retrofitinsert", response.body().toString())

//                BtnOnMachine.isEnabled =false

                findNavController().navigate(R.id.action_qrisFragment_to_homeFragment)

            }

            override fun onFailure(call: Call<ResponseTransactions>, t: Throwable) {
                Log.d("error", t.message.toString())
                if (t.message == t.message){

//                    BtnOnMachine.isEnabled =true

                    Toast.makeText(requireContext(), "Tidak ada koneksi Internet" , Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.ButtonBackQR -> {
                successPayment = false
                GenerateOK = false
                activity?.onBackPressed()
            }
            R.id.ButtonOn -> {
                rentMachine()
//                BtnOnMachine.isEnabled =false

//                Toast.makeText(requireContext(), "Reveived Data : ${SocketPrograming.getData}" , Toast.LENGTH_SHORT).show()

//                successPayment = false
//                GenerateOK = false
//                updateValueMachine(args.machineType!!, ListMachine.idMachine, ListMachine.number)
//                activity?.onBackPressed()
            }
            R.id.ButtonCheck -> {
                successPayment = true
                buttonStat(true)
                showLoading(false)
//                GenerateOK = false
            }
        }
    }
}