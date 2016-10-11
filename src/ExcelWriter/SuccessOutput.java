package ExcelWriter;

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

/**
 * Created by FuBaR on 10/10/2016.
 */
public class SuccessOutput {
    private WritableCellFormat timesBoldUnderline;
    private WritableCellFormat times;
    public String outputFile;

    public SuccessOutput(String outputFile) {
        this.outputFile = outputFile;
    }

    public void write(List<Double> successes) throws IOException, WriteException {

        File file = new File(this.outputFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Successes", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        createLabel(excelSheet);
        createContent(excelSheet, successes);

        workbook.write();
        workbook.close();
        System.out.println("Data written to " + outputFile);
    }

    public void createContent(WritableSheet sheet, List<Double> successes) throws WriteException {
        for (int i = 0; i < successes.size(); i++) {
            addNumber(sheet, 0, i, Double.valueOf(i));
            addNumber(sheet,1, i, successes.get(i));
        }
    }

    private void addNumber(WritableSheet sheet, int column, int row,
                           double d) throws WriteException, RowsExceededException {
        Number number;
        number = new Number(column, row, d, times);
        sheet.addCell(number);
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
    }
}