package com.example.mylaundry.fragment

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.mylaundry.R
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.mylaundry.room.dryermachine.Dryer
import com.example.mylaundry.room.dryermachine.DryerViewModel
import com.example.mylaundry.room.settings.SettingViewModel
import com.example.mylaundry.room.settings.Settings
import com.example.mylaundry.room.washermachine.WasherViewModel
import me.abhinay.input.CurrencyEditText
import java.lang.Exception
import java.util.*


class EditFragment : Fragment(), View.OnClickListener {

    private val args : EditFragmentArgs by navArgs()

    private lateinit var mSettingViewModel : SettingViewModel
    private lateinit var mDryerViewModel : DryerViewModel
    private lateinit var mWasherViewModel : WasherViewModel

    private lateinit var BtnBack : ImageButton
    private lateinit var titleEdit : TextView

    private lateinit var buttonUp : ImageButton
    private lateinit var buttonDown : ImageButton
    private lateinit var UnitValue : TextView
    private lateinit var UnitTextView : TextView

    private lateinit var UnitTextViewIDR : TextView
    private lateinit var EditPrice : CurrencyEditText

    private lateinit var EditKey : EditText

    private lateinit var ButtonSave : Button

    private var title : String = ""
    private var valueMachine : Int = 0
    private var getvalueMachine : Int = 0
    private var statButton : Boolean = false
//    private var finishStat : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BtnBack = view.findViewById(R.id.ButtonBackEdit)
        titleEdit = view.findViewById(R.id.TitleEditValue)
        buttonUp = view.findViewById(R.id.buttonup)
        buttonDown = view.findViewById(R.id.buttondown)
        UnitValue = view.findViewById(R.id.TextNumber)
        UnitTextView = view.findViewById(R.id.UnitTextView)
        EditPrice = view.findViewById(R.id.editTextNumber)
        UnitTextViewIDR = view.findViewById(R.id.unitTextViewIDR)
        ButtonSave = view.findViewById(R.id.buttonSave)
        EditKey = view.findViewById(R.id.editTextKey)

        BtnBack.setOnClickListener(this)
        buttonUp.setOnClickListener(this)
        buttonDown.setOnClickListener(this)
        ButtonSave.setOnClickListener(this)

        mSettingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        mDryerViewModel = ViewModelProvider(this).get(DryerViewModel::class.java)
        mWasherViewModel = ViewModelProvider(this).get(WasherViewModel::class.java)

        titleEdit.text = args.title
        checkTitle()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkTitle(){
        val separate1 = args.title!!.split(" ")[1]
        if(separate1 == "machine"){
            editUnitValue()
            val home = HomeFragment()
            if (statButton == true){
                try {
                    var titleMachine = args.title.toString()
                    Log.d("check", "check title " + titleMachine)
                    if(args.title.toString()=="Washer machine"){
                        updateValue(getvalueMachine.toString(), titleMachine)
                        mWasherViewModel.delete()
                        home.insertDataWasher()
                    }
                    else{
                        updateValue(getvalueMachine.toString(), titleMachine)
                        mDryerViewModel.delete()
                        home.insertDataWasher()
                    }
                }
                catch (e: Exception){
                    Log.d("check", e.toString())
                }
            }
        }
        else if(separate1 == "price"){
            editPriceValue()
            if (statButton == true){
                try {
                    if(EditPrice.text.toString() == ""){
                        updateValue("0", args.title.toString())
                    }
                    else{
                        updateValue(EditPrice.text.toString(), args.title.toString())
                    }
                }
                catch (e: Exception){
                    Log.d("check", e.toString())
                }
            }
        }
        else if(separate1 == "settings"){
            editValue(false)
            if (statButton == true){
                try {
                    Log.d("check", "Ini stat " + statButton.toString())

                    val value = EditKey.text.toString()
                    val encodedString: String = Base64.getEncoder().encodeToString(value.toByteArray())

                    val decodedBytes = Base64.getDecoder().decode(encodedString)
                    val decodedString = String(decodedBytes)
//                    Log.d("checkED", "Ori : $value")
//                    Log.d("checkED", "Encode : $encodedString")
//                    Log.d("checkED", "Decode : $decodedString")
//                    updateValue(encodedString, args.title.toString())
                    updateValue(value, args.title.toString())
                    HomeFragment.passwordvar = EditKey.text.toString()
                }
                catch (e: Exception){
                    Log.d("check", e.toString())
                }
            }
        }
        else if(separate1 == "laundry"){
            editValue(false)
            if (statButton == true){
                try {
                    Log.d("check", "Ini stat " + statButton.toString())

                    val value = EditKey.text.toString()
                    updateValue(value, args.title.toString())
                }
                catch (e: Exception){
                    Log.d("check", e.toString())
                }
            }
        }
        else if(separate1 == "address"){
            editValue(false)
            if (statButton == true){
                try {
                    Log.d("check", "Ini stat " + statButton.toString())

                    val value = EditKey.text.toString()
                    updateValue(value, args.title.toString())
                }
                catch (e: Exception){
                    Log.d("check", e.toString())
                }
            }
        }
        else if(separate1 == "port"){
            editValue(true)
            if (statButton == true){
                try {
                    Log.d("check", "Ini stat " + statButton.toString())

                    val value = EditKey.text.toString()
                    updateValue(value, args.title.toString())
                }
                catch (e: Exception){
                    Log.d("check", e.toString())
                }
            }
        }
        else{
            editValue(false)
            if (statButton == true){
                try {
                    Log.d("check", "Ini stat " + statButton.toString())

                    val value = EditKey.text.toString()
                    val encodedString: String = Base64.getEncoder().encodeToString(value.toByteArray())

                    val decodedBytes = Base64.getDecoder().decode(encodedString)
                    val decodedString = String(decodedBytes)
//                    Log.d("checkED", "Ori : $value")
//                    Log.d("checkED", "Encode : $encodedString")
//                    Log.d("checkED", "Decode : $decodedString")
                    updateValue(encodedString, args.title.toString())
                }
                catch (e: Exception){
                    Log.d("check", e.toString())
                }
            }
        }
    }

    private fun insertData() {
        Log.d("check", "Show DryerMachine " + getvalueMachine.toString())
        for (i in 1..getvalueMachine){
            val dryer = Dryer(i-1, i, false)
            Log.d("check", (i-1).toString() + "     " + (i).toString())
            mDryerViewModel.addDryer(dryer)
        }
//        finishStat = true
    }

    private fun updateValue(value: String, title : String){
        val updatedvalue = Settings(args.idSetting,title,value)
        mSettingViewModel.updateSettings(updatedvalue)
    }

    private fun editUnitValue(){
        buttonUp.isVisible = true
        buttonDown.isVisible = true
        UnitValue.isVisible = true
        UnitTextView.isVisible = true
        UnitTextViewIDR.isVisible = false
        EditPrice.isVisible = false

        EditKey.isVisible = false

        UnitValue.text = args.value
        valueMachine = args.value!!.toInt()
        title = args.title.toString()
//        Log.d("check", "Ini stat " + statButton.toString())
    }

    private fun editPriceValue(){
        buttonUp.isVisible = false
        buttonDown.isVisible = false
        UnitValue.isVisible = false
        UnitTextView.isVisible = false
        UnitTextViewIDR.isVisible = true
        EditPrice.isVisible = true

        EditKey.isVisible = false

        EditPrice.setDelimiter(false)
        EditPrice.setSpacing(false)
        EditPrice.setDecimals(false)
        EditPrice.setSeparator(".")
    }

    private fun editValue(number: Boolean){
        buttonUp.isVisible = false
        buttonDown.isVisible = false
        UnitValue.isVisible = false
        UnitTextView.isVisible = false
        UnitTextViewIDR.isVisible = false
        EditPrice.isVisible = false
        EditKey.hint = "Enter Your ${args.title}"

        EditKey.isVisible = true
        if(number){
            EditKey.inputType = InputType.TYPE_CLASS_NUMBER
        }

        EditPrice.setDelimiter(false)
        EditPrice.setSpacing(false)
        EditPrice.setDecimals(false)
        EditPrice.setSeparator(".")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.ButtonBackEdit -> {
                activity?.onBackPressed()
            }
            R.id.buttonup -> {
                valueMachine++
                getvalueMachine = valueMachine
                UnitValue.text = valueMachine.toString()
            }
            R.id.buttondown -> {
                valueMachine--
                if(valueMachine <= 0){
                    valueMachine = 0
                }
                getvalueMachine = valueMachine
                UnitValue.text = valueMachine.toString()
            }
            R.id.buttonSave -> {
                statButton = true
                checkTitle()
                statButton = false
                if (statButton == false){
                    UnitValue.text = getvalueMachine.toString()
                    activity?.onBackPressed()
                }
            }
        }
    }
}