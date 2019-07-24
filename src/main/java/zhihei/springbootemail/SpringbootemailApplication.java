package zhihei.springbootemail;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Import;
import org.springframework.mail.MailSender;

import javax.activation.MimeType;
import javax.mail.internet.MimeMessage;


@SpringBootApplication
public class SpringbootemailApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(SpringbootemailApplication.class, args);
    }

}
