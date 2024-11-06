package com.happiest.DoctorService.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.happiest.DoctorService.constants.Constants;
import com.happiest.DoctorService.exception.FileStorageException;
import com.happiest.DoctorService.exception.MyFileNotFoundException;
import com.happiest.DoctorService.response.UploadFileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class FileStorageServiceTest {

    @InjectMocks
    private FileStorageService fileStorageService;

    private Path fileStorageLocation;

    @BeforeEach
    void setUp() throws Exception {
        fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        Files.createDirectories(fileStorageLocation);
    }

    @Test
    void testStoreFileSuccess() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.copy(any(java.io.InputStream.class), any(Path.class), any(StandardCopyOption.class))).thenAnswer(invocation -> {
                Path target = invocation.getArgument(1);
                Files.createFile(target);
                return 1L;
            });

            UploadFileResponse response = fileStorageService.storeFile(file);

            assertNotNull(response);
            assertEquals("test.txt", response.getFileName());
            assertEquals("http://localhost:8098/uploads/test.txt", response.getFileDownloadUri());
            assertEquals("text/plain", response.getFileType());
            assertEquals(file.getSize(), response.getSize());
        }
    }

    @Test
    void testStoreFileInvalidPathSequence() {
        MultipartFile file = new MockMultipartFile("file", "../test.txt", "text/plain", "Test content".getBytes());

        Exception exception = assertThrows(FileStorageException.class, () -> {
            fileStorageService.storeFile(file);
        });

        assertEquals(Constants.INVALID_PATH_SEQUENCE + "../test.txt", exception.getMessage());
    }

    @Test
    void testStoreFileIOException() throws Exception {
        MultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Test content".getBytes());

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.copy(any(java.io.InputStream.class), any(Path.class), any(StandardCopyOption.class))).thenThrow(IOException.class);

            Exception exception = assertThrows(FileStorageException.class, () -> {
                fileStorageService.storeFile(file);
            });

            assertEquals(String.format(Constants.FILE_STORAGE_ERROR, "test.txt"), exception.getMessage());
        }
    }

    @Test
    void testLoadFileAsResourceSuccess() throws Exception {
        Path filePath = fileStorageLocation.resolve("test.txt").normalize();
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }

        Resource resource = fileStorageService.loadFileAsResource("test.txt");

        assertNotNull(resource);
        assertTrue(resource.exists());
        assertEquals(filePath.toUri(), resource.getURI());
    }

    @Test
    void testLoadFileAsResourceFileNotFound() {
        Exception exception = assertThrows(MyFileNotFoundException.class, () -> {
            fileStorageService.loadFileAsResource("nonexistent.txt");
        });

        assertEquals(Constants.FILE_NOT_FOUND + "nonexistent.txt", exception.getMessage());
    }

}
