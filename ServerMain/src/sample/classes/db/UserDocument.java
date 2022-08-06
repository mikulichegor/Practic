package sample.classes.db;

import java.io.Serializable;

public class UserDocument implements Serializable {


    public UserDocument(int user_id, int document_id, String confirmDir, String confirmAcc) {
        this.user_id = user_id;
        this.document_id = document_id;
        this.confirmDir = confirmDir;
        this.confirmAcc = confirmAcc;
    }

    private int user_id;

    private int document_id;

    public String getConfirmDir() {
        return confirmDir;
    }

    public void setConfirmDir(String confirmDir) {
        this.confirmDir = confirmDir;
    }

    public String getConfirmAcc() {
        return confirmAcc;
    }

    public void setConfirmAcc(String confirmAcc) {
        this.confirmAcc = confirmAcc;
    }

    private String confirmDir;

    private String confirmAcc;



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDocument_id() {
        return document_id;
    }

    public void setDocument_id(int document_id) {
        this.document_id = document_id;
    }



    @Override
    public String toString() {
        return "UserDocument{" +
                "user_id=" + user_id +
                ", document_id=" + document_id +
                ", confirmDir='" + confirmDir + '\'' +
                ", confirmAcc='" + confirmAcc + '\'' +
                '}';
    }


}
