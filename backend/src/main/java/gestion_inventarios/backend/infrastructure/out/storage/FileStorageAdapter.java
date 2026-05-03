package gestion_inventarios.backend.infrastructure.out.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageAdapter {

    private final String STORAGE_DIRECTORY = "uploads/images";

    public FileStorageAdapter() {
        try {
            Files.createDirectories(Paths.get(STORAGE_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de subida de archivos", e);
        }
    }

    public String saveFile(MultipartFile file, String subdirectory) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("No se puede guardar un archivo vacío");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            Path targetLocation = Paths.get(STORAGE_DIRECTORY).resolve(subdirectory);
            Files.createDirectories(targetLocation);
            targetLocation = targetLocation.resolve(uniqueFilename);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFilename;

        } catch (IOException ex) {
            throw new RuntimeException("No se pudo guardar el archivo", ex);
        }
    }

    public void deleteFile(String filename) {
        try {
            Files.deleteIfExists(Paths.get(STORAGE_DIRECTORY).resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo eliminar el archivo", e);
        }
    }
}