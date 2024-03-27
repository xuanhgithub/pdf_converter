package com.lorenz_orga.fileToPdfConverter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;


@Service
public interface FileService {
    // First approach
    String convertDocxToPDFWithApachePDFBox(MultipartFile file) throws IOException;

    // Second approach
    String convertDocxToPdfWithApachePOI(MultipartFile file) throws IOException;

    byte[] getPDF(String filename) throws IOException;

}