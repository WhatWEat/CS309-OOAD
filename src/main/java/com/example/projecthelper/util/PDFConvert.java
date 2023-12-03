package com.example.projecthelper.util;

import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.example.projecthelper.Exceptions.InvalidFormException;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.mail.search.SearchTerm;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class PDFConvert {
    private static com.aspose.words.License wordLicense = new com.aspose.words.License();
    private static com.aspose.slides.License pptLicense = new com.aspose.slides.License();
    private static final Set<String> WORDS_EXTENSION = new HashSet<>(
        Arrays.asList(
            ".docx", ".doc", ".odt", ".rtf", ".txt", ".html", ".htm", ".mhtml", ".mht", ".xml"
        )
    );

    private static final Set<String> SLIDES_EXTENSION = new HashSet<>(
        Arrays.asList(
            ".pptx", ".ppt", ".ppsx", ".pps", ".potx", ".pot", ".odp"
        )
    );

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
            throw new InvalidFormException("文件不支持转为pdf");
        }
    }
    public static void wordConvert(String inputPath, String outputPath){
        try {
            Document doc = new Document(inputPath);
            doc.save(outputPath, com.aspose.words.SaveFormat.PDF);
        } catch (Exception e) {
            System.out.printf("Problem about convert word %s to pdf %s \n", inputPath, outputPath);
            throw new InvalidFormException("文件不支持转为pdf");
        }
    }

    public static void convertToPdf(String inputPath, String outputPath){
        try{
            String extension = inputPath.substring(inputPath.lastIndexOf("."));
            if(WORDS_EXTENSION.contains(extension)){
                wordConvert(inputPath, outputPath);
            }
            else if(SLIDES_EXTENSION.contains(extension)){
                pptConvert(inputPath, outputPath);
            }
            else
                throw new InvalidFormException("文件不支持转为pdf");
        }catch (IndexOutOfBoundsException e){
            throw new InvalidFormException("文件不支持转为pdf");
        }
    }

    public static void markdownConvert(String markdownFilePath, String pdfFilePath) throws
        IOException {
        // Read Markdown file
        String markdown = new String(Files.readAllBytes(Paths.get(markdownFilePath)));

        // Convert Markdown to HTML
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);

        // Convert HTML to PDF
        try (OutputStream os = new FileOutputStream(pdfFilePath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        }
    }

    public static void htmlConvert(String inputPath, String outputPath) throws IOException {
        // 读取HTML文件
        String html = Files.readString(Paths.get(inputPath));

        // 将HTML转换为PDF
        try (OutputStream os = new FileOutputStream(outputPath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        }
    }
}
