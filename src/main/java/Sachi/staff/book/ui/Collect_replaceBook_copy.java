
package Sachi.staff.book.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;


public class Collect_replaceBook_copy extends javax.swing.JFrame {

    
    public Collect_replaceBook_copy() {
        initComponents();
    }
/*public void retrieveBookData(String searchText)throws SQLException {//book copie eke update ekk yann ona
    
    String sqlisbn="SELECT ISBN_No FROM bookcopies WHERE Accession_No=?";
    stmt=connection.createStatement();
    rs=stmt.executeQuery(query);
    
    while(rs.next()){
        String isbn =rs.getString("ISBN_No");
    
    
        String sql = "SELECT Classification_No,price, Publisher, publishedYear,Publisher_place, Book_adding_Date, Genre, Title, No_of_pages, Publisher_place, copies FROM book WHERE Accession_No = ?"; 
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, searchText);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                   classificationNotext.setText(rs.getString("Classification_No"));
                    isbnNo.setText(rs.getString("ISBN_No"));
             
                    pricelabel.setText(rs.getString("price"));
                    publisherlbel.setText(rs.getString("Publisher"));
                    publisheDate.setText(rs.getString("Published_date"));
                    reservedlabel.setText(rs.getString("reserved_section"));
                    titlelabel.setText(rs.getString("Title"));
                    authorlabel.setText(rs.getString("Author"));
                    noofpageslabel.setText(rs.getString("No_of_pages"));
                    placepublishedlabel.setText(rs.getString("Publisher_place"));
                    
                } else {
                    JOptionPane.showMessageDialog(this, "book not found");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve replaced book data");
        }
    }
   */
    
    public void retrieveBookData() {
    try (Connection connection = new Helper.DatabaseConnection().connection();
         PreparedStatement pst = connection.prepareStatement(
                 "SELECT b.Title, bc.ISBN_No " +
                 "FROM bookcopies bc " +
                 "INNER JOIN book b ON bc.ISBN_No = b.ISBN_No " +
                 "WHERE bc.Accession_No = ?")) {
 String searchText = searchtext.getText();
        pst.setString(1, searchText);
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                title.setText(rs.getString("Title"));
                classificationNotext.setText(rs.getString("ISBN_No"));
            } else {
                JOptionPane.showMessageDialog(null, "Accession number not found");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to retrieve book data");
    }
}

    public void updateBookStatus(String accessionNo) {
        
        String updateQuery = "UPDATE bookcopies SET Book_status = 'Good' WHERE Accession_No = ?";

        try (Connection connection = new Helper.DatabaseConnection().connection();
             PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {

            pstmt.setString(1, accessionNo);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Book status updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Accession number not found.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating book status: " + e.getMessage());
        }
   /* Public void retrieveBookData(String searchText, JTextField title) {
    

    try {
        connection = new Helper.DatabaseConnection().connection();

        // First query to get the ISBN number from bookcopies table
        String sqlIsbn = "SELECT ISBN_No FROM bookcopies WHERE Accession_No = ?";
        pst = connection.prepareStatement(sqlIsbn);
        pst.setString(1, searchText);
        rs = pst.executeQuery();

        if (rs.next()) {
            String isbn = rs.getString("ISBN_No");

            // Close the previous PreparedStatement and ResultSet
            rs.close();
            pst.close();

            // Second query to get the book details from the book table using the ISBN number
            String sqlBookDetails = "SELECT Title FROM book WHERE ISBN_No = ?";
            pst = connection.prepareStatement(sqlBookDetails);
            pst.setString(1, isbn);
            rs = pst.executeQuery();

            if (rs.next()) {
                // Assuming you have a JTextField for displaying the book title
                title.setText(rs.getString("Title"));
            } else {
                JOptionPane.showMessageDialog(null, "Book not found");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Accession number not found");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to retrieve book data");
    } finally {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
*/
    

}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        rSButtonHover4 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover6 = new rojeru_san.complementos.RSButtonHover();
        title = new javax.swing.JLabel();
        searchtext = new javax.swing.JTextField();
        classificationNotext = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel37 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 102, 0));

        jLabel40.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel40.setForeground(null);
        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel40.setText("Home >>");
        jLabel40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel40MouseClicked(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel42.setForeground(null);
        jLabel42.setText("Library Management system >>");

        jLabel43.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel43.setForeground(null);
        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/username_3.png"))); // NOI18N
        jLabel43.setText("Welcome to User Account");

        jPanel4.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jLabel45.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel45.setForeground(null);
        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Books_26px.png"))); // NOI18N
        jLabel45.setText("Collect Replaced Books to inventory");
        jLabel45.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel45MouseClicked(evt);
            }
        });

        jPanel9.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 297, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setForeground(null);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Close.png"))); // NOI18N
        jLabel2.setText("Close");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(275, 275, 275)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel45)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 23, Short.MAX_VALUE)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1240, -1));

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel18.setForeground(null);
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Add.png"))); // NOI18N
        jLabel18.setText("Add  Replaced Book Copies");
        jLabel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 12, 1120, 35));

        jLabel26.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel26.setForeground(null);
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/overdue books_1_1.png"))); // NOI18N
        jLabel26.setText("Type the Accession No :");
        jPanel7.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        jLabel30.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel30.setForeground(null);
        jLabel30.setText("Book Name");
        jPanel7.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 128, -1));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 102, 0)));

        rSButtonHover4.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonHover4.setText("Cancel");
        rSButtonHover4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover4ActionPerformed(evt);
            }
        });

        rSButtonHover6.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonHover6.setText("Ok");
        rSButtonHover6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(555, Short.MAX_VALUE)
                .addComponent(rSButtonHover6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(rSButtonHover4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(405, 405, 405))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rSButtonHover6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSButtonHover4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, -1, -1));

        title.setForeground(new java.awt.Color(255, 153, 0));
        title.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        jPanel7.add(title, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 240, 187, 42));

        searchtext.setBackground(new java.awt.Color(0, 0, 0));
        searchtext.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        searchtext.setForeground(new java.awt.Color(255, 153, 0));
        jPanel7.add(searchtext, new org.netbeans.lib.awtextra.AbsoluteConstraints(296, 74, 189, 36));

        classificationNotext.setForeground(new java.awt.Color(255, 153, 0));
        classificationNotext.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        jPanel7.add(classificationNotext, new org.netbeans.lib.awtextra.AbsoluteConstraints(298, 152, 187, 42));

        jButton1.setBackground(new java.awt.Color(51, 102, 0));
        jButton1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jButton1.setForeground(null);
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/search_5_1.png"))); // NOI18N
        jButton1.setText("Search");
        jButton1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jButton1MouseDragged(evt);
            }
        });
        jButton1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton1FocusGained(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 70, 149, -1));

        jLabel37.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel37.setForeground(null);
        jLabel37.setText("ISBN No");
        jPanel7.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 150, 170, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Background_images/prent (1)-removebg-preview.jpg"))); // NOI18N
        jPanel7.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 540, 420));

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 1240, 470));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel40MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel40MouseClicked
       Sachi.ui.Staff.Home_pg_1 home = new Sachi.ui.Staff.Home_pg_1();
        home.show();
        dispose();// TODO add your handling code here:
    }//GEN-LAST:event_jLabel40MouseClicked

    private void jLabel45MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel45MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel45MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
retrieveBookData();       
       
        
       // searchtext.setText(searchText);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton1FocusGained

    }//GEN-LAST:event_jButton1FocusGained

    private void jButton1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1MouseDragged

    private void rSButtonHover6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover6ActionPerformed
        String accessionNo = searchtext.getText();
        if (accessionNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the accession number.");
            return;
        }

        updateBookStatus(accessionNo);
    }//GEN-LAST:event_rSButtonHover6ActionPerformed

    private void rSButtonHover4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSButtonHover4ActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

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
            java.util.logging.Logger.getLogger(Collect_replaceBook_copy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Collect_replaceBook_copy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Collect_replaceBook_copy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Collect_replaceBook_copy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Collect_replaceBook_copy().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel classificationNotext;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private rojeru_san.complementos.RSButtonHover rSButtonHover4;
    private rojeru_san.complementos.RSButtonHover rSButtonHover6;
    private javax.swing.JTextField searchtext;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
