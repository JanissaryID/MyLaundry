package com.example.mylaundry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.mylaundry.room.settings.SettingViewModel
import com.example.mylaundry.room.settings.Settings
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    val titleName = listOf<String>("Name laundry","Washer price","Dryer price","Client ID","Key Client","Merchant ID","Password settings","server address","server port")
    val valueName = listOf<Any>("Bubble Laundry",100,100,"bd273!@#-*(NDas","bd273!@#-*(NDasasdasd","210000000000000","admin","", "")

    private lateinit var mSettingViewModel : SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSettingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        try {
            insertData()
        }
        catch (e:Exception){
            Log.d("check", e.toString())
        }
    }

    private fun insertData() {
        titleName.forEachIndexed { index, s ->
//            Log.d("checkIndex", index.toString() + "    " + s)
            val settings = Settings(index, titleName[index], valueName[index].toString())
            mSettingViewModel.addSettings(settings)
        }

    }
}