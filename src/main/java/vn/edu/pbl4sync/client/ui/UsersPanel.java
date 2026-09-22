package vn.edu.pbl4sync.client.ui;

import vn.edu.pbl4sync.client.AgentService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UsersPanel extends JPanel {
    private final AgentService service;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[] { "ID", "Username", "Role", "Status", "Created" }, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private List<Map<String, String>> rows = new ArrayList<>();

    public UsersPanel(AgentService s) {
        super(new BorderLayout(8, 8));
        service = s;
        setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel title = new JLabel("User Management");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel b = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton create = new JButton("Create User"), active = new JButton("Activate"), disable = new JButton("Disable"),
                refresh = new JButton("Refresh");
        create.addActionListener(e -> create());
        active.addActionListener(e -> status("ACTIVE"));
        disable.addActionListener(e -> status("DISABLED"));
        refresh.addActionListener(e -> refreshData());
        b.add(create);
        b.add(active);
        b.add(disable);
        b.add(refresh);
        add(b, BorderLayout.SOUTH);
    }

    public void refreshData() {
        UiAsync.run(service::listUsers, r -> {
            rows = r;
            model.setRowCount(0);
            for (var x : r)
                model.addRow(new Object[] { x.get("id"), x.get("username"), x.get("system_role"), x.get("status"),
                        x.get("created_at") });
        }, ex -> UiAsync.error(this, ex));
    }

    private void create() {
        JTextField u = new JTextField(), p = new JTextField();
        JComboBox<String> role = new JComboBox<>(new String[] { "USER", "ADMIN" });
        Object[] f = { "Username", u, "Password", p, "Role", role };
        if (JOptionPane.showConfirmDialog(this, f, "Create User",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
            UiAsync.run(() -> {
                service.createUser(u.getText().trim(), p.getText(), String.valueOf(role.getSelectedItem()));
                return null;
            }, x -> refreshData(), ex -> UiAsync.error(this, ex));
    }

    private void status(String st) {
        int i = table.getSelectedRow();
        if (i < 0)
            return;
        long id = Long.parseLong(rows.get(i).get("id"));
        UiAsync.run(() -> {
            service.setUserStatus(id, st);
            return null;
        }, x -> refreshData(), ex -> UiAsync.error(this, ex));
    }
}
