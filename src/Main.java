import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Software Project Registration Form");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setContentPane(new PanelForm());

            frame.setSize(520, 420);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}