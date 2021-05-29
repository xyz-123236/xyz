package cn.xyz.portal;

import java.util.Vector;

public class T012 {
    Vector<Vector<Integer>> all=new Vector<Vector<Integer>>();
    int[] array = new int[] { 460, 588, 720, 1250, 1800, 2200, 3080, 4100, 4750,
            4375, 5184, 6510, 6900, 9000 };
    int X = 0;
    public T012(){
        for(int i=0;i<this.array.length;i++){
            Vector<Integer> v=new Vector<Integer>();
            pro(v,i);
        }
        for(int i=0;i<this.all.size();i++){
            System.out.println(this.all.get(i).toString());
        }
    }
    private boolean pro(Vector<Integer> v,int index){
        v.add(this.array[index]);
        int sum=sum(v);
        if(sum<=13750){
            if(sum==13750){
            	this.all.add((Vector<Integer>)v.clone());
                v.remove(v.size()-1);
                System.out.println(this.X);
                return true;
            }
            for(int i=index+1;i<this.array.length;i++){
            	this.X++;
                if(!pro(v,i)){
                    break;
                }
            }
            v.remove(v.size()-1);
            return true;
        }else{
            v.remove(v.size()-1);
            return false;
        }
    }
    private static int sum(Vector<Integer> v){
        int sum=0;
        for(int i=0;i<v.size();i++){
            sum+=v.get(i);
        }
        return sum;
    }

    public static void main(String arg[]){
        new T012();
    }

}
