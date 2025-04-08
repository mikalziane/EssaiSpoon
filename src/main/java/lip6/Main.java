package lip6;

import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Main {
    private static JTextArea outputArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Create the main frame
        JFrame frame = new JFrame("Spoon Code Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create analyze button
        JButton analyzeButton = new JButton("Select Source & Analyze");
        buttonPanel.add(analyzeButton);

        // Create output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Add components to main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add action listener to the button
        analyzeButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fileChooser.setMultiSelectionEnabled(true);

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                analyzeFiles(selectedFiles);
            }
        });

        // Add the main panel to the frame
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void analyzeFiles(File[] files) {
        try {
            outputArea.setText(""); // Clear previous output
            SpoonAPI spoon = new Launcher();

            // Add all selected files/directories as input resources
            for (File file : files) {
                spoon.addInputResource(file.getAbsolutePath());
                outputArea.append("Added resource: " + file.getAbsolutePath() + "\n");
            }

            outputArea.append("\nBuilding model...\n");
            CtModel model = spoon.buildModel();
            outputArea.append("Model built successfully!\n\n");

            String currentPackage = "";
            for (CtType<?> type : model.getAllTypes()) {
                if (type instanceof CtClass<?> ctClass) {
                    String packageName = ctClass.getPackage().getQualifiedName();
                    if (!packageName.equals(currentPackage)) {
                        currentPackage = packageName;
                        outputArea.append("Package: " + currentPackage + "\n");
                    }
                    outputArea.append(" Class: " + ctClass.getQualifiedName() + "\n");

                    for (CtField<?> ctField: ctClass.getFields()) {
                        outputArea.append("     Field: " + ctClass.getQualifiedName() + "."+ ctField.getSimpleName() + " : " + ctField.getDeclaringType().getSimpleName() + "\n");
                    }

                    for (CtMethod<?> m : ctClass.getMethods()) {
                        outputArea.append("     Method: " + ctClass.getQualifiedName() + "." + m.getSignature() + "\n");
                    }
                }
            }
        } catch (Exception e) {
            outputArea.append("Error during analysis: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
}
