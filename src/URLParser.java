import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Includes the static method ParseURL(String url) that takes a 
 * URL and uses the JSoup library and StringTokenizer to parse 
 * the text on the specified site into into an array of 
 * Strings. Each String represents a tag on the website. This 
 * information is returned as a TagSet object.
 * 
 * @author Evan W. Drewry (ewd2106)
 *
 */
public class URLParser {

	public static TagSet parseURL(String url) throws IOException{
		Document doc = Jsoup.connect(url).get();
		String t = doc.text();
		t = t.toLowerCase();
		t = t.replaceAll("[^a-z]", " ");
		StringTokenizer st = new StringTokenizer(t, " ");
		
		String[] text = new String[st.countTokens()];
		for(int i = 0; i < text.length; i++){
			text[i] = st.nextToken();
		}

		return new TagSet(url, text);
	}
	
	public static Hashtable<String, Integer> getHashtable(String url) throws IOException{
		TagSet t = parseURL(url);
		String[] s = t.getTags();
		Hashtable<String, Integer> h = new Hashtable<String, Integer>();
		for(String e : s)
			h.put(e, e.hashCode());
		return h;
	}
	
/*	public static void main(String[] args) throws IOException {
		Hashtable<String, Integer> text = getHashtable("http://en.wikipedia.org");
		if(text.containsKey("wikipedia"))
			System.out.println("e");
	} */
}
