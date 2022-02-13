package org.seilea.cherubim;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws IOException {
        String filePath = "/Users/panhong/预算数据——预生产测试数据.xlsx";

        Workbook workbook = WorkbookFactory.create(new File(filePath));
        Sheet sheet = workbook.getSheet("Sheet1");

        int lastRowIndex = sheet.getLastRowNum();
        String sql = "insert into public.hall_estimate (id, hall_id, hall_code, year_month, travel, card, svc, total, creater_id, create_time, update_time, other, operator_id)"
                + " values ";
        for (int i = 2; i <= lastRowIndex; i++) {
            Row row = sheet.getRow(i);
            if (row == null) { break; }
            String hallCode = row.getCell(4).toString();
            LocalDate startDate = LocalDate.parse("2022-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            var start = 9;
            for (var x = 0; x < 12; x++) {
                String card = row.getCell(start + x * 4).toString();
                String travel = row.getCell(start + x * 4 + 1).toString();
                String svc = row.getCell(start + x * 4 + 2).toString();
                String other = row.getCell(start + x * 4 + 3).toString();
                BigDecimal c = isNull(card) ? BigDecimal.ZERO : new BigDecimal(card).setScale(2, RoundingMode.DOWN);
                BigDecimal t = isNull(travel) ? BigDecimal.ZERO : new BigDecimal(travel).setScale(2, RoundingMode.DOWN);
                BigDecimal s = isNull(svc) ? BigDecimal.ZERO : new BigDecimal(svc).setScale(2, RoundingMode.DOWN);
                BigDecimal o = isNull(other) ? BigDecimal.ZERO : new BigDecimal(other).setScale(2, RoundingMode.DOWN);
                BigDecimal total = c.add(t).add(s).add(o);
                sql += "(nextval('yue_seq_a'), (select id from hall where code = '" + hallCode + "'), '" + hallCode + "', '"
                        + startDate.plusMonths(x).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "',"
                        + " " + t + ", " + c + ", " + s + ", " + total + ", null, now(), null, " + o + ", null),\n";
            }
        }
        System.out.println(sql);
        workbook.close();

    }

    private static boolean isNull(String s) {
        return Objects.isNull(s) || "".equals(s);
    }


}

