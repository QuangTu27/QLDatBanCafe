package view;

import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import util.Auth;

public class FrmMain extends JFrame {

    private JPanel pnMenu, pnContent, pnBan;
    private List<JButton> listMenu = new ArrayList<>();
    private JButton btnActive = null;
    private JButton btnTrangChu, btnKhachHang, btnBan, btnMenu, btnDatBan, btnThongKe,
                btnTaiKhoan, btnLogout;
    // Màu sắc
    private final Color COL_SIDEBAR_BG = Color.DARK_GRAY;
    private final Color COL_MENU_HOVER = Color.GRAY;
    private final Color COL_TEXT = Color.WHITE;

    private final Color COL_BAN_TRONG = new Color(46, 204, 113);
    private final Color COL_BAN_COKHACH = new Color(231, 76, 60);
    private final Color COL_BAN_DADAT = new Color(241, 196, 15);

    public FrmMain() {
        initUI();
        setAppLogo();
        addEvents();
    }

    private void initUI() {
        setTitle("Hệ thống Quản lý Cafe");
        setSize(1250, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initSidebar();

        pnContent = new JPanel(new BorderLayout());
        pnContent.setBackground(new Color(245, 245, 245));

        JPanel pnHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnHeader.setBackground(Color.WHITE);
        pnHeader.setPreferredSize(new Dimension(0, 50));

        String user = (Auth.isLogin()) ? Auth.user.getTenHienThi() : "Admin";
        JLabel lblHello = new JLabel("Xin chào, " + user + "!  ");
        lblHello.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        pnHeader.add(lblHello);

        pnContent.add(pnHeader, BorderLayout.NORTH);

        initSoDoBan();

        add(pnMenu, BorderLayout.WEST);
        add(pnContent, BorderLayout.CENTER);
    }

    // custom menu item
    private JButton createMenuItem(String text, String iconPath) {
        JButton btn = new JButton(text);
        try {
            // Tải icon từ resource
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
        }

        listMenu.add(btn);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        btn.setPreferredSize(new Dimension(260, 60));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(COL_TEXT);
        btn.setBackground(COL_SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        // Chỉnh khoảng cách giữa icon và chữ
        btn.setIconTextGap(10);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COL_MENU_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != btnActive) {
                    btn.setBackground(COL_SIDEBAR_BG);
                }
            }
        });

        return btn;
    }

    private void initSidebar() {
        pnMenu = new JPanel();
        pnMenu.setPreferredSize(new Dimension(280, 0));
        pnMenu.setBackground(COL_SIDEBAR_BG);
        pnMenu.setLayout(new BoxLayout(pnMenu, BoxLayout.Y_AXIS));
        // Header 
        JPanel pnTitle = new JPanel();
        pnTitle.setLayout(new BoxLayout(pnTitle, BoxLayout.Y_AXIS));
        pnTitle.setBackground(COL_SIDEBAR_BG);
        pnTitle.setBorder(new EmptyBorder(30, 0, 30, 0));
        pnTitle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        pnTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("CAFE MANAGER");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 25));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        pnTitle.add(lblTitle);
        pnMenu.add(pnTitle);

        //các chức năng
        // Trang chủ
        btnTrangChu = createMenuItem("Trang chủ", "/icons/home.png");
        btnKhachHang = createMenuItem("Quản lý khách hàng", "/icons/customer.png");
        btnBan = createMenuItem("Quản lý bàn", "/icons/table.png");
        btnMenu = createMenuItem("Quản lý Menu", "/icons/menu.png");
        btnDatBan = createMenuItem("Quản lý đặt bàn", "/icons/reserve.png");
        btnThongKe = createMenuItem("Thống kê & Hóa đơn", "/icons/bill.png");
        btnTaiKhoan = createMenuItem("Quản lý tài khoản", "/icons/account.png");
        btnLogout = createMenuItem("ĐĂNG XUẤT", "/icons/logout.png");
        btnLogout.setForeground(new Color(255, 100, 100));
        //add vào panel chung
        pnMenu.add(btnTrangChu);
        pnMenu.add(btnKhachHang);
        pnMenu.add(btnBan);
        pnMenu.add(btnMenu);
        pnMenu.add(btnDatBan);
        pnMenu.add(btnThongKe);
        pnMenu.add(Box.createVerticalGlue());
        pnMenu.add(btnTaiKhoan);
        pnMenu.add(btnLogout);
        pnMenu.add(Box.createVerticalStrut(20));
        // Mặc định chọn trang chủ khi mở lên
        setSelectedMenu(btnTrangChu);
    }

    // focus màu khi chọn chức năng
    private void setSelectedMenu(JButton selectedBtn) {
        for (JButton btn : listMenu) {
            btn.setBackground(COL_SIDEBAR_BG);
        }

        selectedBtn.setBackground(COL_MENU_HOVER);
        btnActive = selectedBtn;
    }

    private void switchPanel(JComponent component) {
        pnContent.removeAll();
        pnContent.add(component, BorderLayout.CENTER);
        pnContent.revalidate();
        pnContent.repaint();
    }

    private void addEvents() {
        btnTrangChu.addActionListener(e -> {
            setSelectedMenu(btnTrangChu);
            pnBan.removeAll();
            initSoDoBan();
            switchPanel(pnBan);
        });

        btnKhachHang.addActionListener(e -> {
            setSelectedMenu(btnKhachHang);
            switchPanel(new FrmKhachHang());
        });

        btnBan.addActionListener(e -> {
                setSelectedMenu(btnBan);
            switchPanel(new FrmBan());
        });

        btnMenu.addActionListener(e -> setSelectedMenu(btnMenu));

        btnDatBan.addActionListener(e -> setSelectedMenu(btnDatBan));

        btnThongKe.addActionListener(e -> setSelectedMenu(btnThongKe));

        btnTaiKhoan.addActionListener(e -> {
            if (Auth.isManager()) {
                setSelectedMenu(btnTaiKhoan);
                switchPanel(new FrmTaiKhoan());
            } else {
                JOptionPane.showMessageDialog(this, "Chức năng chỉ dành cho Admin!");
            }
        });

        btnLogout.addActionListener(e -> {
            int chon = JOptionPane.showConfirmDialog(this, "Đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (chon == JOptionPane.YES_OPTION) {
                Auth.clear();
                new FrmLogin().setVisible(true);
                this.dispose();
            }
        });
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

    public void setAppLogo() {
        try {
            // Tải ảnh từ thư mục resource
            ImageIcon img = new ImageIcon(getClass().getResource("/icons/logocafe.png"));

            // Gán icon cho Frame
            this.setIconImage(img.getImage());
        } catch (Exception e) {
            System.out.println("Không tìm thấy file logo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(() -> new FrmMain().setVisible(true));
    }

}
