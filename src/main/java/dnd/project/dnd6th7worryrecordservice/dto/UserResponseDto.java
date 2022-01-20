package dnd.project.dnd6th7worryrecordservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String email;
    private String imgURL;

}
