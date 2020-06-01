package com.deeaboi.smartlib.Model;

public class ProductsBooks
{
  private String pname,Author,image,pid,date,time;
  public ProductsBooks()
  {

  }

    public ProductsBooks(String pname, String author, String image, String pid, String date, String time)
    {
        this.pname = pname;
        Author = author;
        this.image = image;
        this.pid = pid;
        this.date = date;
        this.time = time;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {

      Author = author; //this. is added
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
