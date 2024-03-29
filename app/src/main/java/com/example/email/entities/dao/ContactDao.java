package com.example.email.entities.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.email.entities.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact WHERE show = 1 ORDER BY display_name ASC")
    List<Contact> getAll();

    @Query("SELECT * FROM contact WHERE id IN (:contactIds)")
    List<Contact> loadAllByIds(int[] contactIds);

    @Query("SELECT * FROM contact WHERE id = :id LIMIT 1")
    Contact findById(int id);

    @Query("SELECT * FROM contact WHERE email = :email LIMIT 1")
    Contact findByEmail(String email);

    @Update
    Integer updateContact(Contact contact);

    @Insert
    void insertAll(Contact... contacts);

    @Delete
    void delete(Contact contact);

    @Query("DELETE FROM contact WHERE id IN (:contactIds)")
    void deleteAllByIds(int[] contactIds);
}
