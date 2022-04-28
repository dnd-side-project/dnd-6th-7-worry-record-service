package dnd.project.dnd6th7worryrecordservice.dto.jwt;

import dnd.project.dnd6th7worryrecordservice.domain.user.SocialType;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserInfoDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtPayloadDto {
    private String socialId;
    private SocialType socialType;
}
