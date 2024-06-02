/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import models.ReservaTickets;

/**
 *
 * @author JuanJoo
 */
public class frmMain extends javax.swing.JFrame {

    private final int TOTAL_ASIENTOS = 30;
    private final int[] FILAS = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
    private ReservaTickets reservaTickets;
    private JLabel[] lbAsientos;

    /**
     * Creates new form frmMain
     */
    public frmMain() {
        initComponents();
        reservaTickets = new ReservaTickets();
        lbAsientos = new JLabel[TOTAL_ASIENTOS];
        showAsientos();
        updateSeatsStatus();
        redirectSystemOutToTextArea();
    }

    public void showAsientos() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        int n = 0;
        for (int i = 0; i < FILAS.length; i++) {
            for (int j = 0; j < FILAS[i]; j++) {
                int seatNumber = n + 1; // Número de asiento
                lbAsientos[n] = new JLabel(" " + seatNumber + " ");
                lbAsientos[n].setBackground(Color.LIGHT_GRAY);
                lbAsientos[n].setOpaque(true);
                lbAsientos[n].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK),
                        BorderFactory.createEmptyBorder(5, 7, 5, 7)
                ));
                lbAsientos[n].setFont(lbAsientos[n].getFont().deriveFont(12.0f));
                lbAsientos[n].setHorizontalAlignment(SwingConstants.CENTER);

                gbc.gridx = j;
                gbc.gridy = i;
                pnlAsientos.add(lbAsientos[n], gbc); // añadir al panel

                n++;
            }
        }
    }

    private void updateSeatsStatus() {
        StyledDocument doc = pnPasajeros.getStyledDocument();
        pnPasajeros.setText("");

        for (Map.Entry<Integer, String> entry : reservaTickets.getSeats().entrySet()) {
            int seat = entry.getKey();
            String passenger = entry.getValue();
            String status = "Asiento " + seat + ": " + (passenger == null ? "Disponible" : passenger) + "\n";

            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
            if (passenger == null) {
                StyleConstants.setForeground(attributeSet, Color.LIGHT_GRAY);
                lbAsientos[seat - 1].setBackground(Color.LIGHT_GRAY); // Color gris para asientos disponibles
            } else {
                StyleConstants.setForeground(attributeSet, Color.BLUE);
                lbAsientos[seat - 1].setBackground(Color.BLUE); // Color azul para asientos reservados
            }

            try {
                doc.insertString(doc.getLength(), status, attributeSet);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private void logMessage(String message) {
        txtLog.append(message);
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
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

    private void reservar(String pasajero, String asiento, int ventanilla) {
        int seat = Integer.parseInt(asiento);
        if (!isEmpty(pasajero, asiento)) {
            if (rangeNumber(seat)) {
                String passenger = pasajero;
                Random random = new Random();
                int tiempoEspera = random.nextInt(5) + 1;
                new Thread(() -> {
                    try {
                        Thread.sleep(tiempoEspera * 1000); // Espera el tiempo en segundos convertido a milisegundos
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean success = reservaTickets.reserveSeat(seat, passenger);
                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            logMessage("Ventanilla " + ventanilla
                                    + ": Reservado por " + passenger + " el asiento No. " + seat + "\n");
                        } else {
                            logMessage("Ventanilla " + ventanilla
                                    + ": El asiento " + seat + " esta reservado. Pasajero " + passenger + " en espera.\n");
                        }
                        updateSeatsStatus();
                    });
                }).start();
            }
        }
    }

    private void eliminar(String pasajero, String asiento, int ventanilla) {
        int seat = Integer.parseInt(asiento);
        if (!isEmpty(pasajero, asiento)) {
            if (rangeNumber(seat)) {
                new Thread(() -> {
                    boolean success = reservaTickets.cancelReservation(seat);
                    SwingUtilities.invokeLater(() -> {
                        if (success) {
                            logMessage("Ventanilla " + ventanilla
                                    + ": Se cancela la reserva del asiento " + seat + "\n");
                        } else {
                            logMessage("Ventanilla " + ventanilla
                                    + ": El asiento " + seat + " no esta reservado.\n");
                        }
                        updateSeatsStatus();
                    });
                }).start();
            }
        }
    }

    private void cambiar(String pasajero, String asiento, int ventanilla) {
        int newSeat = Integer.parseInt(asiento);
        if (!isEmpty(pasajero, asiento)) {
            if (rangeNumber(newSeat)) {
                String passenger = pasajero;
                new Thread(() -> {
                    for (Map.Entry<Integer, String> entry : reservaTickets.getSeats().entrySet()) {
                        if (passenger.equals(entry.getValue())) {
                            boolean success = reservaTickets.changeSeat(entry.getKey(), newSeat, passenger);
                            SwingUtilities.invokeLater(() -> {
                                if (success) {
                                    logMessage("Ventanilla " + ventanilla
                                            + ": Se cambia asiento a " + newSeat + " por " + passenger + "\n");
                                } else {
                                    logMessage("Ventanilla " + ventanilla
                                            + ": Error al cambiar el asiento " + newSeat + " por " + passenger + ". Pasajero en espera.\n");
                                }
                                updateSeatsStatus();
                            });
                            break;
                        }
                    }
                }).start();
            }
        }
    }

    private boolean isEmpty(String nomCliente, String numAsiento) {
        if (numAsiento.isEmpty() || "Seleccionar..".equals(nomCliente)) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos para la reserva.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean rangeNumber(int asiento) {
        boolean estado = false;
        if (asiento > 0 && asiento <= TOTAL_ASIENTOS) {
            estado = true;
        } else {
            JOptionPane.showMessageDialog(this, "El numero de asiento esta fuera de rango.",
                    "Alerta", JOptionPane.WARNING_MESSAGE);
        }
        return estado;
    }

    private void singleNumbers(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnlVentanilla1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtAsientoV1 = new javax.swing.JTextField();
        btnReservarV1 = new javax.swing.JButton();
        btnEliminarV1 = new javax.swing.JButton();
        btnCambiarV1 = new javax.swing.JButton();
        cmbPasajerosV1 = new javax.swing.JComboBox<>();
        pnlAsientos = new javax.swing.JPanel();
        pnlVentanilla2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAsientoV2 = new javax.swing.JTextField();
        btnReservarV2 = new javax.swing.JButton();
        btnEliminarV2 = new javax.swing.JButton();
        btnCambiarV2 = new javax.swing.JButton();
        cmbPasajerosV2 = new javax.swing.JComboBox<>();
        pnlVentanilla3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtAsientoV3 = new javax.swing.JTextField();
        btnReservarV3 = new javax.swing.JButton();
        btnEliminarV3 = new javax.swing.JButton();
        btnCambiarV3 = new javax.swing.JButton();
        cmbPasajerosV3 = new javax.swing.JComboBox<>();
        btnPrueba = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnPasajeros = new javax.swing.JTextPane();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        pnlVentanilla1.setBackground(new java.awt.Color(255, 255, 255));
        pnlVentanilla1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ventanilla 1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel1.setText("Pasajero:");

        jLabel2.setText("No. Asiento:");

        txtAsientoV1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAsientoV1KeyTyped(evt);
            }
        });

        btnReservarV1.setBackground(new java.awt.Color(204, 255, 204));
        btnReservarV1.setText("Reservar");
        btnReservarV1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservarV1ActionPerformed(evt);
            }
        });

        btnEliminarV1.setBackground(new java.awt.Color(255, 204, 204));
        btnEliminarV1.setText("Eliminar");
        btnEliminarV1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarV1ActionPerformed(evt);
            }
        });

        btnCambiarV1.setBackground(new java.awt.Color(153, 204, 255));
        btnCambiarV1.setText("Cambiar");
        btnCambiarV1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarV1ActionPerformed(evt);
            }
        });

        cmbPasajerosV1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar..", "Anderson Jordan", "Juan Echeverria", "Luis Lino", "Erwin Solano", "Maria Mera", "Vicente Fernandez", "Pedro Navaja", "Juan Gabriel", "Raymon Ayala" }));

        javax.swing.GroupLayout pnlVentanilla1Layout = new javax.swing.GroupLayout(pnlVentanilla1);
        pnlVentanilla1.setLayout(pnlVentanilla1Layout);
        pnlVentanilla1Layout.setHorizontalGroup(
            pnlVentanilla1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentanilla1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentanilla1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlVentanilla1Layout.createSequentialGroup()
                        .addGroup(pnlVentanilla1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlVentanilla1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAsientoV1)
                            .addComponent(cmbPasajerosV1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlVentanilla1Layout.createSequentialGroup()
                        .addComponent(btnReservarV1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarV1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCambiarV1)))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        pnlVentanilla1Layout.setVerticalGroup(
            pnlVentanilla1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentanilla1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentanilla1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbPasajerosV1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVentanilla1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtAsientoV1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlVentanilla1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReservarV1)
                    .addComponent(btnEliminarV1)
                    .addComponent(btnCambiarV1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlAsientos.setBackground(new java.awt.Color(255, 255, 255));
        pnlAsientos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Asientos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        pnlVentanilla2.setBackground(new java.awt.Color(255, 255, 255));
        pnlVentanilla2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ventanilla 2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel3.setText("Pasajero:");

        jLabel4.setText("No. Asiento:");

        txtAsientoV2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAsientoV2KeyTyped(evt);
            }
        });

        btnReservarV2.setBackground(new java.awt.Color(204, 255, 204));
        btnReservarV2.setText("Reservar");
        btnReservarV2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservarV2ActionPerformed(evt);
            }
        });

        btnEliminarV2.setBackground(new java.awt.Color(255, 204, 204));
        btnEliminarV2.setText("Eliminar");
        btnEliminarV2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarV2ActionPerformed(evt);
            }
        });

        btnCambiarV2.setBackground(new java.awt.Color(153, 204, 255));
        btnCambiarV2.setText("Cambiar");
        btnCambiarV2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarV2ActionPerformed(evt);
            }
        });

        cmbPasajerosV2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar..", "Anderson Jordan", "Juan Echeverria", "Luis Lino", "Erwin Solano", "Maria Mera", "Vicente Fernandez", "Pedro Navaja", "Juan Gabriel", "Raymon Ayala" }));

        javax.swing.GroupLayout pnlVentanilla2Layout = new javax.swing.GroupLayout(pnlVentanilla2);
        pnlVentanilla2.setLayout(pnlVentanilla2Layout);
        pnlVentanilla2Layout.setHorizontalGroup(
            pnlVentanilla2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentanilla2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentanilla2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVentanilla2Layout.createSequentialGroup()
                        .addGroup(pnlVentanilla2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlVentanilla2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAsientoV2)
                            .addComponent(cmbPasajerosV2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlVentanilla2Layout.createSequentialGroup()
                        .addComponent(btnReservarV2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarV2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCambiarV2)
                        .addGap(0, 4, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlVentanilla2Layout.setVerticalGroup(
            pnlVentanilla2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentanilla2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentanilla2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbPasajerosV2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVentanilla2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAsientoV2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlVentanilla2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReservarV2)
                    .addComponent(btnEliminarV2)
                    .addComponent(btnCambiarV2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlVentanilla3.setBackground(new java.awt.Color(255, 255, 255));
        pnlVentanilla3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ventanilla 3", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel5.setText("Pasajero:");

        jLabel6.setText("No. Asiento:");

        txtAsientoV3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAsientoV3KeyTyped(evt);
            }
        });

        btnReservarV3.setBackground(new java.awt.Color(204, 255, 204));
        btnReservarV3.setText("Reservar");
        btnReservarV3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReservarV3ActionPerformed(evt);
            }
        });

        btnEliminarV3.setBackground(new java.awt.Color(255, 204, 204));
        btnEliminarV3.setText("Eliminar");
        btnEliminarV3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarV3ActionPerformed(evt);
            }
        });

        btnCambiarV3.setBackground(new java.awt.Color(153, 204, 255));
        btnCambiarV3.setText("Cambiar");
        btnCambiarV3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarV3ActionPerformed(evt);
            }
        });

        cmbPasajerosV3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar..", "Anderson Jordan", "Juan Echeverria", "Luis Lino", "Erwin Solano", "Maria Mera", "Vicente Fernandez", "Pedro Navaja", "Juan Gabriel", "Raymon Ayala" }));

        javax.swing.GroupLayout pnlVentanilla3Layout = new javax.swing.GroupLayout(pnlVentanilla3);
        pnlVentanilla3.setLayout(pnlVentanilla3Layout);
        pnlVentanilla3Layout.setHorizontalGroup(
            pnlVentanilla3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentanilla3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentanilla3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlVentanilla3Layout.createSequentialGroup()
                        .addGroup(pnlVentanilla3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlVentanilla3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAsientoV3)
                            .addComponent(cmbPasajerosV3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlVentanilla3Layout.createSequentialGroup()
                        .addComponent(btnReservarV3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarV3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCambiarV3)
                        .addGap(0, 4, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlVentanilla3Layout.setVerticalGroup(
            pnlVentanilla3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentanilla3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentanilla3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cmbPasajerosV3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlVentanilla3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAsientoV3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlVentanilla3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReservarV3)
                    .addComponent(btnEliminarV3)
                    .addComponent(btnCambiarV3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnPrueba.setBackground(new java.awt.Color(255, 255, 204));
        btnPrueba.setText("Prueba de concurrencia");
        btnPrueba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPruebaActionPerformed(evt);
            }
        });

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Log", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane2.setViewportView(txtLog);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pasajeros Asignados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jScrollPane1.setViewportView(pnPasajeros);

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/avion.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("GRUPO 7");

        jLabel10.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel10.setText("Sistema de Reservas de Tickets Aéreos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlAsientos, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlVentanilla2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlVentanilla1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlVentanilla3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrueba, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(209, 209, 209)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9))
                            .addComponent(jLabel10))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(16, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(pnlAsientos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pnlVentanilla1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlVentanilla2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlVentanilla3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPrueba)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReservarV1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservarV1ActionPerformed
        reservar(cmbPasajerosV1.getSelectedItem().toString(), txtAsientoV1.getText(), 1);
    }//GEN-LAST:event_btnReservarV1ActionPerformed

    private void btnReservarV2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservarV2ActionPerformed
        reservar(cmbPasajerosV2.getSelectedItem().toString(), txtAsientoV2.getText(), 2);
    }//GEN-LAST:event_btnReservarV2ActionPerformed

    private void btnReservarV3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReservarV3ActionPerformed
        reservar(cmbPasajerosV3.getSelectedItem().toString(), txtAsientoV3.getText(), 3);
    }//GEN-LAST:event_btnReservarV3ActionPerformed

    private void btnEliminarV1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarV1ActionPerformed
        int asiento = Integer.parseInt(txtAsientoV1.getText());
        eliminar(cmbPasajerosV1.getSelectedItem().toString(), txtAsientoV1.getText(), 1);
    }//GEN-LAST:event_btnEliminarV1ActionPerformed

    private void btnEliminarV2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarV2ActionPerformed
        int asiento = Integer.parseInt(txtAsientoV2.getText());
        eliminar(cmbPasajerosV2.getSelectedItem().toString(), txtAsientoV2.getText(), 2);
    }//GEN-LAST:event_btnEliminarV2ActionPerformed

    private void btnEliminarV3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarV3ActionPerformed
        int asiento = Integer.parseInt(txtAsientoV3.getText());
        eliminar(cmbPasajerosV3.getSelectedItem().toString(), txtAsientoV3.getText(), 3);
    }//GEN-LAST:event_btnEliminarV3ActionPerformed

    private void btnCambiarV1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarV1ActionPerformed
        int asiento = Integer.parseInt(txtAsientoV1.getText());
        cambiar(cmbPasajerosV1.getSelectedItem().toString(), txtAsientoV1.getText(), 1);
    }//GEN-LAST:event_btnCambiarV1ActionPerformed

    private void btnCambiarV2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarV2ActionPerformed
        int asiento = Integer.parseInt(txtAsientoV2.getText());
        cambiar(cmbPasajerosV2.getSelectedItem().toString(), txtAsientoV2.getText(), 2);
    }//GEN-LAST:event_btnCambiarV2ActionPerformed

    private void btnCambiarV3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarV3ActionPerformed
        int asiento = Integer.parseInt(txtAsientoV3.getText());
        cambiar(cmbPasajerosV3.getSelectedItem().toString(), txtAsientoV3.getText(), 3);
    }//GEN-LAST:event_btnCambiarV3ActionPerformed

    private void txtAsientoV1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAsientoV1KeyTyped
        singleNumbers(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_txtAsientoV1KeyTyped

    private void txtAsientoV2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAsientoV2KeyTyped
        singleNumbers(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_txtAsientoV2KeyTyped

    private void txtAsientoV3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAsientoV3KeyTyped
        singleNumbers(evt);
    }//GEN-LAST:event_txtAsientoV3KeyTyped

    private void btnPruebaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPruebaActionPerformed
        if (txtAsientoV1.getText().isEmpty() || 
                "Seleccionar..".equals(cmbPasajerosV1.getSelectedItem().toString()) ||
                txtAsientoV2.getText().isEmpty() || 
                "Seleccionar..".equals(cmbPasajerosV2.getSelectedItem().toString()) ||
                txtAsientoV3.getText().isEmpty() || 
                "Seleccionar..".equals(cmbPasajerosV3.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos para la reserva masiva.",
                    "Campos incompletos", JOptionPane.WARNING_MESSAGE);
        } else {
            reservar(cmbPasajerosV1.getSelectedItem().toString(), txtAsientoV1.getText(), 1);
            reservar(cmbPasajerosV2.getSelectedItem().toString(), txtAsientoV2.getText(), 2);
            reservar(cmbPasajerosV3.getSelectedItem().toString(), txtAsientoV3.getText(), 3);
        }
    }//GEN-LAST:event_btnPruebaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCambiarV1;
    private javax.swing.JButton btnCambiarV2;
    private javax.swing.JButton btnCambiarV3;
    private javax.swing.JButton btnEliminarV1;
    private javax.swing.JButton btnEliminarV2;
    private javax.swing.JButton btnEliminarV3;
    private javax.swing.JButton btnPrueba;
    private javax.swing.JButton btnReservarV1;
    private javax.swing.JButton btnReservarV2;
    private javax.swing.JButton btnReservarV3;
    private javax.swing.JComboBox<String> cmbPasajerosV1;
    private javax.swing.JComboBox<String> cmbPasajerosV2;
    private javax.swing.JComboBox<String> cmbPasajerosV3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane pnPasajeros;
    private javax.swing.JPanel pnlAsientos;
    private javax.swing.JPanel pnlVentanilla1;
    private javax.swing.JPanel pnlVentanilla2;
    private javax.swing.JPanel pnlVentanilla3;
    private javax.swing.JTextField txtAsientoV1;
    private javax.swing.JTextField txtAsientoV2;
    private javax.swing.JTextField txtAsientoV3;
    private javax.swing.JTextArea txtLog;
    // End of variables declaration//GEN-END:variables
}
