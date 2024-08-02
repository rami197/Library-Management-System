/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Sachi.staff.Payments.Ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class fines_collecting extends javax.swing.JFrame {

    private final Connection conn;

    /**
     * Creates new form fines_collecting
     */
    public fines_collecting() {
        initComponents();
         this.conn = new Helper.DatabaseConnection().connection();
        
    }

     public void retrieveLateDays() {
    String memberId = memberfinesbox.getText();

    try (Connection conn = new Helper.DatabaseConnection().connection()) {
        // Fetch transaction date
        String sql = "SELECT transaction_Date FROM transactions WHERE B_ID = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, memberId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Date transactionDateSQL = rs.getDate("transaction_Date");

                    // Convert java.sql.Date to java.util.Date
                    java.util.Date transactionDate = new java.util.Date(transactionDateSQL.getTime());

                    // Use Calendar to add two weeks
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(transactionDate);
                    calendar.add(Calendar.DAY_OF_YEAR, 14);
                    java.util.Date extendedDueDate = calendar.getTime();

                    // Get current date
                    java.util.Date currentDate = new java.util.Date();

                    // Calculate late days
                    long diffInMillis = currentDate.getTime() - extendedDueDate.getTime();
                    long lateDays = diffInMillis / (1000 * 60 * 60 * 24);

                    if (lateDays > 0) {
                        jLabel19.setText(Long.toString(lateDays));
                        // Calculate fine only if there are late days
                        calculateFine(memberId, (int) lateDays, conn);
                    } else {
                        jLabel19.setText("No late days");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Member not found");
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(Security_deposit.class.getName()).log(Level.SEVERE, null, ex);
    }
}
private void calculateFine(String memberId, int lateDays, Connection conn) {
    try {System.out.println("ggggg");
        // Fetch member type
        String sql = "SELECT B_Type FROM borrower WHERE B_ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, memberId);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                String memberType = rs.getString("B_Type"); // Assume member_type is 'Adult', 'Child' or 'Security Deposit Holder'

                // Determine fine amount based on late days
                double baseFineAmount = getFineAmount(conn, memberType, lateDays);
                double finalFineAmount = baseFineAmount * lateDays;
                jLabel10.setText("" + finalFineAmount);
            } else {
                JOptionPane.showMessageDialog(null, "Member type not found");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(Security_deposit.class.getName()).log(Level.SEVERE, null, ex);
    }
}
private double getFineAmount(Connection conn, String memberType, int lateDays) throws SQLException {
    String sql = "SELECT fine_type_amount_for_Childrens, fine_type_amount_for_Adults, fine_type_description " +
                 "FROM fines_type";

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            String description = rs.getString("fine_type_description");
            int[] range = parseRange(description);
            System.out.println("Parsed range: " + range[0] + " - " + range[1] + " for lateDays: " + lateDays);
            if (range[0] <= lateDays && lateDays <= range[1]) {
                double fineAmount;
                if (memberType.equalsIgnoreCase("Child")) {
                    fineAmount = rs.getDouble("fine_type_amount_for_Childrens");
                } else if (memberType.equalsIgnoreCase("Adult") || memberType.equalsIgnoreCase("Security Deposit Holder")) {
                    fineAmount = rs.getDouble("fine_type_amount_for_Adults");
                } else {
                    fineAmount = 0.0;
                }
                System.out.println("Fine Amount for " + memberType + ": " + fineAmount);
                return fineAmount;
            }
        }
    }
    System.out.println("No fine amount found for lateDays: " + lateDays + " and memberType: " + memberType);
    return 0.0; // No fine amount found
}

/*private int[] parseRange(String description) {
    String[] parts = description.split("-");
    int start = Integer.parseInt(parts[0].trim());
    int end = Integer.parseInt(parts[1].trim());
    return new int[]{start, end};
}

*/

private int[] parseRange(String description) {
    if (description.contains("onwards")) {
        String[] parts = description.split(" ");
        int start = Integer.parseInt(parts[1]);
        return new int[]{start, Integer.MAX_VALUE};
    } else {
        String[] parts = description.split("-");
        int start = Integer.parseInt(parts[0].split(" ")[1]);
        int end = Integer.parseInt(parts[1]);
        return new int[]{start, end};
    }
}
 
//record storing
private void insertFineRecord(Connection conn, String memberId, int lateDays, double finalFineAmount) {
    String sql = "INSERT INTO fines (fine_type_id, Accession_No, transaction_ID, Reason, Paid, Amount) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, getFineTypeId(conn, lateDays));
        pstmt.setInt(2, getAccessionNo(conn, memberId));
        pstmt.setInt(3, getTransactionId(conn, memberId));
        pstmt.setString(4, "Overdue");
        pstmt.setBoolean(5, false);
        pstmt.setDouble(6, finalFineAmount);
        
        pstmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(Security_deposit.class.getName()).log(Level.SEVERE, "Failed to insert fine record", ex);
    }
}

private int getFineTypeId(Connection conn, int lateDays) throws SQLException {
    String sql = "SELECT fine_type_id, fine_type_description FROM fines_type";
    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            String description = rs.getString("fine_type_description");
            int fineTypeId = rs.getInt("fine_type_id");

            if (description.startsWith("Day ")) {
                String[] parts = description.split(" ")[1].split("-");
                int startDay = Integer.parseInt(parts[0]);
                int endDay = parts[1].equals("onwards") ? Integer.MAX_VALUE : Integer.parseInt(parts[1]);

                if (lateDays >= startDay && lateDays <= endDay) {
                    return fineTypeId;
                }
            }
        }
    }
    return 0; // Return a default value if not found
}

private int getAccessionNo(Connection conn, String memberId) throws SQLException {
    String sql = "SELECT Accession_No FROM transactions WHERE B_ID = ?";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, memberId);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("Accession_No");
            }
        }
    }
    return 0; // Return a default value if not found
}
//for Damaged books


private void calculateFine(String accessionNo, Connection conn) {
        try {
            String isbn = getISBNByAccessionNo(conn, accessionNo);
            if (isbn == null) {
                JOptionPane.showMessageDialog(null, "ISBN not found for the given accession number");
                return;
            }

            double bookPrice = getBookPriceByISBN(conn, isbn);
            if (bookPrice <= 0) {
                JOptionPane.showMessageDialog(null, "Book price not found for the given ISBN");
                return;
            }

            double fineMultiplier = getDamagedBookFineMultiplier(conn);
            if (fineMultiplier <= 0) {
                JOptionPane.showMessageDialog(null, "Fine multiplier not found for damaged books");
                return;
            }

            double totalFine =bookPrice+ bookPrice * fineMultiplier;
            jLabel10.setText(" " + String.format("%.2f", totalFine));
        } catch (SQLException ex) {
            Logger.getLogger(fines_collecting.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "An error occurred while calculating the fine.");
        }
    }

    private String getISBNByAccessionNo(Connection conn, String accessionNo) throws SQLException {
        String sql = "SELECT ISBN_No FROM bookcopies WHERE Accession_No = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accessionNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ISBN_No");
                }
            }
        }
        return null;
    }

    private double getBookPriceByISBN(Connection conn, String isbn) throws SQLException {
        String sql = "SELECT Price FROM book WHERE ISBN_No = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Price");
                }
            }
        }
        return 0.0;
    }

    private double getDamagedBookFineMultiplier(Connection conn) throws SQLException {
        String sql = "SELECT fine_type_amount_for_Adults FROM fines_type WHERE fine_type_description = 'Damaged/Missed book'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("fine_type_amount_for_Adults");
            }
        }
        return 0.0;
    }
//damaged book record
    private void recordDamagedBookFine(String accessionNo, String memberId, Connection conn) throws SQLException {
        String isbn = getISBNByAccessionNo(conn, accessionNo);
        if (isbn == null) {
            JOptionPane.showMessageDialog(null, "ISBN not found for the given accession number");
            return;
        }

        double bookPrice = getBookPriceByISBN(conn, isbn);
        if (bookPrice <= 0) {
            JOptionPane.showMessageDialog(null, "Book price not found for the given ISBN");
            return;
        }

        double fineMultiplier = getDamagedBookFineMultiplier(conn);
        if (fineMultiplier <= 0) {
            JOptionPane.showMessageDialog(null, "Fine multiplier not found for damaged books");
            return;
        }

        double totalFine = bookPrice + bookPrice * fineMultiplier;

        // Insert damaged book fine record
        insertDamagedBookFineRecord(conn, memberId, totalFine, accessionNo);
    }

private void insertDamagedBookFineRecord(Connection conn, String memberId, double finalFineAmount, String accessionNo) {
    String sql = "INSERT INTO fines (fine_type_id, Accession_No, transaction_ID, Reason, Paid, Amount) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, getDamagedBookFineTypeId(conn));
        pstmt.setString(2, accessionNo);
       pstmt.setInt(3, getTransactionId(conn, memberId));
        pstmt.setString(4, "Damaged");
        pstmt.setBoolean(5, false);
        pstmt.setDouble(6, finalFineAmount);

        pstmt.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(Security_deposit.class.getName()).log(Level.SEVERE, "Failed to insert fine record", ex);
    }
}

  private int getTransactionId(Connection conn, String memberId) throws SQLException {
        String sql = "SELECT transaction_id FROM transactions WHERE B_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("transaction_id");
                }
            }
        }
        return 0;
    }
    /*private String getISBNByAccessionNo(Connection conn, String accessionNo) throws SQLException {
        String sql = "SELECT ISBN FROM bookcopies WHERE AccessionNo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accessionNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("ISBN");
                }
            }
        }
        return null;
    }
*/
   /* private double getBookPriceByISBN(Connection conn, String isbn) throws SQLException {
        String sql = "SELECT BookPrice FROM book WHERE ISBN = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("BookPrice");
                }
            }
        }
        return 0.0;
    }*/
/*
    private double getDamagedBookFineMultiplier(Connection conn) throws SQLException {
        String sql = "SELECT fine_type_amount_for_DamagedBooks FROM fines_type WHERE fine_type_description = 'Damaged Book'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("fine_type_amount_for_DamagedBooks");
            }
        }
        return 0.0;
    }
*/
    private int getDamagedBookFineTypeId(Connection conn) throws SQLException {
        String sql = "SELECT fine_type_id FROM fines_type WHERE fine_type_description = 'Damaged/Missed book'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("fine_type_id");
            }
        }
        return 0; // Return a default value if not found
    }

    /*private int getTransactionId(Connection conn, String memberId) throws SQLException {
        String sql = "SELECT transaction_id FROM transactions WHERE B_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("transaction_id");
                }
            }
        }
        return 0; // Return a default value if not found
    }*/
    public void showBookDetails(String accessionNo, Connection conn, JLabel nameLabel, JLabel priceLabel) {
    if (accessionNo.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please enter an accession number.");
        return;
    }

    String isbn = null;
    String bookName = null;
    double bookPrice = 0.0;

    try {
        // Step 1: Retrieve the ISBN number from the book_copies table
        String queryIsbn = "SELECT ISBN_No FROM bookcopies WHERE Accession_No = ?";
        PreparedStatement pstmtIsbn = conn.prepareStatement(queryIsbn);
        pstmtIsbn.setString(1, accessionNo);
        ResultSet rsIsbn = pstmtIsbn.executeQuery();

        if (rsIsbn.next()) {
            isbn = rsIsbn.getString("ISBN_No");
        } else {
            JOptionPane.showMessageDialog(null, "Accession number not found.");
            return;
        }

        // Step 2: Use the ISBN number to fetch the book name and price from the book table
        String queryBookDetails = "SELECT Title, Price FROM book WHERE ISBN_No = ?";
        PreparedStatement pstmtBookDetails = conn.prepareStatement(queryBookDetails);
        pstmtBookDetails.setString(1, isbn);
        ResultSet rsBookDetails = pstmtBookDetails.executeQuery();

        if (rsBookDetails.next()) {
            bookName = rsBookDetails.getString("Title");
            bookPrice = rsBookDetails.getDouble("Price");
        } else {
            JOptionPane.showMessageDialog(null, "Book details not found for the given ISBN.");
            return;
        }

        // Step 3: Display the book name and price in the labels
        jLabel17.setText(" " + bookName);
       jLabel5.setText("Rs. " + bookPrice);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "An error occurred while retrieving book details: " + e.getMessage());
    }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        memberfinesbox = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        rSButtonHover2 = new rojeru_san.complementos.RSButtonHover();
        jLabel6 = new javax.swing.JLabel();
        rSButtonHover3 = new rojeru_san.complementos.RSButtonHover();
        accessionNotext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(51, 102, 0)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 936, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 600, 940, 30));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 102, 0)));

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Search by Member Id");

        memberfinesbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/search.png"))); // NOI18N
        jLabel18.setText("Search Members");

        rSButtonHover2.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover2.setForeground(new java.awt.Color(153, 255, 0));
        rSButtonHover2.setText("Due Date Fine Generator");
        rSButtonHover2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Due Date & Damaged Book fines  Collecting");

        rSButtonHover3.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover3.setText("Damaged Book fine Generator");
        rSButtonHover3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover3ActionPerformed(evt);
            }
        });

        accessionNotext.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Accession No ");

        jButton1.setBackground(new java.awt.Color(153, 255, 0));
        jButton1.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Record ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 102, 102));
        jButton3.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Record ");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(memberfinesbox, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rSButtonHover2, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(accessionNotext, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3)
                    .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(87, 87, 87))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(608, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(memberfinesbox, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSButtonHover2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(accessionNotext, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)
                        .addComponent(jButton3))))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addGap(22, 22, 22)
                    .addComponent(jLabel6)
                    .addContainerGap(102, Short.MAX_VALUE)))
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 950, 150));

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Original Price of the book");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 430, 210, -1));

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 102));
        jLabel5.setText("Rs.");
        jLabel5.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 420, 250, 40));

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Rs.");
        jLabel8.setToolTipText("Rs.");
        jLabel8.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 490, 40, 50));

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/icons8-reminder-30_1.png"))); // NOI18N
        jLabel9.setText("Late days");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 290, 167, -1));

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/fines_3.png"))); // NOI18N
        jLabel11.setText("Fines  Amount ");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 500, 167, -1));

        jLabel13.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Damaged Book name");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 360, 190, -1));

        jLabel17.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 102, 102));
        jLabel17.setText("Book Name");
        jLabel17.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 350, 167, 40));

        jLabel19.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(153, 255, 0));
        jLabel19.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 280, 170, 50));

        jPanel1.setBackground(new java.awt.Color(51, 102, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 64, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1360, 34, -1, -1));

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Dunkannawa E- Library >>");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, 250, -1));

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/username_3.png"))); // NOI18N
        jLabel16.setText("Welcome to User Account");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 10, 300, 40));

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

        jPanel1.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 40, 10, 40));

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel12.setText("Home >>");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 60, 130, -1));

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Collect fines");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 60, 160, -1));

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 964, 90));

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setToolTipText("Rs.");
        jLabel10.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 490, 200, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 970, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 970, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 660, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       String memberId = memberfinesbox.getText();
        int lateDays = Integer.parseInt(jLabel19.getText());
        double finalFineAmount = Double.parseDouble(jLabel10.getText());

        try (Connection conn = new Helper.DatabaseConnection().connection()) {
            insertFineRecord(conn, memberId, lateDays, finalFineAmount);
            JOptionPane.showMessageDialog(this, "Record added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to add record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
      
/*String accessionNo = accessionNotext.getText();
                if (accessionNo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter an accession number.");
                } else {
                    String memberId = memberfinesbox.getText();
                    if (memberId.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter a member ID.");
                    } else {
                        try {
                            recordDamagedBookFine(accessionNo, memberId, conn);
                        } catch (SQLException ex) {
                            Logger.getLogger(fines_collecting.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "An error occurred while recording the fine.");
                        }*/
    }//GEN-LAST:event_jButton1ActionPerformed
                
    private void rSButtonHover2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover2ActionPerformed
        retrieveLateDays(); // TODO add your handling code here:
    }//GEN-LAST:event_rSButtonHover2ActionPerformed

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        Sachi.ui.Staff.Home_pg_1 homeback = new Sachi.ui.Staff.Home_pg_1();
        homeback.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel12MouseClicked

    private void rSButtonHover3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover3ActionPerformed
 String accessionNo = accessionNotext.getText();
                if (accessionNo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter an accession number.");
                } else {
                    showBookDetails(accessionNo, conn, jLabel17, jLabel5);

                    calculateFine(accessionNo, conn);
                    
                }  
    }//GEN-LAST:event_rSButtonHover3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
 String memberId = memberfinesbox.getText();
                String accessionNo = accessionNotext.getText();

                if (memberId.isEmpty() || accessionNo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Member ID and Accession No must not be empty");
                    return;
                }

                try {
                    recordDamagedBookFine(accessionNo, memberId, conn);
                    JOptionPane.showMessageDialog(null, "Fine recorded successfully");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to record fine: " + ex.getMessage());
                }
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(fines_collecting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fines_collecting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fines_collecting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fines_collecting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new fines_collecting().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField accessionNotext;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTextField memberfinesbox;
    private rojeru_san.complementos.RSButtonHover rSButtonHover2;
    private rojeru_san.complementos.RSButtonHover rSButtonHover3;
    // End of variables declaration//GEN-END:variables
}
