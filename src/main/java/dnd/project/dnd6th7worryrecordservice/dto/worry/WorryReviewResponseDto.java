package dnd.project.dnd6th7worryrecordservice.dto.worry;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class WorryReviewResponseDto {

    private Long worryId;
    private LocalDateTime worryStartDate;
    private String categoryName;
    private String worryText;
    private String worryReview;
}
