package si.gavryushkin.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.jdbc.core.JdbcOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


@SpringBootApplication
public class AuthApplication implements CommandLineRunner {
    private String uuid;
    private ArrayList<User> userList = new ArrayList<>();

    JFrame frame = null;
    JLabel label1 = new JLabel("ФИО:");
    JTextField field1 = new JTextField();
    JLabel label2 = new JLabel("Телефон:");
    JTextField field2 = new JTextField();
    JLabel label3 = new JLabel("E-mail:");
    JTextField field3 = new JTextField();
    JLabel label5 = new JLabel("Комментарий:");
    JTextField field4 = new JTextField();
    JComboBox comboBox = null;

    @Autowired
    JdbcOperations jdbcOperations;

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthApplication.class).headless(false).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        frame = new JFrame("Authorization v.1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        JPanel panel = new JPanel((new BorderLayout()));
        JLabel label = new JLabel("Тип подписки:");
        String[] typeTariff = {
                "Monthly Subscription",
                "Annual Subscription",
                "Annual_With_Support"
        };
        comboBox = new JComboBox(typeTariff);
        ActionListener actionListener1 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                e.getSource();
            }
        };
        comboBox.addActionListener(actionListener1);

//        JLabel label4 = new JLabel("Добавить запись в БД");
        JPanel gridPanel = new JPanel(new GridLayout(7, 2));
        gridPanel.add(label);
        gridPanel.add(comboBox);
        gridPanel.add(label1);
        gridPanel.add(field1);
        gridPanel.add(label2);
        gridPanel.add(field2);
        gridPanel.add(label3);
        gridPanel.add(field3);
//        gridPanel.add(label4);
        gridPanel.add(label5);
        gridPanel.add(field4);
        JButton button = new JButton("Добавить");
        JButton buttonRefresh = new JButton("Обновить данные в таблице");
        JButton buttonCopy = new JButton(("COPY"));
        JButton reset = new JButton("Reset");
        button.setBackground(Color.blue);
        gridPanel.add(button);
        gridPanel.add(buttonRefresh);
        gridPanel.add(buttonCopy);
        gridPanel.add(reset);
        panel.add(gridPanel);
        JTextField result = new JTextField();
        JButton buttonUsers = new JButton("GET USER LIST");
        panel.add(buttonUsers, BorderLayout.PAGE_START);
        panel.add(result, BorderLayout.PAGE_END);
        ActionListener actionListener2 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result.setText(insertInDB());
                userList.clear();
            }
        };
        button.addActionListener(actionListener2);
        ActionListener refreshList = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuffer sb = new StringBuffer();
                for (User user : refreshBase()) {
                    sb.append(user).append(";\r\n");
                }
                result.setText("Отключены:\n" + sb.toString());
                userList.clear();
            }
        };
        ActionListener copy = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copy(result.getText());
            }
        };
        ActionListener resetFields = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        };
        ActionListener userList2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuffer sb = new StringBuffer();
                for (User user : getUserList()) {
                    sb.append(user).append(";\r\n");
                }
                result.setText(sb.toString());
                userList.clear();
            }
        };
        buttonUsers.addActionListener(userList2);
        buttonCopy.addActionListener(copy);
        buttonRefresh.addActionListener(refreshList);
        reset.addActionListener(resetFields);
        gridPanel.setBackground(new Color(100, 134, 197));
        result.setBackground(new Color(246, 255, 241));
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private ArrayList<User> getUserList() {
        final String USERS_QUERY = "select*from AUTH";
        Connection con = null;
        Statement statement = null;
        try {
            con = new SSHConnection().openSSH();
            statement = con.createStatement();
            ResultSet result = statement.executeQuery(USERS_QUERY);
            while (result.next()) {
                userList.add(new User(
                        result.getString("FIO"),
                        result.getString("Phone"),
                        result.getString("MAIL"),
                        result.getString("UUID"),
                        result.getString("TARIFF"),
                        result.getString("COMMENT"),
                        result.getString("BLOCKED"),
                        result.getString("CAUSE")
                ));
            }
            return userList;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Ошибка получения листа");
        } finally {
            try {
                con.close();
                statement.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Соединение не закрывается");
            }
        }
        return userList;
    }

    private ArrayList<User> refreshBase() {
        final String CHECK_QUERY = "select UUID,FIO,PHONE,MAIL,TARIFF,BLOCKED,DEACTIVATE_DATE,COMMENT,CAUSE from AUTH where BLOCKED NOT IN(1) and current_date>DEACTIVATE_DATE";
        final String BLOCK_QUERY = "update AUTH set BLOCKED=1,CAUSE='SUBSCRIPTION OVER' where BLOCKED NOT IN(1) and current_date>DEACTIVATE_DATE";
        Connection con = null;
        Statement statement = null;
        try {
            con = new SSHConnection().openSSH();
            statement = con.createStatement();
            ResultSet result = statement.executeQuery(CHECK_QUERY);
            while (result.next()) {
                int status = result.getInt("BLOCKED");
                Date deactivateDate = result.getDate("DEACTIVATE_DATE");
                if (new Date().getTime() > deactivateDate.getTime()) {
                    userList.add(new User(
                            result.getString("FIO"),
                            result.getString("Phone"),
                            result.getString("MAIL"),
                            result.getString("UUID"),
                            result.getString("TARIFF"),
                            result.getString("COMMENT"),
                            result.getString("BLOCKED"),
                            result.getString("CAUSE")
                    ));
                }
            }
            if (userList.size() != 0) {
                statement.executeUpdate(BLOCK_QUERY);
            } else {
                JOptionPane.showMessageDialog(frame, "Обновлять нечего");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Ошибка добавления юзера");
        } finally {
            try {
                con.close();
                statement.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Соединение не закрывается");
            }
        }

        return userList;
    }

    private String insertInDB() {
        uuid = UUID.randomUUID().toString();
        Connection con = null;
        Statement statement = null;
        PreparedStatement preparedStatement = null;
        java.sql.Date sqlDate = null;
        LocalDate date = LocalDate.now();
        String tariffItem = (String) comboBox.getSelectedItem();
        if (tariffItem.equals("Monthly Subscription")) {
            sqlDate = java.sql.Date.valueOf(date.plusMonths(1));
        }
        if (tariffItem.equals("Annual Subscription")) {
            sqlDate = java.sql.Date.valueOf(date.plusMonths(12));
        }
        if (tariffItem.equals("Annual_With_Support")) {
            sqlDate = java.sql.Date.valueOf(date.plusMonths(12));
        }
        final String ADD_QUERY;
        try {
            ADD_QUERY = "insert into AUTH (UUID,BLOCKED,ACTIVATE_DATE,DEACTIVATE_DATE,FIO,PHONE,MAIL,TARIFF,COMMENT)"
                    + " values (?,0,CURRENT_DATE,?,?,?,?,?,?)";

            con = new SSHConnection().openSSH();
            preparedStatement = con.prepareStatement(ADD_QUERY);
            preparedStatement.setString(1, uuid);
            preparedStatement.setObject(2, sqlDate);
            preparedStatement.setString(3, field1.getText());
            preparedStatement.setString(4, field2.getText());
            preparedStatement.setString(5, field3.getText());
            preparedStatement.setObject(6, comboBox.getSelectedItem());
            preparedStatement.setString(7, field4.getText());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Ошибка добавления в базу данных");
            return null;
        } finally {
            try {
                con.close();
                preparedStatement.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Соединение не закрывается");
            }
        }

        return uuid;
    }

    private void copy(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    private void reset() {
        field1.setText("");
        field2.setText("");
        field3.setText("");
        field4.setText("");
    }
}
