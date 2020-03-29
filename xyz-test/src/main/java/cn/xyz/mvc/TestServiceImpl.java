package cn.xyz.mvc;

@MyService
public class TestServiceImpl implements TestService {

    @Override
    public void printParam(String param) {
        System.out.println("接收到的参数为： "+param);
    }
}

