package org.example;

import javax.persistence.*;
import java.io.Serializable;
//
//@Entity
//@AttributeOverrides({
//        @AttributeOverride(name="id", column = @Column(name="ID"))
//})
//@Table(name="Utilizatori")
//
//public class Utilizator extends EntitateHibernate  implements Serializable {
//
//
//    private String nume;
//    private String username;
//    private String password;
//
//    public Utilizator(String nume, String username, String password) {
//        this.nume = nume;
//        this.username = username;
//        this.password = password;
//    }
//
//    public Utilizator() {
//    }
//
//
//    @Column(name="NUME")
//
//    public String getNume() {
//        return nume;
//    }
//
//    public void setNume(String nume) {
//        this.nume = nume;
//    }
//
//    @Column(name="USERNAME")
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//    @Column(name="PASSWORD")
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    @Override
//    public String toString() {
//        return "Utilizator{" +
//                "nume='" + nume + '\'' +
//                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                '}';
//    }
//}

public class Utilizator extends Entitate<Integer>  implements Serializable {
    private String nume;
    private String username;
    private String password;

    public Utilizator(String nume, String username, String password) {
        this.nume = nume;
        this.username = username;
        this.password = password;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "nume='" + nume + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}