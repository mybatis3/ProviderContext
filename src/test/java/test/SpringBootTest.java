package test;

import mapper.UserMapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@Rollback
@RunWith(SpringRunner.class)
@org.springframework.boot.test.context.SpringBootTest(classes = Application.class)
public class SpringBootTest {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private SqlSession sqlSession;

  // Test for list
  @Test
  public void shouldGetTwoUsers() {
    List<Integer> list = new ArrayList<Integer>();
    list.add(1);
    list.add(3);
    List<User> users = userMapper.getUsers(list);
    assertEquals(2, users.size());
    assertEquals("User1", users.get(0).getName());
    assertEquals("User3", users.get(1).getName());
  }

  // Test for simple value without @Param
  @Test
  public void shouldGetOneUser() {
    {
      User user = userMapper.getUser(4);
      assertNotNull(user);
      assertEquals("User4", user.getName());
    }
    {
      User user = userMapper.getUser(null);
      assertNull(user);
    }
  }

  // Test for empty
  @Test
  public void shouldGetAllUsers() {
    List<User> users = userMapper.getAllUsers();
    assertEquals(4, users.size());
    assertEquals("User1", users.get(0).getName());
    assertEquals("User2", users.get(1).getName());
    assertEquals("User3", users.get(2).getName());
    assertEquals("User4", users.get(3).getName());
  }

  // Test for single JavaBean
  @Test
  public void shouldGetUsersByCriteria() {
    {
      User criteria = new User();
      criteria.setId(1);
      List<User> users = userMapper.getUsersByCriteria(criteria);
      assertEquals(1, users.size());
      assertEquals("User1", users.get(0).getName());
    }
    {
      User criteria = new User();
      criteria.setName("User");
      List<User> users = userMapper.getUsersByCriteria(criteria);
      assertEquals(4, users.size());
      assertEquals("User1", users.get(0).getName());
      assertEquals("User2", users.get(1).getName());
      assertEquals("User3", users.get(2).getName());
      assertEquals("User4", users.get(3).getName());
    }
  }

  // Test for single map
  @Test
  public void shouldGetUsersByCriteriaMap() {
    {
      Map<String, Object> criteria = new HashMap<String, Object>();
      criteria.put("id", 1);
      List<User> users = userMapper.getUsersByCriteriaMap(criteria);
      assertEquals(1, users.size());
      assertEquals("User1", users.get(0).getName());
    }
    {
      Map<String, Object> criteria = new HashMap<String, Object>();
      criteria.put("name", "User");
      List<User> users = userMapper.getUsersByCriteriaMap(criteria);
      assertEquals(4, users.size());
      assertEquals("User1", users.get(0).getName());
      assertEquals("User2", users.get(1).getName());
      assertEquals("User3", users.get(2).getName());
      assertEquals("User4", users.get(3).getName());
    }
  }

  // Test for multiple parameter without @Param
  @Test
  public void shouldGetUsersByName() {
    List<User> users = userMapper.getUsersByName("User", "id DESC");
    assertEquals(4, users.size());
    assertEquals("User4", users.get(0).getName());
    assertEquals("User3", users.get(1).getName());
    assertEquals("User2", users.get(2).getName());
    assertEquals("User1", users.get(3).getName());
  }

  // Test for map without @Param
  @Test
  public void shouldGetUsersByNameUsingMap() {
    List<User> users = userMapper.getUsersByNameUsingMap("User", "id DESC");
    assertEquals(4, users.size());
    assertEquals("User4", users.get(0).getName());
    assertEquals("User3", users.get(1).getName());
    assertEquals("User2", users.get(2).getName());
    assertEquals("User1", users.get(3).getName());
  }

  // Test for multiple parameter with @Param
  @Test
  public void shouldGetUsersByNameWithParamNameAndOrderBy() {
    List<User> users = userMapper.getUsersByNameWithParamNameAndOrderBy("User", "id DESC");
    assertEquals(4, users.size());
    assertEquals("User4", users.get(0).getName());
    assertEquals("User3", users.get(1).getName());
    assertEquals("User2", users.get(2).getName());
    assertEquals("User1", users.get(3).getName());
  }

  // Test for map with @Param
  @Test
  public void shouldGetUsersByNameWithParamNameUsingMap() {
    List<User> users = userMapper.getUsersByNameWithParamNameAndOrderBy("User", "id DESC");
    assertEquals(4, users.size());
    assertEquals("User4", users.get(0).getName());
    assertEquals("User3", users.get(1).getName());
    assertEquals("User2", users.get(2).getName());
    assertEquals("User1", users.get(3).getName());
  }

  // Test for simple value with @Param
  @Test
  public void shouldGetUsersByNameWithParamName() {
    {
      List<User> users = userMapper.getUsersByNameWithParamName("User");
      assertEquals(4, users.size());
      assertEquals("User4", users.get(0).getName());
      assertEquals("User3", users.get(1).getName());
      assertEquals("User2", users.get(2).getName());
      assertEquals("User1", users.get(3).getName());
    }
    {
      List<User> users = userMapper.getUsersByNameWithParamName(null);
      assertEquals(4, users.size());
      assertEquals("User4", users.get(0).getName());
      assertEquals("User3", users.get(1).getName());
      assertEquals("User2", users.get(2).getName());
      assertEquals("User1", users.get(3).getName());
    }
  }

  @Test
  public void notSqlProvider() throws NoSuchMethodException {
    try {
      new ProviderSqlSource(new Configuration(), new Object(), null, null);
      fail();
    } catch (BuilderException e) {
      assertTrue(e.getMessage().contains("Error creating SqlSource for SqlProvider.  Cause: java.lang.NoSuchMethodException: java.lang.Object.type()"));
    }
  }

  @Test
  public void notSupportParameterObjectOnMultipleArguments() throws NoSuchMethodException {
    try {
      Class<?> mapperType = UserMapper.class;
      Method mapperMethod = mapperType.getMethod("getUsersByName", String.class, String.class);
      new ProviderSqlSource(new Configuration(),
              mapperMethod.getAnnotation(SelectProvider.class), mapperType, mapperMethod)
              .getBoundSql(new Object());
      fail();
    } catch (BuilderException e) {
      assertTrue(e.getMessage().contains("Error invoking SqlProvider method (provider.OurSqlBuilder.buildGetUsersByNameQuery). Cannot invoke a method that holds multiple arguments using a specifying parameterObject. In this case, please specify a 'java.util.Map' object."));
    }
  }

  @Test
  public void notSupportParameterObjectOnNamedArgument() throws NoSuchMethodException {
    try {
      Class<?> mapperType = UserMapper.class;
      Method mapperMethod = mapperType.getMethod("getUsersByNameWithParamName", String.class);
      new ProviderSqlSource(new Configuration(),
              mapperMethod.getAnnotation(SelectProvider.class), mapperType, mapperMethod)
              .getBoundSql(new Object());
      fail();
    } catch (BuilderException e) {
      assertTrue(e.getMessage().contains("Error invoking SqlProvider method (provider.OurSqlBuilder.buildGetUsersByNameWithParamNameQuery). Cannot invoke a method that holds named argument(@Param) using a specifying parameterObject. In this case, please specify a 'java.util.Map' object."));
    }
  }

  @Test
  @Rollback
  public void shouldInsertUser() {
    User user = new User();
    user.setId(999);
    user.setName("MyBatis");
    userMapper.insert(user);

    User loadedUser = userMapper.getUser(999);
    assertEquals("MyBatis", loadedUser.getName());

    userMapper.delete(999);
  }

  @Test
  @Rollback
  public void shouldUpdateUser() {
    User user = new User();
    user.setId(999);
    user.setName("MyBatis2");
    userMapper.insert(user);

    user.setName("MyBatis3");
    userMapper.update(user);

    User loadedUser = userMapper.getUser(999);
    assertEquals("MyBatis3", loadedUser.getName());

    userMapper.delete(999);
  }

  @Test
  @Rollback
  public void shouldDeleteUser() {
    User user = new User();
    user.setId(999);
    user.setName("MyBatis2");
    userMapper.insert(user);

    user.setName("MyBatis3");
    userMapper.delete(999);

    User loadedUser = userMapper.getUser(999);
    assertNull(loadedUser);
  }

  @Test
  public void mapperProviderContextOnly() {
    assertEquals("User4", userMapper.selectById(4).getName());
    assertNull(userMapper.selectActiveById(4));
  }

  @Test
  public void mapperOneParamAndProviderContext() {
    assertEquals(1, userMapper.selectByName("User4").size());
    assertEquals(0, userMapper.selectActiveByName("User4").size());
  }

  @Test
  public void mapperMultipleParamAndProviderContextWithAtParam() {
    assertEquals(1, userMapper.selectByIdAndNameWithAtParam(4,"User4").size());
    assertEquals(0, userMapper.selectActiveByIdAndNameWithAtParam(4,"User4").size());
  }

  @Test
  public void mapperMultipleParamAndProviderContext() {
    assertEquals(1, userMapper.selectByIdAndName(4,"User4").size());
    assertEquals(0, userMapper.selectActiveByIdAndName(4,"User4").size());
  }

}
