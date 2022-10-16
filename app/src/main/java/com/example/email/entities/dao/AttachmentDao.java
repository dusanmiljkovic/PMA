package com.example.email.entities.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.email.entities.Attachment;

import java.util.List;

@Dao
public interface AttachmentDao {
    @Query("SELECT * FROM attachment WHERE messageId = :messageId")
    List<Attachment> loadAllByMessageId(int messageId);

    @Insert
    void insertAll(Attachment... attachment);

    @Delete
    void delete(Attachment attachment);

    @Update
    int updateAttachment(Attachment attachment);
}
