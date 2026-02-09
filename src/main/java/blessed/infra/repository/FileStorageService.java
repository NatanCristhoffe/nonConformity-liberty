package blessed.infra.repository;

import blessed.infra.enums.FileType;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String folder, FileType fileType);
    void deleteFile(String fileUrl);
    String generatePresignedUrl(String fileUrl);
}
