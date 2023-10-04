package org.example;

import com.aspose.words.*;
import com.aspose.slides.*;
import java.io.FileInputStream;
import java.io.InputStream;

public class PDFConvert {
    private static com.aspose.words.License wordLicense = new com.aspose.words.License();
    private static com.aspose.slides.License pptLicense = new com.aspose.slides.License();
    static {
        try {
            InputStream is = new FileInputStream("license.xml");
            wordLicense.setLicense(is);
            is = new FileInputStream("license.xml");
            pptLicense.setLicense(is);
        } catch (Exception e) {
            System.out.println("Problem about load license.xml");
            throw new RuntimeException(e);
        }
    }
    public static void pptConvert(String inputPath, String outputPath) {
        try{
            Presentation doc = new Presentation(inputPath);
            doc.save(outputPath, com.aspose.slides.SaveFormat.Pdf);
        } catch (Exception e) {
            System.out.printf("Problem about convert ppt %s to pdf %s \n", inputPath, outputPath);
        }
    }
    public static void wordConvert(String inputPath, String outputPath){
        try {
            Document doc = new Document(inputPath);
            doc.save(outputPath, com.aspose.words.SaveFormat.PDF);
        } catch (Exception e) {
            System.out.printf("Problem about convert word %s to pdf %s \n", inputPath, outputPath);
        }
    }
}
