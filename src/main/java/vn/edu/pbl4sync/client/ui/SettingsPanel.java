package vn.edu.pbl4sync.client.ui;

import vn.edu.pbl4sync.client.AgentService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.file.Path;

public class SettingsPanel extends JPanel {
    private final AgentService service;
    private final JTextField root = new JTextField(), server = new JTextField();

    public SettingsPanel(AgentService s) {
        super(new BorderLayout(8, 8));
        service = s;
        setBorder(new EmptyBorder(18, 18, 18, 18));
        JLabel t = new JLabel("Settings");
        t.setFont(t.getFont().deriveFont(Font.BOLD, 24f));
        add(t, BorderLayout.NORTH);
        JPanel form = new JPanel(new GridLayout(0, 1, 6, 6));
        form.add(new JLabel("Local Sync Root"));
        form.add(root);
        JButton browse = new JButton("Choose Folder");
        browse.addActionListener(e -> choose());
        form.add(browse);
        form.add(new JLabel("Server"));
        server.setEditable(false);
        form.add(server);
        JButton save = new JButton("Save and re-sync");
        save.addActionListener(e -> save());
        form.add(save);
        add(form, BorderLayout.CENTER);
    }

    public void refreshData() {
        root.setText(service.config().syncRoot().toString());
        server.setText(service.config().host() + ":" + service.config().port());
    }

    private void choose() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            root.setText(fc.getSelectedFile().getAbsolutePath());
    }

    private void save() {
        UiAsync.run(() -> {
            service.changeSyncRoot(Path.of(root.getText().trim()));
            return null;
        }, x -> JOptionPane.showMessageDialog(this, "Sync folder updated"), ex -> UiAsync.error(this, ex));
    }
}
