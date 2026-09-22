package vn.edu.pbl4sync.client.ui;

import vn.edu.pbl4sync.client.AgentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ActivityPanel extends JPanel {
    private final AgentService service;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[] { "Time", "User", "Workspace", "Action", "Detail" }, 0) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    public ActivityPanel(AgentService s) {
        super(new BorderLayout(8, 8));
        service = s;
        setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel t = new JLabel("Activity Log");
        t.setFont(t.getFont().deriveFont(Font.BOLD, 24f));
        add(t, BorderLayout.NORTH);
        add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        JButton r = new JButton("Refresh");
        r.addActionListener(e -> refreshData());
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(r);
        add(p, BorderLayout.SOUTH);
    }

    public void refreshData() {
        UiAsync.run(() -> service.listActivity(null), rows -> {
            model.setRowCount(0);
            for (var x : rows)
                model.addRow(new Object[] { x.get("created_at"), x.get("username"), x.get("workspace"), x.get("action"),
                        x.get("detail") });
        }, ex -> UiAsync.error(this, ex));
    }
}
