package Dialog;

import DAO.TaiKhoanDao;
import entity.TaiKhoan;
import java.awt.*;
import javax.swing.*;

public class DlgTaiKhoan extends JDialog {

    private JTextField txtMaTK, txtTenDN, txtTenHienThi;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboPhanQuyen;
    private JButton btnLuu, btnHuy;

    private TaiKhoanDao dao = new TaiKhoanDao();
    private TaiKhoan taiKhoan; // null = thêm | khác null = sửa
    private boolean result = false;

    public DlgTaiKhoan(Window parent, TaiKhoan tk) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.taiKhoan = tk;
        initComponents();
        setLocationRelativeTo(parent);
    }

    public boolean getResult() {
        return result;
    }

    private void initComponents() {
        setSize(400, 350);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(46, 204, 113));
        pnlHeader.setPreferredSize(new Dimension(0, 50));
        JLabel lblTitle = new JLabel(taiKhoan == null ? "THÊM TÀI KHOẢN" : "CẬP NHẬT TÀI KHOẢN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel pnlForm = new JPanel(new GridLayout(5, 2, 10, 10));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        pnlForm.add(new JLabel("Mã TK:"));
        txtMaTK = new JTextField();
        pnlForm.add(txtMaTK);

        pnlForm.add(new JLabel("Tên đăng nhập:"));
        txtTenDN = new JTextField();
        pnlForm.add(txtTenDN);

        pnlForm.add(new JLabel("Mật khẩu:"));
        txtMatKhau = new JPasswordField();
        pnlForm.add(txtMatKhau);

        pnlForm.add(new JLabel("Tên hiển thị:"));
        txtTenHienThi = new JTextField();
        pnlForm.add(txtTenHienThi);

        pnlForm.add(new JLabel("Vai trò:"));
        cboPhanQuyen = new JComboBox<>(new String[]{"Nhân viên", "Quản lý (Admin)"});
        pnlForm.add(cboPhanQuyen);

        add(pnlForm, BorderLayout.CENTER);

        // ===== BUTTON =====
        // Buttons
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        
        btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(231, 76, 60));
        btnHuy.setForeground(Color.WHITE);
        
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        add(pnlButton, BorderLayout.SOUTH);

        // ===== NẾU LÀ SỬA → ĐỔ DỮ LIỆU =====
        if (taiKhoan != null) {
            txtMaTK.setText(taiKhoan.getMaTK());
            txtMaTK.setEditable(false);

            txtTenDN.setText(taiKhoan.getTenDangNhap());
            txtTenDN.setEditable(false);

            txtTenHienThi.setText(taiKhoan.getTenHienThi());
            cboPhanQuyen.setSelectedIndex(taiKhoan.getPhanQuyen());
        }

        // ===== EVENTS =====
        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> xuLyLuu());
    }

    private void xuLyLuu() {
        if (!validateForm()) return;

        String ma = txtMaTK.getText().trim();
        String user = txtTenDN.getText().trim();
        String pass = new String(txtMatKhau.getPassword());
        String ten = txtTenHienThi.getText().trim();
        int role = cboPhanQuyen.getSelectedIndex();

        // ===== THÊM =====
        if (taiKhoan == null) {
            TaiKhoan tk = new TaiKhoan(ma, user, pass, ten, role);
            if (dao.insert(tk)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                result = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        }
        // ===== SỬA =====
        else {
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
}
