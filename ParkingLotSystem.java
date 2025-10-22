import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

class Vehicle {
    String number;
    String type;
    LocalDateTime entryTime;

    public Vehicle(String number, String type) {
        this.number = number;
        this.type = type;
        this.entryTime = LocalDateTime.now();
    }
}

public class ParkingLotSystem extends JFrame {
    private HashMap<String, Vehicle> parkedVehicles = new HashMap<>();
    private int totalSlots = 5;
    private JComboBox<String> vehicleTypeCombo;
    private JTextField vehicleNumberField;
    private JTextArea statusArea;
    private JButton enterBtn, exitBtn;

    public ParkingLotSystem() {
        setTitle("Parking Lot Management System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for input
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Vehicle Number:"));
        vehicleNumberField = new JTextField(10);
        inputPanel.add(vehicleNumberField);

        inputPanel.add(new JLabel("Vehicle Type:"));
        vehicleTypeCombo = new JComboBox<>(new String[]{"Car", "Bike"});
        inputPanel.add(vehicleTypeCombo);

        enterBtn = new JButton("Enter");
        exitBtn = new JButton("Exit");
        inputPanel.add(enterBtn);
        inputPanel.add(exitBtn);

        add(inputPanel, BorderLayout.NORTH);

        // Status area
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        add(new JScrollPane(statusArea), BorderLayout.CENTER);

        // Button actions
        enterBtn.addActionListener(e -> vehicleEntry());
        exitBtn.addActionListener(e -> vehicleExit());

        updateStatus();
        setVisible(true);
    }

    private void vehicleEntry() {
        String number = vehicleNumberField.getText().trim();
        String type = (String) vehicleTypeCombo.getSelectedItem();

        if (number.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter vehicle number!");
            return;
        }

        if (parkedVehicles.size() >= totalSlots) {
            JOptionPane.showMessageDialog(this, "Parking full!");
            return;
        }

        if (parkedVehicles.containsKey(number)) {
            JOptionPane.showMessageDialog(this, "Vehicle already parked!");
            return;
        }

        Vehicle v = new Vehicle(number, type);
        parkedVehicles.put(number, v);
        statusArea.append("Vehicle Entered: " + number + " (" + type + ")\n");
        updateStatus();
    }

    private void vehicleExit() {
        String number = vehicleNumberField.getText().trim();

        if (!parkedVehicles.containsKey(number)) {
            JOptionPane.showMessageDialog(this, "Vehicle not found!");
            return;
        }

        Vehicle v = parkedVehicles.remove(number);
        LocalDateTime exitTime = LocalDateTime.now();
        long minutes = Duration.between(v.entryTime, exitTime).toMinutes();
        double fee = calculateFee(minutes, v.type);

        statusArea.append("Vehicle Exited: " + number + " (" + v.type + "), Duration: " + minutes + " mins, Fee: ₹" + fee + "\n");
        updateStatus();
    }

    private double calculateFee(long minutes, String type) {
        double ratePerHour = type.equals("Car") ? 20 : 10;
        double hours = Math.ceil(minutes / 60.0);
        return hours * ratePerHour;
    }

    private void updateStatus() {
        statusArea.append("\n--- Parking Status ---\n");
        statusArea.append("Total Slots: " + totalSlots + "\n");
        statusArea.append("Occupied: " + parkedVehicles.size() + "\n");
        statusArea.append("Available: " + (totalSlots - parkedVehicles.size()) + "\n");
        if (!parkedVehicles.isEmpty()) {
            statusArea.append("Parked Vehicles:\n");
            for (Vehicle v : parkedVehicles.values()) {
                statusArea.append(v.number + " (" + v.type + ") entered at " + v.entryTime + "\n");
            }
        }
        statusArea.append("---------------------\n\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParkingLotSystem::new);
    }
}
