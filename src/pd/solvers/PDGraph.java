package pd.solvers;

import java.util.ArrayList;

import pd.utils.Direction;

public class PDGraph {
	private static class PDNode
	{
		Direction d;
		ArrayList<PDNode> to;
		ArrayList<PDNode> from;
		
		PDNode(Direction d)
		{
			this.d = d;
			to = new ArrayList<PDNode>();
			from = new ArrayList<PDNode>();
		}
	}
	
	
}
