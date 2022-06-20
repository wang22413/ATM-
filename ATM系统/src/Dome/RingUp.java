package Dome;

import JDBC.ATMDAOImpl;
import JDBC.JDBCUtil;
import JDBC.Users;

import java.sql.Connection;
import java.util.Scanner;

import static Dome.main.menu;

public class RingUp {
    private static ATMDAOImpl adi = new ATMDAOImpl();
    private static Scanner sc = new Scanner(System.in);
    private static Connection conn = null;

    /**
     * 登录
     */
    static void ringUp() {
        conn = JDBCUtil.getConnection();
        System.out.println("------登录界面------");
        System.out.println("请输入卡号:");
        int id = sc.nextInt();
        System.out.println("请输入密码");
        int password = sc.nextInt();
        Users user = adi.getMessage(conn, new Users(id,password));
        if (user != null) {
            System.out.println(user.getName() +"贵宾," + "恭喜登录成功");
            System.out.println("----------------");
            OS.os(user);
        } else {
            System.out.println("用户名或密码错误");
            menu();
        }
    }

}
