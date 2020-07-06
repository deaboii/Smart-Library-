package com.deeaboi.smartlib.Model;

public class Admin
{
    private String Name,Address,Key,Password,Phone,Pin,Tkey;


    public Admin()
    {



    }


    public Admin(String name, String address, String key, String password, String phone, String pin, String tkey)
    {
        Name = name;
        Address = address;
        Key = key;
        Password = password;
        Phone = phone;
        Pin = pin;
        Tkey = tkey;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public String getTkey() {
        return Tkey;
    }

    public void setTkey(String tkey) {
        Tkey = tkey;
    }
}
