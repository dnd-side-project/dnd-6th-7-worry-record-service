package dnd.project.dnd6th7worryrecordservice.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
    @GetMapping("/")
    public String healthCheck() {
        return "HealthCheck OK";
    }
}
