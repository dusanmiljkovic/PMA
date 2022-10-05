package com.example.email.entities.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.email.entities.Folder;
import com.example.email.entities.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM message ORDER BY received_date DESC")
    List<Message> getAll();

    @Query("SELECT * FROM message WHERE id IN (:messageIds)")
    List<Message> loadAllByIds(int[] messageIds);

    @Query("SELECT * FROM message WHERE folderId = (:folderId) ORDER BY received_date DESC")
    List<Message> loadAllByFolderId(int folderId);

    @Query("SELECT * FROM message WHERE message_number = :messageNumber")
    Message findByMessageNumber(int messageNumber);

    @Query("SELECT * FROM message WHERE `from` LIKE :from LIMIT 1")
    Message findByFrom(String from);

    @Insert
    void insertAll(Message... folders);

    @Delete
    void delete(Message folder);
}
