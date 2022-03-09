package dnd.project.dnd6th7worryrecordservice.dto.worry.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorryHomeResponseDto {
    private short meanlessWorryPer;
    private short recentWorryCnt;
    private String imgUrl;
}
