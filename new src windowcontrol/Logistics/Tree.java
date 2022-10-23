package Logistics;

import java.util.Stack;

public class Tree {
	public int value;//priority
	public int goodsID;
	public Tree parent;
	public Tree left;
	public Tree right;
	public Tree() {};
	public Tree(int value, int goodsID) {
		this.value = value;
		this.goodsID = goodsID;
	}
	
	public Tree insert(Tree root,Tree p) {
		if(root==null) {
			root=p;
			root.parent=null;
			return root;
		}
		Tree temp=new Tree();
		temp=root;
		while(temp!=null) {
			if(temp.value<p.value) {
				if(temp.right!=null)  {
					temp=temp.right;
					}
					else {
						temp.right=new Tree();
						temp.right.value=p.value;
						temp.right.parent=temp;
						break;}
				}
				if(temp.value>p.value) {
					if(temp.left!=null) {
						temp=temp.left;
						}
					else{
						temp.left=new Tree();
						temp.left.value=p.value;
						temp.left.parent=temp;
						break;
						}
					}
		}
		return root;
	}
	
	public Tree Select(Tree root,int value) {
		Tree flag=new Tree();
		Stack<Tree> stack=new Stack<Tree>();
		while(!stack.empty()||root!=null)
		{
			if(root!=null) {
			if(root.value==value)
			{
				flag=root;
			}
			stack.push(root);
			root=root.left;
			}
			else {
				root=stack.pop();
				if(root.value==value)
				{
					flag=root;
				}
				root=root.right;
			}
		}
		return flag;
	}
	
	public boolean DeleteBST(Tree root, int key) {
		
		if(root == null) {
			System.out.println("the root node is null");
			return false;
		}
		else {
			if(key == root.value) {
				return this.delete(root);
			}
			else if(key < root.value) {
				 return this.DeleteBST(root.left, key);
			}
			else {
				 return this.DeleteBST(root.right, key);
			}
		}
	}
	
	public boolean delete(Tree root) {
		Tree q, s;
		if(root.right == null) {
			root = root.left;	
		}
		else if(root.left == null) {
			root = root.right;
		}
		else {
			q = root;
			s = root.left;
			while(s.right != null) {
				q = s;
				s = s.right;
			}
			root.value = s.value;
			if(q != root) {
				q.right = s.left;
			}
			else {
				q.left = s.left;
			}
		}	
		return true;
	}

}
