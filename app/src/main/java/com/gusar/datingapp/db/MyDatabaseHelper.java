package com.gusar.datingapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.gusar.datingapp.model.ModelPerson;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

public class MyDatabaseHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "soul_test_dev2.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<ModelPerson, Integer> simpleDao = null;
    private RuntimeExceptionDao<ModelPerson, Integer> simpleRuntimeDao = null;

    private MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static volatile MyDatabaseHelper instance;

    public static MyDatabaseHelper getInstance(Context context) {
        MyDatabaseHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (MyDatabaseHelper.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MyDatabaseHelper(context);
                }
            }
        }
        return localInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(MyDatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, ModelPerson.class);
        } catch (SQLException e) {
            Log.e(MyDatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(MyDatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, ModelPerson.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(MyDatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<ModelPerson, Integer> getDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(ModelPerson.class);
        }
        return simpleDao;
    }

    public RuntimeExceptionDao<ModelPerson, Integer> getPersonDataDao() {
        if (simpleRuntimeDao == null) {
            simpleRuntimeDao = getRuntimeExceptionDao(ModelPerson.class);
        }
        return simpleRuntimeDao;
    }

    @Override
    public void close() {
        super.close();
        simpleDao = null;
        simpleRuntimeDao = null;
    }
}


