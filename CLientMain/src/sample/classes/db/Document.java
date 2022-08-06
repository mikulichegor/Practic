package sample.classes.db;

import java.io.Serializable;

public class Document implements Serializable {

    private int id;

    private String title;

    private String date;


    public Document(String title, String date) {
        this.title = title;
        this.date = date;

    }
    public Document(int id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
