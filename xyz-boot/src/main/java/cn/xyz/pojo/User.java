package cn.xyz.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
	private Long id;
    private String name;
    private Integer age;
    private String email;
    private Long manager_id;
    private LocalDateTime create_time;
}
