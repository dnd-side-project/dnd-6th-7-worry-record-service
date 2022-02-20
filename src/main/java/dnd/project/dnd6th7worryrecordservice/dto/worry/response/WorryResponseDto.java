package dnd.project.dnd6th7worryrecordservice.dto.worry.response;

import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WorryResponseDto {

    private Long worryId;
    private LocalDateTime worryStartDate;
    private LocalDateTime worryExpiryDate;
    private boolean isRealized;
    private boolean isFinished;
    private boolean isLocked;
    private String categoryName;

    @Builder
    public WorryResponseDto(Long worryId, LocalDateTime worryStartDate, LocalDateTime worryExpiryDate, boolean isRealized, boolean isFinished, boolean isLocked, Category category) {
        this.worryId = worryId;
        this.worryStartDate = worryStartDate;
        this.worryExpiryDate = worryExpiryDate;
        this.isRealized = isRealized;
        this.isFinished = isFinished;
        this.isLocked = isLocked;
        this.categoryName = category.getCategoryName();
    }
}
