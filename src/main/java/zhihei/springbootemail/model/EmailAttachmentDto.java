package zhihei.springbootemail.model;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Author: zky
 * Date: 2019-07-24
 * Time: 16:04:00
 * Description:
 */
@Data
public class EmailAttachmentDto
{
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件发送者
     */
    private String from;
    /**
     * 邮件接收者
     */
    private String to;
    /**
     * 邮件抄送人
     */
    private String cc;
    /**
     * 隐秘抄送人
     */
    private String bcc;
    /**
     * 邮件的正文
     */
    private String content;

    /**
     * 附件地址
     */
    private String attachmentPath;
}
