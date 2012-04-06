import java.awt.Component;
import java.awt.Dimension;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.ClassicPickSupport;

/**
 * 
 * This class uses the JUNG DirectedSparseMultigraph<String, 
 * Edge> to implement a tag cloud system that can be saved, 
 * loaded, searched, added to, filtered for the most popular 
 * tags, and visualized.
 * 
 * Method Runtime Analysis:
 * 
 * Add URL: O(n) It takes O(n) time to parse the URL, where n 
 * is the number of words on the website. To add the parsed 
 * data to the graph, we must iterate through each tag and add 
 * a vertex (O(1)) with in-edges from the url (O(1)) and the 
 * previous tag (O(1)), so this is also O(n).
 * 
 * Visualize: O(V + E) because we have to iterate through every
 * vertex, and each vertex has an adjacency list that we must 
 * also iterate through to see what other vertices are adjacent 
 * to it.
 * 
 * Filter top tags: O((V+E)VlogV) The cost to filter out the 
 * top tags is the cost to sort the tags in order of frequency. 
 * the TagFrequencyComparator compares vertices by their in-
 * degrees, which costs O(V + E) to calculate for each vertex.
 * 
 * Find: O(V+E) because it takes O(V+E) time to find all the 
 * predecessors of the vertex once we have found it.
 * Save/Load: O(V+E)  because we have to iterate through every 
 * vertex, and each vertex has an adjacency list that we must 
 * also iterate through.
 * 
 * 
 * @author Evan W. Drewry (ewd2106)
 * 
 *
 */
public class TagCloud implements Serializable{

	private static final long serialVersionUID = 1L;
	private DirectedSparseMultigraph<String, Edge> cloud;
	private ArrayList<String> urls;
	private ArrayList<String> tags;
	private Hashtable<String, Integer> tagsHash;
	
	public TagCloud(){
		cloud = new DirectedSparseMultigraph<String, Edge>();
		urls = new ArrayList<String>();
		tags = new ArrayList<String>();
		tagsHash = new Hashtable<String, Integer>();
	}
	
	/**
	 * Creates a deep clone of the TagCloud
	 * @param tagCloud The TagCloud to be cloned.
	 */
	public TagCloud(TagCloud tagCloud) {
		cloud = new DirectedSparseMultigraph<String, Edge>();
		urls = new ArrayList<String>();
		tags = new ArrayList<String>();
		
		for(String s : tagCloud.urls){
			try {
				addURL(s);
			} catch (IOException e) {
				System.out.println("Failed to add one of the requested URLs.");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Shows the cloud in an interactive view 
	 * using the JUNG libraries.
	 */
	public void show(){
        // Layout<V, E>, VisualizationComponent<V,E>
        Layout<String, EdgeType> layout = new CircleLayout(cloud);
        layout.setSize(new Dimension(800,800));
        VisualizationViewer<String, EdgeType> vv = new VisualizationViewer<String, EdgeType>(layout);
        vv.setPreferredSize(new Dimension(800,800));
        // Show vertex and edge labels
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.setPickSupport(new ClassicPickSupport());
        // Create a graph mouse and add it to the visualization component
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        // For transforming:   gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        gm.setMode(ModalGraphMouse.Mode.PICKING);
        vv.setGraphMouse(gm); 
        JFrame frame = new JFrame("Interactive Graph View 1");
    //   To Quit on exit: frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);  
	}

	/**
	 * Adds a URL to the cloud.
	 * 
	 * @param url The URL to be added.
	 * @throws IOException
	 */
	public void addURL(String url) throws IOException{
		TagSet tagSet = URLParser.parseURL(url);

		//if url isnt already in cloud, add url to cloud
		if(!cloud.containsVertex(url)){
			urls.add(url);
			cloud.addVertex(url);
			//add vertices for each word 
			for(int i = 0; i < tagSet.getTags().length; i++){
				String e = tagSet.getTags()[i];
				if(!tagsHash.contains(e)){
					cloud.addVertex(e);
					tags.add(e);
					tagsHash.put(e, e.hashCode());
				}
				
				//add edge to url
				if(!cloud.isNeighbor(url, e))
					cloud.addEdge(new Edge(EdgeType.URL), url, e);

				//add edge to next tag
				if(i != tagSet.getTags().length - 1) 
					cloud.addEdge(new Edge(EdgeType.TAG), e, tagSet.getTags()[i+1]);
			}
		}
	
		else System.err.println("This url is already in the cloud.");
	
	}
	


	/**
	 * Filters out the less frequent tags from the cloud.
	 * 
	 * @param x The number of tags to include in the result
	 * @return A new TagCloud with only the most frequent 
	 * tags
	 * 
	 */
	public TagCloud getMostFrequentTags(int x){
		TagCloud out = new TagCloud(this);
				
		//sort tags by frequency
		Collections.sort(out.tags, getTagFrequencyComparator());
		
		while(x < out.cloud.getVertexCount())
			out.remove(out.tags.get(0));
		
		return out;
		
	}
	
	/**
	 * Finds a specified tag and prints out its URL, 
	 * predecessors, and successors.
	 * 
	 * @param s The search query
	 */
	public void find(String s){
		if(cloud.containsVertex(s)){
			
			Collection<String> pred = cloud.getPredecessors(s);
			
			ArrayList<String> urls = new ArrayList<String>();
			for(String e : pred){
				if(cloud.findEdge(e, s).getType().equals(EdgeType.URL))
					urls.add(e);
			}
			
			ArrayList<String> predecessors = new ArrayList<String>();
			for(String e : pred){
				if(cloud.findEdge(e, s).getType().equals(EdgeType.TAG))
					predecessors.add(e);
			}
			
			Collection<String> successors = cloud.getSuccessors(s);

			System.out.println("\n URLS:");
			for(String e : urls)
				System.out.println(e);
			System.out.println("\n PREDECESSORS:");
			for(String e : predecessors)
				System.out.println(e);
			System.out.println("\n SUCCESSORS");
			for(String e : successors)
				System.out.println(e);
			
			
		}else
			System.out.println("There are no tags that match your query.");
	}
	
	/**
	 * Removes a tag from the cloud.
	 * 
	 * @param tag The tag to be removed from the graph
	 */
	private void remove(String tag){
		tags.remove(tag);
		cloud.removeVertex(tag);
		for(String s : urls)
			if(cloud.getOutEdges(s).isEmpty()) 
				removeURL(s);
	}


	/**
	 * Removes a URL vertex from the cloud.
	 * 
	 * @param s The url to be removed
	 */
	private void removeURL(String s) {
		cloud.removeVertex(s);		
	}

	/**
	 * @return A Comparator object that compares tags by their
	 * frequency (by counting the number of in-edges).
	 */
	private Comparator<String> getTagFrequencyComparator() {
		return new Comparator<String>(){
			public int compare(String s1, String s2){
				Collection<Edge> s1edges = cloud.getInEdges(s1);
				Collection<Edge> s2edges = cloud.getInEdges(s2);
				
				//sorts in ascending order by frequency
				return(((Integer)s1edges.size()).compareTo((Integer) s2edges.size()));
			}

		};
	}
	
	/**
	 * Serializes the TagCloud object to a specified file.
	 * 
	 * @param fileName The file (or path) we want to save
	 * the Cloud to.
	 * 
	 * @throws IOException
	 */
	public void save(String fileName) throws IOException{

	    ObjectOutput out = new ObjectOutputStream(new FileOutputStream(fileName));
	    out.writeObject(this);
	    out.close();
	}
	
	/**
	 * Deserializes and returns a save()-ed instance of TagCloud.
	 * 
	 * @param path The location of the serialized TagCloud.
	 * 
	 * @return The deserialized instance of TagCloud.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static TagCloud load(String path) throws IOException, ClassNotFoundException{
		FileInputStream f_in = new FileInputStream(path);
		ObjectInputStream obj_in = new ObjectInputStream (f_in);
		Object obj = obj_in.readObject();
		if (obj instanceof TagCloud)
			return (TagCloud) obj;
		else
			throw new RuntimeException("The file is not a TagCloud.");
		
	}

}

