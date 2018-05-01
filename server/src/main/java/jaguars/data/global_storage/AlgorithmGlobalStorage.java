package jaguars.data.global_storage;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class AlgorithmGlobalStorage {
    private HashMap<Integer, AlgorithmInstance> algorithmInstanceTable;

    public AlgorithmGlobalStorage() {
        algorithmInstanceTable = new HashMap<>();
    }

    public Integer registerAlgorithmInstance(AlgorithmInstance ai) {
        Integer hashint = Objects.hash(ai);
        algorithmInstanceTable.put(hashint, ai);
        return hashint;
    }

    public AlgorithmInstance getAlgorithmInstance(Integer hashint) {
        return algorithmInstanceTable.get(hashint);
    }

    public boolean removeAlgorithmInstance(Integer hashint) {
        AlgorithmInstance ai = algorithmInstanceTable.remove(hashint);
        if(ai == null)
            return false;
        return true;
    }
}
