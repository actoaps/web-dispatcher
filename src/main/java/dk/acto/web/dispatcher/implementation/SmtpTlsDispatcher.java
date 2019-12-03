package dk.acto.web.dispatcher.implementation;

import dk.acto.web.DispatchMessage;
import dk.acto.web.dispatcher.AbstractDispatcher;
import io.vavr.collection.List;
import io.vavr.control.Try;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Base64;
import java.util.Properties;

public class SmtpTlsDispatcher extends AbstractDispatcher {
    private final Properties sessionProperties;
    private final Authenticator authenticator;
    private final String user;
    private final String pass;

    public SmtpTlsDispatcher(String configuration, String apiKey) {
        super(configuration, apiKey);
        final var conf = configuration.split(",");
        final var host = conf[0];
        final var port = conf[1];
        user = conf[2];
        pass = conf[3];

        System.out.println("Authenticating with username: " + user + " and pass: " + pass);
        authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        };

        sessionProperties = new Properties();
        System.out.println("Setting host: " + host);
        sessionProperties.put("mail.smtp.host", host);
        System.out.println("Setting port: " + port);
        sessionProperties.put("mail.smtp.port", port);
        System.out.println("Setting auth: true");
        sessionProperties.put("mail.smtp.auth", "true");
        System.out.println("Setting starttls.enable: true");
        sessionProperties.put("mail.smtp.starttls.enable", "true");
    }

    @Override
    public List<String> validate(DispatchMessage message) {
        return List.empty();
    }

    @Override
    public String dispatch(DispatchMessage message) {

        System.out.println("TLSEmail Start");

        final var toEmail = message.getPayload().get("to").getAsString();
        System.out.println("toEmail: "+ toEmail);

        final var session = Session.getInstance(sessionProperties, authenticator);

        final var subject = message.getPayload().get("subject").getAsString();
        System.out.println("subject: "+ subject);

        final var body = message.getPayload().get("body").getAsString();
        System.out.println("body: "+ body);

        var msg = Try.of(() -> new MimeMessage(session))
                .andThenTry(x -> x.setFrom(user))
                .andThenTry(x -> x.setRecipients(Message.RecipientType.TO, toEmail))
                .andThenTry(x -> x.setSubject(subject, "utf-8"))
                .get();

        var bodyPart = Try.of(MimeBodyPart::new)
                .andThenTry(x -> x.setContent(body, "text/html; charset=utf-8"))
                .get();

        var mp = Try.of(() -> new MimeMultipart(bodyPart))
                .get();


        // If containing content...
        if (message.getPayload().get("name") != null && message.getPayload().get("type") != null && message.getPayload().get("data") != null) {
            System.out.println("The message contains some content");
            final var aName = message.getPayload().get("name").getAsString();
            final var aType = message.getPayload().get("type").getAsString();
            final var aData = Base64.getDecoder().decode(message.getPayload().get("data").getAsString());
            final ByteArrayDataSource bads = new ByteArrayDataSource(aData, aType);

            final var newBodyPart = Try.of(MimeBodyPart::new)
                    .andThenTry(x -> x.setDataHandler(new DataHandler(bads)))
                    .andThenTry(x -> x.setFileName(aName))
                    .get();

            try {
                mp.addBodyPart(newBodyPart);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

        try {
            msg.setContent(mp);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return Try.run(() -> Transport.send(msg))
                .map(x -> "Ok")
                .onFailure(Throwable::printStackTrace)
                .getOrElse("Error");
    }
}
