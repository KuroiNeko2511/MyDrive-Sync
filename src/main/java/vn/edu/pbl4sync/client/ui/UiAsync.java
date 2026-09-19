package vn.edu.pbl4sync.client.ui;

import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public final class UiAsync {
    private UiAsync() {}

    public static <T> void run(Callable<T> work, Consumer<T> ok, Consumer<Exception> fail) {
        new SwingWorker<T, Void>() {
            @Override protected T doInBackground() throws Exception { return work.call(); }
            @Override protected void done() {
                try { ok.accept(get()); }
                catch (Exception e) {
                    Throwable c = e.getCause() == null ? e : e.getCause();
                    fail.accept(c instanceof Exception ex ? ex : new Exception(c));
                }
            }
        }.execute();
    }

    public static void error(java.awt.Component parent, Exception e) {
        JOptionPane.showMessageDialog(parent, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
