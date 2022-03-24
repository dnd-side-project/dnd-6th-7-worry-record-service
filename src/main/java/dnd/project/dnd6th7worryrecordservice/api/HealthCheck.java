package dnd.project.dnd6th7worryrecordservice.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @ApiOperation(value = "index")
    @GetMapping("/")
    public String index() {
        return "ok";
    }

    @ApiOperation(value = "Ping Pong")
    @GetMapping("/ping")
    public String healthCheck() {
        return "pong";
    }
}
