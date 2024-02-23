package com.sonderben.trust.printer

import com.sun.javafx.print.PrintHelper
import com.sun.javafx.print.Units
import javafx.print.PageOrientation
import javafx.print.Printer
import javafx.print.PrinterJob
import javafx.scene.Node
import javafx.scene.transform.Scale

object TPrinter {
    //https://stackoverflow.com/questions/64663436/not-able-to-print-the-entire-content-of-text-on-thermal-printer-in-javafx
    fun printNode(node: Node) {
        println("begin")
        val printer = Printer.getDefaultPrinter()

        val printerJob = PrinterJob.createPrinterJob(printer)

        val paper = PrintHelper.createPaper("Roll80", 80.0, 590.0, Units.MM)
        node.prefWidth( paper.width )
        node.maxWidth( paper.width )

        val pageLayout = printerJob!!.printer.createPageLayout(paper, PageOrientation.PORTRAIT, 0.0, 0.0, 0.0, 0.0)

        val height = node.layoutBounds.height

        println("Height: $height")

        val scale = 0.791

        //node.transforms.add(Scale(scale, scale))

        if (printerJob != null) {
            val success = printerJob.printPage(pageLayout, node)
            if (success) {
                printerJob.endJob()

                // System.exit(0);
            }
            println("end")
        }
    }
}
