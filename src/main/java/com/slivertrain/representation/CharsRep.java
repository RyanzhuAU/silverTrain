package com.slivertrain.representation;

import lombok.Data;

/**
 * Created by ryan.zhu on 12/12/2016.
 */

@Data
public class CharsRep {

    private String userId;

    private String chars;

    public CharsRep(String chars, String userId) {
        this.chars = chars;
        this.userId = userId;
    }
}
