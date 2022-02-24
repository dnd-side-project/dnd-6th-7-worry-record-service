package dnd.project.dnd6th7worryrecordservice.dto.worry.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WorryCntResponseDto {
    private int worryCnt;
    private int meaningfulWorryCnt;

    @Builder
    public WorryCntResponseDto(int worryCnt, int meaningfulWorryCnt) {
        this.worryCnt = worryCnt;
        this.meaningfulWorryCnt = meaningfulWorryCnt;
    }
}
