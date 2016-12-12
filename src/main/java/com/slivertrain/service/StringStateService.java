package com.slivertrain.service;

import com.slivertrain.domain.StringState;
import com.slivertrain.representation.CharsRep;
import com.slivertrain.representation.SumRep;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;

/**
 * Created by ryan.zhu on 10/12/2016.
 */
public interface StringStateService {

    public StringState getState(String userId) throws Exception;

    public SumRep getSum(String userId) throws Exception;

    public CharsRep getChars(String userId) throws Exception;

    public StringState addChars(String userId, String json) throws Exception;

    public StringState deleteChars(String userId, String character) throws Exception;
}
