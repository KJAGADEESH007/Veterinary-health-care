package com.example.vetcare;

public class Categories
{
    private String name, image, pid;

    public Categories()
    {

    }

    public Categories(String name, String image, String pid) {
        this.name = name;
        this.image = image;
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
