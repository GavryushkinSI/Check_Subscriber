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
import java.util.UUID;

@SpringBootApplication
public class AuthApplication implements CommandLineRunner {
    JLabel label1 = new JLabel("ФИО:");
    JTextField field1 = new JTextField();
    JLabel label2 = new JLabel("Телефон:");
    JTextField field2 = new JTextField();
    JLabel label3 = new JLabel("E-mail:");
    JTextField field3 = new JTextField();

    @Autowired
    JdbcOperations jdbcOperations;

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthApplication.class).headless(false).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        JFrame frame = new JFrame("Authorization v.1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        JPanel panel = new JPanel((new BorderLayout()));
        JLabel label = new JLabel("Тип подписки:");
        String[] typeTariff = {
                "Месячная подписка",
                "Подписка на год",
        };
        JComboBox comboBox = new JComboBox(typeTariff);
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

        final String ADD_QUERY = "insert into AUTH (UUID,ACTIVATE_DATE,DEACTIVATE_DATE)\n"
                + "values (" + uuid + " ,CURRENT_DATE,CURRENT_DATE+30)";

        final String SELECT_QUERY = "select UUID from AUTH";

        jdbcOperations.execute(ADD_QUERY);


        return "Запись добавлена!";
    }
}
