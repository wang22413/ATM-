package Dome;

import JDBC.ATMDAOImpl;
import JDBC.JDBCUtil;
import JDBC.Users;

import java.sql.Connection;
import java.util.Scanner;

import static Dome.main.menu;

public class SignIn {
    private static ATMDAOImpl adi = new ATMDAOImpl();
    private static Scanner sc = new Scanner(System.in);
    private static Connection conn = JDBCUtil.getConnection();
    private static int password;
    private static Users user;

    /**
     * 注册
     */
    static void signIn() {
        System.out.println("------注册界面------");
        System.out.println("请输入用户名:");
        String name = sc.next();
        System.out.println("请输入密码:(6位数字组成)");
        password = sc.nextInt();
        while (String.valueOf(password).length() != 6) {
            System.out.print("密码不足6位或超过6位,请重新输入:");
            password = sc.nextInt();
        }
        user = new Users(name,password);
        user = adi.insert(conn, user);
        if (user != null) {
            System.out.println(name +"贵宾," + "恭喜注册成功,您的卡号是:" + user.getId());
            OS.os(user);
        } else {
            System.out.println("注册失败");
            System.out.println("重新注册请按:1(其他键退出)");
            if (sc.nextInt() == 1) {
                signIn();
            } else {
                System.out.println("退出成功");
                menu();
            }
        }

    }
}
