package blessed.infra.repository;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile file, String folder);
    void deleteFile(String fileUrl);

}
