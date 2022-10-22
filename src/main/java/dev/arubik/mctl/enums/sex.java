package dev.arubik.mctl.enums;

public enum Sex {
    male(0), female(1);

    private int sex = 0;

    private Sex(int sex){
        this.sex = sex;
    }

    public String getSign(){
        if(sex==0){
            return "♂";
        }
        return "♀";
    }
    public String getColor(){
        if(sex==0){
            return "<#FFC0CB>";
        }
        return "<#00BFFF>";
    }

    public static Boolean contains(String arg) {
        for (Sex arg0 : Sex.values()) {
            if (arg0.toString().equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }
}
