package ua.ibt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

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
        //int max_count = (size + 1) * (size + 2) / 2;
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
        result.add(new ArrayList<>());

        ArrayList<ArrayList<Bone>> allComb = getAllCombinationsOfBones(bones);
        System.out.println("getAllCombinationsOfBones : ");
        allComb.forEach(c -> System.out.println(" + " + c));
        System.out.println("===========================");

        Iterator<ArrayList<Bone>> combinations = allComb.iterator();
        while (combinations.hasNext()) {
            List<Bone> combination = combinations.next();
            ArrayList<Bone> tempComb = new ArrayList<>();
            int step = 0; // 2 step
            int pos_rev = 0;// 0:1 == num1:num2
            while (step < 2) {
                tempComb.add(combination.get(0));
                for (int j = 0; j < combination.size(); j++) {
                    if (j + 1 < combination.size()) { // is next
                        if (pos_rev == 0) {
                            if (combination.get(j).getNum1() == combination.get(j + 1).getNum1()) {
                                pos_rev = 1;
                                tempComb.add(combination.get(j + 1));
                            } else if (combination.get(j).getNum1() == combination.get(j + 1).getNum2()) {
                                pos_rev = 0;
                                tempComb.add(combination.get(j + 1));
                            } else {
                                break;
                            }
                        } else if (pos_rev == 1) {
                            if (combination.get(j).getNum2() == combination.get(j + 1).getNum1()) {
                                pos_rev = 1;
                                tempComb.add(combination.get(j + 1));
                            } else if (combination.get(j).getNum2() == combination.get(j + 1).getNum2()) {
                                pos_rev = 0;
                                tempComb.add(combination.get(j + 1));
                            } else {
                                break;
                            }
                        }
                    }
                }
                pos_rev = 1;
                step++;
                if (tempComb.size() > 1) {
                    boolean exist = isExist(result, tempComb);
                    if (!exist) {
                        // allComb.add(new ArrayList<>(tempComb));
                        result.add(tempComb);
                    }
                }
                tempComb = new ArrayList<>();
            }
        }
        if (result.get(0).isEmpty())
            result.remove(0);
        return result;
    }

    private boolean isExist(ArrayList<ArrayList<Bone>> allComb, ArrayList<Bone> tempComb) {
        boolean exist = true;
        for (ArrayList<Bone> oneComb : allComb) {
            boolean e = true;
            if (oneComb.size() == tempComb.size()) {
                for (int i = 0; i < oneComb.size(); i++) {
                    if (!oneComb.get(i).equals(tempComb.get(i))) {
                        e = false;
                        break;
                    }
                }
            } else e = false;
            exist = e;
        }
        return exist;
    }

    public ArrayList<Bone> getMaxLenghtComb(ArrayList<ArrayList<Bone>> allComb) {
        ArrayList<Bone> maxLenght = new ArrayList<>();
        for (ArrayList<Bone> oneComb : allComb) {
            if (maxLenght.size() < oneComb.size()) {
                maxLenght = oneComb;
            }
        }
        return maxLenght;
    }

    public Long insertSet(List<Bone> set){
        Long id = randomiD();
        try {
            conn.createStatement().executeUpdate("INSERT INTO sets (id_set,set_bon) VALUES ("+id+",'"+set.toString()+"')");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Access error:" + e.getMessage());
        }
        return id;
    }

    public void insertComb(List<Bone> comb, Long id_set){
        Long id = randomiD();
        try {
            conn.createStatement().executeUpdate("INSERT INTO combs (id_comb,comb,id_set) VALUES ("+id+",'"+comb.toString()+"',"+id_set+")");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Access error:" + e.getMessage());
        }
    }

    private static Long randomiD(){
        return Math.abs(UUID.randomUUID().getLeastSignificantBits());
    }
    ////////////////////======================

//    public List<Bone> getBones() {
//        Statement stmt;
//        ResultSet rs;
//        List<Bone> outData = new ArrayList<>();
//        try {
//            stmt = conn.createStatement();
//            rs = stmt.executeQuery("SELECT COUNT(*) as c from bones");
//            rs.next();
//            int resultCount = rs.getInt("c");
//            int count = (int) (Math.random() * resultCount); //Random number of bones
//            System.out.println("count:" + count);
//            outData = executeGetBones(count);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Access error:" + e.getMessage());
//        }
//        return outData;
//    }

//    public List<Bone> getBones(int count) {
//        return executeGetBones(count);
//    }
//
//    private List<Bone> executeGetBones(int count) {
//        List<Bone> outData = new ArrayList<>();
//        Statement stmt;
//        ResultSet rs;
//        int array[];
//        try {
//            stmt = conn.createStatement();
//
//            // -- Prepeare array of rundom id a bones --
//            rs = stmt.executeQuery("SELECT COUNT(*) as c from bones");
//            rs.next();
//            int resultCount = rs.getInt("c");
//            if (count < resultCount) {
//                array = new int[count];
//            } else {
//                array = new int[resultCount];
//            }
//            for (int i = 0; i < array.length; i++) {
//                int num = 0;
//                boolean f = true;
//                while (f) {
//                    num = (int) (Math.random() * resultCount);
//                    f = false;
//                    for (int j : array) {
//                        if (j == num) f = true;
//                    }
//                }
//                array[i] = num;
//            }
//            // -- geting a bones from array id --
//            String query = "";
//            for (int i : array) {
//                query = query + "UNION SELECT id, num1, num2 from bones WHERE id =" + i + " ";
//            }
//            System.out.println("query:" + query);
//            query = query.substring(6, query.length());//cuting off first "UNION"
//            rs = stmt.executeQuery(query);
//            while (rs.next()) {
//                outData.add(new Bone(rs.getInt("id"), rs.getInt("num1"), rs.getInt("num2")));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Access error:" + e.getMessage());
//        }
//        return outData;
//    }

//    public ArrayList<ArrayList<Bone>> getComb(List<Bone> bones) {
//        ArrayList<ArrayList<Bone>> result = new ArrayList<>();
//        result.add(new ArrayList<>());
//
//        for (int i = 0; i < bones.size(); i++) {
//            ArrayList<ArrayList<Bone>> current = new ArrayList<>();
//            Iterator<ArrayList<Bone>> resultIter = result.iterator();
//            while (resultIter.hasNext()) {
//                ArrayList<Bone> l = resultIter.next();
//                for (int j = 0; j < l.size() + 1; j++) {
//                    l.add(j, bones.get(i));
//                    ArrayList<Bone> temp = new ArrayList<>(l);
//                    current.add(temp);
//                    l.remove(j);
//                }
//            }
//            result = new ArrayList<>(current);
//        }
//
//        ArrayList<ArrayList<Bone>> allComb = new ArrayList<>();
//        allComb.add(new ArrayList<>());
//
//        Iterator<ArrayList<Bone>> combinations = result.iterator();
//        while (combinations.hasNext()) {
//            List<Bone> combination = combinations.next();
//            ArrayList<Bone> tempComb = new ArrayList<>();
//            int step = 0; // 2 step
//            int pos_rev = 0;// 0:1 == num1:num2
//            while (step < 2) {
//                tempComb.add(combination.get(0));
//                for (int j = 0; j < combination.size(); j++) {
//                    if (j + 1 < combination.size()) { // is next
//                        if (pos_rev == 0) {
//                            if (combination.get(j).getNum1() == combination.get(j + 1).getNum1()) {
//                                pos_rev = 1;
//                                tempComb.add(combination.get(j + 1));
//                            } else if (combination.get(j).getNum1() == combination.get(j + 1).getNum2()) {
//                                pos_rev = 0;
//                                tempComb.add(combination.get(j + 1));
//                            } else {
//                                break;
//                            }
//                        } else if (pos_rev == 1) {
//                            if (combination.get(j).getNum2() == combination.get(j + 1).getNum1()) {
//                                pos_rev = 1;
//                                tempComb.add(combination.get(j + 1));
//                            } else if (combination.get(j).getNum2() == combination.get(j + 1).getNum2()) {
//                                pos_rev = 0;
//                                tempComb.add(combination.get(j + 1));
//                            } else {
//                                break;
//                            }
//                        }
//                    }
//                }
//                pos_rev = 1;
//                step++;
//                if (tempComb.size() > 1) {
//                    boolean exist = isExist(allComb, tempComb);
//                    if (!exist) {
//                        //allComb.add(new ArrayList<>(tempComb));
//                        allComb.add(tempComb);
//                    }
//                }
//                tempComb = new ArrayList<>();
//            }
//        }
//        if (allComb.get(0).isEmpty())
//            allComb.remove(0);
//        return allComb;
//    }

}
