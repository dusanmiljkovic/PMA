package com.example.email.entities.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.email.entities.MessageTag;
import com.example.email.entities.Tag;

import java.util.List;

public interface MessageTagDao {

    @Query("SELECT * FROM messagetag")
    MessageTag findALl();

    @Query("SELECT * FROM messagetag WHERE messageId = :messageId")
    List<MessageTag> loadAllByMessageId(int messageId);

    @Query("SELECT * FROM messagetag WHERE tagId = :tagId")
    List<MessageTag> loadAllByTagId(int tagId);

    @Query("SELECT * FROM messagetag WHERE id = :id LIMIT 1")
    Tag findById(int id);

    @Insert
    void insertAll(MessageTag... messageTags);

    @Delete
    void delete(MessageTag messageTag);

    @Update
    int updateTag(MessageTag messageTag);
}
