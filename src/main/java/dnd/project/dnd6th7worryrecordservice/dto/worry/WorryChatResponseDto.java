package dnd.project.dnd6th7worryrecordservice.dto.worry;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WorryChatResponseDto {

    private LocalDateTime worryStartDate;
    private String categoryName;
    private String worryText;


    @Builder
    public WorryChatResponseDto(LocalDateTime worryStartDate, String categoryName, String worryText) {
        this.worryStartDate = worryStartDate;
        this.categoryName = categoryName;
        this.worryText = worryText;
    }
}
