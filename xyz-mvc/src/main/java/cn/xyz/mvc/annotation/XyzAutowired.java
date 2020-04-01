package cn.xyz.mvc.annotation;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XyzAutowired {
    String value() default "";
}
