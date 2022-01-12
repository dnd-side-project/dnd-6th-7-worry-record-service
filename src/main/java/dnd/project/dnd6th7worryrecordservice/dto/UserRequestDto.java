package dnd.project.dnd6th7worryrecordservice.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

@Data
public class UserRequestDto {
    private String username;
    private String email;
    private String kakaoId;
    private MultipartFile img;
}
