
/**
 * 
 * This class functions as a container for a set of tags from a 
 * URL. All it contains is an array of all the tags on a 
 * website, in order, and a field containing the URL.
 * 
 * @author Evan W. Drewry (ewd2106)
 *
 */
public class TagSet {

	private final String[] tags;
	private final String url;
	
	
	public TagSet(String url, String[] tags){
		this.url = url;
		this.tags = tags;
	}
	
	public String[] getTags(){
		return tags;
	}
	
	public String getURL(){
		return url;
	}
}