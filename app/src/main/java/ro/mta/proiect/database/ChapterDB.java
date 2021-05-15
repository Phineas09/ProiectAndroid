package ro.mta.proiect.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Chapter.class}, version = 1, exportSchema = false)
public abstract class ChapterDB extends RoomDatabase {

    private final static String DB_NAME = "chapters.db";
    private static ChapterDB instance;

    public static ChapterDB getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context, ChapterDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract ChapterDao getChapterDao();

}
