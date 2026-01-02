package view;

import entity.ChiTietDatBan;
import DAO.ChiTietDatBanDAO;
import util.XImage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class FrmDatBan extends JPanel {

    private JList<ChiTietDatBan> listMenu;
    private DefaultListModel<ChiTietDatBan> listModel;

    private JTextField txtTenMon, txtGia, txtMaMenu;
    private JSpinner spnSoLuong;

    // --- KHAI BÁO CÁC COMPONENT GIAO DIỆN ---
    private JPanel pnlSoDoBan; // Panel chứa các nút bàn
    private JButton selectedBtnBan = null; // Lưu nút bàn đang chọn để đổi màu

    // --- QUAN TRỌNG: DANH SÁCH LƯU TRẠNG THÁI ĐÃ THANH TOÁN ---
    private Set<String> listBanDaThanhToan = new HashSet<>();
    // ----------------------------------------------------------

    private JButton btnThem, btnThanhToan, btnXoa, btnCapNhat;

    private JTable tblGioHang;
    private DefaultTableModel tableModel;
    private JLabel lblTongTien;
    private JPanel pnlLeft;

    private ChiTietDatBanDAO dao;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    // Mặc định chọn bàn 1 khi mở lên
    private String currentMaDatBan = "DB001";

    public FrmDatBan() {
        dao = new ChiTietDatBanDAO();
        initComponents();
        loadDataToMenu();

        // Load mặc định bàn 1
        loadDataToTable();
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

        // Gọi hàm tạo nút bàn
        initListTables();

        pnlLeft.add(pnlSoDoBan, BorderLayout.NORTH);

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
        listMenu.setFixedCellHeight(80);
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

    // --- HÀM TẠO SƠ ĐỒ BÀN (Đã cập nhật logic dấu tích) ---
    private void initListTables() {
        pnlSoDoBan.removeAll();

        List<String> listBanCoKhach = dao.getDanhSachMaDatBanCoMon();

        for (int i = 1; i <= 10; i++) {
            String maBan = String.format("DB%03d", i);
            String tenBan = "Bàn " + i;

            JButton btnBan = new JButton();

            // Icon bàn (Bạn nhớ đổi tên file ảnh cho đúng với file bạn có trong src/image)
            ImageIcon icon = XImage.getResizedIcon("logo_cafe.png", 50, 50);
            if (icon != null) {
                btnBan.setIcon(icon);
            }

            btnBan.setVerticalTextPosition(SwingConstants.BOTTOM);
            btnBan.setHorizontalTextPosition(SwingConstants.CENTER);
            btnBan.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // =================================================================
            // --- LOGIC MỚI: XỬ LÝ TRẠNG THÁI & DẤU TÍCH ---
            // =================================================================
            boolean isCoKhach = listBanCoKhach.contains(maBan);
            boolean isDaThanhToan = listBanDaThanhToan.contains(maBan);

            if (isCoKhach) {
                if (isDaThanhToan) {
                    // TRƯỜNG HỢP 1: Có khách + Đã thanh toán -> Xanh lá + Dấu tích
                    btnBan.setBackground(new Color(144, 238, 144));
                    btnBan.setText("<html><center>" + tenBan + "<br>"
                                + "<b style='color:#006400; font-size:14px;'>Đã TT &#10004;</b>"
                                + "</center></html>");
                } else {
                    // TRƯỜNG HỢP 2: Có khách + Chưa thanh toán -> Hồng
                    btnBan.setBackground(new Color(255, 182, 193));
                    btnBan.setText("<html><center>" + tenBan + "<br>"
                                + "<b style='color:red'>(Có khách)</b>"
                                + "</center></html>");
                }
            } else {
                // TRƯỜNG HỢP 3: Bàn trống -> Trắng
                // Nếu bàn trống thì xóa luôn trạng thái thanh toán (để đón khách mới)
                listBanDaThanhToan.remove(maBan);

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
                    // Khi bấm vào thì chỉ load dữ liệu, màu sắc để initListTables lo
                    currentMaDatBan = maBan;
                    loadDataToTable();

                    String trangThaiText = isDaThanhToan ? " (Đã TT)" : "";
                    ((javax.swing.border.TitledBorder) ((JScrollPane) tblGioHang.getParent().getParent()).getBorder())
                                .setTitle("Chi tiết món ăn - " + tenBan + " (" + maBan + ")" + trangThaiText);

                    // Để làm nổi bật nút đang chọn, ta có thể set tạm màu xanh dương
                    // (Lưu ý: khi initListTables chạy lại, màu này sẽ mất, nhưng không sao)
                    resetMauCacBanKhac();
                    btnBan.setBackground(new Color(173, 216, 230));
                    selectedBtnBan = btnBan;

                    pnlLeft.repaint();
                }
            });

            // Mặc định select bàn 1 nếu mới mở
            if (i == 1 && selectedBtnBan == null) {
                selectedBtnBan = btnBan;
                btnBan.setBackground(new Color(173, 216, 230));
            }

            pnlSoDoBan.add(btnBan);
        }

        pnlSoDoBan.revalidate();
        pnlSoDoBan.repaint();
    }

    // Hàm phụ để reset màu nền các nút về trạng thái gốc (tránh lỗi hiển thị khi click)
    private void resetMauCacBanKhac() {
        // Thực tế hàm initListTables đã vẽ lại màu rất chuẩn rồi.
        // Nhưng nếu muốn đổi màu tức thì khi click thì gọi lại initListTables() là an toàn nhất.
        initListTables();
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
                ChiTietDatBan ctdb = new ChiTietDatBan(maCTMoi, currentMaDatBan, maMon, selectedItem.getTenMon(), soLuongThem, selectedItem.getDonGia());
                if (dao.insertChiTiet(ctdb)) {
                    loadDataToTable();
                    initListTables(); // Cập nhật màu bàn ngay khi thêm món
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
                        initListTables(); // Cập nhật lại màu bàn (nếu xóa hết món thì về màu trắng)
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Chọn món để xóa!");
            }
        });

        // Event Thanh Toán (Đã sửa logic)
        btnThanhToan.addActionListener(e -> {
            // Kiểm tra bàn trống
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Bàn này đang trống, không thể thanh toán!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                        "Xác nhận thanh toán cho " + currentMaDatBan + "?\nTổng tiền: " + lblTongTien.getText(),
                        "Thanh Toán", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                // 1. Thêm bàn hiện tại vào danh sách "Đã thanh toán"
                listBanDaThanhToan.add(currentMaDatBan);

                // 2. Load lại sơ đồ bàn để nó hiện dấu tích lên
                initListTables();

                JOptionPane.showMessageDialog(this, "Đã ghi nhận thanh toán! Bàn hiện dấu tích xanh.");
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> new FrmDatBan().setVisible(true));
    }
}

// --- CLASS VẼ GIAO DIỆN CHO JLIST (Renderer) ---
//class MenuRenderer extends DefaultListCellRenderer {
//
//    @Override
//    public Component getListCellRendererComponent(
//                JList<?> list, Object value, int index,
//                boolean isSelected, boolean cellHasFocus) {
//
//        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//
//        if (value instanceof ChiTietDatBan) {
//            ChiTietDatBan item = (ChiTietDatBan) value;
//
//            java.text.NumberFormat vnMoney = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("vi", "VN"));
//
//            String html = "<html><div style='padding: 5px;'>"
//                        + "<b style='font-size:12px; color:blue;'>" + item.getTenMon() + "</b><br/>"
//                        + "<i style='color:red;'>" + vnMoney.format(item.getDonGia()) + "</i>"
//                        + "</div></html>";
//            setText(html);
//
//            setIcon(XImage.getResizedIcon(item.getMaMenu(), 60, 60));
//
//
//            setIconTextGap(15);
//            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
//
//            if (isSelected) {
//                setBackground(new Color(200, 230, 255));
//                setForeground(Color.BLACK);
//            } else {
//                setBackground(Color.WHITE);
//                setForeground(Color.BLACK);
//            }
//        }
//        return this;
//    }
//}
