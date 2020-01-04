package si.gavryushkin.auth;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class SSHConnection {

    private final static int SSH_PROXY_PORT = 22;
    private final static String PROXY_USER = "root";
    private final static String PROXY_PASSWORD = "kmeZBz1k";
    private final static String PROXY_HOST = "2.59.42.186";
    private final static String DB_HOST = "localhost";
    private final static int DB_PORT = 3306;

    private Session sesion; //represents each ssh session

    public Connection openSSH() {
        JSch jsch = new JSch();
        Connection con = null;
        try {
            sesion = jsch.getSession(PROXY_USER, PROXY_HOST, SSH_PROXY_PORT);
            sesion.setPassword(PROXY_PASSWORD);
            Properties properties = new Properties();
            properties.put("StrictHostKeyChecking", "no");
            sesion.setConfig(properties);
            sesion.connect();
            int forwardPort = sesion.setPortForwardingL(0, DB_HOST, DB_PORT);
            String url = "jdbc:mysql://localhost:" + forwardPort + "/admin_default?useUnicode=true&serverTimezone=UTC";
            con = DriverManager.getConnection(
                    url,
                    "root",
                    "45for896");
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(new JFrame("ERROR"), "Ошибка SSH");
        }
        return con;
    }
}
