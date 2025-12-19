/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FrmLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public FrmLogin() {
        initUI();
    }

    private void initUI() {
        setTitle("Đăng nhập hệ thống");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("PHẦN MỀM QUẢN LÝ CAFE", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel pnForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        JLabel lblPass = new JLabel("Mật khẩu:");

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(52, 152, 219));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        pnForm.add(lblUser, gbc);

        gbc.gridx = 1;
        pnForm.add(txtUsername, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        pnForm.add(lblPass, gbc);

        gbc.gridx = 1;
        pnForm.add(txtPassword, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        pnForm.add(btnLogin, gbc);

        add(pnForm, BorderLayout.CENTER);

        // ===== EVENT =====
        btnLogin.addActionListener((ActionEvent e) -> login());
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ thông tin!",
                    "Lỗi",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // DEMO (sau này thay bằng DB)
        if (username.equals("admin") && password.equals("123")) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");

            // Mở Form Main
            new FrmMain().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Sai tên đăng nhập hoặc mật khẩu!",
                    "Đăng nhập thất bại",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }
}

