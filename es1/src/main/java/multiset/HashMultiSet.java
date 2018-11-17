package multiset;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
//import java.nio.
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.Collectors;

/**
 * <p>A MultiSet models a data structure containing elements along with their frequency count i.e., </p>
 * <p>the number of times an element is present in the set.</p>
 * <p>HashMultiSet is a Map-based concrete implementation of the MultiSet concept.</p>
 * 
 * <p>MultiSet a = <{1:2}, {2:2}, {3:4}, {10:1}></p>
 * */
public final class HashMultiSet<T, V> {

	/**
	 *XXX: data structure backing this MultiSet implementation. 
	 */
	private HashMap<T,V> multimap;
	/**
	 * Sole constructor of the class.
	 **/
	public HashMultiSet() {
		multimap = new HashMap<>();
	}
	
	
	/**
	 * If not present, adds the element to the data structure, otherwise 
	 * simply increments its frequency.
	 * 
	 * @param t T: element to include in the multiset
	 * 
	 * @return V: frequency count of the element in the multiset
	 * */
	@SuppressWarnings("unchecked")
	public V addElement(T t) {
		if(multimap.containsKey(t)){
			Number count = ((Number)multimap.get(t)).intValue() + 1;
			multimap.put(t,(V)count);
			return (V)count;
		}
		Number first = 1;
		multimap.put(t,(V)first);
		return (V)first;

	}

	/**
	 * Check whether the elements is present in the multiset.
	 * 
	 * @param t T: element
	 * 
	 * @return V: true if the element is present, false otherwise.
	 * */	
	public boolean isPresent(T t) {
		return multimap.containsKey(t);
	}
	
	/**
	 * @param t T: element
	 * @return V: frequency count of parameter t ('0' if not present)
	 */
	@SuppressWarnings("unchecked")
	public V getElementFrequency(T t) {
		if(multimap.containsKey(t))
		    return multimap.get(t);

		Number zero = 0;
		return (V)zero;
	}
	
	
	/**
	 * Builds a multiset from a source data file. The source data file contains
	 * a number comma separated elements. 
	 * Example_1: ab,ab,ba,ba,ac,ac -->  <{ab:2},{ba:2},{ac:2}>
	 * Example 2: 1,2,4,3,1,3,4,7 --> <{1:2},{2:1},{3:2},{4:2},{7:1}>
	 * 
	 * @param source Path: source of the multiset
	 * */
	public void buildFromFile(Path source) throws IOException,IllegalArgumentException {
		String exc = "Exception: "+source.toString();

		if(source == null)
			throw new IllegalArgumentException("The given path is null");

        if(Files.notExists(source))
        	throw new IOException(exc + " is an invalid path, file not found");

		if(!(Files.isReadable(source)))
			throw new IOException(exc + " cannot access the file in read mode");
		multimap.clear();
        /**
         * Generate a Stream<String> using Files.lines(), then using map to split elements which returns a Stream<String[]>
         *     flatMap returns a Stream<String> which is then collect into a List<String>
         *
         *  I used lambdas and streams just to practice. Probably this solution is not the best in terms of performance, i believe
         *  (from my understanding) that streams should be used for more complicated tasks.
		 *
		 *  NOTE: the split method leaves spaces after the comma, don't understand why.
         */
         buildFromCollection((List<T>)Files.lines(source).map(x -> x.split(",")).flatMap(Arrays::stream).collect(Collectors.toList()));

	}

	/**
	 * Same as before with the difference being the source type.
	 * @param source List<T>: source of the multiset
	 * */
	public void buildFromCollection(List<? extends T> source) {
	    if(source == null)
	        throw new IllegalArgumentException("Method should be invoked with a non null file path");
	    if(!(source.isEmpty())) {
			multimap.clear();
            for(T element: source) {
                addElement(element);
            }
        }

	}
	
	/**
	 * Produces a linearized, unordered version of the MultiSet data structure.
	 * Example: <{1:2},{2:1}, {3:3}> -> 1 1 2 3 3 3 3
	 * 
	 * @return List<T>: linearized version of the multiset represented by this object.
	 */
	public List<T> linearize() {
		List<T> aux = new ArrayList<>();
        Set<Map.Entry<T,V>> view = multimap.entrySet();

        multimap.forEach((T t, V v) -> {
            Integer i = (Integer) v;
             while(i > 0) {
                 aux.add(t);
                 --i;
             }
        });

		return aux;

	}


}
