package dnd.project.dnd6th7worryrecordservice.domain.worry;

import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Worry {

    @Id @GeneratedValue
    private Long worryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    private String worryText;

    private boolean isSolved;

    private boolean isRealized;

    private boolean isLocked;

    private LocalDateTime worryStartDate;

    private LocalDateTime worryExpiryDate;

    private String worryReview;
}
