package Sachi_Ui_Admin;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author USER
 */
public class Add_Donators extends javax.swing.JFrame {

    /**
     * Creates new form Donator_Mg_Admin
     */
    public Add_Donators() throws SQLException {
        initComponents();
        setNextMemberId();
        getNextDonorId();
    }

    
    public int getNextDonorId() throws SQLException {
        String query = "SELECT COUNT(*) FROM donator";
        int nextId = 0;

        try (Connection conn = new Helper.DatabaseConnection().connection();
             PreparedStatement pstd = conn.prepareStatement(query);
             ResultSet rs = pstd.executeQuery()) {

            if (rs.next()) {
                nextId = rs.getInt(1) + 1; // The next ID will be the count + 1
            }
        }

        return nextId;
    }

     public int insertDonatorAndGetId(String dname, String dtelephone, String dEmail) throws SQLException {
        int generatedId = -1;
        String query = "INSERT INTO donator (Donator_name, ContactNo, d_email) VALUES (?, ?, ?)";

        try (Connection conn = new Helper.DatabaseConnection().connection();
             PreparedStatement pstd = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstd.setString(1, dname);
            pstd.setString(2, dtelephone);
            pstd.setString(3, dEmail);

            int rowsAffected = pstd.executeUpdate();
            if (rowsAffected == 1) {
                try (ResultSet rs = pstd.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        }

        return generatedId;
    }

    // Method to handle the UI logic and call the insertDonatorAndGetId method
    public void InsertDonatorDetails() {
        String dname = dnamebox1.getText();
        String dtelephone = dtelephonebox1.getText();
        String dEmail = demailbox.getText();

        try {
            int nextId = getNextDonorId();
            dRegisterNolabl.setText(String.valueOf(nextId)); // Display the next donor ID

            int generatedId = insertDonatorAndGetId(dname, dtelephone, dEmail);

            if (generatedId != -1) {
                System.out.println("Generated Donator_ID: " + generatedId);
                JOptionPane.showMessageDialog(this, "Donator added successfully");
            } else {
                System.out.println("No data");
                JOptionPane.showMessageDialog(this, "Donator adding failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Donator adding failed: " + e.getMessage());
        }
    }
private void setNextMemberId() throws SQLException {
        int nextMemberId = getNextDonorId();
        dRegisterNolabl.setText(String.valueOf(nextMemberId)); 
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel18 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        dRegisterNolabl = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        demailbox = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        rSButtonPane1 = new rojeru_san.complementos.RSButtonPane();
        jLabel7 = new javax.swing.JLabel();
        dtelephonebox1 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jComboBox5 = new javax.swing.JComboBox<>();
        rSButtonHover4 = new rojeru_san.complementos.RSButtonHover();
        jComboBox6 = new javax.swing.JComboBox<>();
        rSButtonHover11 = new rojeru_san.complementos.RSButtonHover();
        dtelephonebox2 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        dnamebox1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel18.setBackground(new java.awt.Color(51, 102, 0));
        jPanel18.setForeground(new java.awt.Color(0, 0, 0));

        jLabel26.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel26.setText("Home >>");
        jLabel26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel26MouseClicked(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Account_50px.png"))); // NOI18N
        jLabel27.setText("Welcome ");

        jLabel29.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel29.setText("Library Management system >> ");

        jPanel19.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 11, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 47, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Lib_icons/icons8-give-32.png"))); // NOI18N
        jLabel5.setText("Register Donators  ");

        jPanel20.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Close.png"))); // NOI18N
        jLabel2.setText("Close");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Close.png"))); // NOI18N
        jLabel3.setText("Close");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Close.png"))); // NOI18N
        jLabel4.setText("Close");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addGap(12, 12, 12)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(62, 62, 62)
                .addComponent(jLabel27)
                .addGap(54, 54, 54)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addGap(0, 483, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(0, 483, Short.MAX_VALUE)))
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                    .addContainerGap(483, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addContainerGap(483, Short.MAX_VALUE)))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel29)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(3, 3, 3)
                                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(489, 489, 489))
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel18Layout.createSequentialGroup()
                    .addGap(0, 267, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGap(0, 268, Short.MAX_VALUE)))
            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                    .addContainerGap(257, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(268, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1030, 90));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 0), 2));

        jLabel6.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Add members.png"))); // NOI18N
        jLabel6.setText("Register");

        jLabel9.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText(" Telephone No");

        dRegisterNolabl.setBackground(new java.awt.Color(0, 0, 0));
        dRegisterNolabl.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        dRegisterNolabl.setForeground(new java.awt.Color(255, 255, 255));
        dRegisterNolabl.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 255, 255)));

        jPanel24.setBackground(new java.awt.Color(51, 102, 0));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 130, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        jLabel11.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Donator\"s Name");

        demailbox.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        demailbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                demailboxActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Registration No");

        jLabel8.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("E Mail address");

        rSButtonPane1.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonPane1.setColorHover(new java.awt.Color(51, 102, 0));
        rSButtonPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSButtonPane1MouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Register");

        javax.swing.GroupLayout rSButtonPane1Layout = new javax.swing.GroupLayout(rSButtonPane1);
        rSButtonPane1.setLayout(rSButtonPane1Layout);
        rSButtonPane1Layout.setHorizontalGroup(
            rSButtonPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rSButtonPane1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        rSButtonPane1Layout.setVerticalGroup(
            rSButtonPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rSButtonPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dtelephonebox1.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        dtelephonebox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dtelephonebox1ActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));

        jComboBox5.setBackground(new java.awt.Color(0, 0, 0));
        jComboBox5.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jComboBox5.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Donor Details  Report", " ", " " }));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        rSButtonHover4.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonHover4.setText("Generate");
        rSButtonHover4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover4ActionPerformed(evt);
            }
        });

        jComboBox6.setBackground(new java.awt.Color(0, 0, 0));
        jComboBox6.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jComboBox6.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Donated Book Report", " ", " ", " " }));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        rSButtonHover11.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover11.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonHover11.setText("Normal");
        rSButtonHover11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover11ActionPerformed(evt);
            }
        });

        dtelephonebox2.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        dtelephonebox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dtelephonebox2ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Georgia", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Donator ID");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dtelephonebox2))
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(52, 52, 52)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rSButtonHover4, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                    .addComponent(rSButtonHover11, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                .addGap(104, 104, 104))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dtelephonebox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSButtonHover4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSButtonHover11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dnamebox1.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        dnamebox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dnamebox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(rSButtonPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dRegisterNolabl, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(demailbox, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                                    .addComponent(dtelephonebox1)))
                            .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 308, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(169, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 594, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(141, 141, 141))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(236, 236, 236)
                    .addComponent(dnamebox1, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(286, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(rSButtonPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(dRegisterNolabl, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(demailbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dtelephonebox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(189, 189, 189)
                    .addComponent(dnamebox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(302, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(140, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-40, 90, 1060, 620));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel26MouseClicked
        Sachi_Ui_Admin.Librarian_home_pg homeback = new Sachi_Ui_Admin.Librarian_home_pg();
        homeback.show();
        dispose();  // TODO add your handling code here:
    }//GEN-LAST:event_jLabel26MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseClicked

    private void rSButtonHover11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover11ActionPerformed
// Assuming 'donorId' is the ID you want to filter by
String donorId = dtelephonebox2.getText();
//"enteredDonorID"; // Replace with actual donor ID or a variable holding the donor ID

try (Connection connection = new Helper.DatabaseConnection().connection()) {
    // Path to the JRXML file
    String reportPath = "C:\\Users\\USER\\Documents\\Reports\\donatedbooks.jrxml";
    
    // Compile the report
    JasperReport jr = JasperCompileManager.compileReport(reportPath);
    
    // Create a HashMap to store the report parameters
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("did", donorId); // Add the donorId parameter
    
    // Fill the report with parameters and the database connection
    JasperPrint jp = JasperFillManager.fillReport(jr, parameters, connection);
    
    // Display the report using JasperViewer
    JasperViewer.viewReport(jp, false); // 'false' means the application won't exit when the viewer is closed
    
    connection.close();
} catch (Exception e) {
    JOptionPane.showMessageDialog(rootPane, e);
}
//add your handling code here:
    }//GEN-LAST:event_rSButtonHover11ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void rSButtonHover4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover4ActionPerformed
        //C:\Users\USER\Documents\Reports        // TODO add your handling code here:

        //        Date fromDate = birthdate4.getDatoFecha();
        //        Date toDate = birthdate5.getDatoFecha();
        //
        //        // Create a HashMap and put the date objects
        ////        HashMap<String, Object> parameters = new HashMap<>();
        ////        parameters.put("fdate", fromDate);
        ////        parameters.put("enddate", toDate);
        //
        //        jPanel1.removeAll();
        //        jPanel1.repaint();
        //        jPanel1.revalidate();
        //
        //        try (Connection connection = new Helper.DatabaseConnection().connection()){
            ////            JasperDesign jdesign = JRXmlLoader.load("C:\\Users\\USER\\Documents\\Reports\\donorde.jrxml");
            ////            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
            ////            JasperPrint jprint = JasperFillManager.fillReport(jreport,  connection);
            //
            ////            JRViewer viewer = new JRViewer(jprint);
            //           jPanel2.setLayout(new BorderLayout());
            //            jPanel2.add(viewer, BorderLayout.CENTER);
            //
            ////            String outputFilePath = "C:\\Users\\USER\\Documents\\Reports\\donorde.jrxml";
            ////            JasperExportManager.exportReportToPdfFile(jprint, outputFilePath);
            ////
            ////            System.out.println("Report saved as PDF at: " + outputFilePath);
            //
            //        } catch (Exception ex) {
            //            ex.printStackTrace();
            //        }

        try(Connection connection = new Helper.DatabaseConnection().connection()){
            String reportpath= "C:\\Users\\USER\\Documents\\Reports\\donorde.jrxml";
            JasperReport jr = JasperCompileManager.compileReport(reportpath);
            JasperPrint jp =JasperFillManager.fillReport(jr, null,connection);
            JasperViewer.viewReport(jp);
            connection.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }//GEN-LAST:event_rSButtonHover4ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void dtelephonebox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dtelephonebox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dtelephonebox1ActionPerformed

    private void rSButtonPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSButtonPane1MouseClicked
        InsertDonatorDetails();
    }//GEN-LAST:event_rSButtonPane1MouseClicked

    private void demailboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_demailboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_demailboxActionPerformed

    private void dnamebox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dnamebox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dnamebox1ActionPerformed

    private void dtelephonebox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dtelephonebox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dtelephonebox2ActionPerformed

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
            java.util.logging.Logger.getLogger(Add_Donators.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Add_Donators.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Add_Donators.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Add_Donators.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Add_Donators().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Add_Donators.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dRegisterNolabl;
    private javax.swing.JTextField demailbox;
    private javax.swing.JTextField dnamebox1;
    private javax.swing.JTextField dtelephonebox1;
    private javax.swing.JTextField dtelephonebox2;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private rojeru_san.complementos.RSButtonHover rSButtonHover11;
    private rojeru_san.complementos.RSButtonHover rSButtonHover4;
    private rojeru_san.complementos.RSButtonPane rSButtonPane1;
    // End of variables declaration//GEN-END:variables
}
