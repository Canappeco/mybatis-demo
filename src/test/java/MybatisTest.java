import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

public class MybatisTest {

    public static String resource = "mybatis-config.xml";
    public static InputStream inputStream = null;
    public static SqlSessionFactory sqlSessionFactory = null;
    public static SqlSession sqlSession = null;

    private String url = "jdbc:mysql://localhost:3306/mybatis_test";
    private String userName = "root";
    private String password = "qwer1234";

    static {
        try {
            inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            sqlSession = sqlSessionFactory.openSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void selectTest() {
        try {
            User user = sqlSession.selectOne("cn.zmhappy.UserMapper.selectUser", "1");
            System.out.println(user.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void selectAllTest() {
        try {
            List<User> list = sqlSession.selectList("cn.zmhappy.UserMapper.selectManyUsers");
            list.forEach(e -> {
                System.out.println(e.getName());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateTest() {
        try {
            sqlSession.update("cn.zmhappy.UserMapper.updateUser", new User("1", "zhangsan", "girl", new Date(), "21"));
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    //可以同时插入多个
    public void insertTest() {
        try {
            sqlSession.update("cn.zmhappy.UserMapper.insertUser", new User("5", "xiaoyang", "man", new Date(), "8"));
            sqlSession.update("cn.zmhappy.UserMapper.insertUser", new User("6", "xiaoyang", "man", new Date(), "8"));
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteTest() {
        try {
            sqlSession.update("cn.zmhappy.UserMapper.deleteUser", "1");
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void scriptRunnerTest() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, userName, password);
            ScriptRunner scriptRunner = new ScriptRunner(conn);
            //下面配置不要随意更改，否则会出现各种问题
            scriptRunner.setAutoCommit(true);//自动提交
            scriptRunner.setFullLineDelimiter(false);
            scriptRunner.setDelimiter(";");////每条命令间的分隔符
            scriptRunner.setSendFullScript(false);
            scriptRunner.setStopOnError(false);
            //  runner.setLogWriter(null);//设置是否输出日志
            //如果又多个sql文件，可以写多个runner.runScript(xxx),
            scriptRunner.runScript(new InputStreamReader(new FileInputStream("bin/mysql.sh"),"utf-8"));
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
