package com.example.projecthelper.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FileUtil {

    /*此方法还很简陋：
    1.从指定位置获取一个xls文件，并且读取第一张工作表,xlsx可能有问题，还在调试
    2.暂时要求第一行表头必须有id和grade
     */
    public static void manageTableFile(String filePath, List<Long> submitterList, List<Float>gradeList) {
        try {
            FileInputStream file = new FileInputStream(filePath);
//                FileInputStream file = new FileInputStream(new File(""));
            Workbook workbook;
            if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(file);
            } else if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(file);
            }else {
                System.out.println("Invalid file format. Only .xls and .xlsx files are supported.");
                return;
            }
            Sheet sheet = workbook.getSheetAt(0);
            int idColumn = -1;
            int gradeColumn = -1;

            // 遍历第一行，查找“id”和“grade”列头所在的位置
            Row headerRow = sheet.getRow(0);
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                String cellValue = cell.getStringCellValue().trim();
                if (cellValue.equalsIgnoreCase("id")) {
                    idColumn = i;
                } else if (cellValue.equalsIgnoreCase("grade")) {
                    gradeColumn = i;
                }
            }

            if (idColumn == -1 || gradeColumn == -1) {
                System.out.println("Name or grade column not found.");
                return;
            }
            // 遍历所有行（除第一行外），将每一行的name和grade值存入相应的数组

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                long id = (long) dataRow.getCell(idColumn).getNumericCellValue();
                float grade =(float) dataRow.getCell(gradeColumn).getNumericCellValue();
                submitterList.add(id);
                gradeList.add( grade);
            }
//            System.out.println(Arrays.toString(names));
//            System.out.println(Arrays.toString(grades));

            file.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //这个方法用于检测文件的拓展名是否为指定的拓展名
    public static boolean hasExtension(File file, String extension) {
        String fileName = file.getName();
        return fileName.endsWith("." + extension);
    }

    //这个方法用于生成文件路径
    public String getFilePath(String permission, int id, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(permission).append("/")
                .append(id).append("/")
                .append(fileName);
        return sb.toString();
    }

}
