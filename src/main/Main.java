package main;

import gui.frmMain;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import models.Airplane;

public class Main extends JFrame{

    private Airplane airplane;
    private JTextPane seatsStatusPane;
    private JTextArea logArea;

    public Main(Airplane airplane) {
        this.airplane = airplane;
        setTitle("Airplane Reservation System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        seatsStatusPane = new JTextPane();
        seatsStatusPane.setEditable(false);
        JScrollPane seatsScrollPane = new JScrollPane(seatsStatusPane);
        updateSeatsStatus();

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createWindowsPanel(), logScrollPane);
        splitPane.setResizeWeight(0.5);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(seatsScrollPane, BorderLayout.WEST);

        add(mainPanel, BorderLayout.CENTER);

        redirectSystemOutToTextArea();
    }

    private JPanel createWindowsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));

        for (int i = 0; i < 3; i++) {
            panel.add(createWindow());
        }

        return panel;
    }

    private JPanel createWindow() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        JTextField passengerField = new JTextField();
        JTextField seatField = new JTextField();

        JButton reserveButton = new JButton("Reserve");
        JButton cancelButton = new JButton("Cancel");
        JButton changeButton = new JButton("Change");

        panel.add(new JLabel("Passenger:"));
        panel.add(passengerField);
        panel.add(new JLabel("Seat:"));
        panel.add(seatField);
        panel.add(reserveButton);
        panel.add(cancelButton);
        panel.add(changeButton);

        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String passenger = passengerField.getText();
                int seat = Integer.parseInt(seatField.getText());
                new Thread(() -> {
                    boolean success = airplane.reserveSeat(seat, passenger);
                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            logMessage("Reservation successful for " + passenger + " at seat " + seat + "\n");
                        } else {
                            logMessage("Seat " + seat + " is already reserved. Added " + passenger + " to the waiting list.\n");
                        }
                        updateSeatsStatus();
                    });
                }).start();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seat = Integer.parseInt(seatField.getText());
                new Thread(() -> {
                    boolean success = airplane.cancelReservation(seat);
                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            logMessage("Reservation cancelled at seat " + seat);
                        } else {
                            logMessage("Seat " + seat + " is not reserved.\n");
                        }
                        updateSeatsStatus();
                    });
                }).start();
            }
        });

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String passenger = passengerField.getText();
                int newSeat = Integer.parseInt(seatField.getText());
                new Thread(() -> {
                    for (Map.Entry<Integer, String> entry : airplane.getSeats().entrySet()) {
                        if (passenger.equals(entry.getValue())) {
                            boolean success = airplane.changeSeat(entry.getKey(), newSeat, passenger);
                            SwingUtilities.invokeLater(() -> {
                                if (success) {
                                    logMessage("Seat changed to " + newSeat + " for " + passenger + "\n");
                                } else {
                                    logMessage("Failed to change seat to " + newSeat + " for " + passenger + ". Added to the waiting list.\n");
                                }
                                updateSeatsStatus();
                            });
                            break;
                        }
                    }
                }).start();
            }
        });

        return panel;
    }
    
    private void updateSeatsStatus() {
        seatsStatusPane.setText("");
        StyledDocument doc = seatsStatusPane.getStyledDocument();
        
        for (Map.Entry<Integer, String> entry : airplane.getSeats().entrySet()) {
            int seat = entry.getKey();
            String passenger = entry.getValue();
            String status = "Seat " + seat + ": " + (passenger == null ? "Available" : passenger) + "\n";

            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
            if (passenger == null) {
                StyleConstants.setForeground(attributeSet, Color.GREEN);
            } else {
                StyleConstants.setForeground(attributeSet, Color.RED);
            }
            
            try {
                doc.insertString(doc.getLength(), status, attributeSet);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void logMessage(String message) {
        logArea.append(message);
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void redirectSystemOutToTextArea() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                logMessage(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) {
                logMessage(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) {
                write(b, 0, b.length);
            }
        };

        PrintStream printStream = new PrintStream(out);
        System.setOut(printStream);
        System.setErr(printStream);
    }

    public static void main(String[] args) {
        Airplane airplane = new Airplane();
        Main frame = new Main(airplane);
        frame.setVisible(true);
    }
}
