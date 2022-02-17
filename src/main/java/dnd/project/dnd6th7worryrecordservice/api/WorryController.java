package dnd.project.dnd6th7worryrecordservice.api;

import dnd.project.dnd6th7worryrecordservice.domain.category.Category;
import dnd.project.dnd6th7worryrecordservice.domain.category.CategoryRepository;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.domain.worry.Worry;
import dnd.project.dnd6th7worryrecordservice.domain.worry.WorryRepository;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserResponseDto;
import dnd.project.dnd6th7worryrecordservice.dto.worry.WorryResponseDto;
import dnd.project.dnd6th7worryrecordservice.service.UserService;
import dnd.project.dnd6th7worryrecordservice.service.WorryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.xml.ws.Response;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/worries")
@RestController
public class WorryController {
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final WorryRepository worryRepository;
    private final WorryService worryService;
    private EntityManager em;


    //걱정 보관함 - 지난 걱정
    @GetMapping("/past")
    public ResponseEntity<List<WorryResponseDto>> allPastWorryList(@RequestParam("userId") Long userId){
        List<WorryResponseDto> worryList = worryService.findByWorryIsFinished(true, userId);
        return ResponseEntity.ok(worryList);
    }

    //걱정 보관함 - 지난 걱정 - 걱정 카테고리 필터링
    @GetMapping("/past/filter")
    public ResponseEntity<List<WorryResponseDto>> CategorizedPastWorry(@RequestParam("userId") Long userId, @RequestParam("categories") List<Long> categoryId){
        List<WorryResponseDto> worryList = worryService.findCategorizedPastWorry(userId, categoryId);
        return ResponseEntity.ok(worryList);
    }

    //걱정 보관함 - 지난 걱정 - 의미 있는 걱정
    @GetMapping("/past/mean")
    public ResponseEntity<List<WorryResponseDto>> meaningfulWorry(@RequestParam("userId") Long userId){
        List<WorryResponseDto> worryList = worryService.findMeanOrMeanlessWorry(userId, true);
        return ResponseEntity.ok(worryList);
    }

    //걱정 보관함 - 지난 걱정 - 의미 없는 걱정
    @GetMapping("/past/meanless")
    public ResponseEntity<List<WorryResponseDto>> meanlessWorry(@RequestParam("userId") Long userId){
        List<WorryResponseDto> worryList = worryService.findMeanOrMeanlessWorry(userId, false);
        return ResponseEntity.ok(worryList);
    }

    @PatchMapping("/{worryId}")
    public ResponseEntity turnLockState(@RequestParam("worryId") Long worryId){
        worryService.turnWorryLockState(worryId);
        return ResponseEntity.ok("");
    }
}
