package ro.mta.proiect.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Chapters")
public class Chapter implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int version;
    private String chapterName;
    private String chapterImage;
    private String chapterContent;

    public Chapter(String chapterName, String chapterImage, String chapterContent, int version) {
        this.chapterName = chapterName;
        this.chapterImage = chapterImage;
        this.chapterContent = chapterContent;
        this.version = version;
    }

    @Ignore
    public Chapter(int id, int version, String chapterName, String chapterImage, String chapterContent) {
        this.id = id;
        this.chapterName = chapterName;
        this.chapterImage = chapterImage;
        this.chapterContent = chapterContent;
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterImage() {
        return chapterImage;
    }

    public void setChapterImage(String chapterImage) {
        this.chapterImage = chapterImage;
    }

    public String getChapterContent() {
        return chapterContent;
    }

    public void setChapterContent(String chapterContent) {
        this.chapterContent = chapterContent;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "chapterName='" + chapterName + '\'' +
                ", chapterImage='" + chapterImage + '\'' +
                ", chapterContent='" + chapterContent + '\'' +
                '}';
    }
}
