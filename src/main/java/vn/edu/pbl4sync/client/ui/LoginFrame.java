package vn.edu.pbl4sync.client.ui;

import vn.edu.pbl4sync.client.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final ClientConfig config;
    private final JTextField username = new JTextField();
    private final JPasswordField password = new JPasswordField();
    private final JTextField host = new JTextField();
    private final JTextField port = new JTextField();
    private final JButton login = new JButton("Login");
    private final JLabel status = new JLabel(" ");

    public LoginFrame(ClientConfig config) {
        super("PBL4 Company Sync - Login");
        this.config = config;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(430, 430);
        setResizable(false);
        setLocationRelativeTo(null);
        build();
    }

    private void build() {
        JPanel root = new JPanel(new BorderLayout(10, 18));
        root.setBorder(new EmptyBorder(28, 38, 28, 38));
        setContentPane(root);

        JPanel titlePanel = new JPanel(new GridLayout(0, 1));
        JLabel title = new JLabel("COMPANY SYNC", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 25f));
        JLabel sub = new JLabel("PBL4 - Distributed File Synchronization", SwingConstants.CENTER);
        titlePanel.add(title); titlePanel.add(sub);
        root.add(titlePanel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 1, 6, 6));
        form.add(new JLabel("Username")); form.add(username);
        form.add(new JLabel("Password")); form.add(password);
        form.add(new JLabel("Server address")); host.setText(config.host()); form.add(host);
        form.add(new JLabel("Port")); port.setText(String.valueOf(config.port())); form.add(port);
        root.add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        login.setPreferredSize(new Dimension(0, 40));
        login.addActionListener(e -> doLogin());
        password.addActionListener(e -> doLogin());
        bottom.add(login, BorderLayout.NORTH);
        status.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.add(status, BorderLayout.SOUTH);
        root.add(bottom, BorderLayout.SOUTH);
    }

    private void doLogin() {
        String u = username.getText().trim();
        String pw = new String(password.getPassword());
        String h = host.getText().trim();
        int p;
        try { p = Integer.parseInt(port.getText().trim()); }
        catch (Exception e) { JOptionPane.showMessageDialog(this, "Invalid port"); return; }
        if (u.isBlank() || pw.isBlank() || h.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter username, password and server address"); return;
        }
        login.setEnabled(false); status.setText("Connecting...");
        AgentService service = new AgentService(config);
        UiAsync.run(() -> {
            service.connectAndLogin(h, p, u, pw);
            return service;
        }, s -> {
            dispose();
            MainFrame frame = new MainFrame(s);
            frame.setVisible(true);
        }, ex -> {
            service.close();
            login.setEnabled(true); status.setText("Login failed"); UiAsync.error(this, ex);
        });
    }
}
