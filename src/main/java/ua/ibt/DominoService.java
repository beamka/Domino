package ua.ibt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * Created by IRINA on 23.04.2017.
 */
public class DominoService {
    private Connection conn;

    public DominoService() {
        this.conn = new ConnectJDBC().getConnect();
    }

    /**
     * Create a set of bones
     *
     * @param size - the maximum number on the bone
     * @return full set of bones
     */
    public List<Bone> createAllBones(int size) {
        List<Bone> bones = new ArrayList<>();
        int n1 = 0;
        int n2 = 0;
        int id = 0;
        while (n1 <= size) {
            while (n2 <= size) {
                bones.add(new Bone(id, n1, n2));
                id++;
                n2++;
            }
            n1++;
            n2 = n1;
        }
        return bones;
    }

    /**
     * Get a random amount of bones from the allowed
     *
     * @param size - the maximum number on the bone
     * @return random number
     */
    public int getRandomCount(int size) {
        return (int) (Math.random() * (size + 1) * (size + 2) / 2);
    }

    /**
     * Get a list bones from the set of bones
     *
     * @param count - amount of bones
     * @return list of bones
     */
    public List<Bone> getListBones(int count, List<Bone> allBones) {
        List<Bone> result = new ArrayList<>();
        List<Integer> listId = new ArrayList<>();
        int array_arr_size;

        // Prepare list id a bones of random
        // int max_count = (size + 1) * (size + 2) / 2;
        int max_count = allBones.size();
        array_arr_size = count < max_count ? count : max_count;
        while (array_arr_size > 0) {
            Integer num = 0;
            boolean exist = true;
            while (exist) {
                num = (int) (Math.random() * max_count);
                exist = listId.stream().anyMatch(num::equals);
            }
            listId.add(num);
            array_arr_size--;
        }
        // Geting a list bones from allBones by listId
        listId.forEach(id -> result.add(allBones.stream().filter(b -> b.getId() == id).findFirst().orElse(null)));
        return result;
    }

    /**
     * Get all combinations of bones
     *
     * @param bones - list of bones
     * @return list of combinations
     */
    private ArrayList<ArrayList<Bone>> getAllCombinationsOfBones(List<Bone> bones) {
        ArrayList<ArrayList<Bone>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        for (int i = 0; i < bones.size(); i++) {
            ArrayList<ArrayList<Bone>> current = new ArrayList<>();
            Iterator<ArrayList<Bone>> resultIter = result.iterator();
            while (resultIter.hasNext()) {
                ArrayList<Bone> l = resultIter.next();
                for (int j = 0; j < l.size() + 1; j++) {
                    l.add(j, bones.get(i));
                    ArrayList<Bone> temp = new ArrayList<>(l);
                    current.add(temp);
                    l.remove(j);
                }
            }
            result = new ArrayList<>(current);
        }
        return result;
    }

    /**
     * Get all sequence of combinations
     *
     * @param bones - list of bones
     * @return list of sequences
     */
    public ArrayList<ArrayList<Bone>> getAllSequences(List<Bone> bones) {
        ArrayList<ArrayList<Bone>> result = new ArrayList<>();
        ArrayList<ArrayList<Bone>> allComb = getAllCombinationsOfBones(bones);

        Iterator<ArrayList<Bone>> combinations = allComb.iterator();
        while (combinations.hasNext()) {
            List<Bone> combination = combinations.next();
            ArrayList<Bone> tempComb = new ArrayList<>();
            int step = 0; // 2 step
            while (step < 2) {
                tempComb.add(combination.get(0).clone());
                for (int j = 0; j < combination.size(); j++) {
                    if (j + 1 < combination.size()) { // is next bone
                        if (combination.get(j).getNum2() == combination.get(j + 1).getNum1()) {
                            tempComb.add(combination.get(j + 1).clone());
                        } else if (combination.get(j).getNum2() == combination.get(j + 1).getNum2()) {
                            combination.set(j + 1, combination.get(j + 1).twistBone());
                            tempComb.add(combination.get(j + 1).clone());
                        } else {
                            break;
                        }
                    }
                }
                if (!result.isEmpty()) {
                    boolean isExist = false;
                    for (int i = 0; i < result.size(); i++)
                        if (isCompare(result.get(i), tempComb)) {
                            if (tempComb.size() > result.get(i).size())
                                result.set(i, tempComb);
                            isExist = true;
                        }
                    if (!isExist)
                        result.add(tempComb);

                } else { // There is a duplicate in the collection, check the length and write a longer
                    result.add(tempComb);
                }
                step++;
                tempComb = new ArrayList<>();
                combination.set(0, combination.get(0).twistBone()); // twist the first bone
            }
        }
        return result;
    }

    /**
     * Compare two sequences
     *
     * @param sequences1 - first sequences
     * @param sequences2 - second sequence
     * @return true - if the sequence compare is true
     */
    private boolean isCompare(ArrayList<Bone> sequences1, ArrayList<Bone> sequences2) {
        boolean exist = true;
        for (int i = 0; i < (sequences2.size() < sequences1.size() ? sequences2.size() : sequences1.size()); i++) {
            if (!sequences1.get(i).equals(sequences2.get(i))) {
                exist = false;
                break;
            }
        }
        return exist;
    }

    /**
     * Get max length sequence
     *
     * @param allSequences - list of all sequences
     * @return list of bones - max length sequence
     */
    public ArrayList<Bone> getMaxLengthComb(ArrayList<ArrayList<Bone>> allSequences) {
        return allSequences.stream().max((s1, s2) -> ((Integer) s1.size()).compareTo(s2.size())).get();
    }

    /**
     * Save set of bones to the data base
     *
     * @param set - list of set of bones
     * @return ID of set of bones to the data base
     */
    public Long insertSet(List<Bone> set) {
        Long id = randomiD();
        try {
            conn.createStatement().executeUpdate("INSERT INTO sets (id_set,set_bon) VALUES (" + id + ",'" + set.toString() + "')");
            return id;
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("Access error:" + e.getMessage());
            return null;
        }
    }

    /**
     * Save all sequences to the data base
     *
     * @param comb   - list of sequences
     * @param id_set - id of the set of bones to the data base
     * @return list of sequences
     */
    public boolean insertComb(List<Bone> comb, Long id_set) {
        Long id = randomiD();
        try {
            conn.createStatement().executeUpdate("INSERT INTO combs (id_comb,comb,id_set) VALUES (" + id + ",'" + comb.toString() + "'," + id_set + ")");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Access error:" + e.getMessage());
            return false;
        }
    }

    /**
     * Get all sets and sequences from the data base
     *
     * @return list of data
     */
    public List<Map<String, Object>> showHistory() {
        Statement stmt, stmt2;
        ResultSet rs1, rs2;
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
            rs1 = stmt.executeQuery("SELECT id_set, set_bon FROM sets");
            while (rs1.next()) {
                Map<String, Object> row = new HashMap<String, Object>();
                row.put("set", rs1.getString("set_bon"));
                Long id_set = rs1.getLong("id_set");
                rs2 = stmt2.executeQuery("SELECT comb FROM combs WHERE id_set = " + id_set);
                List<String> comb = new ArrayList<>();
                while (rs2.next()) {
                    comb.add(rs2.getString("comb"));
                }
                row.put("comb", comb);
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Access error:" + e.getMessage());
        }
        return result;
    }

    /**
     * Generat id for data base
     *
     * @return ID
     */
    private Long randomiD() {
        return Math.abs(UUID.randomUUID().getLeastSignificantBits());
    }
}
