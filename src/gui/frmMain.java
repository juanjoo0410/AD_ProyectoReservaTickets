/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author JuanJoo
 */
public class frmMain extends javax.swing.JFrame {
    private final int TOTAL_ASIENTOS = 30;
    private JLabel[] asientos;

    /**
     * Creates new form frmMain
     */
    public frmMain() {
        initComponents();
        showAsientos();
    }

    public void showAsientos (){
        asientos = new JLabel[TOTAL_ASIENTOS];
                
        pnlAsientos.setLayout(new GridBagLayout()); // establecer el layout
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Establecer un margen externo alrededor de la cuadrícula
        gbc.insets = new Insets(5, 5, 5, 5); // Ajusta los valores según tus necesidades

        int[] filas = {6, 6, 6, 6, 6}; // número de asientos en cada fila
    
        int n = 0;
        for(int i = 0; i < filas.length; i++){
            for(int j = 0; j < filas[i]; j++){
                asientos[n] = new JLabel(" " + (n + 1)+ " ");
                asientos[n].setBackground(Color.LIGHT_GRAY);
                asientos[n].setOpaque(true);
                asientos[n].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK),
                    BorderFactory.createEmptyBorder(5, 7, 5, 7)
                ));
                asientos[n].setFont(asientos[n].getFont().deriveFont(12.0f));
    
                gbc.gridx = j;
                gbc.gridy = i;
                pnlAsientos.add(asientos[n], gbc); // añadir al panel
    
                n++;
            }
        }
        //Logica.setArreglo_asientos(Arreglo_asientos);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnlVentanilla1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPasajeroV1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtAsientoV1 = new javax.swing.JTextField();
        btnReservarV1 = new javax.swing.JButton();
        btnEliminarV1 = new javax.swing.JButton();
        btnCambiarV1 = new javax.swing.JButton();
        pnlAsientos = new javax.swing.JPanel();
        pnlVentanilla2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtPasajeroV2 = new javax.swing.JTextField();
        txtAsientoV2 = new javax.swing.JTextField();
        btnReservarV2 = new javax.swing.JButton();
        btnEliminarV2 = new javax.swing.JButton();
        btnCambiarV2 = new javax.swing.JButton();
        pnlVentanilla3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPasajeroV3 = new javax.swing.JTextField();
        txtAsientoV3 = new javax.swing.JTextField();
        btnReservarV3 = new javax.swing.JButton();
        btnEliminarV3 = new javax.swing.JButton();
        btnCambiarV3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtLog = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        pnlVentanilla1.setBackground(new java.awt.Color(255, 255, 255));
        pnlVentanilla1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ventanilla 1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jLabel1.setText("Pasajero:");

        jLabel2.setText("No. Asiento:");

        btnReservarV1.setBackground(new java.awt.Color(204, 255, 204));
        btnReservarV1.setText("Reservar");

        btnEliminarV1.setBackground(new java.awt.Color(255, 204, 204));
        btnEliminarV1.setText("Eliminar");

        btnCambiarV1.setBackground(new java.awt.Color(153, 204, 255));
        btnCambiarV1.setText("Cambiar");

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
                            .addComponent(txtPasajeroV1)))
                    .addGroup(pnlVentanilla1Layout.createSequentialGroup()
                        .addComponent(btnReservarV1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarV1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCambiarV1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlVentanilla1Layout.setVerticalGroup(
            pnlVentanilla1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentanilla1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentanilla1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPasajeroV1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        btnReservarV2.setBackground(new java.awt.Color(204, 255, 204));
        btnReservarV2.setText("Reservar");

        btnEliminarV2.setBackground(new java.awt.Color(255, 204, 204));
        btnEliminarV2.setText("Eliminar");

        btnCambiarV2.setBackground(new java.awt.Color(153, 204, 255));
        btnCambiarV2.setText("Cambiar");

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
                            .addComponent(txtPasajeroV2)
                            .addComponent(txtAsientoV2)))
                    .addGroup(pnlVentanilla2Layout.createSequentialGroup()
                        .addComponent(btnReservarV2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarV2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCambiarV2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlVentanilla2Layout.setVerticalGroup(
            pnlVentanilla2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentanilla2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentanilla2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtPasajeroV2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        btnReservarV3.setBackground(new java.awt.Color(204, 255, 204));
        btnReservarV3.setText("Reservar");

        btnEliminarV3.setBackground(new java.awt.Color(255, 204, 204));
        btnEliminarV3.setText("Eliminar");

        btnCambiarV3.setBackground(new java.awt.Color(153, 204, 255));
        btnCambiarV3.setText("Cambiar");

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
                            .addComponent(txtPasajeroV3)
                            .addComponent(txtAsientoV3)))
                    .addGroup(pnlVentanilla3Layout.createSequentialGroup()
                        .addComponent(btnReservarV3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarV3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCambiarV3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlVentanilla3Layout.setVerticalGroup(
            pnlVentanilla3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlVentanilla3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlVentanilla3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtPasajeroV3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jButton1.setBackground(new java.awt.Color(255, 255, 204));
        jButton1.setText("Prueba de concurrencia");

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Log", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        txtLog.setColumns(20);
        txtLog.setRows(5);
        jScrollPane2.setViewportView(txtLog);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pasajeros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 267, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pnlVentanilla2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlVentanilla1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlAsientos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlVentanilla3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pnlVentanilla1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlVentanilla2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pnlAsientos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(pnlVentanilla3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addGap(0, 10, Short.MAX_VALUE))
                            .addComponent(jScrollPane2))
                        .addGap(6, 6, 6))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCambiarV1;
    private javax.swing.JButton btnCambiarV2;
    private javax.swing.JButton btnCambiarV3;
    private javax.swing.JButton btnEliminarV1;
    private javax.swing.JButton btnEliminarV2;
    private javax.swing.JButton btnEliminarV3;
    private javax.swing.JButton btnReservarV1;
    private javax.swing.JButton btnReservarV2;
    private javax.swing.JButton btnReservarV3;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel pnlAsientos;
    private javax.swing.JPanel pnlVentanilla1;
    private javax.swing.JPanel pnlVentanilla2;
    private javax.swing.JPanel pnlVentanilla3;
    private javax.swing.JTextField txtAsientoV1;
    private javax.swing.JTextField txtAsientoV2;
    private javax.swing.JTextField txtAsientoV3;
    private javax.swing.JTextArea txtLog;
    private javax.swing.JTextField txtPasajeroV1;
    private javax.swing.JTextField txtPasajeroV2;
    private javax.swing.JTextField txtPasajeroV3;
    // End of variables declaration//GEN-END:variables
}
