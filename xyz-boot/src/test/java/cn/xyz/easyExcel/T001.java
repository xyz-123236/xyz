package cn.xyz.easyExcel;

import cn.xyz.mapper.UserMapper;
import cn.xyz.pojo.Student;
import cn.xyz.pojo.User;
import cn.xyz.tool.StudentListener;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest//测试的包名要与启动MainApplication一致，否则要加(classes = MainApplication.class)
public class T001 {
	@Resource
	private UserMapper userMapper;

	@Test
	public void test1() {
		ExcelReaderBuilder read = EasyExcel.read("E:\\temp\\easy_excel.xlsx", Student.class, new StudentListener());
		ExcelReaderSheetBuilder sheet = read.sheet();
		sheet.headRowNumber(2);//默认1行
		sheet.autoTrim(true);
		sheet.doRead();
	}

	@Test
	public void test2() {
		ExcelWriterBuilder write = EasyExcel.write("E:\\temp\\test.xlsx", Student.class);
		ExcelWriterSheetBuilder sheet1 = write.sheet();
		List<Student> list = init();
		sheet1.doWrite(list);
	}
	private static List<Student> init(){
		List<Student> students = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Student s = new Student();
			s.setName("姓名"+i);
			s.setGender("班级"+i);
			s.setBirthday("2000-01-01");
			s.setId(i/2.0);
			students.add(s);
		}
		return students;
	}
}
