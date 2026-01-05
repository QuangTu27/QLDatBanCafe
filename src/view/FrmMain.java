package view;

import DAO.BanDao;
import entity.Ban;
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
import javax.swing.UIManager;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import util.Auth;

public class FrmMain extends JFrame {

    private JPanel pnMenu, pnContent, pnBan;
    private List<JButton> listMenu = new ArrayList<>();
    private JButton btnActive = null;
    private JButton btnTrangChu, btnKhachHang, btnBan, btnMenu, btnDatBan, btnGoiMon,
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

        loadSoDoBan();
    }

    private void initUI() {
        setTitle("Hệ thống Quản lý Cafe");
        setSize(1350, 750);
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

        // Khởi tạo pnBan để chứa các nút bàn
        pnBan = new JPanel(new GridLayout(0, 5, 15, 15)); // 5 cột, khoảng cách 15px
        pnBan.setBackground(new Color(245, 245, 245));
        pnBan.setBorder(new EmptyBorder(20, 20, 20, 20));

        pnContent.add(pnHeader, BorderLayout.NORTH);
        //Mặc định đưa sơ đồ bàn vào trung tâm nội dung
        pnContent.add(pnBan, BorderLayout.CENTER);

        add(pnMenu, BorderLayout.WEST);
        add(pnContent, BorderLayout.CENTER);
    }

    private JButton createMenuItem(String text, String iconPath) {
        JButton btn = new JButton(text);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.err.println("Không load được icon: " + iconPath);
        }

        listMenu.add(btn);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        btn.setPreferredSize(new Dimension(260, 60));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(COL_TEXT);
        btn.setBackground(COL_SIDEBAR_BG);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
       
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        btnTrangChu = createMenuItem("Trang chủ", "/icons/home.png");
        btnKhachHang = createMenuItem("Quản lý khách hàng", "/icons/customer.png");
        btnBan = createMenuItem("Quản lý bàn", "/icons/table.png");
        btnMenu = createMenuItem("Quản lý Menu", "/icons/menu.png");
        btnDatBan = createMenuItem("Quản lý đặt bàn", "/icons/reserve.png");
        btnGoiMon = createMenuItem("Gọi món", "/icons/order.png");
        btnTaiKhoan = createMenuItem("Quản lý tài khoản", "/icons/account.png");
        btnLogout = createMenuItem("ĐĂNG XUẤT", "/icons/logout.png");
        btnLogout.setForeground(new Color(255, 100, 100));

        pnMenu.add(btnTrangChu);
        pnMenu.add(btnKhachHang);
        pnMenu.add(btnBan);
        pnMenu.add(btnMenu);
        pnMenu.add(btnDatBan);
        pnMenu.add(btnGoiMon);
        pnMenu.add(Box.createVerticalGlue());
        pnMenu.add(btnTaiKhoan);
        pnMenu.add(btnLogout);
        pnMenu.add(Box.createVerticalStrut(20));

        setSelectedMenu(btnTrangChu);
    }

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
            // LOGIC: Mỗi lần bấm Trang chủ thì làm mới dữ liệu từ Database
            loadSoDoBan();
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

        btnMenu.addActionListener(e -> {
            setSelectedMenu(btnMenu);
            switchPanel(new FrmMenu());
        });

        btnGoiMon.addActionListener(e -> {
            setSelectedMenu(btnGoiMon);
            switchPanel(new FrmGoiMon());
        });
        btnDatBan.addActionListener(e -> {
            setSelectedMenu(btnDatBan);
            switchPanel(new FrmDatBan());
        });
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

    public void loadSoDoBan() {
        if (pnBan == null) {
            return;
        }

        pnBan.removeAll();
        BanDao dao = new BanDao();
        List<Ban> list = dao.selectAll();

        for (Ban b : list) {
            Color mauSac;
            if (b.getTrangThai().equalsIgnoreCase("Trống")) {
                mauSac = COL_BAN_TRONG;
            } else if (b.getTrangThai().equalsIgnoreCase("Có khách")) {
                mauSac = COL_BAN_COKHACH;
            } else {
                mauSac = COL_BAN_DADAT;
            }

            pnBan.add(createBanCard(b.getTenBan(), b.getTrangThai(), mauSac));
        }

        pnBan.revalidate();
        pnBan.repaint();
    }

    private JButton createBanCard(String name, String status, Color color) {
        JButton btn = new JButton("<html><center><h3>" + name + "</h3>" + status + "</center></html>");
        btn.setPreferredSize(new Dimension(130, 130)); // Đảm bảo card có kích thước để hiển thị
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        return btn;
    }

    public void setAppLogo() {
        try {
            ImageIcon img = new ImageIcon(getClass().getResource("/icons/logocafe.png"));
            this.setIconImage(img.getImage());
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        
        new FrmMain().setVisible(true);
    }
}
