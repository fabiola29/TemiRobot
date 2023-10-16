package com.example.temi1.db.TaskTemi;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TemiTaskRoomDatabase_Impl extends TemiTaskRoomDatabase {
  private volatile TemiTaskDao _temiTaskDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `task_table` (`taskId` INTEGER NOT NULL, `hour` INTEGER NOT NULL, `minute` INTEGER NOT NULL, `started` INTEGER NOT NULL, `recurring` INTEGER NOT NULL, `monday` INTEGER NOT NULL, `tuesday` INTEGER NOT NULL, `wednesday` INTEGER NOT NULL, `thursday` INTEGER NOT NULL, `friday` INTEGER NOT NULL, `saturday` INTEGER NOT NULL, `sunday` INTEGER NOT NULL, `taskName` TEXT, `taskActivity` TEXT, `location` TEXT, `created` INTEGER NOT NULL, `userId` INTEGER NOT NULL, PRIMARY KEY(`taskId`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ec5283f5ea2269fa8aa397452955a937')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `task_table`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTaskTable = new HashMap<String, TableInfo.Column>(17);
        _columnsTaskTable.put("taskId", new TableInfo.Column("taskId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("hour", new TableInfo.Column("hour", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("minute", new TableInfo.Column("minute", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("started", new TableInfo.Column("started", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("recurring", new TableInfo.Column("recurring", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("monday", new TableInfo.Column("monday", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("tuesday", new TableInfo.Column("tuesday", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("wednesday", new TableInfo.Column("wednesday", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("thursday", new TableInfo.Column("thursday", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("friday", new TableInfo.Column("friday", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("saturday", new TableInfo.Column("saturday", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("sunday", new TableInfo.Column("sunday", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("taskName", new TableInfo.Column("taskName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("taskActivity", new TableInfo.Column("taskActivity", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("created", new TableInfo.Column("created", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTaskTable.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTaskTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTaskTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTaskTable = new TableInfo("task_table", _columnsTaskTable, _foreignKeysTaskTable, _indicesTaskTable);
        final TableInfo _existingTaskTable = TableInfo.read(_db, "task_table");
        if (! _infoTaskTable.equals(_existingTaskTable)) {
          return new RoomOpenHelper.ValidationResult(false, "task_table(com.example.temi1.model.TemiTask).\n"
                  + " Expected:\n" + _infoTaskTable + "\n"
                  + " Found:\n" + _existingTaskTable);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ec5283f5ea2269fa8aa397452955a937", "da9c7de5d632f503d44ee53ac36842c9");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "task_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `task_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public TemiTaskDao taskDao() {
    if (_temiTaskDao != null) {
      return _temiTaskDao;
    } else {
      synchronized(this) {
        if(_temiTaskDao == null) {
          _temiTaskDao = new TemiTaskDao_Impl(this);
        }
        return _temiTaskDao;
      }
    }
  }
}
