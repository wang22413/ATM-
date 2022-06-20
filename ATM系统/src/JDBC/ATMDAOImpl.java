package JDBC;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class ATMDAOImpl extends BaseDAO implements ATMDAO {

    /**
     * 查询用户的数量
     * @param conn
     * @return
     */
    @Override
    public int inquireCount(Connection conn) {
        Object i = null;
        try {
            String sql = "select count(*) from users";
            i = getValue(conn, sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  Integer.parseInt(i.toString());
    }

    /**
     * 查询余额,并返回
     * @param conn
     * @return
     */
    @Override
    public BigDecimal getBalance(Connection conn,int id) {
        Object value = null;
        try {
            String sql = "select balance from users where id = ?";
            value = getValue(conn, sql,id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn,null);
        }
        return new BigDecimal(value.toString());
    }

    /**
     * 添加新用户,并返回新用户信息
     * @param conn
     * @param user
     */
    @Override
    public Users insert(Connection conn, Users user) {
        try {
            String sql1 = "insert into users(name,password) values(?,?)";
            int i = update(conn, sql1, user.getName(), user.getPassword());
            if (i != 0) {
                String sql2 = "select id,name,balance from users limit ? , 1";
                Users users = getInstance(conn, user.getClass(), sql2, (inquireCount(conn) - 1));
                return users;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn,null);
        }
        return null;
    }

    /**
     * 修改密码
     * @param conn
     * @param user
     * @param password
     * @return
     */
    @Override
    public Boolean changePassword(Connection conn, Users user, int password) {
        try {
            String sql = "update users set password = ? where id = ?";
            int i = update(conn, sql, password, user.getId());
            if (i == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn,null);
        }
        return false;
    }

    /**
     * 注销当前账号
     * @param conn
     * @param user
     * @return
     */
    @Override
    public Boolean writeOff(Connection conn,Users user) {
        try {
            String sql = "delete from users where id = ?";
            int i = update(conn, sql, user.getId());
            if (i == 1) return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException t) {
                t.printStackTrace();
            }
        } finally {
            JDBCUtil.closeResource(conn,null);
        }
        return false;
    }

    /**
     * 根据id查用户是否存在
     * @param id
     * @return
     */
    @Override
    public int inquireId(Connection conn,int id) {
        Object value = null;
        try {
            String sql = "select count(*) from users where id = ?";
            value = getValue(conn, sql, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn,null);
        }
        return Integer.parseInt(value.toString());
    }

    /**
     * 根据id查用户密码
     * @param id
     * @return
     */
    @Override
    public int getPasswordById(Connection conn,int id) {
        Object password = null;
        try {
            String sql = "select password from users where id = ?";
            password = getValue(conn, sql, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn,null);
        }
        return Integer.parseInt(password.toString());
    }

    /**
     * 验证账号密码,返回用户信息
     * @param conn
     * @param user
     * @return
     */
    @Override
    public Users getMessage(Connection conn,Users user) {
        try {
            String sql = "select id,name,balance from users where id = ? and password = ?";
            Users users = getInstance(conn,user.getClass(),sql,user.getId(),user.getPassword());
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeResource(conn,null);
        }
        return null;
    }

    /**
     * 存款
     * @param conn
     * @param id
     * @param balance
     * @return
     */
    @Override
    public Boolean deposit(Connection conn, int id, BigDecimal balance) {
        try {
            String sql1 = "update users set balance = balance + ? where id = ?";
            int i = update(conn, sql1, balance, id);
            if (i != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException t) {
                t.printStackTrace();
            } finally {
                JDBCUtil.closeResource(conn,null);
            }
        }

        return false;
    }

    /**
     * 取款
     * @param conn
     * @param id
     * @param balance
     * @return
     */
    @Override
    public Boolean withdrawal(Connection conn, int id, BigDecimal balance) {
        try {
            String sql1 = "update users set balance = balance - ? where id = ?";
            int i = update(conn, sql1, balance, id);
            if (i != 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException t) {
                t.printStackTrace();
            } finally {
                JDBCUtil.closeResource(conn,null);
            }
        }
        return false;
    }


    /**
     * 转账
     * @param conn
     * @param id1,id2
     * @param balance
     */
    @Override
    public void transfer(Connection conn, int id1,int id2, BigDecimal balance) {
        try {
            String sql1 = "update users set balance = balance - ? where id = ?";
            update(conn,sql1,balance,id1);
            String sql2 = "update users set balance = balance + ? where id = ?";
            update(conn,sql2,balance,id2);
        } catch (Exception e) {
            System.out.println("转账失败");
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException t) {
                t.printStackTrace();
            }
        } finally {
            JDBCUtil.closeResource(conn,null);
        }
    }
}
