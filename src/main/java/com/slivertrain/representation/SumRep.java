package com.slivertrain.representation;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by ryan.zhu on 12/12/2016.
 */

@Data
public class SumRep {
    private String userId;

    private BigInteger sum;

    public SumRep(BigInteger sum, String userId) {
        this.sum = sum;
        this.userId = userId;
    }
}
