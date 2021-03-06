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
    private final String from;
    private final String bcc;

    public SmtpTlsDispatcher(String configuration, String apiKey) {
        super(configuration, apiKey);
        final var conf = configuration.split(",");
        final var host = conf[0];
        final var port = conf[1];
        user = conf[2];
        pass = conf[3];
        from = conf.length > 4 ? conf[4] : null;
        bcc = conf.length > 5 ? conf [5] : null;

        authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        };

        sessionProperties = new Properties();
        sessionProperties.put("mail.smtp.host", host);
        sessionProperties.put("mail.smtp.port", port);
        sessionProperties.put("mail.smtp.auth", "true");
        sessionProperties.put("mail.smtp.starttls.enable", "true");
        sessionProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
    }

    @Override
    public List<String> validate(DispatchMessage message) {
        return List.empty();
    }

    @Override
    public String dispatch(DispatchMessage message) {

        final var toEmail = message.getPayload().get("to").getAsString();

        final var session = Session.getInstance(sessionProperties, authenticator);

        final var subject = message.getPayload().get("subject").getAsString();

        final var body = message.getPayload().get("body").getAsString();

        var msg = Try.of(() -> new MimeMessage(session))
                .andThenTry(x -> x.setFrom(null != from ? from : user))
                .andThenTry(x -> x.setRecipients(Message.RecipientType.TO, toEmail))
                .andThenTry(x -> {
                    if (bcc != null) {
                        x.addRecipients(Message.RecipientType.BCC, bcc);
                    }
                })
                .andThenTry(x -> x.setSubject(subject, "utf-8"))
                .get();

        var bodyPart = Try.of(MimeBodyPart::new)
                .andThenTry(x -> x.setContent(body, "text/html; charset=utf-8"))
                .get();

        var mp = Try.of(() -> new MimeMultipart(bodyPart))
                .get();


        // If containing content...
        if (!message.getPayload().get("name").isJsonNull() && !message.getPayload().get("type").isJsonNull()
                && !message.getPayload().get("data").isJsonNull()) {
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
