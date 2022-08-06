package sample.classes.db;

import java.io.Serializable;

public class Vacation implements Serializable {

    private int id;

    private int userId;

    private int basic;

    private int daysLeft;

    private int daysGot;

    public Vacation(int id, int userId, int basic, int daysLeft, int daysGot) {
        this.id = id;
        this.userId = userId;
        this.basic = basic;
        this.daysLeft = daysLeft;
        this.daysGot = daysGot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBasic() {
        return basic;
    }

    public void setBasic(int basic) {
        this.basic = basic;
    }

    public int getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(int daysLeft) {
        this.daysLeft = daysLeft;
    }

    public int getDaysGot() {
        return daysGot;
    }

    public void setDaysGot(int daysGot) {
        this.daysGot = daysGot;
    }

    @Override
    public String toString() {
        return "Vacation{" +
                "id=" + id +
                ", userId=" + userId +
                ", basic=" + basic +
                ", daysLeft=" + daysLeft +
                ", daysGot=" + daysGot +
                '}';
    }
}
