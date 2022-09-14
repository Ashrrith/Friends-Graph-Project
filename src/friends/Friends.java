package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		p1 = p1.toLowerCase();
		p2 = p2.toLowerCase();

		if(g==null || p1==null || p2==null)
		{
			return null;
		}

		ArrayList<String> shortestChainArr = new ArrayList<String>();
		int[] edgeTo = new int[g.members.length];
		boolean[] visited = new boolean[g.members.length];
		Queue<Person> BFSqueue = new Queue<Person>();

		int indexOfFirstP1 = g.map.get(p1);
		BFSqueue.enqueue(g.members[indexOfFirstP1]);
		visited[indexOfFirstP1] = true;

		if (g.members[indexOfFirstP1].first == null) 
		{
			return null;
		}

		while(!BFSqueue.isEmpty())
		{
			Person v = BFSqueue.dequeue();
			int indexOfV = g.map.get(v.name);
			visited[indexOfV] = true;

			for(Friend friend = v.first; friend!=null; friend = friend.next)
			{
				if(!visited[friend.fnum])
				{
					BFSqueue.enqueue(g.members[friend.fnum]);
					edgeTo[friend.fnum] = indexOfV;
					visited[friend.fnum] = true;
				}

				if (g.members[friend.fnum].name.equals(p2)) 
				{
					Person parent = g.members[friend.fnum];
					while (!parent.name.equals(p1)) 
					{
						shortestChainArr.add(0, parent.name);
						parent = g.members[edgeTo[g.map.get(parent.name)]];
					}
					shortestChainArr.add(0, p1);

					return shortestChainArr;
				}
			}
		}

		return null;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) 
	{
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		for(int i = 0; i < g.members.length; i++)
		{
			if(g.members[i].school != null && g.members[i].school.equalsIgnoreCase(school) && !visited[i])
			{
				ArrayList<String> temp = new ArrayList<String>();
				floodFill(visited,temp,i,g,school);
				cliques.add(temp);
			}
		}
		return cliques;
	}

	private static void floodFill(boolean[] visited, ArrayList<String>temp,int i,Graph g,String school)
	{
		visited[i] = true;
		temp.add(g.members[i].name);
		for(Friend k = g.members[i].first; k != null; k = k.next)
		{
			if(g.members[k.fnum].school != null && g.members[k.fnum].school.equalsIgnoreCase(school) && !visited[k.fnum])
			{
				floodFill(visited,temp,k.fnum,g,school);
			}
		}
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		/** COMPLETE THIS METHOD **/
		
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		ArrayList<String> connectors = new ArrayList<String>();
		boolean[] visited = new boolean[g.members.length];
		int[] dfsnum = new int[g.members.length];
		int[] back = new int[g.members.length];
		int[] parent = new int[g.members.length];
        boolean[] x = new boolean[g.members.length];
        int dfsCounter = 0;
        
        
        for(int i=0; i<g.members.length; i++) 
        	parent[i] = -1;

		for(int i = 0; i < g.members.length; i++)
		{
			if(!visited[i])
			{
      			dfsCounter = dfs(g, visited, g.members[i], i, g.members[i], dfsnum, dfsCounter, back, parent, x);
			}
		}
		for(int i = 0; i<x.length; i++)
		{
			if(x[i])
				connectors.add(g.members[i].name);
		}
		return connectors;
		
	}

	private static int dfs(Graph g, boolean[] visited, Person root, int i, Person u, int[] dfsnum, int dfsCounter, int[] back, int[] parent, boolean[] x)
	{
		int rootChildren = 0;
		visited[i] = true;
		dfsnum[i] = dfsCounter++;
		back[i] = dfsnum[i];

		for(Friend v = u.first; v != null; v = v.next)
		{
			if(!visited[v.fnum])
			{
            	parent[v.fnum] = i;
                
				if(u.name.equals(root.name))
				{
					rootChildren++;
					x[i] = rootChildren > 1;
				}

				dfsCounter = dfs(g,visited,root,v.fnum,g.members[v.fnum],dfsnum,dfsCounter,back,parent,x);

				if(!u.name.equals(root.name) && back[v.fnum] >= dfsnum[i])
        		{
					x[i] = true;
				}
				
				back[i] = Math.min(back[i], back[v.fnum]);
			}
			else if(v.fnum != parent[i])
			{
				back[i] = Math.min(back[i], dfsnum[v.fnum]);
			}
		}
        
        return dfsCounter;
 	}
}

