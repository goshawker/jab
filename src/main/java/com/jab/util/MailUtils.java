package com.jab.util;

import sun.misc.BASE64Encoder;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
*
* @Author: goshawker@yeah.net
* @Description:
* @Date: 2022/9/12 11:00
* @Version: 1.0
*/
public class MailUtils {

  /**
   * The sendhost.
   */
  private String __sendhost = null;

  /**
   * The recvhost.
   */
  private String __recvhost = null;

  /**
   * The username.
   */
  private String __username = null;

  /**
   * The password.
   */
  private String __password = null;

  /**
   * The attach dir.
   */
  private String __attachDir = null;

  /**
   * Instantiates a new mail utils.
   *
   * @param sendhost  the sendhost
   * @param recvhost  the recvhost
   * @param attachDir the attach dir
   * @param username  the username
   * @param password  the password
   */
  public MailUtils(String sendhost, String recvhost, String attachDir,
                   String username, String password) {
    this.__sendhost = sendhost;
    this.__recvhost = recvhost;
    this.__attachDir = attachDir;
    this.__username = username;
    this.__password = password;
  }

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {

  }

  /**
   * Gets the sendhost.
   *
   * @return the sendhost
   */
  public String getSendhost() {
    return this.__sendhost;
  }

  /**
   * Gets the password.
   *
   * @return the password
   */
  public String getPassword() {
    return this.__password;
  }

  /**
   * Gets the username.
   *
   * @return the username
   */
  public String getUsername() {
    return this.__username;
  }

  /**
   * Send.
   *
   * @param mail the mail
   * @throws Exception the exception
   */
  public void send(Mail mail) throws Exception {
    String fromAddr = mail.getFromAddr();
    String toAddr = mail.getToAddr();
    String cc = mail.getCc();
    String bcc = mail.getBcc();
    String title = mail.getTitle();
    String content = mail.getContent();
    Set attachNames = mail.getAttachNames();

    Properties props = new Properties();
    props.put("mail.smtp.host", getSendhost());
    props.put("mail.smtp.auth", "true");
    Session session = Session.getDefaultInstance(props);
    session.setDebug(true);
    MimeMessage message = new MimeMessage(session);
    message.setFrom(new InternetAddress(fromAddr));

    message.addRecipients(Message.RecipientType.TO,
            buildInterAddres(toAddr));

    if (null != cc) {
      message.addRecipients(Message.RecipientType.CC,
              buildInterAddres(cc));
    }

    if (null != bcc) {
      message.addRecipients(Message.RecipientType.BCC,
              buildInterAddres(bcc));
    }

    message.setSubject(title);
    Multipart multipart = new MimeMultipart();
    BodyPart contentPart = new MimeBodyPart();
    contentPart.setText(content);
    multipart.addBodyPart(contentPart);
    Iterator itr = attachNames.iterator();
    while (itr.hasNext()) {
      String attachName = (String) itr.next();
      String attachPath = mail.getAttachPath(attachName);
      BodyPart attachmentPart = new MimeBodyPart();
      DataSource source = new FileDataSource(attachPath);
      attachmentPart.setDataHandler(new DataHandler(source));
      BASE64Encoder enc = new BASE64Encoder();
      attachmentPart.setFileName("=?GBK?B?" + enc.encode(attachName.getBytes()) + "?=");

      multipart.addBodyPart(attachmentPart);
    }
    message.setContent(multipart);
    message.saveChanges();
    Transport transport = session.getTransport("smtp");
    transport.connect(this.__sendhost, getUsername(), getPassword());
    transport.sendMessage(message, message.getAllRecipients());
    transport.close();
  }

  /**
   * Builds the inter addres.
   *
   * @param addr the addr
   * @return the internet address[]
   */
  private InternetAddress[] buildInterAddres(String addr) {
    String[] adds = addr.split(",");
    InternetAddress[] address = new InternetAddress[adds.length];
    for (int num = 0; num < adds.length; num++) {
      address[num] = new InternetAddress();
      address[num].setAddress(adds[num]);
    }
    return address;
  }

  /**
   * Receive.
   *
   * @return the list
   * @throws Exception the exception
   */
  public List receive() throws Exception {
    List mailList = new ArrayList();
    Properties props = System.getProperties();
    Session session = Session.getDefaultInstance(props, null);

    Store store = session.getStore("pop3");
    store.connect(getRecvhost(), getUsername(), getPassword());

    Folder folder = store.getDefaultFolder();
    if (folder != null) {
      folder = folder.getFolder("INBOX");
      if (folder != null) {
        folder.open(1);
        Message[] msgs = folder.getMessages();
        for (int msgNum = 0; msgNum < msgs.length; msgNum++) {
          MimeMessage message = (MimeMessage) msgs[msgNum];
          Mail mail = new Mail();
          setFromAddr(message, mail);
          setMailAddress(message, mail);
          setTitle(message, mail);
          setMailContent(message, mail);
          setAttachMent(message, mail);
          mailList.add(mail);
        }
      }
    }
    if (folder != null)
      folder.close(false);
    if (store != null)
      store.close();
    return mailList;
  }

  /**
   * Sets the from addr.
   *
   * @param message the message
   * @param mail    the mail
   * @throws Exception the exception
   */
  private void setFromAddr(MimeMessage message, Mail mail) throws Exception {
    InternetAddress[] address = (InternetAddress[]) message.getFrom();
    String from = address[0].getAddress();
    if (from == null)
      from = "";
    String personal = address[0].getPersonal();
    if (personal == null)
      personal = "";
    mail.setFromAddr(personal + "<" + from + ">");
  }

  /**
   * Sets the mail address.
   *
   * @param message the message
   * @param mail    the mail
   * @throws Exception the exception
   */
  private void setMailAddress(MimeMessage message, Mail mail)
          throws Exception {
    InternetAddress[] to = (InternetAddress[]) message
            .getRecipients(Message.RecipientType.TO);

    mail.setToAddr(buildAddr(to));

    InternetAddress[] cc = (InternetAddress[]) message
            .getRecipients(Message.RecipientType.CC);

    mail.setCc(buildAddr(cc));
  }

  /**
   * Builds the addr.
   *
   * @param address the address
   * @return the string
   * @throws Exception the exception
   */
  private String buildAddr(InternetAddress[] address) throws Exception {
    String mailaddr = "";
    if (address != null) {
      for (int i = 0; i < address.length; i++) {
        String email = address[i].getAddress();
        if (email == null)
          email = "";
        else {
          email = MimeUtility.decodeText(email);
        }
        String personal = address[i].getPersonal();
        if (personal == null)
          personal = "";
        else {
          personal = MimeUtility.decodeText(personal);
        }
        String compositeto = personal + "<" + email + ">";
        mailaddr = mailaddr + "," + compositeto;
      }
      mailaddr = mailaddr.substring(1);
    }
    return mailaddr;
  }

  /**
   * Sets the title.
   *
   * @param message the message
   * @param mail    the mail
   * @throws Exception the exception
   */
  private void setTitle(MimeMessage message, Mail mail) throws Exception {
    String subject = "";
    subject = MimeUtility.decodeText(message.getSubject());
    if (subject == null)
      subject = "";
    mail.setTitle(subject);
  }

  /**
   * Sets the mail content.
   *
   * @param part the part
   * @param mail the mail
   * @throws Exception the exception
   */
  private void setMailContent(Part part, Mail mail) throws Exception {
    String contenttype = part.getContentType();
    String content = mail.getContent() != null ? mail.getContent() : "";
    int nameindex = contenttype.indexOf("name");
    boolean conname = nameindex != -1;
    if ((part.isMimeType("text/plain")) && (!conname)) {
      content = content + part.getContent();
      mail.setContent(content);
    } else if ((part.isMimeType("text/html")) && (!conname)) {
      content = content + part.getContent();
      mail.setContent(content);
    } else if (part.isMimeType("multipart/*")) {
      Multipart multipart = (Multipart) part.getContent();
      int counts = multipart.getCount();
      for (int i = 0; i < counts; i++) {
        setMailContent(multipart.getBodyPart(i), mail);
      }
    } else if (part.isMimeType("message/rfc822")) {
      setMailContent((Part) part.getContent(), mail);
    }
  }

  /**
   * Sets the attach ment.
   *
   * @param part the part
   * @param mail the mail
   * @throws Exception the exception
   */
  private void setAttachMent(Part part, Mail mail) throws Exception {
    if (part.isMimeType("multipart/*")) {
      Multipart __mp = (Multipart) part.getContent();
      String fileName = "";
      for (int i = 0; i < __mp.getCount(); i++) {
        BodyPart mpart = __mp.getBodyPart(i);
        String disposition = mpart.getDisposition();
        if ((disposition == null)
                || ((!disposition.equals("attachment")) && (!disposition
                .equals("inline")))) {
          continue;
        }
        fileName = mpart.getFileName();
        if ((fileName != null) && (fileName.endsWith("?="))) {
          fileName = MimeUtility.decodeWord(fileName);
        }

        File pathFile = new File(getAttachDir());
        if (!pathFile.exists()) {
          pathFile.mkdir();
        }
        String fileAddr = getAttachDir() + File.separator + fileName;

        mail.addAttachment(fileName, fileAddr);
        File storefile = new File(fileAddr);
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        bos = new BufferedOutputStream(new FileOutputStream(storefile));

        bis = new BufferedInputStream(mpart.getInputStream());
        int c;
        while ((c = bis.read()) != -1) {
          bos.write(c);
          bos.flush();
        }
        bos.close();
        bis.close();
      }
    }
  }

  /**
   * Gets the recvhost.
   *
   * @return the recvhost
   */
  public String getRecvhost() {
    return this.__recvhost;
  }

  /**
   * Gets the attach dir.
   *
   * @return the attach dir
   */
  public String getAttachDir() {
    return this.__attachDir;
  }
}