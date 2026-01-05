package util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class XImage {

    public static ImageIcon getIcon(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return getDefaultIcon();
        }

        fileName = fileName.trim();
        URL url = XImage.class.getResource("/image/" + fileName);

        if (url != null) {
            try {
                // Sử dụng ImageIO để đọc đa định dạng (JPG, PNG, BMP, GIF, WebP...)
                BufferedImage img = ImageIO.read(url);
                if (img != null) {
                    return new ImageIcon(img);
                }
            } catch (IOException e) {
                System.err.println("Lỗi đọc file ảnh: " + fileName);
            }
        }

        System.err.println("Không tìm thấy ảnh: " + fileName);
        return getDefaultIcon();
    }

    private static ImageIcon getDefaultIcon() {
        URL url = XImage.class.getResource("/image/no_image.png");
        try {
            if (url != null) {
                return new ImageIcon(ImageIO.read(url));
            }
        } catch (IOException e) {
        }
        return null;
    }

    // Các hàm getResizedIcon và getAppIcon giữ nguyên logic cũ 
    // nhưng sẽ tự động hưởng lợi từ getIcon mới
    public static ImageIcon getResizedIcon(String fileName, int width, int height) {
        ImageIcon icon = getIcon(fileName);
        if (icon != null) {
            Image img = icon.getImage()
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        return null;
    }
}
