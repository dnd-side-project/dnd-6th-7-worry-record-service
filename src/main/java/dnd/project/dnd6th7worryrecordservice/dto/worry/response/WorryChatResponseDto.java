package dnd.project.dnd6th7worryrecordservice.dto.worry.response;

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
    private String username;


    @Builder
    public WorryChatResponseDto(LocalDateTime worryStartDate, String categoryName, String worryText, String username) {
        this.worryStartDate = worryStartDate;
        this.categoryName = categoryName;
        this.worryText = worryText;
        this.username = username;
    }
}
