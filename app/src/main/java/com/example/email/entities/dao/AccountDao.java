package com.example.email.entities.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.email.entities.Account;
import com.example.email.entities.AccountWithMessages;

import java.util.List;

@Dao
public interface AccountDao {

    @Query("SELECT * FROM account ORDER BY id DESC LIMIT 1")
    Account getLast();

    @Query("SELECT * FROM account WHERE id IN (:accountIds)")
    List<Account> loadAllByIds(int[] accountIds);

    @Query("SELECT * FROM account WHERE id = :id LIMIT 1")
    Account findById(int id);

    @Query("SELECT * FROM account WHERE  username = :username LIMIT 1")
    Account findByEmail(String username);

    @Insert
    void insertAll(Account... accounts);

    @Delete
    void delete(Account account);

    @Transaction
    @Query("SELECT * FROM account")
    public List<AccountWithMessages> getAccountsWithMessages();

    @Transaction
    @Query("SELECT * FROM account WHERE id = :accountId LIMIT 1")
    public AccountWithMessages getAccountWithMessages(int accountId);
}
