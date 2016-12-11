package com.slivertrain.representation;

import lombok.Data;

/**
 * Created by ryan.zhu on 9/12/2016.
 */
@Data
public class StringStateRepTest {

    private String character;

    private Integer amount;

    private String invalidColumn;

    public StringStateRepTest() {

    }

    public StringStateRepTest(String character, Integer amount, String invalidColumn) {
        this.character = character;
        this.amount = amount;
        this.invalidColumn = invalidColumn;
    }
}
