package pl.wat.tim.mobile.infrastructure.persistence;

import java.time.LocalDateTime;

import androidx.room.TypeConverter;

public class DateConventer {

    @TypeConverter
    public static LocalDateTime toLocalDateTime(String date) {
        return date == null ? null : LocalDateTime.parse(date);
    }

    @TypeConverter
    public static String fromLocalDateTime(LocalDateTime date) {
        return date == null ? null : date.toString();
    }
}
