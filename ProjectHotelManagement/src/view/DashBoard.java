/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.converter.LocalDateTimeStringConverter;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import util.DbCon;

/**
 *
 * @author Shohab Sikder
 */
public class DashBoard extends javax.swing.JFrame {

    DbCon con = new DbCon();
    PreparedStatement ps;
    String sql;
    ResultSet rs;

    public DashBoard() {
        initComponents();
        getAllManageRoomInfo();
        dt();
        times();
        setLocationRelativeTo(null);
//        todayRevenue();
        getRoomBooked();
        getRoomUnbooked();
        todayRevenue();

    }

    public void dt() {

        java.util.Date dt = new java.util.Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");

        String dd = sdf.format(dt);
        localDate.setText(dd);

    }

// time
    Timer t;
    SimpleDateFormat st;

    public void times() {

        t = new Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

                java.util.Date dt = new java.util.Date();
                st = new SimpleDateFormat("hh:mm:ss: a");

                String tt = st.format(dt);
                localTime.setText(tt);

            }
        });

        t.start();

    }

//private int getBookedRoomCount() {
//        int count = 0;
//
//       
// 
//        try {
//            
//            String query = "SELECT COUNT(*) FROM manageroom WHERE status = 'Booked'";
//            ResultSet resultSet1 = ps.executeQuery(query);
//
//            if (resultSet1.next()) {
//                count = resultSet1.getInt(1);
//            }
//
//            resultSet1.close();
//            ps.close();
//            con.getCon().close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return count;
//    }
    
    
    LocalDate currentDate = LocalDate.now();
    java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);

    public void todayRevenue() {

        sql = "select sum(actualamount) from checkout ";
        try {
            ps = con.getCon().prepareStatement(sql);
            

            rs = ps.executeQuery();

            while (rs.next()) {
                Float totalPrice = rs.getFloat("sum(actualamount)");
                System.out.println(totalPrice);
                lblTotalRevenue.setText(totalPrice + "");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int getRoomBooked() {
    sql = "SELECT COUNT(RoomNumber) FROM manageroom WHERE status = 'Booked'";
    
    try {
        ps = con.getCon().prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            int bookedRoomCount = rs.getInt(1);
            lblBookedRoom.setText(String.valueOf(bookedRoomCount));
        }
        rs.close();
    } catch (SQLException ex) {
        Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return 0; // You might want to return the booked room count instead of 0.
}

    
    
     public int getRoomUnbooked() {
    sql = "SELECT COUNT(RoomNumber) FROM manageroom WHERE status = 'Unbooked'";
    
    try {
        ps = con.getCon().prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            int bookedRoomCount = rs.getInt(1);
            lblUnbookedRoom.setText(String.valueOf(bookedRoomCount));
        }
        rs.close();
    } catch (SQLException ex) {
        Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return 0; // You might want to return the booked room count instead of 0.
}
     
     
    public void checkInReset() {

        txtName.setText(null);
        txtMobile.setText(null);
        txtEmail.setText(null);
        txtNation.setText(null);
        comboGender.setSelectedIndex(0);
        txtAdress.setText(null);
        txtDate.setDate(null);
        comboRoomNumber.setSelectedIndex(0);
        comboRoomType.setSelectedIndex(0);
        comboBed.setSelectedIndex(0);
        txtPrice.setText(null);

    }

    //Check out Method//
    public void getAllInfoToCheckOut() {
        sql = "select name,mobile,email,price,checkin from checkin where roomnumber=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtCustomerRoom.getText());

            rs = ps.executeQuery();
            while (rs.next()) {
                txtCustomerName.setText(rs.getString("name"));
                txtCustomerCell.setText(rs.getString("mobile"));
                txtCustomerEmail.setText(rs.getString("email"));
                txtCustomerPrice.setText(rs.getString("price"));
                txtCustomerCheckIn.setDate(rs.getDate("checkin"));
            }

            ps.executeQuery();
            ps.close();
            con.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Get Room method//
    public void getRoomNumber(String roomType, String bedType) {
        comboRoomNumber.removeAllItems();

        sql = "select RoomNumber, Price from manageroom where RoomType=? and Bed=? and status=? ";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, roomType);
            ps.setString(2, bedType);
            ps.setString(3, "Unbooked");

            ResultSet rs1 = ps.executeQuery();

            while (rs1.next()) {

                int roomNumber = rs1.getInt("RoomNumber");
                comboRoomNumber.addItem(roomNumber + "");
                txtManageRoomPrice.setText(rs1.getString("Price"));
            }

            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getCheckInPrice() {

        sql = "select Price from manageroom where RoomNumber=?";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setString(1, comboRoomNumber.getItemAt(comboRoomNumber.getSelectedIndex()));
            rs = ps.executeQuery();

            while (rs.next()) {
                txtPrice.setText(rs.getString("Price"));
            }

            ps.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public java.sql.Date convertUtilDateToSqlDate(java.util.Date utilDate) {
        if (utilDate != null) {
            return new java.sql.Date(utilDate.getTime());
        }

        return null;

    }

//    
    public void getAllManageRoomInfo() {

        String[] columnName = {"Room Number", "Room Type", "Bed", "Price", "Status"};

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnName);

        tblManageRoom.setModel(model);

        sql = "select * from manageroom";

        try {
            ps = con.getCon().prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                int roomNumber = rs.getInt("RoomNumber");
                String roomType = rs.getString("RoomType");
                String bed = rs.getString("Bed");
                String price = rs.getString("Price");
                String status = rs.getString("status");

                model.addRow(new Object[]{roomNumber, roomType, bed, price, status});

            }
            rs.close();
            ps.close();
            con.getCon().close();
        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateManageRoom() {
        sql = "update manageroom set RoomType=?,Bed=?,Price=? where RoomNumber=?";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, comboManageRoomType.getSelectedItem().toString());
            ps.setString(2, comboManageRoomBed.getSelectedItem().toString());
            ps.setString(3, txtManageRoomPrice.getText().trim());
            ps.setFloat(4, Float.parseFloat(txtManageRoomNum.getText().trim()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();
            getAllManageRoomInfo();

            JOptionPane.showMessageDialog(rootPane, "Room Updated");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Room not Updated");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void reset() {
        txtManageRoomNum.setText(null);
        txtManageRoomPrice.setText(null);
        comboManageRoomBed.setSelectedItem(null);
        comboManageRoomType.setSelectedItem(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        localDate = new javax.swing.JLabel();
        localTime = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        Dashboard = new javax.swing.JButton();
        btnManageRoom = new javax.swing.JButton();
        btnCheckIn = new javax.swing.JButton();
        btnCheckOut = new javax.swing.JButton();
        btnCustomerDetails = new javax.swing.JButton();
        btnCustomerBill = new javax.swing.JButton();
        btnLogOut = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        menu = new javax.swing.JTabbedPane();
        DashBoard = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        lblUnbookedRoom = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        lblBookedRoom = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        lblTotalRevenue = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        ManageRoom = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtManageRoomNum = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtManageRoomPrice = new javax.swing.JTextField();
        comboManageRoomType = new javax.swing.JComboBox<>();
        comboManageRoomBed = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblManageRoom = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        btnManageAdd = new javax.swing.JButton();
        btnManageUpdate = new javax.swing.JButton();
        btnManageReset = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        CheckIn = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtMobile = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtNation = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtPrice = new javax.swing.JTextField();
        comboGender = new javax.swing.JComboBox<>();
        comboRoomType = new javax.swing.JComboBox<>();
        comboBed = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAdress = new javax.swing.JTextArea();
        btnAllotRoom = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        comboRoomNumber = new javax.swing.JComboBox<>();
        txtDate = new com.toedter.calendar.JDateChooser();
        jLabel20 = new javax.swing.JLabel();
        CheckOut = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txtCustomerName = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtCustomerEmail = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtCustomerCell = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtCustomerPrice = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtCustomerTotalDays = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtCustomerTotalAmount = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        btnCustomerCheckOut = new javax.swing.JButton();
        btnCustomerClear = new javax.swing.JButton();
        jLabel30 = new javax.swing.JLabel();
        txtCustomerRoom = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel57 = new javax.swing.JLabel();
        txtCustomerTax = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        txtCustomerActualAmount = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        txtCustomerReceivedCash = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        txtCustomerReturn = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        txtCustomerInvoice = new javax.swing.JTextField();
        txtCustomerCheckIn = new com.toedter.calendar.JDateChooser();
        txtCustomerCheckOut = new com.toedter.calendar.JDateChooser();
        jLabel31 = new javax.swing.JLabel();
        CustomerDetails = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        radioCheckInDetails = new javax.swing.JRadioButton();
        radioCheckOutDetails = new javax.swing.JRadioButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblCustomerDetails = new javax.swing.JTable();
        viewCustomerDetails = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        dateFrom = new com.toedter.calendar.JDateChooser();
        dateFromTo = new com.toedter.calendar.JDateChooser();
        jLabel37 = new javax.swing.JLabel();
        CustomerBill = new javax.swing.JTabbedPane();
        printDetails = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        billName = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        billMobile = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        billRoomNo = new javax.swing.JTextField();
        billCheckin = new javax.swing.JTextField();
        billCheckOut = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        billTotalAmount = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        billTax = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        billActualAmount = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        billCashReceived = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        billReturn = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        billInvoice = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(0, 153, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel61.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Date");
        jPanel2.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 30, -1, -1));

        jLabel62.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("Time");
        jPanel2.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 60, -1, -1));

        localDate.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        localDate.setForeground(new java.awt.Color(255, 255, 255));
        localDate.setText("0");
        jPanel2.add(localDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 30, 160, 20));

        localTime.setFont(new java.awt.Font("Sylfaen", 1, 14)); // NOI18N
        localTime.setForeground(new java.awt.Color(255, 255, 255));
        localTime.setText("0");
        jPanel2.add(localTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(1070, 60, 160, 20));

        jLabel1.setFont(new java.awt.Font("Sylfaen", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("HOTEL PALACE");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 110));

        jLabel63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Asset/check in icon/rsz_1hp_1-removebg-preview.png"))); // NOI18N
        jLabel63.setText("jLabel63");
        jPanel2.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 110));

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 110));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(255, 102, 0));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Dashboard.setText("DashBoard");
        Dashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardActionPerformed(evt);
            }
        });
        jPanel5.add(Dashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 120, 30));

        btnManageRoom.setText("Manage Room");
        btnManageRoom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManageRoomActionPerformed(evt);
            }
        });
        jPanel5.add(btnManageRoom, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 120, 30));

        btnCheckIn.setText("Check In");
        btnCheckIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckInActionPerformed(evt);
            }
        });
        jPanel5.add(btnCheckIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 120, 30));

        btnCheckOut.setText("Check Out");
        btnCheckOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckOutActionPerformed(evt);
            }
        });
        jPanel5.add(btnCheckOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 230, 120, 30));

        btnCustomerDetails.setText("Customer Details");
        btnCustomerDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerDetailsActionPerformed(evt);
            }
        });
        jPanel5.add(btnCustomerDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 303, 120, 30));

        btnCustomerBill.setText("Customer Bill");
        btnCustomerBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomerBillMouseClicked(evt);
            }
        });
        btnCustomerBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerBillActionPerformed(evt);
            }
        });
        jPanel5.add(btnCustomerBill, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 373, 120, 30));

        btnLogOut.setText("Log Out");
        btnLogOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogOutMouseClicked(evt);
            }
        });
        jPanel5.add(btnLogOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 513, 120, 30));

        btnPrint.setText("Print");
        btnPrint.setHideActionText(true);
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrintMouseClicked(evt);
            }
        });
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jPanel5.add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 443, 120, 30));

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 570));

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(17, 165, 230));

        jLabel44.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("Unbooked Room");

        lblUnbookedRoom.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        lblUnbookedRoom.setForeground(new java.awt.Color(255, 255, 255));
        lblUnbookedRoom.setText("0.0");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(lblUnbookedRoom)
                .addContainerGap(130, Short.MAX_VALUE))
            .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel44)
                .addGap(18, 18, 18)
                .addComponent(lblUnbookedRoom)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, 270, 120));

        jPanel16.setBackground(new java.awt.Color(17, 165, 230));

        jLabel34.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Booked Room");

        lblBookedRoom.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        lblBookedRoom.setForeground(new java.awt.Color(255, 255, 255));
        lblBookedRoom.setText("0.0");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(lblBookedRoom)
                .addContainerGap(130, Short.MAX_VALUE))
            .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addGap(18, 18, 18)
                .addComponent(lblBookedRoom)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 100, 270, 120));

        jPanel17.setBackground(new java.awt.Color(17, 165, 230));

        jLabel32.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Total Revenue");

        lblTotalRevenue.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        lblTotalRevenue.setForeground(new java.awt.Color(255, 255, 255));
        lblTotalRevenue.setText("0.0");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(lblTotalRevenue)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addGap(18, 18, 18)
                .addComponent(lblTotalRevenue)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 300, 270, 120));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Asset/tabPhoto/DashBoard.jpg"))); // NOI18N
        jLabel33.setText("jLabel33");
        jPanel12.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1070, 560));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 1073, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 67, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        DashBoard.addTab("tab1", jPanel11);

        menu.addTab("DashBoard", DashBoard);

        jPanel6.setBackground(new java.awt.Color(240, 141, 155));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Room Number");
        jPanel6.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 120, -1, -1));
        jPanel6.add(txtManageRoomNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, 239, 32));

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Bed");
        jPanel6.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120, -1, -1));

        jLabel4.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Room Type");
        jPanel6.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 250, -1, -1));

        jLabel5.setFont(new java.awt.Font("Berlin Sans FB Demi", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Price Per Day");
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 250, -1, -1));
        jPanel6.add(txtManageRoomPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 300, 235, 36));

        comboManageRoomType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AC", "Non-AC" }));
        jPanel6.add(comboManageRoomType, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 300, 239, 36));

        comboManageRoomBed.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Single", "Double" }));
        jPanel6.add(comboManageRoomBed, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 170, 235, 32));

        tblManageRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblManageRoom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblManageRoomMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblManageRoom);

        jPanel6.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 370, 610, 180));

        jLabel6.setFont(new java.awt.Font("Bell MT", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Manage Room");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, -1, -1));

        btnManageAdd.setText("Add Room");
        btnManageAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnManageAddMouseClicked(evt);
            }
        });
        jPanel6.add(btnManageAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 520, 87, -1));

        btnManageUpdate.setText("Update");
        btnManageUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnManageUpdateMouseClicked(evt);
            }
        });
        jPanel6.add(btnManageUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 520, -1, -1));

        btnManageReset.setText("Reset");
        btnManageReset.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnManageResetMouseClicked(evt);
            }
        });
        jPanel6.add(btnManageReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 520, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Asset/tabPhoto/radisson-blu-dhaka-water-garden-2-radisson-blu-dhaka-water-garden01jpg-3e5035fe-c989-4d92-92d1-740a1e5105d81673535713.jpg"))); // NOI18N
        jLabel7.setText("jLabel7");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 1280, 700));

        ManageRoom.addTab("tab1", jPanel6);

        menu.addTab("tab1", ManageRoom);

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Name");
        jPanel9.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, -1, -1));
        jPanel9.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 210, -1));

        jLabel9.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Mobile Number");
        jPanel9.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, -1, -1));
        jPanel9.add(txtMobile, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 140, 210, -1));

        jLabel10.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Email");
        jPanel9.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 200, -1, -1));
        jPanel9.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 200, 210, -1));

        jLabel11.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Gender");
        jPanel9.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 260, -1, -1));

        jLabel12.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Nationality");
        jPanel9.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 330, -1, -1));
        jPanel9.add(txtNation, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 330, 210, -1));

        jLabel13.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Check In Date");
        jPanel9.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 80, -1, -1));

        jLabel14.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Room Type");
        jPanel9.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 140, -1, -1));

        jLabel15.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(204, 255, 255));
        jLabel15.setText("Bed");
        jPanel9.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 200, -1, -1));

        jLabel16.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Room Number");
        jPanel9.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 260, -1, -1));

        jLabel17.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Price");
        jPanel9.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 330, -1, -1));
        jPanel9.add(txtPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 330, 210, -1));

        comboGender.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---Select---", "Male", "Female" }));
        jPanel9.add(comboGender, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, 210, -1));

        comboRoomType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Select--", "AC", "Non-AC" }));
        comboRoomType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboRoomTypeItemStateChanged(evt);
            }
        });
        jPanel9.add(comboRoomType, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 140, 210, -1));

        comboBed.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Select--", "Single", "Double" }));
        comboBed.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboBedItemStateChanged(evt);
            }
        });
        jPanel9.add(comboBed, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 200, 210, -1));

        jLabel18.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Adress");
        jPanel9.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 400, -1, -1));

        txtAdress.setColumns(20);
        txtAdress.setRows(5);
        jScrollPane2.setViewportView(txtAdress);

        jPanel9.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 380, 210, 50));

        btnAllotRoom.setText("Allote Room");
        btnAllotRoom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAllotRoomMouseClicked(evt);
            }
        });
        jPanel9.add(btnAllotRoom, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 510, -1, -1));

        btnClear.setText("Clear");
        btnClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearMouseClicked(evt);
            }
        });
        jPanel9.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 510, -1, -1));

        jLabel19.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("CHECK IN");
        jPanel9.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, -1, -1));

        comboRoomNumber.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboRoomNumberItemStateChanged(evt);
            }
        });
        comboRoomNumber.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                comboRoomNumberMouseClicked(evt);
            }
        });
        jPanel9.add(comboRoomNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 260, 210, -1));
        jPanel9.add(txtDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 80, 210, -1));

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Asset/tabPhoto/CheckIN (2).jpg"))); // NOI18N
        jLabel20.setText("jLabel20");
        jPanel9.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1140, 620));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
        );

        CheckIn.addTab("tab1", jPanel7);

        menu.addTab("tab2", CheckIn);

        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("CHECK OUT");
        jPanel10.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 40, -1, -1));

        jLabel22.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Customer Name ");
        jPanel10.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, -1, -1));

        txtCustomerName.setEditable(false);
        jPanel10.add(txtCustomerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 200, 180, -1));

        jLabel23.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Customer Email");
        jPanel10.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, -1, -1));

        txtCustomerEmail.setEditable(false);
        jPanel10.add(txtCustomerEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 250, 180, -1));

        jLabel24.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Customer Cell");
        jPanel10.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, -1, -1));

        txtCustomerCell.setEditable(false);
        jPanel10.add(txtCustomerCell, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 300, 180, -1));

        jLabel25.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Price Per Day");
        jPanel10.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, -1, -1));

        txtCustomerPrice.setEditable(false);
        jPanel10.add(txtCustomerPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 350, 180, -1));

        jLabel26.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Total Days");
        jPanel10.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 200, -1, -1));

        txtCustomerTotalDays.setEditable(false);
        txtCustomerTotalDays.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustomerTotalDaysMouseClicked(evt);
            }
        });
        jPanel10.add(txtCustomerTotalDays, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 200, 190, -1));

        jLabel27.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Total Amount");
        jPanel10.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 250, -1, -1));

        txtCustomerTotalAmount.setEditable(false);
        txtCustomerTotalAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustomerTotalAmountMouseClicked(evt);
            }
        });
        jPanel10.add(txtCustomerTotalAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 250, 190, -1));

        jLabel28.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Check In ");
        jPanel10.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 410, -1, -1));

        jLabel29.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Check Out (Today)");
        jPanel10.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 460, -1, -1));

        btnCustomerCheckOut.setText("Check Out");
        btnCustomerCheckOut.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomerCheckOutMouseClicked(evt);
            }
        });
        jPanel10.add(btnCustomerCheckOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 540, -1, -1));

        btnCustomerClear.setText("Clear");
        btnCustomerClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCustomerClearMouseClicked(evt);
            }
        });
        jPanel10.add(btnCustomerClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 540, -1, -1));

        jLabel30.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Room Number");
        jPanel10.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, -1, -1));

        txtCustomerRoom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerRoomFocusLost(evt);
            }
        });
        jPanel10.add(txtCustomerRoom, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, 130, -1));

        btnSearch.setText("Search");
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSearchMouseClicked(evt);
            }
        });
        jPanel10.add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 110, -1, -1));

        jLabel57.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Tax%");
        jPanel10.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 300, -1, -1));

        txtCustomerTax.setEditable(false);
        txtCustomerTax.setText("40");
        jPanel10.add(txtCustomerTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 300, 190, -1));

        jLabel58.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Actual Amount");
        jPanel10.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 350, -1, -1));

        txtCustomerActualAmount.setEditable(false);
        txtCustomerActualAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustomerActualAmountMouseClicked(evt);
            }
        });
        jPanel10.add(txtCustomerActualAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 350, 190, -1));

        jLabel59.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("Received Cash");
        jPanel10.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 410, -1, -1));
        jPanel10.add(txtCustomerReceivedCash, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 410, 190, -1));

        jLabel60.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Return");
        jPanel10.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 460, -1, -1));

        txtCustomerReturn.setEditable(false);
        txtCustomerReturn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCustomerReturnMouseClicked(evt);
            }
        });
        jPanel10.add(txtCustomerReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 460, 190, -1));

        jLabel35.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Invoice No");
        jPanel10.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 110, -1, -1));

        txtCustomerInvoice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerInvoiceFocusLost(evt);
            }
        });
        jPanel10.add(txtCustomerInvoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 110, 130, -1));
        jPanel10.add(txtCustomerCheckIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 410, 180, -1));
        jPanel10.add(txtCustomerCheckOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 460, 180, -1));

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Asset/tabPhoto/checkOut.jpg"))); // NOI18N
        jLabel31.setText("jLabel31");
        jPanel10.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -10, 1140, 650));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        CheckOut.addTab("tab1", jPanel8);

        menu.addTab("tab3", CheckOut);

        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Customer Details");
        jPanel14.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, -1, -1));

        buttonGroup1.add(radioCheckInDetails);
        radioCheckInDetails.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        radioCheckInDetails.setForeground(new java.awt.Color(255, 255, 255));
        radioCheckInDetails.setText("Check In");
        jPanel14.add(radioCheckInDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 180, -1, -1));

        buttonGroup1.add(radioCheckOutDetails);
        radioCheckOutDetails.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        radioCheckOutDetails.setForeground(new java.awt.Color(255, 255, 255));
        radioCheckOutDetails.setText("Check out");
        jPanel14.add(radioCheckOutDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 180, -1, -1));

        tblCustomerDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tblCustomerDetails);

        jPanel14.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 1080, 230));

        viewCustomerDetails.setText("View");
        viewCustomerDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewCustomerDetailsMouseClicked(evt);
            }
        });
        jPanel14.add(viewCustomerDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 180, 100, 30));

        jLabel41.setFont(new java.awt.Font("Sylfaen", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("From");
        jPanel14.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 100, -1, -1));

        jLabel42.setFont(new java.awt.Font("Sylfaen", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("To");
        jPanel14.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 100, -1, -1));
        jPanel14.add(dateFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 100, 120, -1));
        jPanel14.add(dateFromTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 100, 120, -1));

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Asset/tabPhoto/Customer Details.jpg"))); // NOI18N
        jLabel37.setText("jLabel37");
        jPanel14.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1080, 620));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        CustomerDetails.addTab("tab1", jPanel13);

        menu.addTab("tab5", CustomerDetails);

        CustomerBill.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CustomerBillMouseClicked(evt);
            }
        });

        printDetails.setBackground(new java.awt.Color(255, 255, 255));
        printDetails.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Asset/check in icon/rsz_1hp-removebg-preview.png"))); // NOI18N
        jLabel38.setText("Logo");
        printDetails.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 180, 110));

        jTextField1.setFont(new java.awt.Font("Sylfaen", 0, 18)); // NOI18N
        jTextField1.setText("Hotel Palace Corp Limited, Dhaka-1216");
        jTextField1.setBorder(null);
        printDetails.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 370, -1));

        jLabel39.setBackground(new java.awt.Color(255, 255, 255));
        jLabel39.setFont(new java.awt.Font("Blackadder ITC", 1, 36)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(236, 51, 79));
        jLabel39.setText("Receipt");
        printDetails.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 40, 130, 60));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setText("Customer Details");
        printDetails.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, -1, -1));

        jLabel43.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel43.setText("Name");
        printDetails.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, -1, -1));

        billName.setBorder(null);
        printDetails.add(billName, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 290, 130, -1));

        jLabel45.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel45.setText("Mobile ");
        printDetails.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 290, -1, -1));

        billMobile.setBorder(null);
        printDetails.add(billMobile, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 290, 130, -1));

        jLabel46.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel46.setText("Room No");
        printDetails.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, -1, 20));

        jLabel47.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel47.setText("CheckIn");
        printDetails.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, -1, 20));

        jLabel48.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel48.setText("CheckOut");
        printDetails.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 360, -1, 20));

        billRoomNo.setBorder(null);
        printDetails.add(billRoomNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 130, 20));

        billCheckin.setBorder(null);
        printDetails.add(billCheckin, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 350, 130, 20));

        billCheckOut.setBorder(null);
        printDetails.add(billCheckOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 360, 130, 20));

        jLabel49.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel49.setText("Total Amount");
        printDetails.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 290, -1, -1));

        billTotalAmount.setBorder(null);
        printDetails.add(billTotalAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 290, 130, -1));

        jLabel50.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel50.setText("Tax%");
        printDetails.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, 64, -1));

        billTax.setBorder(null);
        printDetails.add(billTax, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 430, 130, -1));

        jLabel51.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel51.setText("Actual Amount");
        printDetails.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 430, -1, -1));

        billActualAmount.setBorder(null);
        printDetails.add(billActualAmount, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 430, 130, -1));

        jLabel52.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel52.setText("Cash Received");
        printDetails.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 360, -1, -1));

        billCashReceived.setBorder(null);
        printDetails.add(billCashReceived, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 360, 130, -1));

        jLabel53.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel53.setText("Return");
        printDetails.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 430, -1, -1));

        billReturn.setBorder(null);
        printDetails.add(billReturn, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 430, 130, -1));

        jLabel54.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel54.setText("Invoice No");
        printDetails.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 210, -1, 20));

        billInvoice.setBorder(null);
        printDetails.add(billInvoice, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 210, 130, 20));

        jLabel55.setFont(new java.awt.Font("Sylfaen", 1, 16)); // NOI18N
        jLabel55.setText("Customer Signature");
        printDetails.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, -1, -1));

        jLabel56.setFont(new java.awt.Font("Sylfaen", 1, 18)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("\"Thank You For Visiting Our Hotel,Hope So You Will Come Again\"");
        printDetails.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 530, 1060, -1));

        CustomerBill.addTab("tab1", printDetails);

        menu.addTab("tab6", CustomerBill);

        jPanel1.add(menu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -70, 1150, 680));

        jPanel4.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, 1080, 570));

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 1280, 570));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCheckInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckInActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(2);
    }//GEN-LAST:event_btnCheckInActionPerformed

    private void btnManageRoomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManageRoomActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(1);
        getAllManageRoomInfo();
        getRoomBooked();
        getRoomUnbooked();
    }//GEN-LAST:event_btnManageRoomActionPerformed

    private void btnCheckOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckOutActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(3);
    }//GEN-LAST:event_btnCheckOutActionPerformed

    private void btnCustomerDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerDetailsActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(4);
    }//GEN-LAST:event_btnCustomerDetailsActionPerformed

    private void btnLogOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogOutMouseClicked
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(rootPane, "Are you sure?");
        dispose();
    }//GEN-LAST:event_btnLogOutMouseClicked

    private void btnManageAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnManageAddMouseClicked
        // TODO add your handling code here:
        sql = "insert into manageroom(RoomNumber,RoomType,Bed,Price,status)values(?,?,?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, txtManageRoomNum.getText().trim());
            ps.setString(2, comboManageRoomType.getSelectedItem().toString());
            ps.setString(3, comboManageRoomBed.getSelectedItem().toString());
            ps.setFloat(4, Float.parseFloat(txtManageRoomPrice.getText().trim()));
            ps.setString(5, "Unbooked");

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Room Saved");
            reset();
            getAllManageRoomInfo();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Room  not Saved");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnManageAddMouseClicked

    private void btnManageUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnManageUpdateMouseClicked
        // TODO add your handling code here:
        updateManageRoom();

    }//GEN-LAST:event_btnManageUpdateMouseClicked

    private void btnManageResetMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnManageResetMouseClicked
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_btnManageResetMouseClicked

    private void tblManageRoomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblManageRoomMouseClicked
        // TODO add your handling code here:
        int row = tblManageRoom.getSelectedRow();

        String roomNumber = tblManageRoom.getModel().getValueAt(row, 0).toString();
        String roomType = tblManageRoom.getModel().getValueAt(row, 1).toString();
        String bed = tblManageRoom.getModel().getValueAt(row, 2).toString();
        String price = tblManageRoom.getModel().getValueAt(row, 3).toString();

        txtManageRoomNum.setText(roomNumber);
        comboManageRoomType.setSelectedItem(roomType);
        comboManageRoomBed.setSelectedItem(bed);
        txtManageRoomPrice.setText(price);
    }//GEN-LAST:event_tblManageRoomMouseClicked

    private void btnAllotRoomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAllotRoomMouseClicked
        // TODO add your handling code here:
        String sql1 = "update manageroom set status=? where RoomNumber=?";
        PreparedStatement ps1;

        sql = "insert into checkin (roomnumber,name,mobile,email,gender,nationality,adress,checkin,roomtype,bed,price)values(?,?,?,?,?,?,?,?,?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);

            ps.setString(1, comboRoomNumber.getSelectedItem().toString());
            ps.setString(2, txtName.getText().trim());
            ps.setString(3, txtMobile.getText().trim());
            ps.setString(4, txtEmail.getText().trim());
            ps.setString(5, comboGender.getSelectedItem().toString());
            ps.setString(6, txtNation.getText().trim());
            ps.setString(7, txtAdress.getText().trim());
            ps.setDate(8, convertUtilDateToSqlDate(txtDate.getDate()));
            System.out.println(convertUtilDateToSqlDate(txtDate.getDate()));
            ps.setString(9, comboRoomType.getSelectedItem().toString());
            ps.setString(10, comboBed.getSelectedItem().toString());
            ps.setFloat(11, Float.parseFloat(txtPrice.getText().trim()));

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            ps1 = con.getCon().prepareStatement(sql1);

            ps1.setString(1, "Booked");
            ps1.setInt(2, Integer.parseInt(comboRoomNumber.getSelectedItem().toString()));

            ps1.executeUpdate();

            ps1.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Alloted Room");

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnAllotRoomMouseClicked

    private void btnCustomerCheckOutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerCheckOutMouseClicked
        // TODO add your handling code here:

        String sql1 = "update manageroom set status=? where RoomNumber=?";
        PreparedStatement ps1;

        sql = "insert into checkout (roomnumber,name,email,cell,price,totaldays,totalamount,checkindate,checkoutdate,tax,actualamount,received,cashreturn)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(txtCustomerRoom.getText().trim()));
            ps.setString(2, txtCustomerName.getText().trim());
            ps.setString(3, txtCustomerEmail.getText().trim());
            ps.setString(4, txtCustomerCell.getText().trim());
            ps.setFloat(5, Float.parseFloat(txtCustomerPrice.getText().trim()));
            ps.setString(6, txtCustomerTotalDays.getText().trim());
            ps.setString(7, txtCustomerTotalAmount.getText().trim());
            ps.setDate(8, convertUtilDateToSqlDate(txtCustomerCheckIn.getDate()));
            ps.setDate(9, convertUtilDateToSqlDate(txtCustomerCheckOut.getDate()));
            ps.setFloat(10, Float.parseFloat(txtCustomerTax.getText().trim()));
            ps.setFloat(11, Float.parseFloat(txtCustomerActualAmount.getText().trim()));
            ps.setFloat(12, Float.parseFloat(txtCustomerReceivedCash.getText().trim()));
            ps.setFloat(13, Float.parseFloat(txtCustomerReturn.getText().trim()));
           

            ps.executeUpdate();
            ps.close();
            con.getCon().close();

            ps1 = con.getCon().prepareStatement(sql1);

            ps1.setString(1, "Unbooked");
            ps1.setInt(2, Integer.parseInt(txtCustomerRoom.getText().trim()));

            ps1.executeUpdate();

            ps1.close();
            con.getCon().close();

            JOptionPane.showMessageDialog(rootPane, "Check Out Succesfully");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(rootPane, "Check Out Failed");
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnCustomerCheckOutMouseClicked

    private void comboBedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboBedItemStateChanged
        // TODO add your handling code here:
        String roomType = comboRoomType.getSelectedItem().toString();
        String bedType = comboBed.getSelectedItem().toString();

        getRoomNumber(roomType, bedType);
        getCheckInPrice();


    }//GEN-LAST:event_comboBedItemStateChanged

    private void btnSearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSearchMouseClicked
        // TODO add your handling code here:
        getAllInfoToCheckOut();

    }//GEN-LAST:event_btnSearchMouseClicked

    private void comboRoomNumberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboRoomNumberMouseClicked
        // TODO add your handling code here:

        String roomType = comboRoomType.getSelectedItem().toString();
        String bedType = comboBed.getSelectedItem().toString();

        getRoomNumber(roomType, bedType);
        getCheckInPrice();

//        sql="select Price from manageroom where RoomNumber=?";
//        
//        try {
//            ps=con.getCon().prepareStatement(sql);
//            ps.setInt(1, Integer.parseInt(comboRoomNumber.getSelectedItem().toString()));
//            rs=ps.executeQuery();
//            while(rs.next()){
//            String price=rs.getString("Price");
//            txtManageRoomPrice.setText(price);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_comboRoomNumberMouseClicked

    private void txtCustomerRoomFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerRoomFocusLost
        // TODO add your handling code here:
        getRoomNumber(sql, sql);
        getAllInfoToCheckOut();

    }//GEN-LAST:event_txtCustomerRoomFocusLost

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseClicked
        // TODO add your handling code here:
        checkInReset();
    }//GEN-LAST:event_btnClearMouseClicked

    private void txtCustomerTotalDaysMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerTotalDaysMouseClicked
        // TODO add your handling code here:
//        String d1=txtCustomerCheckIn.getDate().toString();
//        String d2=txtCustomerCheckOut.getDate().toString();
//        dateDiffrence(d1, d2);
//        System.out.println(dateDiffrence(d2, d2));

//        long d1 = txtCustomerCheckIn.getDate().getTime();
//        long d2 = txtCustomerCheckOut.getDate().getTime();
//
//        long totalDays = d2 - d1;
//        Date d = new Date(864002580);
//        int date = (int) totalDays;
        java.util.Date date1 = txtCustomerCheckIn.getDate();
        java.util.Date date2 = txtCustomerCheckOut.getDate();
        LocalDate localDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate1 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

//        Date input = new Date();
        int totalDays = (int) localDate.until(localDate1, ChronoUnit.DAYS);
        txtCustomerTotalDays.setText(totalDays + "");

        System.out.println(localDate.until(localDate));
        System.out.println(localDate.until(localDate1, ChronoUnit.DAYS));


    }//GEN-LAST:event_txtCustomerTotalDaysMouseClicked

    private void txtCustomerTotalAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerTotalAmountMouseClicked
        // TODO add your handling code here:
        int totalDays = Integer.parseInt(txtCustomerTotalDays.getText());
        float price = Float.parseFloat(txtCustomerPrice.getText());

        Float totalAmount = totalDays * price;
        txtCustomerTotalAmount.setText(totalAmount + "");
        System.out.println(totalAmount + "");
    }//GEN-LAST:event_txtCustomerTotalAmountMouseClicked

    private void btnCustomerBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerBillMouseClicked
        // TODO add your handling code here:
        billRoomNo.setText(txtCustomerRoom.getText());

    }//GEN-LAST:event_btnCustomerBillMouseClicked

    public void getCheckInDetailsByDate(java.util.Date fromDate, java.util.Date toDate) {
        String[] columnName = {"Room Number", "Name", "Mobile", "Email", "Gender", "Nationality", "Adress", "CheckInDate", "Room Type", "Bed", "Price"};

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnName);

        tblCustomerDetails.setModel(model);
        sql = "select * from checkin where checkin between ? and ?";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();
            while (rs.next()) {

                int roomNumber = rs.getInt("roomnumber");
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");
                String email = rs.getString("email");
                String gender = rs.getString("gender");
                String nation = rs.getString("nationality");
                String adress = rs.getString("adress");
                java.util.Date date = rs.getDate("checkin");
                String roomType = rs.getString("roomtype");
                String bed = rs.getString("bed");
                String price = rs.getString("price");

                model.addRow(new Object[]{roomNumber, name, mobile, email, gender, nation, adress, date, roomType, bed, price});
            }
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getCheckOutDetailsByDate(java.util.Date fromDate, java.util.Date toDate) {
        String[] columnName = {"Room Number", "Name", "Email", "Cell", "Price", "Total Days", "Total Amount", "CheckInDate", "CheckOutDate"};

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnName);

        tblCustomerDetails.setModel(model);
        sql = "select * from checkout where checkoutdate between ? and ?";

        try {
            ps = con.getCon().prepareStatement(sql);
            ps.setDate(1, convertUtilDateToSqlDate(fromDate));
            ps.setDate(2, convertUtilDateToSqlDate(toDate));

            rs = ps.executeQuery();
            while (rs.next()) {

                int roomNumber = rs.getInt("roomnumber");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String cell = rs.getString("cell");
                String price = rs.getString("price");
                String totaldays = rs.getString("totaldays");
                String totalamount = rs.getString("totalamount");
                java.util.Date checkindate = rs.getDate("checkindate");
                java.util.Date checkoutdate = rs.getDate("checkoutdate");

                model.addRow(new Object[]{roomNumber, name, email, cell, price, totaldays, totalamount, checkindate, checkoutdate});
            }
            ps.close();
            rs.close();
            con.getCon().close();

        } catch (SQLException ex) {
            Logger.getLogger(DashBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void viewCustomerDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewCustomerDetailsMouseClicked
        // TODO add your handling code here:
        if (radioCheckInDetails.isSelected()) {
            getCheckInDetailsByDate(dateFrom.getDate(), dateFromTo.getDate());
        } else if (radioCheckOutDetails.isSelected()) {
            getCheckOutDetailsByDate(dateFrom.getDate(), dateFromTo.getDate());
        }


    }//GEN-LAST:event_viewCustomerDetailsMouseClicked

    private void comboRoomNumberItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboRoomNumberItemStateChanged
        // TODO add your handling code here:
//       String roomType = comboRoomType.getSelectedItem().toString();
//        String bedType = comboBed.getSelectedItem().toString();
//
//        getRoomNumber(roomType, bedType);

        getCheckInPrice();

    }//GEN-LAST:event_comboRoomNumberItemStateChanged

    private void comboRoomTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboRoomTypeItemStateChanged
        // TODO add your handling code here:
        String roomType = comboRoomType.getSelectedItem().toString();
        String bedType = comboBed.getSelectedItem().toString();

        getRoomNumber(roomType, bedType);
        getCheckInPrice();

    }//GEN-LAST:event_comboRoomTypeItemStateChanged

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
        // TODO add your handling code here:
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Print Data");

        job.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                pf.setOrientation(PageFormat.LANDSCAPE);
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                g2.scale(0.47, 0.47);

                printDetails.print(g2);

                return Printable.PAGE_EXISTS;

            }

        });
        boolean ok = job.printDialog();
        if (ok) {
            try {

                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }
        }


    }//GEN-LAST:event_btnPrintMouseClicked

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    public static String formatUtilDate(java.util.Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        return formattedDate;

    }
    private void CustomerBillMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CustomerBillMouseClicked
        // TODO add your handling code here:

//        txtCustomerRoom.setText(billRoomNo.getText());
        billRoomNo.setText(txtCustomerRoom.getText());
        billCheckin.setText(formatUtilDate(txtCustomerCheckIn.getDate()));
        billCheckOut.setText(formatUtilDate(txtCustomerCheckOut.getDate()));

        billMobile.setText(txtCustomerCell.getText());
        billTotalAmount.setText(txtCustomerTotalAmount.getText());
        billTax.setText(txtCustomerTax.getText());
        billActualAmount.setText(txtCustomerActualAmount.getText());
        billCashReceived.setText(txtCustomerReceivedCash.getText());
        billReturn.setText(txtCustomerReturn.getText());
        billName.setText(txtCustomerName.getText());
        billInvoice.setText(txtCustomerInvoice.getText());
    }//GEN-LAST:event_CustomerBillMouseClicked

    private void btnCustomerBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerBillActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(5);
    }//GEN-LAST:event_btnCustomerBillActionPerformed

    private void txtCustomerActualAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerActualAmountMouseClicked
        // TODO add your handling code here:
        int totalDays = Integer.parseInt(txtCustomerTotalDays.getText());
        float price = Float.parseFloat(txtCustomerPrice.getText());
        int tax = Integer.parseInt(txtCustomerTax.getText());

        Float totalAmount = totalDays * price;
        Float actualAmount = totalAmount + (totalAmount * tax / 100);
        txtCustomerActualAmount.setText(actualAmount + "");

    }//GEN-LAST:event_txtCustomerActualAmountMouseClicked

    private void txtCustomerReturnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCustomerReturnMouseClicked
        // TODO add your handling code here:
        int totalDays = Integer.parseInt(txtCustomerTotalDays.getText());
        float price = Float.parseFloat(txtCustomerPrice.getText());
        float tax = Float.parseFloat(txtCustomerTax.getText());
        float cashR = Float.parseFloat(txtCustomerReceivedCash.getText());

        Float totalAmount = totalDays * price;
        Float actualAmount = totalAmount + (totalAmount * tax / 100);

        Float cashReturn = cashR - actualAmount;
        txtCustomerReturn.setText(cashReturn + "");

    }//GEN-LAST:event_txtCustomerReturnMouseClicked

    public void checkOutReset() {
        txtCustomerRoom.setText(null);
        txtCustomerName.setText(null);
        txtCustomerEmail.setText(null);
        txtCustomerCell.setText(null);
        txtCustomerCheckIn.setDate(null);
        txtCustomerCheckOut.setDate(null);
        txtCustomerPrice.setText(null);
        txtCustomerTax.setText(null);
        txtCustomerTotalAmount.setText(null);
        txtCustomerActualAmount.setText(null);
        txtCustomerReceivedCash.setText(null);
        txtCustomerReturn.setText(null);
        txtCustomerTotalDays.setText(null);
    }
    private void btnCustomerClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCustomerClearMouseClicked
        // TODO add your handling code here:
        checkOutReset();
    }//GEN-LAST:event_btnCustomerClearMouseClicked

    private void DashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardActionPerformed
        // TODO add your handling code here:
        menu.setSelectedIndex(0);
         getRoomBooked();
        getRoomUnbooked();
        todayRevenue();
    }//GEN-LAST:event_DashboardActionPerformed

    private void txtCustomerInvoiceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerInvoiceFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerInvoiceFocusLost

    public void check() {

    }

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
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashBoard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane CheckIn;
    private javax.swing.JTabbedPane CheckOut;
    private javax.swing.JTabbedPane CustomerBill;
    private javax.swing.JTabbedPane CustomerDetails;
    private javax.swing.JTabbedPane DashBoard;
    private javax.swing.JButton Dashboard;
    private javax.swing.JTabbedPane ManageRoom;
    private javax.swing.JTextField billActualAmount;
    private javax.swing.JTextField billCashReceived;
    private javax.swing.JTextField billCheckOut;
    private javax.swing.JTextField billCheckin;
    private javax.swing.JTextField billInvoice;
    private javax.swing.JTextField billMobile;
    private javax.swing.JTextField billName;
    private javax.swing.JTextField billReturn;
    private javax.swing.JTextField billRoomNo;
    private javax.swing.JTextField billTax;
    private javax.swing.JTextField billTotalAmount;
    private javax.swing.JButton btnAllotRoom;
    private javax.swing.JButton btnCheckIn;
    private javax.swing.JButton btnCheckOut;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCustomerBill;
    private javax.swing.JButton btnCustomerCheckOut;
    private javax.swing.JButton btnCustomerClear;
    private javax.swing.JButton btnCustomerDetails;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnManageAdd;
    private javax.swing.JButton btnManageReset;
    private javax.swing.JButton btnManageRoom;
    private javax.swing.JButton btnManageUpdate;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSearch;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> comboBed;
    private javax.swing.JComboBox<String> comboGender;
    private javax.swing.JComboBox<String> comboManageRoomBed;
    private javax.swing.JComboBox<String> comboManageRoomType;
    private javax.swing.JComboBox<String> comboRoomNumber;
    private javax.swing.JComboBox<String> comboRoomType;
    private com.toedter.calendar.JDateChooser dateFrom;
    private com.toedter.calendar.JDateChooser dateFromTo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
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
    private javax.swing.JLabel jLabel41;
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
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblBookedRoom;
    private javax.swing.JLabel lblTotalRevenue;
    private javax.swing.JLabel lblUnbookedRoom;
    private javax.swing.JLabel localDate;
    private javax.swing.JLabel localTime;
    private javax.swing.JTabbedPane menu;
    private javax.swing.JPanel printDetails;
    private javax.swing.JRadioButton radioCheckInDetails;
    private javax.swing.JRadioButton radioCheckOutDetails;
    private javax.swing.JTable tblCustomerDetails;
    private javax.swing.JTable tblManageRoom;
    private javax.swing.JTextArea txtAdress;
    private javax.swing.JTextField txtCustomerActualAmount;
    private javax.swing.JTextField txtCustomerCell;
    private com.toedter.calendar.JDateChooser txtCustomerCheckIn;
    private com.toedter.calendar.JDateChooser txtCustomerCheckOut;
    private javax.swing.JTextField txtCustomerEmail;
    private javax.swing.JTextField txtCustomerInvoice;
    private javax.swing.JTextField txtCustomerName;
    private javax.swing.JTextField txtCustomerPrice;
    private javax.swing.JTextField txtCustomerReceivedCash;
    private javax.swing.JTextField txtCustomerReturn;
    private javax.swing.JTextField txtCustomerRoom;
    private javax.swing.JTextField txtCustomerTax;
    private javax.swing.JTextField txtCustomerTotalAmount;
    private javax.swing.JTextField txtCustomerTotalDays;
    private com.toedter.calendar.JDateChooser txtDate;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtManageRoomNum;
    private javax.swing.JTextField txtManageRoomPrice;
    private javax.swing.JTextField txtMobile;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtNation;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JButton viewCustomerDetails;
    // End of variables declaration//GEN-END:variables
}
