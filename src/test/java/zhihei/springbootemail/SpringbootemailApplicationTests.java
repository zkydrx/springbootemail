package zhihei.springbootemail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootemailApplicationTests
{

    Logger logger = LoggerFactory.getLogger(SpringbootemailApplicationTests.class);

    @Test
    public void contextLoads()
    {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity entity = new HttpEntity(headers);
        String body = "";
        body = restTemplate.exchange("http://wufazhuce.com/", HttpMethod.POST, entity, String.class).getBody();
        logger.info("body:{}",body);

    }

}
