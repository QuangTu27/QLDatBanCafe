package Dialog;

import DAO.BanDao;
import entity.Ban;
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
import javax.swing.JTextField;

public class DlgBan extends JDialog {

    private JTextField txtMaBan, txtTenBan, txtSoGhe;
    private JComboBox<String> cboTrangThai;
    private JButton btnLuu, btnHuy;

    private BanDao dao = new BanDao();
    private Ban banEditing = null;
    private boolean result = false;

    public DlgBan(Window parent, Ban ban) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.banEditing = ban;

        initComponents();

        if (ban != null) {
            txtMaBan.setText(ban.getMaBan());
            txtMaBan.setEditable(false); // Không cho sửa mã khi cập nhật
            txtTenBan.setText(ban.getTenBan());
            txtSoGhe.setText(String.valueOf(ban.getSoGhe()));
            cboTrangThai.setSelectedItem(ban.getTrangThai());
            btnLuu.setText("Cập nhật");
        }
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(500, 450);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(46, 204, 113));
        pnlHeader.setPreferredSize(new Dimension(0, 50));
        JLabel lblTitle = new JLabel(banEditing == null ? "THÊM BÀN MỚI" : "CẬP NHẬT BÀN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // ===== FORM NHẬP LIỆU =====
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 15, 25));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        pnlForm.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 15);

        pnlForm.add(createLabel("Mã bàn:", labelFont));
        txtMaBan = createTextField(labelFont);
        pnlForm.add(txtMaBan);

        pnlForm.add(createLabel("Tên bàn:", labelFont));
        txtTenBan = createTextField(labelFont);
        pnlForm.add(txtTenBan);

        pnlForm.add(createLabel("Số ghế:", labelFont));
        txtSoGhe = createTextField(labelFont);
        pnlForm.add(txtSoGhe);

        pnlForm.add(createLabel("Trạng thái:", labelFont));
        cboTrangThai = new JComboBox<>(new String[]{"Trống", "Có khách", "Đã đặt"});
        cboTrangThai.setFont(labelFont);
        cboTrangThai.setBackground(Color.WHITE);
        pnlForm.add(cboTrangThai);

        add(pnlForm, BorderLayout.CENTER);

        // ===== BUTTONS =====
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 20));
        pnlButton.setBackground(Color.WHITE);

        btnLuu = createToolButton("Lưu", new Color(46, 204, 113));
        btnHuy = createToolButton("Hủy", new Color(231, 76, 60));

        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        add(pnlButton, BorderLayout.SOUTH);

        // Events
        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> xuLyLuu());
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private JTextField createTextField(Font font) {
        JTextField txt = new JTextField();
        txt.setFont(font);
        return txt;
    }

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
        if (!validateForm()) return;

        Ban banMoi = new Ban(
                txtMaBan.getText().trim(),
                txtTenBan.getText().trim(),
                Integer.parseInt(txtSoGhe.getText().trim()),
                cboTrangThai.getSelectedItem().toString()
        );

        boolean thanhCong = (banEditing == null) ? dao.insert(banMoi) : dao.update(banMoi);

        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "Lưu dữ liệu bàn thành công!");
            result = true;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi: Trùng mã bàn hoặc lỗi CSDL!");
        }
    }

    private boolean validateForm() {
        if (txtMaBan.getText().trim().isEmpty() || txtTenBan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ Mã bàn và Tên bàn!");
            return false;
        }

        // Kiểm tra số ghế phải là số nguyên dương
        try {
            int soGhe = Integer.parseInt(txtSoGhe.getText().trim());
            if (soGhe <= 0) {
                JOptionPane.showMessageDialog(this, "Số ghế phải lớn hơn 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số ghế phải là một con số!");
            txtSoGhe.requestFocus();
            return false;
        }

        return true;
    }

    public boolean getResult() {
        return result;
    }
}