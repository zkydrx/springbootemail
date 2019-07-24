package zhihei.springbootemail.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import zhihei.springbootemail.model.EmailAttachmentDto;
import zhihei.springbootemail.model.EmailDto;
import zhihei.springbootemail.model.EmailImageDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Author: zky
 * Date: 2019-07-24
 * Time: 15:21:28
 * Description:
 */
@RestController
public class EmailController
{
    Logger logger = LoggerFactory.getLogger(EmailController.class);
    @Autowired
    JavaMailSender javaMailSender;

    /**
     * 发送普通邮件
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
            helper.setCc(StringUtils.isEmpty(emailAttachmentDto.getCc())? new String[0]:emailAttachmentDto.getCc().split(","));
            helper.setBcc(StringUtils.isEmpty(emailAttachmentDto.getBcc())?new String[0]:emailAttachmentDto.getBcc().split(","));
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
            helper.setCc(StringUtils.isEmpty(imageDto.getCc())? new String[0]:imageDto.getCc().split(","));
            helper.setBcc(StringUtils.isEmpty(imageDto.getBcc())?new String[0]:imageDto.getBcc().split(","));
            helper.setSentDate(new Date());
            helper.setText(imageDto.getContent(),true);
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


}
