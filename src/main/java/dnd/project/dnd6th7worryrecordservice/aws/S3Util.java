package dnd.project.dnd6th7worryrecordservice.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.apache.commons.io.FilenameUtils;


import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String downloadFile(String fileName){
        String fileUrl = bucket + "/homeGIF/" + fileName + ".gif";
        return amazonS3Client.getUrl("",fileUrl).toString();
    }

    public String uploadFile(MultipartFile multipartFile, String dirName) {

        String fileUrl = null;
        String fileName = createFileName(dirName);

        boolean ext = checkExt(multipartFile);
        if (ext == false) {
            return null;
        } else {
            //MultipartFile 로 넘어온 파일을 S3에 업로드하고 fileUrl에 추가하여 리턴
            //MultipartFile 을 따로 File로 만드는 것이 아닌 InputStream 을 받는 방식
            //Stream 을 받기 때문에 데이터가 Byte형태로 받아와지기 때문에 objectMetadata 를 추가해 데이터에 대한 추가정보를 제공해야함
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {
                amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));    //외부에 공개할 이미지이므로, 해당 파일에 public read 권한을 추가합니다.
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
            }
            fileUrl = amazonS3Client.getUrl(bucket, fileName).toString();

            return fileUrl;
        }
    }


    //MultipartFile 확장자 확인
    private boolean checkExt(MultipartFile multipartFile) {
        String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        System.out.println("ext = " + ext);
        if (ext.contains("png") || ext.contains("jpg") || ext.contains("jpeg"))
            return true;
        else
            return false;
    }


    public void deleteFile(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createFileName(String dirName) {
        String fileName = dirName + "/" + System.currentTimeMillis() + ".jpg";
        return fileName;
    }
}