package blessed.infra.storage;

import blessed.exception.BusinessException;
import blessed.infra.enums.FileType;
import blessed.infra.repository.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3FileStorageService implements FileStorageService {
    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.storage.max-file-size}")
    public DataSize maxFileSize;

    public S3FileStorageService(S3Client s3Client){
        this.s3Client = s3Client;
    }

    @Override
    public String uploadFile(MultipartFile file, String folderName, FileType fileType){

        if (file.getSize() > maxFileSize.toBytes()){
            throw new BusinessException("O arquivo excede o tamanho máximo permitido");
        }

        if (!fileType.isAllowed(file.getContentType())) {
            throw new BusinessException("Tipo de arquivo (" + file.getContentType() + ") não permitido. Aceitos: " + fileType.getMimeTypes());        }

        String fileName = folderName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        try{
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putOb, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build()).toExternalForm();

        } catch (IOException e){
            throw new RuntimeException("Erro ao fazer upload do arquivo para o S3");
        }
    }

    @Override
    public void deleteFile(String fileUrl){
        String key = extractKeyFromUrl(fileUrl);
        s3Client.deleteObject(builder -> builder.bucket(bucketName).key(key));
    }


    private String extractKeyFromUrl(String fileUrl) {
        return fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 5);
    }


    private boolean isAllowedType(String contentType){
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/webp") ||
                contentType.equals("application/pdf");
    }
}
