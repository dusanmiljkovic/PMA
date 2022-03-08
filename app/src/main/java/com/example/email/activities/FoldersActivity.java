package com.example.email.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.adapters.FolderListAdapter;
import com.example.email.models.Folder;
import com.example.email.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class FoldersActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private FolderListAdapter folderListAdapter;
    private final List<Folder> folders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);
        initFolders();

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.menu_title_folders);
        setCheckedItem(R.id.menu_folders);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        folderListAdapter = new FolderListAdapter(folders);

        recyclerView.setAdapter(folderListAdapter);

        folderListAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getApplicationContext(), FolderActivity.class);
            startActivity(intent);
        });
    }

    private void initFolders() {
        Properties properties = System.getProperties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.port", "993");
        properties.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imaps.socketFactory.fallback", "false");

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        //Nit za ucitavanje email-ova preko interneta
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                try {
                    lock.lock();
                    Session session = Session.getDefaultInstance(properties, null);
                    Store store = session.getStore("imaps");
                    store.connect("imap.gmail.com", Constants.EMAIL, Constants.PASSWORD);
                    javax.mail.Folder[] emailFolders = store.getDefaultFolder().list("*");
                    long i = 1;
                    for (javax.mail.Folder folder : emailFolders) {
                        if ((folder.getType() & javax.mail.Folder.HOLDS_MESSAGES) != 0) {
                            folders.add(new Folder(i, folder.getFullName(), folder.getMessageCount()));
                            i++;
                        }
                    }
                    condition.signal();
                }
                catch (MessagingException e) {
                    System.out.println("\n\nAn error occurred while getting folders.\n\n");
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }
            }
        };
        thread1.start();

        try {
            lock.lock();
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.folders_options_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_create_folder) {
            startActivity(new Intent(getApplicationContext(), CreateFolderActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

}