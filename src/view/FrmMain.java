package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        // BoxLayout trục Y để xếp dọc
        pnMenu.setLayout(new BoxLayout(pnMenu, BoxLayout.Y_AXIS));

        // --- 1. PHẦN HEADER USER (HI ! Admin) ---
        JPanel pnUser = new JPanel();
        pnUser.setLayout(new BoxLayout(pnUser, BoxLayout.Y_AXIS));
        pnUser.setBackground(COL_SIDEBAR_BG);
        pnUser.setBorder(new EmptyBorder(30, 0, 30, 0));
        pnUser.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120)); // Chiều cao cố định
        pnUser.setAlignmentX(Component.LEFT_ALIGNMENT); // Căn trái để đồng bộ

        JLabel lblHi = new JLabel("CAFE MANAGER");
        lblHi.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblHi.setForeground(Color.WHITE);
        lblHi.setAlignmentX(Component.CENTER_ALIGNMENT); // Chữ căn giữa panel

        pnUser.add(lblHi);
        
        pnMenu.add(pnUser);

        // --- 2. DANH SÁCH MENU (Thêm Icon vào text) ---
        // Lưu ý: Tôi dùng Icon Unicode để mô phỏng. Bạn có thể thay bằng file ảnh nếu muốn.
        pnMenu.add(createMenuItem("Trang chủ", true)); // true = đang chọn (Active)
        pnMenu.add(createMenuItem("Quản lý khách hàng", false));
        pnMenu.add(createMenuItem("Quản lý bàn", false));
        pnMenu.add(createMenuItem("Quản lý Menu", false));
        pnMenu.add(createMenuItem("Quản lý đặt bàn", false));
        pnMenu.add(createMenuItem("Thống kê và Hoá đơn", false));

        // --- 3. ĐẨY CÁC NÚT DƯỚI CÙNG XUỐNG ĐÁY ---
        pnMenu.add(Box.createVerticalGlue());

        // --- 4. CÁC NÚT CHỨC NĂNG DƯỚI (Đổi thông tin, Đăng xuất) ---
        pnMenu.add(createMenuItem("Quản lý tài khoản", false));
        
        JButton btnLogout = createMenuItem("ĐĂNG XUẤT", false);
        // Nút đăng xuất có thể chỉnh màu khác nếu thích, ở đây tôi để giống mẫu
        pnMenu.add(btnLogout);
        
        pnMenu.add(Box.createVerticalStrut(20)); // Khoảng trống dưới cùng
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
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new FrmMain().setVisible(true));
    }
}