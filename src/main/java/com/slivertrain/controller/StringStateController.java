package com.slivertrain.controller;

import com.slivertrain.domain.StringState;
import com.slivertrain.service.StringStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
    public ResponseEntity<String> getState(HttpSession httpSession) {
        try {
            String state = stringStateService.getState(httpSession);

            return new ResponseEntity<String>(state, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET /sum - sums all numbers in a string, e.g. “5abc141def” returns 146, if there are no numbers
     * return 0
     *
     * @param httpSession
     * @return sum result (integer) and status 200, or status 400
     */
    @RequestMapping(value = "/sum", method = RequestMethod.GET)
    public ResponseEntity<Integer> getSum(HttpSession httpSession) {
        try {
            Integer sum = stringStateService.getSum(httpSession);

            return new ResponseEntity<Integer>(sum, HttpStatus.OK);
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
    public ResponseEntity<String> getChars(HttpSession httpSession) {
        try {
            String chars = stringStateService.getChars(httpSession);

            return new ResponseEntity<String>(chars, HttpStatus.OK);
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
            StringState stringState = stringStateService.addChars(httpSession, json);

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
    public ResponseEntity deleteChar(@PathVariable("character") String character, HttpSession httpSession) {
        try {
            stringStateService.deleteChars(httpSession, character);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
