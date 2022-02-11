package dnd.project.dnd6th7worryrecordservice.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String imgURL;

}
