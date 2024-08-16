package me.crystal;

import me.crystal.Interface.TerminalUI;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;

public class main {

    public static void main(String[] args) {
        // Set the FlatLaf theme
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Create and show the terminal UI
        SwingUtilities.invokeLater(TerminalUI::createAndShowGUI);
    }
}