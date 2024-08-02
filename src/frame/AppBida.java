/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frame;

import ModelDAO.BillDAO;
import ModelDAO.BillInfoDAO;
import ModelDAO.FoodCategoryDAO;
import ModelDAO.FoodDAO;
import ModelDAO.MenuDAO;
import ModelDAO.TableBidaDAO;
import entityModel.Bill;
import entityModel.Billinfo;
import entityModel.Food;
import entityModel.FoodCategory;
import entityModel.Menu;
import entityModel.TableBida;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import utils.DateHelper;
import utils.DialogHelper;
import utils.jdbcHelper;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import javax.swing.JScrollPane;
import utils.HourlyRateCalculator;

/**
 *
 * @author MINH DANG
 */
public class AppBida extends javax.swing.JFrame {

    private HourlyRateCalculator hourlyRateCalculator;

    private DefaultTableModel modelTable;
    FoodCategoryDAO fcatedao = new FoodCategoryDAO();

    FoodDAO fdao = new FoodDAO();
    MenuDAO menudao = new MenuDAO();
    BillDAO billdao = new BillDAO();
    BillInfoDAO billifdao = new BillInfoDAO();
    TableBidaDAO tbdao = new TableBidaDAO();
    int idtable;
    int count = 0;
    JButton clickbt;
    /**
     * Creates new form AppBida
     */
    public AppBida() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("BILLIARDS CLUB");
        init();
    }

    //init
    void init() {
        hourlyRateCalculator = new HourlyRateCalculator();
        snpCount.setValue(1);
        setlbDateNow();
        dataCboCategory();
        dataCboTableBida();
        eventAction();
    }

    //selectTab
    private void selectTabs() {
        tabs.setSelectedIndex(1);
    }

    void exit() {
        this.dispose();
        new DangNhapJDiaLog(this, true).setVisible(true);
    }

    //data combobox FoodCategory
    private void dataCboCategory() {
        DefaultComboBoxModel cboModel = (DefaultComboBoxModel) cboCategory.getModel();
        cboModel.removeAllElements();
        try {
            List<FoodCategory> list = fcatedao.selectAll();
            for (FoodCategory f : list) {
                cboModel.addElement(f.getName());
            }
            dataCboFood();
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alertError(this, "Error");
        }
    }

    //data combobox Food and drink
    private void dataCboFood() {
        DefaultComboBoxModel cboModel = (DefaultComboBoxModel) cboFood.getModel();
        cboModel.removeAllElements();
        try {
            int id = cboCategory.getSelectedIndex();
            if (id >= 0) {
                List<Food> listfood = fdao.select_by_idcategory(id + 1);
                for (Food f : listfood) {
                    cboModel.addElement(f);
                }
            } else {
                System.out.println("No");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //data combobox tableBida
    private void dataCboTableBida() {
        DefaultComboBoxModel cboModel = (DefaultComboBoxModel) cboTableBida.getModel();
        cboModel.removeAllElements();
        try {
            List<TableBida> tb = tbdao.selectAll();
            for (TableBida tbida : tb) {
                cboModel.addElement(tbida);
            }
        } catch (Exception e) {
        }
    }

    //*****************CHUYỂN BÀN****************  
    //switchTable
    private void switchTable() {
        TableBida tb = (TableBida) cboTableBida.getSelectedItem();
        int idswitch = tb.getId();
        if (showIdBill() > 0) {
            try {
                tbdao.switchtable(idswitch, idtable);
                DialogHelper.alert(this, "Chuyển bàn thành công");
                this.loadDataonTable();
                this.clearBill();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        DialogHelper.alertError(this, "Chưa bắt đầu giờ chơi");
        return;
    }

    //******************* Bill ************************//
    //set lbDateNow
    private void setlbDateNow() {
        Date datenow = DateHelper.now();
        lbDateNow.setText(DateHelper.toString(datenow, "yyyy-MM-dd"));
    }

    //updatelbTienGio
    private void updatelbTienGio() {
        lbTienGio.setText(String.valueOf(hourlyRateCalculator.getTotalAmount()));
    }

    //showIdFood
    private Integer showIdFood() {
        Integer idfood = null;
        try {
            Food food = (Food) cboFood.getSelectedItem();
            if (food != null) {
                idfood = food.getId();
            } else {
                System.out.println("Select food is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idfood;
    }

    //ShowidBill
    private int showIdBill() {
        int idbill = 0;
        try {
            Integer idbillObject = billdao.findbyIdBill(idtable);
            if (idbillObject != null) {
                idbill = idbillObject.intValue();
                System.out.println(idbill);
                return idbill;
            } else {
                return 0;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return idbill;
    }

    //Clear Bill
    private void clearBill() {
        lbTienGio.setText(String.valueOf(0));
        lbTongTien.setText(String.valueOf(0));
        txtTimeStart.setText("");
        txtTimeStop.setText("");
        txtGiamGia.setText(String.valueOf(0));
        txtPhiDichVu.setText(String.valueOf(0));
        txtPhiKhac.setText(String.valueOf(0));
        txtGhiChu.setText(String.valueOf(0));
        modelTable = (DefaultTableModel) tbInfo.getModel();
        modelTable.setRowCount(0);
    }

    //getBill
    private Bill getBill() {
        Bill model = new Bill();
        model.setDatecheckin(DateHelper.toDate(txtTimeStart.getText(), "yyyy-MM-dd HH:mm:ss"));
        if (txtTimeStop.getText().equals("")) {
            model.setDatecheckout(DateHelper.toDate(null));
        } else {
            model.setDatecheckout(DateHelper.toDate(txtTimeStop.getText()));
        }
        model.setIdtable(idtable);
        return model;
    }

//    //getBillInfo
    private Billinfo getBillIfno() {
        count = (int) snpCount.getValue();
        Billinfo model = new Billinfo();
        model.setIdbill(showIdBill());
        model.setIdfood(showIdFood());
        model.setCount(count);
        return model;
    }

//    //insert Billinfo
    private void insertBillinfo() {
        Billinfo billinfo = getBillIfno();
        try {
            billifdao.insert(billinfo);
            this.loadDataonTable();
            System.out.println("Success");
            snpCount.setValue(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //TienGio
    private void tinhTienGio() {
        hourlyRateCalculator.startTimer();
    }

    //Check input
    private void checkinput(){
        try {
            int giamgia = Integer.parseInt(txtGiamGia.getText());
            int phidichvu = Integer.parseInt(txtPhiDichVu.getText());
            int phikhac = Integer.parseInt(txtPhiKhac.getText());
            if(giamgia < 0 || phidichvu < 0 || phikhac < 0){
                DialogHelper.alertError(this, "Vui lòng nhập số dương");
            }
        } catch (Exception e) {
            DialogHelper.alertError(this, "Vui lòng nhập số hợp lệ!");
        }
    }
    
    //Tính tổng tiền
    private void calTotalPice() {
        this.checkinput();
        float tong = 0;
        this.updatelbTienGio();
        for (int i = 0; i < tbInfo.getRowCount(); i++) {
            float thanhtien = (float) tbInfo.getValueAt(i, 3);
            tong += thanhtien;
        }
        tong += Float.parseFloat(lbTienGio.getText());  //tiengio
        tong -= Float.parseFloat(txtGiamGia.getText()); //giamgia
        tong += Float.parseFloat(txtPhiDichVu.getText()); //Phidichvu
        tong += Float.parseFloat(txtPhiKhac.getText());  //PhiKhac
        lbTongTien.setText(String.valueOf(tong));

    }

    //insert Bill
    private void insertBill() {
        tinhTienGio();
        txtTimeStart.setText(DateHelper.stringsnow());
        Bill model = getBill();
        try {
            billdao.insert(model);
            DialogHelper.alert(this, " thành công");
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Error");
        }
    }

    private void updateBill() {
        Bill model = getBill();
        try {
            model.setDatecheckout(DateHelper.toDate(txtTimeStop.getText()));
            model.setStatus(1);
            model.setTotalPrice(Float.parseFloat(lbTongTien.getText()));
            model.setId(showIdBill());
            billdao.update(model);
            this.billTamTinh();
            this.clearBill();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //load data on Table
    private void loadDataonTable() {
        modelTable = (DefaultTableModel) tbInfo.getModel();
        modelTable.setRowCount(0);
        try {
            this.showDateCheckin();
            List<Menu> m = menudao.selectAllMenuByIdTable(idtable);
            for (Menu menu : m) {
                if (m != null) {
                    Object[] row = {
                        menu.getFoodname(),
                        menu.getCount(),
                        menu.getPrice(),
                        menu.getTotalPrice()
                    };
                    modelTable.addRow(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //event Action
    private void eventAction() {
        SwingUtilities.invokeLater(() -> {
            clickTable();
        });
    }

    //Select showCHeckin
    private void showDateCheckin() {
        try {
            Timestamp bill = billdao.findDatebyIdtable(idtable);
            if (bill != null) {
                txtTimeStart.setText(String.valueOf(bill));
                btnStart.setEnabled(false);
            } else {
                txtTimeStart.setText("");
                btnStart.setEnabled(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Click ThanhToan
    private void clickThanhToan() {
        modelTable = (DefaultTableModel) tbInfo.getModel();
        modelTable.setRowCount(0);
        try {
            billifdao.deleteBillInfo();
            this.loadDataonTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //**************billTamTinh
    private void billTamTinh() {
        try {
            List<Menu> m = menudao.selectAllMenuByIdTable(idtable);
            DialogHelper.alert(this,
                    "Mã hóa đơn :" + lbMaHoaDon.getText()
                    + "\nNgày tạo :" + txtTimeStop.getText()
                    + "\nBàn khách chơi :" + lbTable.getText()
                    + "\nGiờ vào :" + txtTimeStart.getText()
                    + "\nGiờ ra : " + txtTimeStop.getText()
                    + "\nTiền giờ :" + lbTienGio.getText()
                    + "\nGiảm giá :" + txtGiamGia.getText()
                    + "\nPhí dịch vụ : " + txtPhiDichVu.getText()
                    + "\nPhí khác :" + txtPhiKhac.getText()
                    + "\n-----------------------------------------------------------\n"
                    + "Mặt hàng --- Số lượng --- Giá --- Thành tiền\n"
                    + m.toString()
                    + "\n-----------------------------------------------------------\n"
                    + "TỔNG :" + lbTongTien.getText()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //clickStart
    private void clickTable() {
        ActionListener buttonls = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearBill();
                clickbt = (JButton) e.getSource();
                idtable = Integer.parseInt(clickbt.getName());
                loadDataonTable();
                calTotalPice();
                selectTabs();
                lbTable.setText(clickbt.getText());
            }
        };
        btnLiber1.addActionListener(buttonls);
        btnLiber2.addActionListener(buttonls);
        btnLiber3.addActionListener(buttonls);
        btnLiber4.addActionListener(buttonls);
        btnLiber5.addActionListener(buttonls);
        btnLiber6.addActionListener(buttonls);
        btnLiber7.addActionListener(buttonls);
        btnLiber8.addActionListener(buttonls);
        btnLiber9.addActionListener(buttonls);
        btnLiber10.addActionListener(buttonls);
        btnLiber11.addActionListener(buttonls);
        btnLiber12.addActionListener(buttonls);
        btnVip1.addActionListener(buttonls);
        btnVip2.addActionListener(buttonls);
        btnVip3.addActionListener(buttonls);
        btnLo1.addActionListener(buttonls);
        btnLo2.addActionListener(buttonls);
        btnLo3.addActionListener(buttonls);
        btnLo4.addActionListener(buttonls);
        btnLo5.addActionListener(buttonls);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        btnVip2 = new javax.swing.JButton();
        btnVip1 = new javax.swing.JButton();
        btnVip3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnLiber10 = new javax.swing.JButton();
        btnLiber11 = new javax.swing.JButton();
        btnLiber1 = new javax.swing.JButton();
        btnLiber6 = new javax.swing.JButton();
        btnLiber9 = new javax.swing.JButton();
        btnLiber12 = new javax.swing.JButton();
        btnLiber3 = new javax.swing.JButton();
        btnLiber5 = new javax.swing.JButton();
        btnLiber4 = new javax.swing.JButton();
        btnLiber8 = new javax.swing.JButton();
        btnLiber2 = new javax.swing.JButton();
        btnLiber7 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnLo1 = new javax.swing.JButton();
        btnLo2 = new javax.swing.JButton();
        btnLo3 = new javax.swing.JButton();
        btnLo4 = new javax.swing.JButton();
        btnLo5 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cboCategory = new javax.swing.JComboBox<>();
        cboFood = new javax.swing.JComboBox<>();
        snpCount = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();
        cboTableBida = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        txtTimeStart = new javax.swing.JTextField();
        txtTimeStop = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnStop = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        lbTienGio = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        lbTongTien = new javax.swing.JLabel();
        txtPhiKhac = new javax.swing.JTextField();
        txtGiamGia = new javax.swing.JTextField();
        txtPhiDichVu = new javax.swing.JTextField();
        btnThanhToan = new javax.swing.JButton();
        btnTamTinh = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        btnStart = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbInfo = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        btnXacNhan = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        lbMaHoaDon = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lbTable = new javax.swing.JLabel();
        lbDateNow = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        jMenu3.setText("jMenu3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabs.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnVip2.setBackground(new java.awt.Color(204, 255, 204));
        btnVip2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnVip2.setForeground(new java.awt.Color(255, 0, 0));
        btnVip2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnVip2.setText("Bàn VIP 2");
        btnVip2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVip2.setMaximumSize(new java.awt.Dimension(100, 100));
        btnVip2.setMinimumSize(new java.awt.Dimension(100, 100));
        btnVip2.setName("20"); // NOI18N
        btnVip2.setPreferredSize(new java.awt.Dimension(100, 100));
        btnVip2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnVip2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVip2ActionPerformed(evt);
            }
        });

        btnVip1.setBackground(new java.awt.Color(204, 255, 204));
        btnVip1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnVip1.setForeground(new java.awt.Color(255, 0, 0));
        btnVip1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnVip1.setText("Bàn VIP 1");
        btnVip1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVip1.setMaximumSize(new java.awt.Dimension(100, 100));
        btnVip1.setMinimumSize(new java.awt.Dimension(100, 100));
        btnVip1.setName("19"); // NOI18N
        btnVip1.setPreferredSize(new java.awt.Dimension(100, 100));
        btnVip1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnVip1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVip1ActionPerformed(evt);
            }
        });

        btnVip3.setBackground(new java.awt.Color(204, 255, 204));
        btnVip3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnVip3.setForeground(new java.awt.Color(255, 0, 0));
        btnVip3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnVip3.setText("Bàn VIP 3");
        btnVip3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnVip3.setMaximumSize(new java.awt.Dimension(100, 100));
        btnVip3.setMinimumSize(new java.awt.Dimension(100, 100));
        btnVip3.setName("21"); // NOI18N
        btnVip3.setPreferredSize(new java.awt.Dimension(100, 100));
        btnVip3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnVip3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVip3ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Khu vực liber", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Black", 3, 18), new java.awt.Color(0, 0, 0))); // NOI18N

        btnLiber10.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber10.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber10.setText("Bàn 10");
        btnLiber10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber10.setName("11"); // NOI18N
        btnLiber10.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLiber10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiber10ActionPerformed(evt);
            }
        });

        btnLiber11.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber11.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber11.setText("Bàn 11");
        btnLiber11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber11.setName("12"); // NOI18N
        btnLiber11.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLiber11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiber11ActionPerformed(evt);
            }
        });

        btnLiber1.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber1.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber1.setText("Bàn 1");
        btnLiber1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber1.setName("2"); // NOI18N
        btnLiber1.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLiber1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiber1ActionPerformed(evt);
            }
        });

        btnLiber6.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber6.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber6.setText("Bàn 6");
        btnLiber6.setBorderPainted(false);
        btnLiber6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber6.setInheritsPopupMenu(true);
        btnLiber6.setName("7"); // NOI18N
        btnLiber6.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLiber6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiber6ActionPerformed(evt);
            }
        });

        btnLiber9.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber9.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber9.setText("Bàn 9");
        btnLiber9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber9.setName("10"); // NOI18N
        btnLiber9.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnLiber12.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber12.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber12.setText("Bàn 12");
        btnLiber12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber12.setMaximumSize(new java.awt.Dimension(100, 100));
        btnLiber12.setMinimumSize(new java.awt.Dimension(100, 100));
        btnLiber12.setName("13"); // NOI18N
        btnLiber12.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLiber12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiber12ActionPerformed(evt);
            }
        });

        btnLiber3.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber3.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber3.setText("Bàn 3");
        btnLiber3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber3.setName("4"); // NOI18N
        btnLiber3.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnLiber5.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber5.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber5.setText("Bàn 5");
        btnLiber5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber5.setName("6"); // NOI18N
        btnLiber5.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnLiber4.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber4.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber4.setText("Bàn 4");
        btnLiber4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber4.setName("5"); // NOI18N
        btnLiber4.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnLiber8.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber8.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber8.setText("Bàn 8");
        btnLiber8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber8.setName("9"); // NOI18N
        btnLiber8.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnLiber2.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber2.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber2.setText("Bàn 2");
        btnLiber2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber2.setName("3"); // NOI18N
        btnLiber2.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        btnLiber7.setBackground(new java.awt.Color(204, 204, 204));
        btnLiber7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLiber7.setForeground(new java.awt.Color(255, 102, 0));
        btnLiber7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLiber7.setText("Bàn 7");
        btnLiber7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLiber7.setMaximumSize(new java.awt.Dimension(100, 100));
        btnLiber7.setMinimumSize(new java.awt.Dimension(100, 100));
        btnLiber7.setName("8"); // NOI18N
        btnLiber7.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLiber7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLiber5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLiber9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLiber6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLiber2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLiber10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnLiber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnLiber3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLiber11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnLiber7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLiber4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLiber8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLiber12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLiber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLiber2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLiber3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLiber4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnLiber8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLiber7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLiber6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLiber11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLiber12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnLiber5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnLiber10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLiber9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(32, 32, 32))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Khu vực bàn lỗ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI Black", 3, 18), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel5.setForeground(new java.awt.Color(0, 0, 0));

        btnLo1.setBackground(new java.awt.Color(204, 204, 204));
        btnLo1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLo1.setForeground(new java.awt.Color(51, 51, 255));
        btnLo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLo1.setText("Bàn lỗ 1");
        btnLo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLo1.setName("14"); // NOI18N
        btnLo1.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLo1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLo1ActionPerformed(evt);
            }
        });

        btnLo2.setBackground(new java.awt.Color(204, 204, 204));
        btnLo2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLo2.setForeground(new java.awt.Color(51, 51, 255));
        btnLo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLo2.setText("Bàn lỗ 2");
        btnLo2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLo2.setName("15"); // NOI18N
        btnLo2.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLo2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLo2ActionPerformed(evt);
            }
        });

        btnLo3.setBackground(new java.awt.Color(204, 204, 204));
        btnLo3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLo3.setForeground(new java.awt.Color(51, 51, 255));
        btnLo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLo3.setText("Bàn lỗ 3");
        btnLo3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLo3.setMaximumSize(new java.awt.Dimension(100, 100));
        btnLo3.setMinimumSize(new java.awt.Dimension(100, 100));
        btnLo3.setName("16"); // NOI18N
        btnLo3.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLo3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLo3ActionPerformed(evt);
            }
        });

        btnLo4.setBackground(new java.awt.Color(204, 204, 204));
        btnLo4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLo4.setForeground(new java.awt.Color(51, 51, 255));
        btnLo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLo4.setText("Bàn lỗ 4");
        btnLo4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLo4.setMaximumSize(new java.awt.Dimension(100, 100));
        btnLo4.setMinimumSize(new java.awt.Dimension(100, 100));
        btnLo4.setName("17"); // NOI18N
        btnLo4.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLo4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLo4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLo4ActionPerformed(evt);
            }
        });

        btnLo5.setBackground(new java.awt.Color(204, 204, 204));
        btnLo5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLo5.setForeground(new java.awt.Color(51, 51, 255));
        btnLo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/banBida.png"))); // NOI18N
        btnLo5.setText("Bàn lỗ 5");
        btnLo5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLo5.setMaximumSize(new java.awt.Dimension(100, 100));
        btnLo5.setMinimumSize(new java.awt.Dimension(100, 100));
        btnLo5.setName("18"); // NOI18N
        btnLo5.setPreferredSize(new java.awt.Dimension(100, 100));
        btnLo5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLo5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLo5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(btnLo1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(btnLo2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(btnLo3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(btnLo4, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(btnLo5, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLo5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLo4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLo3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI Black", 3, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Khu vực bàn VIP");

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout.png"))); // NOI18N
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel12MouseEntered(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(108, 108, 108)
                                        .addComponent(btnVip3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnVip1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11))
                                        .addGap(59, 59, 59)
                                        .addComponent(btnVip2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(15, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnVip1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnVip2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addComponent(btnVip3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(101, 101, 101))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        tabs.addTab("Phòng bàn", jPanel1);

        cboCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboCategoryItemStateChanged(evt);
            }
        });
        cboCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCategoryActionPerformed(evt);
            }
        });

        cboFood.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboFood.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboFoodItemStateChanged(evt);
            }
        });
        cboFood.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboFoodMouseClicked(evt);
            }
        });
        cboFood.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFoodActionPerformed(evt);
            }
        });

        snpCount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                snpCountStateChanged(evt);
            }
        });
        snpCount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                snpCountMouseClicked(evt);
            }
        });

        cboTableBida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboTableBida.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboTableBidaItemStateChanged(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Chuyển bàn");
        jButton1.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel1.setText("Chuyển bàn :");

        txtTimeStart.setEditable(false);
        txtTimeStart.setBackground(new java.awt.Color(204, 255, 255));
        txtTimeStart.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        txtTimeStart.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTimeStartFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTimeStartFocusLost(evt);
            }
        });
        txtTimeStart.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtTimeStartInputMethodTextChanged(evt);
            }
        });
        txtTimeStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimeStartActionPerformed(evt);
            }
        });

        txtTimeStop.setEditable(false);
        txtTimeStop.setBackground(new java.awt.Color(204, 255, 255));
        txtTimeStop.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N

        jLabel2.setText("Thời gian bắt đầu :");

        jLabel3.setText("Thời gian kết thúc :");

        jLabel5.setText("Phí dịch vụ :");

        jLabel6.setText("Giảm giá :");

        jLabel7.setText("Tiền giờ :");

        jLabel8.setText("Phí khác :");

        btnStop.setBackground(new java.awt.Color(255, 255, 255));
        btnStop.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnStop.setForeground(new java.awt.Color(255, 0, 51));
        btnStop.setText("Dừng giờ");
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        jLabel9.setText("Ghi chú :");

        lbTienGio.setText("0");

        jLabel13.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel13.setText("Menu :");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane3.setViewportView(txtGhiChu);

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 1, 20)); // NOI18N
        jLabel4.setText("Tổng tiền :");

        lbTongTien.setFont(new java.awt.Font("Segoe UI Black", 1, 20)); // NOI18N
        lbTongTien.setForeground(new java.awt.Color(255, 51, 51));
        lbTongTien.setText("0");

        txtPhiKhac.setText("0");
        txtPhiKhac.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPhiKhacFocusLost(evt);
            }
        });

        txtGiamGia.setText("0");
        txtGiamGia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGiamGiaFocusLost(evt);
            }
        });

        txtPhiDichVu.setText("0");
        txtPhiDichVu.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPhiDichVuFocusLost(evt);
            }
        });

        btnThanhToan.setBackground(new java.awt.Color(0, 204, 255));
        btnThanhToan.setFont(new java.awt.Font("Ebrima", 1, 18)); // NOI18N
        btnThanhToan.setForeground(new java.awt.Color(255, 255, 0));
        btnThanhToan.setText("Thanh toán (F1)");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnTamTinh.setBackground(new java.awt.Color(153, 255, 153));
        btnTamTinh.setFont(new java.awt.Font("Ebrima", 1, 18)); // NOI18N
        btnTamTinh.setForeground(new java.awt.Color(255, 0, 51));
        btnTamTinh.setText("Tạm tính (F5)");
        btnTamTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTamTinhActionPerformed(evt);
            }
        });

        btnStart.setBackground(new java.awt.Color(255, 51, 51));
        btnStart.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnStart.setForeground(new java.awt.Color(255, 255, 255));
        btnStart.setText("Bắt đầu");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/lohoBida.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 194, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        tbInfo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ));
        jScrollPane2.setViewportView(tbInfo);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Loại :");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Đồ ăn & đồ uống :");

        btnXacNhan.setBackground(new java.awt.Color(0, 102, 102));
        btnXacNhan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXacNhan.setForeground(new java.awt.Color(255, 255, 255));
        btnXacNhan.setText("Xác nhận");
        btnXacNhan.setPreferredSize(new java.awt.Dimension(100, 100));
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Mã hóa đơn :");

        lbMaHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lbMaHoaDon.setText("HD0826482012");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Bàn :");

        lbTable.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lbTable.setText("Liber 1");

        lbDateNow.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lbDateNow.setText("12 / 12 / 2023 00:00:00");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("Ngày tạo hóa đơn :");

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/logout.png"))); // NOI18N
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel17MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(73, 73, 73)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtTimeStart, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtTimeStop, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(btnStart)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnStop))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(btnTamTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(65, 65, 65)
                                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbDateNow)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lbTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbMaHoaDon, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(27, 27, 27)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtGiamGia)
                                    .addComponent(txtPhiKhac, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(27, 27, 27)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(59, 59, 59))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addComponent(txtPhiDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(39, 39, 39)
                                        .addComponent(lbTienGio, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(131, 131, 131))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator4)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(cboTableBida, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel15)
                                        .addComponent(jLabel14))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel13)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cboCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cboFood, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addComponent(snpCount, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17))
                            .addComponent(jSeparator2)
                            .addComponent(jSeparator1)
                            .addComponent(jSeparator5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnStart)
                            .addComponent(btnStop))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimeStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimeStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel15)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboFood, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(snpCount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnXacNhan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTableBida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(lbTienGio)
                            .addComponent(jLabel6)
                            .addComponent(txtGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtPhiDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtPhiKhac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lbTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTamTinh, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(165, 165, 165))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbDateNow)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(lbMaHoaDon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18)
                    .addComponent(lbTable))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("Thực đơn", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCategoryActionPerformed
        // TODO add your handling code here:
//        this.dataCboFood();
    }//GEN-LAST:event_cboCategoryActionPerformed

    private void txtTimeStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimeStartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimeStartActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        // TODO add your handling code here:
        txtTimeStop.setText(DateHelper.stringsnow());
        calTotalPice();
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnLo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLo1ActionPerformed

    private void btnLo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLo2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLo2ActionPerformed

    private void btnVip1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVip1ActionPerformed
        // TODO add your handling code here:   
    }//GEN-LAST:event_btnVip1ActionPerformed

    private void btnVip2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVip2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVip2ActionPerformed

    private void btnVip3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVip3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVip3ActionPerformed

    private void btnLo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLo3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLo3ActionPerformed

    private void btnLo4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLo4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLo4ActionPerformed

    private void btnLo5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLo5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLo5ActionPerformed

    private void cboCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboCategoryItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            this.dataCboFood();
        }
    }//GEN-LAST:event_cboCategoryItemStateChanged

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        // TODO add your handling code here:
        if (idtable <= 1) {
            DialogHelper.alertError(this, "Chưa chọn bàn chơi !");
            return;
        }
        this.insertBill();
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
    }//GEN-LAST:event_btnStartActionPerformed

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        // TODO add your handling code here:
        if (txtTimeStart.getText().equals("")) {
            DialogHelper.alertError(this, "Chưa bắt đầu giờ");
            return;
        }
        this.insertBillinfo();
        this.calTotalPice();
    }//GEN-LAST:event_btnXacNhanActionPerformed

    private void cboFoodItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboFoodItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            this.showIdFood();
        }
    }//GEN-LAST:event_cboFoodItemStateChanged

    private void jLabel12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel12MouseEntered

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        // TODO add your handling code here:
        this.exit();
    }//GEN-LAST:event_jLabel12MouseClicked

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        // TODO add your handling code here:
        this.dispose();
        new DangNhapJDiaLog(this, true).setVisible(true);
    }//GEN-LAST:event_jLabel17MouseClicked

    private void cboFoodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFoodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboFoodActionPerformed

    private void cboFoodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboFoodMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cboFoodMouseClicked

    private void txtPhiDichVuFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPhiDichVuFocusLost
        // TODO add your handling code here:
        calTotalPice();
    }//GEN-LAST:event_txtPhiDichVuFocusLost

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // TODO add your handling code here:
        if (txtTimeStart.getText().equals("")) {
            DialogHelper.alertError(this, "Chưa có thời gian bắt đầu");
            return;
        }
        if (txtTimeStop.getText().equals("")) {
            DialogHelper.alertError(this, "Chưa dừng giờ");
        } else {
            this.updateBill();
            clickThanhToan();
            DialogHelper.alert(this, "ĐÃ THANH TOÁN");
            btnStart.setEnabled(true);
        }
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void txtGiamGiaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGiamGiaFocusLost
        // TODO add your handling code here:
        calTotalPice();
    }//GEN-LAST:event_txtGiamGiaFocusLost

    private void txtPhiKhacFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPhiKhacFocusLost
        // TODO add your handling code here:
        calTotalPice();
    }//GEN-LAST:event_txtPhiKhacFocusLost

    private void cboTableBidaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboTableBidaItemStateChanged
        // TODO add your handling code here:
//        int idBill = (int) cboTableBida.getSelectedItem();
    }//GEN-LAST:event_cboTableBidaItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if (idtable < 2) {
            DialogHelper.alertError(this, "Chưa chọn bàn chơi");
            return;
        }
        
        if(DialogHelper.comfirm(this, "Xác nhận chuyển bàn?")){
            this.switchTable();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnTamTinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTamTinhActionPerformed
        // TODO add your handling code here:
        this.billTamTinh();
    }//GEN-LAST:event_btnTamTinhActionPerformed

    private void snpCountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_snpCountMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_snpCountMouseClicked

    private void snpCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_snpCountStateChanged
        // TODO add your handling code here:
        int count = (int) snpCount.getValue();
        if (count <= 1) {
            snpCount.setValue(1);
        }
    }//GEN-LAST:event_snpCountStateChanged

    private void btnLiber12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiber12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLiber12ActionPerformed

    private void btnLiber1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiber1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLiber1ActionPerformed

    private void btnLiber11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiber11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLiber11ActionPerformed

    private void btnLiber10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiber10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLiber10ActionPerformed

    private void btnLiber6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiber6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLiber6ActionPerformed

    private void txtTimeStartFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimeStartFocusGained
        // TODO add your handling code here:

    }//GEN-LAST:event_txtTimeStartFocusGained

    private void txtTimeStartInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtTimeStartInputMethodTextChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_txtTimeStartInputMethodTextChanged

    private void txtTimeStartFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTimeStartFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_txtTimeStartFocusLost

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
            java.util.logging.Logger.getLogger(AppBida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppBida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppBida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppBida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppBida().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLiber1;
    private javax.swing.JButton btnLiber10;
    private javax.swing.JButton btnLiber11;
    private javax.swing.JButton btnLiber12;
    private javax.swing.JButton btnLiber2;
    private javax.swing.JButton btnLiber3;
    private javax.swing.JButton btnLiber4;
    private javax.swing.JButton btnLiber5;
    private javax.swing.JButton btnLiber6;
    private javax.swing.JButton btnLiber7;
    private javax.swing.JButton btnLiber8;
    private javax.swing.JButton btnLiber9;
    private javax.swing.JButton btnLo1;
    private javax.swing.JButton btnLo2;
    private javax.swing.JButton btnLo3;
    private javax.swing.JButton btnLo4;
    private javax.swing.JButton btnLo5;
    private javax.swing.JButton btnStart;
    private javax.swing.JButton btnStop;
    private javax.swing.JButton btnTamTinh;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnVip1;
    private javax.swing.JButton btnVip2;
    private javax.swing.JButton btnVip3;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JComboBox<String> cboCategory;
    private javax.swing.JComboBox<String> cboFood;
    private javax.swing.JComboBox<String> cboTableBida;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lbDateNow;
    private javax.swing.JLabel lbMaHoaDon;
    private javax.swing.JLabel lbTable;
    private javax.swing.JLabel lbTienGio;
    private javax.swing.JLabel lbTongTien;
    private javax.swing.JSpinner snpCount;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tbInfo;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtGiamGia;
    private javax.swing.JTextField txtPhiDichVu;
    private javax.swing.JTextField txtPhiKhac;
    private javax.swing.JTextField txtTimeStart;
    private javax.swing.JTextField txtTimeStop;
    // End of variables declaration//GEN-END:variables
}
