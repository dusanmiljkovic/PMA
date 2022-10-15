package com.example.email.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.email.database.MailDatabase;
import com.example.email.entities.Account;

import java.util.Objects;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class MailService {
    private final Properties properties = System.getProperties();
    private final MailDatabase db;
    private Account account;

    public MailService(Context context){
        db = MailDatabase.getDbInstance(context);
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.port", "993");
        properties.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imaps.socketFactory.fallback", "false");
        account = db.accountDao().getLast();
    }

    public void deleteMail(int mailId) {
        try {
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", account.username, account.password);

            javax.mail.Folder[] emailFolders = store.getDefaultFolder().list("*");
            for (javax.mail.Folder folder : emailFolders) {
                if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                    folder.open(Folder.READ_WRITE);
                    Message msg = folder.getMessage(mailId);
                    msg.setFlag(Flags.Flag.DELETED, true);
                    folder.close(true);
                    com.example.email.entities.Message message = db.messageDao().findByMessageNumber(mailId);
                    db.messageDao().delete(message);
                }
            }
        } catch (MessagingException e) {
            System.out.println("An error occurred while deleting an email.");
            e.printStackTrace();

        }
    }

    public void createFolder(String folderName) {
        try {
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", account.username, account.password);

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

            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", account.username, account.password);

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
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", account.username, account.password);

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
}
