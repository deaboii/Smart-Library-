package com.deeaboi.smartlib.Model;

public class Users
{
   private String name,password,phone,image,roll,key,fine;
   // private String name,password,phone,image,roll,;

   public Users()
   {



   }

    public Users(String name, String password, String phone, String image, String roll, String key, String fine) {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.image = image;
        this.roll = roll;
        this.key = key;
        this.fine = fine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFine() {
        return fine;
    }

    public void setFine(String fine) {
        this.fine = fine;
    }
//    public Users(String name, String password, String phone, String image, String roll)
//    {
//        this.name = name;
//        this.password = password;
//        this.phone = phone;
//        this.image = image;
//        this.roll = roll;
//    }
//
//    public String getName()
//    {
//        return name;
//    }
//
//    public void setName(String name)
//    {
//        this.name = name;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password)
//    {
//        this.password = password;
//    }
//
//    public String getPhone()
//    {
//        return phone;
//    }
//
//    public void setPhone(String phone)
//    {
//        this.phone = phone;
//    }
//
//    public String getImage()
//    {
//        return image;
//    }
//
//    public void setImage(String image)
//    {
//        this.image = image;
//    }
//
//    public String getRoll()
//    {
//        return roll;
//    }
//
//    public void setRoll(String roll)
//    {
//        this.roll = roll;
//    }
}
