package Sachi.staff.book.ui;

import Sachi.staff.patron.ui.Membership_renewal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * @author USER
 */
public class Book_issue extends javax.swing.JFrame {

    public static void getDate(JTextField regDate) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public Book_issue() {
        initComponents();
        transaction_startDate.setText(getDate());
    }

    private boolean checkMemberBookLimit(String memberId) {
        boolean hasExceededBookLimit = false;

        String query = "SELECT No_of_Books_Issue, no_ofbooks_issued FROM borrower WHERE B_id = ?";

        try ( Connection connection = new Helper.DatabaseConnection().connection();  PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, memberId);

            try ( ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int bookLimit = resultSet.getInt("No_of_Books_Issue");
                    int booksIssued = resultSet.getInt("no_ofbooks_issued");

                    hasExceededBookLimit = booksIssued >= bookLimit;

                } else {
                    JOptionPane.showMessageDialog(this, "Member ID not found");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error ");
        }

        return hasExceededBookLimit;
    }

    public void retrieveBookData(String bookId) throws SQLException {

        try ( Connection conn = new Helper.DatabaseConnection().connection()) {
            String sql = "select Classification_No,b.ISBN_No,Title from book b inner join bookcopies bc on b.ISBN_No = bc.ISBN_No where bc.Accession_No = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, bookId);
            try ( ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    classificationNo.setText(rs.getString("Classification_No"));
                    isbnNo.setText(rs.getString("ISBN_No"));
                    bookname.setText(rs.getString("Title"));
                    LocalDate issueDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    transaction_startDate.setText(issueDate.format(formatter));

                    LocalDate dueDate = issueDate.plusWeeks(2);
                    String dueDateString = dueDate.format(formatter);

                    due_date.setText(dueDateString);

                } else {
                    JOptionPane.showMessageDialog(this, "  Wrong Accession No");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error accessing the database");
        }

    }

    private void clearFields() {
        classificationNo.setText("");
        isbnNo.setText("");
        bookname.setText("");
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        return dateFormat.format(date);
    }

    private void saveIssue() {
        String mId = b_id.getText();
        String accessionNo = accession_no.getText();
        String issueDate = transaction_startDate.getText();

        String insertSQL = "INSERT INTO transactions (b_id, transaction_date, Accession_no) VALUES (?, ?, ?)";
        String updateSQL = "UPDATE borrower SET no_ofbooks_issued = no_ofbooks_issued + 1 WHERE B_id = ?";
        String updateBookSQL = "UPDATE transactions SET transaction_type = 'Loan' WHERE Accession_No = ?";

        try ( Connection conn = new Helper.DatabaseConnection().connection()) {

            conn.setAutoCommit(false);

            try {

                PreparedStatement pstmt = conn.prepareStatement(insertSQL);
                pstmt.setInt(1, Integer.parseInt(mId));
                pstmt.setString(2, issueDate);
                pstmt.setInt(3, Integer.parseInt(accessionNo));
                pstmt.executeUpdate();

                PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
                updateStmt.setInt(1, Integer.parseInt(mId));
                updateStmt.executeUpdate();

                PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSQL);
                updateBookStmt.setInt(1, Integer.parseInt(accessionNo));
                updateBookStmt.executeUpdate();

                
                conn.commit();

                JOptionPane.showMessageDialog(this, "Book Issued successfully");
                b_id.setText("");
                accession_no.setText("");
                transaction_startDate.setText("");
                due_date.setText("");
                classificationNo.setText("");
                isbnNo.setText("");
                bookname.setText("");
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error Issuing Book");
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Connection Error");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jPanel15 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        b_id = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        dateshow = new javax.swing.JLabel();
        classificationNo = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        isbnNo = new javax.swing.JLabel();
        accession_no = new javax.swing.JTextField();
        member_status = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        bookname = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        rSButtonPane1 = new rojeru_san.complementos.RSButtonPane();
        jLabel86 = new javax.swing.JLabel();
        transaction_startDate = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel80 = new javax.swing.JLabel();
        due_date = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel83 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane2.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPane2.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jPanel15.setBackground(new java.awt.Color(0, 0, 0));
        jPanel15.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 102, 0)));
        jPanel15.setForeground(new java.awt.Color(0, 0, 0));
        jPanel15.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        jLabel74.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("Accession No");

        jLabel75.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(255, 255, 255));
        jLabel75.setText("Classification No");

        jLabel76.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setText("Name of the Book");

        jLabel77.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(255, 153, 0));
        jLabel77.setText("Membership ID");

        jLabel78.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(102, 255, 204));
        jLabel78.setText("Issue Date");

        b_id.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        b_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel79.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("ISBN No");

        dateshow.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        dateshow.setForeground(new java.awt.Color(255, 255, 255));

        classificationNo.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        classificationNo.setForeground(new java.awt.Color(255, 255, 255));
        classificationNo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));

        jLabel84.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(255, 255, 255));
        jLabel84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/search_2.png"))); // NOI18N
        jLabel84.setText("Search");

        isbnNo.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        isbnNo.setForeground(new java.awt.Color(255, 255, 255));
        isbnNo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));

        accession_no.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        accession_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });

        member_status.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        member_status.setForeground(new java.awt.Color(204, 255, 204));
        member_status.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));

        jLabel88.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(255, 153, 0));
        jLabel88.setText("Member status");

        bookname.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        bookname.setForeground(new java.awt.Color(255, 255, 255));
        bookname.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));

        jLabel91.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(204, 255, 204));
        jLabel91.setText("Book details");
        jLabel91.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(204, 255, 204)));

        jLabel82.setBackground(new java.awt.Color(51, 102, 0));
        jLabel82.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(102, 255, 102));
        jLabel82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/Add.png"))); // NOI18N
        jLabel82.setText("Add Another");
        jLabel82.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 255, 204)));
        jLabel82.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel82MouseClicked(evt);
            }
        });

        rSButtonPane1.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonPane1.setColorHover(new java.awt.Color(0, 102, 0));
        rSButtonPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSButtonPane1MouseClicked(evt);
            }
        });

        jLabel86.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(255, 255, 255));
        jLabel86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Book_26px.png"))); // NOI18N
        jLabel86.setText("Issue");
        jLabel86.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel86MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout rSButtonPane1Layout = new javax.swing.GroupLayout(rSButtonPane1);
        rSButtonPane1.setLayout(rSButtonPane1Layout);
        rSButtonPane1Layout.setHorizontalGroup(
            rSButtonPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rSButtonPane1Layout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        rSButtonPane1Layout.setVerticalGroup(
            rSButtonPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel86, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        transaction_startDate.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        transaction_startDate.setForeground(new java.awt.Color(255, 255, 255));
        transaction_startDate.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));

        jButton1.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/search_4_1.png"))); // NOI18N
        jButton1.setText("Search\n");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel80.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(102, 255, 204));
        jLabel80.setText("Due Date");

        due_date.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        due_date.setForeground(new java.awt.Color(255, 255, 255));
        due_date.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));

        jButton2.setBackground(new java.awt.Color(51, 102, 0));
        jButton2.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Search");
        jButton2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 102, 0)));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(51, 102, 0));
        jButton3.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Search");
        jButton3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 102, 0)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel83.setBackground(new java.awt.Color(51, 102, 0));
        jLabel83.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(102, 255, 102));
        jLabel83.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Lib_icons/icons8-remove-administrator-48.png"))); // NOI18N
        jLabel83.setText("Clear book details");
        jLabel83.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 255, 204)));
        jLabel83.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel83MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel76, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                                .addComponent(jLabel78, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel80, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel74, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel75, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(isbnNo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(723, 723, 723))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(transaction_startDate, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateshow, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                                .addGap(569, 569, 569))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(b_id, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(member_status, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(classificationNo, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(20, 20, 20)
                                            .addComponent(accession_no, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(38, 38, 38)
                                            .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(bookname, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(rSButtonPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(114, 114, 114)
                                        .addComponent(jLabel82, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(due_date, javax.swing.GroupLayout.PREFERRED_SIZE, 571, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(402, Short.MAX_VALUE))))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 946, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_id, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel77)
                    .addComponent(jLabel84)
                    .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(member_status, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel91)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel74))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(accession_no, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)
                            .addComponent(jButton3)
                            .addComponent(jLabel83))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(classificationNo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel75))))
                .addGap(33, 33, 33)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel79)
                    .addComponent(isbnNo, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel76)
                        .addGap(24, 24, 24)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel78)
                            .addComponent(dateshow)))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(bookname, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(transaction_startDate, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel80))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(due_date, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rSButtonPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel82))
                .addContainerGap())
        );

        jLayeredPane2.setLayer(jPanel15, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane2Layout = new javax.swing.GroupLayout(jLayeredPane2);
        jLayeredPane2.setLayout(jLayeredPane2Layout);
        jLayeredPane2Layout.setHorizontalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane2Layout.createSequentialGroup()
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jLayeredPane2Layout.setVerticalGroup(
            jLayeredPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Issue books", jLayeredPane2);

        jPanel2.add(jTabbedPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 1040, 580));

        jPanel1.setBackground(new java.awt.Color(51, 102, 0));

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

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Dunkannawa  Library >>");

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/User_Icons/username.png"))); // NOI18N
        jLabel16.setText("Welcome to User Account");

        jPanel8.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 62, Short.MAX_VALUE)
        );

        jLabel23.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_Return_Purchase_26px.png"))); // NOI18N
        jLabel23.setText("Issue Book");
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel23MouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel1.setText("Home >>");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(238, 238, 238)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(7, 7, 7)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel23)
                        .addGap(932, 932, 932)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel1)
                            .addComponent(jLabel23)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1152, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1158, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        Sachi.ui.Staff.Home_pg_1 homeback = new Sachi.ui.Staff.Home_pg_1();
        homeback.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel23MouseClicked

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
               
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rSButtonPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSButtonPane1MouseClicked

    }//GEN-LAST:event_rSButtonPane1MouseClicked

    private void jLabel86MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel86MouseClicked
        saveIssue();
    }//GEN-LAST:event_jLabel86MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String memberId = b_id.getText();
        if (memberId != null && !memberId.isEmpty()) {
            boolean hasExceededBookLimit = checkMemberBookLimit(memberId);
            if (hasExceededBookLimit) {
                member_status.setText("Member has exceeded book limit");
            } else {
                member_status.setText("Member is within book limit,enable to Issue");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid Membership ID");
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
String bookId = accession_no.getText();
        try {
            retrieveBookData(bookId);
        } catch (SQLException ex) {

            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving book data");
        }

        

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel82MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel82MouseClicked
  clearFields();
   accession_no.setText("");
  // TODO add your handling code here:
    }//GEN-LAST:event_jLabel82MouseClicked

    private void jLabel83MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel83MouseClicked
       clearFields();
    }//GEN-LAST:event_jLabel83MouseClicked

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
            java.util.logging.Logger.getLogger(Book_issue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Book_issue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Book_issue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Book_issue.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Book_issue().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField accession_no;
    private javax.swing.JTextField b_id;
    private javax.swing.JLabel bookname;
    private javax.swing.JLabel classificationNo;
    private javax.swing.JLabel dateshow;
    private javax.swing.JLabel due_date;
    private javax.swing.JLabel isbnNo;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel member_status;
    private rojeru_san.complementos.RSButtonPane rSButtonPane1;
    private javax.swing.JLabel transaction_startDate;
    // End of variables declaration//GEN-END:variables
}
