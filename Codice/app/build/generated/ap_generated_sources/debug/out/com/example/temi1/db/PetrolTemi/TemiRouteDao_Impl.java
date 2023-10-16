package com.example.temi1.db.PetrolTemi;

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
import com.example.temi1.db.TypeConverter;
import com.example.temi1.model.TemiRoute;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TemiRouteDao_Impl implements TemiRouteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TemiRoute> __insertionAdapterOfTemiRoute;

  private final EntityDeletionOrUpdateAdapter<TemiRoute> __deletionAdapterOfTemiRoute;

  private final EntityDeletionOrUpdateAdapter<TemiRoute> __updateAdapterOfTemiRoute;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TemiRouteDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTemiRoute = new EntityInsertionAdapter<TemiRoute>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `routeTable` (`routeIdx`,`routeTitle`,`patrolCount`,`destinations`,`userId`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TemiRoute value) {
        stmt.bindLong(1, value.getRouteIdx());
        if (value.getRouteTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getRouteTitle());
        }
        stmt.bindLong(3, value.getPatrolCount());
        final String _tmp;
        _tmp = TypeConverter.inputListToString(value.getDestinations());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp);
        }
        stmt.bindLong(5, value.getUserId());
      }
    };
    this.__deletionAdapterOfTemiRoute = new EntityDeletionOrUpdateAdapter<TemiRoute>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `routeTable` WHERE `routeIdx` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TemiRoute value) {
        stmt.bindLong(1, value.getRouteIdx());
      }
    };
    this.__updateAdapterOfTemiRoute = new EntityDeletionOrUpdateAdapter<TemiRoute>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `routeTable` SET `routeIdx` = ?,`routeTitle` = ?,`patrolCount` = ?,`destinations` = ?,`userId` = ? WHERE `routeIdx` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TemiRoute value) {
        stmt.bindLong(1, value.getRouteIdx());
        if (value.getRouteTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getRouteTitle());
        }
        stmt.bindLong(3, value.getPatrolCount());
        final String _tmp;
        _tmp = TypeConverter.inputListToString(value.getDestinations());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp);
        }
        stmt.bindLong(5, value.getUserId());
        stmt.bindLong(6, value.getRouteIdx());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from routeTable";
        return _query;
      }
    };
  }

  @Override
  public void insertRouteIntoDb(final TemiRoute route) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTemiRoute.insert(route);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteRoute(final TemiRoute temiRoute) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTemiRoute.handle(temiRoute);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateRoute(final TemiRoute updatedRoute) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTemiRoute.handle(updatedRoute);
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
  public LiveData<List<TemiRoute>> getRoutesFromDb() {
    final String _sql = "SELECT * FROM routeTable ORDER BY routeIdx ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"routeTable"}, false, new Callable<List<TemiRoute>>() {
      @Override
      public List<TemiRoute> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRouteIdx = CursorUtil.getColumnIndexOrThrow(_cursor, "routeIdx");
          final int _cursorIndexOfRouteTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "routeTitle");
          final int _cursorIndexOfPatrolCount = CursorUtil.getColumnIndexOrThrow(_cursor, "patrolCount");
          final int _cursorIndexOfDestinations = CursorUtil.getColumnIndexOrThrow(_cursor, "destinations");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final List<TemiRoute> _result = new ArrayList<TemiRoute>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TemiRoute _item;
            final int _tmpRouteIdx;
            _tmpRouteIdx = _cursor.getInt(_cursorIndexOfRouteIdx);
            final String _tmpRouteTitle;
            _tmpRouteTitle = _cursor.getString(_cursorIndexOfRouteTitle);
            final int _tmpPatrolCount;
            _tmpPatrolCount = _cursor.getInt(_cursorIndexOfPatrolCount);
            final ArrayList<String> _tmpDestinations;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfDestinations);
            _tmpDestinations = TypeConverter.stringToInputList(_tmp);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            _item = new TemiRoute(_tmpRouteIdx,_tmpRouteTitle,_tmpDestinations,_tmpPatrolCount,_tmpUserId);
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
}
