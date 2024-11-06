package com.happiest.DoctorService.service;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.exception.FileStorageException;
import com.happiest.DoctorService.exception.MyFileNotFoundException;
import com.happiest.DoctorService.response.UploadFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService() throws FileStorageException {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException(Constants.FILE_STORAGE_EXCEPTION);
        }
    }

    public UploadFileResponse storeFile(MultipartFile file) throws FileStorageException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the filename contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException(Constants.INVALID_PATH_SEQUENCE + fileName);
            }

            // Store the file in the target location
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Build the full file URL
            String fileDownloadUri = "http://localhost:8098/uploads/" + fileName;

            // Return the response with the full file URL
            return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        } catch (IOException ex) {
            throw new FileStorageException(String.format(Constants.FILE_STORAGE_ERROR, fileName));
        }
    }

    public Resource loadFileAsResource(String fileName) throws MyFileNotFoundException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            UrlResource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException(Constants.FILE_NOT_FOUND + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException(Constants.FILE_NOT_FOUND + fileName);
        }
    }
}