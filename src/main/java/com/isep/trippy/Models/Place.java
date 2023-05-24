package com.isep.trippy.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    public String name;
    public int rate;
    public String kinds;
    public double dist;
    public String osm;
    public String wikidata;
    public String xid;

}
