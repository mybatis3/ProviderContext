package base;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import provider.OurSqlBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public interface BaseMapper<T> {

  @SelectProvider(type= OurSqlBuilder.class, method= "buildSelectByIdProviderContextOnly")
  @ContainsLogicalDelete
  T selectById(Integer id);

  @SelectProvider(type= OurSqlBuilder.class, method= "buildSelectByIdProviderContextOnly")
  T selectActiveById(Integer id);

  @SelectProvider(type= OurSqlBuilder.class, method= "buildSelectByNameOneParamAndProviderContext")
  @ContainsLogicalDelete
  List<T> selectByName(String name);

  @SelectProvider(type= OurSqlBuilder.class, method= "buildSelectByNameOneParamAndProviderContext")
  List<T> selectActiveByName(String name);

  @SelectProvider(type= OurSqlBuilder.class, method= "buildSelectByIdAndNameMultipleParamAndProviderContextWithAtParam")
  @ContainsLogicalDelete
  List<T> selectByIdAndNameWithAtParam(@Param("id") Integer id, @Param("name") String name);

  @SelectProvider(type= OurSqlBuilder.class, method= "buildSelectByIdAndNameMultipleParamAndProviderContextWithAtParam")
  List<T> selectActiveByIdAndNameWithAtParam(@Param("id") Integer id, @Param("name") String name);

  @SelectProvider(type= OurSqlBuilder.class, method= "buildSelectByIdAndNameMultipleParamAndProviderContext")
  @ContainsLogicalDelete
  List<T> selectByIdAndName(Integer id, String name);

  @SelectProvider(type= OurSqlBuilder.class, method= "buildSelectByIdAndNameMultipleParamAndProviderContext")
  List<T> selectActiveByIdAndName(Integer id, String name);

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @interface ContainsLogicalDelete {
    boolean value() default false;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  @interface Meta {
    String tableName();
  }

}
