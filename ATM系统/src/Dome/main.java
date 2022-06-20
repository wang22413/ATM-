package Dome;

import java.util.Scanner;

public class main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    /**
     * 主菜单
     */
    public static void menu() {
        System.out.println("----------------");
        System.out.println("  1.登录  2.注册  ");
        System.out.println("----------------");
        menu1(sc.nextInt());
    }

    /**
     * 判断登录和注册
     * @param i
     */
    public static void menu1(int i) {
        if (i == 1) {
            RingUp.ringUp();
        } else if (i == 2) {
            SignIn.signIn();
        } else {
            System.out.println("输入错误,请重新输入");
            menu();
        }
    }

}
