package Jframe;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Signup_page extends javax.swing.JFrame {

    public Signup_page() {
        initComponents();

    }

    public void InsertSignupDetails() {

        String accountType = (String) jComboBox1.getSelectedItem();
        String name = txt_Username1.getText();
        String password = jPasswordField1.getText();
        String mobileNo = txt_Mobileno.getText();
        String email = txt_Email.getText();

        try ( Connection conn = new Helper.DatabaseConnection().connection();) {

            if ("Librarian".equals(accountType) && librarianAccountExists()) {
                JOptionPane.showMessageDialog(this, "A librarian account already exists. Only one librarian account is allowed.");
                return;
            }
            String sql = "insert into users(Role_enum,user_name,password,contact_No,email)VALUES(?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, accountType);
            pst.setString(2, name);
            pst.setString(3, password);
            pst.setString(4, mobileNo);
            pst.setString(5, email);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account created successfully");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Account Creation Failed,This account is already exists");
        }

    }

    private boolean librarianAccountExists() {
        try ( Connection conn = new Helper.DatabaseConnection().connection();) {
            String sql = "SELECT COUNT(*) FROM users WHERE account_type = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "Librarian");
            ResultSet rs = pst.executeQuery();
            if (rs.getInt(1) != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean ValidateSignup() {
        String accountType = (String) jComboBox1.getSelectedItem();
        String name = txt_Username1.getText();
        String password = jPasswordField1.getText();
        String mobileNo = txt_Mobileno.getText();
        String email = txt_Email.getText();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter username");
            return false;
        }
        if (password.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter password");
            return false;
        }
        if (mobileNo.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter MobileNo");
            return false;
        }
        if (email.equals("") || !email.matches("^.+@.+\\..+$")) {
            JOptionPane.showMessageDialog(this, "Please enter valid E-mail");
            return false;
        }
        if (accountType.equals("")) {
            JOptionPane.showMessageDialog(this, "Please select the Role");
            return false;
        }
        return true;
    }
    

    public boolean checkDuplicateUser() {
        String name = jPasswordField1.getText();
        boolean isExist = false;
        try ( Connection conn = new Helper.DatabaseConnection().connection();) {

            PreparedStatement pst = conn.prepareStatement("SELECT * from users where name = ?");
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                isExist = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isExist;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jLabel4 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_Mobileno = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_Email = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        hide = new javax.swing.JButton();
        txt_Username1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        show = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 51, 204));
        setSize(new java.awt.Dimension(990, 830));
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 200, -1));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setForeground(new java.awt.Color(51, 102, 0));
        jPanel2.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 102, 0));
        jLabel1.setText("Welcome to  ");

        jLabel5.setFont(new java.awt.Font("Georgia", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 102, 0));
        jLabel5.setText("Dunkannawa E-Library");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/signup-library-icon.png"))); // NOI18N

        jDesktopPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 334, Short.MAX_VALUE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 710, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel16.setBackground(new java.awt.Color(51, 102, 0));
        jLabel16.setFont(new java.awt.Font("Jokerman", 1, 20)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 102, 0));
        jLabel16.setText("League ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(281, 281, 281)
                                .addComponent(jLabel1))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(161, 161, 161)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(497, 497, 497)
                    .addComponent(jLabel16)
                    .addContainerGap(497, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(76, 76, 76)
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(620, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(761, 761, 761)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(762, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 1560));

        jPanel3.setBackground(new java.awt.Color(51, 102, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Georgia", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Sign up Page");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, -1, -1));

        jLabel7.setFont(new java.awt.Font("Georgia", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Create New Account");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, -1, -1));

        txt_Mobileno.setBackground(new java.awt.Color(51, 102, 0));
        txt_Mobileno.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txt_Mobileno.setForeground(new java.awt.Color(255, 255, 255));
        txt_Mobileno.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(153, 153, 153)));
        txt_Mobileno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_MobilenoActionPerformed(evt);
            }
        });
        jPanel3.add(txt_Mobileno, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 510, 370, 30));

        jLabel8.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Password");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 350, -1, 30));

        jLabel9.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Mobile No");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 460, 100, 40));

        jLabel10.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("E -Mail");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 540, -1, 30));

        jLabel11.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Conference_26px.png"))); // NOI18N
        jLabel11.setText("    Role");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 160, 30));

        txt_Email.setBackground(new java.awt.Color(51, 102, 0));
        txt_Email.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        txt_Email.setForeground(new java.awt.Color(255, 255, 255));
        txt_Email.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(153, 153, 153)));
        txt_Email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_EmailActionPerformed(evt);
            }
        });
        jPanel3.add(txt_Email, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 580, 370, 30));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/username.png"))); // NOI18N
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 50, 45));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/password.png"))); // NOI18N
        jLabel13.setText("jLabel13");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 370, 50, 50));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/telephone.png"))); // NOI18N
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, 60, 50));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/E-mail.png"))); // NOI18N
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 50, 30));

        jButton1.setBackground(new java.awt.Color(204, 255, 204));
        jButton1.setFont(new java.awt.Font("Dubai", 1, 17)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("LOGIN");
        jButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 690, 200, 40));

        jButton2.setBackground(new java.awt.Color(204, 255, 204));
        jButton2.setFont(new java.awt.Font("Dubai", 1, 17)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("SIGNUP");
        jButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 690, 200, 40));

        jLabel18.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Username");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, -1, 30));

        jComboBox1.setBackground(new java.awt.Color(51, 102, 0));
        jComboBox1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Librarian ", "Staff member" }));
        jPanel3.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 210, 260, -1));

        hide.setBackground(new java.awt.Color(0, 0, 0));
        hide.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        hide.setForeground(new java.awt.Color(255, 255, 255));
        hide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/hiding (1).png"))); // NOI18N
        hide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hideMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hideMouseReleased(evt);
            }
        });
        hide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideActionPerformed(evt);
            }
        });
        jPanel3.add(hide, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 400, 50, -1));

        txt_Username1.setBackground(new java.awt.Color(51, 102, 0));
        txt_Username1.setFont(new java.awt.Font("Georgia", 0, 17)); // NOI18N
        txt_Username1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(153, 153, 153)));
        txt_Username1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Username1ActionPerformed(evt);
            }
        });
        jPanel3.add(txt_Username1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 370, 30));

        jPasswordField1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jPanel3.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 400, 300, 40));

        show.setBackground(new java.awt.Color(0, 0, 0));
        show.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        show.setForeground(new java.awt.Color(255, 255, 255));
        show.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/show.png"))); // NOI18N
        show.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                showMouseReleased(evt);
            }
        });
        show.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showActionPerformed(evt);
            }
        });
        jPanel3.add(show, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, 50, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 0, 560, 820));

        setSize(new java.awt.Dimension(1274, 821));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_EmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_EmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_EmailActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (ValidateSignup() == true) {
            InsertSignupDetails();
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowGainedFocus

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        Jframe.Login_page log = new Jframe.Login_page();
        log.show();
        dispose();


    }//GEN-LAST:event_jButton1MouseClicked

    private void txt_MobilenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_MobilenoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_MobilenoActionPerformed

    private void txt_Username1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Username1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Username1ActionPerformed

    private void hideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hideActionPerformed

    }//GEN-LAST:event_hideActionPerformed

    private void hideMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hideMouseReleased
        hide.setVisible(false);
        show.setVisible(true);
        jPasswordField1.setEchoChar('*');

    }//GEN-LAST:event_hideMouseReleased

    private void hideMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hideMouseClicked


    }//GEN-LAST:event_hideMouseClicked

    private void showMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showMouseClicked
        hide.setVisible(true);
        show.setVisible(false);
        jPasswordField1.setEchoChar((char) 0);

        

    }//GEN-LAST:event_showMouseClicked

    private void showMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showMouseReleased

    }//GEN-LAST:event_showMouseReleased

    private void showActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_showActionPerformed

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
            java.util.logging.Logger.getLogger(Signup_page.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Signup_page.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Signup_page.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Signup_page.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Signup_page().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton hide;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JButton show;
    private javax.swing.JTextField txt_Email;
    private javax.swing.JTextField txt_Mobileno;
    private javax.swing.JTextField txt_Username1;
    // End of variables declaration//GEN-END:variables

}
