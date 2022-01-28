package com.example.mylaundry.excel

import org.apache.poi.hssf.usermodel.HSSFCell

import org.apache.poi.hssf.usermodel.HSSFRow

import org.apache.poi.hssf.usermodel.HSSFSheet

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.lang.Exception
import android.os.Environment
import java.io.File
import java.io.FileOutputStream


class createExcel {

    private val filePath: File = File(Environment.getExternalStorageDirectory().toString() + "/Demo.xls")

    fun excelCreate(){
        val hssfWorkbook = HSSFWorkbook()
        val hssfSheet = hssfWorkbook.createSheet("Custom Sheet")

        val hssfRow = hssfSheet.createRow(0)
        val hssfCell = hssfRow.createCell(0)

        hssfCell.setCellValue("Hello World")

        try {
            if (!filePath.exists()) {
                filePath.createNewFile()
            }
            val fileOutputStream = FileOutputStream(filePath)
            hssfWorkbook.write(fileOutputStream)
            if (fileOutputStream != null) {
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}