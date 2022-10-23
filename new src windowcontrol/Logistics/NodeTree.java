package Logistics;

public class NodeTree {
    Goods data; //���ڵ�����
    NodeTree left; //������
    NodeTree right; //������

    public NodeTree() {
        super();
    }

    public NodeTree(Goods data) { //ʵ����������
        super();
        this.data = data;
        left = null;
        right = null;
    }

    public void insert(NodeTree root, Goods data) {
        if (data.priority > root.data.priority) { //�������Ľڵ���ڸ��ڵ�
            if (root.right == null) {          //���������Ϊ�գ��Ͳ��룬�����Ϊ�վ��ٴ���һ���ڵ�
                root.right = new NodeTree(data); //�ͰѲ���Ľڵ�����ұ�
            } else {
                this.insert(root.right, data);
            }
        } else {  //�������Ľڵ�С�ڸ��ڵ�
            if (root.left == null) { //���������Ϊ�գ��Ͳ��룬�����Ϊ�վ��ٴ���һ���ڵ�
                root.left = new NodeTree(data); //�ͰѲ���Ľڵ������߱�
            } else {
                this.insert(root.left, data);
            }
        }
    }
    
    public boolean delete(NodeTree root) {
		NodeTree q, s;
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
			root.data = s.data;
			if(q != root) {
				q.right = s.left;
			}
			else {
				q.left = s.left;
			}
		}	
		return true;
	}

    public static void preOrder(NodeTree root) { // �ȸ�����
        if (root != null) {
            System.out.println(root.data + "-");
            System.out.println();
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    public static void inOrder(NodeTree root) { // �и�����

        if (root != null) {
            inOrder(root.left);
            System.out.print(root.data + "--");
            inOrder(root.right);
        }
    }

    public static void postOrder(NodeTree root) { // �������

        if (root != null) {
            postOrder(root.left);
            postOrder(root.right);
            System.out.print(root.data + "---");
        }
    }
}

