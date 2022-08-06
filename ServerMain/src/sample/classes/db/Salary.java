package sample.classes.db;

import java.io.Serializable;

public class Salary implements Serializable {

    private int id;

    private int userId;

    private int basic;

    private int bonus;

    private int pension;

    public Salary(int basic, int bonus, int pension) {

        this.basic = basic;
        this.bonus = bonus;
        this.pension = pension;
    }
    public Salary(int id, int userId, int basic, int bonus, int pension) {
        this.id = id;
        this.userId = userId;
        this.basic = basic;
        this.bonus = bonus;
        this.pension = pension;
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

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getPension() {
        return pension;
    }

    public void setPension(int pension) {
        this.pension = pension;
    }

    @Override
    public String toString() {
        return "Salary{" +
                "id=" + id +
                ", userId=" + userId +
                ", basic=" + basic +
                ", bonus=" + bonus +
                ", pension=" + pension +
                '}';
    }
}
