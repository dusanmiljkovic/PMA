package com.example.email.entities.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.email.entities.Tag;

public interface TagDao {

    @Query("SELECT * FROM tag")
    Tag findALl();

    @Query("SELECT * FROM tag WHERE id = :id LIMIT 1")
    Tag findById(int id);

    @Query("SELECT * FROM tag WHERE  name = :name LIMIT 1")
    Tag findByName(String name);

    @Insert
    void insertAll(Tag... tags);

    @Delete
    void delete(Tag tag);

    @Update
    int updateTag(Tag tag);

}
