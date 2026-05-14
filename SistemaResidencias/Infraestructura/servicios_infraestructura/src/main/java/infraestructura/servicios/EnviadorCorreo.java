package infraestructura.servicios;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EnviadorCorreo {

    private final String smtpHost;
    private final int smtpPort;
    private final String usuario;
    private final String contrasena;

    public EnviadorCorreo(String smtpHost, int smtpPort, String usuario, String contrasena) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public void enviarConAdjunto(String destinatario, String asunto, String cuerpo, File adjunto) throws MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, contrasena);
            }
        });

        Message mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(usuario));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject(asunto);

        MimeBodyPart textoPart = new MimeBodyPart();
        textoPart.setText(cuerpo);

        MimeBodyPart adjuntoPart = new MimeBodyPart();
        adjuntoPart.attachFile(adjunto);
        adjuntoPart.setFileName("referencia_pago.pdf");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textoPart);
        multipart.addBodyPart(adjuntoPart);

        mensaje.setContent(multipart);
        Transport.send(mensaje);
    }
}
