package dnd.project.dnd6th7worryrecordservice.controller;

import dnd.project.dnd6th7worryrecordservice.aws.S3Uploader;
import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

        userService.join(userRequestDto, imgUrl);
    }

}
