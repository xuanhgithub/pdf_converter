package com.lorenz_orga.fileToPdfConverter.controller;

import com.lorenz_orga.fileToPdfConverter.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestBody MultipartFile file) {
        try {
            String filename = fileService.convertDocxToPdfWithApachePOI(file); // use convertDocxToPDFWithApachePDFBox for first approach
            return ResponseEntity.ok("File uploaded and converted to PDF: " + filename);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error uploading file: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/pdf/{filename:.+}")
    public ResponseEntity<byte[]> getPDF(@PathVariable String filename) {
        try {
            byte[] pdfBytes = fileService.getPDF(filename);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "inline; filename=\"" + filename + "\"")
                    .body(pdfBytes);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}