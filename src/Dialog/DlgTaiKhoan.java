package Dialog;

import DAO.TaiKhoanDao;
import entity.TaiKhoan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DlgTaiKhoan extends JDialog {

    private JTextField txtMaTK, txtTenDN, txtTenHienThi;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboPhanQuyen;
    private JButton btnLuu, btnHuy;

    private TaiKhoanDao dao = new TaiKhoanDao();
    private TaiKhoan taiKhoan; 
    private boolean result = false;

    public DlgTaiKhoan(Window parent, TaiKhoan tk) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.taiKhoan = tk;
        
        initComponents();
        
        if (tk != null) {
            txtMaTK.setText(taiKhoan.getMaTK());
            txtMaTK.setEditable(false);

            txtTenDN.setText(taiKhoan.getTenDangNhap());
            txtTenDN.setEditable(false);

            txtTenHienThi.setText(taiKhoan.getTenHienThi());
            cboPhanQuyen.setSelectedIndex(taiKhoan.getPhanQuyen());
        }
        
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 450);
        setLayout(new BorderLayout());

        // header 
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(46, 204, 113));
        pnlHeader.setPreferredSize(new Dimension(0, 30));
        JLabel lblTitle = new JLabel(taiKhoan == null ? "THÊM TÀI KHOẢN" : "CẬP NHẬT TÀI KHOẢN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // form
        JPanel pnlForm = new JPanel(new GridLayout(5, 2, 15, 20));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);

        JLabel lblMa = new JLabel("Mã TK:");
        lblMa.setFont(labelFont);
        pnlForm.add(lblMa);
        txtMaTK = new JTextField();
        txtMaTK.setFont(labelFont);
        pnlForm.add(txtMaTK);

        JLabel lblTenDN = new JLabel("Tên đăng nhập:");
        lblTenDN.setFont(labelFont);
        pnlForm.add(lblTenDN);
        txtTenDN = new JTextField();
        txtTenDN.setFont(labelFont);
        pnlForm.add(txtTenDN);

        JLabel lblMK = new JLabel("Mật khẩu:");
        lblMK.setFont(labelFont);
        pnlForm.add(lblMK);
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(labelFont);
        pnlForm.add(txtMatKhau);

        JLabel lblTenHT = new JLabel("Tên hiển thị:");
        lblTenHT.setFont(labelFont);
        pnlForm.add(lblTenHT);
        txtTenHienThi = new JTextField();
        txtTenHienThi.setFont(labelFont);
        pnlForm.add(txtTenHienThi);

        JLabel lblVaiTro = new JLabel("Vai trò:");
        lblVaiTro.setFont(labelFont);
        pnlForm.add(lblVaiTro);
        cboPhanQuyen = new JComboBox<>(new String[]{"Nhân viên", "Quản lý (Admin)"});
        cboPhanQuyen.setFont(labelFont);
        pnlForm.add(cboPhanQuyen);

        add(pnlForm, BorderLayout.CENTER);

        // Buttons
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));

        btnLuu = createToolButton("Lưu", new Color(46, 204, 113));
        btnHuy = createToolButton("Hủy", new Color(231, 76, 60));

        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        add(pnlButton, BorderLayout.SOUTH);

        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> xuLyLuu());
    }

    //custom btn
    private JButton createToolButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false); 
        btn.setOpaque(true);

        btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 2),
                    BorderFactory.createEmptyBorder(10, 30, 10, 30)
        ));

        // Hiệu ứng Hover
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color);      
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE); 
                btn.setForeground(color);      
            }
        });

        return btn;
    }

    private void xuLyLuu() {
        if (!validateForm()) {
            return;
        }

        String ma = txtMaTK.getText().trim();
        String user = txtTenDN.getText().trim();
        String pass = new String(txtMatKhau.getPassword());
        String ten = txtTenHienThi.getText().trim();
        int role = cboPhanQuyen.getSelectedIndex();

        if (taiKhoan == null) {
            TaiKhoan tk = new TaiKhoan(ma, user, pass, ten, role);
            if (dao.insert(tk)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                result = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        } else {
            taiKhoan.setTenHienThi(ten);
            taiKhoan.setPhanQuyen(role);

            // Cho phép đổi mật khẩu nếu có nhập
            if (!pass.isEmpty()) {
                taiKhoan.setMatKhau(pass);
            }

            if (dao.update(taiKhoan)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                result = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        }
    }

    private boolean validateForm() {
        if (txtMaTK.getText().trim().isEmpty()
                    || txtTenDN.getText().trim().isEmpty()
                    || txtTenHienThi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }

        if (taiKhoan == null && txtMatKhau.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Mật khẩu không được để trống!");
            return false;
        }
        return true;
    }

    public boolean getResult() {
        return result;
    }
}
