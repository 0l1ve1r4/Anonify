import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    static boolean firstRun = true;
    
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
    logoPanel.setLayout(new BorderLayout());

    // Add logo on the left
    ImageIcon originalIcon = new ImageIcon("res/logo-no-bg-centralized.png");
    Image resizedImage = originalIcon.getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH);
    JLabel logoLabel = new JLabel(new ImageIcon(resizedImage), SwingConstants.LEFT);
    logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
    logoPanel.add(logoLabel, BorderLayout.WEST);

    // Add icons panel on the right
    JPanel iconsPanel = new JPanel();
    iconsPanel.setBackground(Constants.DARK_GRAY);
    iconsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5)); // Align icons to the right

    // Add common icons
    iconsPanel.add(createIconButton("res/settings-icon-white.png", "Settings"));
    iconsPanel.add(createIconButton("res/privacy-icon-white.png", "Privacy"));
    iconsPanel.add(createIconButton("res/help-icon-white.png", "Help"));

    logoPanel.add(iconsPanel, BorderLayout.EAST);

    return logoPanel;
}

// Utility method to create a button with an icon
private JButton createIconButton(String iconPath, String tooltip) {
    ImageIcon icon = new ImageIcon(iconPath);
    Image resizedImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    JButton button = new JButton(new ImageIcon(resizedImage));
    button.setToolTipText(tooltip);
    button.setContentAreaFilled(false); // Transparent background
    button.setBorderPainted(false); // No border
    button.setFocusPainted(false); // No focus border
    return button;
}

}
