package si.gavryushkin.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.jdbc.core.JdbcOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.UUID;


@SpringBootApplication
public class AuthApplication implements CommandLineRunner {
    JFrame frame=null;
    JLabel label1 = new JLabel("ФИО:");
    JTextField field1 = new JTextField();
    JLabel label2 = new JLabel("Телефон:");
    JTextField field2 = new JTextField();
    JLabel label3 = new JLabel("E-mail:");
    JTextField field3 = new JTextField();
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
        };
        comboBox = new JComboBox(typeTariff);
        ActionListener actionListener1 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                e.getSource();
            }
        };
        comboBox.addActionListener(actionListener1);

        JLabel label4 = new JLabel("Добавить запись в БД");
        JPanel gridPanel = new JPanel(new GridLayout(5, 2));
        gridPanel.add(label);
        gridPanel.add(comboBox);
        gridPanel.add(label1);
        gridPanel.add(field1);
        gridPanel.add(label2);
        gridPanel.add(field2);
        gridPanel.add(label3);
        gridPanel.add(field3);
        gridPanel.add(label4);
        JButton button = new JButton("Добавить");
        button.setBackground(Color.blue);
        gridPanel.add(button);
        panel.add(gridPanel);
        JTextField result = new JTextField();
        panel.add(result, BorderLayout.PAGE_END);
        ActionListener actionListener2 = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result.setText(insertInDB());
            }
        };
        button.addActionListener(actionListener2);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private String insertInDB() {
        String uuid = UUID.randomUUID().toString();
        Connection con = null;
        PreparedStatement preparedStatement = null;
        LocalDate date = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(date.plusMonths(1));
        final String ADD_QUERY;
        try {
            ADD_QUERY = "insert into AUTH (UUID,BLOCKED,ACTIVATE_DATE,DEACTIVATE_DATE,FIO,PHONE,MAIL,TARIFF)"
                    + " values (?,0,CURRENT_DATE,?,?,?,?,?)";

            con = new SSHConnection().openSSH();
            preparedStatement = con.prepareStatement(ADD_QUERY);
            preparedStatement.setString(1, uuid);
            preparedStatement.setObject(2, sqlDate);
            preparedStatement.setString(3, field1.getText());
            preparedStatement.setString(4, field2.getText());
            preparedStatement.setString(5, field3.getText());
            preparedStatement.setObject(6, comboBox.getSelectedItem());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage());
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
}
