package com.example.email.entities.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.email.entities.Folder;

import java.util.List;

@Dao
public interface FolderDao {
    @Query("SELECT * FROM folder")
    List<Folder> getAll();

    @Query("SELECT * FROM folder WHERE id IN (:folderIds)")
    List<Folder> loadAllByIds(int[] folderIds);

    @Query("SELECT * FROM folder WHERE name LIKE :name LIMIT 1")
    Folder findByName(String name);

    @Query("SELECT * FROM folder WHERE full_name LIKE :full_name LIMIT 1")
    Folder findByFullName(String full_name);

    @Insert
    void insertAll(Folder... folders);

    @Delete
    void delete(Folder folder);
}
