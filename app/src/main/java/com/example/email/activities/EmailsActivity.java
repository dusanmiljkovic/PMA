package com.example.email.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.EmailListAdapter;
import com.example.email.models.Email;
import com.example.email.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class EmailsActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private EmailListAdapter emailListAdapter;
    private final List<Email> emailList = new ArrayList<>();
    private FloatingActionButton createEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);
        initEmails();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_emails);
        setCheckedItem(R.id.menu_emails);
        createEmailButton = findViewById(R.id.create_email_button);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        emailListAdapter = new EmailListAdapter(emailList);

        recyclerView.setAdapter(emailListAdapter);
        emailListAdapter.setOnItemClickListener(position -> {
            Email email = emailList.get(position);
            Intent intent = new Intent(getApplicationContext(), EmailActivity.class);
            intent.putExtra("From", email.getFrom());
            intent.putExtra("Subject", email.getSubject());
            intent.putExtra("Content", email.getContent());
            startActivity(intent);
        });

        createEmailButton.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), CreateEmailActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.emails_options_menu, menu);

        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search for email here...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                emailListAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    private void initEmails() {
        Properties properties = System.getProperties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.port", "993");
        properties.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imaps.socketFactory.fallback", "false");
        Session imapSession = Session.getInstance(properties);
        int numOfEmails = 12;
        Message[] EmailMessages = new Message[numOfEmails];

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        //Nit za ucitavanje email-ova preko interneta
        Thread thread1 = new Thread()
        {
            @Override
            public void run()
            {
                try {
                    Folder inbox;

                    lock.lock();

                    Store store = imapSession.getStore("imaps");
                    store.connect("imap.gmail.com", Constants.EMAIL, Constants.PASSWORD);
                    inbox = store.getFolder("Inbox");
                    inbox.open(Folder.READ_ONLY);
                    Message[] messages = inbox.getMessages();

                    //Dodavanje email-ova iz inbox-a u listu
                    int numberOfEmails = 0;
                    numberOfEmails = messages.length;
                    System.out.println("Number of emails:");
                    System.out.println(numberOfEmails);
                    System.out.println("Number of emails listed");
                    //Dodato radi testiranja zbog velicine inbox-a
                    if(numberOfEmails > numOfEmails)
                    {
                        numberOfEmails = numOfEmails;
                    }
                    System.out.println(numberOfEmails);

                    System.arraycopy(messages, 0, EmailMessages, 0, numberOfEmails);

                    condition.signal();

                } catch (MessagingException e) {
                    System.out.println("\n\nAn error occurred while getting messages.\n\n");
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }
            }
        };
        thread1.start();
        //Postavljanje glavne niti da ceka ucitavanje email-ova
        try {
            lock.lock();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

        //Nit za dodavanje ucitanih email-ova u listu
        Thread thread2 = new Thread()
        {
            @Override
            public void run() {

                int numOfNulls = 0;
                for(int i = 0; i < numOfEmails; i++)
                {
                    lock.lock();

                    try {
                        if(EmailMessages[i] != null)
                        {
                            emailList.add(new Email(
                                    (long)i,
                                    Arrays.toString(EmailMessages[i].getFrom()),
                                    Constants.EMAIL,
                                    EmailMessages[i].getSentDate(),
                                    EmailMessages[i].getSubject(),
                                    EmailMessages[i].getContent().toString()
                                    ));
                        }
                        else{
                            numOfNulls++;
                        }

                    } catch (MessagingException | IOException e) {
                        System.out.println("Problem");
                        e.printStackTrace();
                    }
                    condition.signal();
                    lock.unlock();
                }
            }
        };
        thread2.start();

        try {
            lock.lock();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}