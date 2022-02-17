package dnd.project.dnd6th7worryrecordservice.domain.worry;

import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorryRepository extends JpaRepository<Worry, Long> {

    Worry save(Worry worry);

    Worry findWorryByWorryId(Long worryId);

    //worries/past
    List<Worry> findByIsFinishedAndUserOrderByWorryStartDateAsc(boolean isFinished, User user);

    //worries/past/filter
    List<Worry> findByUserAndCategoryInOrderByWorryStartDateAsc(User user, List<Category> categoryList);

    //worries/past/mean,meanless
    List<Worry> findByUserAndIsFinishedAndIsRealized(User user ,boolean isFinished, boolean isRealized);

    //worries/{worryId} - delete
    void delete(Worry worry);

}
