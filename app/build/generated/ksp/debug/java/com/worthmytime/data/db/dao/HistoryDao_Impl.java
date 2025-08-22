package com.worthmytime.data.db.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.worthmytime.data.db.Converters;
import com.worthmytime.data.db.entities.HistoryEntity;
import com.worthmytime.domain.model.Decision;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HistoryDao_Impl implements HistoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HistoryEntity> __insertionAdapterOfHistoryEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<HistoryEntity> __deletionAdapterOfHistoryEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDecision;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public HistoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHistoryEntity = new EntityInsertionAdapter<HistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `history` (`id`,`priceBeforeTax`,`salesTaxPctAtCheck`,`priceWithTax`,`netHourlyAtCheck`,`hours`,`workdays`,`workweeks`,`decision`,`createdAt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoryEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindDouble(2, entity.getPriceBeforeTax());
        statement.bindDouble(3, entity.getSalesTaxPctAtCheck());
        statement.bindDouble(4, entity.getPriceWithTax());
        statement.bindDouble(5, entity.getNetHourlyAtCheck());
        statement.bindDouble(6, entity.getHours());
        statement.bindDouble(7, entity.getWorkdays());
        statement.bindDouble(8, entity.getWorkweeks());
        final String _tmp = __converters.fromDecision(entity.getDecision());
        if (_tmp == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp);
        }
        statement.bindLong(10, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfHistoryEntity = new EntityDeletionOrUpdateAdapter<HistoryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `history` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoryEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateDecision = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE history SET decision = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM history WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM history";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final HistoryEntity historyItem,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHistoryEntity.insert(historyItem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final HistoryEntity historyItem,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHistoryEntity.handle(historyItem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDecision(final String historyId, final String decision,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDecision.acquire();
        int _argIndex = 1;
        if (decision == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, decision);
        }
        _argIndex = 2;
        _stmt.bindString(_argIndex, historyId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateDecision.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String historyId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, historyId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HistoryEntity>> observeRecent(final int limit) {
    final String _sql = "SELECT * FROM history ORDER BY createdAt DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"history"}, new Callable<List<HistoryEntity>>() {
      @Override
      @NonNull
      public List<HistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPriceBeforeTax = CursorUtil.getColumnIndexOrThrow(_cursor, "priceBeforeTax");
          final int _cursorIndexOfSalesTaxPctAtCheck = CursorUtil.getColumnIndexOrThrow(_cursor, "salesTaxPctAtCheck");
          final int _cursorIndexOfPriceWithTax = CursorUtil.getColumnIndexOrThrow(_cursor, "priceWithTax");
          final int _cursorIndexOfNetHourlyAtCheck = CursorUtil.getColumnIndexOrThrow(_cursor, "netHourlyAtCheck");
          final int _cursorIndexOfHours = CursorUtil.getColumnIndexOrThrow(_cursor, "hours");
          final int _cursorIndexOfWorkdays = CursorUtil.getColumnIndexOrThrow(_cursor, "workdays");
          final int _cursorIndexOfWorkweeks = CursorUtil.getColumnIndexOrThrow(_cursor, "workweeks");
          final int _cursorIndexOfDecision = CursorUtil.getColumnIndexOrThrow(_cursor, "decision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<HistoryEntity> _result = new ArrayList<HistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoryEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final double _tmpPriceBeforeTax;
            _tmpPriceBeforeTax = _cursor.getDouble(_cursorIndexOfPriceBeforeTax);
            final double _tmpSalesTaxPctAtCheck;
            _tmpSalesTaxPctAtCheck = _cursor.getDouble(_cursorIndexOfSalesTaxPctAtCheck);
            final double _tmpPriceWithTax;
            _tmpPriceWithTax = _cursor.getDouble(_cursorIndexOfPriceWithTax);
            final double _tmpNetHourlyAtCheck;
            _tmpNetHourlyAtCheck = _cursor.getDouble(_cursorIndexOfNetHourlyAtCheck);
            final double _tmpHours;
            _tmpHours = _cursor.getDouble(_cursorIndexOfHours);
            final double _tmpWorkdays;
            _tmpWorkdays = _cursor.getDouble(_cursorIndexOfWorkdays);
            final double _tmpWorkweeks;
            _tmpWorkweeks = _cursor.getDouble(_cursorIndexOfWorkweeks);
            final Decision _tmpDecision;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfDecision)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfDecision);
            }
            _tmpDecision = __converters.toDecision(_tmp);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new HistoryEntity(_tmpId,_tmpPriceBeforeTax,_tmpSalesTaxPctAtCheck,_tmpPriceWithTax,_tmpNetHourlyAtCheck,_tmpHours,_tmpWorkdays,_tmpWorkweeks,_tmpDecision,_tmpCreatedAt);
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
  public Flow<List<HistoryEntity>> observeAll() {
    final String _sql = "SELECT * FROM history ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"history"}, new Callable<List<HistoryEntity>>() {
      @Override
      @NonNull
      public List<HistoryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPriceBeforeTax = CursorUtil.getColumnIndexOrThrow(_cursor, "priceBeforeTax");
          final int _cursorIndexOfSalesTaxPctAtCheck = CursorUtil.getColumnIndexOrThrow(_cursor, "salesTaxPctAtCheck");
          final int _cursorIndexOfPriceWithTax = CursorUtil.getColumnIndexOrThrow(_cursor, "priceWithTax");
          final int _cursorIndexOfNetHourlyAtCheck = CursorUtil.getColumnIndexOrThrow(_cursor, "netHourlyAtCheck");
          final int _cursorIndexOfHours = CursorUtil.getColumnIndexOrThrow(_cursor, "hours");
          final int _cursorIndexOfWorkdays = CursorUtil.getColumnIndexOrThrow(_cursor, "workdays");
          final int _cursorIndexOfWorkweeks = CursorUtil.getColumnIndexOrThrow(_cursor, "workweeks");
          final int _cursorIndexOfDecision = CursorUtil.getColumnIndexOrThrow(_cursor, "decision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<HistoryEntity> _result = new ArrayList<HistoryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoryEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final double _tmpPriceBeforeTax;
            _tmpPriceBeforeTax = _cursor.getDouble(_cursorIndexOfPriceBeforeTax);
            final double _tmpSalesTaxPctAtCheck;
            _tmpSalesTaxPctAtCheck = _cursor.getDouble(_cursorIndexOfSalesTaxPctAtCheck);
            final double _tmpPriceWithTax;
            _tmpPriceWithTax = _cursor.getDouble(_cursorIndexOfPriceWithTax);
            final double _tmpNetHourlyAtCheck;
            _tmpNetHourlyAtCheck = _cursor.getDouble(_cursorIndexOfNetHourlyAtCheck);
            final double _tmpHours;
            _tmpHours = _cursor.getDouble(_cursorIndexOfHours);
            final double _tmpWorkdays;
            _tmpWorkdays = _cursor.getDouble(_cursorIndexOfWorkdays);
            final double _tmpWorkweeks;
            _tmpWorkweeks = _cursor.getDouble(_cursorIndexOfWorkweeks);
            final Decision _tmpDecision;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfDecision)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfDecision);
            }
            _tmpDecision = __converters.toDecision(_tmp);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new HistoryEntity(_tmpId,_tmpPriceBeforeTax,_tmpSalesTaxPctAtCheck,_tmpPriceWithTax,_tmpNetHourlyAtCheck,_tmpHours,_tmpWorkdays,_tmpWorkweeks,_tmpDecision,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
