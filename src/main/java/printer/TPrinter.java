package printer;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

public class TPrinter {

//https://stackoverflow.com/questions/64663436/not-able-to-print-the-entire-content-of-text-on-thermal-printer-in-javafx
    public void printNode(Node node) {

        Printer printer = Printer.getDefaultPrinter();

        PrinterJob printerJob = PrinterJob.createPrinterJob(printer);

        Paper paper = PrintHelper.createPaper("Roll80", 80, 590, Units.MM);

        PageLayout pageLayout = printerJob.getPrinter().createPageLayout(paper, PageOrientation.PORTRAIT, 0, 0, 0, 0);

        double height = node.getLayoutBounds().getHeight();

        System.out.println("Height: " + height);

        double scale = 0.791;

        node.getTransforms().add(new Scale(scale, scale));

        if (printerJob != null) {
            boolean success = printerJob.printPage(pageLayout, node);
            if (success) {
                printerJob.endJob();
               // System.exit(0);
            }
        }

    }


}
