import com.jab.util.DBUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: goshawker@yeah.net
 * @Description:
 * @Date: 2022/09/17 10:48:31
 * @Version: 1.0
 */
public class DBUtilsTest {
  public static void main(String[] args) {
    try {
      Connection connection1 = DBUtils.buildConnect();
      java.sql.PreparedStatement preparedStatement = connection1.prepareStatement("select * from stu where name=?");
      preparedStatement.setString(1,"mike");
      java.sql.ResultSet resultSet = preparedStatement.executeQuery();
      while(resultSet.next()){
        String id = resultSet.getString("id");
        String name =  resultSet.getString("name");
        int age  = resultSet.getInt("age");
        String sex  =  resultSet.getString("sex");
        Date birthdate =  resultSet.getDate("BirthDate");
        System.out.printf("%s %s %d %s %tF" ,id,name,age,sex ,birthdate);
        System.out.println();
      }

    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
