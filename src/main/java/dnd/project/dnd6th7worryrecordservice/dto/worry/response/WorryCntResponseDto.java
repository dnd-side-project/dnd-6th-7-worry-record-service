package dnd.project.dnd6th7worryrecordservice.dto.worry.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WorryCntResponseDto {
    private int worryCnt;
    private int meaningfulWorryCnt;
    private String username;
    private LocalDateTime worryStartDate;
    private String categoryName;

    @Builder
    public WorryCntResponseDto(int worryCnt, int meaningfulWorryCnt, String username, LocalDateTime worryStartDate, String categoryName) {
        this.worryCnt = worryCnt;
        this.meaningfulWorryCnt = meaningfulWorryCnt;
        this.username = username;
        this.worryStartDate = worryStartDate;
        this.categoryName = categoryName;
    }
}
