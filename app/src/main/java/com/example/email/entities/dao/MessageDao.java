package com.example.email.entities.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.email.entities.Folder;
import com.example.email.entities.Message;
import com.example.email.entities.MessageToContacts;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM message ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN received_date END ASC, \n" +
            "CASE WHEN :isAsc = 0 THEN received_date END DESC")
    List<Message> getAll(boolean isAsc);

    @Query("SELECT * FROM message ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN received_date END ASC, \n" +
            "CASE WHEN :isAsc = 0 THEN received_date END DESC " +
            "LIMIT :limit OFFSET :offset")
    List<Message> getNext(boolean isAsc, int offset, int limit);

    @Query("SELECT * FROM message WHERE id IN (:messageIds)")
    List<Message> loadAllByIds(int[] messageIds);

    @Query("SELECT * FROM message WHERE id = :messageIds")
    Message findById(int messageIds);

    @Query("SELECT * FROM message WHERE folderId = (:folderId) ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN received_date END ASC, \n" +
            "CASE WHEN :isAsc = 0 THEN received_date END DESC")
    List<Message> loadAllByFolderId(int folderId, boolean isAsc);

    @Query("SELECT * FROM message WHERE folderId = (:folderId) ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN received_date END ASC, \n" +
            "CASE WHEN :isAsc = 0 THEN received_date END DESC " +
            "LIMIT :limit OFFSET :offset")
    List<Message> loadNextByFolderId(int folderId, boolean isAsc, int offset, int limit);

    @Query("SELECT * FROM message WHERE message_number = :messageNumber")
    Message findByMessageNumber(long messageNumber);

    @Query("SELECT * FROM message WHERE `from` LIKE :from LIMIT 1")
    Message findByFrom(String from);

    @Query("SELECT COUNT(id) FROM message WHERE folderId = :folderId")
    int countByFolderId(int folderId);

    @Insert
    void insertAll(Message... messages);

    @Delete
    void delete(Message message);

    @Update
    void updateMessage(Message message);

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
