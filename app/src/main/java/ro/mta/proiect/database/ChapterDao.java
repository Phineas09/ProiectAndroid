package ro.mta.proiect.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChapter(Chapter chapter);

    @Query("select * from chapters")
    List<Chapter> getAllChapters();

    @Query("select * from chapters where chapterName =:chapterName")
    List<Chapter> getByChapterName(String chapterName);

    @Query("select * from chapters where id =:chapterId")
    Chapter getChapterByKey(long chapterId);

    @Update
    void updateChapter(Chapter chapter);

}
