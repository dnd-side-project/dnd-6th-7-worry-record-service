package dnd.project.dnd6th7worryrecordservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UserRequestDto {
    private String username;
    private String email;
    private String kakaoId;
    private MultipartFile img;
}
