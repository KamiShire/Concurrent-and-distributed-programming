package pcd2018.exe2;

import java.util.*;
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
    HashMap<Integer,Long> valueMapA = new HashMap<>(IntStream.rangeClosed(1,LIMIT)
                                        .parallel()
                                        .boxed()
                                        .collect(
                                                Collectors.toMap(val->val,
                                                        val -> DiffieHellmanUtils.modPow(publicB,val,p))));

    //shallow copy
    //HashMap<Integer,Long> valueMapB = new HashMap<>(valueMapA);

    HashMap<Integer,Long> valueMapB = new HashMap<>(IntStream.rangeClosed(1,LIMIT)
                                        .parallel()
                                        .boxed()
                                        .collect(
                                              Collectors.toMap(val->val,
                                                      val -> DiffieHellmanUtils.modPow(publicA,val,p))));
    /*
    *  A questo punto ho tutti valori possibili di A^b mod p e B^a mod p. Resta solo da confrontarli
    *  Per ogni entry di valueMapA confronto il value con value di tutte le entry di valueMapB se sono uguali
    *  allora inserisco a e b in res
    */
    /*  NOTA (al di fuori dello scopo dell'esercizio):
        Siccome per ogni a è possibile avere uno o più b (in un contesto di intervalli numerici ampi)
        allora è necessario salvare in una struttura di supporto
        tutti i valori b che soddisfano A^b mod p = B^a mod p. Tuttavia visto il test in DiffieHellmanTest si ha che
        la lista viene esaminata a coppie, quindi si presuppone che per ogni a esista uno e uno solo valore b
        tale per cui  A^b mod p = B^a mod p, questo è vero in questi intervalli di numeri. Tuttavia se si usassero intervalli più
        ampi sarebbe possibile ottenere più b1,b2,..,bn per alcuni a tali per cui  B^a mod p = A^b1 mod p = A^b2 mod p = ...
        In tal caso la soluzione migliore per valutare i test consisterebbe nel utilizzare una nuova struttura dati:
        una mappa che per chiavi ha il valore di a e per valori una lista di interi nella quale sono memorizzati i diversi
        b1,b2,...,bn.
     */
    valueMapA.entrySet().parallelStream().forEach(
            val -> {
              Integer[] results = valueMapB.entrySet().parallelStream()
                      .filter(b -> b.getValue().equals(val.getValue()))
                      .map(x->x.getKey()).toArray(Integer[]::new);
              // Ad ogni valore di a viene associato un array che contiene i valori di b tali per cui A^b mod p = B^a mod p


              Arrays.stream(results).sorted().forEach(el -> {
                synchronized (this){
                  res.add(val.getKey());
                  res.add(el);
                }
              });
            });
    return res;
  }
  public List<Integer> crackThreads(long publicA, long publicB) {
    List<Integer> res = new ArrayList<Integer>();
    List<CrackThread> callable = new ArrayList<>();

    for(int i = 0; i < 6; ++i) {
      callable.add(new CrackThread());
    }

    return res;
  }
}
