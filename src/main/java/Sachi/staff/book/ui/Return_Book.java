package Sachi.staff.book.ui;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import static org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory.model;

public class Return_Book extends javax.swing.JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public Return_Book() {
        initComponents();
        Book_issue.getDate();
        loadMemberData();
    }

    private void loadMemberData() {
        tableModel = new DefaultTableModel(new String[]{"Accession No", "Book Name", "No of Late days"}, 0);
        rSTableMetro1.setModel(tableModel);
    }

    /**
     *
     * @param memberId
     */
    public void updateTableData(String memberId) {
        String query = "SELECT t.Accession_No, b.Title, DATEDIFF(CURRENT_DATE, t.Transaction_Date) AS Days_Difference "
                + "FROM transactions t "
                + "INNER JOIN bookcopies bc ON t.Accession_No = bc.Accession_No "
                + "INNER JOIN book b ON b.ISBN_No = bc.ISBN_No "
                + "WHERE t.B_Id = ?";

        try ( Connection connection = new Helper.DatabaseConnection().connection();  PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, memberId);

            try ( ResultSet rs = pstmt.executeQuery()) {

                tableModel.setRowCount(0);

                while (rs.next()) {
                    String accessionNo = rs.getString("Accession_No");
                    String bookName = rs.getString("Title");
                    int daysDifference = rs.getInt("Days_Difference");

                    tableModel.addRow(new Object[]{accessionNo, bookName, daysDifference});
                }

                rSTableMetro1.setModel(tableModel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   private void updateTransactionType(String accessionNo, String action) {
    if ("damaged_return".equalsIgnoreCase(action)) {
        recordDamagedReturn(accessionNo);
        return;
    }

    String updateTransactionQuery;
    if ("return".equalsIgnoreCase(action)) {
        updateTransactionQuery = "UPDATE transactions SET Transaction_Type = 'return', Return_Date = CURRENT_DATE "
                               + "WHERE Accession_No = ? AND Transaction_Type = 'loan'";
    } else {
        JOptionPane.showMessageDialog(null, "Invalid action type specified.");
        return;
    }

    try (Connection connection = new Helper.DatabaseConnection().connection();
         PreparedStatement pstmtTransaction = connection.prepareStatement(updateTransactionQuery)) {

        // Update transactions table
        pstmtTransaction.setString(1, accessionNo);
        int rowsAffected = pstmtTransaction.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Book return updated successfully.");
            // Optionally, refresh the table data after update
            String memberId = memberIdBox.getText().trim(); // Assuming you have memberIdBox to get the current member ID
            updateTableData(memberId);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update book return. Please check if the book is issued.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "An error occurred while updating book return.");
    }
}

private void reissueTransaction(String accessionNo, String memberId) {
    String insertQuery = "INSERT INTO transactions (B_id, Accession_No, Transaction_Type, Transaction_Date) "
                       + "VALUES (?, ?, 'loan', CURRENT_DATE)";

    try (Connection connection = new Helper.DatabaseConnection().connection();
         PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {

        pstmt.setString(1, memberId);
        pstmt.setString(2, accessionNo);

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Book reissued successfully.");
            // Optionally, refresh the table data after update
            updateTableData(memberId);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to reissue book.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "An error occurred while reissuing the book.");
    }
}

     private void recordDamagedReturn(String accessionNo) {
    String updateTransactionQuery = "UPDATE transactions SET Transaction_Type = 'damaged_return', Return_Date = CURRENT_DATE "
                                  + "WHERE Accession_No = ? AND Transaction_Type = 'loan'";
    String updateBookCopiesQuery = "UPDATE bookcopies SET Book_Status = 'damaged' WHERE Accession_No = ?";

    try (Connection connection = new Helper.DatabaseConnection().connection();
         PreparedStatement pstmtTransaction = connection.prepareStatement(updateTransactionQuery);
         PreparedStatement pstmtBookCopies = connection.prepareStatement(updateBookCopiesQuery)) {

        // Update transactions table
        pstmtTransaction.setString(1, accessionNo);
        int transactionRowsAffected = pstmtTransaction.executeUpdate();

        if (transactionRowsAffected > 0) {
            // Update book_copies table
            pstmtBookCopies.setString(1, accessionNo);
            pstmtBookCopies.executeUpdate();

            JOptionPane.showMessageDialog(null, "Damaged book return updated successfully.");
            // Optionally, refresh the table data after update
            String memberId = memberIdBox.getText().trim(); // Assuming you have memberIdBox to get the current member ID
            updateTableData(memberId);
        } else {
            JOptionPane.showMessageDialog(null, "Failed to update damaged book return. Please check if the book is issued.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "An error occurred while updating damaged book return.");
    }
}

  
 /*  private void updateTransactionType(String accessionNo, String action) {
        String updateQuery;
        if ("return".equals(action)) {
            updateQuery = "UPDATE transactions SET Transaction_Type = 'return', Return_Date = CURRENT_DATE "
                        + "WHERE Accession_No = ? AND Transaction_Type = 'issue'";
        } else if ("reissue".equals(action)) {
            updateQuery = "UPDATE transactions SET Transaction_Type = 'reissue', Transaction_Date = CURRENT_DATE "
                        + "WHERE Accession_No = ? AND Transaction_Type = 'issue'";
        } else {
            return;
        }

        try (Connection connection = new Helper.DatabaseConnection().connection();
             PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {

            pstmt.setString(1, accessionNo);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Transaction updated successfully.");
                // Optionally, refresh the table data after update
                String memberId = memberIdBox.getText().trim(); // Assuming you have memberIdBox to get the current member ID
                updateTableData(memberId);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update transaction. Please check if the book is issued.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while updating the transaction.");
        }
    }*/
    // Main method or other necessary


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        memberIdBox = new javax.swing.JTextField();
        label3 = new java.awt.Label();
        label4 = new java.awt.Label();
        rSButtonHover5 = new rojeru_san.complementos.RSButtonHover();
        jScrollPane2 = new javax.swing.JScrollPane();
        rSTableMetro1 = new rojeru_san.complementos.RSTableMetro();
        jPanel5 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 102, 0)));

        memberIdBox.setBackground(new java.awt.Color(0, 0, 0));
        memberIdBox.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        memberIdBox.setForeground(new java.awt.Color(204, 153, 0));
        memberIdBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                memberIdBoxActionPerformed(evt);
            }
        });

        label3.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        label3.setForeground(new java.awt.Color(255, 255, 255));
        label3.setText("Membership No");

        label4.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        label4.setForeground(new java.awt.Color(255, 255, 255));
        label4.setText("Collect books need to be returned\n and removed from inventory\n");

        rSButtonHover5.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover5.setText("Checkins");
        rSButtonHover5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover5ActionPerformed(evt);
            }
        });

        rSTableMetro1.setForeground(new java.awt.Color(0, 0, 0));
        rSTableMetro1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Accession No", "Book Name", "No of Days Late"
            }
        ));
        rSTableMetro1.setColorBackgoundHead(new java.awt.Color(51, 102, 0));
        rSTableMetro1.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        rSTableMetro1.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        rSTableMetro1.setColorSelBackgound(new java.awt.Color(51, 102, 0));
        rSTableMetro1.setDropMode(javax.swing.DropMode.ON);
        rSTableMetro1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        rSTableMetro1.setRowHeight(29);
        jScrollPane2.setViewportView(rSTableMetro1);
        rSTableMetro1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (SwingUtilities.isRightMouseButton(evt)) {
                    int row = rSTableMetro1.rowAtPoint(evt.getPoint());
                    rSTableMetro1.setRowSelectionInterval(row, row);

                    JPopupMenu contextMenu = new JPopupMenu();
                    JMenuItem returnItem = new JMenuItem("Return");
                    JMenuItem damagedReturnItem = new JMenuItem("Damaged Return");
                    JMenuItem reissueItem = new JMenuItem("Reissue");

                    // Set font size for menu items
                    Font menuItemFont = new Font("Arial", Font.PLAIN, 17); // Adjust the font size as needed
                    returnItem.setFont(menuItemFont);
                    damagedReturnItem.setFont(menuItemFont);
                    reissueItem.setFont(menuItemFont);

                    // Action listener for the "Return" menu item
                    returnItem.addActionListener(e -> {
                        String accessionNo = (String) tableModel.getValueAt(row, 0);
                        updateTransactionType(accessionNo, "return");
                    });

                    // Action listener for the "Damaged Return" menu item
                    damagedReturnItem.addActionListener(e -> {
                        String accessionNo = (String) tableModel.getValueAt(row, 0);
                        updateTransactionType(accessionNo, "damaged_return");
                    });

                    // Action listener for the "Reissue" menu item
                    reissueItem.addActionListener(e -> {
                        String accessionNo = (String) tableModel.getValueAt(row, 0);
                        String memberId = memberIdBox.getText().trim();
                        reissueTransaction(accessionNo, memberId);
                    });

                    contextMenu.add(returnItem);
                    contextMenu.add(damagedReturnItem);
                    contextMenu.add(reissueItem);
                    contextMenu.show(rSTableMetro1, evt.getX(), evt.getY());
                }
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 837, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(100, 100, 100))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(memberIdBox, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(rSButtonHover5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(memberIdBox, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSButtonHover5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(440, 440, 440))
        );

        jPanel18.setBackground(new java.awt.Color(51, 102, 0));
        jPanel18.setForeground(new java.awt.Color(0, 0, 0));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel26.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel26.setText("Home >>");
        jLabel26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel26MouseClicked(evt);
            }
        });
        jPanel18.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(547, 66, 120, 33));

        jLabel27.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/username_3_1.png"))); // NOI18N
        jLabel27.setText("Welcome to Staff Account");
        jPanel18.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 0, 371, -1));
        jPanel18.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(596, 12, 9, 33));

        jLabel29.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel29.setText("Library Management system >> ");
        jPanel18.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(251, 69, -1, -1));

        jPanel19.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        jPanel18.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 63, -1, -1));

        jLabel30.setFont(new java.awt.Font("Jokerman", 2, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 51, 0));
        jLabel30.setText("League Developers");
        jPanel18.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(715, 124, 150, 40));

        jLabel31.setFont(new java.awt.Font("Jokerman", 2, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 51, 0));
        jLabel31.setText("League Developers");
        jPanel18.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(715, 124, 150, 40));

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/overdue books_2.png"))); // NOI18N
        jLabel5.setText("Book Returning");
        jPanel18.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(656, 65, 192, 35));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, 1002, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 337, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 865, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(318, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel26MouseClicked
        Sachi.ui.Staff.Home_pg_1 homeback = new Sachi.ui.Staff.Home_pg_1();
        homeback.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel26MouseClicked

    private void rSButtonHover5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover5ActionPerformed

        String memberId = memberIdBox.getText();
        updateTableData(memberId);

    }//GEN-LAST:event_rSButtonHover5ActionPerformed

    private void memberIdBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_memberIdBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_memberIdBoxActionPerformed

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
            java.util.logging.Logger.getLogger(Return_Book.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Return_Book.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Return_Book.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Return_Book.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Return_Book().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private javax.swing.JTextField memberIdBox;
    private rojeru_san.complementos.RSButtonHover rSButtonHover5;
    private rojeru_san.complementos.RSTableMetro rSTableMetro1;
    // End of variables declaration//GEN-END:variables
}
