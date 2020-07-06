package com.deeaboi.smartlib.Model;

public class Teacher
{
    private String name,password,phone,key;
    public Teacher()
    {





    }

    public Teacher(String name, String password, String phone, String key)
    {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password
    ) {
        this.password = password;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
