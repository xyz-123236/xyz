package cn.xyz.tool;

import cn.xyz.pojo.Student;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

public class StudentListener extends AnalysisEventListener<Student> {

    //每行调用
    @Override
    public void invoke(Student student, AnalysisContext analysisContext) {
        System.out.println(student);
    }
    //读取完整个文档后调用
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
