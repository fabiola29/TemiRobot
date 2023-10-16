package com.example.temi1.db.UserTemi;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.temi1.model.TemiUser;

import java.util.List;

@Dao
public interface TemiUserDao {

    @Insert
    void insertUserIntoDb(TemiUser user);

    @Query("DELETE from userTable")
    void deleteAll();

    @Delete
    void deleteUser(TemiUser user);

    @Update
    void updateUser(TemiUser updatedUser);

    @Query("SELECT * FROM userTable ORDER BY userId ASC")
    LiveData<List<TemiUser>> getUserFromDb();

    @Query("SELECT * FROM userTable")
    List<TemiUser> getAll();

    @Query("SELECT * FROM userTable WHERE biometric_key = :biometricKey")
    TemiUser getUserByBiometricKey(String biometricKey);

    @Query("SELECT * FROM userTable WHERE userId = :userId AND name = :name")
    TemiUser getUserByIdAndName(int userId, String name);

    @Query("SELECT * FROM userTable WHERE userId = :userId")
    TemiUser getUserById(int userId);

    @Query("SELECT * FROM userTable WHERE email = :email")
    TemiUser getUserByEmail(String email);

}
