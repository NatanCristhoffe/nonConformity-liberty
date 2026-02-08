package blessed.infra.repository;

import blessed.infra.enums.FileType;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String folder, FileType fileType);
    void deleteFile(String fileUrl);

}
