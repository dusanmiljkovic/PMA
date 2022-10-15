package com.example.email.services;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.email.database.MailDatabase;
import com.example.email.entities.Account;
import com.example.email.entities.Folder;
import com.example.email.entities.Message;
import com.sun.mail.util.QPDecoderStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

public class MailWorker extends Worker {
    private final Properties properties = System.getProperties();
    private MailDatabase db;
    private Account account;
    private boolean textIsHtml;

    public MailWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);

        initialise(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        getFolders();
        getEmails();
        return Result.success();
    }

    private void initialise(Context context){
        db = MailDatabase.getDbInstance(context);
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.port", "993");
        properties.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imaps.socketFactory.fallback", "false");
        account = db.accountDao().getLast();
    }

    public void getFolders(){
        try {
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", account.username, account.password);

            javax.mail.Folder[] emailFolders = store.getDefaultFolder().list("*");
            for (javax.mail.Folder folder : emailFolders) {
                String folderName = folder.getName();

                if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                    if (db.folderDao().findByName(folderName) == null) {
                        Folder folderToAdd = new Folder();
                        folderToAdd.holds = javax.mail.Folder.HOLDS_MESSAGES;
                        folderToAdd.name = folderName;
                        folderToAdd.fullName = folder.getFullName();
                        db.folderDao().insertAll(folderToAdd);
                    }
                }
            }
        }
        catch (MessagingException e) {
            System.out.println("An error occurred while getting folders.");
            e.printStackTrace();
        }
    }

    public void getEmails(){
        try {
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", account.username, account.password);

            javax.mail.Folder[] emailFolders = store.getDefaultFolder().list("*");

            for (javax.mail.Folder folder : emailFolders) {
                String folderName = folder.getName();

                if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                    folder.open(javax.mail.Folder.READ_ONLY);
                    javax.mail.Message[] emailMessages = folder.getMessages();
                    for (javax.mail.Message msg : emailMessages) {
                        try {
                            if (db.messageDao().findByMessageNumber(msg.getMessageNumber()) == null) {
                                int folderFromDb = db.folderDao().findByName(folderName).id;
                                Message message = new Message();
                                message.messageNumber = msg.getMessageNumber();
                                message.setReceivedDate(msg.getReceivedDate());
                                message.subject = msg.getSubject();
                                message.content = getText(msg);
                                message.textIsHtml = textIsHtml;
                                message.from = InternetAddress.toString(msg.getFrom());
                                message.to = InternetAddress.toString(msg.getRecipients(javax.mail.Message.RecipientType.TO));
                                message.folderId = folderFromDb;
                                message.accountId = account.id;
                                db.messageDao().insertAll(message);
                            }

                        } catch (IOException e) {
                            System.out.println("An error occurred while getting message.");
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
        catch (MessagingException e) {
            System.out.println("An error occurred while getting messages.");
            e.printStackTrace();
        }
    }

    /**
     * Return the primary text content of the message.
     */
    private String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            if (p.getContent() instanceof String) {
                String s = (String)p.getContent();
                textIsHtml = p.isMimeType("text/html");
                return s;
            } else if (p.getContent() instanceof QPDecoderStream) {
                BufferedInputStream bis = new BufferedInputStream((InputStream) p.getContent());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (true) {
                    int c = bis.read();
                    if (c == -1) {
                        break;
                    }
                    baos.write(c);
                }
                String s = baos.toString();
                textIsHtml = p.isMimeType("text/html");
                return s;
            }
            else {
                return "";
            }

        }
        if (p.isMimeType("multipart/alternative")) {
        // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }
}
