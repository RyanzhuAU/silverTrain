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

/**
 * Created by ryan.zhu on 11/12/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StringStateServiceTest {
    @Autowired
    private StringStateRepository stringStateRepository;

    @Autowired
    private MockHttpSession mockSession;

    @Autowired
    private WebApplicationContext context;

    private StringStateService stringStateService;

    @Before
    public void setup() throws Exception {
        stringStateService = new StringStateServiceImpl(stringStateRepository);
        mockSession = new MockHttpSession(context.getServletContext(), "test1");

        StringState stringState = new StringState("test1", "123abc12");
        stringStateRepository.save(stringState);
    }

    @Test
    public void testGetState() throws Exception {
        String state = stringStateService.getState(mockSession);

        Assert.assertEquals(state, "123abc12");
    }

    @Test
    public void testGetSum() throws Exception {
        Integer sum = stringStateService.getSum(mockSession);

        Assert.assertEquals(sum, Integer.valueOf(135));
    }

    @Test
    public void testGetChars() throws Exception {
        String chars = stringStateService.getChars(mockSession);

        Assert.assertEquals(chars, "abc");
    }

    @Test
    public void testAddChars() throws Exception {
        StringState stringState = stringStateService.addChars(mockSession, "{\"character\":\"a\", \"amount\":3}");

        Assert.assertEquals(stringState.getUserId(), "test1");

        String state = stringStateRepository.findByUserId("test1").getState();
        Assert.assertEquals(state, "123abc12aaa");
    }

    @Test
    public void testDeleteChars() throws Exception {
        stringStateService.deleteChars(mockSession, "1");

        String state = stringStateRepository.findByUserId("test1").getState();
        Assert.assertEquals(state, "123abc2");
    }
}
