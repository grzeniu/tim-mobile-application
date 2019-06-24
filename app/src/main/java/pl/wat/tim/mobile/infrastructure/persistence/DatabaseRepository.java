package pl.wat.tim.mobile.infrastructure.persistence;

import android.content.Context;
import android.os.AsyncTask;

import pl.wat.tim.mobile.model.User;

public class DatabaseRepository {

    private UserDao userDao;

    public DatabaseRepository(Context context) {
        this.userDao = AppDatabase.getAppDatabase(context).userDao();
    }

    public void addUser(User user) {
        new AddUserAsyncTask(userDao).execute(user);
    }

    private static class AddUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        AddUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... params) {
            userDao.insert(params);
            return null;
        }
    }
}
