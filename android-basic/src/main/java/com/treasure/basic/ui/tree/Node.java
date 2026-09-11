package com.treasure.basic.ui.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * author : dongyan
 * time   : 2023/9/16
 */
public class Node<T> {
	private T mData;
	private long id;
	private String sort;
	private String type;

	/**
	 * 根节点pId为0
	 */
	private long pId = 0;

	private String name;

	/**
	 * 当前的级别
	 */
	private int level;

	/**
	 * 是否展开
	 */
	private boolean isExpand = true;

	private int icon;

	/**
	 * 下一级的子Node
	 */
	private List<Node> children = new ArrayList<Node>();

	/**
	 * 父Node
	 */
	private Node parent;

	public Node() {
	}

	public Node(long id, long pId, String name,T data) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.mData=data;
	}

	public T getmData() {
		return mData;
	}

	public void setmData(T mData) {
		this.mData = mData;
	}

	public int getIcon()
	{
		return icon;
	}

	public void setIcon(int icon)
	{
		this.icon = icon;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getpId()
	{
		return pId;
	}

	public void setpId(long pId)
	{
		this.pId = pId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public boolean isExpand()
	{
		return isExpand;
	}

	public List<Node> getChildren()
	{
		return children;
	}

	public void setChildren(List<Node> children)
	{
		this.children = children;
	}

	public Node getParent()
	{
		return parent;
	}

	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 是否为跟节点
	 * 
	 * @return
	 */
	public boolean isRoot()
	{
		return parent == null;
	}

	/**
	 * 判断父节点是否展开
	 * 
	 * @return
	 */
	public boolean isParentExpand()
	{
		if (parent == null)
			return false;
		return parent.isExpand();
	}

	/**
	 * 是否是叶子界点
	 * 
	 * @return
	 */
	public boolean isLeaf()
	{
		return children.size() == 0;
	}

	/**
	 * 获取level
	 */
	public int getLevel()
	{
		return parent == null ? 0 : parent.getLevel() + 1;
	}

	/**
	 * 设置展开
	 * 
	 * @param isExpand
	 */
	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
		if (!isExpand) {
			for (Node node : children) {
				node.setExpand(isExpand);
			}
		}
	}
}
