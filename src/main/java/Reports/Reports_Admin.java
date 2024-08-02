/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Reports;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1CFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/*
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import javax.swing.text.Document;
import org.apache.poi.ss.usermodel.Table;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.layout.element.Table;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;*/
/**
 *
 * @author USER
 */
public class Reports_Admin extends javax.swing.JFrame {

    
    public Reports_Admin() {
        initComponents();
    }
    
    
/*
public class PdfReportWithCalendar {
    public static void main(String[] args) {
        // Assuming fdateboxx and todatboxx are already initialized Calendar models
        Calendar fdateboxx = Calendar.getInstance(); // Example initialization
        Calendar todatboxx = Calendar.getInstance(); // Example initialization

        // Here you would set the desired dates for fdateboxx and todatboxx
        // For demonstration, we'll set them to specific values:
        fdateboxx.set(2024, Calendar.JULY, 1); // Example start date
        todatboxx.set(2024, Calendar.JULY, 10); // Example end date

        // Call the method to generate the PDF report
        generatePdfReport(fdateboxx, todatboxx);
    }

    private static void generatePdfReport(Calendar fromDateCalendar, Calendar toDateCalendar) {
        String dest = "output/report.pdf"; // Destination file path

        // Convert Calendar to Date
        Date startDate = fromDateCalendar.getTime();
        Date endDate = toDateCalendar.getTime();

        // Create a PDF writer
        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add title
            document.add(new Paragraph("Supplier Report"));
            document.add(new Paragraph("Date Range: " + startDate + " to " + endDate));
            document.add(new Paragraph("\n"));

            // Create a table for supplier details
            Table table = new Table(3); // Assuming 3 columns: ID, Name, Date
            table.addCell("Supplier ID");
            table.addCell("Supplier Name");
            table.addCell("Date");

            

            try (Connection connection = new Helper.DatabaseConnection().connection()) {
                String query = "SELECT id, name, date FROM supplier WHERE date BETWEEN ? AND ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
                preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    table.addCell(resultSet.getString("id"));
                    table.addCell(resultSet.getString("name"));
                    table.addCell(resultSet.getString("date"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();
            System.out.println("PDF report created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

   
   /* public void LoadReport() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    try (Connection connection = new Helper.DatabaseConnection().connection()) {
        // Assuming fdateboxx and todateboxx are instances of JDatePicker
        Calendar fromDateCalendar = (Calendar) fdateboxx.getModel().getValue();
        Calendar toDateCalendar = (Calendar) todatboxx.getModel().getValue();

        // Convert Calendar to Date objects
        Date fromDate = fromDateCalendar.getTime();
        Date toDate = toDateCalendar.getTime();

        String fsupdate = dateFormat.format(fromDate);
        String tosupdate = dateFormat.format(toDate);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("fromdate", fromDate); // Use Date object
        parameters.put("enddate", toDate); // Use Date object

        // Clear the JPanel
        jPanel1.removeAll();
        jPanel1.repaint();
        jPanel1.revalidate();

        // Load the JRXML file
        File reportFile = new File("C:\\src\\reports\\report2.jrxml");
        if (!reportFile.exists()) {
            throw new FileNotFoundException("Report file not found: " + reportFile.getAbsolutePath());
        }

        try (InputStream reportStream = new FileInputStream(reportFile)) {
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            String sql = "SELECT * FROM supplier";
            JRResultSetDataSource dataSource = new JRResultSetDataSource(connection.createStatement().executeQuery(sql));

            // Fill the report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export the report to PDF
            String pdfOutputPath = "E:\\Projects\\Imgt project1(libdbms)\\Interfaces\\Dtabase\\Library Management System\\Library Management System\\src\\main\\java\\Reports\\report2.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdfOutputPath);

            // Optionally, you can preview the report in a viewer
            JasperViewer.viewReport(jasperPrint, false);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error loading report: " + e.getMessage());
    }
}



   
   public void LoadDonorReport() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (Connection connection = new Helper.DatabaseConnection().connection()) {
            // Assuming jDatePicker7 and jDatePicker8 are instances of JDatePicker
            Calendar fromCalendar = (Calendar) jDatePicker7.getModel().getValue();
            Calendar toCalendar = (Calendar) jDatePicker8.getModel().getValue();

            // Convert Calendar to Date
            Date fromDate = fromCalendar.getTime();
            Date toDate = toCalendar.getTime();

            String fdordate = dateFormat.format(fromDate);
            String todordate = dateFormat.format(toDate);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("fdodate", fromDate); // Use Date object
            parameters.put("enddordate", toDate); // Use Date object

            // Clear the JPanel
            jPanel1.removeAll();
            jPanel1.repaint();
            jPanel1.revalidate();

            // Load and compile the report
            JasperDesign jdesign = JRXmlLoader.load("E:\\Projects\\Imgt project1(libdbms)\\Interfaces\\Dtabase\\Library Management System\\Library Management System\\src\\main\\java\\Reports\\supplierreport.jrxml");
            JasperReport jreport = JasperCompileManager.compileReport(jdesign);

            // Fill the report
            JasperPrint jprint = JasperFillManager.fillReport(jreport, parameters, connection);

            // Create a viewer for the report
            JRViewer viewer = new JRViewer(jprint);

            // Add the viewer to the JPanel
            jPanel1.setLayout(new BorderLayout());
            jPanel1.add(viewer, BorderLayout.CENTER);

            // Refresh the JPanel to display the report
            jPanel1.revalidate();
            jPanel1.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading report: " + e.getMessage());
        }
    }
*/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel9 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        rSButtonHover6 = new rojeru_san.complementos.RSButtonHover();
        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        rSButtonHover4 = new rojeru_san.complementos.RSButtonHover();
        jPanel8 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        rSButtonHover7 = new rojeru_san.complementos.RSButtonHover();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        rSButtonHover3 = new rojeru_san.complementos.RSButtonHover();
        jPanel6 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        rSButtonHover5 = new rojeru_san.complementos.RSButtonHover();
        jLabel1 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        rSButtonHover13 = new rojeru_san.complementos.RSButtonHover();
        jLabel33 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 51, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(0, 0, 0));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));
        jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText(" Fines Report");
        jLabel10.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(204, 255, 204)));

        rSButtonHover6.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonHover6.setText("Generate");
        rSButtonHover6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                .addComponent(rSButtonHover6, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(rSButtonHover6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 513, 80));

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));
        jPanel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText(" Donater");
        jLabel11.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(204, 255, 204)));

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rSButtonHover4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel11))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rSButtonHover4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, -1, -1));

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));
        jPanel8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText(" Books");
        jLabel15.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(204, 255, 204)));

        jComboBox3.setBackground(new java.awt.Color(0, 0, 0));
        jComboBox3.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jComboBox3.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Books Report", " " }));

        rSButtonHover7.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover7.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonHover7.setText("Generate");
        rSButtonHover7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(rSButtonHover7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSButtonHover7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 160, -1, 80));

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));
        jPanel10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Supplier ");
        jLabel16.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(204, 255, 204)));

        jComboBox4.setBackground(new java.awt.Color(0, 0, 0));
        jComboBox4.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jComboBox4.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Supplier details Report", " ", " " }));
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        rSButtonHover3.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonHover3.setText("Generate");
        rSButtonHover3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSButtonHover3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 40, 482, 80));

        jPanel6.setBackground(new java.awt.Color(0, 0, 0));
        jPanel6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 102, 0), 2, true));

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText(" Donater");
        jLabel12.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(204, 255, 204)));

        jComboBox6.setBackground(new java.awt.Color(0, 0, 0));
        jComboBox6.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jComboBox6.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Donated Book Report", " ", " ", " " }));
        jComboBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox6ActionPerformed(evt);
            }
        });

        rSButtonHover5.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonHover5.setText("Generate");
        rSButtonHover5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rSButtonHover5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel12))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rSButtonHover5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jPanel9.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 513, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/neww icons/Free_Vector___Documents_concept_illustration-removebg-preview.png"))); // NOI18N
        jPanel9.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 260, 320, 180));

        getContentPane().add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 98, 1190, 455));

        jPanel18.setBackground(new java.awt.Color(51, 102, 0));
        jPanel18.setForeground(new java.awt.Color(0, 0, 0));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel27.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8_Account_50px.png"))); // NOI18N
        jLabel27.setText("Welcome to Librarian's Account");
        jPanel18.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(815, 12, 371, -1));
        jPanel18.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(585, 12, 9, 33));

        jLabel29.setFont(new java.awt.Font("Dialog", 0, 20)); // NOI18N
        jLabel29.setText("Library Management system >>");
        jPanel18.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 31, -1, -1));

        jLabel30.setFont(new java.awt.Font("Jokerman", 2, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 51, 0));
        jLabel30.setText("League Developers");
        jPanel18.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 124, 150, 40));

        jLabel31.setFont(new java.awt.Font("Jokerman", 2, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 51, 0));
        jLabel31.setText("League Developers");
        jPanel18.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 124, 150, 40));

        rSButtonHover13.setBackground(new java.awt.Color(51, 102, 0));
        rSButtonHover13.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rSButtonHover13.setText("Print");
        rSButtonHover13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover13ActionPerformed(evt);
            }
        });
        jPanel18.add(rSButtonHover13, new org.netbeans.lib.awtextra.AbsoluteConstraints(903, 248, 0, -1));

        jLabel33.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/home_24px.png"))); // NOI18N
        jLabel33.setText("Home >>");
        jLabel33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel33MouseClicked(evt);
            }
        });
        jPanel18.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(324, 29, 104, 33));

        jLabel26.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jLabel26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/adminIcons/icons8_View_Details_26px.png"))); // NOI18N
        jLabel26.setText("Reports ");
        jLabel26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel26MouseClicked(evt);
            }
        });
        jPanel18.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(434, 32, 145, -1));

        getContentPane().add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1190, 92));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rSButtonHover4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover4ActionPerformed
//C:\Users\USER\Documents\Reports        // TODO add your handling code here:
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

    private void rSButtonHover6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover6ActionPerformed
//Date fromDate = jDateChooser2.getDate();
//Date toDate = jDateChooser1.getDate();
//
//// Create a HashMap and put the date objects
//HashMap<String, Object> parameters = new HashMap<>();
//parameters.put("fromd", fromDate);
//parameters.put("tod", toDate);


        
        try(Connection connection = new Helper.DatabaseConnection().connection()){
              String reportpath= "C:\\Users\\USER\\Documents\\Reports\\fines.jrxml";
              JasperReport jr = JasperCompileManager.compileReport(reportpath);
              JasperPrint jp =JasperFillManager.fillReport(jr, null,connection);
              JasperViewer.viewReport(jp);
              connection.close();
          }catch(Exception e){
          JOptionPane.showMessageDialog(rootPane, e);
          }             // TODO add your handling code here:
    }//GEN-LAST:event_rSButtonHover6ActionPerformed

    private void rSButtonHover7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover7ActionPerformed
try(Connection connection = new Helper.DatabaseConnection().connection()){
              String reportpath= "C:\\Users\\USER\\Documents\\Reports\\book.jrxml";
              JasperReport jr = JasperCompileManager.compileReport(reportpath);
              JasperPrint jp =JasperFillManager.fillReport(jr, null,connection);
              JasperViewer.viewReport(jp);
              connection.close();
          }catch(Exception e){
          JOptionPane.showMessageDialog(rootPane, e);
          }             // TODO add your handling code here:
    }//GEN-LAST:event_rSButtonHover7ActionPerformed

    private void jLabel26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel26MouseClicked
        Sachi_Ui_Admin.Librarian_home_pg homeback= new Sachi_Ui_Admin.Librarian_home_pg();
        
        homeback.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel26MouseClicked

    private void rSButtonHover13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rSButtonHover13ActionPerformed

    private void jLabel33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseClicked
Sachi_Ui_Admin.Librarian_home_pg homeback= new Sachi_Ui_Admin.Librarian_home_pg();
        
        homeback.show();
        dispose();          // TODO add your handling code here:
    }//GEN-LAST:event_jLabel33MouseClicked

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jComboBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox6ActionPerformed

    private void rSButtonHover5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover5ActionPerformed
try(Connection connection = new Helper.DatabaseConnection().connection()){
              String reportpath= "C:\\Users\\USER\\Documents\\Reports\\donatedbooks.jrxml";
              JasperReport jr = JasperCompileManager.compileReport(reportpath);
              JasperPrint jp =JasperFillManager.fillReport(jr, null,connection);
              JasperViewer.viewReport(jp);
              connection.close();
          }catch(Exception e){
          JOptionPane.showMessageDialog(rootPane, e);
          }          // TODO add your handling code here:
    }//GEN-LAST:event_rSButtonHover5ActionPerformed

    private void rSButtonHover3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover3ActionPerformed
        //List<String[]> data = Reports_Admin.fetchData();
        // Generate the PDF
        // Reports_Admin.generatePDF(data, "report.pdf");
        // Notify the user
        // JOptionPane.showMessageDialog(null, "PDF Report Generated!");//LoadReport();
        try(Connection connection = new Helper.DatabaseConnection().connection()){
            String reportpath= "C:\\Users\\USER\\Documents\\Reports\\supplierdetails1.jrxml";
            JasperReport jr = JasperCompileManager.compileReport(reportpath);
            JasperPrint jp =JasperFillManager.fillReport(jr, null,connection);
            JasperViewer.viewReport(jp);
            connection.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_rSButtonHover3ActionPerformed

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox4ActionPerformed

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
            java.util.logging.Logger.getLogger(Reports_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reports_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reports_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reports_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new Reports_Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private rojeru_san.complementos.RSButtonHover rSButtonHover13;
    private rojeru_san.complementos.RSButtonHover rSButtonHover3;
    private rojeru_san.complementos.RSButtonHover rSButtonHover4;
    private rojeru_san.complementos.RSButtonHover rSButtonHover5;
    private rojeru_san.complementos.RSButtonHover rSButtonHover6;
    private rojeru_san.complementos.RSButtonHover rSButtonHover7;
    // End of variables declaration//GEN-END:variables
}
