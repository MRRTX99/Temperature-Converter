import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class TemperatureConverterGUI extends JFrame {
    private JComboBox<String> fromUnitComboBox;
    private JComboBox<String> toUnitComboBox;
    private JTextField inputTextField;
    private JTextField outputTextField;
    private JButton convertButton;
    private DefaultListModel<String> historyListModel;
    private JList<String> historyList;
    private JLabel resultMessageLabel;
    private JPanel keyboardPanel;

    public TemperatureConverterGUI() {
        setTitle("Temperature Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400); // Increased the height to accommodate the "Developed by" message
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel fromLabel = new JLabel("From:");
        fromUnitComboBox = new JComboBox<>(new String[]{"Celsius", "Fahrenheit", "Kelvin"});
        JLabel toLabel = new JLabel("To:");
        toUnitComboBox = new JComboBox<>(new String[]{"Celsius", "Fahrenheit", "Kelvin"});
        JLabel inputLabel = new JLabel("Enter Temperature:");
        inputTextField = new JTextField();
        JLabel outputLabel = new JLabel("Result:");
        outputTextField = new JTextField();
        outputTextField.setEditable(false);

        convertButton = new JButton("Convert");
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertTemperature();
            }
        });

        panel.add(fromLabel);
        panel.add(fromUnitComboBox);
        panel.add(toLabel);
        panel.add(toUnitComboBox);
        panel.add(inputLabel);
        panel.add(inputTextField);
        panel.add(convertButton);

        add(panel, BorderLayout.NORTH);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.add(outputLabel, BorderLayout.NORTH);
        outputPanel.add(outputTextField, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.CENTER);

        // Result message label
        resultMessageLabel = new JLabel("");
        resultMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(resultMessageLabel, BorderLayout.SOUTH);

        // History panel
        historyListModel = new DefaultListModel<>();
        historyList = new JList<>(historyListModel);
        JScrollPane historyScrollPane = new JScrollPane(historyList);
        historyScrollPane.setBorder(BorderFactory.createTitledBorder("Conversion History"));
        add(historyScrollPane, BorderLayout.EAST);

        // Create and add the numeric keyboard
        keyboardPanel = createNumericKeyboard();
        add(keyboardPanel, BorderLayout.WEST);

        // Add the "Developed by message at the bottom
        JLabel developedByLabel = new JLabel("Developed by RTX");
        developedByLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(developedByLabel, BorderLayout.SOUTH);
    }

    private JPanel createNumericKeyboard() {
        JPanel panel = new JPanel(new GridLayout(4, 3));
        panel.setBorder(BorderFactory.createTitledBorder("Numeric Keyboard"));

        // Create numeric buttons
        for (int i = 1; i <= 9; i++) {
            addButton(panel, String.valueOf(i));
        }

        // Add 0 button
        addButton(panel, "0");

        // Add Clear button
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextField.setText("");
            }
        });
        panel.add(clearButton);

        return panel;
    }

    private void addButton(JPanel panel, String label) {
        JButton button = new JButton(label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextField.setText(inputTextField.getText() + label);
            }
        });
        panel.add(button);
    }

    private void convertTemperature() {
        try {
            String fromUnit = fromUnitComboBox.getSelectedItem().toString();
            String toUnit = toUnitComboBox.getSelectedItem().toString();
            double inputValue = Double.parseDouble(inputTextField.getText());

            double result = convert(fromUnit, toUnit, inputValue);

            // Format the result with two decimal places
            DecimalFormat df = new DecimalFormat("#.##");
            String formattedResult = df.format(result);

            // Display the result
            outputTextField.setText(formattedResult);

            // Add the conversion to the history list
            String historyEntry = String.format("%.2f %s to %.2f %s", inputValue, fromUnit, result, toUnit);
            historyListModel.addElement(historyEntry);

            // Set the result message based on the converted temperature and selected toUnit
            setResultMessage(result, toUnit);
        } catch (NumberFormatException e) {
            outputTextField.setText("Invalid input. Please enter a valid number.");
            resultMessageLabel.setText("");
        }
    }

    private void setResultMessage(double result, String toUnit) {
        String message = "";

        if (toUnit.equals("Fahrenheit")) {
            if (result < 32) {
                message = "It's very cold!";
            } else if (result >= 32 && result < 68) {
                message = "It's chilly.";
            } else if (result >= 68 && result < 86) {
                message = "It's warm.";
            } else {
                message = "It's hot!";
            }
        } else if (toUnit.equals("Kelvin")) {
            if (result < 273.15) {
                message = "It's very cold!";
            } else if (result >= 273.15 && result < 293.15) {
                message = "It's chilly.";
            } else if (result >= 293.15 && result < 303.15) {
                message = "It's warm.";
            } else {
                message = "It's hot!";
            }
        } else {
            message = ""; // Default message for Celsius
        }

        resultMessageLabel.setText(message);
    }

    private double convert(String fromUnit, String toUnit, double value) {
        if (fromUnit.equals(toUnit)) {
            return value;
        }

        if (fromUnit.equals("Celsius")) {
            if (toUnit.equals("Fahrenheit")) {
                return (value * 9 / 5) + 32;
            } else if (toUnit.equals("Kelvin")) {
                return value + 273.15;
            }
        } else if (fromUnit.equals("Fahrenheit")) {
            if (toUnit.equals("Celsius")) {
                return (value - 32) * 5 / 9;
            } else if (toUnit.equals("Kelvin")) {
                return (value + 459.67) * 5 / 9;
            }
        } else if (fromUnit.equals("Kelvin")) {
            if (toUnit.equals("Celsius")) {
                return value - 273.15;
            } else if (toUnit.equals("Fahrenheit")) {
                return (value * 9 / 5) - 459.67;
            }
        }

        return 0.0;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TemperatureConverterGUI().setVisible(true);
            }
        });
    }
}
