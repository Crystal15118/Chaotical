package me.crystal.Interface;

import me.crystal.Logic.CommandExecutor;
import me.crystal.Logic.TerminalPersonality;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.*;
import java.util.List;

public class TerminalUI {

    private static final String PROMPT = "> ";
    private static int lastPromptPos;
    private static final int MOVE_INTERVAL = 50; // Interval for movement updates in milliseconds
    private static final int MOVE_DURATION = 1000; // Duration of the move animation in milliseconds
    private static final int MOVE_RADIUS = 100; // Radius for random movement
    public static JTextArea textArea; // Declare textArea as static
    private static final List<String> PROJECT_URLS = Arrays.asList(
            "https://github.com/KingContaria/seedqueue",
            "https://github.com/Crystal15118/Onecycle-Endfight-Simulator",
            "https://github.com/Crystal15118/Oneshot-Endfight-Simulator",
            "https://github.com/Crystal15118/MCSR-Macros",
            "https://github.com/elebumm/RedditVideoMakerBot",
            "https://github.com/elebumm/PowerToys"
    );

    static {
        new HashMap<String, String>() {{
            put("https://github.com/KingContaria/seedqueue",
                    "<html><body style='text-align: center; color: black;'>Efficient seed selection for Minecraft seeds.<br>" +
                            "Great for finding the perfect seed for your next adventure with fast and reliable results.</body></html>");
            put("https://github.com/Crystal15118/Onecycle-Endfight-Simulator",
                    "<html><body style='text-align: center; color: black;'>Simulate an end fight in Minecraft with one cycle.<br>" +
                            "Perfect for testing and optimizing your endgame strategies.</body></html>");
            put("https://github.com/Crystal15118/Oneshot-Endfight-Simulator",
                    "<html><body style='text-align: center; color: black;'>Test your ability to complete an end fight in one shot.<br>" +
                            "This simulator helps you prepare for the ultimate Minecraft challenge.</body></html>");
            put("https://github.com/Crystal15118/MCSR-Macros",
                    "<html><body style='text-align: center; color: black;'>Automate repetitive tasks in Minecraft with macros.<br>" +
                            "Enhance your gameplay experience with custom automation scripts.</body></html>");
            put("https://github.com/elebumm/RedditVideoMakerBot",
                    "<html><body style='text-align: center; color: black;'>Create engaging videos from Reddit posts effortlessly.<br>" +
                            "Ideal for content creators looking to turn text posts into videos.</body></html>");
            put("https://github.com/elebumm/PowerToys",
                    "<html><body style='text-align: center; color: black;'>A set of useful tools for Windows power users.<br>" +
                            "Includes features to enhance productivity and improve your workflow.</body></html>");
        }};
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Chaotical (The Chaotic-Terminal Experience)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Center the frame on the screen initially
        frame.setLocationRelativeTo(null);

        textArea = new JTextArea() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    int pos = getCaretPosition();
                    Rectangle r = modelToView(pos);
                    g.setColor(Color.WHITE);
                    g.fillRect(r.x, r.y, 8, r.height);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        };

        textArea.setEditable(true);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setCaretColor(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(textArea);

        textArea.append(PROMPT);
        lastPromptPos = textArea.getDocument().getLength();

        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int caretPos = textArea.getCaretPosition();

                if (caretPos < lastPromptPos) {
                    textArea.setCaretPosition(lastPromptPos);
                }

                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (caretPos <= lastPromptPos) {
                        e.consume();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    processCommand(textArea);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (textArea.getCaretPosition() < lastPromptPos) {
                    textArea.setCaretPosition(lastPromptPos);
                }
            }
        });

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);

        // Timer to move the window smoothly every 1 minute
        new javax.swing.Timer(60_000, e -> moveWindowSmoothly(frame)).start();

        // Timer to show pop-ups at regular intervals
        new javax.swing.Timer(40_000, e -> showPopupAd(frame)).start();
    }

    public static void clearTextArea() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("");  // Clear the text area
            textArea.append(PROMPT); // Add the prompt back
            textArea.setCaretPosition(textArea.getDocument().getLength()); // Move the caret to the end
        });
    }

    private static void moveWindowSmoothly(JFrame frame) {
        Point startLocation = frame.getLocation();
        Random random = new Random();
        int targetX = startLocation.x + random.nextInt(2 * MOVE_RADIUS + 1) - MOVE_RADIUS; // -100 to +100 range
        int targetY = startLocation.y + random.nextInt(2 * MOVE_RADIUS + 1) - MOVE_RADIUS; // -100 to +100 range

        new javax.swing.Timer(MOVE_INTERVAL, new ActionListener() {
            private long startTime = System.currentTimeMillis();
            private final int duration = MOVE_DURATION;

            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                double progress = Math.min(1.0, (double) elapsed / duration);
                int newX = (int) (startLocation.x + progress * (targetX - startLocation.x));
                int newY = (int) (startLocation.y + progress * (targetY - startLocation.y));
                frame.setLocation(new Point(newX, newY));
                if (progress >= 1.0) {
                    ((javax.swing.Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    private static void processCommand(JTextArea textArea) {
        String input = textArea.getText().substring(lastPromptPos).trim();
        if (input.equalsIgnoreCase("clear")) {
            clearTextArea();
        } else if (!input.isEmpty()) {
            String output = CommandExecutor.executeCommand(input);
            String comment = TerminalPersonality.getRemark();
            textArea.append("\n" + comment + "\n" + output + "\n" + PROMPT);
            lastPromptPos = textArea.getDocument().getLength();
        }
    }

    private static void showPopupAd(JFrame frame) {
        if (PROJECT_URLS.isEmpty()) return;

        // Select a random project URL
        String url = PROJECT_URLS.get(new Random().nextInt(PROJECT_URLS.size()));

        JFrame adFrame = new JFrame("Ad Pop-up");
        adFrame.setSize(400, 200);
        adFrame.setLocationRelativeTo(null);
        adFrame.setUndecorated(false); // Show the window border
        adFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel adPanel = new JPanel();
        adPanel.setBackground(Color.GRAY); // Set the background color to grayish
        adPanel.setLayout(new BorderLayout());

        JLabel linkLabel = new JLabel("<html><a href='" + url + "'>" + url + "</a></html>", SwingConstants.CENTER);
        linkLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Adjust font size
        linkLabel.setForeground(Color.WHITE); // Set text color to white
        linkLabel.setOpaque(true); // Make sure the background color is visible
        linkLabel.setBackground(Color.GRAY); // Match the background color

        adPanel.add(linkLabel, BorderLayout.CENTER);

        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                linkLabel.setText("<html><a href='" + url + "' style='color: blue;'>" + url + "</a></html>"); // Change color to blue on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                linkLabel.setText("<html><a href='" + url + "'>" + url + "</a></html>"); // Reset color when not hovering
            }
        });

        adFrame.add(adPanel);
        adFrame.setVisible(true);
    }
}
