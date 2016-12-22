
public class Ucb1policy extends Ucbselectionpolicybase {

	@Override
	public double getucbvalue(treenode node) {
		// TODO Auto-generated method stub
		return node.getAverageScore() + Math.sqrt(2 * Math.log(node.getParent().getnumberofvisit()) / node.getnumberofvisit());
		
	}

	/**
	 * @param args
	 */
	

}
