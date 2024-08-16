package me.crystal.Logic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CommandExecutor {

    private static final String COMMANDS_FILE = "commands.txt";
    private static final Path COMMANDS_PATH = Paths.get(COMMANDS_FILE);
    private static final String DATA_FILE = "simulation_results.csv"; // Data file in the same directory
    private static final Path DATA_FILE_PATH = Paths.get(DATA_FILE);
    private static String lastCipheredOutput = "";

    private static final int CIPHER_SHIFT = 3;
    private static String simulationMode = "onecycle"; // Default mode
    private static String filePath = DATA_FILE.toString(); // Default file path
    private static int numSims = 1000; // Default number of simulations

    static {
        try {
            if (!Files.exists(COMMANDS_PATH)) {
                Files.createFile(COMMANDS_PATH);
            }
            if (!Files.exists(DATA_FILE_PATH)) {
                Files.createFile(DATA_FILE_PATH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String executeCommand(String command) {
        String result = "";
        try {
            if (command.startsWith("add ")) {
                result = addCommand(command.substring(4).trim());
            } else if (command.startsWith("remove ")) {
                result = removeCommand(command.substring(7).trim());
            } else if (command.equals("clear")) {
                result = "__CLEAR__"; // This will be handled in TerminalUI
            } else if (command.startsWith("search ")) {
                result = searchGoogle(command.substring(7).trim());
            } else if (command.startsWith("uncipher")) {
                result = uncipherLastOutput();
            } else if (command.startsWith("startsimulation ")) {
                result = startSimulation(command.substring(16).trim());
            } else if (command.startsWith("setmode ")) {
                result = setMode(command.substring(8).trim());
            } else if (command.startsWith("setnum ")) {
                result = setNumSims(command.substring(7).trim());
            } else {
                result = cipherOutput(readCommandFromFile(command));
            }
        } catch (IOException e) {
            result = cipherOutput("Error: " + e.getMessage());
        }

        return result;
    }

    private static String addCommand(String command) throws IOException {
        String[] parts = command.split(":", 2);
        if (parts.length != 2) {
            return cipherOutput("Error: Command must be in the format 'name:response'");
        }

        String name = parts[0].trim();
        String response = parts[1].trim();

        if (name.contains(" ")) {
            return cipherOutput("Error: Command name cannot contain spaces");
        }

        List<String> lines = new ArrayList<>(Files.readAllLines(COMMANDS_PATH));
        boolean commandExists = lines.stream().anyMatch(line -> line.startsWith(name + ":"));

        if (commandExists) {
            return cipherOutput("Error: Command already exists");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(COMMANDS_PATH, StandardOpenOption.APPEND)) {
            writer.write(name + ":" + response);
            writer.newLine();
        }

        return cipherOutput("Added command: " + name + ":" + response);
    }

    private static String removeCommand(String name) throws IOException {
        List<String> lines = Files.readAllLines(COMMANDS_PATH);
        List<String> updatedLines = new ArrayList<>();

        boolean commandRemoved = false;

        for (String line : lines) {
            if (!line.startsWith(name + ":")) {
                updatedLines.add(line);
            } else {
                commandRemoved = true;
            }
        }

        if (!commandRemoved) {
            return cipherOutput("Error: Command not found");
        }

        Files.write(COMMANDS_PATH, updatedLines);

        return cipherOutput("Removed command: " + name);
    }

    private static String readCommandFromFile(String command) throws IOException {
        List<String> lines = Files.readAllLines(COMMANDS_PATH);
        return lines.stream()
                .filter(line -> line.startsWith(command + ":"))
                .map(line -> line.substring(command.length() + 1))
                .findFirst()
                .orElse("Error: Command not found");
    }

    private static String searchGoogle(String query) {
        try {
            String url = "https://www.google.com/search?q=" + query.replace(" ", "+");
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
            return cipherOutput("Opened browser to search: " + query);
        } catch (IOException e) {
            return cipherOutput("Error opening browser: " + e.getMessage());
        }
    }

    private static String startSimulation(String seeds) {
        // Adjusted to match the new SimulationManager constructor
        SimulationManager manager = new SimulationManager(numSims, filePath);
        return manager.startSimulation(seeds);
    }

    private static String setMode(String mode) {
        if (mode.equals("onecycle") || mode.equals("oneshot")) {
            simulationMode = mode;
            return cipherOutput("Simulation mode set to: " + mode);
        } else {
            return cipherOutput("Error: Invalid mode. Use 'onecycle' or 'oneshot'.");
        }
    }

    private static String setNumSims(String numSimsStr) {
        try {
            int num = Integer.parseInt(numSimsStr);
            if (num > 0) {
                numSims = num;
                return cipherOutput("Number of simulations set to: " + numSims);
            } else {
                return cipherOutput("Error: Number of simulations must be greater than 0.");
            }
        } catch (NumberFormatException e) {
            return cipherOutput("Error: Invalid number format.");
        }
    }

    private static String cipherOutput(String output) {
        if (output == null || output.isEmpty()) {
            return "Error: Output is empty";
        }
        lastCipheredOutput = applyCaesarCipher(output, CIPHER_SHIFT);
        return lastCipheredOutput;
    }

    private static String uncipherLastOutput() {
        if (!lastCipheredOutput.isEmpty()) {
            return applyCaesarCipher(lastCipheredOutput, -CIPHER_SHIFT);
        }
        return "Error: No valid ciphered output to uncipher";
    }

    private static String applyCaesarCipher(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char i : text.toCharArray()) {
            if (Character.isLetter(i)) {
                char base = Character.isUpperCase(i) ? 'A' : 'a';
                result.append((char) ((i - base + shift + 26) % 26 + base));
            } else {
                result.append(i);
            }
        }
        return result.toString();
    }
}
