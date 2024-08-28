package me.letscode.minecraft.tools.nbt.gui.dialogs;

import me.letscode.minecraft.tools.nbt.gui.NBTSelectorRenderer;
import me.letscode.minecraft.tools.nbt.utils.NBTTagInfo;
import me.letscode.minecraft.tools.nbt.utils.NBTTagRegistry;
import org.jnbt.*;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class NbtEditDialog extends JDialog {

    private final ResourceBundle bundle;

    private final NBTTagInfo info;

    /// Components
    private JTextField nameField;

    private JTextField valueField;

    private JComboBox<NBTTagInfo> typeSelector;

    private boolean cancelled = true;

    public NbtEditDialog(Frame frame, ImageIcon image, ResourceBundle bundle, NBTTagInfo info) {
        super(frame, true);

        this.bundle = bundle;
        this.info = info;
        this.initialize(image, true, null);
    }

    public NbtEditDialog(Frame frame, ImageIcon image, ResourceBundle bundle, NBTTagInfo info, Tag tag) {
        super(frame, true);

        this.bundle = bundle;
        this.info = info;
        this.initialize(image, false, tag);
    }

    private void initialize(ImageIcon imageIcon, boolean create, Tag tag) {
        this.setTitle(this.bundle.getString("dialog.edit.title"));

        this.setPreferredSize(new Dimension(360, 180));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);

        this.setLayout(new BorderLayout(10, 10));
        if (imageIcon != null) {
            this.setIconImage(imageIcon.getImage());
        }

        String defaultName = "";
        String defaultValue = "0";
        if (tag != null) {
            defaultName = tag.getName();
            if (info.isArray()) {
                defaultValue = String.valueOf(tag.getValue());
            } else if (info.nbtTypeId() == NBTConstants.TYPE_LIST) {
                defaultValue = String.valueOf(tag.getValue());
            }
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        JLabel nameLbl = new JLabel(this.bundle.getString("dialog.edit.name.label"));
        panel.add(nameLbl, c);

        if (!info.hasChildren()) {
            c.gridy = 1;
            String key = "dialog.edit.value.label";
            if (this.info.isArray()) {
                key = "dialog.edit.size.label";
            }

            JLabel valueLbl = new JLabel(this.bundle.getString(key));
            panel.add(valueLbl, c);
        } else if (info.nbtTypeId() == NBTConstants.TYPE_LIST) {
            c.gridy = 1;

            JLabel valueLbl = new JLabel(this.bundle.getString("dialog.edit.selector.label"));
            panel.add(valueLbl, c);
        }

        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 2.0;

        this.nameField = new JTextField(defaultName);
        this.nameField.setEditable(create);
        panel.add(this.nameField, c);

        this.valueField = new JTextField(defaultValue);
        this.typeSelector = new JComboBox<>();

        c.gridy = 1;
        if (!info.hasChildren()) {
            panel.add(this.valueField, c);
        } else if (info.nbtTypeId() == NBTConstants.TYPE_LIST) {
            for (var info : NBTTagRegistry.getSorted()) {
                this.typeSelector.addItem(info);
            }
            this.typeSelector.setSelectedIndex(0);
            this.typeSelector.setRenderer(new NBTSelectorRenderer());
            panel.add(this.typeSelector, c);
        }

        this.add(panel, BorderLayout.CENTER);
        String key = create ? "dialog.edit.create" : "dialog.edit.confirm";

        JButton okayBtn = new JButton(this.bundle.getString(key));
        okayBtn.addActionListener((event) -> {
            if (this.validateInput()) {
                this.cancelled = false;
                this.dispose();
            }
        });

        JButton cancelBtn = new JButton(this.bundle.getString("dialog.edit.cancel"));
        cancelBtn.addActionListener((event) -> {
            this.dispose();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GRAY));
        bottomPanel.add(cancelBtn);
        bottomPanel.add(okayBtn);

        this.add(bottomPanel, BorderLayout.SOUTH);
        this.pack();
    }

    private boolean validateInput() {
        return true;
    }

    public Tag showCenter() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        if (this.cancelled) {
            return null;
        }
        else return createTag();
    }

    private Tag createTag() {
        String name = this.nameField.getText();
        String value = this.valueField.getText();

        return switch (this.info.nbtTypeId()) {
            // value is integer
            case NBTConstants.TYPE_INT -> new IntTag(name, Integer.parseInt(value));
            case NBTConstants.TYPE_BYTE -> new ByteTag(name, Byte.parseByte(value));
            case NBTConstants.TYPE_LONG -> new LongTag(name, Long.parseLong(value));

            // value is size
            case NBTConstants.TYPE_INT_ARRAY -> new IntArrayTag(name, Integer.parseInt(value));
            case NBTConstants.TYPE_BYTE_ARRAY -> new ByteArrayTag(name, Integer.parseInt(value));
            case NBTConstants.TYPE_LONG_ARRAY -> new LongArrayTag(name, Integer.parseInt(value));

            // value is real
            case NBTConstants.TYPE_FLOAT-> new FloatTag(name, Float.parseFloat(value));
            case NBTConstants.TYPE_DOUBLE -> new DoubleTag(name, Double.parseDouble(value));

            // value is different
            case NBTConstants.TYPE_COMPOUND -> new CompoundTag(name);
            case NBTConstants.TYPE_LIST -> {
                var infoType = (NBTTagInfo) this.typeSelector.getSelectedItem();
                yield new ListTag(name, infoType.tagClass());
            }
            case NBTConstants.TYPE_STRING -> new StringTag(name, value);

            default -> throw new IllegalArgumentException("invalid nbt type");
        };
    }



}
