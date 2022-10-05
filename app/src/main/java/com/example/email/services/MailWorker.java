package com.example.email.services;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.email.database.MailDatabase;
import com.example.email.entities.Folder;
import com.example.email.utils.Constants;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class MailWorker extends Worker {
    private final Properties properties = System.getProperties();
    private final Session imapSession = Session.getInstance(properties);
    private MailDatabase db;

    public MailWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);

        db = MailDatabase.getDbInstance(context);
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
            for (javax.mail.Folder folder : emailFolders) {

                Folder folderToAdd = new Folder();
                folderToAdd.name = folder.getName();
                folderToAdd.fullName = folder.getFullName();
                if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0)
                    if (db.folderDao().findByName(folderToAdd.name) == null)
                        db.folderDao().insertAll(folderToAdd);

            }
            System.out.println("\n\nDONE\n\n");

        }
        catch (MessagingException e) {
            System.out.println("\n\nAn error occurred while getting folders.\n\n");
            e.printStackTrace();
        }
    }
}
