package com.example.mylaundry.data

object SettingData {
    private val titleNames = arrayOf(
        "Washer machine",
        "Dryer machine",
        "Washer price",
        "Dryer price",
        "Key Wallet"
    )

    private val titleDetails = arrayOf(
        12,
        12,
        "25.000",
        "20.000",
        "JHu28h36rkfl!@#n2ieh6eh2k127ng5d"
    )


    val listData: ArrayList<Setting>
        get() {
            val list = arrayListOf<Setting>()
            for (position in titleNames.indices) {
                val setting = Setting()
                setting.titleNames = titleNames[position]
                setting.valueNames = titleDetails[position].toString()
                list.add(setting)
            }
            return list
        }
}