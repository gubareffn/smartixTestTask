package app.smartix_test_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartixTestTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartixTestTaskApplication.class, args);
    }

}
