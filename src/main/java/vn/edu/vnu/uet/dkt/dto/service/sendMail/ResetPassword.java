package vn.edu.vnu.uet.dkt.dto.service.sendMail;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.edu.vnu.uet.dkt.dto.dao.student.StudentDao;
import vn.edu.vnu.uet.dkt.dto.model.Student;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class ResetPassword extends Thread{
    private final int LENGTH_PASSWORD = 10;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void resetPassword(String email) throws MessagingException, javax.mail.MessagingException {
        Student student = studentDao.getByEmail(email);
        if (student == null) return;
        String password = generateRandomPassword(LENGTH_PASSWORD);
        String passwordEncode = passwordEncoder.encode(password);
        student.setPassword(passwordEncode);
        studentDao.save(student);
        sendResetPassword(email, password,student.getFullName());
    }

    public void sendResetPassword(String to, String password, String name) throws MessagingException, javax.mail.MessagingException {
        String subject = "Reset account dkt";
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        String content = getContent(password,name);
        message.setContent(content,"text/html");
        Thread job = new Thread(() -> emailSender.send(message));
        job.start();
    }

    private String getContent(String password, String name) {
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<title>Đặt lại tài khoản hệ thống Đăng Ký Thi</title>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"+
                "</head>\n" +
                "<body>\n" +
                "\t<p>Dear <b>" + name +"</b>,</p>\n" +
                "<p>Mật khẩu của bạn đã được reset. Mật khẩu mới là:<b>" + password + "</b></p>" +
                "</body>\n" +
                "</html>";
        return content;
    }

    public String generateRandomPassword(int len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return RandomStringUtils.random(len, chars);
    }
}
