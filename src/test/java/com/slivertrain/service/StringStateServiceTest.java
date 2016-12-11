package com.slivertrain.service;

import com.slivertrain.domain.StringState;
import com.slivertrain.repository.StringStateRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;

/**
 * Created by ryan.zhu on 11/12/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StringStateServiceTest {
    @Autowired
    private StringStateRepository stringStateRepository;

    @Autowired
    private WebApplicationContext context;

    private StringStateService stringStateService;
    private String userId;

    @Before
    public void setup() throws Exception {
        stringStateService = new StringStateServiceImpl(stringStateRepository);
        userId = "test1";

        StringState stringState = new StringState("test1", "123abc12");
        stringStateRepository.save(stringState);
    }

    @Test
    public void testGetState() throws Exception {
        String state = stringStateService.getState(userId);

        Assert.assertEquals(state, "123abc12");
    }

    @Test
    public void testGetSum() throws Exception {
        BigInteger sum = stringStateService.getSum(userId);

        Assert.assertEquals(sum, BigInteger.valueOf(135));
    }

    @Test
    public void testGetChars() throws Exception {
        String chars = stringStateService.getChars(userId);

        Assert.assertEquals(chars, "abc");
    }

    @Test
    public void testAddChars() throws Exception {
        StringState stringState = stringStateService.addChars(userId, "{\"character\":\"a\", \"amount\":3}");

        Assert.assertEquals(stringState.getUserId(), "test1");

        String state = stringStateRepository.findByUserId("test1").getState();
        Assert.assertEquals(state, "123abc12aaa");
    }

    @Test
    public void testDeleteChars() throws Exception {
        stringStateService.deleteChars(userId, "1");

        String state = stringStateRepository.findByUserId("test1").getState();
        Assert.assertEquals(state, "123abc2");
    }
}
