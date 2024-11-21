import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle(Constants.JFRAME_TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 600);
        setLayout(new BorderLayout());

        // Add components
        add(createLogoPanel(), BorderLayout.NORTH);
        ChatPanel chatPanel = new ChatPanel();
        add(chatPanel.getScrollPane(), BorderLayout.CENTER);
        add(new InputPanel(chatPanel), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Constants.DARK_GRAY);
        logoPanel.setPreferredSize(new Dimension(getWidth(), 50));

        ImageIcon originalIcon = new ImageIcon("res/logo-no-bg-centralized.png");
        Image resizedImage = originalIcon.getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(resizedImage), SwingConstants.CENTER);
        logoPanel.add(logoLabel);

        return logoPanel;
    }
}
