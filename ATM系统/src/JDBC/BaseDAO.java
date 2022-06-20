package JDBC;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDAO {
    //通用的增删改操作---version 2.0(考虑上事务)
    public int update(Connection conn, String sql, Object...args) {
        PreparedStatement ps = null;
        try {
            // 1.预编译sql语句，返回PrepareStatement的实例
            ps = conn.prepareStatement(sql);
            // 2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            // 3.执行
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }/* finally {
            // 4.资源的关闭
            JDBCUtil.closeResource(null, ps);
        }*/
        return 0;
    }

    //通用的查询操作,用与返回数据表中的单条记录构成的集合 (version 2.0考虑上事务)
    public static <T> T getInstance(Connection conn,Class<T> clazz,String sql,Object...args) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);

            for (int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = clazz.newInstance();

                for (int i=0;i<columnCount;i++) {
                    Object object = rs.getObject(i + 1);

                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,object);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }/* finally {
            JDBCUtil.closeResource(null,ps,rs);
        }*/
        return null;
    }

    //通用的查询操作,用与返回数据表中的多条记录构成的集合 (version 2.0考虑上事务)
    public <T> List<T> getForList(Connection conn, Class<T> clazz, String sql, Object...args) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1.预编译sql语句，返回PrepareStatement的实例
            ps = conn.prepareStatement(sql);
            // 2.填充占位符
            for (int i=0;i<args.length;i++){
                ps.setObject(i+1,args[i]);
            }
            // 3.执行
            rs = ps.executeQuery();
            // 4.获取结果集元数据：ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 5.通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            // 6.创建集合对象
            ArrayList<T> list = new ArrayList<>();
            while (rs.next()) {
                T t = clazz.newInstance();
                // 7.处理结果集一行数据中的每一个列,给t对象指定的属性赋值
                for (int i=0;i<columnCount;i++) {
                    // 8.获取列值
                    Object columnValue = rs.getObject(i + 1);
                    // 9.获取每个列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    // 10.给t对象指定的columnLabel属性,赋值为通过反射
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }/* finally {
            JDBCUtil.closeResource(null,ps,rs);
        }*/
        return null;
    }

    //用于查询特殊值
    public static  <E> E getValue(Connection conn,String sql,Object...args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                return (E) rs.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }/* finally {
            JDBCUtil.closeResource(null,ps,rs);
        }*/
        return null;
    }

}
