package me.letscode.minecraft.tools.nbt.gui;

import me.letscode.minecraft.tools.nbt.gui.menu.MenuBuilder;
import me.letscode.minecraft.tools.nbt.gui.menu.MenuPopupBuilder;
import me.letscode.minecraft.tools.nbt.utils.*;
import org.jnbt.CompoundTag;
import org.jnbt.NBTConstants;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class GuiNbtEditor extends JFrame {

    static {
        System.setProperty("java.util.PropertyResourceBundle.encoding", "ISO-8859-1");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot change window style: \n"
                            + StringHelper.toString(e), "Error on initializing",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private File currentDirectory;

    private NbtTabPanel tabPanel;

    private JMenu fileMenu;

    private ResourceBundle guiTranslations;

    private boolean unsavedModifications = false;

    public GuiNbtEditor() {
        init();
    }

    private void init() {
        try {
            this.guiTranslations = ResourceBundle.getBundle("lang.gui", Locale.getDefault());
        } catch (MissingResourceException e) {
            JOptionPane.showMessageDialog(null, "An error occurred: \n"
                            + StringHelper.toString(e) + "\n\nPlease check your file!", "Error on initializing",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }

        setTitle(this.guiTranslations.getString("gui.title"));

        setLayout(new BorderLayout());
        setIconImage(Resources.getImage("/images/compound.png").getImage());

        addComponents();

        setPreferredSize(new Dimension(840, 480));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitProgram();
            }
        });

        String home = System.getenv("HOME"); // Linux, MacOS (Darwin)
        if (home == null) {
            home = System.getenv("userprofile"); // Windows
        }

        this.currentDirectory = new File(home);
        if (!this.currentDirectory.exists() || this.currentDirectory.isFile()) {
            this.currentDirectory = new File(System.getProperty("user.dir"));
        }
        this.pack();
    }

    private void addComponents() {
        MenuBuilder.setLanguageBundle(this.guiTranslations);
        MenuPopupBuilder.setLanguageBundle(this.guiTranslations);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        var fileBuilder = new MenuBuilder("menu.file", (handler, item) -> {
            if (item.getActionCommand() == null) {
                return;
            }
            switch (item.getActionCommand()) {
                case "open" -> {
                    JFileChooser nbtChooser = new JFileChooser(currentDirectory);
                    nbtChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    nbtChooser.setMultiSelectionEnabled(true);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(this.guiTranslations.getString("dialog.open.filter"),
                            "nbt", "dat", "mca", "mcr");
                    nbtChooser.setFileFilter(filter);

                    int returnVal = nbtChooser.showDialog(this, this.guiTranslations.getString("dialog.open.title"));
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        currentDirectory = nbtChooser.getCurrentDirectory();
                        for (var file : nbtChooser.getSelectedFiles()) {
                            this.tabPanel.addFile(file);
                        }
                    }
                }
                case "close" -> {
                    this.tabPanel.closeCurrentFile();
                }
                case "quit" -> {
                    quitProgram();
                }
            }
        })
        .addMenuItem("menu.file.open", "open", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK))
        .addMenuItem("menu.file.close", "close", true)
        .addSeperator()
        .addMenuItem("menu.file.quit", "quit");

        menuBar.add(this.fileMenu = fileBuilder.getJMenu());

        JToolBar toolBar = new JToolBar();
        setupToolBar(toolBar);

        this.tabPanel = new NbtTabPanel(this);

        add(toolBar, BorderLayout.NORTH);
        add(this.tabPanel, BorderLayout.CENTER);
    }


    public JMenuItem getByActionCommand(String actionCommand) {
        return ComponentUtils.search(this.fileMenu, actionCommand);
    }

    private void quitProgram() {
        if (unsavedModifications) {
            int returnVal = JOptionPane.showConfirmDialog(this,
                    this.guiTranslations.getString("dialog.unsaved.message"), this.guiTranslations.getString("dialog.unsaved.title"),
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (returnVal == JOptionPane.NO_OPTION) {
                return;
            }
        }
        dispose();
        System.exit(0);
    }



    private void setupToolBar(JToolBar toolBar) {
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        for (NBTTagInfo info : NBTTagRegistry.getSorted()) {
            var image = Resources.getImage(info.imagePath());

            JButton button = new JButton(Resources.getScaledImage(image, 24, 24));
            //button.setPreferredSize(new Dimension(32, 32));
            button.setActionCommand("node." + info.name().toLowerCase());

            toolBar.add(button);
            if (info.nbtTypeId() == NBTConstants.TYPE_DOUBLE || info.nbtTypeId() == NBTConstants.TYPE_LONG_ARRAY) {
                toolBar.addSeparator(new Dimension(3, 10));
            }
        }
    }


}
