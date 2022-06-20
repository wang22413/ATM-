package JDBC;

import java.math.BigDecimal;

public class Users {
    private int id;
    private String name;
    private int password;
    private BigDecimal balance;

    public Users() {
    }

    public Users(String name, int password) {
        this.name = name;
        this.password = password;
    }

    public Users(int id, int password) {
        this.id = id;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }
}