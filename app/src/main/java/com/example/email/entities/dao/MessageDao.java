package com.example.email.entities.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.email.entities.Folder;
import com.example.email.entities.Message;
import com.example.email.entities.MessageToContacts;

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

    @Query("DELETE FROM message WHERE accountId = :accountId")
    void deleteAllByAccountId(int accountId);

//    @Transaction
//    @Query("SELECT * FROM message")
//    public List<MessageToContacts> getMessagesWithContacts();
//
//    @Transaction
//    @Query("SELECT * FROM message where id = :messageId")
//    public List<MessageToContacts> getMessageWithContacts(int messageId);
//
//    @Transaction
//    @Query("SELECT * FROM message where message_number = :messageNumber")
//    public List<MessageToContacts> getMessageWithContactsByMessageNumber(int messageNumber);
}
