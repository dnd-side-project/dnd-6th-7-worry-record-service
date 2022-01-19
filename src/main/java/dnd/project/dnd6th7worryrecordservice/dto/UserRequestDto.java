package dnd.project.dnd6th7worryrecordservice.dto;

import dnd.project.dnd6th7worryrecordservice.domain.user.Role;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UserRequestDto {
    private String username;
    private String email;
    private String kakaoId;
    private String imgURL;

    public UserRequestDto(String username, String email, String kakaoId, String imgURL) {
        this.username = username;
        this.email = email;
        this.kakaoId = kakaoId;
        this.imgURL = imgURL;
    }

    public User toEntity(){
        User user = new User(this.username, this.email, this.kakaoId, Role.USER, this.imgURL);
        return user;
    }

}
