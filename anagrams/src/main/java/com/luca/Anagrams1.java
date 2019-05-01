package com.luca;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://www.hackerrank.com/challenges/sherlock-and-anagrams
 * <p>
 * Using substring to obtaining the substrings is N^3 with N being length of the string.
 */
public class Anagrams1 {

    static Map<Integer, List<Map<Character, Integer>>> subStrings;

    static int sherlockAndAnagrams(String s) {
        subStrings = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            for (int j = i + 1; j < s.length() + 1; j++) {
                String substring = s.substring(i, j);
                loadSubstring(substring);
            }
        }
        AtomicInteger anagrams = new AtomicInteger();
        subStrings.values().parallelStream().forEach(substrings -> {
            for (int i = 0; i < substrings.size(); i++) {
                for (int j = i + 1; j < substrings.size(); j++) {
                    if (areAnagrams(substrings.get(i), substrings.get(j))) anagrams.incrementAndGet();
                }
            }
        });
        return anagrams.get();
    }

    static boolean areAnagrams(Map<Character, Integer> mapA, Map<Character, Integer> mapB) {
        if (mapA.keySet().size() != mapB.keySet().size()) return false;
        if (!mapA.keySet().containsAll(mapB.keySet())) return false;
        return 0 == mapA.entrySet().stream().filter(entry -> mapB.get(entry.getKey()) != entry.getValue()).count();
    }

    static void loadSubstring(String s) {
        Map<Character, Integer> characters = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            characters.merge(s.charAt(i), 1, Integer::sum);
        }
        List<Map<Character, Integer>> charactersList = new ArrayList<>();
        charactersList.add(characters);
        subStrings.merge(s.length(), charactersList, (a, b) -> {
            a.addAll(b);
            return a;
        });
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int q = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int qItr = 0; qItr < q; qItr++) {
            String s = scanner.nextLine();

            int result = sherlockAndAnagrams(s);
            System.out.println(result);
        }


        scanner.close();
    }
}
