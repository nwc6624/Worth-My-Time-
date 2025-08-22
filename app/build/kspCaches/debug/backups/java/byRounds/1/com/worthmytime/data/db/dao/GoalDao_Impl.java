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
import com.worthmytime.data.db.entities.GoalEntity;
import com.worthmytime.domain.model.GoalCategory;
import java.lang.Class;
import java.lang.Double;
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
public final class GoalDao_Impl implements GoalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GoalEntity> __insertionAdapterOfGoalEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<GoalEntity> __deletionAdapterOfGoalEntity;

  private final EntityDeletionOrUpdateAdapter<GoalEntity> __updateAdapterOfGoalEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSavedDollars;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTaxSnapshot;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public GoalDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGoalEntity = new EntityInsertionAdapter<GoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `goals` (`id`,`label`,`price`,`category`,`savedDollars`,`createdAt`,`useTaxSnapshot`,`salesTaxPctAtCreation`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GoalEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getLabel());
        statement.bindDouble(3, entity.getPrice());
        final String _tmp = __converters.fromGoalCategory(entity.getCategory());
        statement.bindString(4, _tmp);
        statement.bindDouble(5, entity.getSavedDollars());
        statement.bindLong(6, entity.getCreatedAt());
        final int _tmp_1 = entity.getUseTaxSnapshot() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.getSalesTaxPctAtCreation() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getSalesTaxPctAtCreation());
        }
      }
    };
    this.__deletionAdapterOfGoalEntity = new EntityDeletionOrUpdateAdapter<GoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `goals` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GoalEntity entity) {
        statement.bindString(1, entity.getId());
      }
    };
    this.__updateAdapterOfGoalEntity = new EntityDeletionOrUpdateAdapter<GoalEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `goals` SET `id` = ?,`label` = ?,`price` = ?,`category` = ?,`savedDollars` = ?,`createdAt` = ?,`useTaxSnapshot` = ?,`salesTaxPctAtCreation` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GoalEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getLabel());
        statement.bindDouble(3, entity.getPrice());
        final String _tmp = __converters.fromGoalCategory(entity.getCategory());
        statement.bindString(4, _tmp);
        statement.bindDouble(5, entity.getSavedDollars());
        statement.bindLong(6, entity.getCreatedAt());
        final int _tmp_1 = entity.getUseTaxSnapshot() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        if (entity.getSalesTaxPctAtCreation() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getSalesTaxPctAtCreation());
        }
        statement.bindString(9, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateSavedDollars = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE goals SET savedDollars = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTaxSnapshot = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE goals SET useTaxSnapshot = ?, salesTaxPctAtCreation = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM goals WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final GoalEntity goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGoalEntity.insert(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final GoalEntity goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGoalEntity.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final GoalEntity goal, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfGoalEntity.handle(goal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateSavedDollars(final String goalId, final double savedDollars,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSavedDollars.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, savedDollars);
        _argIndex = 2;
        _stmt.bindString(_argIndex, goalId);
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
          __preparedStmtOfUpdateSavedDollars.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTaxSnapshot(final String goalId, final boolean useTaxSnapshot,
      final Double salesTaxPct, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTaxSnapshot.acquire();
        int _argIndex = 1;
        final int _tmp = useTaxSnapshot ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        if (salesTaxPct == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindDouble(_argIndex, salesTaxPct);
        }
        _argIndex = 3;
        _stmt.bindString(_argIndex, goalId);
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
          __preparedStmtOfUpdateTaxSnapshot.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String goalId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, goalId);
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
  public Flow<List<GoalEntity>> observeGoals() {
    final String _sql = "SELECT * FROM goals ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"goals"}, new Callable<List<GoalEntity>>() {
      @Override
      @NonNull
      public List<GoalEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfSavedDollars = CursorUtil.getColumnIndexOrThrow(_cursor, "savedDollars");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUseTaxSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "useTaxSnapshot");
          final int _cursorIndexOfSalesTaxPctAtCreation = CursorUtil.getColumnIndexOrThrow(_cursor, "salesTaxPctAtCreation");
          final List<GoalEntity> _result = new ArrayList<GoalEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GoalEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpLabel;
            _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final GoalCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toGoalCategory(_tmp);
            final double _tmpSavedDollars;
            _tmpSavedDollars = _cursor.getDouble(_cursorIndexOfSavedDollars);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final boolean _tmpUseTaxSnapshot;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfUseTaxSnapshot);
            _tmpUseTaxSnapshot = _tmp_1 != 0;
            final Double _tmpSalesTaxPctAtCreation;
            if (_cursor.isNull(_cursorIndexOfSalesTaxPctAtCreation)) {
              _tmpSalesTaxPctAtCreation = null;
            } else {
              _tmpSalesTaxPctAtCreation = _cursor.getDouble(_cursorIndexOfSalesTaxPctAtCreation);
            }
            _item = new GoalEntity(_tmpId,_tmpLabel,_tmpPrice,_tmpCategory,_tmpSavedDollars,_tmpCreatedAt,_tmpUseTaxSnapshot,_tmpSalesTaxPctAtCreation);
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
