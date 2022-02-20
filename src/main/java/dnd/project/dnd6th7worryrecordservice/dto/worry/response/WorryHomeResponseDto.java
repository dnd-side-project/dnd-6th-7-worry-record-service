package dnd.project.dnd6th7worryrecordservice.dto.worry.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorryHomeResponseDto {
    private String meanlessWorryPer;
    private short recentWorryCnt;
}
