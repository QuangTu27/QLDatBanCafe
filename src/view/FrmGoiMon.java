/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import DAO.ChiTietDatBanDAO;
import DAO.DatBanDao;
import entity.Ban;
import entity.ChiTietDatBan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component; // Mới thêm
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import util.XImage;

/**
 *
 * @author Admin
 */
public class FrmGoiMon extends JPanel {

    private JList<ChiTietDatBan> listMenu;
    private DefaultListModel<ChiTietDatBan> listModel;

    private JTextField txtTenMon, txtGia, txtMaMenu;
    private JSpinner spnSoLuong;

    // --- KHAI BÁO CÁC COMPONENT GIAO DIỆN ---
    private JPanel pnlSoDoBan; // Panel chứa các nút bàn
    private JButton selectedBtnBan = null; // Lưu nút bàn đang chọn để đổi màu

    // [ĐÃ XÓA] listBanDaThanhToan vì dùng Database rồi
    // ----------------------------------------------------------
    private JButton btnThem, btnThanhToan, btnXoa, btnCapNhat;

    private JTable tblGioHang;
    private DefaultTableModel tableModel;
    private JLabel lblTongTien;
    private JPanel pnlLeft;

    private ChiTietDatBanDAO dao;
    private DatBanDao daoDatBan = new DatBanDao();
    private DAO.BanDao banDao = new DAO.BanDao(); // Khai báo thêm Dao quản lý bàn
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    private String currentMaDatBan = ""; // Để trống ban đầu
    private String currentMaBan = "";
    
    public FrmGoiMon() {
        dao = new ChiTietDatBanDAO();
        initComponents();
        loadDataToMenu();

        // Gọi hàm tạo nút bàn
        initListTables();

        // Tự động click bàn đầu tiên nếu có (Logic an toàn)
        if (pnlSoDoBan.getComponentCount() > 0) {
            Component c = pnlSoDoBan.getComponent(0);
            if (c instanceof JButton) {
                ((JButton) c).doClick();
            }
        }
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1000, 650));
        setLayout(new GridLayout(1, 2, 10, 10));

        // ==============================================================================
        // --- PANEL TRÁI: CHỨA SƠ ĐỒ BÀN + BẢNG ODER ---
        // ==============================================================================
        pnlLeft = new JPanel(new BorderLayout(5, 5));
        pnlLeft.setBorder(BorderFactory.createTitledBorder("Khu Vực Bàn & Gọi Món"));

        // 1. KHỞI TẠO SƠ ĐỒ BÀN (NORTH)
        pnlSoDoBan = new JPanel(new GridLayout(2, 5, 10, 10));
        pnlSoDoBan.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        pnlSoDoBan.setPreferredSize(new Dimension(0, 220));

        // Hàm initListTables sẽ gọi sau trong Constructor
        pnlLeft.add(new JScrollPane(pnlSoDoBan), BorderLayout.NORTH); // Thêm JScrollPane cho an toàn

        // 2. BẢNG GIỎ HÀNG (CENTER)
        String[] headers = {"Mã CT", "Mã Đặt Bàn", "Mã Menu", "Số Lượng", "Đơn Giá"};
        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblGioHang = new JTable(tableModel);
        tblGioHang.setRowHeight(30);
        tblGioHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tblGioHang.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblGioHang.getColumnModel().getColumn(1).setPreferredWidth(60);
        tblGioHang.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblGioHang.getColumnModel().getColumn(3).setPreferredWidth(50);
        tblGioHang.getColumnModel().getColumn(4).setPreferredWidth(90);

        JScrollPane scrTable = new JScrollPane(tblGioHang);
        scrTable.setBorder(BorderFactory.createTitledBorder("Chi tiết món ăn của bàn"));
        pnlLeft.add(scrTable, BorderLayout.CENTER);

        // 3. FOOTER TRÁI (SOUTH)
        JPanel pnlFooterLeft = new JPanel(new GridLayout(2, 1));
        JPanel pnlButtonsLeft = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnXoa = new JButton("Xóa Món");
        btnXoa.setBackground(Color.WHITE);
        btnXoa.setForeground(Color.BLACK);
        pnlButtonsLeft.add(btnXoa);

        JPanel pnlThanhToan = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTien = new JLabel("Tổng tiền: 0 ₫");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 18));
        lblTongTien.setForeground(Color.BLUE);

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

        // ==============================================================================
        // --- PANEL PHẢI: MENU ---
        // ==============================================================================
        JPanel pnlRight = new JPanel(new BorderLayout());
        pnlRight.setBorder(BorderFactory.createTitledBorder("Danh Sách Món"));

        listModel = new DefaultListModel<>();
        listMenu = new JList<>(listModel);
        listMenu.setCellRenderer(new MenuRenderer());
        listMenu.setFixedCellHeight(95);
        listMenu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        pnlRight.add(new JScrollPane(listMenu), BorderLayout.CENTER);

        JPanel pnlChiTiet = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaMenu = new JTextField(15);
        txtMaMenu.setEditable(false);
        txtTenMon = new JTextField(15);
        txtTenMon.setEditable(false);
        txtGia = new JTextField(15);
        txtGia.setEditable(false);
        spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        btnThem = new JButton("THÊM MÓN");
        btnThem.setBackground(new Color(51, 153, 255));
        btnThem.setForeground(Color.BLACK);
        btnCapNhat = new JButton("CẬP NHẬT SL");
        btnCapNhat.setBackground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlChiTiet.add(new JLabel("Mã Món:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        pnlChiTiet.add(txtMaMenu, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlChiTiet.add(new JLabel("Tên Món:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        pnlChiTiet.add(txtTenMon, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlChiTiet.add(new JLabel("Đơn Giá:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        pnlChiTiet.add(txtGia, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        pnlChiTiet.add(new JLabel("Số Lượng:"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        pnlChiTiet.add(spnSoLuong, gbc);

        JPanel pnlBtnGroup = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBtnGroup.add(btnThem);
        pnlBtnGroup.add(btnCapNhat);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        pnlChiTiet.add(pnlBtnGroup, gbc);

        pnlRight.add(pnlChiTiet, BorderLayout.SOUTH);

        add(pnlLeft);
        add(pnlRight);

        addEvents();
    }

    // --- HÀM TẠO SƠ ĐỒ BÀN (SỬA LOGIC LẤY TỪ DB) ---
    private void initListTables() {
        pnlSoDoBan.removeAll();

        // 1. Dùng GetListBan để lấy cả cột TrangThai từ DB
        List<Ban> danhSachBan = banDao.selectAll();

        if (danhSachBan == null || danhSachBan.isEmpty()) {
            // Xử lý khi không có dữ liệu
            JLabel lbl = new JLabel("Chưa có bàn nào trong CSDL");
            pnlSoDoBan.add(lbl);
            pnlSoDoBan.revalidate();
            pnlSoDoBan.repaint();
            return;
        }

        for (Ban ban : danhSachBan) {
            String maBan = ban.getMaBan();
            String tenBan = ban.getTenBan();

            JButton btnBan = new JButton();

            btnBan.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnBan.setHorizontalTextPosition(SwingConstants.CENTER);
            btnBan.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // --- LOGIC MỚI: CHECK TRANG THÁI TỪ DB ---
            String status = ban.getTrangThai();
            if (status == null) {
                status = "Trống";
            }
            final String finalStatus = status.trim(); // Biến final để dùng trong sự kiện

            if (finalStatus.equalsIgnoreCase("Có khách")) {
                btnBan.setBackground(new Color(255, 182, 193)); // Hồng
                btnBan.setText("<html><center>" + tenBan + "<br>"
                            + "<b style='color:red'>(Có khách)</b>"
                            + "</center></html>");
            } else if (finalStatus.equalsIgnoreCase("Đã đặt")) {
                btnBan.setBackground(new Color(255, 255, 153)); // Vàng
                btnBan.setText("<html><center>" + tenBan + "<br>"
                            + "<b style='color:#8B8000'>(Đã đặt)</b>"
                            + "</center></html>");
            } else {
                // Mặc định: Trống
                btnBan.setBackground(Color.WHITE);
                btnBan.setText("<html><center>" + tenBan + "<br>"
                            + "<i style='color:green'>(Trống)</i>"
                            + "</center></html>");
            }
            // =================================================================

            // Sự kiện bấm vào bàn
            btnBan.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Logic cũ của bạn: Lấy mã đặt bàn thực tế (nếu có)
                    // Nếu "Trống" thì mã đặt bàn thực tế có thể là null hoặc chính là mã bàn
                    // Mình giữ nguyên logic kiểm tra này để không phá vỡ quy trình

                    currentMaDatBan = maBan; // Mặc định lấy mã bàn (B001)
// Nếu đang có khách, thử tìm mã Booking
                    if (!finalStatus.equalsIgnoreCase("Trống")) {
                        String maDBThucTe = dao.getMaDatBanDangHoatDong(ban.getMaBan());
                        if (maDBThucTe != null) {
                            currentMaDatBan = maDBThucTe;
                        }
                    }

                    loadDataToTable();

                    // Cập nhật tiêu đề bảng
                    try {
                        String title = "Chi tiết món ăn - " + tenBan + " (" + finalStatus + ")";
                        ((javax.swing.border.TitledBorder) ((JScrollPane) tblGioHang.getParent().getParent()).getBorder()).setTitle(title);
                    } catch (Exception ex) {
                    }

                    // Đổi màu nút đang chọn (Xanh dương)
                    initListTables_GiuMauChon(btnBan);
                    selectedBtnBan = btnBan;
                    pnlLeft.repaint();
                }
            });

            pnlSoDoBan.add(btnBan);
        }

        pnlSoDoBan.revalidate();
        pnlSoDoBan.repaint();
    }

    // Hàm phụ để reset màu nền các nút về trạng thái gốc (giữ nút đang chọn màu xanh)
    private void initListTables_GiuMauChon(JButton btnDangChon) {
        for (Component c : pnlSoDoBan.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                if (btn == btnDangChon) {
                    btn.setBackground(new Color(173, 216, 230)); // Xanh dương (Selected)
                } else {
                    // Trả về màu gốc dựa trên text
                    String text = btn.getText();
                    if (text.contains("Có khách")) {
                        btn.setBackground(new Color(255, 182, 193));
                    } else if (text.contains("Đã đặt")) {
                        btn.setBackground(new Color(255, 255, 153));
                    } else {
                        btn.setBackground(Color.WHITE);
                    }
                }
            }
        }
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
                    ct.getMaCTDatBan(), ct.getMaDatBan(), ct.getMaMenu(),
                    ct.getSoLuong(), currencyFormat.format(ct.getDonGia())
                });
            }
        }
        updateTongTien();
    }

    private void updateTongTien() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                int sl = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
                String donGiaStr = tableModel.getValueAt(i, 4).toString();
                Number donGiaNum = currencyFormat.parse(donGiaStr);
                total += sl * donGiaNum.doubleValue();
            } catch (Exception e) {
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
        // Event click bảng giỏ hàng
        tblGioHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblGioHang.getSelectedRow();
                if (row != -1) {
                    txtMaMenu.setText(tableModel.getValueAt(row, 2).toString());
                    txtTenMon.setText(getTenMonByMa(txtMaMenu.getText()));
                    txtGia.setText(tableModel.getValueAt(row, 4).toString());
                    spnSoLuong.setValue(Integer.parseInt(tableModel.getValueAt(row, 3).toString()));
                    listMenu.clearSelection();
                }
            }
        });

        // Event click List Menu
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

        // Event Thêm
        btnThem.addActionListener(e -> {
            ChiTietDatBan selectedItem = listMenu.getSelectedValue();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món ăn từ thực đơn bên phải!");
                return;
            }
            int soLuongThem = (int) spnSoLuong.getValue();
            String maMon = selectedItem.getMaMenu();
            String maCTMoi = dao.taoMaCTMoi();

            boolean found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 2).equals(maMon)) {
                    JOptionPane.showMessageDialog(this, "Món này đã có trong bàn " + currentMaDatBan + "! Hãy dùng nút Cập Nhật.");
                    found = true;
                    tblGioHang.setRowSelectionInterval(i, i);
                    break;
                }
            }
            if (!found) {
                ChiTietDatBan ctdb = new ChiTietDatBan(
                            maCTMoi,
                            currentMaDatBan,
                            maMon,
                            selectedItem.getTenMon(),
                            soLuongThem,
                            selectedItem.getDonGia(),
                            selectedItem.getHinhAnh()
                );

                if (dao.insertChiTiet(ctdb)) {
                    // --- UPDATE DATABASE: CHUYỂN TRẠNG THÁI THÀNH CÓ KHÁCH ---
                    banDao.CapNhatTrangThaiBan(currentMaDatBan, "Có khách");

                    loadDataToTable();
                    initListTables(); // Vẽ lại màu bàn
                }
            }
        });

        // Event Cập nhật
        btnCapNhat.addActionListener(e -> {
            int row = tblGioHang.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Chọn món trong giỏ hàng (bên trái) để cập nhật!");
                return;
            }
            String maCT = tableModel.getValueAt(row, 0).toString();
            if (dao.updateSoLuong(maCT, (int) spnSoLuong.getValue())) {
                loadDataToTable();
                JOptionPane.showMessageDialog(this, "Đã cập nhật số lượng!");
            }
        });

        // Event Xóa
        btnXoa.addActionListener(e -> {
            int row = tblGioHang.getSelectedRow();
            if (row != -1) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn muốn xóa món này khỏi bàn " + currentMaDatBan + "?");
                if (confirm == JOptionPane.YES_OPTION) {
                    if (dao.deleteChiTiet(tableModel.getValueAt(row, 0).toString())) {
                        loadDataToTable();
                        // Nếu xóa hết món có thể check để trả bàn về Trống (Tùy chọn)
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chọn món để xóa!");
            }
        });

        // Event Thanh Toán (LOGIC MỚI: RESET DB)
        // Event Thanh Toán trong addEvents()
        btnThanhToan.addActionListener(e -> {

            // 1. Kiểm tra có đơn đang hoạt động không
            if (currentMaDatBan == null || currentMaDatBan.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chưa chọn bàn để thanh toán!");
                return;
            }

            // 2. Kiểm tra có món ăn không
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Bàn này chưa gọi món!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Xác nhận thanh toán đơn: " + currentMaDatBan
                        + "\n" + lblTongTien.getText(),
                        "Xác nhận thanh toán",
                        JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // 3. KẾT THÚC ĐƠN + GIẢI PHÓNG BÀN (Transaction)
            boolean success = daoDatBan.ketThucDatBan(currentMaDatBan, currentMaBan);

            if (success) {
                JOptionPane.showMessageDialog(this, "Thanh toán thành công!");

                // 4. Reset giao diện
                currentMaDatBan = "";
                currentMaBan = "";

                tableModel.setRowCount(0);
                lblTongTien.setText("Tổng tiền: 0 ₫");

                // 5. Vẽ lại sơ đồ bàn → đổi màu Trống
                initListTables();

            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi thanh toán!");
            }
        });

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        // Test nhanh
        javax.swing.JFrame f = new javax.swing.JFrame();
        f.add(new FrmGoiMon());
        f.pack();
        f.setVisible(true);
    }
}
