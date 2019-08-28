package zhihei.springbootemail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SpringbootemailApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(SpringbootemailApplication.class, args);
    }

}
