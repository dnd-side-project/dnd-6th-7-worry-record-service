package dnd.project.dnd6th7worryrecordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class UserResponseDto {

    private String jwtAccessToken;
    private String jwtRefreshToken;
}
