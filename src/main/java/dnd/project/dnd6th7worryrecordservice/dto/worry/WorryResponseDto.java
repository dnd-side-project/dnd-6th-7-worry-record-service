package dnd.project.dnd6th7worryrecordservice.dto.worry;

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
    private boolean isFinished;
    private boolean isRealized;
    private boolean isLocked;
    private String categoryName;

    @Builder
    public WorryResponseDto(Long worryId, LocalDateTime worryStartDate, LocalDateTime worryExpiryDate, boolean isFinished, boolean isRealized, boolean isLocked, Category category) {
        this.worryId = worryId;
        this.worryStartDate = worryStartDate;
        this.worryExpiryDate = worryExpiryDate;
        this.isFinished = isFinished;
        this.isRealized = isRealized;
        this.isLocked = isLocked;
        this.categoryName = category.getCategoryName();
    }

    @Builder
    public WorryResponseDto(Long worryId, LocalDateTime worryStartDate, LocalDateTime worryExpiryDate, boolean isRealized, boolean isLocked, Category category) {
        this.worryId = worryId;
        this.worryStartDate = worryStartDate;
        this.worryExpiryDate = worryExpiryDate;
        this.isRealized = isRealized;
        this.isLocked = isLocked;
        this.categoryName = category.getCategoryName();
    }
}
