package main;

import gui.frmMain;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
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
    
    private JPanel pnlAsientos;
    private JLabel[] seatsLabels;
    
    private static final int[] FILAS = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3}; // Número de asientos en cada fila
    private static final int TOTAL_ASIENTOS = 30;

    public Main(Airplane airplane) {
        this.airplane = airplane;
        setTitle("Reservas de tickets aéreos");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        seatsStatusPane = new JTextPane();
        seatsStatusPane.setEditable(false);
        JScrollPane seatsScrollPane = new JScrollPane(seatsStatusPane);

        pnlAsientos = new JPanel(new GridBagLayout());
        pnlAsientos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(pnlAsientos, BorderLayout.CENTER);

        seatsLabels = new JLabel[TOTAL_ASIENTOS];
        showAsientos();
        updateSeatsStatus();

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createWindowsPanel(), logScrollPane);
        splitPane.setResizeWeight(0.5);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(pnlAsientos, BorderLayout.WEST);
        mainPanel.add(seatsScrollPane, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        redirectSystemOutToTextArea();
    }
    
    private void showAsientos() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Ajustar los valores según tus necesidades

        int n = 0;
        for (int i = 0; i < FILAS.length; i++) {
            for (int j = 0; j < FILAS[i]; j++) {
                int seatNumber = n + 1; // Número de asiento
                seatsLabels[n] = new JLabel(" " + seatNumber + " ");
                seatsLabels[n].setBackground(Color.LIGHT_GRAY);
                seatsLabels[n].setOpaque(true);
                seatsLabels[n].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK),
                        BorderFactory.createEmptyBorder(5, 7, 5, 7)
                ));
                seatsLabels[n].setFont(seatsLabels[n].getFont().deriveFont(12.0f));
                seatsLabels[n].setHorizontalAlignment(SwingConstants.CENTER);

                gbc.gridx = j;
                gbc.gridy = i;
                pnlAsientos.add(seatsLabels[n], gbc); // añadir al panel

                n++;
            }
        }
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

        JButton reserveButton = new JButton("Reservar");
        JButton cancelButton = new JButton("Cancelar");
        JButton changeButton = new JButton("Cambiar");

        panel.add(new JLabel("Pasajero:"));
        panel.add(passengerField);
        panel.add(new JLabel("Asiento:"));
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
                            logMessage("Reservado por " + passenger + " el asiento No. " + seat + "\n");
                        } else {
                            logMessage("El asiento " + seat + " esta reservado. Pasajero " + passenger + " en espera.\n");
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
                            logMessage("Se cancela la reserva del asiento " + seat);
                        } else {
                            logMessage("El asiento " + seat + " no esta reservado.\n");
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
                                    logMessage("Se cambia asiento a " + newSeat + " por " + passenger + "\n");
                                } else {
                                    logMessage("Error al cambiar el asiento " + newSeat + " por " + passenger + ". Pasajero en espera.\n");
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
        StyledDocument doc = seatsStatusPane.getStyledDocument();
        seatsStatusPane.setText("");
        
        for (Map.Entry<Integer, String> entry : airplane.getSeats().entrySet()) {
            int seat = entry.getKey();
            String passenger = entry.getValue();
            String status = "Asiento " + seat + ": " + (passenger == null ? "Disponible" : passenger) + "\n";

            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
            if (passenger == null) {
                StyleConstants.setForeground(attributeSet, Color.BLACK);
                seatsLabels[seat - 1].setBackground(Color.LIGHT_GRAY); // Color rojo para asientos disponibles
            } else {
                SimpleAttributeSet redBold = new SimpleAttributeSet();
                StyleConstants.setForeground(attributeSet, Color.BLUE);
                seatsLabels[seat - 1].setBackground(Color.BLUE); // Color verde para asientos reservados
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
