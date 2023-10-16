package com.example.temi1.db.UserTemi;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.temi1.model.TemiUser;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TemiUserDao_Impl implements TemiUserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TemiUser> __insertionAdapterOfTemiUser;

  private final EntityDeletionOrUpdateAdapter<TemiUser> __deletionAdapterOfTemiUser;

  private final EntityDeletionOrUpdateAdapter<TemiUser> __updateAdapterOfTemiUser;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TemiUserDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTemiUser = new EntityInsertionAdapter<TemiUser>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `userTable` (`userId`,`name`,`email`,`password`,`imagePath`,`biometric_key`,`created`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TemiUser value) {
        stmt.bindLong(1, value.getUserId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEmail());
        }
        if (value.getPassword() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPassword());
        }
        if (value.getImagePath() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getImagePath());
        }
        if (value.getBiometricKey() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getBiometricKey());
        }
        stmt.bindLong(7, value.getCreated());
      }
    };
    this.__deletionAdapterOfTemiUser = new EntityDeletionOrUpdateAdapter<TemiUser>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `userTable` WHERE `userId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TemiUser value) {
        stmt.bindLong(1, value.getUserId());
      }
    };
    this.__updateAdapterOfTemiUser = new EntityDeletionOrUpdateAdapter<TemiUser>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `userTable` SET `userId` = ?,`name` = ?,`email` = ?,`password` = ?,`imagePath` = ?,`biometric_key` = ?,`created` = ? WHERE `userId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TemiUser value) {
        stmt.bindLong(1, value.getUserId());
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getEmail() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getEmail());
        }
        if (value.getPassword() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getPassword());
        }
        if (value.getImagePath() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getImagePath());
        }
        if (value.getBiometricKey() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getBiometricKey());
        }
        stmt.bindLong(7, value.getCreated());
        stmt.bindLong(8, value.getUserId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from userTable";
        return _query;
      }
    };
  }

  @Override
  public void insertUserIntoDb(final TemiUser user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTemiUser.insert(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteUser(final TemiUser user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTemiUser.handle(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateUser(final TemiUser updatedUser) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTemiUser.handle(updatedUser);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<TemiUser>> getUserFromDb() {
    final String _sql = "SELECT * FROM userTable ORDER BY userId ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"userTable"}, false, new Callable<List<TemiUser>>() {
      @Override
      public List<TemiUser> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfBiometricKey = CursorUtil.getColumnIndexOrThrow(_cursor, "biometric_key");
          final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
          final List<TemiUser> _result = new ArrayList<TemiUser>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TemiUser _item;
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpPassword;
            _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            final String _tmpBiometricKey;
            _tmpBiometricKey = _cursor.getString(_cursorIndexOfBiometricKey);
            _item = new TemiUser(_tmpUserId,_tmpName,_tmpEmail,_tmpBiometricKey,_tmpPassword);
            final String _tmpImagePath;
            _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            _item.setImagePath(_tmpImagePath);
            final long _tmpCreated;
            _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
            _item.setCreated(_tmpCreated);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<TemiUser> getAll() {
    final String _sql = "SELECT * FROM userTable";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfBiometricKey = CursorUtil.getColumnIndexOrThrow(_cursor, "biometric_key");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final List<TemiUser> _result = new ArrayList<TemiUser>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TemiUser _item;
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpPassword;
        _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        final String _tmpBiometricKey;
        _tmpBiometricKey = _cursor.getString(_cursorIndexOfBiometricKey);
        _item = new TemiUser(_tmpUserId,_tmpName,_tmpEmail,_tmpBiometricKey,_tmpPassword);
        final String _tmpImagePath;
        _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        _item.setImagePath(_tmpImagePath);
        final long _tmpCreated;
        _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
        _item.setCreated(_tmpCreated);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TemiUser getUserByBiometricKey(final String biometricKey) {
    final String _sql = "SELECT * FROM userTable WHERE biometric_key = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (biometricKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, biometricKey);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfBiometricKey = CursorUtil.getColumnIndexOrThrow(_cursor, "biometric_key");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final TemiUser _result;
      if(_cursor.moveToFirst()) {
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpPassword;
        _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        final String _tmpBiometricKey;
        _tmpBiometricKey = _cursor.getString(_cursorIndexOfBiometricKey);
        _result = new TemiUser(_tmpUserId,_tmpName,_tmpEmail,_tmpBiometricKey,_tmpPassword);
        final String _tmpImagePath;
        _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        _result.setImagePath(_tmpImagePath);
        final long _tmpCreated;
        _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
        _result.setCreated(_tmpCreated);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TemiUser getUserByIdAndName(final int userId, final String name) {
    final String _sql = "SELECT * FROM userTable WHERE userId = ? AND name = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    _argIndex = 2;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfBiometricKey = CursorUtil.getColumnIndexOrThrow(_cursor, "biometric_key");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final TemiUser _result;
      if(_cursor.moveToFirst()) {
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpPassword;
        _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        final String _tmpBiometricKey;
        _tmpBiometricKey = _cursor.getString(_cursorIndexOfBiometricKey);
        _result = new TemiUser(_tmpUserId,_tmpName,_tmpEmail,_tmpBiometricKey,_tmpPassword);
        final String _tmpImagePath;
        _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        _result.setImagePath(_tmpImagePath);
        final long _tmpCreated;
        _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
        _result.setCreated(_tmpCreated);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TemiUser getUserById(final int userId) {
    final String _sql = "SELECT * FROM userTable WHERE userId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, userId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfBiometricKey = CursorUtil.getColumnIndexOrThrow(_cursor, "biometric_key");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final TemiUser _result;
      if(_cursor.moveToFirst()) {
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpPassword;
        _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        final String _tmpBiometricKey;
        _tmpBiometricKey = _cursor.getString(_cursorIndexOfBiometricKey);
        _result = new TemiUser(_tmpUserId,_tmpName,_tmpEmail,_tmpBiometricKey,_tmpPassword);
        final String _tmpImagePath;
        _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        _result.setImagePath(_tmpImagePath);
        final long _tmpCreated;
        _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
        _result.setCreated(_tmpCreated);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public TemiUser getUserByEmail(final String email) {
    final String _sql = "SELECT * FROM userTable WHERE email = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfBiometricKey = CursorUtil.getColumnIndexOrThrow(_cursor, "biometric_key");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final TemiUser _result;
      if(_cursor.moveToFirst()) {
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpPassword;
        _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        final String _tmpBiometricKey;
        _tmpBiometricKey = _cursor.getString(_cursorIndexOfBiometricKey);
        _result = new TemiUser(_tmpUserId,_tmpName,_tmpEmail,_tmpBiometricKey,_tmpPassword);
        final String _tmpImagePath;
        _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
        _result.setImagePath(_tmpImagePath);
        final long _tmpCreated;
        _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
        _result.setCreated(_tmpCreated);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
