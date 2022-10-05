package com.example.email.services;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.email.utils.Constants;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class MailWorker extends Worker {
    private final Properties properties = System.getProperties();
    private final Session imapSession = Session.getInstance(properties);

    public MailWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        initialise();
    }

    @Override
    public Result doWork() {

        getFolders();

        return Result.success();
    }

    private void initialise(){
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.port", "993");
        properties.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imaps.socketFactory.fallback", "false");
    }

    public void getFolders(){
        try {
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", Constants.EMAIL, Constants.PASSWORD);
            javax.mail.Folder[] emailFolders = store.getDefaultFolder().list("*");
            long i = 1;
            for (javax.mail.Folder folder : emailFolders) {
                if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
//                    folders.add(new Folder(i, folder.getFullName(), folder.getMessageCount()));
                    i++;
                }
            }
            System.out.println("\n\nDONE\n\n");

        }
        catch (MessagingException e) {
            System.out.println("\n\nAn error occurred while getting folders.\n\n");
            e.printStackTrace();
        }
    }
}
