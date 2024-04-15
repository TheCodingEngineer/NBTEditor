package me.letscode.minecraft.tools.nbt.gui.dialogs;

import me.letscode.minecraft.tools.nbt.utils.Resources;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AboutDialog extends JDialog {

    private static final String ABOUT_TEXT = "<html><h1>%s NBTEditor v%s</h1><p>%s<br><br>&copy; 2023-%d %s<br>%s</p></html>";

    private static final String VERSION = "1.0.0"; // TODO: get version form manifest

    private final ResourceBundle bundle;

    public AboutDialog(Frame frame, ImageIcon image, ResourceBundle bundle) {
        super(frame, true);

        this.bundle = bundle;
        this.initialize(image);
    }

    private void initialize(ImageIcon imageIcon) {
        this.setTitle(this.bundle.getString("dialog.about.title"));

        this.setPreferredSize(new Dimension(680, 360));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);

        this.setLayout(new BorderLayout(10, 10));
        if (imageIcon != null) {
            this.setIconImage(imageIcon.getImage());

            JLabel imageLabel = new JLabel(Resources.getScaledImage(imageIcon, 128, 128));
            imageLabel.setPreferredSize(new Dimension(128, 128));

            this.add(imageLabel, BorderLayout.WEST);
        }

        JLabel descLabel = new JLabel(String.format(ABOUT_TEXT,
                this.bundle.getString("dialog.about.title"),
                VERSION,
                this.bundle.getString("dialog.about.description"),
                LocalDate.now().getYear(),
                this.bundle.getString("dialog.about.authors"),
                this.bundle.getString("dialog.about.license")
        ));
        this.add(descLabel, BorderLayout.CENTER);

        JButton button = new JButton(this.bundle.getString("dialog.about.close"));
        button.addActionListener((event) -> {
            this.dispose();
        });
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY));
        bottomPanel.add(button);

        this.add(bottomPanel, BorderLayout.SOUTH);
        this.pack();
    }

    public void showCenter() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


}
