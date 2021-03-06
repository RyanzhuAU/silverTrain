package com.slivertrain.controller;

import com.slivertrain.domain.StringState;
import com.slivertrain.representation.CharsRep;
import com.slivertrain.representation.SumRep;
import com.slivertrain.service.StringStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;

/**
 * Created by ryan.zhu on 9/12/2016.
 */

@RestController
public class StringStateController {

    @Autowired
    private StringStateService stringStateService;

    /**
     * GET /state - returns the current state
     *
     * @param httpSession
     * @return state string and status 200, or status 400
     */
    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public ResponseEntity<StringState> getState(HttpSession httpSession) {
        try {
            String userId = httpSession.getId();
            StringState state = stringStateService.getState(userId);

            return new ResponseEntity<StringState>(state, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET /sum - sums all numbers in a string, e.g. “5abc141def” returns 146, if there are no numbers
     * return 0
     *
     * @param httpSession
     * @return sum result and status 200, or status 400
     */
    @RequestMapping(value = "/sum", method = RequestMethod.GET)
    public ResponseEntity<SumRep> getSum(HttpSession httpSession) {
        try {
            String userId = httpSession.getId();
            SumRep sumRep = stringStateService.getSum(userId);

            return new ResponseEntity<SumRep>(sumRep, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET /chars - shows the current state without numbers, e.g. “5abc141def” returns abcdef
     *
     * @param httpSession
     * @return the current state without numbers and status 200, or status 400
     */
    @RequestMapping(value = "/chars", method = RequestMethod.GET)
    public ResponseEntity<CharsRep> getChars(HttpSession httpSession) {
        try {
            String userId = httpSession.getId();
            CharsRep chars = stringStateService.getChars(userId);

            return new ResponseEntity<CharsRep>(chars, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST /chars - adds the character/s to the string state
     * e.g. with JSON input {“character”:”a”,”amount”:3} adds “aaa” to the state string
     *
     * @param json
     * @param httpSession
     * @return JSON - StringState object and status 200, or status 400
     */
    @RequestMapping(value = "/chars", method = RequestMethod.POST)
    public ResponseEntity<Object> setChars(@RequestBody String json, HttpSession httpSession) {
        try {
            String userId = httpSession.getId();
            StringState stringState = stringStateService.addChars(userId, json);

            return new ResponseEntity<Object>(stringState, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * DELETE /chars/<character> - deletes the last occurrence of the character in the state string
     *
     * @param character
     * @param httpSession
     * @return status 200 or status 400
     */
    @RequestMapping(value = "/chars/{character}", method = RequestMethod.DELETE)
    public ResponseEntity<StringState> deleteChar(@PathVariable("character") String character, HttpSession httpSession) {
        try {
            String userId = httpSession.getId();
            StringState state = stringStateService.deleteChars(userId, character);

            return new ResponseEntity<StringState>(state, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
