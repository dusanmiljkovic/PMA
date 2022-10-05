package com.example.email.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.email.entities.Folder;
import com.example.email.entities.Message;
import com.example.email.entities.dao.FolderDao;
import com.example.email.entities.dao.MessageDao;

@Database(entities = {Folder.class, Message.class}, version = 1)
public abstract class MailDatabase extends RoomDatabase {

    public abstract FolderDao folderDao();

    public abstract MessageDao messageDao();

    private static MailDatabase INSTANCE;

    public static MailDatabase getDbInstance(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MailDatabase.class, "mail-db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
