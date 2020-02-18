package pl.edu.agh.pierogi.visitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import pl.edu.agh.pierogi.model.Person;

import java.util.List;

@Component
public class MailSenderVisitor implements Visitor {

    @Autowired
    public JavaMailSender emailSender;
    private String subject;
    private String text;

    public MailSenderVisitor() {
        this.subject = "[Pierogi] ";
        this.text = "";
    }

    public void sendMails(List<Person> subscribers, String subject, String text) {
        this.subject = "[Pierogi] " + subject;
        this.text = text;
        subscribers.forEach(this::visit);
    }

    @Override
    public void visit(Person person) {
        try {
            String email = person.getEmail();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(this.subject);
            message.setText(this.text);
            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
