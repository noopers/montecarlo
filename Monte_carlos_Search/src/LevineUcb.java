


public class LevineUcb  extends Ucbselectionpolicybase{

	/**
	 * @param args
	 */
	

	@Override
	public double getucbvalue(treenode node)
	{
		return node.getAverageScore() + 40000* 
			Math.sqrt(Math.log(node.getParent().getnumberofvisit()) / node.getnumberofvisit());
	}

}
