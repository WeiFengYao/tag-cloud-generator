import java.io.Serializable;


/**
 * 
 * This class represents an edge on the 
 * DirectedSparseMultiGraph. The only property of an Edge is 
 * type, which can be either Tag->Tag or URL->Tag. This is 
 * specified by the enum EdgeType.
 * 
 * @author Evan W. Drewry (ewd2106)
 *
 */
public class Edge implements Serializable {

	private final EdgeType type;
	
	public Edge(EdgeType type){
		this.type = type;
	}

	public EdgeType getType() {
		return type;
	}
	
	public String toString(){
		return "";
	}
}
