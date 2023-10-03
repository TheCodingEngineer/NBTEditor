package me.letscode.minecraft.tools.nbt.gui;


import me.letscode.minecraft.tools.nbt.gui.components.CloseTabComponent;
import me.letscode.minecraft.tools.nbt.utils.NBTTagInfo;
import me.letscode.minecraft.tools.nbt.utils.NBTTagRegistry;
import me.letscode.minecraft.tools.nbt.utils.Resources;
import me.letscode.minecraft.tools.nbt.utils.StringHelper;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NbtTabPanel extends JTabbedPane {

    private final GuiNbtEditor parent;

    private final Map<Integer, ImageIcon> nbtIcons;

    public NbtTabPanel(GuiNbtEditor parent) {
        this.parent = parent;
        this.nbtIcons = new HashMap<>();

        this.init();
    }

    private void init() {
        for (NBTTagInfo info : NBTTagRegistry.getValues()) {
            var image = Resources.getImage(info.imagePath());

            this.nbtIcons.put(info.nbtTypeId(), Resources.getScaledImage(image, 16, 16));
        }

        var dropAdapter = (new DropTargetAdapter() {

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                boolean accept = false;
                Transferable t = dtde.getTransferable();
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        accept = true;
                        if (t.getTransferData(DataFlavor.javaFileListFlavor) instanceof List<?> list) {
                            for (var item : list) {
                                if (!(item instanceof File file)) {
                                    accept = false;
                                    break;
                                }
                            }
                        }
                    } catch (UnsupportedFlavorException | IOException e) {
                        e.printStackTrace();
                    }
                }
                if (accept) {
                    dtde.acceptDrag(DnDConstants.ACTION_COPY);
                } else {
                    dtde.rejectDrag();
                }
            }

            @Override
            public void drop(DropTargetDropEvent dropTargetDropEvent) {
                Transferable t = dropTargetDropEvent.getTransferable();
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY);
                    try {
                        List<String> paths = new ArrayList<>();
                        if (t.getTransferData(DataFlavor.javaFileListFlavor) instanceof List<?> list) {
                            for (var item : list) {
                                if (item instanceof File file) {
                                    paths.add(file.getAbsolutePath());
                                }
                            }
                        }

                        paths.forEach((path) -> addFile(new File(path)));
                        //JOptionPane.showMessageDialog(parent, "Files: \n" + String.join("\n", paths));
                    } catch (UnsupportedFlavorException | IOException e) {
                        JOptionPane.showMessageDialog(null, "An error occurred: \n"
                                        + StringHelper.toString(e) + "\n\nPlease check your dropped file!", "Error on dropping",
                                JOptionPane.ERROR_MESSAGE);                    }
                } else {
                    dropTargetDropEvent.rejectDrop();
                }
            }
        });

        setDropTarget(new DropTarget(this, DnDConstants.ACTION_COPY, dropAdapter, true));
    }

    public void addFile(File file) {
        var panel = new NbtPanel(this, file);
        this.add(file.getName(), new JLabel(file.getName()));

        int latestIndex = this.getTabCount() - 1;
        this.setTabComponentAt(latestIndex, new CloseTabComponent(this));
        var scrollContainer = new JScrollPane(panel);
        scrollContainer.getVerticalScrollBar().setUnitIncrement(10);
        scrollContainer.getHorizontalScrollBar().setUnitIncrement(10);
        this.setComponentAt(latestIndex, scrollContainer);

        parent.getByActionCommand("close").setEnabled(this.hasSelectedFile());
    }

    public void closeCurrentFile() {
        if (this.hasSelectedFile()) {
            removeTabAt(this.getSelectedIndex());

            parent.getByActionCommand("close").setEnabled(this.hasSelectedFile());
        }
    }

    public void closeFile(int index) {
        if (this.hasSelectedFile()) {
            removeTabAt(index);

            parent.getByActionCommand("close").setEnabled(this.hasSelectedFile());
        }
    }

    public boolean hasSelectedFile() {
        return this.getSelectedIndex() != -1;
    }

    public Map<Integer, ImageIcon> getNbtIcons() {
        return nbtIcons;
    }
}
