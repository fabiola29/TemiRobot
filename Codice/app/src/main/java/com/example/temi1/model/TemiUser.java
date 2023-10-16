package com.example.temi1.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "userTable")
public class TemiUser {

    @PrimaryKey
    @NonNull
    private int userId;
    private String name;
    private String email;
    private String password;
    private String imagePath;
    @ColumnInfo(name = "biometric_key")
    private String biometricKey;
    private long created;

    @Ignore
    public TemiUser() {
    }

    public TemiUser(int userId, String name, String email, String biometricKey, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.biometricKey = biometricKey;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBiometricKey() {
        return biometricKey;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBiometricKey(String biometricKey) {
        this.biometricKey = biometricKey;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "TemiUser{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", biometricKey='" + biometricKey + '\'' +
                '}';
    }
}
