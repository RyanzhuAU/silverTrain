package com.slivertrain.representation;

import lombok.Data;

/**
 * Created by ryan.zhu on 9/12/2016.
 */
@Data
public class StringStateRep {

    private String character;

    private Integer amount;

    public StringStateRep() {

    }

    public StringStateRep(String character, Integer amount) {
        this.character = character;
        this.amount = amount;
    }
}
