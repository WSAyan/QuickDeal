package com.example.limit.quickdealfinal.Activity.Pojo;


public class GridRowItem
{

    String image;
    String name;
    String price;


    public GridRowItem(String image,String name,String price)
    {
        this.image = image;
        this.name = name;
        this.price = price;
    }




    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

}
