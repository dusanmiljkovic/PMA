package com.example.email.entities.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.email.entities.Contact;
import com.example.email.entities.Folder;
import com.example.email.entities.FolderWithMessages;

import java.util.List;

@Dao
public interface FolderDao {
    @Query("SELECT * FROM folder")
    List<Folder> getAll();

    @Query("SELECT * FROM folder WHERE id IN (:folderIds)")
    List<Folder> loadAllByIds(int[] folderIds);

    @Query("SELECT * FROM folder WHERE id = :id LIMIT 1")
    Folder findById(int id);

    @Query("SELECT * FROM folder WHERE name LIKE :name LIMIT 1")
    Folder findByName(String name);

    @Insert
    void insertAll(Folder... folders);

    @Update
    Integer updateFolder(Folder folder);

    @Delete
    void delete(Folder folder);

    @Query("DELETE FROM folder WHERE id IN (:folderIds)")
    Void deleteAllByIds(int[] folderIds);

    @Query("SELECT * FROM folder WHERE folderId = :folderId")
    List<Folder> getSubFolders(int folderId);

    @Transaction
    @Query("SELECT * FROM folder")
    public List<FolderWithMessages> getFoldersWithMessages();

    @Transaction
    @Query("SELECT * FROM folder WHERE id = (:folderId)")
    public FolderWithMessages getFolderWithMessages(int folderId);
}
