package com.example.projecthelper.util;

import com.example.projecthelper.Exceptions.FileProcessingException;
import com.example.projecthelper.Exceptions.InvalidFormException;
import com.example.projecthelper.entity.Assignment;
import com.example.projecthelper.entity.SubmittedAssignment;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
    public static final String PATH_PREFIX = "Files/";
    public static final String TEA_PREFIX = "tea";
    public static final String STU_PREFIX = "stu";
    public static int TEA_ID_OF_ASS_OR_SUBMITTED_ASS_PATH = 1;
    public static int ASS_ID_OF_ASS_OR_SUBMITTED_ASS_PATH = 2;
    public static int SUBMITTER_ID_OF_SUBMITTED_ASS_PATH = 3;


    public static String generateDir(String ... segments){
        StringBuilder dir = new StringBuilder(PATH_PREFIX);
        for(int i = 0; i < segments.length; i++){
            dir.append(segments[i]);
            if(i != segments.length-1)
                dir.append("/");
        }
        return dir.toString();
    }

    public static String generateAssPath(Assignment assignment){
        return generateDir(FileUtil.TEA_PREFIX, String.valueOf(assignment.getCreatorId()), String.valueOf(assignment.getAssignmentId()));
    }

    public static String generateSubmittedAssPath(Assignment assignment, Long submitterId){
        return generateDir(FileUtil.TEA_PREFIX, String.valueOf(assignment.getCreatorId()), String.valueOf(assignment.getAssignmentId()), String.valueOf(submitterId));
    }

    public static String getIdFromPath(String path, int idIndex){
        try {
            return path.split("/")[idIndex];
        }catch (IndexOutOfBoundsException e){
            throw new InvalidFormException("非法索引或路径");
        }
    }

    /*此方法还很简陋：
    1.从指定位置获取一个xls文件，并且读取第一张工作表,xlsx可能有问题，还在调试
    2.暂时要求第一行表头必须有id和grade
     */
    public static List<SubmittedAssignment> manageTableFile(MultipartFile file) {
        try {
            Workbook workbook;
            if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if (file.getOriginalFilename().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }else {
                throw new InvalidFormException("非法文件格式；仅支持.xls和.xlsx");
            }
            Sheet sheet = workbook.getSheetAt(0);
            int submitterIdColumn = -1;
            int gradeColumn = -1;
            int commentColumn = -1;
            int reviewColumn = -1;

            // 遍历第一行，查找“id”和“grade”列头所在的位置
            Row headerRow = sheet.getRow(0);
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null)
                    continue;
                String cellValue = cell.getStringCellValue();
                if(cellValue != null)
                    cellValue = cellValue.trim();
                else
                    continue;
                if (cellValue.equalsIgnoreCase("submitterId")) {
                    submitterIdColumn = i;
                } else if (cellValue.equalsIgnoreCase("grade")) {
                    gradeColumn = i;
                } else if (cellValue.equalsIgnoreCase("comment")) {
                    commentColumn = i;
                } else if (cellValue.equalsIgnoreCase("review")) {
                    reviewColumn = i;
                }
            }

            if (submitterIdColumn == -1 || gradeColumn == -1) {
                throw new InvalidFormException("submitterId或成绩列缺失");
            }
            // 遍历所有行（除第一行外），将每一行的name和grade值存入相应的数组
            List<SubmittedAssignment> sas = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row dataRow = sheet.getRow(i);
                long submitterId = (long) dataRow.getCell(submitterIdColumn).getNumericCellValue();
                float grade =(float) dataRow.getCell(gradeColumn).getNumericCellValue();
                String comment = null;
                String review = null;
                if (commentColumn != -1)
                    comment = (String) dataRow.getCell(commentColumn).getStringCellValue();
                if (reviewColumn != -1)
                    review = (String) dataRow.getCell(commentColumn).getStringCellValue();
                sas.add(new SubmittedAssignment(submitterId, grade, comment, review));
            }
//            System.out.println(Arrays.toString(names));
//            System.out.println(Arrays.toString(grades));

            workbook.close();
            return sas;
        } catch (IOException e) {
            throw new FileProcessingException("文件处理异常");
        }
    }

    //这个方法用于检测文件的拓展名是否为指定的拓展名
    public static boolean hasExtension(File file, String extension) {
        String fileName = file.getName();
        return fileName.endsWith("." + extension);
    }

    public static boolean hasExtension(MultipartFile file, String extension){
        return Objects.requireNonNull(file.getOriginalFilename()).endsWith(extension);
    }

    //这个方法用于生成文件路径
    public String getFilePath(String permission, int id, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(permission).append("/")
                .append(id).append("/")
                .append(fileName);
        return sb.toString();
    }

    public static String saveFile(String uploadDir, String fileName, MultipartFile multipartFile){
        try{
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = fileName.substring(dotIndex);
                fileName = fileName.substring(0, dotIndex);
            }

            // Check if file with the same name exists, if so, append a number to the file name
            int count = 1;
            while (Files.exists(filePath)) {
                String newFileName = fileName + "(" + count++ + ")" + fileExtension;
                filePath = uploadPath.resolve(newFileName);
            }

            // Copy the file to the target location (Replacing existing file with the same name)
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.err.println(filePath.toString());
            return filePath.toString();
        }catch (IOException ioe){
            throw new FileProcessingException("文件存入失败");
        }
    }

    public static String getFilenameFromPath(String path){
        int dotIndex = path.lastIndexOf('\\');
        if (dotIndex > 0) {
            return path.substring(dotIndex+1);
        }
        return path;
    }

    public static String getMIMEType(String fileName){
        String type = "application/octet-stream";
        Optional<MediaType> mediaType = MediaTypeFactory.getMediaType(fileName);
        if(mediaType.isPresent()){
            type=mediaType.get().toString();
        }
        return type;
    }


}
