import java.util.Collection;


public abstract class Ucbselectionpolicybase implements SelectionPolicy {

	@Override
	public treenode Select(treenode node) {
		// TODO Auto-generated method stub
		
		Collection<treenode> children=node.getChildren();
		
		treenode selectedchild=null;
		double max=Double.NEGATIVE_INFINITY;
		double currentucb=0;
		if(children==null){
			throw new NullPointerException("children is not present to apply policy");
		}
		
		
		
		for(treenode ch:children){
			currentucb=getucbvalue(ch);
			if(Double.isNaN(currentucb)){
				throw new NullPointerException("Value not calculated");
			}
			
			else {
				if(currentucb>max){
					max=currentucb;
					selectedchild=ch;
				}
			}
			
			
		}
		
		
		return selectedchild;
	}

	@Override
	public boolean getEvaluateAllChildrenOnExpansion() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @param args
	 */

	public abstract double getucbvalue(treenode node);


}
