package com.laioffer.jupiter.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoriteRequestBody {
    //@JsonProperty
    private final Item favoriteItem;

    @JsonCreator //new request-body object, used on constructor
    public FavoriteRequestBody(@JsonProperty("favorite") Item favoriteItem) {//single direction conversion
        //only convert json string to java object
        this.favoriteItem = favoriteItem; //favorite one item each time
    }

    public Item getFavoriteItem() {
        return favoriteItem;
    }
}
