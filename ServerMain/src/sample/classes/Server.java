package sample.classes;

import sample.classes.db.*;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Server extends Thread {
    private Handler handler = new Handler();
    private Socket clientSocket = null;
    public Server(Socket socket){
        this.clientSocket = socket;
    }

    private static final String LOG_IN = "logIn";
    private static final String SHOW_QUERY = "show";
    private static final String DELETE_QUERY = "delete";
    private static final String UPDATE_QUERY = "update";
    private static final String SEARCH_QUERY = "search";
    private static final String ADD_USER = "add user";
    private static final String CHECK_USER = "check user";
    private static final String ADD_TRANSACT = "add transact";
    private static final String ADD_REFWITHD = "add refwithd";
    private static final String ADD_SELLJW = "add selljw";
    private static final String ADD_STOCK = "add stock";
    private static final String ADD_CHANGE = "add change";
    private static final String ADD_COMPANY = "add company";
    private static final String ADD_CURRENCY = "add currency";
    private static final String ADD_JWL = "add jwl";
    private static final String SET_CHOICES = "set choices";

    private static final String CHECK_ROLE = "set choices";


    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(8000);

        while(true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Клиент подключился!");
            Server server = new Server(clientSocket);
            server.start();
        }
    }

    public void run() {
        try{
            while (true){
                ObjectOutputStream writerObj = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream readerObj = new ObjectInputStream(clientSocket.getInputStream());

                while (!clientSocket.isClosed()){
                    String action = (String) readerObj.readObject();
                    String request = "";
                    String choices = "";
                    String post = "";
                    String id = "";
                    String[] arr;

                    switch (action){

                        case "CHECK_VALID" -> {
                            String data = String.valueOf(readerObj.readObject());
                            String[] splited = data.split("\\s+");

                            if (handler.AuthCheck(splited[0], splited[1]))
                            {
                                System.out.println("Логин и пароль верный.");
                                writerObj.writeObject(handler.returnUser(splited[0], splited[1]));
                            }
                            else {
                                System.out.println("Логин и пароль неверный, либо не существует.");
                                writerObj.writeObject("INVALID");
                            }
                        }

                        case "SHOW_UDOC" -> {
                            System.out.println("Показать доки");
                            ArrayList<Document> documents = handler.show("document");
                            ArrayList<UserDocument> userDocuments = handler.show("user_document");
                            ArrayList <Unique> result = new ArrayList<>();

                            for(UserDocument ud:userDocuments){
                                Unique unique = new Unique("","","","","","","");
                                unique.setText1(String.valueOf(ud.getUser_id()));
                                unique.setText2(String.valueOf(ud.getDocument_id()));
                                unique.setText5(ud.getConfirmDir());
                                unique.setText6(ud.getConfirmAcc());
                                for (Document doc:documents){
                                    if(ud.getDocument_id() == doc.getId()){
                                        unique.setText3(doc.getTitle());
                                        unique.setText4(doc.getDate());
                                    }
                                }
                                result.add(unique);

                            }
                            writerObj.writeObject(result);
                        }

                        case "SHOW_USERS" -> {
                            ArrayList <Unique> result = new ArrayList<>();
                            ArrayList<User> users = handler.show("user");
                            ArrayList<Salary> salaris = handler.show("salary");
                            for(User u: users){
                                Unique unique = new Unique("","","","","","","");
                                unique.setText1(String.valueOf(u.getId()));
                                unique.setText2(u.getName());
                                unique.setText3(u.getSurname());
                                unique.setText4(u.getPost());
                                for(Salary sal:salaris){
                                    if(u.getId() == sal.getUserId()){
                                        unique.setText5(String.valueOf(sal.getBasic()));
                                        unique.setText6(String.valueOf(sal.getBonus()));
                                        unique.setText7(String.valueOf(sal.getPension()));
                                    }
                                }
                                result.add(unique);
                            }
                            writerObj.writeObject(result);
                        }

                        case "SHOW_QUERY" -> {

                            ArrayList<User> list = handler.set_ch();
                            writerObj.writeObject(list);
                        }
                        case "SHOW_AVG_SALARIES" -> {
                            ArrayList <Unique> result = new ArrayList<>();
                            ArrayList <Unique> posts_salaries = new ArrayList<>();
                            Set<String> posts_tmp = new HashSet<>();
                            ArrayList<sample.classes.db.User> users = handler.show("user");
                            ArrayList<Salary> salaris = handler.show("salary");

                            for(User u: users){
                                posts_tmp.add(u.getPost());
                            }
                            ArrayList<String> posts = new ArrayList<>(posts_tmp);

                            for(User u: users){
                                Unique unique = new Unique("","");
                                unique.setText1(u.getPost());
                                for(Salary sal:salaris){
                                    if(u.getId() == sal.getUserId()){
                                        unique.setText2(String.valueOf(sal.getBasic()));
                                    }
                                }
                                posts_salaries.add(unique);
                            }

                            for (String s : posts) {
                                Unique unique = new Unique("", "", "");
                                int wrkrs = 0;
                                int sumsal = 0;
                                for (Unique posts_salary : posts_salaries) {
                                    System.out.println(s);
                                    System.out.println(posts_salary.getText1());
                                    if (Objects.equals(s, posts_salary.getText1())) {
                                        wrkrs += 1;
                                        sumsal += Integer.parseInt(posts_salary.getText2());
                                    }
                                }
                                System.out.println(s);
                                System.out.println(wrkrs);
                                System.out.println(sumsal);
                                int avgsal = sumsal / wrkrs;
                                unique.setText1(s);
                                unique.setText2(String.valueOf(wrkrs));
                                unique.setText3(String.valueOf(avgsal));

                                result.add(unique);

                            }
                            writerObj.writeObject(result);
                        }

                        case "CONFIRM" -> {
                            Unique unique = (Unique) readerObj.readObject();
                            String role = String.valueOf(readerObj.readObject());
                            if (Objects.equals(role, "DIRECTOR")){
                                handler.docSign(Integer.parseInt(unique.getText1()),
                                        Integer.parseInt(unique.getText2()),1,"Одобрено");
                            }
                            else{
                                handler.docSign(Integer.parseInt(unique.getText1()),
                                        Integer.parseInt(unique.getText2()),2,"Одобрено");

                            }
                        }

                        case "DECLINE" -> {
                            Unique unique = (Unique) readerObj.readObject();
                            String role = String.valueOf(readerObj.readObject());
                            if (Objects.equals(role, "DIRECTOR")){
                                handler.docSign(Integer.parseInt(unique.getText1()),
                                        Integer.parseInt(unique.getText2()),1,"Отклонено");
                            }
                            else{
                                handler.docSign(Integer.parseInt(unique.getText1()),
                                        Integer.parseInt(unique.getText2()),2,"Отклонено");
                            }
                        }

                        case "ADD_USER" -> {

                            User user = (User)readerObj.readObject();
                            Salary salary = (Salary)readerObj.readObject();
                            handler.addUser(user);
                            User buf = handler.returnUser(user.getLogin(), user.getPassword());
                            handler.addSalary(salary, buf.getId());
                            handler.addVacation(buf.getId());
                        }

                        case "DELETE_USER" -> {
                            Unique unique = (Unique) readerObj.readObject();
                            int userId = Integer.parseInt(unique.getText1());
                            handler.deleteUser(userId);
                            handler.deleteSalary(userId);
                            handler.deleteVacation(userId);
                            handler.deleteUserDocument(userId);


                        }

                        case "UPDATE_POST" -> {
                            String postt = (String) readerObj.readObject();
                            String  userId = (String) readerObj.readObject();

                            handler.updatePost(Integer.parseInt(userId), postt);


                        }

                        case "UPDATE_SALARY" -> {

                            String salary = (String) readerObj.readObject();
                            String userId = (String)readerObj.readObject();
                            handler.updateSalary(Integer.parseInt(userId), Integer.parseInt(salary));

                        }

                        case "UPDATE_BONUS" -> {

                            String bonus = (String)readerObj.readObject();
                            String userId = (String)readerObj.readObject();
                            handler.updateBonus(Integer.parseInt(userId), Integer.parseInt(bonus));


                        }

                        case "UPDATE_PENSION" -> {

                            String pension = (String)readerObj.readObject();
                            String userId = (String)readerObj.readObject();
                            handler.updatePension(Integer.parseInt(userId), Integer.parseInt(pension));


                        }

                        case "UPDATE_VAC" -> {
                            int userId = (int)readerObj.readObject();
                            handler.updateVacation(userId);

                        }


                        /////////////////////
                        case "SHOW_USERS_DIR" -> {
                            ArrayList <Unique> result = new ArrayList<>();
                            ArrayList<sample.classes.db.User> users = handler.show("user");
                            ArrayList<Salary> salaris = handler.show("salary");
                            for(User u: users){
                                Unique unique = new Unique("","","","","","");
                                unique.setText1(String.valueOf(u.getId()));
                                unique.setText2(u.getLogin());
                                unique.setText3(u.getName());
                                unique.setText4(u.getSurname());
                                unique.setText5(u.getPost());
                                for(Salary sal:salaris){
                                    if(u.getId() == sal.getUserId()){
                                        unique.setText6(String.valueOf(sal.getBasic()));
                                    }
                                }
                                result.add(unique);
                            }
                            writerObj.writeObject(result);
                        }

                        case "SHOW_SEL_SALARY" -> {
                            id = (String) readerObj.readObject();
                            ArrayList <Unique> result = new ArrayList<>();
                            ArrayList<sample.classes.db.User> users = handler.show("user");
                            ArrayList<Salary> salaris = handler.show("salary");
                            for(User u: users){
                                if(u.getId() == Integer.parseInt(id)){
                                    Unique unique = new Unique("","","");
                                    for(Salary sal:salaris){
                                        if(u.getId() == sal.getUserId()){
                                            unique.setText1(String.valueOf(sal.getBasic()));
                                            unique.setText2(String.valueOf(sal.getBonus()));
                                            unique.setText3(String.valueOf(sal.getPension()));
                                        }
                                    }
                                    result.add(unique);
                                }
                            }
                            writerObj.writeObject(result);
                        }

                        case "SHOW_SEL_VACATION" -> {
                            id = (String) readerObj.readObject();
                            ArrayList <Unique> result = new ArrayList<>();
                            ArrayList<sample.classes.db.User> users = handler.show("user");
                            ArrayList<Vacation> vacations = handler.show("vacation");
                            for(User u: users){
                                if(u.getId() == Integer.parseInt(id)){
                                    Unique unique = new Unique("","","");
                                    for(Vacation vac:vacations){
                                        if(u.getId() == vac.getUserId()){
                                            unique.setText1(String.valueOf(vac.getBasic()));
                                            unique.setText2(String.valueOf(vac.getDaysLeft()));
                                            unique.setText3(String.valueOf(vac.getDaysGot()));
                                        }
                                    }
                                    result.add(unique);
                                }
                            }
                            writerObj.writeObject(result);
                        }

                        case "SET_WORKER" -> {
                            id = (String) readerObj.readObject();
                            ArrayList <Unique> result = new ArrayList<>();
                            ArrayList<sample.classes.db.User> users = handler.show("user");
                            ArrayList<Salary> salaris = handler.show("salary");
                            ArrayList<Vacation> vacations = handler.show("vacation");
                            for(User u: users){
                                if(u.getId() == Integer.parseInt(id)){
                                    Unique unique = new Unique("","","","","","","","","","","");
                                    unique.setText1(u.getName());
                                    unique.setText2(u.getSurname());
                                    unique.setText3(u.getPost());
                                    unique.setText4(u.getLogin());
                                    unique.setText5(u.getPassword());
                                    unique.setText6(String.valueOf(u.getRole()));
                                    for(Salary sal:salaris){
                                        if(u.getId() == sal.getUserId()){
                                            unique.setText7(String.valueOf(sal.getBasic()));
                                            unique.setText8(String.valueOf(sal.getBonus()));
                                            unique.setText9(String.valueOf(sal.getPension()));
                                        }
                                    }
                                    for(Vacation vac:vacations){
                                        if(u.getId() == vac.getUserId()){
                                            unique.setText10(String.valueOf(vac.getDaysLeft()));
                                            unique.setText11(String.valueOf(vac.getDaysGot()));
                                        }
                                    }
                                    result.add(unique);
                                }
                            }
                            writerObj.writeObject(result);
                        }

                        case "ADD_DOC" ->{
                            Document doc = (Document) readerObj.readObject();
                            System.out.println(doc.toString());
                            handler.addDoc(doc);

                        }

                        case "SET_WORKER_DOCS" -> {
                            id = (String) readerObj.readObject();
                            System.out.println("Показать доки");
                            ArrayList<Document> documents = handler.show("document");
                            ArrayList<UserDocument> userDocuments = handler.show("user_document");
                            ArrayList <Unique> result = new ArrayList<>();

                            for(UserDocument ud:userDocuments){
                                Unique unique = new Unique("","","","");
                                unique.setText3(ud.getConfirmDir());
                                unique.setText4(ud.getConfirmAcc());
                                for (Document doc:documents){
                                    if(ud.getDocument_id() == doc.getId()){
                                        unique.setText1(doc.getTitle());
                                        unique.setText2(doc.getDate());
                                    }
                                }
                                if (Objects.equals(ud.getUser_id(), Integer.parseInt(id))) {
                                    result.add(unique);
                                }

                            }
                            writerObj.writeObject(result);
                        }

                        case "SET_CHOICES" -> {

                            ArrayList list = handler.set_ch();
                            writerObj.writeObject(list);
                        }

                        case "SHOW_POSTS" -> {
                            request = (String) readerObj.readObject();
                            ArrayList list = handler.showPost(request);
                            writerObj.writeObject(list);
                        }




                    }
                }

                readerObj.close();
                writerObj.close();
                clientSocket.close();
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
}
