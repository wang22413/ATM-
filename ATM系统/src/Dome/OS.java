package Dome;

import JDBC.ATMDAOImpl;
import JDBC.JDBCUtil;
import JDBC.Users;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static Dome.main.menu;

public class OS {

    private static Scanner sc = new Scanner(System.in);
    private static ATMDAOImpl adi = new ATMDAOImpl();
    private static Connection conn = null;
    private static BigDecimal balance = new BigDecimal("0.00");
    private static BigDecimal balanceToDay = new BigDecimal("30000.00");

    public static void os(Users user) {
        conn = JDBCUtil.getConnection();
        System.out.println("---欢迎来到WH银行用户操作系统---");
        System.out.println("1.查询");
        System.out.println("2.存款");
        System.out.println("3.取款");
        System.out.println("4.转账");
        System.out.println("5.修改密码");
        System.out.println("6.注销当前账号");
        System.out.print("您可以继续选择功能进行操作(其他键退出当前账号):");
        int i = sc.nextInt();
        switch (i) {
            case 1:
                System.out.println("---   个人信息如下   ---");
                inquire(user);break;
            case 2:
                System.out.println("--- 欢迎来到存款界面  ---");
                deposit(user);break;
            case 3:
                System.out.println("--- 欢迎来到取款界面  ---");
                withdrawal(user);break;
            case 4:
                System.out.println("--- 欢迎来到转账界面  ---");
                transfer(user);break;
            case 5:
                System.out.println("---欢迎来到修改密码界面---");
                changePassword(user);break;
            case 6:
                System.out.println("---  欢迎来到注销界面  ---");
                writeOff(user);break;
            default:
                System.out.println("已退出当前账号");
                menu();
                break;
        }
    }

    /**
     * 查询个人信息
     */
    public static void inquire(Users user) {
        System.out.print("id:");
        System.out.println(user.getId());
        System.out.print("姓名:");
        System.out.println(user.getName());
        System.out.print("余额:");
        System.out.println(adi.getBalance(conn,user.getId()));
        System.out.print("当日可取余额:");
        balanceToDay = balanceToDay.subtract(balance);
        System.out.println(balanceToDay);
        oss(user);
    }

    /**
     * 存款
     */
    public static void deposit(Users user) {
        System.out.print("请输入存款数额:");
        BigDecimal balance = sc.nextBigDecimal();
        Boolean b = adi.deposit(conn, user.getId(), balance);
        if (b) {
            System.out.println("存款成功");
            oss(user);
        }
    }

    /**
     * 取款
     */
    public static void withdrawal(Users user) {
        System.out.print("请输入取款数额:");
        balance = sc.nextBigDecimal();
        if (balanceToDay.compareTo(balance) == -1) {
            System.out.println("当日可取余额不足,请和银行人员协商或减小取款数目");
            os(user);
        }
        while (user.getBalance().compareTo(balance) == -1) {
            System.out.print("余额不足,请重新输入(当前余额为" +adi.getBalance(conn,user.getId()) + "):");
            conn = JDBCUtil.getConnection();
            balance = sc.nextBigDecimal();
        }
        Boolean b = adi.withdrawal(conn, user.getId(), balance);
        if (b) {
            System.out.println("取款成功");
            oss(user);
        }
    }

    /**
     * 转账
     */
    public static void transfer(Users user) {
        System.out.print("转账用户id:");
        int id = sc.nextInt();
        while (adi.inquireId(conn,id) == 0) {
            conn = JDBCUtil.getConnection();
            System.out.println("id错误,请重新输入id:");
            id = sc.nextInt();
        }
        System.out.print("转账数额:");
        balance = sc.nextBigDecimal();
        conn = JDBCUtil.getConnection();
        while (user.getBalance().compareTo(balance) == -1) {
            conn = JDBCUtil.getConnection();
            System.out.println("余额不足,请重新输入余额:(当前余额为" +user.getBalance() + ")");
            balance = sc.nextBigDecimal();
        }
        adi.transfer(conn,user.getId(),id,balance);
        System.out.println("转账成功");
        oss(user);
    }

    /**
     * 修改密码
     */
    public static void changePassword(Users user) {
        System.out.print("请当前输入密码:");
        int password = sc.nextInt();
        if (password != adi.getPasswordById(conn,user.getId())) {
            System.out.println("当前密码错误");
            System.out.println("----------------");
            conn = JDBCUtil.getConnection();
            changePassword(user);
        }
        System.out.print("请输入新密码:");
        int i1 = sc.nextInt();
        System.out.print("再次输入新密码:");
        int i2 = sc.nextInt();
        if (i1 == i2) {
            conn = JDBCUtil.getConnection();
            Boolean Boolean = adi.changePassword(conn, user, i1);
            if (Boolean) {
                System.out.println("修改成功");
                System.out.println("当前密码不正确,已退出");
                menu();
            }
        } else {
            System.out.println("两次密码不同,请重新输入:");
            changePassword(user);
        }

    }

    /**
     * 注销当前账号
     */
    public static void writeOff(Users user) {
        System.out.print("是否注销该账号:");
        if ("是".equals(sc.next())) {
            Boolean Boolean = adi.writeOff(conn, user);
            if (Boolean == true) {
                System.out.println("注销成功");
                menu();
            }
        }
         else {
            System.out.println("注销失败");
            oss(user);
        }
    }

    /**
     * 循环操作
     * @param user
     */
    public static void oss(Users user) {
        try {
            conn.close();
        } catch (SQLException t) {
            t.printStackTrace();
        }
        System.out.print("按1继续操作(其他键退出当前账号):");
        if (sc.nextInt() == 1) {
            os(user);
        } else {
            JDBCUtil.closeResource(conn,null);
            System.out.println("已退出当前账号");
            menu();
        }
    }
}
