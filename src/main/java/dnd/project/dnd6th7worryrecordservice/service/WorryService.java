package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.domain.worry.Worry;
import dnd.project.dnd6th7worryrecordservice.domain.worry.WorryRepository;
import dnd.project.dnd6th7worryrecordservice.dto.worry.request.WorryRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.worry.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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

    //홈 화면
    public WorryHomeResponseDto home(Long userId) {
        Optional<User> optionalUser = userService.findUserByUserId(userId);
        if (optionalUser.isPresent()) {
            LocalDateTime time = LocalDateTime.now();

            double meanlessWorryCnt = 0;
            double finishedWorryCnt = 0;
            List<Worry> worryList = worryRepository.findByUserAndWorryStartDateBetween(optionalUser.get(), time.minusMonths(2), time);
            for (Worry worry : worryList) {
                if (worry.isFinished() == true)
                    finishedWorryCnt++;

                if (worry.isFinished() == true && worry.isRealized() == false)
                    meanlessWorryCnt++;
            }
            String meanlessWorryPer = Math.round((meanlessWorryCnt / finishedWorryCnt * 100)) + "%";

            WorryHomeResponseDto worryResponseDto = new WorryHomeResponseDto(meanlessWorryPer, (short) worryList.size());
            return worryResponseDto;
        } else {
            return null;
        }
    }

    //걱정 작성
    public boolean addWorry(WorryRequestDto worryRequestDto) {
        Optional<User> optionalUser = userService.findUserByUserId(worryRequestDto.getUserId());
        if (optionalUser.isPresent()) {
            LocalDateTime time = LocalDateTime.now();
            Worry worry = Worry.builder()
                    .user(optionalUser.get())
                    .category(categoryService.findCategoryByCategoryId(worryRequestDto.getCategoryId()))
                    .worryText(worryRequestDto.getWorryText())
                    .worryStartDate(time)
                    .worryExpiryDate(worryRequestDto.getWorryExpiryDate())
                    .build();

            worryRepository.save(worry);
            return true;
        } else {
            return false;
        }
    }

    //걱정 보관함 - 요즘 걱정
    public List<WorryResponseDto> findRecentWorry(Long userId){
        Optional<User> optionalUser = userService.findUserByUserId(userId);
        if(optionalUser.isPresent()){
            LocalDateTime time = LocalDateTime.now();
            List<WorryResponseDto> worryDtoList = new ArrayList<>();

            List<Worry> worryList = worryRepository.findByUserAndWorryStartDateBetween(optionalUser.get(), time.minusMonths(2), time);
            for (Worry worry : worryList) {
                if(worry.isFinished() == false) {
                    WorryResponseDto responseDto = getWorryResponseDto(worry);
                    worryDtoList.add(responseDto);
                }
            }
            return worryDtoList;
        } else{
            return null;
        }
    }

    //걱정 보관함 - 요즘 걱정 - 걱정 카테고리 필터링
    public List<WorryResponseDto> findCategorizedRecentWorry(Long userId, List<Long> categoryId){
        Optional<User> optionalUser = userService.findUserByUserId(userId);
        if(optionalUser.isPresent()){
            LocalDateTime time = LocalDateTime.now();
            List<WorryResponseDto> worryDtoList = new ArrayList<>();

            List<Category> categoryList = new ArrayList<>();
            for (Long id : categoryId) {
                categoryList.add(categoryService.findCategoryByCategoryId(id));
            }

            List<Worry> worryList = worryRepository.findByUserAndIsFinishedAndCategoryInAndWorryStartDateBetweenOrderByWorryStartDateAsc(
                    optionalUser.get(), false, categoryList, time.minusMonths(2), time);
            for (Worry worry : worryList) {
                WorryResponseDto responseDto = getWorryResponseDto(worry);
                worryDtoList.add(responseDto);
            }
            return worryDtoList;
        }else{
            return null;
        }
    }

    //걱정 보관함 - 지난 걱정
    public List<WorryResponseDto> findWorryByIsFinished(boolean isFinished, Long userId) {
        Optional<User> user = userService.findUserByUserId(userId);
        if (isFinished == true && user.isPresent()) {
            List<WorryResponseDto> worryDtoList = new ArrayList<>();

            List<Worry> worryList = worryRepository.findByIsFinishedAndUserOrderByWorryStartDateAsc(isFinished, user.get());
            for (Worry worry : worryList) {
                WorryResponseDto responseDto = getWorryResponseDto(worry);
                worryDtoList.add(responseDto);
            }
            return worryDtoList;
        } else {
            return null;
        }
    }


    //걱정 보관함 - 지난 걱정 - 걱정 카테고리 필터링
    public List<WorryResponseDto> findCategorizedPastWorry(Long userId, List<Long> categoryId) {

        Optional<User> user = userService.findUserByUserId(userId);
        if (user.isPresent()) {

            List<Category> categoryList = new ArrayList<>();
            for (Long id : categoryId) {
                categoryList.add(categoryService.findCategoryByCategoryId(id));
            }

            List<Worry> worryList = worryRepository.findByUserAndIsFinishedAndCategoryInOrderByWorryStartDateAsc(user.get(), true, categoryList);
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
    public List<WorryResponseDto> findMeanOrMeanlessWorry(Long userId, boolean isRealized) {
        Optional<User> user = userService.findUserByUserId(userId);
        if (user.isPresent()) {

            List<Worry> worryList = worryRepository.findByUserAndIsFinishedAndIsRealizedOrderByWorryStartDateAsc(user.get(), true, isRealized);
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

    //걱정 보관함 - 걱정 잠금/해제
    public void turnWorryLockState(Long worryId) {
        Worry worry = worryRepository.findWorryByWorryId(worryId);
        if (worry.isLocked() == true)
            worryRepository.openLockState(worryId, false);
        else
            worryRepository.closeLockState(worryId, true);
    }

    //걱정 보관함 - 걱정 삭제
    public void deleteWorry(Long worryId) {
        Worry worry = worryRepository.findWorryByWorryId(worryId);
        worryRepository.delete(worry);
    }

    //걱정 후기 채팅방 - 열기
    public WorryChatResponseDto worryChatOpen(Long worryId) {
        Worry worry = worryRepository.findWorryByWorryId(worryId);
        WorryChatResponseDto worryChatResponseDto = WorryChatResponseDto.builder()
                .worryStartDate(worry.getWorryStartDate())
                .categoryName(worry.getCategory().getCategoryName())
                .worryText(worry.getWorryText())
                .build();

        return worryChatResponseDto;
    }

    //걱정 후기 채팅방 - 걱정 실현 여부 입력
    public WorryCntResponseDto worryChatSetRealized(Long userId, Long worryId, boolean isRealized) {
        worryRepository.setIsRealized(worryId, isRealized);
        Optional<User> optionalUser = userService.findUserByUserId(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            LocalDateTime time = LocalDateTime.now();

            List<Worry> worryList = worryRepository.findByUserAndWorryStartDateBetweenOrderByWorryStartDateAsc(user, time.minusMonths(2), time);
            int realizedCnt = 0;

            for (Worry w : worryList) {
                if (w.isRealized() == true) {
                    realizedCnt++;
                }
            }

            WorryCntResponseDto worryCntResponseDto = WorryCntResponseDto.builder()
                    .worryCnt(worryList.size())
                    .meaningfulWorryCnt(realizedCnt)
                    .build();

            return worryCntResponseDto;
        } else {
            return null;
        }
    }

    //걱정 후기 채팅방 - 걱정 만료일 수정
    public boolean changeWorryExpiryDate(Long worryId, LocalDateTime worryExpiryDate) {
        try {
            Worry worry = worryRepository.findWorryByWorryId(worryId);
            worryRepository.changeExpiryDate(worryId, worryExpiryDate);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    //걱정 후기 불러오기
    public WorryReviewResponseDto checkIsLockedAndCallReview(Long worryId) {
        Worry worry = worryRepository.findWorryByWorryId(worryId);
        if (worry.isLocked() == false) {

            WorryReviewResponseDto worryDto = WorryReviewResponseDto.builder()
                    .worryId(worry.getWorryId())
                    .worryStartDate(worry.getWorryStartDate())
                    .categoryName(worry.getCategory().getCategoryName())
                    .worryText(worry.getWorryText())
                    .worryReview(worry.getWorryReview())
                    .build();

            return worryDto;
        } else {
            return null;
        }
    }

    //worries/review/realize
    public void worryReviewModifyRealized(Long worryId, boolean isRealized) {
        worryRepository.setIsRealized(worryId, isRealized);
    }

    //worries/review - patch
    public void worryReviewModifyText(Long worryId, String worryText) {
        worryRepository.changeWorryReview(worryId, worryText);
    }


    private WorryResponseDto getWorryResponseDto(Worry worry) {
        WorryResponseDto responseDto = WorryResponseDto.builder()
                .worryId(worry.getWorryId())
                .worryStartDate(worry.getWorryStartDate())
                .worryExpiryDate(worry.getWorryExpiryDate())
                .isRealized(worry.isRealized())
                .isFinished(worry.isFinished())
                .isLocked(worry.isLocked())
                .category(worry.getCategory())
                .build();
        return responseDto;

    }
}
