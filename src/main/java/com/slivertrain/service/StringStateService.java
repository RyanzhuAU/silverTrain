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

    StringState getState(String userId) throws Exception;

    SumRep getSum(String userId) throws Exception;

    CharsRep getChars(String userId) throws Exception;

    StringState addChars(String userId, String json) throws Exception;

    StringState deleteChars(String userId, String character) throws Exception;
}
