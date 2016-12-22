import java.util.Random;


public class Mctsucb  extends Ucbselectionpolicybase{
	private static final double EPSILON = 1e-6;
	private static final Random random = new Random();
	@Override
	public double getucbvalue(treenode node) {
		// TODO Auto-generated method stub
		return (double)node.getTotalScore() / (node.getnumberofvisit() + EPSILON) 
				+ Math.sqrt(Math.log(node.getParent().getnumberofvisit() + 1) / (node.getnumberofvisit() + EPSILON))
				+ random.nextDouble() * EPSILON;
	}

}
