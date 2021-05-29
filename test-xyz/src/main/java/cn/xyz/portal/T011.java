package cn.xyz.portal;

public class T011 {
    public static void doit(){
        int[] a = new int[] { 460, 720, 1250, 1800, 2200, 3080, 4100, 4750,
                6510, 6900, 9000 };
        int i = 0;
        for(int i1=0;i1<a.length;i1++){
            for(int i2=i1+1;i2<a.length;i2++){
                if(a[i1]+a[i2]==13750){
                    System.out.println(a[i1]+";"+a[i2]);
                }
                for(int i3=i2+1;i3<a.length;i3++){
                    if(a[i1]+a[i2]+a[i3]==13750){
                        System.out.println(a[i1]+";"+a[i2]+";"+a[i3]);
                    }
                    for(int i4=i3+1;i4<a.length;i4++){
                        if(a[i1]+a[i2]+a[i3]+a[i4]==13750){
                            System.out.println(a[i1]+";"+a[i2]+";"+a[i3]+";"+a[i4]);
                        }
                        for(int i5=i4+1;i5<a.length;i5++){
                            if(a[i1]+a[i2]+a[i3]+a[i4]+a[i5]==13750){
                                System.out.println(a[i1]+";"+a[i2]+";"+a[i3]+";"+a[i4]+";"+a[i5]);
                            }
                            for(int i6=i5+1;i6<a.length;i6++){
                                if(a[i1]+a[i2]+a[i3]+a[i4]+a[i5]+a[i6]==13750){
                                    System.out.println(a[i1]+";"+a[i2]+";"+a[i3]+";"+a[i4]+";"+a[i5]+";"+a[i6]);
                                }
                                for(int i7=i6+1;i7<a.length;i7++){
                                    i++;
                                    if(a[i1]+a[i2]+a[i3]+a[i4]+a[i5]+a[i6]+a[i7]==13750){
                                        System.out.println(a[i1]+";"+a[i2]+";"+a[i3]+";"+a[i4]+";"+a[i5]+";"+a[i6]+";"+a[i7]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(i);
    }
    public static void main(String[] args) {
        doit();
    }
}
