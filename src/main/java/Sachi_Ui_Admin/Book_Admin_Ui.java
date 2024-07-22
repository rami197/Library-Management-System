
package Sachi_Ui_Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 * @author USER
 */
public class Book_Admin_Ui extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    public Book_Admin_Ui() {
        initComponents();
        loadbookData();
    }

     private void loadbookData() {
       tableModel = new DefaultTableModel(new String[]{
            "ISBN", "Title", "Publisher", 
            "Accession No", "Transaction Type", 
            "Author ID", "Author Name"}, 0);
        rSTableMetro1.setModel(tableModel);
        updateTableData();
    }

   
    public void updateTableData() {
        String query = "SELECT b.ISBN_No, b.title, b.publisher_Name, " +
                       "bc.Accession_No, t.transaction_type, " +
                       "a.Author_ID, a.Author_Name " +
                       "FROM book b " +
                       "INNER JOIN bookcopies bc ON b.ISBN_No = bc.ISBN_No " +
                       "INNER JOIN transactions t ON bc.Accession_No = t.Accession_No " +
                       "INNER JOIN Author a ON b.Author_ID = a.Author_ID";

        try (Connection connection = new Helper.DatabaseConnection().connection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            
            tableModel.setRowCount(0);

            while (rs.next()) {
                String isbn = rs.getString("ISBN_No");
                String title = rs.getString("title");
                String publisher = rs.getString("publisher_Name");
                String accessionNo = rs.getString("Accession_No");
                String transactionType = rs.getString("transaction_type");
                String authorId = rs.getString("Author_ID");
                String authorName = rs.getString("Author_Name");
                tableModel.addRow(new Object[]{isbn, title, publisher, accessionNo, transactionType, authorId, authorName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
public void searchTable(String searchQuery) {

    String sql = "SELECT bc.Accession_No, b.Publisher_Name, b.ISBN_No, b.Title, a.Author_ID, a.Author_Name, t.transaction_type " +
                 "FROM book b " +
                 "INNER JOIN author a ON a.Author_ID = b.Author_ID " +
                 "INNER JOIN bookcopies bc ON b.ISBN_No = bc.ISBN_No " +
                 "LEFT JOIN transactions t ON t.Accession_No = bc.Accession_No " + // Adjust if needed
                 "WHERE b.Title LIKE ? " +
                 "OR b.Publisher_Name LIKE ? " +
                 "OR a.Author_Name LIKE ?";
                 
    try (Connection connection = new Helper.DatabaseConnection().connection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {

        stmt.setString(1, "%" + searchQuery + "%");
        stmt.setString(2, "%" + searchQuery + "%");
        stmt.setString(3, "%" + searchQuery + "%");

        try (ResultSet rs = stmt.executeQuery()) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }

            rSTableMetro1.setModel(tableModel);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        // Consider logging the error or providing user feedback
    }}
    //select * from book b in
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jCTextField1 = new app.bolivia.swing.JCTextField();
        jCTextField2 = new app.bolivia.swing.JCTextField();
        rSButtonHover1 = new rojeru_san.complementos.RSButtonHover();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        rSTableMetro1 = new rojeru_san.complementos.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 102, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel15.setText("Dunkannawa  Library >>");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, 250, -1));

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Adminuser.png"))); // NOI18N
        jLabel16.setText("Welcome to Librarian's Account");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 20, 360, 40));

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 10, 40));

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel12.setText("Home >>");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 60, 140, -1));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Books_26px.png"))); // NOI18N
        jLabel1.setText("Book Details");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 60, 180, -1));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 1, true));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/search.png"))); // NOI18N
        jLabel3.setText("Search");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jCTextField1.setBackground(new java.awt.Color(0, 0, 0));
        jCTextField1.setForeground(new java.awt.Color(255, 153, 0));
        jCTextField1.setToolTipText("Search by Author,Publisher,Name of the Book\n\n");
        jCTextField1.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jCTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTextField1ActionPerformed(evt);
            }
        });

        jCTextField2.setText("Search by Author,Publisher,Name of the Book  ");
        jCTextField2.setToolTipText("Search by Author,Publisher,Name of the Book\n\n");
        jCTextField2.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jCTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTextField2ActionPerformed(evt);
            }
        });

        rSButtonHover1.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover1.setText("Refresh");
        rSButtonHover1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jCTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(rSButtonHover1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jCTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSButtonHover1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1007, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 62, Short.MAX_VALUE)
        );

        rSTableMetro1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Accession No", "ISBN No", "Title", "Author ID", "Author", "Publisher", "Transaction Type"
            }
        ));
        rSTableMetro1.setColorBackgoundHead(new java.awt.Color(0, 0, 0));
        rSTableMetro1.setColorFilasBackgound1(new java.awt.Color(0, 0, 0));
        rSTableMetro1.setColorFilasBackgound2(new java.awt.Color(0, 0, 0));
        rSTableMetro1.setColorFilasForeground2(new java.awt.Color(0, 102, 102));
        rSTableMetro1.setColorForegroundHead(new java.awt.Color(255, 153, 51));
        rSTableMetro1.setColorSelBackgound(new java.awt.Color(255, 153, 0));
        rSTableMetro1.setColorSelForeground(new java.awt.Color(0, 0, 0));
        rSTableMetro1.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        rSTableMetro1.setRowHeight(20);
        jScrollPane1.setViewportView(rSTableMetro1);
        if (rSTableMetro1.getColumnModel().getColumnCount() > 0) {
            rSTableMetro1.getColumnModel().getColumn(2).setPreferredWidth(10);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(141, 141, 141)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 35, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        Sachi_Ui_Admin.Librarian_home_pg hm = new Sachi_Ui_Admin.Librarian_home_pg();
        hm.show();
        dispose();// TODO add your handling code here:
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jCTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField1ActionPerformed

    private void rSButtonHover1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover1ActionPerformed
       loadbookData();
                updateTableData();
        
        String query = jCTextField1.getText();
                searchTable(query);  
        
        
    }//GEN-LAST:event_rSButtonHover1ActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
String query = jCTextField1.getText();
          //      searchTable(query); loadbookData();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jCTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField2ActionPerformed

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
//lo        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseClicked

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
            java.util.logging.Logger.getLogger(Book_Admin_Ui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Book_Admin_Ui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Book_Admin_Ui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Book_Admin_Ui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Book_Admin_Ui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private app.bolivia.swing.JCTextField jCTextField1;
    private app.bolivia.swing.JCTextField jCTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private rojeru_san.complementos.RSButtonHover rSButtonHover1;
    private rojeru_san.complementos.RSTableMetro rSTableMetro1;
    // End of variables declaration//GEN-END:variables
}
