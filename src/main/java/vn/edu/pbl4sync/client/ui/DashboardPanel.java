package vn.edu.pbl4sync.client.ui;

import vn.edu.pbl4sync.client.AgentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardPanel extends JPanel {
    private final AgentService service;
    private final JPanel stats = new JPanel(new GridLayout(1, 4, 12, 12));
    private final JLabel connection = new JLabel();

    public DashboardPanel(AgentService service) {
        super(new BorderLayout(15,15)); this.service = service; setBorder(new EmptyBorder(20,20,20,20));
        JPanel top = new JPanel(new BorderLayout());
        JLabel title = new JLabel(service.isAdmin() ? "Admin Dashboard" : "Dashboard"); title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        JButton refresh = new JButton("Refresh"); refresh.addActionListener(e -> refreshData());
        top.add(title, BorderLayout.WEST); top.add(refresh, BorderLayout.EAST); add(top, BorderLayout.NORTH);
        add(stats, BorderLayout.CENTER);
        connection.setFont(connection.getFont().deriveFont(15f)); add(connection, BorderLayout.SOUTH);
    }

    public void refreshData() {
        connection.setText(service.connected() ? "● Agent connected - auto synchronization is active" : "● Disconnected");
        UiAsync.run(service::dashboard, this::render, ex -> UiAsync.error(this, ex));
    }

    private void render(Map<String,String> m) {
        stats.removeAll();
        Map<String,String> labels = new LinkedHashMap<>();
        if (service.isAdmin()) {
            labels.put("Users", m.getOrDefault("users","0")); labels.put("Workspaces", m.getOrDefault("workspaces","0"));
            labels.put("Online Agents", m.getOrDefault("online_agents","0")); labels.put("Files", m.getOrDefault("files","0"));
        } else {
            labels.put("My Workspaces", m.getOrDefault("workspaces","0")); labels.put("Files", m.getOrDefault("files","0"));
            labels.put("Online Agents", m.getOrDefault("online_agents","0")); labels.put("My Activities", m.getOrDefault("activities","0"));
        }
        for (var e : labels.entrySet()) stats.add(card(e.getKey(), e.getValue()));
        stats.revalidate(); stats.repaint();
    }

    private JPanel card(String name, String value) {
        JPanel p = new JPanel(new BorderLayout()); p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(),new EmptyBorder(18,18,18,18)));
        JLabel n = new JLabel(name); n.setFont(n.getFont().deriveFont(Font.BOLD, 15f));
        JLabel v = new JLabel(value, SwingConstants.CENTER); v.setFont(v.getFont().deriveFont(Font.BOLD, 35f));
        p.add(n, BorderLayout.NORTH); p.add(v, BorderLayout.CENTER); return p;
    }
}
