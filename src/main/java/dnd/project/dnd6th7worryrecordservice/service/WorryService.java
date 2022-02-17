package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.category.CategoryRepository;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.domain.user.UserRepository;
import dnd.project.dnd6th7worryrecordservice.domain.worry.Worry;
import dnd.project.dnd6th7worryrecordservice.domain.worry.WorryRepository;
import dnd.project.dnd6th7worryrecordservice.dto.worry.WorryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class WorryService {
    private final WorryRepository worryRepository;
    private final UserService userService;
    private final CategoryService categoryService;


    public List<WorryResponseDto> findByWorryIsFinished(boolean isFinished, Long userId) {
        Optional<User> user = userService.findUserByUserId(userId);
        if (isFinished == true && user.isPresent()) {
            List<WorryResponseDto> worryDtoList = new ArrayList<>();

            List<Worry> worryList = worryRepository.findByIsFinishedAndUserOrderByWorryStartDateAsc(isFinished, user.get());
            for (Worry worry : worryList) {
                WorryResponseDto responseDto = getWorryResponseDto_noIsFinished(worry);
                worryDtoList.add(responseDto);
            }
            return worryDtoList;
        } else {
            return null;
        }
    }


    //걱정 보관함 - 지난 걱정 - 걱정 카테고리 필터링
    public List<WorryResponseDto> findCategorizedPastWorry(Long userId, List<Long> CategoryId) {

        Optional<User> user = userService.findUserByUserId(userId);
        if (user.isPresent()) {

            List<Category> CategoryList = new ArrayList<>();
            for (Long id : CategoryId) {
                CategoryList.add(categoryService.findCategoryByCategoryId(id));
            }

            List<Worry> worryList = worryRepository.findByUserAndCategoryInOrderByWorryStartDateAsc(user.get(), CategoryList);
            List<WorryResponseDto> worryDtoList = new ArrayList<>();

            for (Worry worry : worryList) {
                WorryResponseDto responseDto = getWorryResponseDto(worry);
                worryDtoList.add(responseDto);
            }

            return worryDtoList;
        } else {
            return null;
        }
    }

    //걱정 보관함 - 지난 걱정 - 의미 있는 걱정/의미 없는 걱정
    public List<WorryResponseDto> findMeanOrMeanlessWorry(Long userId, boolean isRealized){
        Optional<User> user = userService.findUserByUserId(userId);
        if(user.isPresent()){

            List<Worry> worryList = worryRepository.findByUserAndIsFinishedAndIsRealized(user.get(), true, isRealized);
            List<WorryResponseDto> worryDtoList = new ArrayList<>();

            for (Worry worry : worryList) {
                WorryResponseDto responseDto = getWorryResponseDto(worry);
                worryDtoList.add(responseDto);
            }

            return worryDtoList;
        } else{
            return null;
        }
    }

    //걱정 보관함 - 걱정 잠금/해제
    public void turnWorryLockState(Long worryId){
        Worry worry = worryRepository.findWorryByWorryId(worryId);
        worry.changeLockState();
    }

    //걱정 보관함 - 걱정 삭제
    public void deleteWorry(Long worryId){
        Worry worry = worryRepository.findWorryByWorryId(worryId);
        worryRepository.delete(worry);
    }



    private WorryResponseDto getWorryResponseDto(Worry worry) {
        WorryResponseDto responseDto = WorryResponseDto.builder()
                .worryId(worry.getWorryId())
                .worryStartDate(worry.getWorryStartDate())
                .worryExpiryDate(worry.getWorryExpiryDate())
                .isFinished(worry.isFinished())
                .isRealized(worry.isRealized())
                .isLocked(worry.isLocked())
                .category(worry.getCategory())
                .build();
        return responseDto;

    }


    private WorryResponseDto getWorryResponseDto_noIsFinished(Worry worry) {
        WorryResponseDto responseDto = WorryResponseDto.builder()
                .worryId(worry.getWorryId())
                .worryStartDate(worry.getWorryStartDate())
                .worryExpiryDate(worry.getWorryExpiryDate())
                .isRealized(worry.isRealized())
                .isLocked(worry.isLocked())
                .category(worry.getCategory())
                .build();
        return responseDto;


    }
}
