package vn.edu.vnu.uet.dkt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DktApplication {

    public static void main(String[] args) {
        SpringApplication.run(DktApplication.class, args);
    }

}
