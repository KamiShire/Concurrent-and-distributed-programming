package pcd2018.exe2;

import java.util.HashMap;
import java.util.concurrent.Callable;

public class CrackThread implements Callable<HashMap<Integer,Long>> {
    private long publicK;
    private long p;
    private int limit;
    public CrackThread(long pub, long p, int l) {
        publicK = pub;
        this.p = p;
        limit = l;

    @Override
    public HashMap<Integer, Long> call() {
        HashMap<Integer, Long> res = new HashMap<>();
        for(int i = 1; i <= l; ++i) {
            res.put(i,DiffieHellmanUtils.modPow(publicK,i,p));
        }
        return res;
    }
}
