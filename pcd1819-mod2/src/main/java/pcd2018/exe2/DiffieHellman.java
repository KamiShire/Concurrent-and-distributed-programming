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
    List<Integer> res = new ArrayList<Integer>();
    Map<Integer,Integer> map = IntStream.rangeClosed(1,65536).parallel().collect(
            Collectors.toMap(
            val -> val ,
                    v -> {
              for(int i = 1; i <= 65536; ++i)
                if(DiffieHellmanUtils.modPow(publicB,((Number)val).longValue(),p) == DiffieHellmanUtils.modPow(publicA,i,p))
                  return i;

              return 0;
            }
      )
    );

    return res;
  }
}
