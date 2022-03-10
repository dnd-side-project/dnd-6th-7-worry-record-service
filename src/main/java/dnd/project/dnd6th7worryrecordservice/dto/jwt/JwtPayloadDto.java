package dnd.project.dnd6th7worryrecordservice.dto.jwt;

import dnd.project.dnd6th7worryrecordservice.domain.user.SocialType;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtPayloadDto {
    private String username;
    private String email;
    private String socialId;
    private SocialType socialType;
    private String imgURL;

    public UserRequestDto toUserRequestDto(){
        UserRequestDto userRequestDto = new UserRequestDto(this.username, this.email, this.socialId, this.socialType, this.imgURL);
        return userRequestDto;
    }
}
