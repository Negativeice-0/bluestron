package co.ke.bluestron.bsapi.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class MediaService {
    private final Path rootLocation = Paths.get("uploads/videos");
    
    public Resource streamVideo(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;  // Serve the video file like a streaming fountain
            }
            throw new RuntimeException("Video not found");
        } catch (Exception e) {
            throw new RuntimeException("Error streaming video");
        }
    }
}
