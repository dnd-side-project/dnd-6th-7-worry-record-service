package dnd.project.dnd6th7worryrecordservice.dto.worry;

import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.worry.Worry;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class WorryRequestDto {

    private Long worryId;
    private LocalDateTime worryStartDate;
    private LocalDateTime worryExpiryDate;
    private boolean isFinished;
    private boolean isRealized;
    private boolean isLocked;
    private Category category;

    public WorryRequestDto(Long worryId, LocalDateTime worryStartDate, LocalDateTime worryExpiryDate, boolean isFinished, boolean isRealized, boolean isLocked, Category category) {
        this.worryId = worryId;
        this.worryStartDate = worryStartDate;
        this.worryExpiryDate = worryExpiryDate;
        this.isFinished = isFinished;
        this.isRealized = isRealized;
        this.isLocked = isLocked;
        this.category = category;
    }

    public WorryRequestDto(Long worryId, LocalDateTime worryStartDate, LocalDateTime worryExpiryDate, boolean isRealized, boolean isLocked, Category category) {
        this.worryId = worryId;
        this.worryStartDate = worryStartDate;
        this.worryExpiryDate = worryExpiryDate;
        this.isRealized = isRealized;
        this.isLocked = isLocked;
        this.category = category;
    }
}
