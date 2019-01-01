package pcd2018.exe2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Classe da completare per l'esercizio 2.
 */
public class DiffieHellman {

  /**
   * Limite massimo dei valori segreti da cercare
   */
  private static final int LIMIT = 65536;

  private final long p;
  private final long g;

  public DiffieHellman(long p, long g) {
    this.p = p;
    this.g = g;
  }

  /**
   * Metodo da completare
   * 
   * @param publicA valore di A
   * @param publicB valore di B
   * @return tutte le coppie di possibili segreti a,b
   */
  public List<Integer> crack(long publicA, long publicB) {
      System.out.println("In function.");
    List<Integer> res = new ArrayList<Integer>();
    Map<Integer,Integer> map = IntStream.rangeClosed(1,65536).boxed().parallel().collect(
            Collectors.toMap(
            val -> val , val -> {
              System.out.println("In map.");
              for(int i = 1; i <= 65536; ++i) {
                if((DiffieHellmanUtils.modPow(g,((Number)val).longValue(),p) == publicA) &&
                        (DiffieHellmanUtils.modPow(g,i,p) == publicB)) {
                    System.out.println("a: "+val+" b: "+i);
                    return i;
                }
              }
              return 0;
            }
      )
    );
    map.entrySet().stream().forEach(entry -> {
        if(entry.getValue() != 0) {
            res.add(entry.getKey());
            res.add(entry.getValue());
        }
    });

    return res;
  }
}
