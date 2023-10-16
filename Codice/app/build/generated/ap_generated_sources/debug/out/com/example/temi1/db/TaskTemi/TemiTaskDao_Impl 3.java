package com.example.temi1.db.TaskTemi;

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
import com.example.temi1.model.TemiTask;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TemiTaskDao_Impl implements TemiTaskDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TemiTask> __insertionAdapterOfTemiTask;

  private final EntityDeletionOrUpdateAdapter<TemiTask> __deletionAdapterOfTemiTask;

  private final EntityDeletionOrUpdateAdapter<TemiTask> __updateAdapterOfTemiTask;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TemiTaskDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTemiTask = new EntityInsertionAdapter<TemiTask>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `task_table` (`taskId`,`hour`,`minute`,`started`,`recurring`,`monday`,`tuesday`,`wednesday`,`thursday`,`friday`,`saturday`,`sunday`,`taskName`,`taskActivity`,`location`,`created`,`userId`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TemiTask value) {
        stmt.bindLong(1, value.getTaskId());
        stmt.bindLong(2, value.getHour());
        stmt.bindLong(3, value.getMinute());
        final int _tmp;
        _tmp = value.isStarted() ? 1 : 0;
        stmt.bindLong(4, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isRecurring() ? 1 : 0;
        stmt.bindLong(5, _tmp_1);
        final int _tmp_2;
        _tmp_2 = value.isMonday() ? 1 : 0;
        stmt.bindLong(6, _tmp_2);
        final int _tmp_3;
        _tmp_3 = value.isTuesday() ? 1 : 0;
        stmt.bindLong(7, _tmp_3);
        final int _tmp_4;
        _tmp_4 = value.isWednesday() ? 1 : 0;
        stmt.bindLong(8, _tmp_4);
        final int _tmp_5;
        _tmp_5 = value.isThursday() ? 1 : 0;
        stmt.bindLong(9, _tmp_5);
        final int _tmp_6;
        _tmp_6 = value.isFriday() ? 1 : 0;
        stmt.bindLong(10, _tmp_6);
        final int _tmp_7;
        _tmp_7 = value.isSaturday() ? 1 : 0;
        stmt.bindLong(11, _tmp_7);
        final int _tmp_8;
        _tmp_8 = value.isSunday() ? 1 : 0;
        stmt.bindLong(12, _tmp_8);
        if (value.getTaskName() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getTaskName());
        }
        if (value.getTaskActivity() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getTaskActivity());
        }
        if (value.getLocation() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getLocation());
        }
        stmt.bindLong(16, value.getCreated());
        stmt.bindLong(17, value.getUserId());
      }
    };
    this.__deletionAdapterOfTemiTask = new EntityDeletionOrUpdateAdapter<TemiTask>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `task_table` WHERE `taskId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TemiTask value) {
        stmt.bindLong(1, value.getTaskId());
      }
    };
    this.__updateAdapterOfTemiTask = new EntityDeletionOrUpdateAdapter<TemiTask>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `task_table` SET `taskId` = ?,`hour` = ?,`minute` = ?,`started` = ?,`recurring` = ?,`monday` = ?,`tuesday` = ?,`wednesday` = ?,`thursday` = ?,`friday` = ?,`saturday` = ?,`sunday` = ?,`taskName` = ?,`taskActivity` = ?,`location` = ?,`created` = ?,`userId` = ? WHERE `taskId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TemiTask value) {
        stmt.bindLong(1, value.getTaskId());
        stmt.bindLong(2, value.getHour());
        stmt.bindLong(3, value.getMinute());
        final int _tmp;
        _tmp = value.isStarted() ? 1 : 0;
        stmt.bindLong(4, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isRecurring() ? 1 : 0;
        stmt.bindLong(5, _tmp_1);
        final int _tmp_2;
        _tmp_2 = value.isMonday() ? 1 : 0;
        stmt.bindLong(6, _tmp_2);
        final int _tmp_3;
        _tmp_3 = value.isTuesday() ? 1 : 0;
        stmt.bindLong(7, _tmp_3);
        final int _tmp_4;
        _tmp_4 = value.isWednesday() ? 1 : 0;
        stmt.bindLong(8, _tmp_4);
        final int _tmp_5;
        _tmp_5 = value.isThursday() ? 1 : 0;
        stmt.bindLong(9, _tmp_5);
        final int _tmp_6;
        _tmp_6 = value.isFriday() ? 1 : 0;
        stmt.bindLong(10, _tmp_6);
        final int _tmp_7;
        _tmp_7 = value.isSaturday() ? 1 : 0;
        stmt.bindLong(11, _tmp_7);
        final int _tmp_8;
        _tmp_8 = value.isSunday() ? 1 : 0;
        stmt.bindLong(12, _tmp_8);
        if (value.getTaskName() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getTaskName());
        }
        if (value.getTaskActivity() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getTaskActivity());
        }
        if (value.getLocation() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getLocation());
        }
        stmt.bindLong(16, value.getCreated());
        stmt.bindLong(17, value.getUserId());
        stmt.bindLong(18, value.getTaskId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from task_table";
        return _query;
      }
    };
  }

  @Override
  public void insertTaskIntoDb(final TemiTask temiTask) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTemiTask.insert(temiTask);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteTask(final TemiTask task) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTemiTask.handle(task);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTask(final TemiTask updatedTask) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTemiTask.handle(updatedTask);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final TemiTask temiTask) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTemiTask.handle(temiTask);
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
  public List<TemiTask> getAll() {
    final String _sql = "SELECT * FROM task_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
      final int _cursorIndexOfHour = CursorUtil.getColumnIndexOrThrow(_cursor, "hour");
      final int _cursorIndexOfMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "minute");
      final int _cursorIndexOfStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "started");
      final int _cursorIndexOfRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "recurring");
      final int _cursorIndexOfMonday = CursorUtil.getColumnIndexOrThrow(_cursor, "monday");
      final int _cursorIndexOfTuesday = CursorUtil.getColumnIndexOrThrow(_cursor, "tuesday");
      final int _cursorIndexOfWednesday = CursorUtil.getColumnIndexOrThrow(_cursor, "wednesday");
      final int _cursorIndexOfThursday = CursorUtil.getColumnIndexOrThrow(_cursor, "thursday");
      final int _cursorIndexOfFriday = CursorUtil.getColumnIndexOrThrow(_cursor, "friday");
      final int _cursorIndexOfSaturday = CursorUtil.getColumnIndexOrThrow(_cursor, "saturday");
      final int _cursorIndexOfSunday = CursorUtil.getColumnIndexOrThrow(_cursor, "sunday");
      final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
      final int _cursorIndexOfTaskActivity = CursorUtil.getColumnIndexOrThrow(_cursor, "taskActivity");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final List<TemiTask> _result = new ArrayList<TemiTask>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TemiTask _item;
        final int _tmpTaskId;
        _tmpTaskId = _cursor.getInt(_cursorIndexOfTaskId);
        final int _tmpHour;
        _tmpHour = _cursor.getInt(_cursorIndexOfHour);
        final int _tmpMinute;
        _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
        final boolean _tmpStarted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfStarted);
        _tmpStarted = _tmp != 0;
        final boolean _tmpRecurring;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfRecurring);
        _tmpRecurring = _tmp_1 != 0;
        final boolean _tmpMonday;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfMonday);
        _tmpMonday = _tmp_2 != 0;
        final boolean _tmpTuesday;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfTuesday);
        _tmpTuesday = _tmp_3 != 0;
        final boolean _tmpWednesday;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfWednesday);
        _tmpWednesday = _tmp_4 != 0;
        final boolean _tmpThursday;
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfThursday);
        _tmpThursday = _tmp_5 != 0;
        final boolean _tmpFriday;
        final int _tmp_6;
        _tmp_6 = _cursor.getInt(_cursorIndexOfFriday);
        _tmpFriday = _tmp_6 != 0;
        final boolean _tmpSaturday;
        final int _tmp_7;
        _tmp_7 = _cursor.getInt(_cursorIndexOfSaturday);
        _tmpSaturday = _tmp_7 != 0;
        final boolean _tmpSunday;
        final int _tmp_8;
        _tmp_8 = _cursor.getInt(_cursorIndexOfSunday);
        _tmpSunday = _tmp_8 != 0;
        final String _tmpTaskName;
        _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
        final String _tmpTaskActivity;
        _tmpTaskActivity = _cursor.getString(_cursorIndexOfTaskActivity);
        final String _tmpLocation;
        _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        final long _tmpCreated;
        _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item = new TemiTask(_tmpTaskId,_tmpHour,_tmpMinute,_tmpTaskName,_tmpCreated,_tmpStarted,_tmpRecurring,_tmpMonday,_tmpTuesday,_tmpWednesday,_tmpThursday,_tmpFriday,_tmpSaturday,_tmpSunday,_tmpUserId,_tmpTaskActivity,_tmpLocation);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<TemiTask>> getTaskFromDb() {
    final String _sql = "SELECT * FROM task_table ORDER BY taskId ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"task_table"}, false, new Callable<List<TemiTask>>() {
      @Override
      public List<TemiTask> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
          final int _cursorIndexOfHour = CursorUtil.getColumnIndexOrThrow(_cursor, "hour");
          final int _cursorIndexOfMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "minute");
          final int _cursorIndexOfStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "started");
          final int _cursorIndexOfRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "recurring");
          final int _cursorIndexOfMonday = CursorUtil.getColumnIndexOrThrow(_cursor, "monday");
          final int _cursorIndexOfTuesday = CursorUtil.getColumnIndexOrThrow(_cursor, "tuesday");
          final int _cursorIndexOfWednesday = CursorUtil.getColumnIndexOrThrow(_cursor, "wednesday");
          final int _cursorIndexOfThursday = CursorUtil.getColumnIndexOrThrow(_cursor, "thursday");
          final int _cursorIndexOfFriday = CursorUtil.getColumnIndexOrThrow(_cursor, "friday");
          final int _cursorIndexOfSaturday = CursorUtil.getColumnIndexOrThrow(_cursor, "saturday");
          final int _cursorIndexOfSunday = CursorUtil.getColumnIndexOrThrow(_cursor, "sunday");
          final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
          final int _cursorIndexOfTaskActivity = CursorUtil.getColumnIndexOrThrow(_cursor, "taskActivity");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<TemiTask> _result = new ArrayList<TemiTask>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TemiTask _item;
            final int _tmpTaskId;
            _tmpTaskId = _cursor.getInt(_cursorIndexOfTaskId);
            final int _tmpHour;
            _tmpHour = _cursor.getInt(_cursorIndexOfHour);
            final int _tmpMinute;
            _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
            final boolean _tmpStarted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfStarted);
            _tmpStarted = _tmp != 0;
            final boolean _tmpRecurring;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRecurring);
            _tmpRecurring = _tmp_1 != 0;
            final boolean _tmpMonday;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMonday);
            _tmpMonday = _tmp_2 != 0;
            final boolean _tmpTuesday;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfTuesday);
            _tmpTuesday = _tmp_3 != 0;
            final boolean _tmpWednesday;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfWednesday);
            _tmpWednesday = _tmp_4 != 0;
            final boolean _tmpThursday;
            final int _tmp_5;
            _tmp_5 = _cursor.getInt(_cursorIndexOfThursday);
            _tmpThursday = _tmp_5 != 0;
            final boolean _tmpFriday;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfFriday);
            _tmpFriday = _tmp_6 != 0;
            final boolean _tmpSaturday;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfSaturday);
            _tmpSaturday = _tmp_7 != 0;
            final boolean _tmpSunday;
            final int _tmp_8;
            _tmp_8 = _cursor.getInt(_cursorIndexOfSunday);
            _tmpSunday = _tmp_8 != 0;
            final String _tmpTaskName;
            _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
            final String _tmpTaskActivity;
            _tmpTaskActivity = _cursor.getString(_cursorIndexOfTaskActivity);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final long _tmpCreated;
            _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            _item = new TemiTask(_tmpTaskId,_tmpHour,_tmpMinute,_tmpTaskName,_tmpCreated,_tmpStarted,_tmpRecurring,_tmpMonday,_tmpTuesday,_tmpWednesday,_tmpThursday,_tmpFriday,_tmpSaturday,_tmpSunday,_tmpUserId,_tmpTaskActivity,_tmpLocation);
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
  public List<TemiTask> getTaskByNameAndTimeAndDays(final String name, final int hour,
      final int minute, final boolean mon, final boolean tue, final boolean wed, final boolean thu,
      final boolean fri, final boolean sat, final boolean sun) {
    final String _sql = "SELECT * FROM task_table WHERE taskName = ? AND hour = ? AND minute = ? AND monday = ? AND tuesday = ? AND wednesday = ? AND thursday = ? AND friday = ? AND saturday = ? AND sunday = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 10);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, hour);
    _argIndex = 3;
    _statement.bindLong(_argIndex, minute);
    _argIndex = 4;
    final int _tmp;
    _tmp = mon ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    _argIndex = 5;
    final int _tmp_1;
    _tmp_1 = tue ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_1);
    _argIndex = 6;
    final int _tmp_2;
    _tmp_2 = wed ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_2);
    _argIndex = 7;
    final int _tmp_3;
    _tmp_3 = thu ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_3);
    _argIndex = 8;
    final int _tmp_4;
    _tmp_4 = fri ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_4);
    _argIndex = 9;
    final int _tmp_5;
    _tmp_5 = sat ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_5);
    _argIndex = 10;
    final int _tmp_6;
    _tmp_6 = sun ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_6);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
      final int _cursorIndexOfHour = CursorUtil.getColumnIndexOrThrow(_cursor, "hour");
      final int _cursorIndexOfMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "minute");
      final int _cursorIndexOfStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "started");
      final int _cursorIndexOfRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "recurring");
      final int _cursorIndexOfMonday = CursorUtil.getColumnIndexOrThrow(_cursor, "monday");
      final int _cursorIndexOfTuesday = CursorUtil.getColumnIndexOrThrow(_cursor, "tuesday");
      final int _cursorIndexOfWednesday = CursorUtil.getColumnIndexOrThrow(_cursor, "wednesday");
      final int _cursorIndexOfThursday = CursorUtil.getColumnIndexOrThrow(_cursor, "thursday");
      final int _cursorIndexOfFriday = CursorUtil.getColumnIndexOrThrow(_cursor, "friday");
      final int _cursorIndexOfSaturday = CursorUtil.getColumnIndexOrThrow(_cursor, "saturday");
      final int _cursorIndexOfSunday = CursorUtil.getColumnIndexOrThrow(_cursor, "sunday");
      final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
      final int _cursorIndexOfTaskActivity = CursorUtil.getColumnIndexOrThrow(_cursor, "taskActivity");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final List<TemiTask> _result = new ArrayList<TemiTask>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TemiTask _item;
        final int _tmpTaskId;
        _tmpTaskId = _cursor.getInt(_cursorIndexOfTaskId);
        final int _tmpHour;
        _tmpHour = _cursor.getInt(_cursorIndexOfHour);
        final int _tmpMinute;
        _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
        final boolean _tmpStarted;
        final int _tmp_7;
        _tmp_7 = _cursor.getInt(_cursorIndexOfStarted);
        _tmpStarted = _tmp_7 != 0;
        final boolean _tmpRecurring;
        final int _tmp_8;
        _tmp_8 = _cursor.getInt(_cursorIndexOfRecurring);
        _tmpRecurring = _tmp_8 != 0;
        final boolean _tmpMonday;
        final int _tmp_9;
        _tmp_9 = _cursor.getInt(_cursorIndexOfMonday);
        _tmpMonday = _tmp_9 != 0;
        final boolean _tmpTuesday;
        final int _tmp_10;
        _tmp_10 = _cursor.getInt(_cursorIndexOfTuesday);
        _tmpTuesday = _tmp_10 != 0;
        final boolean _tmpWednesday;
        final int _tmp_11;
        _tmp_11 = _cursor.getInt(_cursorIndexOfWednesday);
        _tmpWednesday = _tmp_11 != 0;
        final boolean _tmpThursday;
        final int _tmp_12;
        _tmp_12 = _cursor.getInt(_cursorIndexOfThursday);
        _tmpThursday = _tmp_12 != 0;
        final boolean _tmpFriday;
        final int _tmp_13;
        _tmp_13 = _cursor.getInt(_cursorIndexOfFriday);
        _tmpFriday = _tmp_13 != 0;
        final boolean _tmpSaturday;
        final int _tmp_14;
        _tmp_14 = _cursor.getInt(_cursorIndexOfSaturday);
        _tmpSaturday = _tmp_14 != 0;
        final boolean _tmpSunday;
        final int _tmp_15;
        _tmp_15 = _cursor.getInt(_cursorIndexOfSunday);
        _tmpSunday = _tmp_15 != 0;
        final String _tmpTaskName;
        _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
        final String _tmpTaskActivity;
        _tmpTaskActivity = _cursor.getString(_cursorIndexOfTaskActivity);
        final String _tmpLocation;
        _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        final long _tmpCreated;
        _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item = new TemiTask(_tmpTaskId,_tmpHour,_tmpMinute,_tmpTaskName,_tmpCreated,_tmpStarted,_tmpRecurring,_tmpMonday,_tmpTuesday,_tmpWednesday,_tmpThursday,_tmpFriday,_tmpSaturday,_tmpSunday,_tmpUserId,_tmpTaskActivity,_tmpLocation);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TemiTask> getTaskByTimeAndDays(final int hour, final int minute, final boolean mon,
      final boolean tue, final boolean wed, final boolean thu, final boolean fri, final boolean sat,
      final boolean sun) {
    final String _sql = "SELECT * FROM task_table WHERE hour = ? AND minute = ? AND monday = ? AND tuesday = ? AND wednesday = ? AND thursday = ? AND friday = ? AND saturday = ? AND sunday = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 9);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, hour);
    _argIndex = 2;
    _statement.bindLong(_argIndex, minute);
    _argIndex = 3;
    final int _tmp;
    _tmp = mon ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp);
    _argIndex = 4;
    final int _tmp_1;
    _tmp_1 = tue ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_1);
    _argIndex = 5;
    final int _tmp_2;
    _tmp_2 = wed ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_2);
    _argIndex = 6;
    final int _tmp_3;
    _tmp_3 = thu ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_3);
    _argIndex = 7;
    final int _tmp_4;
    _tmp_4 = fri ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_4);
    _argIndex = 8;
    final int _tmp_5;
    _tmp_5 = sat ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_5);
    _argIndex = 9;
    final int _tmp_6;
    _tmp_6 = sun ? 1 : 0;
    _statement.bindLong(_argIndex, _tmp_6);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
      final int _cursorIndexOfHour = CursorUtil.getColumnIndexOrThrow(_cursor, "hour");
      final int _cursorIndexOfMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "minute");
      final int _cursorIndexOfStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "started");
      final int _cursorIndexOfRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "recurring");
      final int _cursorIndexOfMonday = CursorUtil.getColumnIndexOrThrow(_cursor, "monday");
      final int _cursorIndexOfTuesday = CursorUtil.getColumnIndexOrThrow(_cursor, "tuesday");
      final int _cursorIndexOfWednesday = CursorUtil.getColumnIndexOrThrow(_cursor, "wednesday");
      final int _cursorIndexOfThursday = CursorUtil.getColumnIndexOrThrow(_cursor, "thursday");
      final int _cursorIndexOfFriday = CursorUtil.getColumnIndexOrThrow(_cursor, "friday");
      final int _cursorIndexOfSaturday = CursorUtil.getColumnIndexOrThrow(_cursor, "saturday");
      final int _cursorIndexOfSunday = CursorUtil.getColumnIndexOrThrow(_cursor, "sunday");
      final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
      final int _cursorIndexOfTaskActivity = CursorUtil.getColumnIndexOrThrow(_cursor, "taskActivity");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final List<TemiTask> _result = new ArrayList<TemiTask>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TemiTask _item;
        final int _tmpTaskId;
        _tmpTaskId = _cursor.getInt(_cursorIndexOfTaskId);
        final int _tmpHour;
        _tmpHour = _cursor.getInt(_cursorIndexOfHour);
        final int _tmpMinute;
        _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
        final boolean _tmpStarted;
        final int _tmp_7;
        _tmp_7 = _cursor.getInt(_cursorIndexOfStarted);
        _tmpStarted = _tmp_7 != 0;
        final boolean _tmpRecurring;
        final int _tmp_8;
        _tmp_8 = _cursor.getInt(_cursorIndexOfRecurring);
        _tmpRecurring = _tmp_8 != 0;
        final boolean _tmpMonday;
        final int _tmp_9;
        _tmp_9 = _cursor.getInt(_cursorIndexOfMonday);
        _tmpMonday = _tmp_9 != 0;
        final boolean _tmpTuesday;
        final int _tmp_10;
        _tmp_10 = _cursor.getInt(_cursorIndexOfTuesday);
        _tmpTuesday = _tmp_10 != 0;
        final boolean _tmpWednesday;
        final int _tmp_11;
        _tmp_11 = _cursor.getInt(_cursorIndexOfWednesday);
        _tmpWednesday = _tmp_11 != 0;
        final boolean _tmpThursday;
        final int _tmp_12;
        _tmp_12 = _cursor.getInt(_cursorIndexOfThursday);
        _tmpThursday = _tmp_12 != 0;
        final boolean _tmpFriday;
        final int _tmp_13;
        _tmp_13 = _cursor.getInt(_cursorIndexOfFriday);
        _tmpFriday = _tmp_13 != 0;
        final boolean _tmpSaturday;
        final int _tmp_14;
        _tmp_14 = _cursor.getInt(_cursorIndexOfSaturday);
        _tmpSaturday = _tmp_14 != 0;
        final boolean _tmpSunday;
        final int _tmp_15;
        _tmp_15 = _cursor.getInt(_cursorIndexOfSunday);
        _tmpSunday = _tmp_15 != 0;
        final String _tmpTaskName;
        _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
        final String _tmpTaskActivity;
        _tmpTaskActivity = _cursor.getString(_cursorIndexOfTaskActivity);
        final String _tmpLocation;
        _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        final long _tmpCreated;
        _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item = new TemiTask(_tmpTaskId,_tmpHour,_tmpMinute,_tmpTaskName,_tmpCreated,_tmpStarted,_tmpRecurring,_tmpMonday,_tmpTuesday,_tmpWednesday,_tmpThursday,_tmpFriday,_tmpSaturday,_tmpSunday,_tmpUserId,_tmpTaskActivity,_tmpLocation);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<TemiTask> getTaskByTime(final int hour, final int minute) {
    final String _sql = "SELECT * FROM task_table WHERE hour = ? AND minute = ? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, hour);
    _argIndex = 2;
    _statement.bindLong(_argIndex, minute);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTaskId = CursorUtil.getColumnIndexOrThrow(_cursor, "taskId");
      final int _cursorIndexOfHour = CursorUtil.getColumnIndexOrThrow(_cursor, "hour");
      final int _cursorIndexOfMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "minute");
      final int _cursorIndexOfStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "started");
      final int _cursorIndexOfRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "recurring");
      final int _cursorIndexOfMonday = CursorUtil.getColumnIndexOrThrow(_cursor, "monday");
      final int _cursorIndexOfTuesday = CursorUtil.getColumnIndexOrThrow(_cursor, "tuesday");
      final int _cursorIndexOfWednesday = CursorUtil.getColumnIndexOrThrow(_cursor, "wednesday");
      final int _cursorIndexOfThursday = CursorUtil.getColumnIndexOrThrow(_cursor, "thursday");
      final int _cursorIndexOfFriday = CursorUtil.getColumnIndexOrThrow(_cursor, "friday");
      final int _cursorIndexOfSaturday = CursorUtil.getColumnIndexOrThrow(_cursor, "saturday");
      final int _cursorIndexOfSunday = CursorUtil.getColumnIndexOrThrow(_cursor, "sunday");
      final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
      final int _cursorIndexOfTaskActivity = CursorUtil.getColumnIndexOrThrow(_cursor, "taskActivity");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final List<TemiTask> _result = new ArrayList<TemiTask>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final TemiTask _item;
        final int _tmpTaskId;
        _tmpTaskId = _cursor.getInt(_cursorIndexOfTaskId);
        final int _tmpHour;
        _tmpHour = _cursor.getInt(_cursorIndexOfHour);
        final int _tmpMinute;
        _tmpMinute = _cursor.getInt(_cursorIndexOfMinute);
        final boolean _tmpStarted;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfStarted);
        _tmpStarted = _tmp != 0;
        final boolean _tmpRecurring;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfRecurring);
        _tmpRecurring = _tmp_1 != 0;
        final boolean _tmpMonday;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfMonday);
        _tmpMonday = _tmp_2 != 0;
        final boolean _tmpTuesday;
        final int _tmp_3;
        _tmp_3 = _cursor.getInt(_cursorIndexOfTuesday);
        _tmpTuesday = _tmp_3 != 0;
        final boolean _tmpWednesday;
        final int _tmp_4;
        _tmp_4 = _cursor.getInt(_cursorIndexOfWednesday);
        _tmpWednesday = _tmp_4 != 0;
        final boolean _tmpThursday;
        final int _tmp_5;
        _tmp_5 = _cursor.getInt(_cursorIndexOfThursday);
        _tmpThursday = _tmp_5 != 0;
        final boolean _tmpFriday;
        final int _tmp_6;
        _tmp_6 = _cursor.getInt(_cursorIndexOfFriday);
        _tmpFriday = _tmp_6 != 0;
        final boolean _tmpSaturday;
        final int _tmp_7;
        _tmp_7 = _cursor.getInt(_cursorIndexOfSaturday);
        _tmpSaturday = _tmp_7 != 0;
        final boolean _tmpSunday;
        final int _tmp_8;
        _tmp_8 = _cursor.getInt(_cursorIndexOfSunday);
        _tmpSunday = _tmp_8 != 0;
        final String _tmpTaskName;
        _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
        final String _tmpTaskActivity;
        _tmpTaskActivity = _cursor.getString(_cursorIndexOfTaskActivity);
        final String _tmpLocation;
        _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        final long _tmpCreated;
        _tmpCreated = _cursor.getLong(_cursorIndexOfCreated);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item = new TemiTask(_tmpTaskId,_tmpHour,_tmpMinute,_tmpTaskName,_tmpCreated,_tmpStarted,_tmpRecurring,_tmpMonday,_tmpTuesday,_tmpWednesday,_tmpThursday,_tmpFriday,_tmpSaturday,_tmpSunday,_tmpUserId,_tmpTaskActivity,_tmpLocation);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
