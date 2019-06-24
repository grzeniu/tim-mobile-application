package pl.wat.tim.mobile.infrastructure.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import pl.wat.tim.mobile.model.User;

@Dao
public interface UserDao {

    @Insert
    void insert(User... users);
}
