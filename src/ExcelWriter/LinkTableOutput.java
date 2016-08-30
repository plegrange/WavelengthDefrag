package ExcelWriter;

/**
 * Created by FuBaR on 5/26/2016.
 */

import Defragmentation.LinkTable;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class LinkTableOutput {

    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    private String outputFile;

    public LinkTableOutput(String outputFile) {
        this.outputFile = outputFile;
    }

    public void write(LinkTable linkTable) throws IOException, WriteException {
        File file = new File(outputFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        createLabel(excelSheet);
        createContent(excelSheet, linkTable);

        workbook.write();
        workbook.close();
        System.out.println("Table written to " + outputFile);
    }


    private void createLabel(WritableSheet sheet)
            throws WriteException {
        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
        // Define the cell format
        times = new WritableCellFormat(times10pt);
        // Lets automatically wrap the cells
        times.setWrap(true);

        // create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);
        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);

        // Write a few headers
    }

    private void createContent(WritableSheet sheet, LinkTable linkTable) throws WriteException,
            RowsExceededException {
        List<Double> wavelengths = linkTable.wavelengths;
        List<String> linkIDs = linkTable.linkIDs;
        String[][] table = linkTable.table;
        for (int i = 1; i <= linkIDs.size(); i++) {
            addString(sheet, i, 0, linkIDs.get(i - 1));
        }
        for (int i = 1; i <= wavelengths.size(); i++) {
            addNumber(sheet, 0, i, wavelengths.get(i - 1));
        }
        for (int i = 1; i <= linkIDs.size(); i++) {
            for (int j = 1; j <= wavelengths.size(); j++) {
                addString(sheet, i, j, table[i - 1][j - 1]);
            }
        }

    }


    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    private void addString(WritableSheet sheet, int column, int row,
                           String s) throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    private void addNumber(WritableSheet sheet, int column, int row,
                           double d) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, d, times);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }
}

