package me.letscode.minecraft.tools.nbt.gui.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuPopupBuilder implements ActionListener, MenuHandler {

	private static ResourceBundle language;

	private String name;
	private JPopupMenu menu;
	MenuListener menuListener;

	public MenuPopupBuilder(String name, MenuListener menuListener) {
		Objects.requireNonNull(language, "language bundle not set");
		this.name = name;
		this.menu = new JPopupMenu(language.getString(name));
		this.menuListener = menuListener;
	}
	
	public String getName(){
		return this.name;
	}
	
	public JPopupMenu getJPopupMenu() {
		return menu;
	}
	
	public MenuPopupBuilder addMenuItem(String name, String actionCommand){
		JMenuItem item = new JMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return this;
	}
	
	public MenuPopupBuilder addCheckboxMenuItem(String name, String actionCommand){
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return this;
	}
	
	public MenuPopupBuilder addCheckboxMenuItem(String name, String actionCommand, boolean checked){
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setSelected(checked);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return this;
	}
	
	public MenuPopupBuilder addCheckboxMenuItem(String name, String actionCommand, Icon icon){
		JCheckBoxMenuItem item = new JCheckBoxMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setIcon(icon);
		item.setActionCommand(actionCommand);
		menu.add(item);
		return this;
	}

	
	public MenuPopupBuilder addSeparator(){
		menu.addSeparator();
		return this;
	}
	
	
	public MenuPopupBuilder addMenuItem(String name, String actionCommand, boolean disabled){
		JMenuItem item = new JMenuItem(language.getString(name));
		item.addActionListener(this);
		item.setActionCommand(actionCommand);
		item.setEnabled(!disabled);
		menu.add(item);
		return this;
	}
	
	public MenuPopupBuilder addMenu(MenuBuilder builder) {
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
		for (int i = 0; i < menu.getComponentCount(); i++){
			Component component = menu.getComponent(i);
			if (component instanceof JMenuItem){
				JMenuItem item = (JMenuItem) component;
				if (item.getActionCommand() != null && item.getActionCommand().equals(actionCommand)){
					return item;
				}
			}
			
		}
		return null;
	}

	public static void setLanguageBundle(ResourceBundle languageBundle) {
		Objects.requireNonNull(languageBundle, "language bundle cannot be null!");
		language = languageBundle;
	}
	
}
