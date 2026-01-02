package view;

import DAO.MenuDao;
import Dialog.DlgMenu;
import entity.Menu;
import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class FrmMenu extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTimKiem;
    private JComboBox<String> cboLocLoai; // Bộ lọc loại món
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    private MenuDao dao = new MenuDao();
    private List<Menu> fullList = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#,###");

    public FrmMenu() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1000, 650));

        initToolbar();
        initTable();
        loadTable();
        addEvents();
    }

    private void initToolbar() {
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(Color.WHITE);
        pnlTop.setBorder(new EmptyBorder(10, 20, 10, 20));
        pnlTop.setPreferredSize(new Dimension(0, 130));

        // --- NHÓM CHỨC NĂNG ---
        JPanel pnlChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlChucNang.setBackground(Color.WHITE);
        pnlChucNang.setBorder(createGroupBorder("Chức năng"));

        btnThem = createToolButton("Thêm", new Color(46, 204, 113));
        btnSua = createToolButton("Sửa", new Color(241, 196, 15));
        btnXoa = createToolButton("Xóa", new Color(231, 76, 60));

        pnlChucNang.add(btnThem);
        pnlChucNang.add(btnSua);
        pnlChucNang.add(btnXoa);

        // --- NHÓM LỌC & TÌM KIẾM ---
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pnlTimKiem.setBackground(Color.WHITE);
        pnlTimKiem.setBorder(createGroupBorder("Bộ lọc danh sách"));

        // ComboBox lọc loại món
        cboLocLoai = new JComboBox<>(new String[]{"Tất cả loại", "Bánh", "Trà sữa", "Cafe", "Matcha", "Nước ép", "Sinh tố", "Khác"});
        cboLocLoai.setPreferredSize(new Dimension(150, 40));
        cboLocLoai.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Ô nhập tìm kiếm chung (Tất cả)
        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(200, 40));
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btnLamMoi = createToolButton("Làm mới", new Color(52, 152, 219));

        JLabel lblLoc = new JLabel("Lọc loại:");
        lblLoc.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlTimKiem.add(lblLoc);
        pnlTimKiem.add(cboLocLoai);

        JLabel lblTim = new JLabel("Tìm kiếm:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnlTimKiem.add(lblTim);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnLamMoi);

        pnlTop.add(pnlChucNang, BorderLayout.WEST);
        pnlTop.add(pnlTimKiem, BorderLayout.EAST);
        add(pnlTop, BorderLayout.NORTH);
    }

    private TitledBorder createGroupBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1), title);
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        border.setTitleColor(new Color(100, 100, 100));
        return border;
    }

    private JButton createToolButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setForeground(color);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 2),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(color);
                btn.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(color);
            }
        });
        return btn;
    }

    private void initTable() {
        String[] cols = {"Mã", "Hình ảnh", "Thông tin sản phẩm"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                return (c == 1) ? ImageIcon.class : Object.class;
            }
        };

        table = new JTable(model);
        table.setRowHeight(100);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setMaxWidth(120);

        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                JPanel pnl = new JPanel(new GridLayout(2, 1));
                pnl.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                pnl.setBorder(new EmptyBorder(10, 20, 10, 10));
                if (value instanceof Menu) {
                    Menu m = (Menu) value;
                    JLabel n = new JLabel(m.getTenMon());
                    n.setFont(new Font("Segoe UI", Font.BOLD, 16));
                    JLabel g = new JLabel(df.format(m.getDonGia()) + " VNĐ");
                    g.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    g.setForeground(new Color(231, 76, 60));
                    pnl.add(n);
                    pnl.add(g);
                }
                return pnl;
            }
        });

        // Thay đổi trong hàm initTable()
        table.setSelectionBackground(new Color(185, 211, 238)); 
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(230, 230, 230));
        table.setShowVerticalLines(false);
        
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(new EmptyBorder(0, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);
    }

    private void loadTable() {
        fullList = dao.selectAll();
        hienThiDanhSach(fullList);
    }

    private void hienThiDanhSach(List<Menu> list) {
        model.setRowCount(0);
        for (Menu m : list) {
            ImageIcon icon = null;
            if (m.getHinhAnh() != null && new File(m.getHinhAnh()).exists()) {
                Image img = new ImageIcon(m.getHinhAnh()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
            }
            model.addRow(new Object[]{m.getMaMenu(), icon, m});
        }
    }

    private void timKiem() {
        String keyword = txtTimKiem.getText().trim().toLowerCase();
        String loaiChon = cboLocLoai.getSelectedItem().toString();

        List<Menu> ketQua = new ArrayList<>();

        for (Menu m : fullList) {
            // Lọc theo ComboBox Loại món
            boolean matchLoai = loaiChon.equals("Tất cả loại") || m.getLoaiMon().equals(loaiChon);

            // Tìm kiếm văn bản (Tên hoặc Giá)
            boolean matchKey = m.getTenMon().toLowerCase().contains(keyword)
                        || String.valueOf(m.getDonGia()).contains(keyword);

            if (matchLoai && matchKey) {
                ketQua.add(m);
            }
        }
        hienThiDanhSach(ketQua);
    }

    private void addEvents() {
        btnThem.addActionListener(e -> {
            Window parent = SwingUtilities.getWindowAncestor(this);
            DlgMenu dlg = new DlgMenu(parent, null);
            dlg.setVisible(true);
            if (dlg.getResult()) {
                loadTable();
            }
        });

        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
                return;
            }
            Menu m = (Menu) model.getValueAt(row, 2);
            DlgMenu dlg = new DlgMenu(SwingUtilities.getWindowAncestor(this), m);
            dlg.setVisible(true);
            if (dlg.getResult()) {
                loadTable();
            }
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }
            String ma = model.getValueAt(row, 0).toString();
            if (JOptionPane.showConfirmDialog(this, "Xóa món này?", "Xác nhận", 0) == 0) {
                if (dao.delete(ma)) {
                    loadTable();
                }
            }
        });

        btnLamMoi.addActionListener(e -> {
            txtTimKiem.setText("");
            cboLocLoai.setSelectedIndex(0);
            loadTable();
        });

        // Sự kiện khi chọn Loại món
        cboLocLoai.addActionListener(e -> timKiem());

        // Sự kiện tìm kiếm Real-time
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiem();
            }
        });
    }
}
