package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import util.Auth;

public class FrmMain extends JFrame {

    private JPanel pnMenu, pnContent, pnBan;
    
    // --- 1. DANH SÁCH QUẢN LÝ MENU (ĐỂ ĐỔI MÀU) ---
    private List<JButton> listMenu = new ArrayList<>(); 
    private JButton btnActive = null; // Biến lưu nút nào đang được chọn

    // Màu sắc
    private final Color COL_SIDEBAR_BG = Color.DARK_GRAY;
    private final Color COL_MENU_HOVER = Color.GRAY;
    private final Color COL_TEXT = Color.WHITE;

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

        initSidebar();

        pnContent = new JPanel(new BorderLayout());
        pnContent.setBackground(new Color(245, 245, 245));

        JPanel pnHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnHeader.setBackground(Color.WHITE);
        pnHeader.setPreferredSize(new Dimension(0, 50));
        
        // Hiển thị tên người dùng nếu đã đăng nhập
        String user = (Auth.isLogin()) ? Auth.user.getTenHienThi() : "Admin";
        pnHeader.add(new JLabel("Xin chào, " + user + "!  "));
        
        pnContent.add(pnHeader, BorderLayout.NORTH);

        initSoDoBan();

        add(pnMenu, BorderLayout.WEST);
        add(pnContent, BorderLayout.CENTER);
    }

    private void initSidebar() {
        pnMenu = new JPanel();
        pnMenu.setPreferredSize(new Dimension(260, 0));
        pnMenu.setBackground(COL_SIDEBAR_BG);
        pnMenu.setLayout(new BoxLayout(pnMenu, BoxLayout.Y_AXIS));

        // Header User
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

        // --- TẠO CÁC NÚT MENU ---
        
        // 1. Trang chủ
        JButton btnTrangChu = createMenuItem("Trang chủ");
        btnTrangChu.addActionListener(e -> {
            setSelectedMenu(btnTrangChu); // <--- KÍCH HOẠT MÀU
            pnBan.removeAll();
            initSoDoBan();
            switchPanel(pnBan);
        });
        pnMenu.add(btnTrangChu);

        // 2. Quản lý khách hàng
        JButton btnKhachHang = createMenuItem("Quản lý khách hàng");
        btnKhachHang.addActionListener(e -> {
            setSelectedMenu(btnKhachHang); // <--- KÍCH HOẠT MÀU
            switchPanel(new FrmKhachHang());
        });
        pnMenu.add(btnKhachHang);

        // 3. Các nút khác (Chưa có chức năng thì chỉ đổi màu thôi)
        JButton btnBan = createMenuItem("Quản lý bàn");
        btnBan.addActionListener(e -> setSelectedMenu(btnBan));
        pnMenu.add(btnBan);

        JButton btnMenu = createMenuItem("Quản lý Menu");
        btnMenu.addActionListener(e -> setSelectedMenu(btnMenu));
        pnMenu.add(btnMenu);

        JButton btnDatBan = createMenuItem("Quản lý đặt bàn");
        btnDatBan.addActionListener(e -> setSelectedMenu(btnDatBan));
        pnMenu.add(btnDatBan);
        
        JButton btnThongKe = createMenuItem("Thống kê & Hóa đơn");
        btnThongKe.addActionListener(e -> setSelectedMenu(btnThongKe));
        pnMenu.add(btnThongKe);

        pnMenu.add(Box.createVerticalGlue());

        // 7. Quản lý tài khoản
        JButton btnTaiKhoan = createMenuItem("Quản lý tài khoản");
        btnTaiKhoan.addActionListener(e -> {
            if (Auth.isManager()) {
                setSelectedMenu(btnTaiKhoan); // <--- KÍCH HOẠT MÀU
                switchPanel(new FrmTaiKhoan());
            } else {
                JOptionPane.showMessageDialog(this, "Chức năng chỉ dành cho Admin!");
            }
        });
        pnMenu.add(btnTaiKhoan);

        // 8. Đăng xuất (Nút này không cần Active màu nền)
        JButton btnLogout = createMenuItem("ĐĂNG XUẤT");
        btnLogout.setForeground(new Color(255, 100, 100));
        btnLogout.addActionListener(e -> {
            int chon = JOptionPane.showConfirmDialog(this, "Đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (chon == JOptionPane.YES_OPTION) {
                Auth.clear();
                new FrmLogin().setVisible(true);
                this.dispose();
            }
        });
        pnMenu.add(btnLogout);
        pnMenu.add(Box.createVerticalStrut(20));
        
        // Mặc định chọn trang chủ khi mở lên
        setSelectedMenu(btnTrangChu);
    }
    
    // ===== HÀM MỚI: XỬ LÝ ĐỔI MÀU MENU KHI CLICK =====
    private void setSelectedMenu(JButton selectedBtn) {
        // 1. Reset màu tất cả các nút về màu nền gốc
        for (JButton btn : listMenu) {
            btn.setBackground(COL_SIDEBAR_BG);
        }
        
        // 2. Set màu nút được chọn thành màu sáng
        selectedBtn.setBackground(COL_MENU_HOVER);
        
        // 3. Lưu lại nút đang active để xử lý hover
        btnActive = selectedBtn;
    }

    private void switchPanel(JComponent component) {
        pnContent.removeAll();
        pnContent.add(component, BorderLayout.CENTER);
        pnContent.revalidate();
        pnContent.repaint();
    }

    // tạo nút menu
    private JButton createMenuItem(String text) {
        JButton btn = new JButton(text);
        listMenu.add(btn); 

        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btn.setPreferredSize(new Dimension(260, 48));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(COL_TEXT);
        btn.setBackground(COL_SIDEBAR_BG); // Mặc định là màu tối
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Di chuột vào thì sáng lên
                btn.setBackground(COL_MENU_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn == btnActive) {
                    btn.setBackground(COL_MENU_HOVER);
                } else {
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