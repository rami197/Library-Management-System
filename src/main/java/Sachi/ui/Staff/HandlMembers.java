package Sachi.ui.Staff;

import Sachi.staff.book.ui.Book_issue;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class HandlMembers extends javax.swing.JFrame {

    private SimpleDateFormat dateFormat;

    public HandlMembers() {
        initComponents();

        regDate1.setText(Book_issue.getDate());
        setNextMemberId();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    private int getNextMemberId() {
        int nextMemberId = 1; // Default value if no suppliers exist

        try ( Connection conn = new Helper.DatabaseConnection().connection();  PreparedStatement countstmt = conn.prepareStatement("SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'library_ms' AND TABLE_NAME = 'borrower'");  ResultSet rs = countstmt.executeQuery()) {

            if (rs.next()) {
                nextMemberId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve the next Member ID");
        }

        return nextMemberId;
    }

    private void setNextMemberId() {
        int nextMemberId = getNextMemberId();
        mRegistrationNo.setText(String.valueOf(nextMemberId)); 
    }

  private void insertMemberData() {
    try (Connection conn = new Helper.DatabaseConnection().connection();) {
        String mname = Mnamebox.getText();
        String maddress = addressbox.getText();
        String reservedSec = (String) typecombobox.getSelectedItem();
        Date mbirthdate = birthdate.getDatoFecha();
        SimpleDateFormat birthdateString = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = birthdateString.format(mbirthdate);
        Date mRegisDate = dateFormat.parse(regDate1.getText());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mRegisDate);
        calendar.add(Calendar.YEAR, 1);
        String membershipEndDate = dateFormat.format(calendar.getTime());
        String issueBookAmount = bookamountbox.getText().trim(); // Trim the input string here
        int amount = Integer.parseInt(issueBookAmount); // Now parse the trimmed string

        String hContact = homecontactbox.getText();
        String mContact = mobilecontactbox.getText();
        String oContact = officecontactbox.getText();
        String moccupation = moccupationbox.getText();

        // Certifier and guarantor fields
        String cFullName = cFullnamebox.getText();
        String cDesignation = cDesignationbox.getText();
        String caddress = cAddressbox.getText();
        String gcontactNo = gContactNo.getText();
        String gFullname = gnamebox.getText();
        String gOccupation = goccupationbox.getText();
        String gNIC = gnicbox.getText();
        String gAddress = gaddresbox.getText();

        // Check if the member type is the one that should skip certain fields
        boolean isSpecialMember = "Security Deposit holder".equals(reservedSec);

        // Validate common details
        if (mname.isEmpty() || maddress.isEmpty() || mbirthdate == null || hContact.isEmpty() || 
            mContact.isEmpty() || oContact.isEmpty() || moccupation.isEmpty() || issueBookAmount.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        // Validate that special members do not fill certifier and guarantor fields
        if (isSpecialMember && (!cFullName.isEmpty() || !cDesignation.isEmpty() || !caddress.isEmpty() ||
            !gFullname.isEmpty() || !gOccupation.isEmpty() || !gNIC.isEmpty() || !gAddress.isEmpty())) {
            JOptionPane.showMessageDialog(this, "Security deposit holder does not require certifier and guarantor information.");
            return;
        }

        String sql;
        if (isSpecialMember) {
            sql = "INSERT INTO borrower(B_Name, B_Address, B_DOB, B_occupation, B_HomeContactNo, B_MobileContactNo, B_OfficialContactNo, no_of_Books_issue, B_type, m_RegistrationDate, membership_end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO borrower(B_Name, B_Address, B_DOB, B_occupation, B_HomeContactNo, B_MobileContactNo, B_OfficialContactNo, no_of_Books_issue, B_type, certifier_Name, certifier_Address, certifier_desigation, G_fullname, G_NIC_No, G_contactNo, G_occupation, m_RegistrationDate, membership_end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, mname);
            pst.setString(2, maddress);
            pst.setDate(3, java.sql.Date.valueOf(formattedDate));
            pst.setString(4, moccupation);
            pst.setString(5, hContact);
            pst.setString(6, mContact);
            pst.setString(7, oContact);
            pst.setInt(8, amount);
            pst.setString(9, reservedSec);
            pst.setDate(10, new java.sql.Date(mRegisDate.getTime()));
            pst.setDate(11, java.sql.Date.valueOf(membershipEndDate));

            if (!isSpecialMember) {
                pst.setString(12, cFullName);
                pst.setString(13, caddress);
                pst.setString(14, cDesignation);
                pst.setString(15, gFullname);
                pst.setString(16, gOccupation);
                pst.setString(17, gNIC);
                pst.setString(18, gAddress);
                pst.setString(19, gcontactNo);
            }

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Member registration is successful");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to insert member data");
    } catch (ParseException ex) {
        Logger.getLogger(HandlMembers.class.getName()).log(Level.SEVERE, null, ex);
    }
}
private void clearFields() {
    Mnamebox.setText("");
    addressbox.setText("");
    typecombobox.setSelectedIndex(0);
    birthdate.setDatoFecha(null);
    regDate1.setText("");
    bookamountbox.setText("");
    homecontactbox.setText("");
    mobilecontactbox.setText("");
    officecontactbox.setText("");
    moccupationbox.setText("");
    cFullnamebox.setText("");
    cDesignationbox.setText("");
    cAddressbox.setText("");
    gContactNo.setText("");
    gnamebox.setText("");
    goccupationbox.setText("");
    gnicbox.setText("");
    gaddresbox.setText("");
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel13 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        birthdate = new rojeru_san.componentes.RSDateChooser();
        mRegistrationNo = new javax.swing.JLabel();
        Mnamebox = new javax.swing.JTextField();
        addressbox = new javax.swing.JTextField();
        registrationdate = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        homecontactbox = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        mobilecontactbox = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        officecontactbox = new javax.swing.JTextField();
        moccupationbox = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        bookamountbox = new javax.swing.JTextField();
        officecontactbox1 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        regDate1 = new javax.swing.JTextField();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jPanel17 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        gnicbox = new javax.swing.JTextField();
        gContactNo = new javax.swing.JTextField();
        gnamebox = new javax.swing.JTextField();
        goccupationbox = new javax.swing.JTextField();
        gaddresbox = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        cDesignationbox = new javax.swing.JTextField();
        cAddressbox = new javax.swing.JTextField();
        cFullnamebox = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        typecombobox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 102, 0));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Account_50px.png"))); // NOI18N
        jLabel2.setText("Welcome to Staff Account");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 10, 364, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Conference_26px.png"))); // NOI18N
        jLabel3.setText("Member Registration ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 20, 209, 27));

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Library Management system >> ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(294, 12, -1, 41));

        jPanel6.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 12, -1, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1114, 251, 30, 27));

        jLabel42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel42.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel42MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(1369, 261, 30, 27));

        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel43.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel43MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(1114, 251, 30, 27));

        jLabel44.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel44.setText("Home >>");
        jLabel44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel44MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(596, 19, -1, -1));

        jLabel63.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel63.setText("Home >>");
        jLabel63.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel63MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(1112, 285, 109, 33));

        jLabel64.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel64.setText("Home >>");
        jLabel64.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel64MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(1293, 221, 109, 33));

        jPanel3.setBackground(new java.awt.Color(51, 102, 0));
        jPanel3.setForeground(new java.awt.Color(0, 0, 0));

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Account_50px.png"))); // NOI18N
        jLabel11.setText("Welcome to Librarian's Account");

        jLabel34.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel34.setText("Library Management system ");

        jPanel18.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 8, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel35MouseClicked(evt);
            }
        });

        jLabel65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel65.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel65MouseClicked(evt);
            }
        });

        jLabel66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel66.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel66MouseClicked(evt);
            }
        });

        jLabel67.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel67.setText("Home");
        jLabel67.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel67MouseClicked(evt);
            }
        });

        jLabel86.setFont(new java.awt.Font("Jokerman", 0, 20)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(0, 51, 0));
        jLabel86.setText("Legue Developers");

        jLabel68.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(255, 255, 255));
        jLabel68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel68.setText("Home >>");
        jLabel68.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel68MouseClicked(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel69.setText("Home >>");
        jLabel69.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel69MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jLabel86)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34)
                .addGap(165, 165, 165)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(381, 381, 381)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1006, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(1114, 1114, 1114)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1336, Short.MAX_VALUE)))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(1345, Short.MAX_VALUE)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(1105, 1105, 1105)))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(1114, 1114, 1114)
                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1336, Short.MAX_VALUE)))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(1112, 1112, 1112)
                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(1259, Short.MAX_VALUE)))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(1269, Short.MAX_VALUE)
                    .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(1102, 1102, 1102)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel86)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel67))
                    .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(542, 542, 542))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(251, 251, 251)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addGap(252, 252, 252)))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(261, 261, 261)
                    .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addGap(242, 242, 242)))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(251, 251, 251)
                    .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addGap(252, 252, 252)))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(285, 285, 285)
                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(286, Short.MAX_VALUE)))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                    .addContainerGap(295, Short.MAX_VALUE)
                    .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(276, 276, 276)))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 264, 2480, 76));

        jLabel70.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(204, 255, 204));
        jLabel70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Conference_26px.png"))); // NOI18N
        jLabel70.setText("Member Registration");
        jLabel70.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel70MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(1139, 240, 225, 49));

        jLabel71.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(204, 255, 204));
        jLabel71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Conference_26px.png"))); // NOI18N
        jLabel71.setText("Member Registration");
        jLabel71.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel71MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(1149, 250, 225, 49));

        jPanel19.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(751, 68, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 12, 1859, 76));

        jPanel9.setBackground(new java.awt.Color(0, 0, 0));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 102, 0)));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jTabbedPane1.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jTabbedPane1ComponentAdded(evt);
            }
        });

        jLayeredPane1.setBackground(new java.awt.Color(0, 0, 0));
        jLayeredPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(0, 0, 0));
        jPanel13.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 102, 0)));
        jPanel13.setForeground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel45.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Member Details");
        jLabel45.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));
        jPanel13.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 13, 161, -1));

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Collaborator_Male_26px.png"))); // NOI18N
        jLabel15.setText("MemberShip No");
        jPanel13.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 180, -1));

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Collaborator_Male_26px.png"))); // NOI18N
        jLabel16.setText("Member Name");
        jPanel13.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 160, -1));

        jLabel17.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Contact_26px.png"))); // NOI18N
        jLabel17.setText("Address");
        jPanel13.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 130, -1));

        jLabel18.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/icons8-birthday-24.png"))); // NOI18N
        jLabel18.setText("Date of birth");
        jPanel13.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 127, -1));

        jLabel46.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/icons8-calendar-24.png"))); // NOI18N
        jLabel46.setText("Date of Registration");
        jPanel13.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 390, 200, -1));

        jLabel47.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/icons8-telephone-24.png"))); // NOI18N
        jLabel47.setText("Contact No");
        jPanel13.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, 140, -1));

        jLabel19.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/icons8-id-verified-24.png"))); // NOI18N
        jLabel19.setText("NIC No");
        jPanel13.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 100, -1));

        birthdate.setBackground(new java.awt.Color(0, 102, 0));
        birthdate.setForeground(new java.awt.Color(255, 255, 255));
        birthdate.setColorBackground(new java.awt.Color(51, 102, 0));
        birthdate.setColorButtonHover(new java.awt.Color(204, 255, 204));
        birthdate.setColorForeground(new java.awt.Color(0, 0, 0));
        birthdate.setPlaceholder("Select date\n\n");
        jPanel13.add(birthdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 280, 290, -1));

        mRegistrationNo.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        mRegistrationNo.setForeground(new java.awt.Color(255, 255, 255));
        mRegistrationNo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 102, 0)));
        jPanel13.add(mRegistrationNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, 170, 35));

        Mnamebox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnameboxActionPerformed(evt);
            }
        });
        jPanel13.add(Mnamebox, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 130, 290, 40));

        addressbox.setText("\n");
        jPanel13.add(addressbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(232, 210, 290, 50));

        registrationdate.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        registrationdate.setForeground(new java.awt.Color(255, 255, 255));
        jPanel13.add(registrationdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(809, 49, 306, -1));

        jLabel48.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Home");
        jPanel13.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 120, 81, -1));

        homecontactbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        homecontactbox.setText("\n");
        homecontactbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homecontactboxActionPerformed(evt);
            }
        });
        jPanel13.add(homecontactbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 118, 240, 30));

        jLabel49.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Mobile");
        jPanel13.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 170, 99, -1));

        mobilecontactbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        mobilecontactbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mobilecontactboxActionPerformed(evt);
            }
        });
        jPanel13.add(mobilecontactbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 168, 240, 30));

        jLabel50.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Official");
        jPanel13.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 220, 99, -1));

        officecontactbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jPanel13.add(officecontactbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 420, 270, 40));

        moccupationbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jPanel13.add(moccupationbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 318, 240, 30));

        jLabel72.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(255, 255, 255));
        jLabel72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/overdue books.png"))); // NOI18N
        jLabel72.setText("No of Books issue");
        jPanel13.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 210, -1));

        bookamountbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        bookamountbox.setText("\n");
        bookamountbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookamountboxActionPerformed(evt);
            }
        });
        jPanel13.add(bookamountbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 340, 170, 40));

        officecontactbox1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jPanel13.add(officecontactbox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 218, 240, 30));

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Occupation");
        jPanel13.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 320, -1, -1));

        regDate1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        regDate1.setText("\n");
        regDate1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regDate1ActionPerformed(evt);
            }
        });
        jPanel13.add(regDate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 390, 240, 40));

        jLayeredPane1.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 1110, 560));

        jTabbedPane1.addTab("Member Details", jLayeredPane1);

        jPanel17.setBackground(new java.awt.Color(0, 0, 0));
        jPanel17.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 102, 0)));
        jPanel17.setForeground(new java.awt.Color(255, 153, 0));

        jLabel23.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 153, 0));
        jLabel23.setText("Guranter Details");
        jLabel23.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));

        jLabel81.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Read_Online_26px.png"))); // NOI18N
        jLabel81.setText("Full Name");

        jLabel89.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(255, 255, 255));
        jLabel89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Lib_icons/icons8-give-32.png"))); // NOI18N
        jLabel89.setText("Occupation");

        jLabel91.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(255, 255, 255));
        jLabel91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/icons8-id-verified-24.png"))); // NOI18N
        jLabel91.setText("NIC No");

        jLabel92.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(255, 255, 255));
        jLabel92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/icons8-telephone-24.png"))); // NOI18N
        jLabel92.setText("Contact No");

        gnicbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        gnicbox.setForeground(new java.awt.Color(255, 153, 0));
        gnicbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gnicboxActionPerformed(evt);
            }
        });

        gContactNo.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        gContactNo.setForeground(new java.awt.Color(255, 153, 0));
        gContactNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gContactNoActionPerformed(evt);
            }
        });

        gnamebox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        gnamebox.setForeground(new java.awt.Color(255, 153, 0));
        gnamebox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gnameboxActionPerformed(evt);
            }
        });

        goccupationbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        goccupationbox.setForeground(new java.awt.Color(255, 153, 0));

        gaddresbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        gaddresbox.setForeground(new java.awt.Color(255, 153, 0));
        gaddresbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gaddresboxActionPerformed(evt);
            }
        });

        jLabel75.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(255, 255, 255));
        jLabel75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Contact_26px.png"))); // NOI18N
        jLabel75.setText("Address");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(102, 102, 102)
                                .addComponent(jLabel89)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(goccupationbox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(gnamebox, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel17Layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(gnicbox, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(gaddresbox, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(gContactNo, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(525, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel23)
                .addGap(35, 35, 35)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel81)
                    .addComponent(gnamebox, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel89)
                    .addComponent(goccupationbox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(gnicbox, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel91))
                .addGap(39, 39, 39)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(gaddresbox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel75))
                .addGap(42, 42, 42)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel92)
                    .addComponent(gContactNo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(78, 78, 78))
        );

        jLayeredPane2.setLayer(jPanel17, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Guranter Details", jLayeredPane2);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));

        jPanel21.setBackground(new java.awt.Color(0, 0, 0));
        jPanel21.setForeground(new java.awt.Color(51, 102, 0));

        jLabel61.setBackground(new java.awt.Color(0, 0, 0));
        jLabel61.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Lib_icons/icons8-supplier-32.png"))); // NOI18N
        jLabel61.setText("Full  Name(School Principle/Chief of the Company)");

        jLabel73.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(255, 255, 255));
        jLabel73.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Edit.png"))); // NOI18N
        jLabel73.setText("Designation");

        jLabel74.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AddNewBookIcons/icons8_Contact_26px.png"))); // NOI18N
        jLabel74.setText("Address(School/Company/Institution)");

        cDesignationbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cDesignationbox.setForeground(new java.awt.Color(0, 102, 102));

        cAddressbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cAddressbox.setForeground(new java.awt.Color(0, 102, 102));

        cFullnamebox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cFullnamebox.setForeground(new java.awt.Color(0, 102, 102));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cFullnamebox, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(98, 98, 98)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cDesignationbox)
                            .addComponent(cAddressbox))))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cFullnamebox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(cDesignationbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel73)
                        .addGap(51, 51, 51)))
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(cAddressbox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(79, 79, 79))
        );

        jLabel59.setFont(new java.awt.Font("Dialog", 1, 20)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(0, 102, 102));
        jLabel59.setText("Certifier Details");
        jLabel59.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel59)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(834, 834, 834))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel59)
                .addGap(34, 34, 34)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );

        jButton3.setBackground(new java.awt.Color(51, 102, 0));
        jButton3.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jButton3.setText("OK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(51, 102, 0));
        jButton4.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jButton4.setText("Cancel");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1713, 1713, 1713)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 1002, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addGap(64, 64, 64))
        );

        jTabbedPane1.addTab("Certifier Details", jPanel2);

        jPanel9.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, 1050, 540));

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1188, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 154, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(2364, 553, -1, -1));

        jButton5.setBackground(new java.awt.Color(51, 102, 0));
        jButton5.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jButton5.setText("OK");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 610, 173, -1));

        jButton6.setBackground(new java.awt.Color(51, 102, 0));
        jButton6.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jButton6.setText("Cancel");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel9.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1113, 610, 140, -1));

        typecombobox.setBackground(new java.awt.Color(0, 0, 0));
        typecombobox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        typecombobox.setForeground(new java.awt.Color(153, 102, 0));
        typecombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select the member type", "Adult", "Child", "Security Deposit holder", " " }));
        typecombobox.setToolTipText("Select the member Type\nAdult\nChild\nSecurity Deposit holder");
        typecombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                typecomboboxActionPerformed(evt);
            }
        });
        jPanel9.add(typecombobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 280, 40));
        typecombobox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedType = (String) e.getItem();
                    boolean isSpecialMember = "Security Deposit holder".equals(selectedType);

                    cFullnamebox.setEnabled(!isSpecialMember);
                    cDesignationbox.setEnabled(!isSpecialMember);
                    cAddressbox.setEnabled(!isSpecialMember);
                    gnamebox.setEnabled(!isSpecialMember);
                    goccupationbox.setEnabled(!isSpecialMember);
                    gnicbox.setEnabled(!isSpecialMember);
                    gaddresbox.setEnabled(!isSpecialMember);

                    if (isSpecialMember) {
                        cFullnamebox.setText("");
                        cDesignationbox.setText("");
                        cAddressbox.setText("");
                        gnamebox.setText("");
                        goccupationbox.setText("");
                        gnicbox.setText("");
                        gaddresbox.setText("");
                    }
                }
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Add members.png"))); // NOI18N
        jPanel9.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, -1, -1));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/Free_Vector___Documents_concept_illustration-removebg-preview.png"))); // NOI18N
        jPanel9.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 280, 380));

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 80, 1629, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked

        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel42MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel42MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel42MouseClicked

    private void jLabel43MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel43MouseClicked

    }//GEN-LAST:event_jLabel43MouseClicked

    private void jLabel44MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel44MouseClicked
        Sachi.ui.Staff.Home_pg_1 hm = new Sachi.ui.Staff.Home_pg_1();
        hm.show();
        dispose();
    }//GEN-LAST:event_jLabel44MouseClicked

    private void jLabel63MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel63MouseClicked
        Sachi.ui.Staff.Home_pg_1 home = new Sachi.ui.Staff.Home_pg_1();
        home.show();
        dispose();// TODO add your handling code here:
    }//GEN-LAST:event_jLabel63MouseClicked

    private void jLabel64MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel64MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel64MouseClicked

    private void jLabel35MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel35MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel35MouseClicked

    private void jLabel65MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel65MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel65MouseClicked

    private void jLabel66MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel66MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel66MouseClicked

    private void jLabel67MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel67MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel67MouseClicked

    private void jLabel68MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel68MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel68MouseClicked

    private void jLabel69MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel69MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel69MouseClicked

    private void jLabel70MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel70MouseClicked
        Sachi.ui.Staff.HandlMembers mem = new Sachi.ui.Staff.HandlMembers();
        mem.show();
        dispose();// TODO add your handling code here:
    }//GEN-LAST:event_jLabel70MouseClicked

    private void jLabel71MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel71MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel71MouseClicked

    private void MnameboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnameboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MnameboxActionPerformed

    private void homecontactboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homecontactboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homecontactboxActionPerformed

    private void mobilecontactboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mobilecontactboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mobilecontactboxActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        insertMemberData();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jTabbedPane1ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTabbedPane1ComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane1ComponentAdded

    private void bookamountboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookamountboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bookamountboxActionPerformed

    private void gnicboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gnicboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gnicboxActionPerformed

    private void gnameboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gnameboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gnameboxActionPerformed

    private void gaddresboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gaddresboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gaddresboxActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void typecomboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_typecomboboxActionPerformed
      

 // TODO add your handling code here:
    }//GEN-LAST:event_typecomboboxActionPerformed

    private void regDate1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regDate1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_regDate1ActionPerformed

    private void gContactNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gContactNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_gContactNoActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
clearFields();       // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(HandlMembers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HandlMembers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HandlMembers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HandlMembers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HandlMembers().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Mnamebox;
    private javax.swing.JTextField addressbox;
    private rojeru_san.componentes.RSDateChooser birthdate;
    private javax.swing.JTextField bookamountbox;
    private javax.swing.JTextField cAddressbox;
    private javax.swing.JTextField cDesignationbox;
    private javax.swing.JTextField cFullnamebox;
    private javax.swing.JTextField gContactNo;
    private javax.swing.JTextField gaddresbox;
    private javax.swing.JTextField gnamebox;
    private javax.swing.JTextField gnicbox;
    private javax.swing.JTextField goccupationbox;
    private javax.swing.JTextField homecontactbox;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel mRegistrationNo;
    private javax.swing.JTextField mobilecontactbox;
    private javax.swing.JTextField moccupationbox;
    private javax.swing.JTextField officecontactbox;
    private javax.swing.JTextField officecontactbox1;
    private javax.swing.JTextField regDate1;
    private javax.swing.JLabel registrationdate;
    private javax.swing.JComboBox<String> typecombobox;
    // End of variables declaration//GEN-END:variables
}
