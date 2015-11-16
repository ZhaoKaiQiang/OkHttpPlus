package com.socks.sample.okhttp.model;

public class User {

    public String name;
    public int age;
    public String blog;

    public User() {
    }

    public User(String name, int age, String blog) {
        this.name = name;
        this.age = age;
        this.blog = blog;
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

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", blog='" + blog + '\'' +
                '}';
    }
}
