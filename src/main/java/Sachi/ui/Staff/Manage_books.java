package Sachi.ui.Staff;

import Helper.DatabaseConnection;
import Jframe.*;
import static Sachi.staff.book.ui.Book_issue.getDate;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import static javax.swing.text.html.HTML.Tag.SELECT;

/**
 *
 * @author USER
 */
public class Manage_books extends javax.swing.JFrame {

    public Manage_books() {
        initComponents();
        setNextAccessionNo();
        populateAuthorCombobox();
        populatePublisherCombobox();
        acquireddate.setText(getDate());
        populateAuthorCombobox();
        setupAutoComplete(authorCombobox);
    }

    public int getNextAccessionNo() {
    int nextAccessionNo = 1;

    try (Connection conn = new Helper.DatabaseConnection().connection()) {

        // Modify the query to get the last added accession number in descending order
        String query = "SELECT Accession_No FROM bookcopies ORDER BY Accession_No DESC LIMIT 1";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        // Retrieve and increment the last accession number
        if (rs.next()) {
            nextAccessionNo = rs.getInt(1) + 1;
        }
        rs.close();
        stmt.close();

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to retrieve the next Accession No");
    }

    return nextAccessionNo;
}


    private void setNextAccessionNo() {
        int nextAccessionNo = getNextAccessionNo();
        AccessionNo1.setText(String.valueOf(nextAccessionNo));

    }
    public String getCompleteISBN() {
    String part1 = isbnbox1.getText().trim();
    String part2 = isbnbox2.getText().trim();
    String part3 = isbnbox3.getText().trim();
    String part4 = isbnbox4.getText().trim();
    String part5 = isbnbox.getText().trim();

    // Concatenate all parts with hyphens
    String isbn = part1 + "-" + part2 + "-" + part3 + "-" + part4 + "-" + part5;

    return isbn;
}

    public void insertBookDetails() {
        try ( Connection conn = new Helper.DatabaseConnection().connection()) {

            try ( Statement stmt = conn.createStatement()) {
                stmt.execute("SET FOREIGN_KEY_CHECKS=0");
            }

           String isbn = getCompleteISBN(); 
            String price = pricebox.getText();
            String publisher = publishercombobox.getSelectedItem().toString();
            String acquireddatee = acquireddate.getText();
            int publishedyear = rSYearDate1.getYear();//*
            String reservedSec = (String) reserveCombobox.getSelectedItem();
            String bookName = bnamebox.getText();
            String pages = pgNobox.getText();
            String publishedPlace = publishplacebox.getText();
            String genre = generecombobox.getSelectedItem().toString();
            int authorid = Integer.parseInt(authorCombobox.getSelectedItem().toString().split(" ")[0]);
            String classificationNo = classificationNolabel.getText();

            String supacquiredmethodtext = (String) methodComboBox.getSelectedItem();
            String donacquiredmethodtext = (String) methodComboBox.getSelectedItem();

            String q = "INSERT INTO book (Genre, Classification_No, ISBN_No, Price, Publisher_Name, PublishedYear, reserved_section, No_of_pages, Publisher_place,Title, Author_ID, Book_adding_Date, copies) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement pstm = conn.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, genre);
            pstm.setString(2, classificationNo);
            pstm.setString(3, isbn);
            pstm.setString(4, price);
            pstm.setString(5, publisher);
            pstm.setInt(6, publishedyear);
            pstm.setString(7, reservedSec);
            pstm.setString(8, pages);
            pstm.setString(9, publishedPlace);
            pstm.setString(10, bookName);
            pstm.setInt(11, authorid);
            pstm.setDate(12, new java.sql.Date(new java.util.Date().getTime()));
            pstm.setInt(13, 0);

            pstm.executeUpdate();

            String qcop = "INSERT INTO bookcopies ( ISBN_No, AcquisitionDate) VALUES ( ?, ?)";
            PreparedStatement pcop = conn.prepareStatement(qcop, Statement.RETURN_GENERATED_KEYS);
            pcop.setString(1, isbn);
            pcop.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
            pcop.executeUpdate();

            ResultSet bookRs = pcop.getGeneratedKeys();
            int lastBookId = -1;
            if (bookRs.next()) {
                lastBookId = bookRs.getInt(1);
            }

            if (lastBookId != -1) {

                if (jRadioButton1.isSelected()) {

                    String supplierIdText = methodComboBox.getSelectedItem().toString().split(" ")[0]; // Assuming this is the supplier ID

                    int supplierId = Integer.parseInt(supplierIdText);

                    String acquisitionQuery = "INSERT INTO book_supplier (Supplier_ID, Accession_No)VALUES (?, ?)";
                    PreparedStatement acquisitionStmt = conn.prepareStatement(acquisitionQuery);
 System.out.println(supplierId);
                    acquisitionStmt.setInt(1, supplierId);
                    acquisitionStmt.setInt(2, lastBookId);
                    acquisitionStmt.executeUpdate();
                } else if (jRadioButton2.isSelected()) {

                    String donatorIdText = methodComboBox.getSelectedItem().toString().split(" ")[0];

                    int donatorId = Integer.parseInt(donatorIdText);
System.out.println(lastBookId+donatorId);
                    String checkDonatorQuery = "INSERT INTO book_donator (Donator_ID, Accession_No) VALUES (?, ?)";
                    PreparedStatement checkDonatorStmt = conn.prepareStatement(checkDonatorQuery);

                    checkDonatorStmt.setInt(1, donatorId);
                    checkDonatorStmt.setInt(2, lastBookId);

                    checkDonatorStmt.executeUpdate();;
 
                }
                JOptionPane.showMessageDialog(this, "Book details and acquisition method inserted successfully ");

            } else {
                JOptionPane.showMessageDialog(this, "Book details and acquisition method inserted Unsuccessfully ");

            }
        } catch (SQLException e) {
            System.out.println("Error inserting book details: " + e.getMessage());
        }

    }

    public ResultSet getAllSuppliers() {
        ResultSet rs = null;
        try ( Connection conn = new Helper.DatabaseConnection().connection();) {

            String query = "SELECT Supplier_ID, Supplier_Name FROM suppliers";
            PreparedStatement pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching suppliers: " + e.getMessage());
        }
        return rs;
    }

    public ResultSet getAllDonors() {
        ResultSet rs = null;
        try ( Connection conn = new Helper.DatabaseConnection().connection();) {

            String query = "SELECT Donator_ID, Donator_Name FROM donator";
            PreparedStatement pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error fetching suppliers: " + e.getMessage());
        }
        return rs;
    }

    private void populateSupplierComboBox() {

        try ( Connection conn = new Helper.DatabaseConnection().connection();) {
            methodComboBox.removeAllItems();
            methodComboBox.addItem("Please Select Supplier");
            String query = "SELECT Supplier_ID, Supplier_Name FROM supplier";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs != null && rs.next()) {
                int supplierId = rs.getInt("Supplier_ID");
                String supplierName = rs.getString("Supplier_Name");
                methodComboBox.addItem(String.valueOf(supplierId + " " + supplierName));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching suppliers: " + e.getMessage());
        }

    }

    private void populateAuthorCombobox() {
        authorCombobox.addItem("Please Select Author");
        try ( Connection conn = new Helper.DatabaseConnection().connection();) {

            String query = "SELECT Author_Name,Author_ID FROM author";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs != null && rs.next()) {

                String authorName = rs.getString("Author_Name");
                String authorid = rs.getString("Author_ID");
                authorCombobox.addItem(String.valueOf(authorid + " " + authorName));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Authors: " + e.getMessage());
        }
    }
     private void setupAutoComplete(JComboBox<String> authorCombobox) {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < authorCombobox.getItemCount(); i++) {
            items.add((String) authorCombobox.getItemAt(i));
        }

        JTextField textField = (JTextField) authorCombobox.getEditor().getEditorComponent();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                SwingUtilities.invokeLater(() -> {
                    String text = textField.getText();
                    if (text.isEmpty()) {
                       authorCombobox.hidePopup();
                        return;
                    }

                    authorCombobox.removeAllItems();
                    for (String item : items) {
                        if (item.toLowerCase().contains(text.toLowerCase())) {
                            authorCombobox.addItem(item);
                        }
                    }

                    textField.setText(text);
                   authorCombobox.showPopup();
                });
            }
        });

        authorCombobox.setEditable(true);
        }
    private void populateDonorComboBox() {

        try ( Connection conn = new Helper.DatabaseConnection().connection();) {
            methodComboBox.removeAllItems();
            methodComboBox.addItem("Please Select Donor");

            String query = "SELECT Donator_ID, Donator_Name FROM donator";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs != null && rs.next()) {
                int donorId = rs.getInt("Donator_ID");
                String donorName = rs.getString("Donator_Name");
                methodComboBox.addItem(String.valueOf(donorId + " " + donorName));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching suppliers: " + e.getMessage());
        }

    }

    private void populatePublisherCombobox() {
        publishercombobox.addItem("Please Select Publisher");
        try ( Connection conn = new Helper.DatabaseConnection().connection();) {

            String query = "SELECT Publisher_Name FROM publisher";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs != null && rs.next()) {

                String publisherName = rs.getString("Publisher_Name");
                publishercombobox.addItem(String.valueOf(publisherName));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching Authors: " + e.getMessage());
        }
    }

    private void checkAvailability() {
    String isbn = getCompleteISBN(); // Use the method to get the full ISBN number
    int availableCopies = getAvailableCopies(isbn);
    copyamountbix.setText("Available " + availableCopies + " copies");

    if (availableCopies > 0) {
        retrieveAndDisplayBookDetails();
    } else {
        // clearBookDetails(); // Uncomment this line if you have a method to clear book details
    }
}

    private int getAvailableCopies(String isbn) {
        int count = 0;
        String query = "SELECT COUNT(*) AS copyCount FROM bookcopies WHERE ISBN_No = ?";

        try ( Connection conn = new Helper.DatabaseConnection().connection();  PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("copyCount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    private void retrieveAndDisplayBookDetails() {
        String selectQuery = "SELECT Classification_No, Title, Author_ID, Publisher_Name, Publisher_place, PublishedYear, Genre, reserved_section, No_of_pages, Price FROM book WHERE ISBN_No = ?";

        try ( Connection conn = new Helper.DatabaseConnection().connection();  PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
String isbn=isbnbox.getText();
            selectStmt.setString(1, isbn);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String classificationNo = rs.getString("Classification_No");
                String bookName = rs.getString("Title");
                String authorId = rs.getString("Author_ID");
                String publisher = rs.getString("Publisher_Name");
                String publishedPlace = rs.getString("Publisher_place");
                int publishedyear = rs.getInt("PublishedYear");
                String genre = rs.getString("Genre");
                String reservedSec = rs.getString("reserved_section");
                String pages = rs.getString("No_of_pages");
                String price = rs.getString("Price");

                // Display the retrieved details in the textboxes
                classificationNolabel.setText(classificationNo);
                bnamebox.setText(bookName);
                pricebox.setText(price);

                rSYearDate1.setYear(ERROR);
                rSYearDate1.setYear(publishedyear);
                reserveCombobox.setSelectedItem(reservedSec);
                generecombobox.setSelectedItem(genre);
                
                pgNobox.setText(pages);
                publishplacebox.setText(publishedPlace);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearBookDetails() {
        classificationNolabel.setText("");
        bnamebox.setText("");
        isbnbox.setText("");
        pricebox.setText("");
        publishercombobox.setSelectedItem(null);
        rSYearDate1.setYear(Calendar.getInstance().get(Calendar.YEAR));
        reserveCombobox.setSelectedItem(null);
        generecombobox.setSelectedItem(null);
         bNamebox.setText("");
        pgNobox.setText("");
        publishplacebox.setText("");
        methodComboBox.setSelectedItem(null);
    }

    private void addBookCopy() {
    String isbn = getCompleteISBN();
    String acquiredDate = acquireddate.getText();
    String copiesText = copiesAmountBox.getSelectedItem().toString().trim(); // Trim the string to remove extra spaces
    int copies = 0;
    
    try {
        copies = Integer.parseInt(copiesText); // Parse the trimmed string to an integer
    } catch (NumberFormatException e) {
        // Handle the case where the input is not a valid integer
        JOptionPane.showMessageDialog(this, "Please enter a valid number of copies.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return; // Exit the method if the input is invalid
    }

    String insertQuery = "INSERT INTO bookcopies (ISBN_No, AcquisitionDate) VALUES (?, ?)";

    try (Connection conn = new Helper.DatabaseConnection().connection();
         PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

        for (int i = 0; i < copies; i++) {
            insertStmt.setString(1, isbn);
            insertStmt.setString(2, acquiredDate);
            insertStmt.executeUpdate();
        }

        JOptionPane.showMessageDialog(this, copies + " book copies added successfully.");

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
    }
}



    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        pricebox = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        pgNobox = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        isbnbox = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        publishplacebox = new javax.swing.JTextField();
        bNamebox = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        publishercombobox = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        reserveCombobox = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        rSButtonHover4 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover6 = new rojeru_san.complementos.RSButtonHover();
        acquireddate = new javax.swing.JLabel();
        generecombobox = new javax.swing.JComboBox<>();
        methodComboBox = new javax.swing.JComboBox<>();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        classificationNolabel = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        authorCombobox = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        copyamountbix = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        bnamebox = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        AccessionNo1 = new javax.swing.JLabel();
        rSYearDate1 = new rojeru_san.componentes.RSYearDate();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        copiesAmountBox = new javax.swing.JComboBox<>();
        isbnbox1 = new javax.swing.JTextField();
        isbnbox2 = new javax.swing.JTextField();
        isbnbox3 = new javax.swing.JTextField();
        isbnbox4 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 102, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel40.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel40.setText("Home >>");
        jLabel40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel40MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 60, 109, 33));

        jLabel42.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel42.setText("Library Management system >>");
        jPanel1.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 288, -1));

        jLabel43.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/User Account.png"))); // NOI18N
        jLabel43.setText("Welcome to User Account");
        jPanel1.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 50, 371, -1));

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

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 52, -1, -1));

        jLabel45.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Books_26px.png"))); // NOI18N
        jLabel45.setText("Book Inventory");
        jLabel45.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel45MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 60, 172, 33));

        jPanel9.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 127, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 100, -1, -1));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        jPanel7.setBackground(new java.awt.Color(0, 0, 0));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Accession No");
        jPanel7.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 15, 144, -1));

        jLabel21.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/search_1.png"))); // NOI18N
        jLabel21.setText("Search");
        jPanel7.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(718, 17, 156, -1));

        pricebox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        pricebox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceboxActionPerformed(evt);
            }
        });
        jPanel7.add(pricebox, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 270, 120, 34));

        jLabel27.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("No of Pages");
        jPanel7.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 159, -1));

        pgNobox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jPanel7.add(pgNobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 220, 100, 36));

        jLabel30.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("ISBN No");
        jPanel7.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 70, 86, -1));

        isbnbox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        isbnbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isbnboxActionPerformed(evt);
            }
        });
        jPanel7.add(isbnbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 70, 70, 36));

        jLabel31.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Author");
        jPanel7.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 180, 60, -1));

        jLabel32.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Publisher");
        jPanel7.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 360, 94, -1));

        jLabel33.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText(" published Year");
        jPanel7.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 420, 151, -1));

        jLabel34.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Place of Published");
        jLabel34.setToolTipText("");
        jPanel7.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 250, -1, 15));

        publishplacebox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jPanel7.add(publishplacebox, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 240, 190, 30));

        bNamebox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        bNamebox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNameboxActionPerformed(evt);
            }
        });
        bNamebox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                bNameboxPropertyChange(evt);
            }
        });
        bNamebox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bNameboxKeyTyped(evt);
            }
        });
        bNamebox.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                bNameboxVetoableChange(evt);
            }
        });
        jPanel7.add(bNamebox, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 12, 420, 31));
        bNamebox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateSuggestions(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateSuggestions(); }

            private void updateSuggestions() {
                String text =bNamebox.getText().toLowerCase();

                ArrayList<String> geners = new ArrayList<>();
                geners.addAll(List.of("000 -Computer science information general works" ,
                    "100 - Philosophy & psychology",
                    "200 - Religion" ,
                    "300 - Social sciences" ,
                    "400 - Language" ,
                    "500 - Science" ,
                    "600 - Technology" ,
                    "700 - Arts & recreation" ,
                    "800 - Literature" ,
                    "900 - History & geography"));
            for(String keyword : geners) {
                if (keyword.toLowerCase().startsWith(text)) {
                    generecombobox.getModel().setSelectedItem(keyword);
                }

            }
        }
    });

    jLabel35.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel35.setForeground(new java.awt.Color(255, 153, 0));
    jLabel35.setText("Acquiring Method");
    jPanel7.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 320, 151, -1));

    publishercombobox.setBackground(new java.awt.Color(0, 0, 0));
    publishercombobox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    publishercombobox.setForeground(new java.awt.Color(255, 255, 255));
    publishercombobox.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            publishercomboboxActionPerformed(evt);
        }
    });
    jPanel7.add(publishercombobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 350, 230, -1));

    jLabel36.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel36.setForeground(new java.awt.Color(255, 255, 255));
    jLabel36.setText("Reserved Section");
    jPanel7.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 470, 151, 35));

    reserveCombobox.setBackground(new java.awt.Color(0, 0, 0));
    reserveCombobox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    reserveCombobox.setForeground(new java.awt.Color(255, 255, 255));
    reserveCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Children", "Adults", " ", " " }));
    reserveCombobox.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            reserveComboboxActionPerformed(evt);
        }
    });
    jPanel7.add(reserveCombobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 470, 240, -1));

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
            .addContainerGap(675, Short.MAX_VALUE)
            .addComponent(rSButtonHover6, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(75, 75, 75)
            .addComponent(rSButtonHover4, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(60, 60, 60))
    );
    jPanel3Layout.setVerticalGroup(
        jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel3Layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(rSButtonHover4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(rSButtonHover6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel7.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(58, 513, 1387, -1));

    acquireddate.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    acquireddate.setForeground(new java.awt.Color(255, 255, 255));
    acquireddate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 0)));
    jPanel7.add(acquireddate, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 310, 230, 30));

    generecombobox.setBackground(new java.awt.Color(0, 0, 0));
    generecombobox.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
    generecombobox.setForeground(new java.awt.Color(255, 255, 255));
    generecombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "000 -Computer science information ,general works", "100 - Philosophy & psychology", "200 - Religion", "300 - Social sciences", "400 - Language", "500 - Science", "600 - Technology", "700 - Arts & recreation", "800 - Literature", "900 - History & geography", "823 - English fiction" }));
    generecombobox.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            generecomboboxActionPerformed(evt);
        }
    });
    jPanel7.add(generecombobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(944, 72, 420, -1));

    methodComboBox.setBackground(new java.awt.Color(0, 0, 0));
    methodComboBox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    methodComboBox.setForeground(new java.awt.Color(255, 255, 255));
    methodComboBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
        }
        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
        }
        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            methodComboBoxPopupMenuWillBecomeVisible(evt);
        }
    });
    methodComboBox.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            methodComboBoxActionPerformed(evt);
        }
    });
    jPanel7.add(methodComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 320, 180, -1));

    jRadioButton1.setBackground(new java.awt.Color(0, 0, 0));
    buttonGroup.add(jRadioButton1);
    jRadioButton1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jRadioButton1.setForeground(new java.awt.Color(255, 255, 255));
    jRadioButton1.setText("Supplier");
    jRadioButton1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 1, true));
    jRadioButton1.setBorderPainted(true);
    jRadioButton1.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jRadioButton1MouseClicked(evt);
        }
    });
    jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jRadioButton1ActionPerformed(evt);
        }
    });
    jPanel7.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 350, 119, 39));

    jRadioButton2.setBackground(new java.awt.Color(0, 0, 0));
    buttonGroup.add(jRadioButton2);
    jRadioButton2.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jRadioButton2.setForeground(new java.awt.Color(255, 255, 255));
    jRadioButton2.setText("Donor");
    jRadioButton2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 1, true));
    jRadioButton2.setBorderPainted(true);
    jRadioButton2.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jRadioButton2MouseClicked(evt);
        }
    });
    jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jRadioButton2ActionPerformed(evt);
        }
    });
    jPanel7.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 420, 120, 38));

    classificationNolabel.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jPanel7.add(classificationNolabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(944, 130, 420, 30));

    jLabel29.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel29.setForeground(new java.awt.Color(255, 255, 255));
    jLabel29.setText("Book Genre");
    jPanel7.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(718, 75, -1, -1));

    authorCombobox.setEditable(true);
    authorCombobox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    authorCombobox.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            authorComboboxActionPerformed(evt);
        }
    });
    jPanel7.add(authorCombobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 180, 230, 35));

    jLabel23.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel23.setForeground(new java.awt.Color(0, 102, 102));
    jLabel23.setText("Copies Amount");
    jPanel7.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, 140, -1));

    jLabel25.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel25.setForeground(new java.awt.Color(255, 255, 255));
    jLabel25.setText("Name of the Book");
    jPanel7.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, 194, -1));

    copyamountbix.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    copyamountbix.setForeground(new java.awt.Color(0, 102, 102));
    copyamountbix.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 0)));
    jPanel7.add(copyamountbix, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 120, 228, 42));

    jLabel37.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel37.setForeground(new java.awt.Color(255, 255, 255));
    jLabel37.setText("Classification No");
    jPanel7.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(718, 132, 157, -1));

    jLabel38.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel38.setForeground(new java.awt.Color(0, 102, 102));
    jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/overdue books_2.png"))); // NOI18N
    jLabel38.setText("Check Copies");
    jLabel38.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jLabel38MouseClicked(evt);
        }
    });
    jPanel7.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 150, -1));

    jLabel39.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel39.setForeground(new java.awt.Color(255, 255, 255));
    jLabel39.setText("Acquired Date");
    jPanel7.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 160, -1));

    bnamebox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jPanel7.add(bnamebox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 180, 228, 31));

    jLabel44.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel44.setForeground(new java.awt.Color(255, 255, 255));
    jLabel44.setText("Price");
    jPanel7.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 94, -1));

    AccessionNo1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    AccessionNo1.setForeground(new java.awt.Color(255, 255, 255));
    AccessionNo1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 102, 0)));
    jPanel7.add(AccessionNo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(563, 12, 110, 42));

    rSYearDate1.setBackground(new java.awt.Color(255, 255, 255));
    rSYearDate1.setForeground(new java.awt.Color(255, 255, 255));
    rSYearDate1.setColorBackground(new java.awt.Color(51, 102, 0));
    rSYearDate1.setColorButtonHover(new java.awt.Color(51, 102, 0));
    rSYearDate1.setName("Select year"); // NOI18N
    rSYearDate1.setTxtFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
    jPanel7.add(rSYearDate1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 400, 230, -1));

    jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Add.png"))); // NOI18N
    jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jLabel2MouseClicked(evt);
        }
    });
    jPanel7.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 180, 50, 30));

    jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/icons8-refresh-30.png"))); // NOI18N
    jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jLabel3MouseClicked(evt);
        }
    });
    jPanel7.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 180, -1, 30));

    jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/icons8-refresh-30.png"))); // NOI18N
    jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jLabel4MouseClicked(evt);
        }
    });
    jPanel7.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 360, 40, 40));

    jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/icons8-checking-24.png"))); // NOI18N
    jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jLabel5MouseClicked(evt);
        }
    });
    jPanel7.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 120, 40, 40));

    jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Add.png"))); // NOI18N
    jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jLabel6MouseClicked(evt);
        }
    });
    jPanel7.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 120, 40, 40));

    jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Add.png"))); // NOI18N
    jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jLabel7MouseClicked(evt);
        }
    });
    jPanel7.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 360, 30, 30));

    jLabel1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel1.setText("Rs.");
    jPanel7.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 270, 30, 30));

    copiesAmountBox.setEditable(true);
    copiesAmountBox.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    copiesAmountBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1  ", "2  ", "3  ", "4  ", "5  ", "6  ", "7  ", "8  ", "9  ", "10  ", "11  ", "12  ", "13  ", "14  ", "15  ", "16  ", "17  ", "18  ", "19  ", "20  ", "21  ", "22  ", "23  ", "24  ", "25  ", "26  ", "27  ", "28  ", "29  ", "30  ", "31  ", "32  ", "33  ", "34  ", "35  ", "36  ", "37  ", "38  ", "39  ", "40  ", "41  ", "42  ", "43  ", "44  ", "45  ", "46  ", "47  ", "48  ", "49  ", "50  ", "51  ", "52  ", "53  ", "54  ", "55  ", "56  ", "57  ", "58  ", "59  ", "60  ", "61  ", "62  ", "63  ", "64  ", "65  ", "66  ", "67  ", "68  ", "69  ", "70  ", "71  ", "72  ", "73  ", "74  ", "75  ", "76  ", "77  ", "78  ", "79  ", "80  ", "81  ", "82  ", "83  ", "84  ", "85  ", "86  ", "87  ", "88  ", "89  ", "90  ", "91  ", "92  ", "93  ", "94  ", "95  ", "96  ", "97  ", "98  ", "99  ", "100" }));
    jPanel7.add(copiesAmountBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 70, 30));

    isbnbox1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    isbnbox1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            isbnbox1ActionPerformed(evt);
        }
    });
    jPanel7.add(isbnbox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, 50, 36));

    isbnbox2.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    isbnbox2.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            isbnbox2ActionPerformed(evt);
        }
    });
    jPanel7.add(isbnbox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 70, 30, 36));

    isbnbox3.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    isbnbox3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            isbnbox3ActionPerformed(evt);
        }
    });
    jPanel7.add(isbnbox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, 40, 36));

    isbnbox4.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    isbnbox4.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            isbnbox4ActionPerformed(evt);
        }
    });
    jPanel7.add(isbnbox4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 70, 90, 36));

    jLabel8.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel8.setText(" -");
    jPanel7.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, 20, 30));

    jLabel9.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel9.setText(" -");
    jPanel7.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 20, 10));

    jLabel10.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel10.setText(" -");
    jPanel7.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, 20, 10));

    jLabel11.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
    jLabel11.setText(" -");
    jPanel7.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, 20, 10));

    jLabel18.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
    jLabel18.setForeground(new java.awt.Color(255, 153, 0));
    jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Add.png"))); // NOI18N
    jLabel18.setText("Add Books");
    jLabel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(66, 66, 66)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap())
    );
    jPanel2Layout.setVerticalGroup(
        jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel2Layout.createSequentialGroup()
            .addGap(1, 1, 1)
            .addComponent(jLabel18)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(228, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

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

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        populateDonorComboBox();
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton2MouseClicked

    }//GEN-LAST:event_jRadioButton2MouseClicked

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        populateSupplierComboBox();

    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jRadioButton1MouseClicked

    }//GEN-LAST:event_jRadioButton1MouseClicked

    private void methodComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_methodComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_methodComboBoxActionPerformed

    private void methodComboBoxPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_methodComboBoxPopupMenuWillBecomeVisible

    }//GEN-LAST:event_methodComboBoxPopupMenuWillBecomeVisible

    private void rSButtonHover6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover6ActionPerformed
        insertBookDetails();
        
        setNextAccessionNo();
        checkAvailability(); 
        
    }//GEN-LAST:event_rSButtonHover6ActionPerformed

    private void rSButtonHover4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover4ActionPerformed
        clearBookDetails();
    }//GEN-LAST:event_rSButtonHover4ActionPerformed

    private void reserveComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reserveComboboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reserveComboboxActionPerformed

    private void publishercomboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publishercomboboxActionPerformed

    }//GEN-LAST:event_publishercomboboxActionPerformed

    private void priceboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_priceboxActionPerformed

    private void generecomboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generecomboboxActionPerformed

    }//GEN-LAST:event_generecomboboxActionPerformed

    private void authorComboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_authorComboboxActionPerformed

    }//GEN-LAST:event_authorComboboxActionPerformed

    private void bNameboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNameboxActionPerformed

    }//GEN-LAST:event_bNameboxActionPerformed

    private void bNameboxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bNameboxKeyTyped
        System.out.println(evt.toString());
    }//GEN-LAST:event_bNameboxKeyTyped

    private void bNameboxPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_bNameboxPropertyChange

    }//GEN-LAST:event_bNameboxPropertyChange

    private void bNameboxVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_bNameboxVetoableChange
        // TODO add your handling code here:
    }//GEN-LAST:event_bNameboxVetoableChange

    private void isbnboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isbnboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_isbnboxActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
 Sachi.staff.Settings.Ui.add_author addAuthor = new Sachi.staff.Settings.Ui.add_author();
        addAuthor.show();      // TODO add your handling code here:
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
      populateSupplierComboBox();  // TODO add your handling code here:
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
 populatePublisherCombobox();
     // TODO add your handling code here:
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
checkAvailability();      // TODO add your handling code here:
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
addBookCopy(); 
 setNextAccessionNo();
checkAvailability(); 
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
Sachi.staff.Settings.Ui.Add_New_Publishers addpub = new Sachi.staff.Settings.Ui.Add_New_Publishers();
        addpub.show();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel38MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel38MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel38MouseClicked

    private void isbnbox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isbnbox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_isbnbox1ActionPerformed

    private void isbnbox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isbnbox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_isbnbox2ActionPerformed

    private void isbnbox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isbnbox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_isbnbox3ActionPerformed

    private void isbnbox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isbnbox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_isbnbox4ActionPerformed

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
            java.util.logging.Logger.getLogger(Manage_books.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Manage_books.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Manage_books.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Manage_books.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Manage_books().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AccessionNo1;
    private javax.swing.JLabel acquireddate;
    private javax.swing.JComboBox<String> authorCombobox;
    private javax.swing.JTextField bNamebox;
    private javax.swing.JTextField bnamebox;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField classificationNolabel;
    private javax.swing.JComboBox<String> copiesAmountBox;
    private javax.swing.JLabel copyamountbix;
    private javax.swing.JComboBox<String> generecombobox;
    private javax.swing.JTextField isbnbox;
    private javax.swing.JTextField isbnbox1;
    private javax.swing.JTextField isbnbox2;
    private javax.swing.JTextField isbnbox3;
    private javax.swing.JTextField isbnbox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
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
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JComboBox<String> methodComboBox;
    private javax.swing.JTextField pgNobox;
    private javax.swing.JTextField pricebox;
    private javax.swing.JComboBox<String> publishercombobox;
    private javax.swing.JTextField publishplacebox;
    private rojeru_san.complementos.RSButtonHover rSButtonHover4;
    private rojeru_san.complementos.RSButtonHover rSButtonHover6;
    private rojeru_san.componentes.RSYearDate rSYearDate1;
    private javax.swing.JComboBox<String> reserveCombobox;
    // End of variables declaration//GEN-END:variables
}
