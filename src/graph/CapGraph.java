/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author Your name here.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */

public class CapGraph implements Graph {

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	
	HashSet<Integer> vertices;
	HashMap<Integer, HashSet<Integer>> edges;
	
	
	public CapGraph() {
		vertices= new HashSet<Integer>();
		edges= new HashMap<Integer, HashSet<Integer>>();
	}
	
	@Override
	public void addVertex(int num) {
		
		vertices.add(num);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		
		if(edges.containsKey(from)) {
			edges.get(from).add(to);
		}
		
		else {
			HashSet<Integer> adj=  new HashSet<Integer>();
			adj.add(to);
			edges.put(from, adj);
		}
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	@Override
	public Graph getEgonet(int center) {
		
		
		if(!vertices.contains(center))
			return null;
		
		Graph egonet= new CapGraph();
		egonet.addVertex(center);
		
		for(int i: edges.get(center)) {
			egonet.addVertex(i);
			egonet.addEdge(center, i);
			for(int j: ((CapGraph)egonet).vertices) {
				if(edges.containsKey(i) && edges.get(i).contains(j))
					egonet.addEdge(i, j);
				if(edges.containsKey(j) && edges.get(j).contains(i))
					egonet.addEdge(j, i);
			}
		}
		
		return egonet;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
	
		Stack<Integer> visited= new Stack<Integer>(); 
		Stack<Integer> finished= new Stack<Integer>();
		
		HashSet<Integer> current= new HashSet<Integer>();
		List<Graph> ret= new LinkedList<Graph>();
		
		for(int i: vertices) {
			if(!visited.contains(i))
				dfs(i, visited, finished);
		}
		
		CapGraph thisTranspose= transpose();
		visited= new Stack<Integer>();
		
		while(!finished.isEmpty()) {
			int i= finished.pop();
			if(!visited.contains(i)) {
				thisTranspose.dfsNew(i, visited, current);
				
				Graph t= getGraph(current);
				if(t!=null)
					ret.add(t);
				current.clear();
			}	
		}
		
		return ret;
	}
	
	
	private CapGraph getGraph(HashSet<Integer> current) {
		
		CapGraph ret= new CapGraph();
		
		for(int i: current) {
			ret.vertices.add(i);
			if(edges.containsKey(i)) {
				ret.edges.put(i, edges.get(i));
			}
		}
		
		return ret;
	}
	
	
	private void dfsNew(int i, Stack<Integer> visited, HashSet<Integer> current) {
		
		visited.push(i);
		current.add(i);
		if(!edges.containsKey(i) || edges.get(i).isEmpty()) {
			return;
		}
		
		HashSet<Integer> adj= edges.get(i);
		for(int j: adj) {
			if(!visited.contains(j))
				dfsNew(j, visited, current);
		}
	}
	
	
	private void dfs(int i, Stack<Integer> visited, Stack<Integer> finished) {
		
		visited.push(i);
		if(!edges.containsKey(i) || edges.get(i).isEmpty()) {
			finished.push(i);
			return;
		}
		
		HashSet<Integer> adj= edges.get(i);
		for(int j: adj) {
			if(!visited.contains(j))
				dfs(j, visited, finished);
		}
		finished.push(i);
	}
	
	
	private CapGraph transpose() {
		
		CapGraph ret= new CapGraph();
		
		for(int i: vertices) {
			ret.vertices.add(i);
			HashSet<Integer> adj=  new HashSet<Integer>();
			ret.edges.put(i, adj);
		}
		
		for(int i: vertices) {
			if(edges.containsKey(i)) {
				for(int j: edges.get(i))
					ret.edges.get(j).add(i);
			}
		}
		
		return ret;
		
	}

	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		
		return edges;
	}

}
