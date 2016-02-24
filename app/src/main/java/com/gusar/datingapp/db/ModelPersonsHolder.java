package com.gusar.datingapp.db;

import android.content.Context;
import com.gusar.datingapp.interfaces.MyPersonsCallback;
import com.gusar.datingapp.model.ModelPerson;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.testpackage.test_sdk.android.testlib.db.PersonsHolder;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import bolts.Continuation;
import bolts.Task;

public class ModelPersonsHolder {

    private Context context;
    RuntimeExceptionDao<ModelPerson, Integer> dao;

    public RuntimeExceptionDao<ModelPerson, Integer> getDao() {
        return dao;
    }

    public ModelPersonsHolder(Context context) {
        this.context = context;
    }

    public void savePersons(List<ModelPerson> persons) {
        for (ModelPerson p : persons) {
            savePerson(p);
        }
    }

    public void savePerson(ModelPerson p) {
        dao = MyDatabaseHelper.getInstance(context).getPersonDataDao();
        dao.createOrUpdate(p);
    }

    public ModelPerson getPersonById(int id) {
        RuntimeExceptionDao<ModelPerson, Integer> dao = MyDatabaseHelper.getInstance(context).getPersonDataDao();
        QueryBuilder qb = dao.queryBuilder();
        ModelPerson person = null;
        try {
            person = (ModelPerson) qb.where().eq("id", id).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return person;
    }

    public void dropAllPersons(final PersonsDropCallback callback) {
        getAllPersons(new MyPersonsCallback() {
            @Override
            public void onResult(List<ModelPerson> persons) {
                RuntimeExceptionDao<ModelPerson, Integer> dao = MyDatabaseHelper.getInstance(context).getPersonDataDao();
                dao.delete(persons);
                callback.onSuccess();
            }
        });
    }

    public void getAllPersons(final MyPersonsCallback callback) {
        Task.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(4000);
                return null;
            }
        }).onSuccess(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                RuntimeExceptionDao<ModelPerson, Integer> dao = MyDatabaseHelper.getInstance(context).getPersonDataDao();
                callback.onResult(dao.queryForAll());
                return null;
            }
        });
    }

    public void getPortionPersons(final int offset, final int limit, final MyPersonsCallback callback) {
        Task.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(4000);
                return null;
            }
        }).onSuccess(new Continuation<Object, Object>() {
            @Override
            public Object then(Task<Object> task) throws Exception {
                RuntimeExceptionDao<ModelPerson, Integer> dao = MyDatabaseHelper.getInstance(context).getPersonDataDao();
                callback.onResult(dao.queryBuilder().offset(offset).limit(limit).query());
                return null;
            }
        });
    }

    public interface PersonsDropCallback {
        void onSuccess();
    }
}
