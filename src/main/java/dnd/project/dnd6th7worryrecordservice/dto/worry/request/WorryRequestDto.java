package dnd.project.dnd6th7worryrecordservice.dto.worry.request;

import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import lombok.*;
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

    @Builder
    public WorryRequestDto(Long userId, Long categoryId, String worryText, LocalDateTime worryExpiryDate) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.worryText = worryText;
        this.worryExpiryDate = worryExpiryDate;
    }
}
