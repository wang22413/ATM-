package JDBC;

import java.math.BigDecimal;
import java.sql.Connection;

public interface ATMDAO {

    /**
     * 查询新添加的用户的id
     * @param conn
     * @return
     */
    int inquireCount(Connection conn);

    /**
     * 查询余额,并返回
     * @param conn
     * @return
     */
    BigDecimal getBalance(Connection conn,int id);

    /**
     * 添加用户,并返回用户信息
     * @param conn
     * @param user
     */
    Users insert(Connection conn, Users user);

    /**
     * 修改密码
     * @param conn
     * @param user
     * @param password
     * @return
     */
    Boolean changePassword(Connection conn,Users user,int password);

    /**
     * 注销当前账号
     * @param conn
     * @param user
     * @return
     */
    Boolean writeOff(Connection conn,Users user);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    int inquireId(Connection conn, int id);

    /**
     * 根据id查用户密码
     * @param conn
     * @param id
     * @return
     */
    int getPasswordById(Connection conn,int id);

    /**
     * 验证账号密码,返回用户信息
     * @param conn
     * @param user
     */
    Users getMessage(Connection conn,Users user);

    /**
     * 存款
     */
    Boolean deposit(Connection conn, int id, BigDecimal balance);

    /**
     * 取款
     */
    Boolean withdrawal(Connection conn, int id, BigDecimal balance);

    /**
     * 转账
     * @param conn
     * @param id1,id2
     */
    void transfer(Connection conn, int id1,int id2, BigDecimal balance);
}
