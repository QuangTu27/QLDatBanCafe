package view;

import DAO.DatBanDao;
import Dialog.DlgDatBan;
import entity.Ban;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import entity.DatBan;

public class FrmDatBan extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTimKiem;
    private JButton btnThem, btnKetThuc, btnXoa, btnBatDau, btnLamMoi;

    private DatBanDao dao = new DatBanDao();
    private List<DatBan> fullList = new ArrayList<>();

    public FrmDatBan() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1000, 650));

        initToolbar();
        initTable();
        loadTable();
        addEvents();
    }

    private void initToolbar() {
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnlTop.setPreferredSize(new Dimension(0, 130));

        // Panel Chức năng bên trái
        JPanel pnlChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlChucNang.setBackground(Color.WHITE);
        pnlChucNang.setBorder(createGroupBorder("Chức năng"));
        // Trong hàm initToolbar()
        btnBatDau = createToolButton("Bắt đầu", new Color(52, 152, 219)); // Màu xanh dương
        btnThem = createToolButton("Thêm", new Color(46, 204, 113));
        btnKetThuc = createToolButton("Kết thúc", new Color(241, 196, 15));
        btnXoa = createToolButton("Xóa", new Color(231, 76, 60));
        pnlChucNang.add(btnBatDau);
        pnlChucNang.add(btnThem);
        pnlChucNang.add(btnKetThuc);
        pnlChucNang.add(btnXoa);

        // Panel Bộ lọc bên phải
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlTimKiem.setBackground(Color.WHITE);
        pnlTimKiem.setBorder(createGroupBorder("Bộ lọc danh sách đặt bàn"));

        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(250, 40));
        txtTimKiem.setFont(font);

        btnLamMoi = createToolButton("Làm mới", new Color(52, 152, 219));

        JLabel lblTim = new JLabel("Tìm kiếm khách/SĐT:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlTimKiem.add(lblTim);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnLamMoi);

        pnlTop.add(pnlChucNang, BorderLayout.WEST);
        pnlTop.add(pnlTimKiem, BorderLayout.EAST);

        add(pnlTop, BorderLayout.NORTH);
    }

    private TitledBorder createGroupBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1), title);
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        border.setTitleColor(new Color(100, 100, 100));
        return border;
    }

    private JButton createToolButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
                btn.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(color);
            }
        });
        return btn;
    }

    private void initTable() {
        // Đã thêm cột Giờ vào thực tế theo yêu cầu trước đó
        String[] cols = {"Mã đặt", "Tên khách", "Số điện thoại", "Bàn", "Giờ hẹn", "Giờ đặt", "Trạng thái"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setSelectionBackground(new Color(220, 235, 250));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(0, 20, 20, 20));
        scroll.getViewport().setBackground(Color.WHITE);
        // SỬA LỖI: Add đúng biến scroll vào Center
        add(scroll, BorderLayout.CENTER);
    }

    private void hienThiDanhSach(List<DatBan> list) {
        model.setRowCount(0);
        for (DatBan db : list) {
            model.addRow(new Object[]{
                db.getMaDatBan(),
                db.getTenKhachHang(),
                db.getSoDienThoai(),
                db.getMaBan(),
                db.getThoiGianHen(),
                db.getThoiGianBatDau(), // Giờ vào thực tế
                db.getTrangThai()
            });
        }
    }

    private void loadTable() {
        fullList = dao.selectAll();
        hienThiDanhSach(fullList);
    }

    private void addEvents() {
        // Nút Thêm
        btnThem.addActionListener(e -> {
            openDialogThem();
        });

        // Tìm kiếm khi gõ phím
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                timKiem();
            }
        });

        // Nút Làm mới
        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            loadTable();
        });

        // Nút Xóa (Hủy đơn)
        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
                return;
            }

            // Lấy Mã đặt bàn từ cột 0 và Mã bàn từ cột 3
            String maDB = model.getValueAt(row, 0).toString();
            String maBan = model.getValueAt(row, 3).toString();

            int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn hủy đặt bàn " + maDB + " và giải phóng bàn " + maBan + "?",
                        "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.delete(maDB, maBan)) {
                    JOptionPane.showMessageDialog(this, "Đã hủy đơn và giải phóng bàn thành công!");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa dữ liệu!");
                }
            }
        });

        // Nút Kết thúc bàn (Quan trọng)
        btnKetThuc.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn đặt bàn để kết thúc!");
                return;
            }

            String maDB = table.getValueAt(row, 0).toString();
            String maBan = table.getValueAt(row, 3).toString();
            String trangThai = table.getValueAt(row, 6).toString();

            if (trangThai.equals("Hoàn tất")) {
                JOptionPane.showMessageDialog(this, "Đơn này đã hoàn tất rồi!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                        "Xác nhận kết thúc và trả bàn " + maBan + "?", "Thông báo", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.ketThucDatBan(maDB, maBan)) { // Sử dụng hàm đã viết trong DAO
                    JOptionPane.showMessageDialog(this, "Đã kết thúc và giải phóng bàn!");
                    loadTable();
                    // Nếu có trang chủ, gọi refresh ở đây
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xử lý!");
                }
            }
        });
        // Trong hàm addEvents()
        btnBatDau.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn đặt bàn!");
                return;
            }

            // --- BƯỚC 1: TỰ ĐỘNG TÌM CHỈ SỐ CỘT THEO TÊN (Tránh lỗi Index) ---
            int colTrangThai = -1;
            int colMaBan = -1;
            int colMaDB = -1;

            for (int i = 0; i < table.getColumnCount(); i++) {
                String colName = table.getColumnName(i).toLowerCase();
                if (colName.contains("trạng thái")) {
                    colTrangThai = i;
                }
                if (colName.contains("bàn")) {
                    colMaBan = i;
                }
                if (colName.contains("mã đặt")) {
                    colMaDB = i;
                }
            }

            // --- BƯỚC 2: LẤY DỮ LIỆU VÀ XỬ LÝ CHUỖI ---
            String maDB = table.getValueAt(row, colMaDB).toString();
            String maBan = table.getValueAt(row, colMaBan).toString();

            // Lấy trạng thái, xóa khoảng trắng và chuẩn hóa Unicode
            Object val = table.getValueAt(row, colTrangThai);
            String trangThai = (val != null) ? val.toString().trim() : "";

            // In ra Console để bạn tự kiểm tra nếu vẫn lỗi
            System.out.println("DEBUG: MaDB=" + maDB + " | TrangThai=[" + trangThai + "]");

            // --- BƯỚC 3: SO SÁNH (Dùng equalsIgnoreCase để an toàn tuyệt đối) ---
            if (!trangThai.equalsIgnoreCase("Đã đặt")) {
                JOptionPane.showMessageDialog(this,
                            "Lỗi: Đơn này đang ở trạng thái '" + trangThai + "'\n"
                            + "Hệ thống chỉ cho phép bắt đầu với đơn hàng 'Đã đặt'.");
                return;
            }

            // --- BƯỚC 4: XÁC NHẬN VÀ GỌI DAO ---
            int confirm = JOptionPane.showConfirmDialog(this,
                        "Xác nhận khách đã vào bàn " + maBan + "?", "Khách vào bàn",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.batDauPhucVu(maDB, maBan)) {
                    JOptionPane.showMessageDialog(this, "Thành công: Trạng thái bàn đã chuyển sang 'Có khách'");
                    loadTable(); // Tải lại danh sách đặt bàn

                    // Cập nhật sơ đồ bàn ở trang chủ (nếu có)
                    Window parentWindow = SwingUtilities.getWindowAncestor(this);
                    if (parentWindow instanceof FrmMain) {
                        ((FrmMain) parentWindow).loadSoDoBan();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!");
                }
            }
        });
    }

    private void timKiem() {
        String keyword = txtTimKiem.getText().trim();
        List<DatBan> ketQua = dao.searchByCustomerName(keyword);
        hienThiDanhSach(ketQua);
    }

    private void openDialogThem() {
        // Sửa dòng này: Bỏ chữ Dialog. phía trước nếu đã import
        DlgDatBan dlg = new DlgDatBan(javax.swing.SwingUtilities.getWindowAncestor(this));
        dlg.setVisible(true);

        if (dlg.getResult()) {
            loadTable();
        }
    }
}
