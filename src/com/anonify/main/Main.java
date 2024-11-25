package com.anonify.main;

import javax.swing.SwingUtilities;
import com.anonify.ui.MainFrame;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
