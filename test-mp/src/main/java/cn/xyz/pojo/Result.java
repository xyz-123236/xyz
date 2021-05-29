package cn.xyz.pojo;


import cn.xyz.config.CustomException;
import lombok.Data;

@Data
public class Result {

	private boolean status;
	private Integer code;
	private String msg;
	private Object rows;
	private Integer total;
	//KindEditor: 成功返回{error：0, url：""},失败：{error：1, message：""}
	private Integer error;
	private String url;
	private String message;

	//private static Logger logger = Logger.getLogger(Result.class.getName());

	public Result(Integer code, String msg) {
		this.error = code;
		this.url = msg;
		this.message = msg;
	}
	public Result(Object data, Integer total, String msg, Integer code, boolean status) {
		this.status = status;
		this.code = code;
		this.msg = msg;
		this.rows = data;
		this.total = total;
	}


	public static Result success(){
		return success("");
	}
	/*public static Result success(String msg){
		return success(new JSONArray(), msg);
	}*/
	public static Result success(Object data){
		return success(data, "");
	}
	public static Result success(Object data, String msg){
		return success(data, 0, msg);
	}
	public static Result success(Object data, Integer total){
		return success(data, total, "");
	}
	public static Result success(Object data, Integer total, String msg){
		return success(data, total, msg, 200);
	}
	public static Result success(Object data, Integer total, String msg, Integer code){
		return result(data, total, msg, code, true);
	}

	public static Result error(String msg){
		return result(null, null, msg, 500, false);
	}
	public static Result error(String msg, Object data){
		return result(data, null, msg, 500, false);
	}

	public static Result result(Object data, Integer total, String msg, Integer code, boolean status) {
		return new Result(data, total, msg, code, status);
	}

	//KindEditor返回码
	public static Result result(Integer code, String msg){//KindEditor使用
		return new Result(code, msg);
	}

	public static Result error(Exception e) {
		/*if(!Tools.isEmpty(e.getMessage()) && e.getMessage().contains("unique constraint violated")) {
			return error("主键重复");
		}*/
		if(e instanceof CustomException) {
			return error(e.getMessage());
		}
		//log.error("程序异常: ", e);//可发邮件，存数据库
		return error("程序异常: ", e);
	}
}
