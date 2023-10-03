package me.letscode.minecraft.tools.nbt.gui.menu;

import me.letscode.minecraft.tools.nbt.utils.ComponentUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuBuilder implements ActionListener, MenuHandler {

	private static ResourceBundle language;

	private String name;
	private JMenu menu;
	MenuListener menuListener;

	public MenuBuilder(String name, MenuListener menuListener) {
		Objects.requireNonNull(language, "language bundle not set");
		this.name = name;
		this.menu = new JMenu(language.getString(name));
		this.menuListener = menuListener;
	}
	
	public MenuBuilder(String name, MenuPopupBuilder parent) {
		this(name, parent.menuListener);
	}

    public String getName(){
		return this.name;
	}
	
	public JMenu getJMenu() {
		return menu;
	}
	
	public MenuBuilder addMenuItem(String name, String actionCommand){
		JMenuItem item = new JMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return this;
	}
	
	public MenuBuilder addCheckboxMenuItem(String name, String actionCommand){
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return this;
	}
	
	public MenuBuilder addCheckboxMenuItem(String name, String actionCommand, boolean checked){
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setSelected(checked);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return this;
	}
	
	public MenuBuilder addCheckboxMenuItem(String name, String actionCommand, KeyStroke accelerator, boolean checked){
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setSelected(checked);
		item.setAccelerator(accelerator);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return this;
	}
	
	public MenuBuilder addCheckboxMenuItem(String name, String actionCommand, KeyStroke accelerator){
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setAccelerator(accelerator);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return this;
	}
	
	public MenuBuilder addCheckboxMenuItem(String name, String actionCommand, Icon icon, KeyStroke accelerator){
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setAccelerator(accelerator);
		item.setIcon(icon);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return this;
	}

	
	public MenuBuilder addSeperator(){
		menu.addSeparator();
		return this;
	}
	
	public MenuBuilder addMenuItem(String name, String actionCommand, KeyStroke accelerator){
		JMenuItem item = new JMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		item.setAccelerator(accelerator);
		menu.add(item);
		return this;
	}
	
	public MenuBuilder addMenuItem(String name, String actionCommand, Icon icon, KeyStroke accelerator){
		JMenuItem item = new JMenuItem(language.getString(name));
		item.setIcon(icon);
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		item.setAccelerator(accelerator);
		menu.add(item);
		return this;
	}
	
	public MenuBuilder addMenuItem(String name, String actionCommand, boolean selected, KeyStroke accelerator){
		JMenuItem item = new JMenuItem(language.getString(name));
		item.setSelected(selected);
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		item.setAccelerator(accelerator);
		menu.add(item);
		return this;
	}
	
	public MenuBuilder addMenuItem(String name, String actionCommand, KeyStroke accelerator, boolean disabled){
		JMenuItem item = new JMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		item.setEnabled(!disabled);
		item.setAccelerator(accelerator);
		menu.add(item);
		return this;
	}
	
	public MenuBuilder addMenuItem(String name, String actionCommand, boolean disabled){
		JMenuItem item = new JMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		item.setEnabled(!disabled);
		menu.add(item);
		return this;
	}
	
	public MenuBuilder addMenu(MenuBuilder builder) {
		menu.add(builder.getJMenu());
		return this;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object instanceof JMenuItem){
			if (this.menuListener != null){
				this.menuListener.clicked(this, (JMenuItem) object);
			}
		}
		
	}

	@Override
	public JMenuItem getByActionCommand(String actionCommand) {
		return ComponentUtils.search(this.menu, actionCommand);
	}


	public static void setLanguageBundle(ResourceBundle languageBundle) {
		Objects.requireNonNull(languageBundle, "language bundle cannot be null!");
		language = languageBundle;
	}

	
	
}
