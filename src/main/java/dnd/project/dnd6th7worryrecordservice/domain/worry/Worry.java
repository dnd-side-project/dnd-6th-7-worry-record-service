package dnd.project.dnd6th7worryrecordservice.domain.worry;

import com.sun.istack.NotNull;
import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

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
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @Column(name = "worryText", nullable = false)
    @NotNull
    private String worryText;

    @Column(name = "isFinished", columnDefinition = "boolean default false", nullable = false)
    private boolean isFinished;

    @Column(name = "isRealized")
    private boolean isRealized;

    @Column(name = "isLocked", columnDefinition = "boolean default true", nullable = false)
    private boolean isLocked;

    @Column(name = "worryStartDate", nullable = false)
    private LocalDateTime worryStartDate;

    @Column(name = "worryExpiryDate", nullable = false)
    private LocalDateTime worryExpiryDate;

    @Column(name = "worryReview", nullable = false)
    private String worryReview;

    public Worry(User user, Category category, String worryText, boolean isFinished, boolean isRealized, boolean isLocked, LocalDateTime worryStartDate, LocalDateTime worryExpiryDate, String worryReview) {
        this.user = user;
        this.category = category;
        this.worryText = worryText;
        this.isFinished = isFinished;
        this.isRealized = isRealized;
        this.isLocked = isLocked;
        this.worryStartDate = worryStartDate;
        this.worryExpiryDate = worryExpiryDate;
        this.worryReview = worryReview;
    }
}
