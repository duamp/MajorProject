package sample;

public class History {
    public History(String id,int num,String v1, String v2, String v3){
        System.out.println(id);
        System.out.println(num);
    }
    public void print(){ //method includes setter and getter. void = mutator
        System.out.println("Used constructor, to create method");

    }
    public int return_int(){ //getter/accessor
        return 1;
    }

}
