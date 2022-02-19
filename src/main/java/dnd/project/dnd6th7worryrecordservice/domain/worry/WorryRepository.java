package dnd.project.dnd6th7worryrecordservice.domain.worry;

import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface WorryRepository extends JpaRepository<Worry, Long> {

    Worry save(Worry worry);

    Worry findWorryByWorryId(Long worryId);

    //worries/past
    List<Worry> findByIsFinishedAndUserOrderByWorryStartDateAsc(boolean isFinished, User user);

    //worries/past/filter
    List<Worry> findByUserAndCategoryInOrderByWorryStartDateAsc(User user, List<Category> categoryList);

    //worries/past/mean,meanless
    List<Worry> findByUserAndIsFinishedAndIsRealizedOrderByWorryStartDateAsc(User user , boolean isFinished, boolean isRealized);

    //worries/chat/realized/{worryId}
    List<Worry> findByUserAndWorryStartDateBetweenOrderByWorryStartDateAsc(User user, LocalDateTime fromDate, LocalDateTime toDate);

    //worries/{worryId} - delete
    void delete(Worry worry);

    //worries/review/date
    @Modifying
    @Query("update Worry w set w.worryExpiryDate = ?2 where w.worryId = ?1")
    void changeExpiryDate(Long worryId, LocalDateTime expiryDate);

    //worries/{worryId} - patch
    @Modifying
    @Query("update Worry w set w.isLocked = ?2 where w.worryId = ?1")
    void openLockState(Long worryId, boolean state);

    //worries/{worryId} - patch
    @Modifying
    @Query("update Worry w set w.isLocked = ?2 where w.worryId = ?1")
    void closeLockState(Long worryId, boolean state);

    //worries/chat/realize
    @Modifying
    @Query("update Worry w set w.isRealized = ?2 where w.worryId = ?1")
    void setIsRealized(Long worryId, boolean state);

}
