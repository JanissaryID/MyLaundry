package com.example.mylaundry.excel

import android.content.Context
import android.widget.Toast
import android.os.Environment
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import com.example.mylaundry.room.transactions.Transactions
import com.example.mylaundry.room.transactions.TransactionsDatabaseGet

import org.apache.poi.ss.usermodel.Cell

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet

import org.apache.poi.ss.usermodel.Workbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception





class ExportToExcel {

    lateinit var db: TransactionsDatabaseGet

    fun createXlFile() {

        val data: List<Transactions> = db.dataDao().getAllData()

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
        for (value in data){
            val row1: Row = sheet.createRow(a + 1)
            cell = row1.createCell(0)
            cell.setCellValue(value.typeMachine)
            cell = row1.createCell(1)
            cell.setCellValue(value.noMachine.toString())
            cell = row1.createCell(2)
            cell.setCellValue(value.date.toString())
            cell = row1.createCell(3)
            cell.setCellValue(value.timeMachine.toString())
            cell = row1.createCell(4)
            cell.setCellValue(value.priceMachine.toString())

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
}