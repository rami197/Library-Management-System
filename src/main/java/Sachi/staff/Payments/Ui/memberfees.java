/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Sachi.staff.Payments.Ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class memberfees extends javax.swing.JFrame {

    /**
     * Creates new form memberfees
     */
    public memberfees() {
        initComponents();
    }

    public int retrieveMemberData() {
        String memberId = memberidbox.getText();
        int retrievedId = -1; 

        try ( Connection conn = new Helper.DatabaseConnection().connection()) {
            String sql = "SELECT B_ID, B_Name FROM borrower WHERE B_ID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, memberId);

            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    membername.setText(rs.getString("B_Name"));
                    membership.setText(rs.getString("B_ID"));
                    
                    retrievedId = rs.getInt("B_ID");
                } else {
                    JOptionPane.showMessageDialog(this, "Member not found");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Security_deposit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retrievedId;
    }
    
    public void registrationFeePayment() throws SQLException {

    int borrowerId = retrieveMemberData();

    if (borrowerId == -1) {
        JOptionPane.showMessageDialog(this, "No valid borrower selected. Please check the member ID.");
        return;
    }

    try (Connection conn = new Helper.DatabaseConnection().connection()) {

        // Retrieve member type (adult or child)
        String borrowerTypeQuery = "SELECT b_type FROM borrower WHERE B_ID = ?";
        PreparedStatement psBorrowerType = conn.prepareStatement(borrowerTypeQuery);
        psBorrowerType.setInt(1, borrowerId);
        ResultSet rsBorrowerType = psBorrowerType.executeQuery();

        String borrowerType = null;
        if (rsBorrowerType.next()) {
            borrowerType = rsBorrowerType.getString("b_type");
        }

        if (borrowerType == null) {
            JOptionPane.showMessageDialog(this, "Member type could not be determined. Please check the member ID.");
            return;
        }

        // Retrieve the fine_type_id for member registration
        int registrationFineTypeId = 7; // Assuming the fine_type_id for member registration fee is 7

        // Retrieve the registration fee amount based on the member type
        String feeAmountQuery = "SELECT fine_type_amount_for_Childrens, fine_type_amount_for_Adults FROM fines_type WHERE fine_type_id = ?";
        PreparedStatement psFeeAmount = conn.prepareStatement(feeAmountQuery);
        psFeeAmount.setInt(1, registrationFineTypeId);
        ResultSet rsFeeAmount = psFeeAmount.executeQuery();

        double feeAmount = 0.0;
        if (rsFeeAmount.next()) {
            if (borrowerType.equalsIgnoreCase("Child")) {
                feeAmount = rsFeeAmount.getDouble("fine_type_amount_for_Childrens");
            } else if (borrowerType.equalsIgnoreCase("Adult")) {
                feeAmount = rsFeeAmount.getDouble("fine_type_amount_for_Adults");
            }
        }

        if (feeAmount == 0.0) {
            JOptionPane.showMessageDialog(this, "Registration fee amount could not be determined.");
            return;
        }

        // Display the fee amount in the amountbox text box
        amountboxx.setText(String.valueOf(feeAmount));

        // Update the registration table with the fee payment record
        double registrationFeeAmount = Double.parseDouble(amountboxx.getText());

        String sql = "INSERT INTO registration_transactions (fine_type_id, B_ID, Amount) VALUES (?, ?, ?)";
        PreparedStatement psInsert = conn.prepareStatement(sql);

        psInsert.setInt(1, registrationFineTypeId);
        psInsert.setInt(2, borrowerId);
        psInsert.setDouble(3, registrationFeeAmount);

        psInsert.executeUpdate();

        JOptionPane.showMessageDialog(this, "Registration fee collected and recorded successfully!");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error processing registration fee!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
//for member renewal
    public void renewalFeePayment() throws SQLException {

    int borrowerId = retrieveMemberData();

    if (borrowerId == -1) {
        JOptionPane.showMessageDialog(this, "No valid borrower selected. Please check the member ID.");
        return;
    }

    try (Connection conn = new Helper.DatabaseConnection().connection()) {

        // Retrieve member type (adult or child)
        String borrowerTypeQuery = "SELECT b_type FROM borrower WHERE B_ID = ?";
        PreparedStatement psBorrowerType = conn.prepareStatement(borrowerTypeQuery);
        psBorrowerType.setInt(1, borrowerId);
        ResultSet rsBorrowerType = psBorrowerType.executeQuery();

        String borrowerType = null;
        if (rsBorrowerType.next()) {
            borrowerType = rsBorrowerType.getString("b_type");
        }

        if (borrowerType == null) {
            JOptionPane.showMessageDialog(this, "Member type could not be determined. Please check the member ID.");
            return;
        }

        // Retrieve the fine_type_id for member renewal
        int renewalFineTypeId = 8; // Assuming the fine_type_id for member renewal fee is 8

        // Retrieve the renewal fee amount based on the member type
        String feeAmountQuery = "SELECT fine_type_amount_for_Childrens, fine_type_amount_for_Adults FROM fines_type WHERE fine_type_id = ?";
        PreparedStatement psFeeAmount = conn.prepareStatement(feeAmountQuery);
        psFeeAmount.setInt(1, renewalFineTypeId);
        ResultSet rsFeeAmount = psFeeAmount.executeQuery();

        double feeAmount = 0.0;
        if (rsFeeAmount.next()) {
            if (borrowerType.equalsIgnoreCase("child")) {
                feeAmount = rsFeeAmount.getDouble("fine_type_amount_for_Childrens");
            } else if (borrowerType.equalsIgnoreCase("adult")) {
                feeAmount = rsFeeAmount.getDouble("fine_type_amount_for_Adults");
            }
        }

        if (feeAmount == 0.0) {
            JOptionPane.showMessageDialog(this, "Renewal fee amount could not be determined.");
            return;
        }

        // Display the fee amount in the amountbox text box
        amountboxx.setText(String.valueOf(feeAmount));

        // Update the registration table with the fee payment record
        double renewalFeeAmount = Double.parseDouble(amountboxx.getText());

        String sql = "INSERT INTO registration_transactions (fine_type_id, B_ID, Amount) VALUES (?, ?, ?)";
        PreparedStatement psInsert = conn.prepareStatement(sql);

        psInsert.setInt(1, renewalFineTypeId);
        psInsert.setInt(2, borrowerId);
        psInsert.setDouble(3, renewalFeeAmount);

        psInsert.executeUpdate();

        JOptionPane.showMessageDialog(this, "Renewal fee collected and recorded successfully!");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error processing renewal fee!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
     public void clearTextBoxes() {
        amountboxx.setText("");
        memberidbox.setText("");
        membership.setText("");
        membername.setText("");
        amountboxx.setText("");
     }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        amountboxx = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        membership = new javax.swing.JLabel();
        membername = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        memberidbox = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Goudy Old Style", 0, 20), new java.awt.Color(51, 102, 0))); // NOI18N

        amountboxx.setBackground(new java.awt.Color(0, 0, 0));
        amountboxx.setForeground(new java.awt.Color(204, 255, 0));
        amountboxx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amountboxxActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Amount Paid");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText(" Membership No");

        membership.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        membership.setForeground(new java.awt.Color(204, 255, 0));
        membership.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        membername.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        membername.setForeground(new java.awt.Color(204, 255, 0));
        membername.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel20.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Name");

        jLabel21.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Print receipt");

        jButton3.setBackground(new java.awt.Color(51, 102, 0));
        jButton3.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Lib_icons/icons8-print-30.png"))); // NOI18N
        jButton3.setText("Print");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(182, 182, 182)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(membership, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel21))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                    .addComponent(amountboxx)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(18, 18, 18)
                                .addComponent(membername, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel8))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(membership, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(membername, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(amountboxx, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jButton3))
                .addGap(16, 16, 16))
        );

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/search_4.png"))); // NOI18N
        jLabel3.setText("Search Members by member ID");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        memberidbox.setBackground(new java.awt.Color(0, 0, 0));
        memberidbox.setForeground(new java.awt.Color(204, 255, 0));
        memberidbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memberidboxActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/download (4)-removebg-preview (1).jpg"))); // NOI18N

        jButton2.setBackground(new java.awt.Color(0, 102, 102));
        jButton2.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Renew");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 153, 0));
        jButton1.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Registration");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(memberidbox, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(memberidbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(57, 57, 57)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 1100, 430));

        jPanel11.setBackground(new java.awt.Color(51, 102, 0));

        jLabel18.setBackground(new java.awt.Color(204, 255, 204));
        jLabel18.setFont(new java.awt.Font("Georgia", 0, 20)); // NOI18N
        jLabel18.setText("Dunkannawa E- Library >>");

        jLabel19.setBackground(new java.awt.Color(204, 255, 204));
        jLabel19.setFont(new java.awt.Font("Georgia", 0, 20)); // NOI18N
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/username_4.png"))); // NOI18N
        jLabel19.setText("Welcome to User Account");

        jPanel13.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jLabel13.setBackground(new java.awt.Color(204, 255, 204));
        jLabel13.setFont(new java.awt.Font("Georgia", 0, 20)); // NOI18N
        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel13.setText("Home >>");
        jLabel13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel13MouseClicked(evt);
            }
        });

        jLabel14.setBackground(new java.awt.Color(204, 255, 204));
        jLabel14.setFont(new java.awt.Font("Jokerman", 2, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 51, 0));

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Collect Membership  & Renewal Fees");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 335, Short.MAX_VALUE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel19)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(-30, 0, 1140, 80));

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(51, 102, 0)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 966, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 570, 970, 30));

        jButton4.setBackground(new java.awt.Color(51, 102, 0));
        jButton4.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Cancel");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 530, 146, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1120, 610));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void amountboxxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amountboxxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_amountboxxActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        retrieveMemberData();// Call the retrieveBookData function
    }//GEN-LAST:event_jLabel3MouseClicked

    private void memberidboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memberidboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_memberidboxActionPerformed

    private void jLabel13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel13MouseClicked
        Sachi.ui.Staff.Home_pg_1 homeback = new Sachi.ui.Staff.Home_pg_1();
        homeback.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel13MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            registrationFeePayment();
        } catch (SQLException ex) {
            Logger.getLogger(memberfees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            renewalFeePayment();        // TODO add your handling code here:
        } catch (SQLException ex) {
            Logger.getLogger(memberfees.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
clearTextBoxes();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(memberfees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(memberfees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(memberfees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(memberfees.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new memberfees().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amountboxx;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField memberidbox;
    private javax.swing.JLabel membername;
    private javax.swing.JLabel membership;
    // End of variables declaration//GEN-END:variables
}
