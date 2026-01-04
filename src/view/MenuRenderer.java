package view;

import entity.ChiTietDatBan;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.Locale;

public class MenuRenderer extends DefaultListCellRenderer {

    private NumberFormat vnMoney = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof ChiTietDatBan) {
            ChiTietDatBan item = (ChiTietDatBan) value;

            // 1. Hiển thị Text bằng HTML 
            String html = "<html>"
                    + "<div style='padding:5px;'>"
                    + "<b style='font-size:12px; color:#003366;'>"
                    + item.getTenMon()
                    + "</b><br/>"
                    + "<span style='color:red; font-weight:bold;'>"
                    + vnMoney.format(item.getDonGia())
                    + "</span>"
                    + "</div></html>";
            setText(html);

            // 2. ĐỒNG BỘ ẢNH: Đọc từ đường dẫn tuyệt đối 
            String path = item.getHinhAnh(); 
            if (path != null && !path.isEmpty()) {
                File f = new File(path);
                if (f.exists()) {
                    // Nếu file tồn tại trên máy tính, nạp và scale ảnh
                    ImageIcon imgIcon = new ImageIcon(path);
                    Image img = imgIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    setIcon(new ImageIcon(img));
                } else {
                    // Nếu đường dẫn sai, dùng icon mặc định
                    setIcon(util.XImage.getResizedIcon("logo_cafe.png", 70, 70));
                }
            } else {
                // Nếu không có đường dẫn ảnh, dùng icon mặc định
                setIcon(util.XImage.getResizedIcon("logo_cafe.png", 70, 70));
            }

            // 3. Tùy chỉnh khoảng cách và màu sắc lựa chọn
            setIconTextGap(15);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            
            if (isSelected) {
                setBackground(new Color(0, 120, 215)); 
                setForeground(Color.WHITE);
            } else {
                setBackground(index % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                setForeground(Color.BLACK);
            }
        }
        return this;
    }
}