import com.formdev.flatlaf.FlatLightLaf;
import ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatLightLaf.setup();
            new LoginFrame().setVisible(true);
        });
    }
}
