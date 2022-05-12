package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.aws.S3Util;
import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.domain.worry.Worry;
import dnd.project.dnd6th7worryrecordservice.domain.worry.WorryRepository;
import dnd.project.dnd6th7worryrecordservice.dto.worry.request.WorryRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.worry.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class WorryService {
    private final WorryRepository worryRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final S3Util s3Util;

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

            short meanlessWorryPer;
            if (finishedWorryCnt == 0) {
                meanlessWorryPer = 100;
            } else {
                meanlessWorryPer = (short) Math.round((meanlessWorryCnt / finishedWorryCnt * 100));
            }

            int worryCnt = (int) (worryList.size() - finishedWorryCnt);

            String cloudCnt = setCloudCnt(worryCnt);
            String categoryName = setCategoryName(userId, cloudCnt);

            String imgUrl = s3Util.downloadFile(categoryName + cloudCnt);

            WorryHomeResponseDto worryResponseDto = new WorryHomeResponseDto(meanlessWorryPer, (short) worryCnt, imgUrl);
            return worryResponseDto;
        } else
            log.error("Error :: UserId \"{}\" is not Present",userId);
        return null;
    }

    //걱정 작성
    public WorryWriteResponseDto addWorry(WorryRequestDto worryRequestDto) {
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

            WorryWriteResponseDto worryWriteResponseDto = WorryWriteResponseDto.builder()
                    .worryStartDate(time)
                    .imgUrl(s3Util.downloadFile("worryWriteGIF/write"))
                    .build();

            return worryWriteResponseDto;
        } else
            log.error("Error :: UserId \"{}\" in RequestDto is not present!!!",worryRequestDto.getWorryText());
        return null;
    }

    //걱정 보관함 - 요즘 걱정
    public List<WorryResponseDto> findRecentWorry(Long userId) {
        Optional<User> optionalUser = userService.findUserByUserId(userId);
        if (optionalUser.isPresent()) {
            LocalDateTime time = LocalDateTime.now();
            List<WorryResponseDto> worryDtoList = new ArrayList<>();

            List<Worry> worryList = worryRepository.findByUserAndWorryStartDateBetween(optionalUser.get(), time.minusMonths(2), time);
            for (Worry worry : worryList) {
                if (worry.isFinished() == false) {
                    WorryResponseDto responseDto = getWorryResponseDto(worry);
                    worryDtoList.add(responseDto);
                }
            }
            return worryDtoList;
        } else {
            log.error("Error :: UserId \"{}\" is not Present",userId);
            return null;
        }
    }

    //걱정 보관함 - 요즘 걱정 - 걱정 카테고리 필터링
    public List<WorryResponseDto> findCategorizedRecentWorry(Long userId, List<Long> categoryId) {
        Optional<User> optionalUser = userService.findUserByUserId(userId);
        if (optionalUser.isPresent()) {
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
        } else {
            log.error("Error :: UserId \"{}\" is not Present",userId);
            return null;
        }
    }

    //걱정 보관함 - 지난 걱정
    public List<WorryPastResponseDto> findWorryByIsFinished(boolean isFinished, Long userId) {
        Optional<User> user = userService.findUserByUserId(userId);
        if (isFinished == true && user.isPresent()) {
            List<WorryPastResponseDto> worryDtoList = new ArrayList<>();

            List<Worry> worryList = worryRepository.findByIsFinishedAndUserOrderByWorryStartDateAsc(isFinished, user.get());
            for (Worry worry : worryList) {
                WorryPastResponseDto responseDto = getWorryPastResponseDto(worry);
                worryDtoList.add(responseDto);
            }
            return worryDtoList;
        } else {
            log.error("Error :: isFinished is not \"True\" And UserId \"{}\" is not Present",userId);
            return null;
        }
    }


    //걱정 보관함 - 지난 걱정 - 걱정 카테고리 필터링
    public List<WorryPastResponseDto> findCategorizedPastWorry(Long userId, List<Long> categoryId) {

        Optional<User> user = userService.findUserByUserId(userId);
        if (user.isPresent()) {

            List<Category> categoryList = new ArrayList<>();
            for (Long id : categoryId) {
                categoryList.add(categoryService.findCategoryByCategoryId(id));
            }

            List<Worry> worryList = worryRepository.findByUserAndIsFinishedAndCategoryInOrderByWorryStartDateAsc(user.get(), true, categoryList);
            List<WorryPastResponseDto> worryDtoList = new ArrayList<>();

            for (Worry worry : worryList) {
                WorryPastResponseDto responseDto = getWorryPastResponseDto(worry);
                worryDtoList.add(responseDto);
            }

            return worryDtoList;
        } else {
            log.error("Error :: UserId \"{}\" is not Present",userId);
            return null;
        }
    }

    //걱정 보관함 - 지난 걱정 - 의미 있는 걱정/의미 없는 걱정
    public List<WorryPastResponseDto> findMeanOrMeanlessWorry(Long userId, boolean isRealized) {
        Optional<User> user = userService.findUserByUserId(userId);
        if (user.isPresent()) {

            List<Worry> worryList = worryRepository.findByUserAndIsFinishedAndIsRealizedOrderByWorryStartDateAsc(user.get(), true, isRealized);
            List<WorryPastResponseDto> worryDtoList = new ArrayList<>();

            for (Worry worry : worryList) {
                WorryPastResponseDto responseDto = getWorryPastResponseDto(worry);
                worryDtoList.add(responseDto);
            }

            return worryDtoList;
        } else {
            log.error("Error :: UserId \"{}\" is not Present",userId);
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
    public void deleteWorry(List<Long> worryIds) {
        for (Long worryId : worryIds) {
            Worry worry = worryRepository.findWorryByWorryId(worryId);
            worryRepository.delete(worry);
        }
    }

    //걱정 후기 채팅방 - 열기
    public WorryChatResponseDto worryChatOpen(Long worryId) {
        Worry worry = worryRepository.findWorryByWorryId(worryId);
        WorryChatResponseDto worryChatResponseDto = WorryChatResponseDto.builder()
                .worryStartDate(worry.getWorryStartDate())
                .categoryName(worry.getCategory().getCategoryName())
                .worryText(worry.getWorryText())
                .username(worry.getUser().getUsername())
                .build();

        return worryChatResponseDto;
    }

    //걱정 후기 채팅방 - 걱정 실현 여부 입력
    public WorryCntResponseDto worryChatSetRealized(Long userId, Long worryId, boolean isRealized) {
        Optional<User> optionalUser = userService.findUserByUserId(userId);

        if (optionalUser.isPresent()) {
            worryRepository.setIsRealized(worryId, isRealized);
            worryRepository.setIsFinished(worryId, true);
            Worry worry = worryRepository.findWorryByWorryId(worryId);

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
                    .username(user.getUsername())
                    .categoryName(worry.getCategory().getCategoryName())
                    .worryStartDate(worry.getWorryStartDate())
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
            worryRepository.setIsFinished(worryId, false);
            return true;
        } catch (Exception e) {
            log.error("Error :: {}", e.getMessage());
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
                    .isRealized(worry.isRealized())
                    .build();

            return worryDto;
        } else {
            log.error("Error :: worry.isLocked is \"{}\"",worry.isLocked());
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

    private WorryPastResponseDto getWorryPastResponseDto(Worry worry) {
        WorryPastResponseDto responseDto = WorryPastResponseDto.builder()
                .worryId(worry.getWorryId())
                .worryStartDate(worry.getWorryStartDate())
                .worryExpiryDate(worry.getWorryExpiryDate())
                .isRealized(worry.isRealized())
                .isFinished(worry.isFinished())
                .isLocked(worry.isLocked())
                .category(worry.getCategory())
                .worryReview(worry.getWorryReview())
                .build();
        return responseDto;

    }

    private String setCategoryName(Long userId, String cloudCnt) {
        if (cloudCnt == "_0" || cloudCnt == "_1" || cloudCnt == "_2" || cloudCnt == "_3")
            return "기본";
        else {
            Optional<User> optionalUser = userService.findUserByUserId(userId);
            if (optionalUser.isPresent()) {
                List<Worry> worry = worryRepository.findCategory(optionalUser.get());
                return worry.get(0).getCategory().getCategoryName();
            } else
                log.error("Error :: UserId \"{}\" is not Present",userId);
            return null;
        }
    }

    public List<Worry> findWorryByUser(User user){
        return worryRepository.findByUser(user);
    }

    private String setCloudCnt(int worryCnt) {
        if (worryCnt == 0) {
            return "_0";
        } else if (worryCnt == 1) {
            return "_1";
        } else if (worryCnt == 2) {
            return "_2";
        } else if (worryCnt == 3) {
            return "_3";
        } else if (worryCnt >= 4 && worryCnt <= 9) {
            return "_4";
        } else
            return "_5";
    }
}
