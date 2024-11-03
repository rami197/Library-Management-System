/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Sachi.ui.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class childframe extends javax.swing.JInternalFrame {

   
    public childframe() {
        initComponents();
    }
public javax.swing.JTextField  getSchoolbox1() {
        return moccupationbox1;
    }
//    public void insertChildMemberData() {
//    try (Connection conn = new Helper.DatabaseConnection().connection()) {
//        // Collect data from AdultMemberFrame
//        String occupationSchool= moccupationbox1.getText();
//        
//        
//        
//        
//        // SQL Insert statement
//        String sql = "INSERT INTO borrower( B_occupation) VALUES (?)";
//        
//        try (PreparedStatement pst = conn.prepareStatement(sql)) {
//            pst.setString(1, occupationSchool);
//            
//            
//            pst.executeUpdate();
//            JOptionPane.showMessageDialog(this, "Child member registration is successful");
//        }
//    } catch (SQLException e) {
//        e.printStackTrace();
//        JOptionPane.showMessageDialog(this, "Failed to insert Child member data");
//    }
//}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel26 = new javax.swing.JLabel();
        moccupationbox1 = new javax.swing.JTextField();

        setBackground(new java.awt.Color(51, 102, 0));
        setClosable(true);
        setTitle("For Child Patrent");

        jLabel26.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("School Name");

        moccupationbox1.setBackground(new java.awt.Color(0, 0, 0));
        moccupationbox1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        moccupationbox1.setForeground(new java.awt.Color(204, 102, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(moccupationbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(477, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26)
                    .addComponent(moccupationbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel26;
    private javax.swing.JTextField moccupationbox1;
    // End of variables declaration//GEN-END:variables
}
