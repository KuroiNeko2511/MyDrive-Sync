package vn.edu.pbl4sync.server.ui;

import vn.edu.pbl4sync.server.ServerCore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ServerMonitorFrame extends JFrame {
    private final ServerCore server;
    private final JLabel status = new JLabel("Stopped");
    private final JLabel agents = new JLabel("0");
    private final JTextArea logs = new JTextArea();
    private final JButton start = new JButton("Start Server");
    private final JButton stop = new JButton("Stop Server");

    public ServerMonitorFrame(ServerCore server) {
        super("PBL4 Sync - Server Monitor");
        this.server = server;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(850, 560);
        setLocationRelativeTo(null);
        build();
        server.addLogListener(line -> SwingUtilities.invokeLater(() -> {
            logs.append(line + "\n");
            logs.setCaretPosition(logs.getDocument().getLength());
        }));
        new Timer(1000, e -> refresh()).start();
    }

    private void build() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(root);

        JLabel title = new JLabel("PBL4 FILE SYNC SERVER");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        root.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        JPanel info = new JPanel(new GridLayout(2, 3, 10, 10));
        info.add(card("Status", status));
        info.add(card("Port", new JLabel(String.valueOf(server.port()))));
        info.add(card("Online Agents", agents));
        center.add(info, BorderLayout.NORTH);

        logs.setEditable(false);
        logs.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        center.add(new JScrollPane(logs), BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        start.addActionListener(e -> startServer());
        stop.addActionListener(e -> server.stop());
        buttons.add(start); buttons.add(stop);
        root.add(buttons, BorderLayout.SOUTH);
        refresh();
    }

    private JPanel card(String name, JLabel value) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), new EmptyBorder(10,10,10,10)));
        JLabel n = new JLabel(name); n.setFont(n.getFont().deriveFont(Font.BOLD));
        value.setFont(value.getFont().deriveFont(18f));
        p.add(n, BorderLayout.NORTH); p.add(value, BorderLayout.CENTER);
        return p;
    }

    private void startServer() {
        start.setEnabled(false);
        new Thread(() -> {
            try { server.start(); }
            catch (Exception ex) { SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, ex.getMessage(), "Start failed", JOptionPane.ERROR_MESSAGE)); }
            finally { SwingUtilities.invokeLater(this::refresh); }
        }, "server-start-ui").start();
    }

    private void refresh() {
        boolean running = server.isRunning();
        status.setText(running ? "RUNNING" : "STOPPED");
        agents.setText(String.valueOf(server.onlineCount()));
        start.setEnabled(!running); stop.setEnabled(running);
    }
}
