package com.slivertrain.service;

import com.slivertrain.domain.StringState;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;

/**
 * Created by ryan.zhu on 10/12/2016.
 */
public interface StringStateService {

    public String getState(String userId) throws Exception;

    public BigInteger getSum(String userId) throws Exception;

    public String getChars(String userId) throws Exception;

    public StringState addChars(String userId, String json) throws Exception;

    public void deleteChars(String userId, String character) throws Exception;
}
