package com.example.email.services;

import android.content.Context;

import com.example.email.database.MailDatabase;
import com.example.email.entities.Account;

import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {
    private final Properties properties = System.getProperties();
    private final MailDatabase db;
    private Account account;
    private Session session;
    private Store store;


    public MailService(Context context){
        db = MailDatabase.getDbInstance(context);
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.port", "993");
        properties.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imaps.socketFactory.fallback", "false");
        account = db.accountDao().getLast();
        connect();
    }

    private void connect(){
        try {
            if (store == null) {
                session = Session.getDefaultInstance(properties, null);
                store = session.getStore("imaps");
                store.connect("imap.gmail.com", account.username, account.password);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void deleteMail(int mailId) {
        try {
            if (!store.isConnected())
            {
                session = Session.getDefaultInstance(properties, null);
                store = session.getStore("imaps");
                store.connect("imap.gmail.com", account.username, account.password);
            }
            com.example.email.entities.Message message = db.messageDao().findByMessageNumber(mailId);
            com.example.email.entities.Folder messageFolder = db.folderDao().findById(message.folderId);
            javax.mail.Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            try {
                Message msg = folder.getMessage(mailId);
                if (msg != null) {
                    msg.setFlag(Flags.Flag.DELETED, true);
                    folder.close(true);
                }
            }catch (Exception ignored){
            }
            db.messageDao().delete(message);
        } catch (MessagingException e) {
            System.out.println("An error occurred while deleting an email.");
            e.printStackTrace();
        }
    }

    public void createFolder(String folderName) {
        try {
            if (!store.isConnected())
            {
                session = Session.getDefaultInstance(properties, null);
                store = session.getStore("imaps");
                store.connect("imap.gmail.com", account.username, account.password);
            }
            Folder someFolder = store.getFolder(folderName);
            if (!someFolder.exists()) {
                if (someFolder.create(Folder.HOLDS_MESSAGES)) {
                    someFolder.setSubscribed(true);
                    System.out.println("Folder was created successfully");

                    com.example.email.entities.Folder folder = new com.example.email.entities.Folder();
                    folder.name = folderName;
                    folder.fullName = folderName;
                    db.folderDao().insertAll(folder);
                }
            }
        } catch (MessagingException e) {
            System.out.println("An error occurred while creating a folder.");
            e.printStackTrace();
        }
    }

    public void updateFolder(int folderId, String folderName, String oldFolderName) {
        try {
            if(Objects.equals(folderName, oldFolderName))
                return;

            if (!store.isConnected())
            {
                session = Session.getDefaultInstance(properties, null);
                store = session.getStore("imaps");
                store.connect("imap.gmail.com", account.username, account.password);
            }
            Folder someFolder = store.getFolder(oldFolderName);
            if (someFolder.exists()) {
                Folder newFolder = store.getFolder(folderName);
                someFolder.renameTo(newFolder);

                com.example.email.entities.Folder folder = db.folderDao().findById(folderId);
                folder.name = folderName;
                folder.fullName = folderName;
                db.folderDao().updateFolder(folder);
            }
        } catch (MessagingException e) {
            System.out.println("An error occurred while updating a folder.");
            e.printStackTrace();
        }
    }

    public void deleteFolder(int folderId) {
        try {
            if (!store.isConnected())
            {
                session = Session.getDefaultInstance(properties, null);
                store = session.getStore("imaps");
                store.connect("imap.gmail.com", account.username, account.password);
            }
            com.example.email.entities.Folder folder = db.folderDao().findById(folderId);
            Folder someFolder = store.getFolder(folder.name);
            if (someFolder.exists()) {
                someFolder.delete(true);
            }
            db.folderDao().delete(folder);
        } catch (MessagingException e) {
            System.out.println("An error occurred while updating a folder.");
            e.printStackTrace();
        }
    }

    public void sendMail(com.example.email.entities.Message message) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account.username, account.password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(account.username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.to));
//            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(message.cc));
//            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(message.cc));
            msg.setSubject(message.subject);
            msg.setText(message.content);
            Transport.send(msg);
            Folder[] folders = store.getDefaultFolder().list("*");
            for (Folder folder: folders) {
                if (Objects.equals(folder.getName(), "Sent Mail")){
                    com.example.email.entities.Folder folderInDb = db.folderDao().findByName("Sent Mail");
                    message.folderId = folderInDb.id;
                    db.messageDao().insertAll(message);
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void saveMessage(com.example.email.entities.Message message) {
        try {
            if (!store.isConnected())
            {
                session = Session.getDefaultInstance(properties, null);
                store = session.getStore("imaps");
                store.connect("imap.gmail.com", account.username, account.password);
            }
            Folder[] folders = store.getDefaultFolder().list("*");
            for (Folder folder: folders) {
                if (Objects.equals(folder.getName(), "Drafts")){
                    folder.open(Folder.READ_WRITE);
                    MimeMessage draftMessage = new MimeMessage(session);
                    draftMessage.setFrom(new InternetAddress(account.username));
                    if (message.to != "")
                        draftMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.to));
                    if (message.subject != "")
                        draftMessage.setSubject(message.subject);
                    if (message.content != "")
                        draftMessage.setText(message.content);
                    draftMessage.setFlag(Flags.Flag.DRAFT, true);
                    MimeMessage []draftMessages = {draftMessage};
                    folder.appendMessages(draftMessages);

                    com.example.email.entities.Folder folderInDb = db.folderDao().findByName("Drafts");
                    message.folderId = folderInDb.id;

                    db.messageDao().insertAll(message);
                }
            }
        }catch (MessagingException e){
            System.out.println("An error occurred while saving a mail to drafts.");
            e.printStackTrace();
        }
    }
}
