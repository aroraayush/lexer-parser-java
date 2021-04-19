package lexer_parser.parser;

import java.util.HashMap;

/**
 * This class tracks the identifiers within a program.
 */
public class IdTable {

    // Stores identifiers and its address.
    private HashMap<String, Integer> identifiersMap = new HashMap<>();
    private int address = 0;

    /**
     * This method adds an entry to the map, you need only send the id.
     *
     * @param str
     */
    public void add(String str) {
        this.identifiersMap.put(str, address);
        this.address++;
    }

    /**
     * This method returns the address associated with an id, or -1 if not found
     *
     * @return address
     */
    public int getAddress(String str) {
        return identifiersMap.getOrDefault(str, -1);
    }

    /**
     * This method iterates through the hashmap and adds the
     * key(ID)-value(address) pair to a string variable.
     *
     * @return id and address value pair as a string
     */
    @Override
    public String toString() {

        return  identifiersMap.toString();
//        String st = "(";
//        // for loop which iterates through the Hashmap
//        for (HashMap.Entry<String, Integer> set : identifiersMap.entrySet()) {
//            st = st + (set.getKey() + "=" + set.getValue()) + ",";
//        }
//        return st.substring(0, st.length() - 1) + ")";
    }
}

