package ua.ibt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IRINA on 23.04.2017.
 */
public class DominoService {
    private Connection conn;

    public DominoService() {
        this.conn = new ConnectJDBC().getConnect();
    }

    public List<Bone> getBones() {
        Statement stmt;
        ResultSet rs;
        List<Bone> outData = new ArrayList<>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) as c from bones");
            rs.next();
            int resultCount = rs.getInt("c");
            int count = (int) (Math.random() * resultCount); //Random number of bones
            System.out.println("count:" + count);
            outData = executeGetBones(count);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Access error:" + e.getMessage());
        }
        return outData;
    }

    public List<Bone> getBones(int count) {
        return executeGetBones(count);
    }

    private List<Bone> executeGetBones(int count) {
        List<Bone> outData = new ArrayList<>();
        Statement stmt;
        ResultSet rs;
        int array[];
        try {
            stmt = conn.createStatement();

            // -- Prepeare array of rundom id a bones --
            rs = stmt.executeQuery("SELECT COUNT(*) as c from bones");
            rs.next();
            int resultCount = rs.getInt("c");
            if (count < resultCount) {
                array = new int[count];
            } else {
                array = new int[resultCount];
            }
            for (int i = 0; i < array.length; i++) {
                int num = 0;
                boolean f = true;
                while (f) {
                    num = (int) (Math.random() * resultCount);
                    f = false;
                    for (int j : array) {
                        if (j == num) f = true;
                    }
                }
                array[i] = num;
            }
            // -- geting a bones from array id --
            String query = "";
            for (int i : array) {
                query = query + "UNION SELECT id, num1, num2 from bones WHERE id =" + i + " ";
            }
            System.out.println("query:" + query);
            query = query.substring(6, query.length());//cuting off first "UNION"
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                outData.add(new Bone(rs.getInt("id"), rs.getInt("num1"), rs.getInt("num2")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Access error:" + e.getMessage());
        }
        return outData;
    }

    public ArrayList<Bone> getMaxLenghtComb(ArrayList<ArrayList<Bone>> allComb) {
        ArrayList<Bone> maxLenght = new ArrayList<>();
        for (ArrayList<Bone> oneComb : allComb) {
            if (maxLenght.size() < oneComb.size()){
                maxLenght = oneComb;
            }
        }
        return maxLenght;
    }

    public ArrayList<ArrayList<Bone>> getComb(List<Bone> bones) {
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

        ArrayList<ArrayList<Bone>> allComb = new ArrayList<>();
        allComb.add(new ArrayList<>());

        Iterator<ArrayList<Bone>> combinations = result.iterator();
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
                    boolean exist = isExist(allComb, tempComb);
                    if (!exist) {
                        //allComb.add(new ArrayList<>(tempComb));
                        allComb.add(tempComb);
                    }
                }
                tempComb = new ArrayList<>();
            }
        }
        if(allComb.get(0).isEmpty())
            allComb.remove(0);
        return allComb;
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
}
