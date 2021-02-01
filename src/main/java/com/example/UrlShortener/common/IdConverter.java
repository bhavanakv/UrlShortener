package com.example.UrlShortener.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdConverter {

    private static final Logger logger = LoggerFactory.getLogger(IdConverter.class);

    public IdConverter() {

        initializeCharToIndex();
        initializeIndexToChar();
    }

    private static HashMap<Character,Integer> charToIndexTable;
    private static List<Character> indexToCharTable;

    private void initializeIndexToChar() {
        indexToCharTable = new ArrayList<>();
        char c;
        for(int i=0;i<26;i++) {
            c = (char)(97+i);
            indexToCharTable.add(c);
        }
        for(int i=0;i<26;i++) {
            c = (char)(65+i);
            indexToCharTable.add(c);
        }
        for(int i=0;i<10;i++) {
            c = (char)(48+i);
            indexToCharTable.add(c);
        }
        logger.info("IndexToCharTable: " + indexToCharTable.toString());
    }

    private void initializeCharToIndex() {
        charToIndexTable = new HashMap<>();
        char c;
        int k = 0;
        for(int i=0;i<26;i++,k++) {
            c = (char)(97+i);
            charToIndexTable.put(c,k);
        }
        for(int i=0;i<26;i++,k++) {
            c = (char)(65+i);
            charToIndexTable.put(c,k);
        }
        for(int i=0;i<10;i++,k++) {
            c = (char)(48+i);
            charToIndexTable.put(c,k);
        }
        logger.info("charToIndexTable: " + charToIndexTable.toString());
    }
    
    public String createUniqueId(long id) {
        List<Integer> base62ID = convertBase10to62Id(id);
        StringBuilder uniqueIdUrl = new StringBuilder();
        for(int digit: base62ID) {
            uniqueIdUrl.append(indexToCharTable.get(digit));
        }
        return uniqueIdUrl.toString();
    }

    private static List<Integer> convertBase10to62Id(long id) {
        LinkedList<Integer> digits = new LinkedList<>();
        if(id == 0) {
            digits.addFirst(0);
            return digits;
        }
        while(id > 0) {
            int rem = (int)(id % 62);
            digits.addFirst(rem);
            id = id / 62;
        }
        return digits;
    }

    public Long getKeyFromUniqueId(String uniqueId) {
        List<Character> base62Ids = new ArrayList<>();
        for(int i=0;i<uniqueId.length();i++) {
            base62Ids.add(uniqueId.charAt(i));
        }
        Long key = convertBase62to10Id(base62Ids);
        return key;
    }

    private static Long convertBase62to10Id(List<Character> base62Ids) {
        long id = 0L;
        for(int i=0, exp = base62Ids.size() -1 ;i<base62Ids.size();i++) {
            int base10 = charToIndexTable.get(base62Ids.get(i));
            id += base10 * Math.pow(62.0, exp);
        }
        return id;
    }
}
