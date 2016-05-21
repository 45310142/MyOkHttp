package com.mymap.huza.myokhttp;

/**
 * Created by Huza on 2016/4/19.
 */
public class Person {

    private String name;
    private int age;
    private String phone;
    private String image_name;

    public Person(String name, int age, String phone) {
        this.name = name;
        this.age = age;
        this.phone = phone;
    }

    public Person(String name, int age, String image_name, String phone) {
        this.name = name;
        this.age = age;
        this.image_name = image_name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
