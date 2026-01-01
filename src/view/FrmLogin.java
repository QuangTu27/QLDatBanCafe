package view;

import DAO.TaiKhoanDao;
import entity.TaiKhoan;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import util.Auth;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class FrmLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private TaiKhoanDao dao = new TaiKhoanDao();

    private final Color MAIN_COLOR = new Color(46, 204, 113);

    public FrmLogin() {
        initUI();
    }

    private void initUI() {
        setTitle("Đăng nhập hệ thống");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2)); // Chia đôi màn hình: Trái - Phải
        setResizable(false);

        // 1. PANEL TRÁI
        JPanel pnlLeft = new JPanel();
        pnlLeft.setBackground(MAIN_COLOR);
        pnlLeft.setLayout(new GridBagLayout());

        JLabel lblIcon = new JLabel("️️🍵", JLabel.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setPreferredSize(new Dimension(200, 150));
//        JLabel lblIcon = new JLabel(); 
//        ImageIcon iconLogo = loadIcon("/icons/logo.jpg", 150, 150);
//        if (iconLogo != null) {
//            lblIcon.setIcon(iconLogo);
//        } else {
//            // Phòng hờ nếu chưa có ảnh thì hiện chữ tạm
//            lblIcon.setText("CAFE");
//            lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 40));
//            lblIcon.setForeground(Color.WHITE);
//        }

        JLabel lblBrand = new JLabel("CAFE MANAGER");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblBrand.setForeground(Color.WHITE);

        JLabel lblSlogan = new JLabel("Quản lý chuyên nghiệp");
        lblSlogan.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblSlogan.setForeground(new Color(230, 250, 230));

        // Add vào Panel trái
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        pnlLeft.add(lblIcon, gbcLeft);

        gbcLeft.gridy = 1;
        gbcLeft.insets = new Insets(10, 0, 0, 0);
        pnlLeft.add(lblBrand, gbcLeft);

        gbcLeft.gridy = 2;
        pnlLeft.add(lblSlogan, gbcLeft);

        // 2. PANEL PHẢI
        JPanel pnlRight = new JPanel();
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề Form
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(MAIN_COLOR);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(Color.GRAY);

        txtUsername = new JTextField(15);
        styleTextField(txtUsername); // Hàm làm đẹp ô nhập

        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setForeground(Color.GRAY);

        txtPassword = new JPasswordField(15);
        styleTextField(txtPassword); // Hàm làm đẹp ô nhập

        btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(MAIN_COLOR);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(0, 45));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        pnlRight.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridy = 1;
        pnlRight.add(lblUser, gbc);
        gbc.gridy = 2;
        pnlRight.add(txtUsername, gbc);

        gbc.gridy = 3;
        pnlRight.add(lblPass, gbc);
        gbc.gridy = 4;
        pnlRight.add(txtPassword, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(30, 10, 10, 10);
        pnlRight.add(btnLogin, gbc);

        add(pnlLeft);
        add(pnlRight);

        btnLogin.addActionListener((ActionEvent e) -> login());
        getRootPane().setDefaultButton(btnLogin);
    }

    private void styleTextField(JTextField txt) {
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setForeground(Color.DARK_GRAY);
        txt.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 2, 0, new Color(200, 200, 200)),
                    new EmptyBorder(5, 5, 5, 5)
        ));
        txt.setBackground(Color.WHITE);
    }

    private void login() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        try {
            TaiKhoan tk = dao.selectByUsername(user);
            if (tk == null) {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập!");
            } else if (!pass.equals(tk.getMatKhau())) {
                JOptionPane.showMessageDialog(this, "Sai mật khẩu!");
            } else {
                // Đăng nhập thành công
                Auth.user = tk;
                new FrmMain().setVisible(true);
                this.dispose();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!");
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        SwingUtilities.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }
}
