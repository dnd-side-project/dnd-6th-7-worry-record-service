package dnd.project.dnd6th7worryrecordservice.api;

import dnd.project.dnd6th7worryrecordservice.dto.worry.request.WorryRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.worry.request.WorryReviewModifyRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.worry.response.*;
import dnd.project.dnd6th7worryrecordservice.service.WorryService;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/worries")
@RestController
public class WorryController {
    private final WorryService worryService;

    //홈 화면
    @GetMapping("/home")
    public ResponseEntity<WorryHomeResponseDto> home(@RequestParam("userId") Long userId) {
        try {
            WorryHomeResponseDto worryHomeResponseDto = worryService.home(userId);
            return ResponseEntity.ok(worryHomeResponseDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //홈 - 걱정 작성
    @PostMapping("/write")
    public ResponseEntity addWorry(@RequestBody WorryRequestDto worryRequestDto) {
        boolean check = worryService.addWorry(worryRequestDto);
        if (check == true)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //걱정 보관함 - 요즘 걱정
    @GetMapping("/recent")
    public ResponseEntity<List<WorryResponseDto>> allRecentWorryList(@RequestParam("userId") Long userId) {
        try {
            List<WorryResponseDto> worryList = worryService.findRecentWorry(userId);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //걱정 보관함 - 요즘 걱정 - 걱정 카테고리 필터링
    @GetMapping("/recent/filter")
    public ResponseEntity<List<WorryResponseDto>> categorizedRecentWorry(@RequestParam("userId") Long userId, @RequestParam("categories") List<Long> categoryId) {
        try {
            List<WorryResponseDto> worryList = worryService.findCategorizedRecentWorry(userId, categoryId);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //걱정 보관함 - 지난 걱정
    @GetMapping("/past")
    public ResponseEntity<List<WorryResponseDto>> allPastWorryList(@RequestParam("userId") Long userId) {
        try {
            List<WorryResponseDto> worryList = worryService.findWorryByIsFinished(true, userId);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //걱정 보관함 - 지난 걱정 - 걱정 카테고리 필터링
    @GetMapping("/past/filter")
    public ResponseEntity<List<WorryResponseDto>> categorizedPastWorry(@RequestParam("userId") Long userId, @RequestParam("categories") List<Long> categoryId) {
        try {
            List<WorryResponseDto> worryList = worryService.findCategorizedPastWorry(userId, categoryId);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //걱정 보관함 - 지난 걱정 - 의미 있는 걱정
    @GetMapping("/past/mean")
    public ResponseEntity<List<WorryResponseDto>> meaningfulWorry(@RequestParam("userId") Long userId) {
        try {
            List<WorryResponseDto> worryList = worryService.findMeanOrMeanlessWorry(userId, true);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //걱정 보관함 - 지난 걱정 - 의미 없는 걱정
    @GetMapping("/past/meanless")
    public ResponseEntity<List<WorryResponseDto>> meanlessWorry(@RequestParam("userId") Long userId) {
        try {
            List<WorryResponseDto> worryList = worryService.findMeanOrMeanlessWorry(userId, false);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //걱정 보관함 - 걱정 잠금/해제
    @PatchMapping("/{worryId}")
    public ResponseEntity turnLockState(@PathVariable Long worryId) {
        worryService.turnWorryLockState(worryId);
        return ResponseEntity.ok("");
    }

    //걱정 보관함 - 걱정 삭제
    @DeleteMapping("/{worryId}")
    public ResponseEntity removeWorry(@PathVariable Long worryId) {
        try {
            worryService.deleteWorry(worryId);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    //후기 작성 채팅방 - 열기
    @GetMapping("/chat/{worryId}")
    public ResponseEntity<WorryChatResponseDto> openWorryChat(@PathVariable Long worryId) {
        WorryChatResponseDto worryChatResponseDto = worryService.worryChatOpen(worryId);
        return ResponseEntity.ok(worryChatResponseDto);
    }

    //후기 작성 채팅방 - 걱정 실현 여부 입력
    @PostMapping("/chat/realize")
    public ResponseEntity inputWorryIsRealized(@RequestParam("userId") Long userId, @RequestParam("worryId") Long worryId, @RequestParam("isRealized") boolean isRealized) {
        try {
            WorryCntResponseDto worryCntResponseDto = worryService.worryChatSetRealized(userId, worryId, isRealized);
            Assert.notNull(worryCntResponseDto);
            return ResponseEntity.ok(worryCntResponseDto);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    //후기 작성 채팅방 - 걱정 만료일 수정
    @PutMapping("/review/date")
    public ResponseEntity changeWorryExpiryDate(@RequestParam("worryId") Long worryId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("expiryDate") LocalDateTime expiryDate) {
        boolean check = worryService.changeWorryExpiryDate(worryId, expiryDate);

        if (check == true) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    //걱정 후기 불러오기
    @GetMapping("/review/{worryId}")
    public ResponseEntity<WorryReviewResponseDto> callWorryReview(@PathVariable Long worryId) {
        try {
            WorryReviewResponseDto worryReviewResponseDto = worryService.checkIsLockedAndCallReview(worryId);
            Assert.notNull(worryReviewResponseDto);

            return ResponseEntity.ok(worryReviewResponseDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //걱정 후기 수정 - 실현 여부 수정
    @PatchMapping("/review/realize")
    public ResponseEntity modifyIsRealized(@RequestParam("worryId") Long worryId, @RequestParam("isRealized") boolean isRealized) {
        try {
            worryService.worryReviewModifyRealized(worryId, isRealized);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    //걱정 후기 수정 - 후기 내용 수정
    @PatchMapping("/review")
    public ResponseEntity modifyWorryReview(@RequestBody final WorryReviewModifyRequestDto worryRequestDto) {
        try {
            worryService.worryReviewModifyText(worryRequestDto.getWorryId(), worryRequestDto.getWorryReview());
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
