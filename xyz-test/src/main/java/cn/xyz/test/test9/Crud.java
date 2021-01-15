package cn.xyz.test.test9;

public class Crud<T extends BaseMapper> {
    public void find(){
        T.find();
    }
    public void add(){
        T.add();
    }
}
