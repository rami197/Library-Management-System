package Sachi.Admin.patron.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class Members_Admin extends javax.swing.JFrame {

    private DefaultTableModel tableModel;

    public Members_Admin() {
        initComponents();
        loadMemberData();
    }

    private void loadMemberData() {
        tableModel = new DefaultTableModel(new String[]{"Membership ID", "Member Name","Member Type", "Registration Date", "Status"}, 0);
        rSTableMetro1.setModel(tableModel);
        updateTableData();
    }

    public void updateTableData() {
        String query = "SELECT B_id, B_Name, B_type, m_RegistrationDate, status FROM borrower";

        try ( Connection connection = new Helper.DatabaseConnection().connection();  PreparedStatement pstmt = connection.prepareStatement(query);  ResultSet rs = pstmt.executeQuery()) {

            tableModel.setRowCount(0);

            while (rs.next()) {
                String memberId = rs.getString("B_id");
                String name = rs.getString("B_Name");
                String memberType = rs.getString("B_type");
                String mRegistrationDate = rs.getString("m_RegistrationDate");
                String status = rs.getString("status");
                tableModel.addRow(new Object[]{memberId, name, memberType, mRegistrationDate, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
private void searchMemberDetails(String searchQuery) {
    String sql = "SELECT b.B_id, b.B_Name, b.B_type, b.m_RegistrationDate, b.status FROM borrower b "
               + "WHERE b.B_id LIKE ? "
               + "OR b.B_Name LIKE ?";

    try (Connection connection = new Helper.DatabaseConnection().connection();
         PreparedStatement stmt = connection.prepareStatement(sql)) {

        stmt.setString(1, "%" + searchQuery + "%");
        stmt.setString(2, "%" + searchQuery + "%");

        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        rSTableMetro1.setModel(model);

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void filterMembers(String category) {
        String sql;

        switch (category) {
            case "Newest":
                sql = "SELECT b.B_id, b.B_Name, b.B_type, b.m_RegistrationDate, b.status FROM borrower b  ORDER BY b.m_RegistrationDate DESC";
                break;
            case "Adult Members":
                sql = "SELECT b.B_id, b.B_Name, b.B_type, b.m_RegistrationDate, b.status FROM borrower b  WHERE b.B_type = 'Adult'";
                break;
            case "Child Members":
                sql = "SELECT b.B_id, b.B_Name, b.B_type, b.m_RegistrationDate, b.status FROM borrower b  WHERE b.B_type = 'Child'";
                break;
            case "Security Deposit holders":
                sql = "SELECT b.B_id, b.B_Name, b.B_type, b.m_RegistrationDate, b.status FROM borrower b  WHERE b.B_type= 'Security Deposit holder'";
                break;
            default:
                sql = "SELECT b.B_id, b.B_Name, b.B_type, b.m_RegistrationDate, b.status FROM borrower b";
                break;
        }

        try (Connection connection = new Helper.DatabaseConnection().connection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            rSTableMetro1.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        rSTableMetro1 = new rojeru_san.complementos.RSTableMetro();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        searchboxx = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 51, 255));

        jPanel9.setBackground(new java.awt.Color(0, 0, 0));

        jScrollPane4.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jScrollPane4ComponentAdded(evt);
            }
        });

        rSTableMetro1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Membership ID", "Member Type", "Registration Date", "Note"
            }
        ));
        rSTableMetro1.setCellSelectionEnabled(true);
        rSTableMetro1.setColorBackgoundHead(new java.awt.Color(51, 102, 0));
        rSTableMetro1.setColorForegroundHead(new java.awt.Color(0, 0, 0));
        rSTableMetro1.setColorSelForeground(new java.awt.Color(0, 0, 0));
        rSTableMetro1.setDropMode(javax.swing.DropMode.ON_OR_INSERT);
        rSTableMetro1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        rSTableMetro1.setFuenteFilas(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rSTableMetro1.setFuenteFilasSelect(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        rSTableMetro1.setRowHeight(25);
        jScrollPane4.setViewportView(rSTableMetro1);

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/search_3_1.png"))); // NOI18N
        jLabel5.setText("Search");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Sort by");

        jComboBox1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(255, 153, 0));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Newest", "Adult Members", "Child Members", "Security Deposit holders", " ", " " }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        searchboxx.setBackground(new java.awt.Color(0, 0, 0));
        searchboxx.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        searchboxx.setForeground(new java.awt.Color(255, 153, 0));
        searchboxx.setText("jTextField2");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 1246, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(269, 269, 269)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchboxx, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(990, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchboxx, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(110, 110, 110)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(684, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(51, 102, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Calibri", 0, 20)); // NOI18N
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Conference_26px.png"))); // NOI18N
        jLabel27.setText("Members handling");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 60, 220, -1));

        jLabel28.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Adminuser.png"))); // NOI18N
        jLabel28.setText("Welcome to Librarian's Account");
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 30, 400, -1));

        jPanel15.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, 10, 40));

        jLabel29.setFont(new java.awt.Font("Dubai Light", 0, 20)); // NOI18N
        jLabel29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel29.setText("Home >>");
        jLabel29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel29MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 120, -1));

        jLabel30.setFont(new java.awt.Font("Calibri", 0, 20)); // NOI18N
        jLabel30.setText("Dunkannawa  Library >>");
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 60, 220, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 2361, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel29MouseClicked
        Sachi_Ui_Admin.Librarian_home_pg hmad = new Sachi_Ui_Admin.Librarian_home_pg();
        hmad.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel29MouseClicked

    private void jScrollPane4ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jScrollPane4ComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane4ComponentAdded

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        String query = searchboxx.getText();
                searchMemberDetails(query); 
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        
             {
                String selectedCategory = (String) jComboBox1.getSelectedItem();
                filterMembers(selectedCategory);
            }
      
    

    }//GEN-LAST:event_jComboBox1ActionPerformed

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
            java.util.logging.Logger.getLogger(Members_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Members_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Members_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Members_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Members_Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane4;
    private rojeru_san.complementos.RSTableMetro rSTableMetro1;
    private javax.swing.JTextField searchboxx;
    // End of variables declaration//GEN-END:variables
}
