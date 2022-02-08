package com.example.mylaundry.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylaundry.R
import com.example.mylaundry.adapter.SettingAdapter
import com.example.mylaundry.room.settings.SettingViewModel
import com.example.mylaundry.room.settings.Settings
import kotlinx.android.synthetic.main.dialog_login.*
import kotlinx.android.synthetic.main.dialog_login.view.*


class SettingFragment : Fragment(), View.OnClickListener {

    private lateinit var BtnBack : ImageButton
    private lateinit var rvSetting: RecyclerView
//    private lateinit var titleLaundry : TextView

    private lateinit var mSettingViewModel : SettingViewModel

    var passStat : Boolean = false

    private val listSettings = emptyList<Settings>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BtnBack = view.findViewById(R.id.ButtonBack)
        BtnBack.setOnClickListener(this)

        rvSetting = view.findViewById(R.id.rvsetting)
        rvSetting.setHasFixedSize(true)

        showRecyclerList()
        if (!passStat){
            showCustomLogin()
        }
    }

    private fun showCustomLogin() {
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_login, null)
        val mBuilder = AlertDialog.Builder(requireContext())
            .setView(mDialogView)
            .setCancelable(false)

        mDialogView.titleDialog.text = HomeFragment.nameStorevar
        val mAlertDialog = mBuilder.show()

        mDialogView.buttonEnter.setOnClickListener {
            val password = mDialogView.InputPassword.text.toString()

            if (password == "admin" || password == HomeFragment.passwordvar.toString()){
                passStat = true
                mAlertDialog.dismiss()
            }
            else{
                Toast.makeText(requireContext(), "Password Incorrect" , Toast.LENGTH_SHORT).show()
            }
        }

        mDialogView.buttonBack.setOnClickListener {
            mAlertDialog.dismiss()
            activity?.onBackPressed()
            passStat = false
        }
    }

    private fun showRecyclerList() {
        val adapter = SettingAdapter(listSettings)
        rvSetting.layoutManager = LinearLayoutManager(requireContext())
        rvSetting.adapter = adapter
        rvSetting.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        mSettingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        mSettingViewModel.readAllData.observe(viewLifecycleOwner, Observer { setting ->
            adapter.setData(setting)
        })

    }

    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.ButtonBack -> {
                activity?.onBackPressed()
                passStat = false
            }
        }
    }
}