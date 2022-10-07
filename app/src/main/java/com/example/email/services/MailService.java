package com.example.email.services;

import android.content.Context;

import com.example.email.database.MailDatabase;
import com.example.email.entities.Account;

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
            System.out.println("An error occurred while getting folders.");
            e.printStackTrace();

        }
    }
}
