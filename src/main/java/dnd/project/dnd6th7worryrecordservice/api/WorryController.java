package dnd.project.dnd6th7worryrecordservice.api;

import dnd.project.dnd6th7worryrecordservice.dto.worry.request.WorryRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.worry.request.WorryReviewModifyRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.worry.response.*;
import dnd.project.dnd6th7worryrecordservice.service.WorryService;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.*;
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
    @ApiOperation(value = "홈 화면", notes = "홈 화면에 필요한 데이터 호출")
    @ApiImplicitParam(
            name = "userId"
            , value = "유저PK")
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
    @ApiOperation(value = "홈 - 걱정 작성", notes = "걱정 작성 후 저장")
    @PostMapping("/write")
    public ResponseEntity<WorryWriteResponseDto> addWorry(@RequestBody WorryRequestDto worryRequestDto) {
        try {
            WorryWriteResponseDto worryWriteResponseDto = worryService.addWorry(worryRequestDto);
            return ResponseEntity.ok(worryWriteResponseDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    //걱정 보관함 - 요즘 걱정
    @ApiOperation(value = "걱정 보관함 - 요즘 걱정", notes = "걱정 보관함에서 모든 요즘 걱정 불러오기")
    @ApiImplicitParam(
            name = "userId"
            , value = "유저PK")
    @GetMapping("/recent")
    public ResponseEntity<List<WorryResponseDto>> allRecentWorryList(@RequestParam("userId") Long userId) {
        try {
            List<WorryResponseDto> worryList = worryService.findRecentWorry(userId);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //걱정 보관함 - 요즘 걱정 - 걱정 카테고리 필터링
    @ApiOperation(value = "걱정 보관함 - 요즘 걱정 카테고리 필터링", notes = "걱정 보관함에서 카테고리 필터링된 요즘 걱정 불러오기")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId"
                    , value = "유저PK"),
            @ApiImplicitParam(
                    name = "categories"
                    , value = "카테고리PK List"
            )
    })
    @GetMapping("/recent/filter")
    public ResponseEntity<List<WorryResponseDto>> categorizedRecentWorry(@RequestParam("userId") Long userId, @RequestParam("categories") List<Long> categoryId) {
        try {
            List<WorryResponseDto> worryList = worryService.findCategorizedRecentWorry(userId, categoryId);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //걱정 보관함 - 지난 걱정
    @ApiOperation(value = "걱정 보관함 - 지난 걱정", notes = "걱정 보관함에서 모든 지난 걱정 불러오기")
    @ApiImplicitParam(
            name = "userId"
            , value = "유저PK")
    @GetMapping("/past")
    public ResponseEntity<List<WorryResponseDto>> allPastWorryList(@RequestParam("userId") Long userId) {
        try {
            List<WorryResponseDto> worryList = worryService.findWorryByIsFinished(true, userId);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //걱정 보관함 - 지난 걱정 - 걱정 카테고리 필터링
    @ApiOperation(value = "걱정 보관함 - 지난 걱정 카테고리 필터링", notes = "걱정 보관함에서 카테고리 필터링된 지난 걱정 불러오기")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId"
                    , value = "유저PK"),
            @ApiImplicitParam(
                    name = "categories"
                    , value = "카테고리PK List"
            )
    })
    @GetMapping("/past/filter")
    public ResponseEntity<List<WorryResponseDto>> categorizedPastWorry(@RequestParam("userId") Long userId, @RequestParam("categories") List<Long> categoryId) {
        try {
            List<WorryResponseDto> worryList = worryService.findCategorizedPastWorry(userId, categoryId);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //걱정 보관함 - 지난 걱정 - 의미 있는 걱정
    @ApiOperation(value = "걱정 보관함 - 지난 걱정 - 의미있는 걱정", notes = "걱정 보관함에서 실현된 지난 걱정 불러오기")
    @ApiImplicitParam(
            name = "userId"
            , value = "유저PK")
    @GetMapping("/past/mean")
    public ResponseEntity<List<WorryResponseDto>> meaningfulWorry(@RequestParam("userId") Long userId) {
        try {
            List<WorryResponseDto> worryList = worryService.findMeanOrMeanlessWorry(userId, true);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //걱정 보관함 - 지난 걱정 - 의미 없는 걱정
    @ApiOperation(value = "걱정 보관함 - 지난 걱정 - 의미없는 걱정", notes = "걱정 보관함에서 실현되지 않은 지난 걱정 불러오기")
    @ApiImplicitParam(
            name = "userId"
            , value = "유저PK")
    @GetMapping("/past/meanless")
    public ResponseEntity<List<WorryResponseDto>> meanlessWorry(@RequestParam("userId") Long userId) {
        try {
            List<WorryResponseDto> worryList = worryService.findMeanOrMeanlessWorry(userId, false);
            Assert.notNull(worryList);
            return ResponseEntity.ok(worryList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //걱정 보관함 - 걱정 잠금/해제
    @ApiOperation(value = "걱정 보관함 - 걱정 잠금/해제", notes = "걱정을 잠금 or 잠금 해제한다.")
    @ApiImplicitParam(
            name = "worryId"
            , value = "걱정PK")
    @PatchMapping("/{worryId}")
    public ResponseEntity turnLockState(@PathVariable Long worryId) {
        worryService.turnWorryLockState(worryId);
        return ResponseEntity.ok("");
    }

    //걱정 보관함 - 걱정 삭제
    @ApiOperation(value = "걱정 삭제", notes = "걱정 보관함의 걱정 삭제")
    @ApiImplicitParam(
            name = "worryId"
            , value = "걱정PK")
    @DeleteMapping
    public ResponseEntity removeWorry(@RequestParam("worryIds") List<Long> worryIds) {
        try {
            worryService.deleteWorry(worryIds);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    //후기 작성 채팅방 - 열기
    @ApiOperation(value = "후기 작성 채팅방 오픈", notes = "후기 작성 채팅방 오픈시 필요한 데이터 호출")
    @ApiImplicitParam(
            name = "worryId"
            , value = "걱정PK")
    @GetMapping("/chat/{worryId}")
    public ResponseEntity<WorryChatResponseDto> openWorryChat(@PathVariable Long worryId) {
        WorryChatResponseDto worryChatResponseDto = worryService.worryChatOpen(worryId);
        return ResponseEntity.ok(worryChatResponseDto);
    }

    //후기 작성 채팅방 - 걱정 실현 여부 입력
    @ApiOperation(value = "후기 작성 채팅방 - 실현 여부 입력", notes = "걱정 실현 여부 입력")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId"
                    , value = "유저PK"),
            @ApiImplicitParam(
                    name = "worryId"
                    , value = "걱정PK"),
            @ApiImplicitParam(
                    name = "isRealized"
                    , value = "걱정 실현 여부")
    })
    @PostMapping("/chat/realize")
    public ResponseEntity inputWorryIsRealized(@RequestParam("userId") Long userId, @RequestParam("worryId") Long worryId, @RequestParam("isRealized") boolean isRealized) {
        try {
            WorryCntResponseDto worryCntResponseDto = worryService.worryChatSetRealized(userId, worryId, isRealized);
            Assert.notNull(worryCntResponseDto);
            return ResponseEntity.ok(worryCntResponseDto);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    //후기 작성 채팅방 - 걱정 만료일 수정
    @ApiOperation(value = "후기 작성 채팅방 - 걱정 만료일 수정", notes = "걱정이 끝나지 않았을 때 만료일 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "worryId"
                    , value = "걱정PK"),
            @ApiImplicitParam(
                    name = "expiryDate")
    })
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
    @ApiOperation(value = "걱정 후기 불러오기", notes = "걱정 후기 데이터를 불러온다")
    @ApiImplicitParam(
            name = "worryId"
            , value = "걱정PK")
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
    @ApiOperation(value = "걱정 후기 수정 - 실현 여부 수정", notes = "걱정 실현 여부 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "worryId"
                    , value = "걱정PK"),
            @ApiImplicitParam(
                    name = "isRealized"
                    , value = "걱정 실현 여부")
    })
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
    @ApiOperation(value = "걱정 후기 수정 - 내용 수정", notes = "후기 내용 수정")
    @PatchMapping("/review")
    public ResponseEntity modifyWorryReview(@RequestBody WorryReviewModifyRequestDto worryRequestDto) {
        try {
            worryService.worryReviewModifyText(worryRequestDto.getWorryId(), worryRequestDto.getWorryReview());
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
