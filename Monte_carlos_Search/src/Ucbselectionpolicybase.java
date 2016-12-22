import java.util.Collection;


public abstract class Ucbselectionpolicybase implements SelectionPolicy {

	@Override
	public treenode Select(treenode node) {
		// TODO Auto-generated method stub
		
		Collection<treenode> children=node.getChildren();
		
		treenode selectedchild=null;
		double max=Double.MIN_VALUE;
		double currentucb=0;
		if(children==null){
			throw new NullPointerException("children is not present to apply policy");
		}
		
		
		
		for(treenode ch:children){
			currentucb=getucbvalue(ch);
			if(Double.isNaN(currentucb)){
			throw new NumberFormatException("CurrentUcd is Not a value ");	
			}
			
			else {
				if(currentucb>max){
					max=currentucb;
					selectedchild=node;
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
