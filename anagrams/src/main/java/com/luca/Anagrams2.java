package com.luca;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * https://www.hackerrank.com/challenges/sherlock-and-anagrams
 * <p>
 * Using character maps and accumulating first there's no need for substring
 * and the order for obtaining the substrings is N*N*M with N being length of the string
 * and M being the amount of distinct characters in the string.
 */
public class Anagrams2 {

    static class CharacterList {
        private Map<Character, Integer> map = new HashMap<>();

        public CharacterList(List<Character> list) {
            list.forEach(c -> map.merge(c, 1, Integer::sum));
        }

        private CharacterList(Map<Character, Integer> map) {
            this.map = map;
        }

        public boolean isAnagram(CharacterList characterList) {
            if (map.keySet().size() != characterList.map.keySet().size()) return false;
            if (!map.keySet().containsAll(characterList.map.keySet())) return false;
            return 0 == map.entrySet().stream()
                    .filter(entry -> characterList.map.get(entry.getKey()) != entry.getValue())
                    .count();
        }

        public CharacterList difference(CharacterList characterList) {
            HashMap<Character, Integer> differenceMap = new HashMap<>(map);
            characterList.map.entrySet().stream().forEach(
                    entry -> {
                        if (differenceMap.get(entry.getKey()) == entry.getValue()) differenceMap.remove(entry.getKey());
                        else differenceMap.merge(entry.getKey(), entry.getValue() * -1, Integer::sum);
                    }
            );
            return new CharacterList(differenceMap);
        }
    }

    static class SubstringManager {

        private final CharacterList[] charListArray;

        private Map<Integer, List<CharacterList>> substringMap = new HashMap<>();

        public SubstringManager(String string) {
            charListArray = new CharacterList[string.length() + 1];
            ArrayList<Character> list = new ArrayList<>();
            charListArray[0] = new CharacterList(Collections.emptyList());
            for (int i = 0; i < string.length(); i++) {
                list.add(string.charAt(i));
                charListArray[i + 1] = new CharacterList(list);
            }
        }

        public void registerSubstring(int i, int j) {
            ArrayList<CharacterList> list = new ArrayList<>();
            list.add(charListArray[j].difference(charListArray[i]));
            substringMap.merge(j - i, list, (a, b) -> {
                a.addAll(b);
                return a;
            });
        }

        public Map<Integer, List<CharacterList>> getSubstringMap() {
            return substringMap;
        }
    }

    static int sherlockAndAnagrams(String s) {
        SubstringManager substringManager = new SubstringManager(s);
        for (int i = 0; i < s.length(); i++) {
            for (int j = i + 1; j < s.length() + 1; j++) {
                substringManager.registerSubstring(i, j);
            }
        }

        AtomicInteger anagrams = new AtomicInteger();
        Map<Integer, List<CharacterList>> substringsByLength = substringManager.getSubstringMap();
        substringsByLength.keySet().stream().forEach(
                length -> {
                    List<CharacterList> substrings = substringsByLength.get(length);
                    for (int i = 0; i < substrings.size(); i++) {
                        for (int j = i + 1; j < substrings.size(); j++) {
                            if (substrings.get(i).isAnagram(substrings.get(j))) anagrams.incrementAndGet();
                        }
                    }
                }
        );
        return anagrams.get();
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
