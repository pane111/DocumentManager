package com.fhtw.ocrservice;

import lombok.extern.java.Log;
import net.sourceforge.tess4j.Tesseract;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Log
class OcrServiceApplicationTests {

    @Test
    void tesseractTest() throws Exception {
        File testImg = new File("src/test/resources/testimage.png");
        if (!testImg.exists()) {
            throw new Exception("testimage not found at " + testImg.getAbsolutePath());
        }
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage("eng");

        String result = tesseract.doOCR(testImg);
        System.out.println("OCR Result: " + result);
        assertTrue(result.toLowerCase().contains("test file"));
    }
}
