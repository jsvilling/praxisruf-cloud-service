package ch.fhnw.ip5.praxiscloudservice.cloud;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/praxis/hello")
public class HelloPraxisController {

    private static final String HELLO_PRAXIS = "Hello Praxis";

    @GetMapping
    public String sayHelloPraxis() {
        return HELLO_PRAXIS;
    }

}
