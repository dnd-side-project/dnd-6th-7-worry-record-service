package dnd.project.dnd6th7worryrecordservice.dto.worry;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WorryReviewModifyRequestDto {
    private Long worryId;
    private String worryReview;
}
