package dnd.project.dnd6th7worryrecordservice.domain.worry;

import com.sun.istack.NotNull;
import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Getter
@NoArgsConstructor
@Table(name = "WORRY")
@Entity
public class Worry {

    @Id
    @GeneratedValue
    private Long worryId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;

    @Column(name = "worryText", nullable = false)
    @NotNull
    private String worryText;

    @Column(name = "isFinished")
    private boolean isFinished;

    @Column(name = "isRealized")
    private boolean isRealized;

    @Column(name = "isLocked")
    private boolean isLocked;

    @Column(name = "worryStartDate", nullable = false)
    private LocalDateTime worryStartDate;

    @Column(name = "worryExpiryDate", nullable = false)
    private LocalDateTime worryExpiryDate;

    @Column(name = "worryReview")
    private String worryReview;

    @Builder
    public Worry(User user, Category category, String worryText, LocalDateTime worryStartDate, LocalDateTime worryExpiryDate, boolean isLocked) {
        this.user = user;
        this.category = category;
        this.worryText = worryText;
        this.worryStartDate = worryStartDate;
        this.worryExpiryDate = worryExpiryDate;
        this.isLocked = isLocked;
    }
}
