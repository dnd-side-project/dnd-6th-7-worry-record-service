package dnd.project.dnd6th7worryrecordservice.dto.user;

import dnd.project.dnd6th7worryrecordservice.domain.user.Role;
import dnd.project.dnd6th7worryrecordservice.domain.user.SocialType;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class UserInfoDto {
    private String username;
    private String email;
    private String socialId;
    private String imgURL;
    private String refreshToken;
    private String deviceToken;
    private SocialType socialType;

    public UserInfoDto(String username, String email, String socialId, String refreshToken) {
        this.username = username;
        this.email = email;
        this.socialId = socialId;
        this.imgURL = imgURL;
        this.refreshToken = refreshToken;
    }


    public UserInfoDto(String username, String email, String socialId, SocialType socialType, String imgURL) {
        this.username = username;
        this.email = email;
        this.socialId = socialId;
        this.socialType = socialType;
        this.imgURL = imgURL;
    }
    //테스트용
    public UserInfoDto(String username, String email, String socialId, SocialType socialType, String imgURL,String refreshToken, String diviceToken) {
        this.username = username;
        this.email = email;
        this.socialId = socialId;
        this.socialType = socialType;
        this.imgURL = imgURL;
        this.refreshToken = refreshToken;
        this.deviceToken = diviceToken;
    }

    //UserRequestDto를 User Entity로 변환하여 return
    public User toEntity(){
        User user = new User(this.username, this.email, this.socialId, Role.USER, this.imgURL, this.refreshToken, this.deviceToken, this.socialType);
        return user;
    }

}
