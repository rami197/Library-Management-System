package Jframe;

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
public class Login_page extends javax.swing.JFrame {

    public Login_page() {
        initComponents();
    }

    public void LoginDetails() {

        String username = usernamefield.getText();
        String password = passwordf.getText();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both Username and Password are required");
        } else {
            try ( Connection conn = new Helper.DatabaseConnection().connection();) {

                String qlog = "SELECT * FROM users WHERE user_name = ?";
                PreparedStatement checkstmt = conn.prepareStatement(qlog);

                checkstmt.setString(1, username);

                ResultSet rs = checkstmt.executeQuery();
                if (rs.next()) {
                    String dbpassword = rs.getString("password");
                    String role = rs.getString("Role_enum");
                    if (password.equals(dbpassword)) {

                        switch (role) {
                            case "Staff member":
                                System.out.println("hold");

                                Sachi.ui.Staff.Home_pg_1 home = new Sachi.ui.Staff.Home_pg_1();
                                home.setVisible(true);
                                dispose();
                                JOptionPane.showMessageDialog(this, "Login Successful");
                                break;
                            case "Librarian":
                                Sachi_Ui_Admin.Librarian_home_pg lhome = new Sachi_Ui_Admin.Librarian_home_pg();
                                lhome.setVisible(true);
                                dispose();
                                JOptionPane.showMessageDialog(this, "Login Successful");
                                break;
                            default:
                                JOptionPane.showMessageDialog(this, "Invalid Role");
                                break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect password");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "user not Found");
                }
                

            } catch (SQLException ex) {
                Logger.getLogger(Login_page.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        usernamefield = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        loginbutton = new javax.swing.JButton();
        signupbutton = new javax.swing.JButton();
        passwordf = new javax.swing.JPasswordField();
        showl = new javax.swing.JButton();
        hidel = new javax.swing.JButton();

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
        jPanel2.setFont(new java.awt.Font("Georgia", 1, 12)); // NOI18N

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 102, 0));
        jLabel1.setText("Welcome to  ");

        jLabel5.setFont(new java.awt.Font("Georgia", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 102, 0));
        jLabel5.setText("Dunkannawa Library");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/signup-library-icon.png"))); // NOI18N

        jDesktopPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 727, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 417, Short.MAX_VALUE))
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 53, Short.MAX_VALUE))
        );

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
                        .addGap(236, 236, 236)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(666, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 1560));

        jPanel3.setBackground(new java.awt.Color(51, 102, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Georgia", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Login Page");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, -1, -1));

        jLabel7.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Log to your account here");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, -1, -1));

        jLabel8.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Password");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 110, 30));

        jLabel11.setFont(new java.awt.Font("Georgia", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Username");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, 120, 30));

        usernamefield.setBackground(new java.awt.Color(51, 102, 0));
        usernamefield.setFont(new java.awt.Font("Georgia", 0, 17)); // NOI18N
        usernamefield.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        usernamefield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernamefieldActionPerformed(evt);
            }
        });
        jPanel3.add(usernamefield, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 300, 390, 30));

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/username.png"))); // NOI18N
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 50, 40));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/password.png"))); // NOI18N
        jLabel13.setText("jLabel13");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 50, 50));

        loginbutton.setBackground(new java.awt.Color(204, 255, 204));
        loginbutton.setFont(new java.awt.Font("Dubai", 1, 17)); // NOI18N
        loginbutton.setForeground(new java.awt.Color(0, 0, 0));
        loginbutton.setText("LOGIN");
        loginbutton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        loginbutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginbuttonMouseClicked(evt);
            }
        });
        loginbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginbuttonActionPerformed(evt);
            }
        });
        jPanel3.add(loginbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 680, 200, 40));

        signupbutton.setBackground(new java.awt.Color(204, 255, 204));
        signupbutton.setFont(new java.awt.Font("Dubai", 1, 17)); // NOI18N
        signupbutton.setForeground(new java.awt.Color(0, 0, 0));
        signupbutton.setText("SIGNUP");
        signupbutton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        signupbutton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signupbuttonMouseClicked(evt);
            }
        });
        signupbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupbuttonActionPerformed(evt);
            }
        });
        jPanel3.add(signupbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 680, 200, 40));

        passwordf.setBackground(new java.awt.Color(51, 102, 0));
        passwordf.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        passwordf.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        jPanel3.add(passwordf, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 459, 280, 30));

        showl.setBackground(new java.awt.Color(0, 0, 0));
        showl.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        showl.setForeground(new java.awt.Color(255, 255, 255));
        showl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/show.png"))); // NOI18N
        showl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showlMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                showlMouseReleased(evt);
            }
        });
        showl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showlActionPerformed(evt);
            }
        });
        jPanel3.add(showl, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 450, 50, -1));

        hidel.setBackground(new java.awt.Color(0, 0, 0));
        hidel.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        hidel.setForeground(new java.awt.Color(255, 255, 255));
        hidel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/hiding (1).png"))); // NOI18N
        hidel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hidelMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                hidelMouseReleased(evt);
            }
        });
        hidel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hidelActionPerformed(evt);
            }
        });
        jPanel3.add(hidel, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 450, 50, -1));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, -10, 560, 1580));

        setSize(new java.awt.Dimension(1274, 821));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void usernamefieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernamefieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernamefieldActionPerformed

    private void loginbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginbuttonActionPerformed
LoginDetails();

    }//GEN-LAST:event_loginbuttonActionPerformed

    private void signupbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signupbuttonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_signupbuttonActionPerformed

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowGainedFocus

    private void signupbuttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signupbuttonMouseClicked

        Jframe.Signup_page sign = new Jframe.Signup_page();
        sign.show();
        dispose();

    }//GEN-LAST:event_signupbuttonMouseClicked

    private void loginbuttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginbuttonMouseClicked
        LoginDetails();
    }//GEN-LAST:event_loginbuttonMouseClicked

    private void showlMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showlMouseClicked
        hidel.setVisible(true);
        showl.setVisible(false);
        passwordf.setEchoChar((char) 0);
        
    }//GEN-LAST:event_showlMouseClicked

    private void showlMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showlMouseReleased

    }//GEN-LAST:event_showlMouseReleased

    private void showlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showlActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_showlActionPerformed

    private void hidelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hidelMouseClicked

    }//GEN-LAST:event_hidelMouseClicked

    private void hidelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hidelMouseReleased
        hidel.setVisible(false);
        showl.setVisible(true);
        passwordf.setEchoChar('*');
        
    }//GEN-LAST:event_hidelMouseReleased

    private void hidelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hidelActionPerformed

    }//GEN-LAST:event_hidelActionPerformed

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
            java.util.logging.Logger.getLogger(Login_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login_page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login_page().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton hidel;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton loginbutton;
    private javax.swing.JPasswordField passwordf;
    private javax.swing.JButton showl;
    private javax.swing.JButton signupbutton;
    private javax.swing.JTextField usernamefield;
    // End of variables declaration//GEN-END:variables
}
