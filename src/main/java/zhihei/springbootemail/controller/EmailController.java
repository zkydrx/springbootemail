package zhihei.springbootemail.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import zhihei.springbootemail.model.Car;
import zhihei.springbootemail.model.EmailAttachmentDto;
import zhihei.springbootemail.model.EmailDto;
import zhihei.springbootemail.model.EmailFreemarkerDto;
import zhihei.springbootemail.model.EmailImageDto;
import zhihei.springbootemail.model.EmailThreepartHtmlDto;
import zhihei.springbootemail.model.EmailThymeleafDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Author: zky
 * Date: 2019-07-24
 * Time: 15:21:28
 * Description:
 */
@RestController
@Component
public class EmailController
{
    Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    /**
     * 发送普通邮件
     *
     * @param email
     * @return
     */
    @PostMapping(value = "/sendEmail")
    public JSONObject simpleSender(@RequestBody EmailDto email)
    {
        JSONObject jsonObject = new JSONObject(true);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(email.getSubject());
        simpleMailMessage.setFrom(email.getFrom());
        simpleMailMessage.setTo(email.getTo().split(","));
        simpleMailMessage.setCc(email.getCc().split(","));
        simpleMailMessage.setBcc(email.getBcc().split(","));
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setText(email.getContent());
        javaMailSender.send(simpleMailMessage);
        jsonObject.put("code", "200");
        jsonObject.put("desc", "成功");
        jsonObject.put("data", JSON.toJSON(simpleMailMessage));
        return jsonObject;
    }


    /**
     * 发送附件邮件
     *
     * @param emailAttachmentDto
     * @return
     */
    @PostMapping(value = "/attachmentEmail")
    public JSONObject attachmentSender(@RequestBody EmailAttachmentDto emailAttachmentDto)
    {
        JSONObject jsonObject = new JSONObject(true);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try
        {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(emailAttachmentDto.getSubject());
            helper.setFrom(emailAttachmentDto.getFrom());
            helper.setTo(emailAttachmentDto.getTo().split(","));
            helper.setCc(StringUtils.isEmpty(emailAttachmentDto.getCc()) ? new String[0] : emailAttachmentDto.getCc().split(","));
            helper.setBcc(StringUtils.isEmpty(emailAttachmentDto.getBcc()) ? new String[0] : emailAttachmentDto.getBcc().split(","));
            helper.setSentDate(new Date());
            helper.setText(emailAttachmentDto.getContent());
            helper.addAttachment("1.png", new File(emailAttachmentDto.getAttachmentPath().split(",")[0]));
            javaMailSender.send(mimeMessage);
            jsonObject.put("code", "200");
            jsonObject.put("desc", "成功");
            jsonObject.put("data", JSON.toJSON(emailAttachmentDto));
        }
        catch (MessagingException e)
        {
            logger.info("发送异常:", e);
            jsonObject.put("code", "404");
            jsonObject.put("desc", "发送失败");
            jsonObject.put("data", JSON.toJSON(emailAttachmentDto));
        }
        return jsonObject;
    }


    /**
     * 发送图片邮件
     *
     * @param imageDto
     * @return
     */
    @PostMapping(value = "/imageEmail")
    public JSONObject imageSender(@RequestBody EmailImageDto imageDto)
    {
        JSONObject jsonObject = new JSONObject(true);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try
        {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(imageDto.getSubject());
            helper.setFrom(imageDto.getFrom());
            helper.setTo(imageDto.getTo().split(","));
            helper.setCc(StringUtils.isEmpty(imageDto.getCc()) ? new String[0] : imageDto.getCc().split(","));
            helper.setBcc(StringUtils.isEmpty(imageDto.getBcc()) ? new String[0] : imageDto.getBcc().split(","));
            helper.setSentDate(new Date());
            helper.setText(imageDto.getContent(), true);
            helper.addInline("image1", new FileSystemResource(new File(imageDto.getAttachmentPath().split(",")[0])));
            helper.addInline("image2", new FileSystemResource(new File(imageDto.getAttachmentPath().split(",")[1])));
            helper.addInline("image3", new FileSystemResource(new File(imageDto.getAttachmentPath().split(",")[2])));
            javaMailSender.send(mimeMessage);
            jsonObject.put("code", "200");
            jsonObject.put("desc", "成功");
            jsonObject.put("data", JSON.toJSON(imageDto));
        }
        catch (MessagingException e)
        {
            logger.info("发送异常:", e);
            jsonObject.put("code", "404");
            jsonObject.put("desc", "发送失败");
            jsonObject.put("data", JSON.toJSON(imageDto));
        }
        return jsonObject;
    }


    /**
     * 使用 Freemarker 作邮件模板
     *
     * @param emailFreemarkerDto
     * @return
     */
    @PostMapping(value = "/freeMarkerEmail")
    public JSONObject freeMarkerSender(@RequestBody EmailFreemarkerDto emailFreemarkerDto)
    {
        JSONObject jsonObject = new JSONObject(true);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try
        {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(emailFreemarkerDto.getSubject());
            helper.setFrom(emailFreemarkerDto.getFrom());
            helper.setTo(emailFreemarkerDto.getTo().split(","));
            helper.setCc(StringUtils.isEmpty(emailFreemarkerDto.getCc()) ? new String[0] : emailFreemarkerDto.getCc().split(","));
            helper.setBcc(StringUtils.isEmpty(emailFreemarkerDto.getBcc()) ? new String[0] : emailFreemarkerDto.getBcc().split(","));
            helper.setSentDate(new Date());
            /**
             * 构建Freemarker 的基本配置
             */
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
            /**
             * 配置模板的位置
             */
            ClassLoader classLoader = getClass().getClassLoader();
            configuration.setClassLoaderForTemplateLoading(classLoader, "templates");
            /**
             * 加载模板
             */
            Template template = configuration.getTemplate("mail.ftl");
            Car car = new Car();
            car.setBrand("Audi");
            car.setName("奥迪");
            car.setPrice("20000$");
            StringWriter stringWriter = new StringWriter();
            /**
             * 模板渲染，渲染的结果将被保存到 stringWriter中 ，将stringWriter中的 html字符串发送即可
             */
            template.process(car, stringWriter);
            helper.setText(stringWriter.toString(), true);
            javaMailSender.send(mimeMessage);
            jsonObject.put("code", "200");
            jsonObject.put("desc", "成功");
            jsonObject.put("data", JSON.toJSON(emailFreemarkerDto));
        }
        catch (MessagingException e)
        {
            logger.info("发送异常:", e);
            jsonObject.put("code", "404");
            jsonObject.put("desc", "发送失败");
            jsonObject.put("data", JSON.toJSON(emailFreemarkerDto));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        catch (MalformedTemplateNameException e)
        {
            e.printStackTrace();
        }
        catch (TemplateNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (TemplateException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 使用 Thymeleaf 作邮件模板
     *
     * @param emailThymeleafDto
     * @return
     */
    @PostMapping(value = "/thymeleafEmail")
    public JSONObject thymeleafSender(@RequestBody EmailThymeleafDto emailThymeleafDto)
    {
        JSONObject jsonObject = new JSONObject(true);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try
        {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(emailThymeleafDto.getSubject());
            helper.setFrom(emailThymeleafDto.getFrom());
            helper.setTo(emailThymeleafDto.getTo().split(","));
            helper.setCc(StringUtils.isEmpty(emailThymeleafDto.getCc()) ? new String[0] : emailThymeleafDto.getCc().split(","));
            helper.setBcc(StringUtils.isEmpty(emailThymeleafDto.getBcc()) ? new String[0] : emailThymeleafDto.getBcc().split(","));
            helper.setSentDate(new Date());

            Context context = new Context();
            context.setVariable("brand", "Benz");
            context.setVariable("price", "30000$");
            context.setVariable("name", "奔驰");
            String process = templateEngine.process("mail.html", context);
            helper.setText(process, true);
            javaMailSender.send(mimeMessage);
            jsonObject.put("code", "200");
            jsonObject.put("desc", "成功");
            jsonObject.put("data", JSON.toJSON(emailThymeleafDto));
        }
        catch (MessagingException e)
        {
            logger.info("发送异常:", e);
            jsonObject.put("code", "404");
            jsonObject.put("desc", "发送失败");
            jsonObject.put("data", JSON.toJSON(emailThymeleafDto));
        }

        return jsonObject;
    }


    /**
     * 使用 Threepart html 发送邮件
     *
     * @param emailThreepartHtmlDto
     * @return
     */
    @PostMapping(value = "/oneHtmlEmail")
    public JSONObject oneHtmlSender(@RequestBody EmailThreepartHtmlDto emailThreepartHtmlDto)
    {
        JSONObject jsonObject = new JSONObject(true);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try
        {
            helper = new MimeMessageHelper(mimeMessage, true);
            helper.setSubject(emailThreepartHtmlDto.getSubject());
            helper.setFrom(emailThreepartHtmlDto.getFrom());
            helper.setTo(emailThreepartHtmlDto.getTo().split(","));
            helper.setCc(StringUtils.isEmpty(emailThreepartHtmlDto.getCc()) ? new String[0] : emailThreepartHtmlDto.getCc().split(","));
            helper.setBcc(StringUtils.isEmpty(emailThreepartHtmlDto.getBcc()) ? new String[0] : emailThreepartHtmlDto.getBcc().split(","));
            helper.setSentDate(new Date());
            JSONObject oneHtml = getOneHtml();
            String imgUrl = oneHtml.getString("img_url");
            String word = oneHtml.getString("word");
            Context context = new Context();
            context.setVariable("imgUrl", imgUrl);
            context.setVariable("word", word);
            String process = templateEngine.process("one.html", context);
            helper.setText(process, true);
            javaMailSender.send(mimeMessage);
            jsonObject.put("code", "200");
            jsonObject.put("desc", "成功");
            jsonObject.put("data", JSON.toJSON(emailThreepartHtmlDto));
        }
        catch (MessagingException e)
        {
            logger.info("发送异常:", e);
            jsonObject.put("code", "404");
            jsonObject.put("desc", "发送失败");
            jsonObject.put("data", JSON.toJSON(emailThreepartHtmlDto));
        }
        return jsonObject;
    }

    /**
     * 获取one 的html
     * @return
     */
    public JSONObject getOneHtml()
    {
        RestTemplate restTemplate = restTemplateBuilder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TransCode","030111");
        jsonObject.put("OpenId","123456789");
        HttpEntity<JSONObject> entity = new HttpEntity<>(jsonObject,headers);
        String body = restTemplate.exchange("https://api.hibai.cn/api/index/index", HttpMethod.POST, entity, String.class).getBody();
        logger.info("body:{}",body);
        JSONObject respJson = JSONObject.parseObject(body);
        JSONObject body1 = respJson.getJSONObject("Body");
        return body1;
    }


}
