package view;

import entity.ChiTietDatBan;
import DAO.ChiTietDatBanDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class FrmDatBan extends JFrame {

    private JList<ChiTietDatBan> listMenu;
    private DefaultListModel<ChiTietDatBan> listModel;

    private JTextField txtTenMon, txtGia, txtMaMenu;
    private JSpinner spnSoLuong;
    
    // Khai báo nút
    private JButton btnThem, btnThanhToan, btnXoa, btnCapNhat;

    private JTable tblGioHang;
    private DefaultTableModel tableModel;
    private JLabel lblTongTien;

    private ChiTietDatBanDAO dao;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    
    private String currentMaDatBan = "DB001"; // Mã bàn đang chọn

    public FrmDatBan() {
        dao = new ChiTietDatBanDAO();
        initComponents();
        loadDataToMenu();
        loadDataToTable();
        addEvents();
    }

    private void initComponents() {
        setTitle("Quản Lý Đặt Bàn - Quán Cafe");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2, 10, 10));

        // --- PANEL TRÁI: DANH SÁCH ĐẶT BÀN ---
        JPanel pnlLeft = new JPanel(new BorderLayout());
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Danh Sách Đặt Bàn"));

        String[] headers = {"Mã CT", "Mã Đặt Bàn", "Mã Menu", "Số Lượng", "Đơn Giá"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tblGioHang = new JTable(tableModel);
        tblGioHang.setRowHeight(25);
        tblGioHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
        tblGioHang.getColumnModel().getColumn(0).setPreferredWidth(80);  
        tblGioHang.getColumnModel().getColumn(1).setPreferredWidth(80);  
        tblGioHang.getColumnModel().getColumn(2).setPreferredWidth(80);  
        tblGioHang.getColumnModel().getColumn(3).setPreferredWidth(60);  
        tblGioHang.getColumnModel().getColumn(4).setPreferredWidth(100); 

        pnlLeft.add(new JScrollPane(tblGioHang), BorderLayout.CENTER);

        
        JPanel pnlFooterLeft = new JPanel(new GridLayout(2, 1));
        
        JPanel pnlButtonsLeft = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnXoa = new JButton("Xóa Món");
        btnXoa.setBackground(Color.RED);
        btnXoa.setForeground(Color.BLACK);
        pnlButtonsLeft.add(btnXoa);

        JPanel pnlThanhToan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTien = new JLabel("Tổng tiền: 0 ₫");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongTien.setForeground(Color.BLACK);
        
        btnThanhToan = new JButton("THANH TOÁN / LƯU");
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 14));
        btnThanhToan.setBackground(new Color(0, 153, 76));
        btnThanhToan.setForeground(Color.BLACK);
        
        pnlThanhToan.add(lblTongTien);
        pnlThanhToan.add(Box.createHorizontalStrut(20));
        pnlThanhToan.add(btnThanhToan);

        pnlFooterLeft.add(pnlButtonsLeft);
        pnlFooterLeft.add(pnlThanhToan);
        pnlLeft.add(pnlFooterLeft, BorderLayout.SOUTH);

        
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setBorder(BorderFactory.createTitledBorder("Danh Sách Thực Đơn"));

        listModel = new DefaultListModel<>();
        listMenu = new JList<>(listModel);
        listMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pnlRight.add(new JScrollPane(listMenu), BorderLayout.CENTER);

        JPanel pnlChiTiet = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaMenu = new JTextField(15); txtMaMenu.setEditable(false);
        txtTenMon = new JTextField(15); txtTenMon.setEditable(false);
        txtGia = new JTextField(15); txtGia.setEditable(false);
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        
        btnThem = new JButton("THÊM MÓN");
        // Nút Cập nhật màu cam
        btnCapNhat = new JButton("CẬP NHẬT SL");
        btnCapNhat.setBackground(Color.ORANGE);

        gbc.gridx=0; gbc.gridy=0; pnlChiTiet.add(new JLabel("Mã Món:"), gbc);
        gbc.gridx=1; gbc.gridy=0; pnlChiTiet.add(txtMaMenu, gbc);

        gbc.gridx=0; gbc.gridy=1; pnlChiTiet.add(new JLabel("Tên Món:"), gbc);
        gbc.gridx=1; gbc.gridy=1; pnlChiTiet.add(txtTenMon, gbc);

        gbc.gridx=0; gbc.gridy=2; pnlChiTiet.add(new JLabel("Đơn Giá:"), gbc);
        gbc.gridx=1; gbc.gridy=2; pnlChiTiet.add(txtGia, gbc);

        gbc.gridx=0; gbc.gridy=3; pnlChiTiet.add(new JLabel("Số Lượng:"), gbc);
        gbc.gridx=1; gbc.gridy=3; pnlChiTiet.add(spnSoLuong, gbc);

        JPanel pnlBtnGroup = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBtnGroup.add(btnThem);
        pnlBtnGroup.add(btnCapNhat);

        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=2; 
        pnlChiTiet.add(pnlBtnGroup, gbc);

        pnlRight.add(pnlChiTiet, BorderLayout.SOUTH);

        add(pnlLeft);
        add(pnlRight);
    }

    private void loadDataToMenu() {
        List<ChiTietDatBan> menuList = dao.getDanhSachMenu();
        listModel.clear();
        if (menuList != null) {
            for (ChiTietDatBan mon : menuList) {
                listModel.addElement(mon);
            }
        }
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<ChiTietDatBan> listOrder = dao.getChiTietByMaDatBan(currentMaDatBan);
        if (listOrder != null) {
            for (ChiTietDatBan ct : listOrder) {
                tableModel.addRow(new Object[]{
                    ct.getMaCTDatBan(),             
                    ct.getMaDatBan(),               
                    ct.getMaMenu(),                 
                    ct.getSoLuong(),                
                    currencyFormat.format(ct.getDonGia()) 
                });
            }
        }
        updateTongTien();
    }

    private void updateTongTien() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                int sl = (int) tableModel.getValueAt(i, 3);
                String donGiaStr = tableModel.getValueAt(i, 4).toString();
                Number donGiaNum = currencyFormat.parse(donGiaStr);
                total += sl * donGiaNum.doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        lblTongTien.setText("Tổng tiền: " + currencyFormat.format(total));
    }

    
    private String getTenMonByMa(String maMenu) {
        for (int i = 0; i < listModel.size(); i++) {
            ChiTietDatBan item = listModel.getElementAt(i);
            if (item.getMaMenu().equals(maMenu)) {
                return item.getTenMon();
            }
        }
        return "Món không xác định";
    }

    private void addEvents() {
        // 1. SỰ KIỆN CLICK BẢNG: Đổ dữ liệu xuống ô nhập liệu
        tblGioHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblGioHang.getSelectedRow();
                if (row != -1) {
                    String maMenu = tableModel.getValueAt(row, 2).toString();
                    int soLuong = (int) tableModel.getValueAt(row, 3);
                    String giaStr = tableModel.getValueAt(row, 4).toString();

                    txtMaMenu.setText(maMenu);
                    txtTenMon.setText(getTenMonByMa(maMenu));
                    txtGia.setText(giaStr);
                    spnSoLuong.setValue(soLuong);                   
                    listMenu.clearSelection();
                }
            }
        });

        
        listMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ChiTietDatBan item = listMenu.getSelectedValue();
                if (item != null) {
                    txtMaMenu.setText(item.getMaMenu());
                    txtTenMon.setText(item.getTenMon());
                    txtGia.setText(currencyFormat.format(item.getDonGia()));
                    spnSoLuong.setValue(1);
                    tblGioHang.clearSelection();
                }
            }
        });

       
        btnThem.addActionListener(e -> {
            ChiTietDatBan selectedItem = listMenu.getSelectedValue();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn từ thực đơn!");
                return;
            }

            int soLuongThem = (int) spnSoLuong.getValue();
            String maMon = selectedItem.getMaMenu();
            String maCTMoi = dao.taoMaCTMoi(); 
            boolean found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 2).equals(maMon)) { 
                    JOptionPane.showMessageDialog(this, "Món này đã có trong danh sách! Hãy dùng nút Cập Nhật.");
                    found = true;
                    tblGioHang.setRowSelectionInterval(i, i);
                    break;
                }
            }

            if (!found) {
                ChiTietDatBan ctdb = new ChiTietDatBan(maCTMoi, currentMaDatBan, maMon, selectedItem.getTenMon(), soLuongThem, selectedItem.getDonGia());
                
                if(dao.insertChiTiet(ctdb)) {
                    // Thêm vào bảng giao diện
                    tableModel.addRow(new Object[]{
                        maCTMoi, currentMaDatBan, maMon, soLuongThem, 
                        currencyFormat.format(selectedItem.getDonGia()) 
                    });
                    updateTongTien();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi thêm vào CSDL!");
                }
            }
        });
        btnCapNhat.addActionListener(e -> {
            int row = tblGioHang.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Chọn món trong bảng bên trái để cập nhật!");
                return;
            }
            
            String maCT = tableModel.getValueAt(row, 0).toString();
            int soLuongMoi = (int) spnSoLuong.getValue();
            
            if (dao.updateSoLuong(maCT, soLuongMoi)) {
                tableModel.setValueAt(soLuongMoi, row, 3);
                updateTongTien();
                JOptionPane.showMessageDialog(this, "Đã cập nhật số lượng!");
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật CSDL!");
            }
        });

        btnXoa.addActionListener(e -> {
            int row = tblGioHang.getSelectedRow();
            if (row != -1) {
                String maCT = tableModel.getValueAt(row, 0).toString();
                
                if(dao.deleteChiTiet(maCT)) {
                    tableModel.removeRow(row);
                    updateTongTien();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa khỏi CSDL!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chọn món để xóa!");
            }
        });

        btnThanhToan.addActionListener(e -> {
            loadDataToTable();
            JOptionPane.showMessageDialog(this, "Đã tải lại dữ liệu từ Server!");
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new FrmDatBan().setVisible(true));
    }
}