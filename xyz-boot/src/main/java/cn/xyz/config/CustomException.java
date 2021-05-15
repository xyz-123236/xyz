package cn.xyz.config;

public class CustomException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public CustomException(){
        super();
    }
    public CustomException(String message){
        super(message);
        //Do something...
    }
    
}
