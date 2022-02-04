package com.example.mylaundry.excel

import android.os.Environment
import android.util.Log
import com.example.mylaundry.fragment.TransactionFragment
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.format.Alignment
import jxl.format.VerticalAlignment
import jxl.write.Label
import jxl.write.WritableCellFormat
import jxl.write.WritableFont
import jxl.write.WritableWorkbook
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CreateExcel {

    var workbook: WritableWorkbook? = null

    fun createExcelSheet(date: String) {
        var dateTitle = date
        var csvFile = ""
        var a = 0

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter)

        if(dateTitle == ""){
            csvFile = "Report ${formatted}.xls"
        }
        else{
            csvFile = "Report ${date}.xls"
        }

        val futureStudioIconFile = File(
            Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/" + csvFile
        )
        val wbSettings = WorkbookSettings()
        wbSettings.locale = Locale("en", "EN")
        try {
            workbook = Workbook.createWorkbook(futureStudioIconFile, wbSettings)
            createFirstSheet()
            workbook?.write()
            workbook?.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun createFirstSheet() {
        val font1 = WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD)
        val format1 = WritableCellFormat(font1)
        format1.alignment = Alignment.CENTRE
        format1.verticalAlignment = VerticalAlignment.CENTRE

        val font2 = WritableFont(WritableFont.ARIAL, 11)
        val format2 = WritableCellFormat(font2)
        format2.alignment = Alignment.CENTRE

        try {
            val listdata: MutableList<TransactionMachine> = ArrayList()
            listdata.add(TransactionMachine(1, "Dryer", 1, "2-2-2022", "12.34","30.000"))
            listdata.add(TransactionMachine(2, "Washer", 3, "2-2-2022","12.04","25.000"))
            listdata.add(TransactionMachine(3, "Washer", 6, "2-2-2022","10.23","25.000"))
            //Excel sheet name. 0 (number)represents first sheet
            val sheet = workbook!!.createSheet("sheet1", 0)

            sheet.setColumnView(0,4)
            sheet.setColumnView(1,15)
            sheet.setColumnView(2,15)
            sheet.setColumnView(3,15)
            sheet.setColumnView(4,15)
            sheet.setColumnView(5,15)

            sheet.mergeCells(0,0,0,1)
            sheet.mergeCells(1,0,2,0)
            sheet.mergeCells(3,0,3,1)
            sheet.mergeCells(4,0,4,1)
            sheet.mergeCells(5,0,5,1)
            sheet.mergeCells(6,0,6,1)

            // column and row title
            sheet.addCell(Label(0, 0, "No.", format1))
            sheet.addCell(Label(1, 0, "Machine", format1))
            sheet.addCell(Label(1, 1, "Type",format1))
            sheet.addCell(Label(2, 1, "No",format1))
            sheet.addCell(Label(3, 0, "Date",format1))
            sheet.addCell(Label(4, 0, "Time",format1))
            sheet.addCell(Label(5, 0, "Price",format1))
            for (i in TransactionFragment.listTrans.indices) {
                sheet.addCell(Label(0, i + 2, (i+1).toString(), format2))
                sheet.addCell(Label(1, i + 2, TransactionFragment.listTrans[i].type))
                sheet.addCell(Label(2, i + 2, TransactionFragment.listTrans[i].number.toString(),format2))
                sheet.addCell(Label(3, i + 2, TransactionFragment.listTrans[i].date, format2))
                sheet.addCell(Label(4, i + 2, TransactionFragment.listTrans[i].time,format2))
                sheet.addCell(Label(5, i + 2, TransactionFragment.listTrans[i].price,format2))
            }
            sheet.mergeCells(0,(TransactionFragment.listTrans.size) + 2,(TransactionFragment.listTrans.size),(TransactionFragment.listTrans.size))
            sheet.addCell(Label(4, (TransactionFragment.listTrans.size) + 2, "Total",format2))
//            Log.d("excel", TransactionFragment.listTrans.size.toString())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}