package Dialog;

import entity.Menu;
import DAO.MenuDao;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DlgMenu extends JDialog {

    private JTextField txtMa, txtTen, txtGia;
    private JComboBox<String> cboLoai;
    private JLabel lblHinh;
    private JButton btnChonHinh, btnLuu, btnHuy;

    private MenuDao dao = new MenuDao();
    private Menu menuEditing = null;
    private String hinhFile = null;
    private boolean result = false;

    // Tăng kích thước ảnh lên như lúc đầu
    private static final int IMG_W = 250;
    private static final int IMG_H = 200;

    public DlgMenu(Window parent, Menu menu) {
        super(parent, ModalityType.APPLICATION_MODAL);
        this.menuEditing = menu;
        initComponents();
        if (menu != null) {
            fillForm();
        }
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(550, 700); // Tăng chiều cao để chứa ảnh to
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // ===== HEADER =====
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(new Color(46, 204, 113));
        pnlHeader.setPreferredSize(new Dimension(0, 50));
        JLabel lblTitle = new JLabel(menuEditing == null ? "THÊM MÓN MỚI" : "CẬP NHẬT MÓN ĂN");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // ===== FORM NHẬP LIỆU (Dùng GridBagLayout để kiểm soát kích thước ô ảnh) =====
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);

        // Hàng 1: Mã món
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblMaMon = new JLabel("Mã Món:");
        lblMaMon.setFont(labelFont);
        pnlForm.add(lblMaMon, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtMa = new JTextField();
        txtMa.setFont(labelFont);
        pnlForm.add(txtMa, gbc);

        // Hàng 2: Tên món
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblTenMon = new JLabel("Tên Món:");
        lblTenMon.setFont(labelFont);
        pnlForm.add(lblTenMon, gbc);
        gbc.gridx = 1;
        txtTen = new JTextField();
        txtTen.setFont(labelFont);
        pnlForm.add(txtTen, gbc);

        // Hàng 3: Đơn giá
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblGia = new JLabel("Đơn giá:");
        lblGia.setFont(labelFont);
        pnlForm.add(lblGia, gbc);
        gbc.gridx = 1;
        txtGia = new JTextField();
        txtGia.setFont(labelFont);
        pnlForm.add(txtGia, gbc);

        // Hàng 4: Loại món
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblLoaiMon = new JLabel("Loại món: ");
        lblLoaiMon.setFont(labelFont);
        pnlForm.add(lblLoaiMon, gbc);
        gbc.gridx = 1;
        cboLoai = new JComboBox<>(new String[]{"Bánh", "Trà sữa", "Cafe", "Matcha", "Nước ép", "Sinh tố", "Khác"});
        cboLoai.setFont(labelFont);
        cboLoai.setBackground(Color.WHITE);
        pnlForm.add(cboLoai, gbc);

        // Hàng 5: Hình ảnh (Phần quan trọng để ảnh to)
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel lblHinhAnh = new JLabel("Hình ảnh: ");
        lblHinhAnh.setFont(labelFont);
        pnlForm.add(lblHinhAnh, gbc);

        gbc.gridx = 1;
        lblHinh = new JLabel("Chưa có ảnh", SwingConstants.CENTER);
        lblHinh.setPreferredSize(new Dimension(IMG_W, IMG_H));
        lblHinh.setMinimumSize(new Dimension(IMG_W, IMG_H));
        lblHinh.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        // Dùng JPanel bọc Label ảnh để giữ kích thước cố định
        JPanel pnlImgContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlImgContainer.setBackground(Color.WHITE);
        pnlImgContainer.add(lblHinh);
        pnlForm.add(pnlImgContainer, gbc);

        // Hàng 6: Nút chọn hình
        gbc.gridy = 5;
        btnChonHinh = createToolButton("Chọn ảnh từ máy", new Color(52, 152, 219));
        pnlForm.add(btnChonHinh, gbc);

        add(pnlForm, BorderLayout.CENTER);

        // ===== BOTTOM BUTTONS =====
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        pnlBottom.setBackground(Color.WHITE);

        btnLuu = createToolButton("Lưu món", new Color(46, 204, 113));
        btnHuy = createToolButton("Hủy bỏ", new Color(231, 76, 60));

        pnlBottom.add(btnLuu);
        pnlBottom.add(btnHuy);
        add(pnlBottom, BorderLayout.SOUTH);

        // Events
        btnChonHinh.addActionListener(e -> chonHinh());
        btnHuy.addActionListener(e -> dispose());
        btnLuu.addActionListener(e -> xuLyLuu());
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
                    BorderFactory.createEmptyBorder(10, 25, 10, 25)
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

    private void chonHinh() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            hinhFile = fc.getSelectedFile().getAbsolutePath();
            updateImageLabel(hinhFile);
        }
    }

    private void updateImageLabel(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(IMG_W, IMG_H, Image.SCALE_SMOOTH);
        lblHinh.setIcon(new ImageIcon(img));
        lblHinh.setText("");
    }

    private void fillForm() {
        txtMa.setText(menuEditing.getMaMenu());
        txtMa.setEditable(false);
        txtTen.setText(menuEditing.getTenMon());
        txtGia.setText(String.valueOf(menuEditing.getDonGia()));
        cboLoai.setSelectedItem(menuEditing.getLoaiMon());
        if (menuEditing.getHinhAnh() != null && new File(menuEditing.getHinhAnh()).exists()) {
            updateImageLabel(menuEditing.getHinhAnh());
            hinhFile = menuEditing.getHinhAnh();
        }
    }

    private void xuLyLuu() {
        if (!validateForm()) {
            return;
        }

        double giaMon = Double.parseDouble(txtGia.getText().trim());
        String loai = cboLoai.getSelectedItem().toString();

        if (menuEditing == null) {
            if (dao.insert(new Menu(txtMa.getText().trim(), txtTen.getText().trim(), giaMon, loai, hinhFile))) {
                result = true;
                dispose();
            }
        } else {
            menuEditing.setTenMon(txtTen.getText().trim());
            menuEditing.setDonGia(giaMon);
            menuEditing.setLoaiMon(loai);
            menuEditing.setHinhAnh(hinhFile);
            if (dao.update(menuEditing)) {
                result = true;
                dispose();
            }
        }
    }

    private boolean validateForm() {
        if (txtMa.getText().trim().isEmpty() || txtTen.getText().trim().isEmpty() || txtGia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        try {
            Double.parseDouble(txtGia.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Giá phải là số hợp lệ!");
            return false;
        }
        return true;
    }

    public boolean getResult() {
        return result;
    }
}
