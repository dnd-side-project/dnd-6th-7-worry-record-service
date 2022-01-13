package dnd.project.dnd6th7worryrecordservice.controller;

import dnd.project.dnd6th7worryrecordservice.aws.S3Uploader;
import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("user")
@RestController
public class UserController {
    private final UserServiceImpl userService;
    private final S3Uploader s3Uploader;


    @PostMapping("register")
    public void addUser(@ModelAttribute UserRequestDto userRequestDto) throws IOException {
        String imgUrl = s3Uploader.uploadFile(userRequestDto.getImg(), "userImage");
        System.out.println("imgUrl = " + imgUrl);
        if (imgUrl == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "업로드된 파일이 없거나 잘못된 파일입니다.");
        } else {
            userService.join(userRequestDto, imgUrl);
            throw new ResponseStatusException(HttpStatus.OK, "파일 업로드 완료.");
        }
    }

}
