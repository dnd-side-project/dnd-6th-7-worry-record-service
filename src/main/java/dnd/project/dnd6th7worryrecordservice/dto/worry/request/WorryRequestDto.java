package dnd.project.dnd6th7worryrecordservice.dto.worry.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WorryRequestDto {

    private Long userId;
    private Long categoryId;
    private String worryText;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime worryExpiryDate;
    private boolean isLocked;

    @Builder
    public WorryRequestDto(Long userId, Long categoryId, String worryText, LocalDateTime worryExpiryDate, boolean isLocked) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.worryText = worryText;
        this.worryExpiryDate = worryExpiryDate;
        this.isLocked = isLocked;
    }
}
