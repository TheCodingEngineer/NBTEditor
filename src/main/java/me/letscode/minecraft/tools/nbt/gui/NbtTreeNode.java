package me.letscode.minecraft.tools.nbt.gui;

import org.jnbt.CompoundTag;
import org.jnbt.ListTag;
import org.jnbt.Tag;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.Vector;

public class NbtTreeNode extends DefaultMutableTreeNode {

    private boolean modifyStructure = false;

    public NbtTreeNode(Tag nbtTag) {
        super(nbtTag);
        this.createChildNodes(nbtTag);
        this.modifyStructure = true;
    }


    private void createChildNodes(Tag nbtTag) {
        if (nbtTag instanceof CompoundTag compoundTag) {
            for (Tag child : compoundTag.getValue().values()) {
                this.add(new NbtTreeNode(child));
            }
        } else if (nbtTag instanceof ListTag listTag) {
            for (Tag child : listTag.getValue()) {
                this.add(new NbtTreeNode(child));
            }
        }
    }


    @Override
    public boolean getAllowsChildren() {
        return this.userObject instanceof CompoundTag || this.userObject instanceof ListTag;
    }

    public Tag getNbtTag() {
        return (Tag) this.userObject;
    }

    @Override
    public String toString() {
        Tag tag = getNbtTag();
        if (tag instanceof CompoundTag compoundTag) {
            return tag.getName() + " [" + compoundTag.getValue().size() + " entries]";
        } else if (tag instanceof ListTag listTag) {
            return tag.getName() + " [" + listTag.getValue().size() + " entries]";
        }
        return tag.getName() + ": " + tag.getValue().toString();
    }

    @Override
    public void setAllowsChildren(boolean allows) {
        throw new UnsupportedOperationException("setAllowsChildren is not supported");
    }

    @Override
    public void setUserObject(Object userObject) {
        if (userObject == null) {
            throw new NullPointerException("userObject cannot be null");
        }
        if (!(userObject instanceof Tag)) {
            throw new IllegalArgumentException("userObject must be a Tag");
        }
        super.setUserObject(userObject);
    }

    @Override
    public void remove(int childIndex) {
        var oldNode = (NbtTreeNode) this.children.get(childIndex);
        super.remove(childIndex);

        if (!modifyStructure) {
            return;
        }
        if (getNbtTag() instanceof CompoundTag compoundTag) {
            compoundTag.getValue().remove(oldNode.getNbtTag().getName());
        } else if (getNbtTag() instanceof ListTag listTag) {
            listTag.getValue().remove(childIndex);
        }
    }

    @Override
    public void insert(MutableTreeNode newChild, int childIndex) {
        super.insert(newChild, childIndex);

        if (!modifyStructure) {
            return;
        }

        var newNode = (NbtTreeNode) newChild;
        if (getNbtTag() instanceof CompoundTag compoundTag) {
            compoundTag.getValue().put(newNode.getNbtTag().getName(), newNode.getNbtTag());
        } else if (getNbtTag() instanceof ListTag listTag) {
            listTag.getValue().add(childIndex, newNode.getNbtTag());
        }
    }
}
