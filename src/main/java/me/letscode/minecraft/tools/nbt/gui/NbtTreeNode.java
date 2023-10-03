package me.letscode.minecraft.tools.nbt.gui;

import org.jnbt.CompoundTag;
import org.jnbt.ListTag;
import org.jnbt.Tag;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.Vector;

public class NbtTreeNode implements TreeNode {

    private final NbtTreeNode parent;
    private final Tag nbtTag;

    private final Vector<NbtTreeNode> children;

    public NbtTreeNode(Tag nbtTag) {
        this(null, nbtTag);
    }

    public NbtTreeNode(NbtTreeNode parent, Tag nbtTag) {
        this.parent = parent;
        this.nbtTag = nbtTag;
        this.children = this.createChildNodes(nbtTag);
    }
    private boolean allowsChildren(Tag nbtTag) {
        return nbtTag instanceof CompoundTag || nbtTag instanceof ListTag;
    }

    private Vector<NbtTreeNode> createChildNodes(Tag nbtTag) {
        Vector<NbtTreeNode> vector = new Vector<>();
        if (nbtTag instanceof CompoundTag compoundTag) {
            for (Tag child : compoundTag.getValue().values()) {
                vector.add(new NbtTreeNode(this, child));
            }
        } else if (nbtTag instanceof ListTag listTag) {
            for (Tag child : listTag.getValue()) {
                vector.add(new NbtTreeNode(this, child));
            }
        }
        return vector;
    }


    @Override
    public TreeNode getChildAt(int i) {
        if (i >= 0 && i < getChildCount()) {
            return this.children.get(i);
        }
        return null;
    }

    @Override
    public int getChildCount() {
        return this.children.size();
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public int getIndex(TreeNode treeNode) {
        return this.children.indexOf(treeNode);
    }

    @Override
    public boolean getAllowsChildren() {
        return this.allowsChildren(nbtTag);
    }

    @Override
    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    @Override
    public Enumeration<? extends TreeNode> children() {
        return this.children.elements();
    }

    public Tag getNbtTag() {
        return nbtTag;
    }

    @Override
    public String toString() {
        if (nbtTag instanceof CompoundTag compoundTag) {
            return nbtTag.getName() + " [" + compoundTag.getValue().size() + " entries]";
        } else if (nbtTag instanceof ListTag listTag) {
            return nbtTag.getName() + " [" + listTag.getValue().size() + " entries]";
        }
        return nbtTag.getName() + ": " + nbtTag.getValue().toString();
    }

    @Override
    public int hashCode() {
        return nbtTag.hashCode();
    }
}
