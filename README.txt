Note: Main method is in TagCloudGenerator.java

TagCloudGenerator:

This class creates a new TagCloud object upon instantiation, 
and when the start() method is called, the program prompts 
for user input via the console and gives options to find, 
save, load, print, print the top tags, input a new url, or 
quit.


TagCloud:

This class uses the JUNG DirectedSparseMultigraph<String, 
Edge> to implement a tag cloud system that can be saved, 
loaded, searched, added to, filtered for the most popular 
tags, and visualized.


Edge:

This class represents an edge on the 
DirectedSparseMultiGraph. The only property of an Edge is 
type, which can be either Tag->Tag or URL->Tag. This is 
specified by the enum EdgeType.


EdgeType:

This enum includes two types of edges, URL and TAG, which 
specify whether the edge points from URL to Tag or from Tag 
to Tag.


URLParser:

Includes the static method ParseURL(String url) that takes a 
URL and uses the JSoup library and StringTokenizer to parse 
the text on the specified site into into an array of 
Strings. Each String represents a tag on the website. This 
information is returned as a TagSet object.


TagSet:

This class functions as a container for a set of tags from a 
URL. All it contains is an array of all the tags on a 
website, in order, and a field containing the URL.



Method Runtime Analysis:

Add URL: O(n) It takes O(n) time to parse the URL, where n 
is the number of words on the website. To add the parsed 
data to the graph, we must iterate through each tag and add 
a vertex (O(1)) with in-edges from the url (O(1)) and the 
previous tag (O(1)), so this is also O(n).

Visualize: O(V + E) because we have to iterate through every 
vertex, and each vertex has an adjacency list that we must 
also iterate through to see what other vertices are adjacent 
to it.

Filter top tags: O((V+E)VlogV) The cost to filter out the 
top tags is the cost to sort the tags in order of frequency. 
the TagFrequencyComparator compares vertices by their in-
degrees, which costs O(V + E) to calculate for each vertex.

Find: O(V+E) because it takes O(V+E) time to find all the 
predecessors of the vertex once we have found it.

Save/Load: O(V+E)  because we have to iterate through every 
vertex, and each vertex has an adjacency list that we must 
also iterate through