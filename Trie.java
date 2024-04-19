import java.util.ArrayList;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';

    private class TrieNode {
        boolean terminFlag = false;
        TrieNode[] children = new TrieNode[62];
        public int charToIndex(char c) {
            if (c >= 'a' && c <= 'z') {
                return c - 'a';
            } else if (c >= 'A' && c <= 'Z') {
                return 26 + c - 'A';
            } else if (c >= '0' && c <= '9' ) {
                return 52 + c - '0';
            }
            return 0;
        }
    }

    TrieNode root;
    public Trie() {
        root = new TrieNode();
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        TrieNode node;
        node = root;
        for (int i = 0; i < s.length(); i++){
            int index = node.charToIndex(s.charAt(i));
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.terminFlag = true;
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        TrieNode node = root;
        for (int i = 0; i<s.length(); i++) {
            int index = node.charToIndex(s.charAt(i));
            if (node.children[index] == null) {
                return false;
            }
            node = node.children[index];
        }
        return node.terminFlag;
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        prefixhelper(root, s, "", results, 0, limit);
    }

    void prefixhelper(TrieNode node, String s, String current, ArrayList<String> rn, int index, int limit) {
        if (rn.size() >= limit) {
            return;
        }
        if (index == s.length()) {
            collectAll(node, current, rn, limit);
        } else {
            if (s.charAt(index) == WILDCARD) {
                for (int i = 0; i < node.children.length; i++) {
                    if (node.children[i] != null) {
                        prefixhelper(node.children[i], s, current + indexToChar(i), rn, index + 1, limit);
                    }
                }
            } else {
                int ch = node.charToIndex(s.charAt(index));
                if (node.children[ch] != null) {
                    prefixhelper(node.children[ch], s, current + s.charAt(index), rn, index + 1, limit);
                }
            }
        }
    }

    void collectAll(TrieNode node, String prefix, ArrayList<String> results, int limit) {
        if (results.size() >= limit) {
            return;
        }
        if (node.terminFlag) {
            results.add(prefix);
        }
        for (int i = 0; i<node.children.length; i++) {
            if (node.children[i] != null) {
                collectAll(node.children[i], prefix + indexToChar(i), results, limit);
            }
        }
    }

    private char indexToChar(int index) {
        if (index < 26) {
            return (char) ('a' + index);
        } else if (index < 52) {
            return (char) ('A' + index - 26);
        } else {
            return (char) ('0' + index - 52);
        }
    }


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        String[] result1 = t.prefixSearch("pe", 10);
        String[] result2 = t.prefixSearch("pe.", 10);
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
