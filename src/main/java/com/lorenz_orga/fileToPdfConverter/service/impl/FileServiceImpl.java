package com.lorenz_orga.fileToPdfConverter.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.lorenz_orga.fileToPdfConverter.service.FileService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {

    // First approach
    @Override
    public String convertDocxToPDFWithApachePDFBox(MultipartFile file) throws IOException {
        XWPFDocument docx = new XWPFDocument(file.getInputStream());
        String pdfFilename = Objects.requireNonNull(file.getOriginalFilename()).replace(".docx", ".pdf");

        try (PDDocument pdfDocument = new PDDocument()) {
            PDPage page = new PDPage();
            pdfDocument.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                float yPosition = page.getMediaBox().getHeight() - 50; // Start position
                for (XWPFParagraph paragraph : docx.getParagraphs()) {
                    List<String> lines = paragraph.getText().lines().toList();
                    for (String line : lines) {
                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yPosition);
                        contentStream.showText(line);
                        contentStream.endText();
                        yPosition -= 15;
                        if (yPosition <= 50) {
                            contentStream.close();
                            page = new PDPage();
                            pdfDocument.addPage(page);
                            contentStream.moveTo(50, page.getMediaBox().getHeight() - 50);
                            yPosition = page.getMediaBox().getHeight() - 50;
                        }
                    }
                }
            }
            pdfDocument.save(pdfFilename);
        }

        return pdfFilename;
    }

    // Second approach
    @Override
    public String convertDocxToPdfWithApachePOI(MultipartFile file) throws IOException {

        InputStream docxInputStream = file.getInputStream();
        String pdfFilename = Objects.requireNonNull(file.getOriginalFilename()).replace(".docx", ".pdf");

        try (XWPFDocument document = new XWPFDocument(docxInputStream);
             OutputStream pdfOutputStream = new FileOutputStream(pdfFilename)) {
            Document pdfDocument = new Document();
            PdfWriter.getInstance(pdfDocument, pdfOutputStream);
            pdfDocument.open();

            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                pdfDocument.add(new Paragraph(paragraph.getText()));

                // for handling images
                List<XWPFPicture> pictures = paragraph.getRuns().stream()
                        .flatMap(run -> run.getEmbeddedPictures().stream())
                        .toList();
                for (XWPFPicture picture : pictures) {
                    XWPFPictureData pictureData = picture.getPictureData();
                    byte[] pictureBytes = pictureData.getData();
                    Image img = Image.getInstance(pictureBytes);
                    img.scaleToFit((float) picture.getWidth(), (float) picture.getWidth());

                    pdfDocument.add(img);
                }
            }

            pdfDocument.close();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        return pdfFilename;
    }

    @Override
    public byte[] getPDF(String filename) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PDDocument document = PDDocument.load(new File(filename))) {
            document.save(baos);
        }
        return baos.toByteArray();
    }

}
