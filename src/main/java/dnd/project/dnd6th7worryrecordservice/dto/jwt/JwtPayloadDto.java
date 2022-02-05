package dnd.project.dnd6th7worryrecordservice.dto.jwt;

import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtPayloadDto {
    private String username;
    private String email;
    private String kakaoId;
    private String imgURL;

    public UserRequestDto toUserRequestDto(){
        UserRequestDto userRequestDto = new UserRequestDto(this.username, this.email, this.kakaoId, this.imgURL);
        return userRequestDto;
    }
}
