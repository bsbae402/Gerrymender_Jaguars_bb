package jaguars.data;

import org.springframework.stereotype.Service;

@Service
public class CensusManager {
    public int getCensusYear(int year) {
        return (year/10) * 10;
    }
}
