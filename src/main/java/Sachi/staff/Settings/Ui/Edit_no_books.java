
package Sachi.staff.Settings.Ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class Edit_no_books extends javax.swing.JFrame {

    
    public Edit_no_books() {
        initComponents();
    }
private void UpdateBookCount() {
    String memberIdStr = memberidbox.getText().trim();
    String bookCountStr = bookamount.getText().trim();
    
    if (memberIdStr.isEmpty() || bookCountStr.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Member ID and book amount cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int memberId;
    int bookCount;

    try {
        memberId = Integer.parseInt(memberIdStr);
        bookCount = Integer.parseInt(bookCountStr);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Member ID and book amount must be valid!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String sql = "UPDATE borrower SET No_of_Books_Issue = ? WHERE B_ID = ?";

    try (Connection conn = new Helper.DatabaseConnection().connection(); 
         PreparedStatement p = conn.prepareStatement(sql)) {

        p.setInt(1, bookCount);
        p.setInt(2, memberId);

        
        int rowsUpdated = p.executeUpdate();
        
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(null, "Book amount updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "No member found with the given ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error updating book count: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        memberidbox = new app.bolivia.swing.JCTextField();
        rSButtonHover1 = new rojeru_san.complementos.RSButtonHover();
        jPanel2 = new javax.swing.JPanel();
        bookamount = new app.bolivia.swing.JCTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Member_ID");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 114, 102, 60));

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Edit Issued book amount");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 194, 54));

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Books Amount");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 180, 112, 60));

        memberidbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memberidboxActionPerformed(evt);
            }
        });
        jPanel1.add(memberidbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, -1, -1));

        rSButtonHover1.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover1.setText("Ok");
        rSButtonHover1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover1ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 250, 94, -1));

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, -1, -1));

        bookamount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookamountActionPerformed(evt);
            }
        });
        jPanel1.add(bookamount, new org.netbeans.lib.awtextra.AbsoluteConstraints(136, 197, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 300));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void rSButtonHover1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover1ActionPerformed
        UpdateBookCount();
    }//GEN-LAST:event_rSButtonHover1ActionPerformed

    private void memberidboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memberidboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_memberidboxActionPerformed

    private void bookamountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookamountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bookamountActionPerformed

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
            java.util.logging.Logger.getLogger(Edit_no_books.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Edit_no_books.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Edit_no_books.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Edit_no_books.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Edit_no_books().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private app.bolivia.swing.JCTextField bookamount;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private app.bolivia.swing.JCTextField memberidbox;
    private rojeru_san.complementos.RSButtonHover rSButtonHover1;
    // End of variables declaration//GEN-END:variables
}
