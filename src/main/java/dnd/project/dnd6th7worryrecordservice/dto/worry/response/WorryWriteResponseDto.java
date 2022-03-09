package dnd.project.dnd6th7worryrecordservice.dto.worry.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WorryWriteResponseDto {

    private String imgUrl;
    private LocalDateTime worryStartDate;
}
