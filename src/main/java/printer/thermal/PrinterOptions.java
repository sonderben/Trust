package printer.thermal;

import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

//https://stackoverflow.com/questions/17505070/printing-reciepts-with-thermal-printer-in-java
public class PrinterOptions {
    String commandSet = "";

    public String initialize() {
        final byte[] Init = {27, 64};
        commandSet += new String(Init);
        return new String(Init);
    }

    public String chooseFont(int Options) {
        String s;
        final byte[] ChooseFontA = {27, 77, 0};
        final byte[] ChooseFontB = {27, 77, 1};
        final byte[] ChooseFontC = {27, 77, 48};
        final byte[] ChooseFontD = {27, 77, 49};

        s = switch (Options) {
            case 1 -> new String(ChooseFontA);
            case 3 -> new String(ChooseFontC);
            case 4 -> new String(ChooseFontD);
            default -> new String(ChooseFontB);
        };
        commandSet += s;
        return s;
    }

    public String feedBack(byte lines) {
        final byte[] Feed = {27,101,lines};
        String s = new String(Feed);
        commandSet += s;
        return s;
    }

    public String feed(byte lines) {
        final byte[] Feed = {27,100,lines};
        String s = new String(Feed);
        commandSet += s;
        return s;
    }

    public String alignLeft() {
        final byte[] AlignLeft = {27, 97,48};
        String s = new String(AlignLeft);
        commandSet += s;
        return s;
    }

    public String alignCenter() {
        final byte[] AlignCenter = {27, 97,49};
        String s = new String(AlignCenter);
        commandSet += s;
        return s;
    }

    public String alignRight() {
        final byte[] AlignRight = {27, 97,50};
        String s = new String(AlignRight);
        commandSet += s;
        return s;
    }

    public String newLine() {
        final  byte[] LF = {10};
        String s = new String(LF);
        commandSet += s;
        return s;
    }

    public String reverseColorMode(boolean enabled) {
        final byte[] ReverseModeColorOn = {29, 66, 1};
        final byte[] ReverseModeColorOff = {29, 66, 0};

        String s = "";
        if(enabled)
            s = new String(ReverseModeColorOn);
        else
            s = new String(ReverseModeColorOff);

        commandSet += s;
        return s;
    }

    public String doubleStrik(boolean enabled) {
        final byte[] DoubleStrikeModeOn = {27, 71, 1};
        final byte[] DoubleStrikeModeOff = {27, 71, 0};

        String s="";
        if(enabled)
            s = new String(DoubleStrikeModeOn);
        else
            s = new String(DoubleStrikeModeOff);

        commandSet += s;
        return s;
    }

    public String doubleHeight(boolean enabled) {
        final byte[] DoubleHeight = {27, 33, 17};
        final byte[] UnDoubleHeight={27, 33, 0};

        String s = "";
        if(enabled)
            s = new String(DoubleHeight);
        else
            s = new String(UnDoubleHeight);

        commandSet += s;
        return s;
    }

    public String emphasized(boolean enabled) {
        final byte[] EmphasizedOff={27 ,0};
        final byte[] EmphasizedOn={27 ,1};

        String s="";
        if(enabled)
            s = new String(EmphasizedOn);
        else
            s = new String(EmphasizedOff);

        commandSet += s;
        return s;
    }

    public String underLine(int Options) {
        final byte[] UnderLine2Dot = {27, 45, 50};
        final byte[] UnderLine1Dot = {27, 45, 49};
        final byte[] NoUnderLine = {27, 45, 48};

        String s = switch (Options) {
            case 0 -> new String(NoUnderLine);
            case 1 -> new String(UnderLine1Dot);
            default -> new String(UnderLine2Dot);
        };
        commandSet += s;
        return s;
    }

    public String color(int Options) {
        final byte[] ColorRed = {27, 114, 49};
        final byte[] ColorBlack = {27, 114, 48};

        String s;
        if (Options==1)
            s= new String(ColorRed);
        else
            s= new String(ColorBlack);
        commandSet += s;
        return s;
    }

    public String finit() {
        final byte[] FeedAndCut = {29, 'V', 66, 0};

        String s = new String(FeedAndCut);

        final byte[] DrawerKick={27,70,0,60,120};
        s += new String(DrawerKick);

        commandSet+=s;
        return s;
    }

    public String addLineSeparator() {
        String lineSpace = "----------------------------------------";
        commandSet += lineSpace;
        return lineSpace;
    }

    public void resetAll() {
        commandSet = "";
    }

    public void setText(String s) {
        commandSet+=s;
    }

    public String finalCommandSet() {
        return commandSet;
    }

      public boolean feedPrinter(byte[] b) {
        try {
            AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName("EPSON TM-U220 ReceiptE4", null)); //EPSON TM-U220 ReceiptE4

            DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();
            //PrintServiceLookup.lookupDefaultPrintService().createPrintJob();

            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);
            PrintJobWatcher pjDone = new PrintJobWatcher(job);

            job.print(doc, null);
            pjDone.waitForDone();
            System.out.println("Done !");
        } catch (javax.print.PrintException pex) {
            System.out.println("Printer Error " + pex.getMessage());
            return false;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void print(){

        PrinterOptions p = new PrinterOptions();

        p.resetAll();
        p.initialize();
        p.feedBack((byte) 2);
        p.color(0);
        p.alignCenter();
        p.setText("The Dum Dum Name");
        p.newLine();
        p.setText("Restaurant Dining");
        p.newLine();
        p.addLineSeparator();
        p.setText("Bling Bling");
        p.newLine();
        p.addLineSeparator();
        p.newLine();

        p.alignLeft();
        p.setText("POD No \t: 2001 \tTable \t: E511");
        p.newLine();

        p.setText("Res Date \t: " + "01/01/1801 22:59");

        p.newLine();
        p.setText("Session \t: Evening Session");
        p.newLine();
        p.setText("Staff \t: Bum Dale");
        p.newLine();
        p.addLineSeparator();
        p.newLine();
        p.alignCenter();
        p.setText(" - Some Items - ");
        p.newLine();
        p.alignLeft();
        p.addLineSeparator();

        p.newLine();

        p.setText("No \tItem\t\tUnit\tQty");
        p.newLine();
        p.addLineSeparator();
        p.setText("1" + "\t" + "Aliens Everywhere" + "\t" + "Rats" + "\t" + "500");
        p.setText("1" + "\t" + "Aliens Everywhere" + "\t" + "Rats" + "\t" + "500");
        p.setText("1" + "\t" + "Aliens Everywhere" + "\t" + "Rats" + "\t" + "500");
        p.setText("1" + "\t" + "Aliens Everywhere" + "\t" + "Rats" + "\t" + "500");
        p.setText("1" + "\t" + "Aliens Everywhere" + "\t" + "Rats" + "\t" + "500");
        p.setText("1" + "\t" + "Aliens Everywhere" + "\t" + "Rats" + "\t" + "500");

        p.addLineSeparator();
        p.feed((byte) 3);
        p.finit();
        System.out.println("men commandSet: "+p.finalCommandSet());
        //p.feedPrinter(p.finalCommandSet().toByteArray())

    }

}
