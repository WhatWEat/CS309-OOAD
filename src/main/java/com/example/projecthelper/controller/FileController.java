package com.example.projecthelper.controller;

import com.example.projecthelper.Exceptions.FileProcessingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("filePath")String filePath) {
        if (file.isEmpty()) {
            return " no file!";
        }
        try {
            byte[] bytes = file.getBytes();
            filePath += file.getOriginalFilename();
            Files.write(Paths.get(filePath), bytes);
            return "upload successfully!";
        } catch (IOException e) {
            throw new FileProcessingException("文件写入失败");
        }
    }

    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam("filePath") String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
            return "File deleted successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    

}
