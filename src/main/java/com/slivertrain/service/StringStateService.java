package com.slivertrain.service;

import com.slivertrain.domain.StringState;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;

/**
 * Created by ryan.zhu on 10/12/2016.
 */
public interface StringStateService {

    public String getState(HttpSession httpSession) throws Exception;

    public BigInteger getSum(HttpSession httpSession) throws Exception;

    public String getChars(HttpSession httpSession) throws Exception;

    public StringState addChars(HttpSession httpSession, String json) throws Exception;

    public void deleteChars(HttpSession httpSession, String character) throws Exception;
}
