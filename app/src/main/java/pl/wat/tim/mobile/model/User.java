package pl.wat.tim.mobile.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.wat.tim.mobile.infrastructure.persistence.DateConventer;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "user")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @ColumnInfo(name = "username")
    private String username;

    @Ignore
    private String token;

    @TypeConverters(DateConventer.class)
    private LocalDateTime dateOfLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getDateOfLogin() {
        return dateOfLogin;
    }

    public void setDateOfLogin(LocalDateTime dateOfLogin) {
        this.dateOfLogin = dateOfLogin;
    }
}

