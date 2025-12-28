package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import view.FrmKhachHang;
import view.FrmTaiKhoan;
import util.Auth;

public class FrmMain extends JFrame {

    private JPanel pnMenu, pnContent, pnBan;

    // --- BẢNG MÀU GIỐNG ẢNH MẪU ---
    // Màu xanh lá chủ đạo (Giống ảnh)
    private final Color COL_SIDEBAR_BG = new Color(88, 175, 87);
    // Màu xanh đậm hơn cho nút đang chọn hoặc hover
    private final Color COL_MENU_HOVER = new Color(0, 150, 136);
    // Màu chữ trắng
    private final Color COL_TEXT = Color.WHITE;

    // Màu bàn ăn
    private final Color COL_BAN_TRONG = new Color(46, 204, 113);
    private final Color COL_BAN_COKHACH = new Color(231, 76, 60);
    private final Color COL_BAN_DADAT = new Color(241, 196, 15);

    public FrmMain() {
        initUI();
    }

    private void initUI() {
        setTitle("Hệ thống Quản lý Cafe");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. MENU BÊN TRÁI (SIDEBAR)
        initSidebar();

        // 2. NỘI DUNG BÊN PHẢI (CONTENT)
        pnContent = new JPanel(new BorderLayout());
        pnContent.setBackground(new Color(245, 245, 245)); // Màu nền xám nhạt

        // 2a. Header trắng bên phải (Tùy chọn)
        JPanel pnHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnHeader.setBackground(Color.WHITE);
        pnHeader.setPreferredSize(new Dimension(0, 50));
        pnHeader.add(new JLabel("Xin chào!"));
        pnContent.add(pnHeader, BorderLayout.NORTH);

        // 2b. Sơ đồ bàn
        initSoDoBan();

        add(pnMenu, BorderLayout.WEST);
        add(pnContent, BorderLayout.CENTER);
    }

    // ===== PHẦN QUAN TRỌNG NHẤT: SIDEBAR GIỐNG MẪU =====
    private void initSidebar() {
        pnMenu = new JPanel();
        pnMenu.setPreferredSize(new Dimension(260, 0));
        pnMenu.setBackground(COL_SIDEBAR_BG);
        pnMenu.setLayout(new BoxLayout(pnMenu, BoxLayout.Y_AXIS));

        // --- 1. PHẦN HEADER USER (HI ! Admin) ---
        JPanel pnUser = new JPanel();
        pnUser.setLayout(new BoxLayout(pnUser, BoxLayout.Y_AXIS));
        pnUser.setBackground(COL_SIDEBAR_BG);
        pnUser.setBorder(new EmptyBorder(30, 0, 30, 0));
        pnUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        pnUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblHi = new JLabel("CAFE MANAGER");
        lblHi.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblHi.setForeground(Color.WHITE);
        lblHi.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnUser.add(lblHi);
        pnMenu.add(pnUser);

        // --- 2. DANH SÁCH MENU ---
        // [NÚT 1] TRANG CHỦ (Về lại sơ đồ bàn)
        JButton btnTrangChu = createMenuItem("Trang chủ", true);
        btnTrangChu.addActionListener(e -> {
            pnBan.removeAll(); // Xóa bàn cũ
            initSoDoBan();     // Vẽ lại bàn mới (để cập nhật trạng thái xanh/đỏ)
            switchPanel(pnBan); // Hiện sơ đồ bàn
        });
        pnMenu.add(btnTrangChu);

        // [NÚT 2] QUẢN LÝ KHÁCH HÀNG (Phần của TÚ - Đã xong)
        JButton btnKhachHang = createMenuItem("Quản lý khách hàng", false);
        btnKhachHang.addActionListener(e -> {
            switchPanel(new FrmKhachHang()); // Gọi Form Khách Hàng
        });
        pnMenu.add(btnKhachHang);

        // [NÚT 3,4,5,6] CÁC CHỨC NĂNG CỦA THÀNH VIÊN KHÁC (Để tạm)
        pnMenu.add(createMenuItem("Quản lý bàn", false));      // Của Thảo
     
        JButton btnMenu = createMenuItem("Quản lý Menu", false);
        btnMenu.addActionListener(e -> {
            switchPanel(new FrmMenu()); // Gọi Form Menu
        });
        pnMenu.add(btnMenu);// Của Thảo
        pnMenu.add(createMenuItem("Quản lý đặt bàn", false));  // Của Minh
        pnMenu.add(createMenuItem("Thống kê và Hoá đơn", false)); // Của Đăng

        // --- 3. ĐẨY CÁC NÚT DƯỚI CÙNG XUỐNG ĐÁY ---
        pnMenu.add(Box.createVerticalGlue());

        // --- 4. CÁC NÚT CHỨC NĂNG DƯỚI ---
        // [NÚT 7] QUẢN LÝ TÀI KHOẢN (Phần của TÚ - Chỉ Admin mới thấy)
        JButton btnTaiKhoan = createMenuItem("Quản lý tài khoản", false);
        btnTaiKhoan.addActionListener(e -> {
            // Check quyền Admin
            if (util.Auth.isManager()) {
                switchPanel(new FrmTaiKhoan()); // Gọi Form Tài Khoản
            } else {
                JOptionPane.showMessageDialog(this, "Chức năng chỉ dành cho Quản lý (Admin)!");
            }
        });
        pnMenu.add(btnTaiKhoan);

        // [NÚT 8] ĐĂNG XUẤT
        JButton btnLogout = createMenuItem("ĐĂNG XUẤT", false);
        btnLogout.setForeground(new Color(255, 100, 100)); // Màu đỏ nhạt cho nổi
        btnLogout.addActionListener(e -> {
            int chon = JOptionPane.showConfirmDialog(this, "Bạn có muốn đăng xuất không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (chon == JOptionPane.YES_OPTION) {
                util.Auth.clear(); // Xóa session
                new FrmLogin().setVisible(true); // Về màn hình đăng nhập
                this.dispose(); // Đóng Main
            }
        });
        pnMenu.add(btnLogout);

        pnMenu.add(Box.createVerticalStrut(20)); // Khoảng trống dưới cùng
    }

    // Hàm hỗ trợ chuyển đổi nội dung bên phải
    private void switchPanel(JComponent component) {
        pnContent.removeAll(); // Xóa cái cũ (ví dụ Sơ đồ bàn)
        pnContent.add(component, BorderLayout.CENTER); // Add cái mới vào
        pnContent.revalidate(); // Tính toán lại bố cục
        pnContent.repaint(); // Vẽ lại
    }

    // ===== HÀM TẠO NÚT MENU (ĐÃ CHỈNH SỬA CHO GIỐNG MẪU) =====
    private JButton createMenuItem(String text, boolean isActive) {
        JButton btn = new JButton(text);

        // Căn trái cho BoxLayout
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Kích thước chuẩn
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setPreferredSize(new Dimension(260, 48));

        // Font
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Màu chữ
        btn.setForeground(COL_TEXT);

        // Màu nền
        btn.setBackground(isActive ? COL_MENU_HOVER : COL_SIDEBAR_BG);

        // Căn chữ trái
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        // 🔴 BORDER LUÔN CỐ ĐỊNH → KHÔNG LỆCH
        btn.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        // Tắt toàn bộ style mặc định của JButton
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);

        // Con trỏ chuột
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COL_MENU_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!isActive) {
                    btn.setBackground(COL_SIDEBAR_BG);
                }
            }
        });

        return btn;
    }

    private void initSoDoBan() {
        pnBan = new JPanel(new GridLayout(3, 4, 20, 20));
        pnBan.setBackground(new Color(245, 245, 245));
        pnBan.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Demo dữ liệu bàn
        pnBan.add(createBanCard("Bàn 1", "Trống", COL_BAN_TRONG));
        pnBan.add(createBanCard("Bàn 2", "Có khách", COL_BAN_COKHACH));
        pnBan.add(createBanCard("Bàn 3", "Đã đặt", COL_BAN_DADAT));
        pnBan.add(createBanCard("Bàn 4", "Trống", COL_BAN_TRONG));
        pnBan.add(createBanCard("Bàn 5", "Trống", COL_BAN_TRONG));
        pnBan.add(createBanCard("Bàn 6", "Trống", COL_BAN_TRONG));
        pnBan.add(createBanCard("Bàn 7", "Đã đặt", COL_BAN_DADAT));
        pnBan.add(createBanCard("Bàn 8", "Có khách", COL_BAN_COKHACH));

        pnContent.add(pnBan, BorderLayout.CENTER);
    }

    private JButton createBanCard(String name, String status, Color color) {
        JButton btn = new JButton("<html><center><h3>" + name + "</h3>" + status + "</center></html>");
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> new FrmMain().setVisible(true));
    }
}
