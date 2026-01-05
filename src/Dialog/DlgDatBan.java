/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dialog;

/**
 *
 * @author ThinkPad
 */


import DAO.BanDao;
import DAO.DatBanDao;
import entity.Ban;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DlgDatBan extends JDialog {
    private JTextField txtTenKH, txtSDT, txtGioHen;
    private JComboBox<String> cboBan;
    private JButton btnLuu, btnHuy;
    private boolean result = false;
    private DatBanDao datBanDao = new DatBanDao();
    private BanDao banDao = new BanDao();

    public DlgDatBan(Window parent) {
        super(parent, "Thông Tin Đặt Bàn", ModalityType.APPLICATION_MODAL);
        setLayout(new BorderLayout());
        setSize(400, 450);
        setLocationRelativeTo(parent);

        initComponents();
    }

    private void initComponents() {
        JPanel pnlMain = new JPanel(new GridBagLayout());
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlMain.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        
        // --- Tên khách hàng ---
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblTen = new JLabel("Tên khách hàng:"); lblTen.setFont(labelFont);
        pnlMain.add(lblTen, gbc);
        
        gbc.gridy = 1;
        txtTenKH = new JTextField(20);
        txtTenKH.setPreferredSize(new Dimension(0, 35));
        pnlMain.add(txtTenKH, gbc);

        // --- Số điện thoại ---
        gbc.gridy = 2;
        JLabel lblSdt = new JLabel("Số điện thoại:"); lblSdt.setFont(labelFont);
        pnlMain.add(lblSdt, gbc);
        
        gbc.gridy = 3;
        txtSDT = new JTextField();
        txtSDT.setPreferredSize(new Dimension(0, 35));
        pnlMain.add(txtSDT, gbc);

        // --- Giờ hẹn ---
        gbc.gridy = 4;
        JLabel lblGio = new JLabel("Giờ hẹn (yyyy-MM-dd HH:mm):"); lblGio.setFont(labelFont);
        pnlMain.add(lblGio, gbc);
        
        gbc.gridy = 5;
        txtGioHen = new JTextField("2026-01-05 18:00:00");
        txtGioHen.setPreferredSize(new Dimension(0, 35));
        pnlMain.add(txtGioHen, gbc);

        // --- Chọn bàn trống ---
        gbc.gridy = 6;
        JLabel lblBan = new JLabel("Chọn bàn trống:"); lblBan.setFont(labelFont);
        pnlMain.add(lblBan, gbc);
        
        gbc.gridy = 7;
        cboBan = new JComboBox<>();
        cboBan.setPreferredSize(new Dimension(0, 35));
        loadBanTrong();
        pnlMain.add(cboBan, gbc);

        // --- Nút bấm ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlButtons.setBackground(new Color(245, 245, 245));
        btnLuu = new JButton("Xác nhận");
        btnLuu.setBackground(new Color(46, 204, 113));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        btnHuy = new JButton("Hủy bỏ");
        
        btnLuu.addActionListener(e -> {
            if(validateInput()) {
                String ten = txtTenKH.getText().trim();
                String sdt = txtSDT.getText().trim();
                String gio = txtGioHen.getText().trim();
                String maBan = cboBan.getSelectedItem().toString();
                
                if(datBanDao.insertDatBan(ten, sdt, gio, maBan)) {
                    result = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi đặt bàn!");
                }
            }
        });
        
        btnHuy.addActionListener(e -> dispose());

        pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);

        add(pnlMain, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    private void loadBanTrong() {
        List<Ban> list = banDao.getDanhSachBanTrong();
        for (Ban b : list) {
            cboBan.addItem(b.getMaBan());
        }
    }

    private boolean validateInput() {
        if (txtTenKH.getText().isEmpty() || txtSDT.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return false;
        }
        if (cboBan.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Không có bàn trống để đặt!");
            return false;
        }
        return true;
    }

    public boolean getResult() { return result; }
}
