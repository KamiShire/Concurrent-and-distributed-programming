package multiset;

import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HashMultiSetTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testbuildFromCollection() {
	    exception.expect(IllegalArgumentException.class);
	    exception.expectMessage("Method should be invoked with a non null file path");
	    HashMultiSet<String, Integer> hmSet = new HashMultiSet<>();
	    hmSet.buildFromCollection(null);
	}
	
	@Test
	public void testElementFrequency() {
	    HashMultiSet<Integer, Integer> hmSet = new HashMultiSet<>();
	    hmSet.addElement(1);
	    hmSet.addElement(1);	    
	    assertEquals("Equal", true, hmSet.getElementFrequency(1) == 2);
	}

	@Test
	public void testBuildFromFile() throws Exception {
		List<String> excepted = Arrays.asList("ab", "cdc","ab","cd","cd","fg","gf","fg");

		HashMultiSet<String,Integer> hmSet = new HashMultiSet<>();

		hmSet.buildFromCollection(excepted);

		List<String> result = hmSet.linearize();
		Collections.sort(excepted);
		Collections.sort(result);

		Path path = Paths.get("test.txt");
		if (Files.notExists(path)) {
			path = Files.createFile(Paths.get("test.txt")).toAbsolutePath();

		}
		else {
			path = path.toAbsolutePath();
		}

		hmSet.buildFromFile(path);

		//System.out.println(hmSet.linearize());
		List<String> actual = hmSet.linearize();
		Collections.sort(actual);
		Collections.sort(excepted);
		assertEquals(excepted, actual);
	}

}
