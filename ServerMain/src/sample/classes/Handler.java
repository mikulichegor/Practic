package sample.classes;


import sample.classes.db.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Handler {
    public static Connection getDbConnection() throws SQLException, IOException, ClassNotFoundException{
        Connection dbConnection;
        String connection = "jdbc:mysql://localhost:3306/company";
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connection,"root","qwertydb");
        return dbConnection;
    }
    public ArrayList show(String table) throws SQLException, IOException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Salary> salaries = new ArrayList<>();
        ArrayList<Document> documents = new ArrayList<>();
        ArrayList<Vacation> vacations = new ArrayList<>();
        ArrayList<UserDocument> userDocuments = new ArrayList<>();

        Statement preparedSt = getDbConnection().createStatement();
        ResultSet set = preparedSt.executeQuery("SELECT * FROM " + table);

        switch (table){

            case "user" -> {

                while (set.next()){
                    User user = new User(set.getInt(1), set.getString(2),
                            set.getString(3), set.getInt(4),
                            set.getString(5), set.getString(6), set.getString(7));
                    users.add(user);
                }
                return users;
            }

            case "salary" -> {

                while (set.next()){
                    Salary salary = new Salary(set.getInt(1), set.getInt(2), set.getInt(3),
                            set.getInt(4),set.getInt(5));
                    salaries.add(salary);
                }
                return salaries;
            }

            case "document" -> {

                while (set.next()){
                    Document document = new Document(set.getInt(1),
                            set.getString(2), set.getString(3));
                    documents.add(document);
                }
                return documents;
            }

            case "vacation" -> {
                
                while (set.next()){
                    Vacation vacation = new Vacation(set.getInt(1), set.getInt(2),
                            set.getInt(3), set.getInt(4), set.getInt(5));
                    vacations.add(vacation);
                }
                return vacations;
            }

            case "user_document" -> {

                while (set.next()){
                    UserDocument userDocument = new UserDocument(set.getInt(1), set.getInt(2), set.getString(3), set.getString(4));

                    userDocuments.add(userDocument);
                }
                return userDocuments;
            }

        }
        return null;
    }
/*

    public Object showOneNote(int id, String table) throws SQLException, IOException, ClassNotFoundException {
        Statement preparedSt = getDbConnection().createStatement();

        ResultSet set = preparedSt.executeQuery("SELECT * FROM " + table + " WHERE id = " + id);

        switch (table){
            case "jwls" -> {
                System.out.println("Вывод драгоценности на изменение.\n");
                Jwls jwls = new Jwls();

                while (set.next()){
                    jwls = new Jwls(set.getString(2));
                    System.out.println(jwls);
                }
                return jwls;
            }

            case "companies" -> {
                System.out.println("Вывод компании на изменение.\n");
                Companies companies = new Companies();
                while (set.next()){
                    companies = new Companies(set.getInt(1), set.getString(2));
                    System.out.println(companies);
                }
                return companies;
            }

            case "currencies " -> {
                System.out.println("Вывод валюты на изменение.\n");
                Currencies currencies = new Currencies();
                while (set.next()){
                    currencies = new Currencies(set.getInt(1), set.getString(2));
                    System.out.println(currencies);
                }
                return currencies;
            }

            case "users" -> {
                System.out.println("Вывод пользователя на изменение.\n");
                User users = new User();
                while (set.next()){
                    users = new User(set.getInt(1), set.getString(2), set.getString(3),
                            set.getInt(4), set.getString(5));
                    System.out.println(users);
                }
                return users;
            }

            case "refwitdhs" -> {
                System.out.println("Вывод пополнения счета/снятия средств на изменение.\n");
                RefWithd refWithds = new RefWithd();
                while (set.next()){
                    refWithds = new RefWithd(set.getInt(1), set.getInt(2), set.getString(3),
                            set.getInt(4), set.getString(5), set.getString(6), set.getInt(7));
                    System.out.println(refWithds);
                }
                return refWithds;
            }

            case "changes" -> {
                System.out.println("Вывод обмена валют на изменение.\n");
                Change change = new Change();
                while (set.next()){
                    change = new Change(set.getInt(1), set.getInt(2), set.getString(3),
                            set.getInt(4), set.getString(5), set.getString(6), set.getString(7));
                    System.out.println(change);
                }
                return change;
            }
            case "selljws" -> {
                System.out.println("Вывод продажи драгоценностей на изменение.\n");
                SellJw sellJw = new SellJw();
                while (set.next()){
                    sellJw = new SellJw(set.getInt(1), set.getInt(2), set.getString(3),
                            set.getInt(4), set.getString(5), set.getString(6), set.getInt(7));
                    System.out.println(sellJw);
                }
                return sellJw;
            }
            case "transacts" -> {
                System.out.println("Вывод перевода денег на изменение.\n");
                Transaction transaction = new Transaction();
                while (set.next()){
                    transaction = new Transaction(set.getInt(1), set.getInt(2), set.getString(3),
                            set.getInt(4), set.getInt(5), set.getInt(6), set.getString(7));
                    System.out.println(transaction);
                }
                return transaction;
            }
            case "stocks" -> {
                System.out.println("Вывод обмена валют изменение.\n");
                Stock stock = new Stock();
                while (set.next()){
                    stock = new Stock(set.getInt(1), set.getInt(2), set.getString(3),
                            set.getInt(4), set.getInt(5), set.getString(6), set.getInt(7));
                    System.out.println(stock);
                }
                return stock;
            }
        }

        return null;
    }

    public static String findUser(String request) throws SQLException, IOException, ClassNotFoundException {
        Statement preparedSt = getDbConnection().createStatement();
        ResultSet set = preparedSt.executeQuery("SELECT * FROM users");
        String[] arr = request.split("=");
        String res = "Неверный логин или пароль!";

        while (set.next()){
            if(Objects.equals(arr[0], set.getString(2))){
                if(Objects.equals(arr[1], set.getString(3))){
                    if(set.getInt(4) == 1){
                        res = "admin;" + set.getInt(1);
                        System.out.println(res);
                    } else {
                        res = "user;" + set.getInt(1);
                        System.out.println(res);
                    }
                }
            }
        }

        return res;
    }

    public void insertJwls(Jwls jwls) {
        try {
            Statement statement = getDbConnection().createStatement();
            statement.executeUpdate("INSERT INTO jwls(item)"
                    + " VALUES('" + jwls.getItem() + "');");
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertCompanies(Companies companies) {
        try {
            Statement statement = getDbConnection().createStatement();
            statement.executeUpdate("INSERT INTO companies(name)"
                    + " VALUES('" + companies.getName() + "');");
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertCurrencies(Currencies currencies) {
        try {
            Statement statement = getDbConnection().createStatement();
            statement.executeUpdate("INSERT INTO currencies(name)"
                    + " VALUES('" + currencies.getName() + "');");
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertRefWithds(RefWithd refWithd) {
        try {
            Statement statement = getDbConnection().createStatement();
            statement.executeUpdate("INSERT INTO refwithds(userid, date, sum, address, operation, cvv)"
                    + " VALUES('" + refWithd.getUserId() + "', " + refWithd.getDate() + ", '"
                    + refWithd.getSum() + "', '" + refWithd.getAddress() + "', '" + refWithd.getOperation() +
                    "', '" + refWithd.getCvv() +"');");
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertChanges(Change change) {
        try {
            Statement statement = getDbConnection().createStatement();
            statement.executeUpdate("INSERT INTO changes(userid, date, sum, address, curfrom, curto)"
                    + " VALUES('" + change.getUserId() + "', " + change.getDate() + ", '"
                    + change.getSum() + "', '" + change.getAddress() + "', '" + change.getCurrFrom() +
                    "', '" + change.getCurrTo() +"');");
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertSellJws(SellJw sellJw) {
        try {
            Statement statement = getDbConnection().createStatement();
            statement.executeUpdate("INSERT INTO selljws(userid, date, sum, address, item, weight)"
                    + " VALUES('" + sellJw.getUserId() + "', " + sellJw.getDate() + ", '"
                    + sellJw.getSum() + "', '" + sellJw.getAddress() + "', '" + sellJw.getItem() +
                    "', '" + sellJw.getWeight() +"');");
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void insertTransacts(Transaction transact) {
        try {
            Statement statement = getDbConnection().createStatement();
            String txtD = transact.getDate();
            System.out.println(transact.getDate());
            statement.executeUpdate("INSERT INTO transacts(userid, date, sum, cvv, otherid, comment)"
                    + " VALUES('" + transact.getUserId() + "', " + txtD + ", '"
                    + transact.getSum() + "', '" + transact.getCvv() + "', '" + transact.getOtherId() +
                    "', '" + transact.getComment() +"');");
            System.out.println(transact.getDate());
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void insertStocks(Stock stock) {
        try {
            Statement statement = getDbConnection().createStatement();
            statement.executeUpdate("INSERT INTO stocks(userid, date, sum, cvv, company, amount)"
                    + " VALUES('" + stock.getUserId() + "', " + stock.getDate() + ", '"
                    + stock.getSum() + "', '" + stock.getCvv() + "', '" + stock.getCompany() +
                    "', '" + stock.getAmount() +"');");
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void insertUser(User user) {
        try {
            Statement statement = getDbConnection().createStatement();
            statement.executeUpdate("INSERT INTO users(login, password, role, name)" + " VALUES('" + user.getLogin() +
                    "', '" + user.getPassword() + "', '" + user.getRole() + "', '" + user.getName() + "');");
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void delete(int idForDelete, String table) throws IOException {
        String delete = "DELETE FROM " + table + "\n" + "WHERE id = " + idForDelete;
        try {
            PreparedStatement preparedSt = getDbConnection().prepareStatement(delete);
            preparedSt.executeUpdate();

        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void update(Basic basic, String table) throws SQLException, IOException, ClassNotFoundException {
        String updatePart1 = null;
        String updatePart2;


        switch (table){
            case "refwitdhs" -> {
                String text1 = basic.getText1(), text2 = basic.getText2(), text3 = basic.getText3(),
                        text4 = basic.getText4(), text5 = basic.getText5();
                RefWithd refWithd = (RefWithd) showOneNote(basic.getId(), "refwitdhs");

                if(Objects.equals(text1, "=")) text1 = refWithd.getDate();
                if(Objects.equals(text2, "=")) text2 = Integer.toString(refWithd.getSum());
                if(Objects.equals(text3, "=")) text3 = refWithd.getAddress();
                if(Objects.equals(text4, "=")) text4 = refWithd.getOperation();
                if(Objects.equals(text5, "=")) text5 = Integer.toString(refWithd.getCvv());

                updatePart2 = " SET " + "date = '" + text1 + "', sum = " + Integer.parseInt(text2) +
                        ", address = '" + text3 + "', operation = '" + text4 + "', cvv = '" +
                        Integer.parseInt(text5) + "'";
                updatePart1 = "UPDATE " + table + updatePart2 + " WHERE id = " + basic.getId();

                System.out.println(updatePart1);
            }

            case "changes" -> {
                String text1 = basic.getText1(), text2 = basic.getText2(), text3 = basic.getText3(),
                        text4 = basic.getText4(), text5 = basic.getText5();
                Change change = (Change) showOneNote(basic.getId(), "changes");

                if(Objects.equals(text1, "=")) text1 = change.getDate();
                if(Objects.equals(text2, "=")) text2 = Integer.toString(change.getSum());
                if(Objects.equals(text3, "=")) text3 = change.getAddress();
                if(Objects.equals(text4, "=")) text4 = change.getCurrFrom();
                if(Objects.equals(text5, "=")) text5 = change.getCurrTo();

                updatePart2 = " SET " + "date = '" + text1 + "', sum = " + Integer.parseInt(text2) +
                        ", address = '" + text3 + "', curfrom = '" + text4 + "', curto = '" +
                        text5 + "'";
                updatePart1 = "UPDATE " + table + updatePart2 + " WHERE id = " + basic.getId();

                System.out.println(updatePart1);
            }

            case "selljws" -> {
                String text1 = basic.getText1(), text2 = basic.getText2(), text3 = basic.getText3(),
                        text4 = basic.getText4(), text5 = basic.getText5();
                SellJw sellJw = (SellJw) showOneNote(basic.getId(), "selljws");

                if(Objects.equals(text1, "=")) text1 = sellJw.getDate();
                if(Objects.equals(text2, "=")) text2 = Integer.toString(sellJw.getSum());
                if(Objects.equals(text3, "=")) text3 = sellJw.getAddress();
                if(Objects.equals(text4, "=")) text4 = sellJw.getItem();
                if(Objects.equals(text5, "=")) text5 = Integer.toString(sellJw.getWeight());

                updatePart2 = " SET " + "date = '" + text1 + "', sum = " + Integer.parseInt(text2) +
                        ", address = '" + text3 + "', item = '" + text4 + "', weight = '" +
                        Integer.parseInt(text5) + "'";
                updatePart1 = "UPDATE " + table + updatePart2 + " WHERE id = " + basic.getId();

                System.out.println(updatePart1);
            }

            case "transacts" -> {
                String text1 = basic.getText1(), text2 = basic.getText2(), text3 = basic.getText3(),
                        text4 = basic.getText4(), text5 = basic.getText5();
                Transaction transaction = (Transaction) showOneNote(basic.getId(), "transacts");

                if(Objects.equals(text1, "=")) text1 = transaction.getDate();
                if(Objects.equals(text2, "=")) text2 = Integer.toString(transaction.getSum());
                if(Objects.equals(text3, "=")) text3 = Integer.toString(transaction.getCvv());
                if(Objects.equals(text4, "=")) text4 = Integer.toString(transaction.getOtherId());
                if(Objects.equals(text5, "=")) text5 = transaction.getComment();

                updatePart2 = " SET " + "date = '" + text1 + "', sum = " + Integer.parseInt(text2) +
                        ", cvv = '" + Integer.parseInt(text3) + "', otherid = '" + Integer.parseInt(text4) + "', comment = '" +
                        text5 + "'";
                updatePart1 = "UPDATE " + table + updatePart2 + " WHERE id = " + basic.getId();

                System.out.println(updatePart1);
            }

            case "stocks" -> {
                String text1 = basic.getText1(), text2 = basic.getText2(), text3 = basic.getText3(),
                        text4 = basic.getText4(), text5 = basic.getText5();
                Stock stock = (Stock) showOneNote(basic.getId(), "stocks");

                if(Objects.equals(text1, "=")) text1 = stock.getDate();
                if(Objects.equals(text2, "=")) text2 = Integer.toString(stock.getSum());
                if(Objects.equals(text3, "=")) text3 = Integer.toString(stock.getCvv());
                if(Objects.equals(text4, "=")) text4 = stock.getCompany();
                if(Objects.equals(text5, "=")) text5 = Integer.toString(stock.getAmount());

                updatePart2 = " SET " + "date = '" + text1 + "', sum = " + Integer.parseInt(text2) +
                        ", cvv = '" + Integer.parseInt(text3) + "', company = '" + text4 + "', amount = '" +
                        Integer.parseInt(text5) + "'";
                updatePart1 = "UPDATE " + table + updatePart2 + " WHERE id = " + basic.getId();

                System.out.println(updatePart1);
            }

            case "jwls" -> {
                String text1 = basic.getText1();
                Jwls jwls = (Jwls) showOneNote(basic.getId(), "jwls");

                if(Objects.equals(text1, "=")) text1 = jwls.getItem();

                updatePart2 = " SET " + "item = '" + text1 +  "'";
                updatePart1 = "UPDATE " + table + updatePart2 + " WHERE id = " + basic.getId();

                System.out.println(updatePart1);
            }
            case "companies" -> {
                String text1 = basic.getText1();
                Companies companies = (Companies) showOneNote(basic.getId(), "companies");

                if(Objects.equals(text1, "=")) text1 = companies.getName();

                updatePart2 = " SET " + "name = '" + text1 +  "'";
                updatePart1 = "UPDATE " + table + updatePart2 + " WHERE id = " + basic.getId();

                System.out.println(updatePart1);
            }
            case "currencies" -> {
                String text1 = basic.getText1();
                Currencies currencies = (Currencies) showOneNote(basic.getId(), "currencies");

                if(Objects.equals(text1, "=")) text1 = currencies.getName();

                updatePart2 = " SET " + "name = '" + text1 +  "'";
                updatePart1 = "UPDATE " + table + updatePart2 + " WHERE id = " + basic.getId();

                System.out.println(updatePart1);
            }
            case "users" -> {
                String text1 = basic.getText1(), text2 = basic.getText2(), text3 = basic.getText3(),
                        text4 = basic.getText4();
                User user = (User) showOneNote(basic.getId(), "users");

                if(Objects.equals(text1, "=")) text1 = user.getLogin();
                if(Objects.equals(text2, "=")) text2 = user.getPassword();
                if(Objects.equals(text3, "=")) text3 = Integer.toString(user.getRole());
                if(Objects.equals(text4, "=")) text4 = user.getName();

                updatePart2 = " SET " + "login = '" + text1 + "', password = " + text2 +
                        ", role = '" + Integer.parseInt(text3) + "', name = '" + text4 + "'";
                updatePart1 = "UPDATE " + table + updatePart2 + " WHERE id = " + basic.getId();

                System.out.println(updatePart1);
            }
        }
        PreparedStatement preparedSt = getDbConnection().prepareStatement(updatePart1);
        preparedSt.executeUpdate();
    }

    public ArrayList search(Basic basic , String table) throws SQLException, IOException, ClassNotFoundException {
        ArrayList<User> users = new ArrayList<>();

        ArrayList<Jwls> jwls = new ArrayList<>();
        ArrayList<Companies> companies = new ArrayList<>();
        ArrayList<Currencies> currencies = new ArrayList<>();

        ArrayList<RefWithd> refWithds = new ArrayList<>();
        ArrayList<Change> changes = new ArrayList<>();
        ArrayList<SellJw> sellJws = new ArrayList<>();
        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<Stock> stocks = new ArrayList<>();

        int id = basic.getId();
        String sqlQuery = null, text = "";
        Statement preparedSt = getDbConnection().createStatement();

        if(id != 0) text += "id = " + id + " AND ";

        switch (table){
            case "refwidth" -> {
                if(!Objects.equals(basic.getId(), 0)) text += "id = '" + basic.getId() + "' AND ";
                if(!Objects.equals(basic.getText1(), "=")) text += "userid = '" + Integer.parseInt(basic.getText1()) + "' AND ";
                if(!Objects.equals(basic.getText2(), "=")) text += "date = '" + basic.getText2() + "' AND ";
                if(!Objects.equals(basic.getText3(), "=")) text += "sum = " + Integer.parseInt(basic.getText3()) + " AND ";
                if(!Objects.equals(basic.getText4(), "=")) text += "address = '" + basic.getText4() + "' AND ";
                if(!Objects.equals(basic.getText5(), "=")) text += "type = '" + basic.getText5() + "' AND ";
                if(!Objects.equals(basic.getText6(), "=")) text += "cvv = '" + Integer.parseInt(basic.getText6()) + "' AND ";

                text = text.substring(0, text.length() - 4);
                sqlQuery = "SELECT * FROM " + table + " WHERE " + text;
                System.out.println(sqlQuery);

                ResultSet set = preparedSt.executeQuery(sqlQuery);
                while (set.next()){
                    RefWithd item = new RefWithd(set.getInt(1), set.getInt(2), set.getString(3), set.getInt(4),
                            set.getString(5), set.getString(6), set.getInt(7));
                    refWithds.add(item);
                }

                return refWithds;
            }
            case "changes" -> {
                if(!Objects.equals(basic.getId(), 0)) text += "id = '" + basic.getId() + "' AND ";
                if(!Objects.equals(basic.getText1(), "=")) text += "userid = '" + Integer.parseInt(basic.getText1()) + "' AND ";
                if(!Objects.equals(basic.getText2(), "=")) text += "date = '" + basic.getText2() + "' AND ";
                if(!Objects.equals(basic.getText3(), "=")) text += "sum = " + Integer.parseInt(basic.getText3()) + " AND ";
                if(!Objects.equals(basic.getText4(), "=")) text += "address = '" + basic.getText4() + "' AND ";
                if(!Objects.equals(basic.getText5(), "=")) text += "curfrom = '" + basic.getText5() + "' AND ";
                if(!Objects.equals(basic.getText6(), "=")) text += "curto = '" + basic.getText6() + "' AND ";

                text = text.substring(0, text.length() - 4);
                sqlQuery = "SELECT * FROM " + table + " WHERE " + text;
                System.out.println(sqlQuery);

                ResultSet set = preparedSt.executeQuery(sqlQuery);
                while (set.next()){
                    Change item = new Change(set.getInt(1), set.getInt(2), set.getString(3), set.getInt(4),
                            set.getString(5), set.getString(6), set.getString(7));
                    changes.add(item);
                }

                return changes;
            }
            case "selljws" -> {
                if(!Objects.equals(basic.getId(), 0)) text += "id = '" + basic.getId() + "' AND ";
                if(!Objects.equals(basic.getText1(), "=")) text += "userid = '" + Integer.parseInt(basic.getText1()) + "' AND ";
                if(!Objects.equals(basic.getText2(), "=")) text += "date = '" + basic.getText2() + "' AND ";
                if(!Objects.equals(basic.getText3(), "=")) text += "sum = " + Integer.parseInt(basic.getText3()) + " AND ";
                if(!Objects.equals(basic.getText4(), "=")) text += "address = '" + basic.getText4() + "' AND ";
                if(!Objects.equals(basic.getText5(), "=")) text += "item = '" + basic.getText5() + "' AND ";
                if(!Objects.equals(basic.getText6(), "=")) text += "weight = '" + Integer.parseInt(basic.getText6()) + "' AND ";

                text = text.substring(0, text.length() - 4);
                sqlQuery = "SELECT * FROM " + table + " WHERE " + text;
                System.out.println(sqlQuery);

                ResultSet set = preparedSt.executeQuery(sqlQuery);
                while (set.next()){
                    SellJw item = new SellJw(set.getInt(1), set.getInt(2), set.getString(3), set.getInt(4),
                            set.getString(5), set.getString(6), set.getInt(7));
                    sellJws.add(item);
                }

                return sellJws;
            }
            case "transacts" -> {
                if(!Objects.equals(basic.getId(), 0)) text += "id = '" + basic.getId() + "' AND ";
                if(!Objects.equals(basic.getText1(), "=")) text += "userid = '" + Integer.parseInt(basic.getText1()) + "' AND ";
                if(!Objects.equals(basic.getText2(), "=")) text += "date = '" + basic.getText2() + "' AND ";
                if(!Objects.equals(basic.getText3(), "=")) text += "sum = " + Integer.parseInt(basic.getText3()) + " AND ";
                if(!Objects.equals(basic.getText4(), "=")) text += "cvv = '" + Integer.parseInt(basic.getText4()) + "' AND ";
                if(!Objects.equals(basic.getText5(), "=")) text += "otherid = '" + Integer.parseInt(basic.getText5()) + "' AND ";
                if(!Objects.equals(basic.getText6(), "=")) text += "comment = '" + basic.getText6() + "' AND ";

                text = text.substring(0, text.length() - 4);
                sqlQuery = "SELECT * FROM " + table + " WHERE " + text;
                System.out.println(sqlQuery);

                ResultSet set = preparedSt.executeQuery(sqlQuery);
                while (set.next()){
                    Transaction item = new Transaction(set.getInt(1), set.getInt(2), set.getString(3), set.getInt(4),
                            set.getInt(5), set.getInt(6), set.getString(7));
                    transactions.add(item);
                }

                return transactions;
            }
            case "stocks" -> {
                if(!Objects.equals(basic.getId(), 0)) text += "id = '" + basic.getId() + "' AND ";
                if(!Objects.equals(basic.getText1(), "=")) text += "userid = '" + Integer.parseInt(basic.getText1()) + "' AND ";
                if(!Objects.equals(basic.getText2(), "=")) text += "date = '" + basic.getText2() + "' AND ";
                if(!Objects.equals(basic.getText3(), "=")) text += "sum = " + Integer.parseInt(basic.getText3()) + " AND ";
                if(!Objects.equals(basic.getText4(), "=")) text += "cvv = '" + Integer.parseInt(basic.getText4()) + "' AND ";
                if(!Objects.equals(basic.getText5(), "=")) text += "company = '" + basic.getText5() + "' AND ";
                if(!Objects.equals(basic.getText6(), "=")) text += "amount = '" + Integer.parseInt(basic.getText6()) + "' AND ";

                text = text.substring(0, text.length() - 4);
                sqlQuery = "SELECT * FROM " + table + " WHERE " + text;
                System.out.println(sqlQuery);

                ResultSet set = preparedSt.executeQuery(sqlQuery);
                while (set.next()){
                    Stock item = new Stock(set.getInt(1), set.getInt(2), set.getString(3), set.getInt(4),
                            set.getInt(5), set.getString(6), set.getInt(7));
                    stocks.add(item);
                }

                return stocks;
            }
            case "users" -> {
                if(!Objects.equals(basic.getId(), 0)) text += "id = '" + basic.getId() + "' AND ";
                if(!Objects.equals(basic.getText1(), "=")) text += "login = '" + basic.getText1() + "' AND ";
                if(!Objects.equals(basic.getText2(), "=")) text += "password = " + basic.getText2() + " AND ";
                if(!Objects.equals(basic.getText3(), "=")) text += "role = '" + Integer.parseInt(basic.getText3()) + "' AND ";
                if(!Objects.equals(basic.getText4(), "=")) text += "name = '" + basic.getText4() + "' AND ";

                text = text.substring(0, text.length() - 4);
                sqlQuery = "SELECT * FROM " + table + " WHERE " + text;
                System.out.println(sqlQuery);

                ResultSet set = preparedSt.executeQuery(sqlQuery);
                while (set.next()){
                    User item = new User(set.getInt(1), set.getString(2), set.getString(3),
                            set.getInt(4), set.getString(5));
                    users.add(item);
                }

                return users;
            }
            case "companies" -> {
                if(!Objects.equals(basic.getText1(), "=")) text += "name = '" + basic.getText1() + "' AND ";


                text = text.substring(0, text.length() - 4);
                sqlQuery = "SELECT * FROM " + table + " WHERE " + text;
                System.out.println(sqlQuery);

                ResultSet set = preparedSt.executeQuery(sqlQuery);
                while (set.next()){
                    Companies item = new Companies(set.getInt(1), set.getString(2));
                    companies.add(item);
                }

                return companies;
            }
            case "currencies" -> {
                if(!Objects.equals(basic.getText1(), "=")) text += "name = '" + basic.getText1() + "' AND ";


                text = text.substring(0, text.length() - 4);
                sqlQuery = "SELECT * FROM " + table + " WHERE " + text;
                System.out.println(sqlQuery);

                ResultSet set = preparedSt.executeQuery(sqlQuery);
                while (set.next()){
                    Currencies item = new Currencies(set.getInt(1), set.getString(2));
                    currencies.add(item);
                }

                return currencies;
            }
            case "jwls" -> {
                if(!Objects.equals(basic.getText1(), "=")) text += "item = '" + basic.getText1() + "' AND ";


                text = text.substring(0, text.length() - 4);
                sqlQuery = "SELECT * FROM " + table + " WHERE " + text;
                System.out.println(sqlQuery);

                ResultSet set = preparedSt.executeQuery(sqlQuery);
                while (set.next()){
                    Jwls item = new Jwls(set.getInt(1), set.getString(2));
                    jwls.add(item);
                }

                return jwls;
            }
        }
        return null;
    }

    public Boolean checkUser(String login) throws SQLException, IOException, ClassNotFoundException {
        Statement preparedSt = getDbConnection().createStatement();
        ResultSet set = preparedSt.executeQuery("SELECT * FROM users");
        Boolean flag = true;

        while (set.next()){
            if(Objects.equals(login, set.getString(2))){
                flag = false;
            }
        }

        return flag;
    }
*/


    public User returnUser(String login, String password) throws SQLException, IOException, ClassNotFoundException {
        Statement preparedSt = getDbConnection().createStatement();

        ResultSet set = preparedSt.executeQuery("SELECT * FROM user WHERE login = '" + login + "' AND password = '" + password + "'");
        set.next();
        User user = new User(set.getInt(1), set.getString(2),
                set.getString(3), set.getInt(4),
                set.getString(5), set.getString(6), set.getString(7));


        return user;
    }

    public Salary returnSalary(int userId) throws SQLException, IOException, ClassNotFoundException {
        Statement preparedSt = getDbConnection().createStatement();

        ResultSet set = preparedSt.executeQuery("SELECT * FROM salary WHERE userid = '" + userId + "'");
        set.next();
        Salary salary = new Salary(set.getInt(1), set.getInt(2), set.getInt(3), set.getInt(4), set.getInt(5));

        return salary;
    }
    public void addUser(User user) throws SQLException, IOException, ClassNotFoundException {
            Statement statement = getDbConnection().createStatement();
            String s = "INSERT INTO `user` (`login`, `password`, `role`, `name`, `surname`, `post`) VALUES ('" + user.getLogin() + "', '" + user.getPassword() + "', '" + user.getRole() + "', " +
                    "'" + user.getName() + "', '" + user.getSurname() + "', '" + user.getPost() + "')";

            statement.executeUpdate(s);

    }

    public void deleteUser(int userId) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();

        String s = "DELETE FROM user WHERE (`id` = '" + userId + "');";
        statement.executeUpdate(s);
    }

    public void deleteSalary(int userId) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();

        String s = "DELETE FROM salary WHERE (`userid` = '" + userId + "');";
        statement.executeUpdate(s);
    }

    public ArrayList set_ch() throws SQLException, IOException, ClassNotFoundException{
        Statement preparedSt = getDbConnection().createStatement();
        ResultSet set = preparedSt.executeQuery("SELECT * FROM user");

        Set<String> unique = new HashSet<>();

        while (set.next()) {
            User item = new User(set.getInt(1), set.getString(2), set.getString(3), set.getInt(4), set.getString(5), set.getString(6), set.getString(7));
            unique.add(item.getPost());
        }
        ArrayList<String> choices = new ArrayList<>(unique);

        return choices;
    }

    public void deleteVacation(int userId) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();

        String s = "DELETE FROM vacation WHERE (`userid` = '" + userId + "');";
        statement.executeUpdate(s);
    }

    public void deleteUserDocument(int userId) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();

        String s = "DELETE FROM user_document WHERE (`user_id` = '" + userId + "');";
        statement.executeUpdate(s);
    }
    public void addSalary(Salary salary, int userId) {
        try {
            Statement statement = getDbConnection().createStatement();
            String s = "INSERT INTO salary(userid, basic, bonus, pension)" + " VALUES('" + userId +
                    "', '" + salary.getBasic() + "', '" + salary.getBonus() + "', '" + salary.getPension() + "');";
            statement.executeUpdate(s);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void addVacation(int userid) {
            try {
                Statement statement = getDbConnection().createStatement();
                String s = "INSERT INTO vacation (userid, basic, daysleft, daysgot)" + " VALUES ('" + userid + "'," +
                        " '30', '30', '0');";
                System.out.println(s);
                statement.executeUpdate(s);
            } catch (SQLException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
    }
    public int findDoc(String s) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();
        String s1 = "SELECT * FROM document" + " WHERE `title` = '" + s + "';";

        ResultSet set = statement.executeQuery(s1);
        set.next();

        return set.getInt(1);

    }
    public void addDoc(Document doc) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();
        String s1 = doc.getTitle() + doc.getId();
        String s = "INSERT INTO document (title, date)" + " VALUES ('" + s1 + "', '" + doc.getDate() + "');";
        statement.executeUpdate(s);
        int docId = findDoc(s1);
        s = "INSERT INTO user_document (user_id, document_id)" + " VALUES ('" + doc.getId() + "', '" + docId + "');";
        statement.executeUpdate(s);
        s = "UPDATE document SET title = '" + doc.getTitle() + "' WHERE id =" + docId + ";";
        statement.executeUpdate(s);
    }
    public void updatePost(int userId, String post) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();
        String s;
        if (Objects.equals(post, "Бухгалтер")){
            s = "UPDATE user SET post = '" + post + "', role = '" + 2 + "' WHERE id =" + userId + ";";
            statement.executeUpdate(s);


        }
        if(Objects.equals(post, "Директор")){
            s = "UPDATE user SET post = '" + post + "', role = '" + 1 + "' WHERE id =" + userId + ";";
            statement.executeUpdate(s);

        }
        if (!Objects.equals(post, "Директор") && !Objects.equals(post, "Бухгалтер")){
            s = "UPDATE user SET post = '" + post + "', role = '" + 3 + "' WHERE id =" + userId + ";";
            statement.executeUpdate(s);

        }
    }
    public void updateSalary(int userId, int salary) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();
        String s = "UPDATE salary SET basic = '" + salary + "' WHERE userid =" + userId + ";";
        statement.executeUpdate(s);
    }
    public void updateVacation(int userId) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();
        String s1 = "SELECT * FROM vacation" + " WHERE `userid` = '" + userId + "';";

        ResultSet set = statement.executeQuery(s1);
        set.next();
        if (set.getInt(4) >= 15){
            int loc1 = set.getInt(4 ) - 15;
            int loc2 = set.getInt(5) + 15;
            String s = "UPDATE vacation SET  daysleft = '" + loc1 + "', daysgot = '" + loc2 + "' WHERE userid =" + userId + ";";
            statement.executeUpdate(s);
        }


    }
    public void updateBonus(int userId, int bonus) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();
        String s = "UPDATE salary SET bonus = '" + bonus + "' WHERE userid =" + userId + ";";
        statement.executeUpdate(s);
    }
    public void updatePension(int userId, int pension) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();
        String s = "UPDATE salary SET pension = '" + pension + "' WHERE userid =" + userId + ";";
        statement.executeUpdate(s);
    }
    public Boolean AuthCheck(String login, String password) throws SQLException, IOException, ClassNotFoundException {

        Statement preparedSt = getDbConnection().createStatement();
        ResultSet set = preparedSt.executeQuery("SELECT * FROM user WHERE login = '" + login + "' AND password = '" + password + "'");

        return set.next();


    }
    public void docSign(int userId, int documentId, int role, String status) throws SQLException, IOException, ClassNotFoundException {
        Statement preparedSt = getDbConnection().createStatement();
        String confirm = role == 1 ? "confirmdir" : "confirmacc";
        String str = "UPDATE user_document SET " + confirm + " = '" + status + "' WHERE user_id = " + userId + " AND document_id = " + documentId;
        System.out.println(str);
        preparedSt.executeUpdate(str);

    }


    public void cnfVacation(int userId, int docId, int type) throws SQLException, IOException, ClassNotFoundException {
        Statement statement = getDbConnection().createStatement();
        if (type == 1){
            String s1 = "UPDATE vacation SET daysleft = '" + 15 + "' WHERE userid =" + userId + ";";
            statement.executeUpdate(s1);
            String s2 = "UPDATE vacation SET daysgot = '" + 15 + "' WHERE userid =" + userId + ";";
            statement.executeUpdate(s2);
            String s3 = "UPDATE document SET title = 'Отпуск 1+' WHERE id =" + docId + ";";
            statement.executeUpdate(s3);
        }
        if (type == 2){
            String s1 = "UPDATE vacation SET daysleft = '" + 0 + "' WHERE userid =" + userId + ";";
            statement.executeUpdate(s1);
            String s2 = "UPDATE vacation SET daysgot = '" + 30 + "' WHERE userid =" + userId + ";";
            statement.executeUpdate(s2);
            String s3 = "UPDATE document SET title = 'Отпуск 2+' WHERE id =" + docId + ";";
            statement.executeUpdate(s3);
        }
    }



    public ArrayList showPost(String post) throws SQLException, IOException, ClassNotFoundException {
        Statement preparedSt = getDbConnection().createStatement();
        ResultSet set = preparedSt.executeQuery("SELECT * FROM user WHERE post = '" + post + "';");

        ArrayList al = new ArrayList();

                while (set.next()){
                    Salary salary = returnSalary(set.getInt(1));
                    Unique item = new Unique(String.valueOf(set.getInt(1)), set.getString(2), set.getString(5), set.getString(6), set.getString(7), String.valueOf(salary.getBasic()));
                    al.add(item);

                    System.out.println(String.valueOf(set.getString(1))+" "+ set.getString(2)+" "+ set.getString(5)+" "+ set.getString(6) +" "+ set.getString(7)+" "+ String.valueOf(salary.getBasic()));
                }

                return al;

    }




}
