package ua.ibt;

/**
 * Created by IRINA on 22.04.2017.
 */
public class Bone {
    private int id;
    private int num1;
    private int num2;

    public Bone(int id, int num1, int num2) {
        this.id = id;
        this.num1 = num1;
        this.num2 = num2;
    }

    public int getNum1() {
        return num1;
    }

    public int getNum2() {
        return num2;
    }

    @Override
    public String toString() {
        return "[" + num1 + ":" + num2 + "] ";
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Bone)) {
            return false;
        }
        Bone other = (Bone) object;
        return this.id == other.id;
    }


}
