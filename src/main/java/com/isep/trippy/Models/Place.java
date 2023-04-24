package com.isep.trippy.Models;

import lombok.Data;

@Data

public class Place {

    public String name;
    public int rate;
    public String kinds;
    public double dist;
    public String osm;
    public String wikidata;
    public String xid;

}
