package com.slivertrain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slivertrain.domain.StringState;
import com.slivertrain.repository.StringStateRepository;
import com.slivertrain.representation.CharsRep;
import com.slivertrain.representation.StringStateRep;
import com.slivertrain.representation.SumRep;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;

/**
 * Created by ryan.zhu on 11/12/2016.
 */
@Service
public class StringStateServiceImpl implements StringStateService {
    @Autowired
    private StringStateRepository stringStateRepository;

    public StringStateServiceImpl (StringStateRepository stringStateRepository) {
        this.stringStateRepository = stringStateRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * GET /state - returns the current state
     *
     * @param userId
     * @return the current state
     * @throws Exception
     */
    public StringState getState(String userId) throws Exception {
        try {
            StringState state = getStateFromDB(userId);

            logger.info("userId: \"" + state.getUserId() + "\", check the current state.");

            return state;
        } catch (Exception e) {
            throw new Exception("Error occur.");
        }

    }

    /**
     * GET /sum - sums all numbers in a string, e.g. “5abc141def” returns 146, if there are no numbers
     * return 0
     *
     * @param userId
     * @return SumRep which include the sum result and the userId
     * @throws Exception
     */
    public SumRep getSum(String userId) throws Exception {
        try {
            BigInteger sum = BigInteger.valueOf(0);
            StringState state = getStateFromDB(userId);

            if (StringUtils.isNotBlank(state.getState())) {
//                String filtered = state.getState().replaceAll("\\D+", " ");
                String[] numbers = state.getState().split("\\D+");

                for (String number : numbers) {
                    if (StringUtils.isNotBlank(number)) {
                        sum = sum.add(new BigInteger(number));
                    }
                }
            }

            SumRep sumRep = new SumRep(sum, state.getUserId());

            logger.info("userId: \"" + state.getUserId() + "\", sum request.");

            return sumRep;

        } catch (Exception e) {
            throw new Exception("Error occur.");
        }
    }

    /**
     * GET /chars - shows the current state without numbers, e.g. “5abc141def” returns abcdef
     *
     * @param userId
     * @return CharsRep which include the sum result and the userId
     * @throws Exception
     */
    public CharsRep getChars(String userId) throws Exception {
        try {
            StringState state = getStateFromDB(userId);

            String chars = state.getState().replaceAll("\\d+", "");

            CharsRep charsRep = new CharsRep(chars, state.getUserId());

            logger.info("userId: \"" + state.getUserId() + "\", shows the current state without numbers.");

            return charsRep;

        } catch (Exception e) {
            throw new Exception("Error occur.");
        }
    }

    /**
     * POST /chars - adds the character/s to the string state
     * e.g. with JSON input {“character”:”a”,”amount”:3} adds “aaa” to the state string
     *
     * @param userId
     * @param json
     * @return StringState object
     * @throws Exception
     */
    public StringState addChars(String userId, String json) throws Exception {
        try {
            String tempState = "";

            ObjectMapper om = new ObjectMapper();
            StringStateRep stringStateRep = om.readValue(json, StringStateRep.class);

            String character = stringStateRep.getCharacter();
            Integer amount = stringStateRep.getAmount();

            StringState state = getStateFromDB(userId);

            if (character.length() > 1 || !character.matches("[A-Za-z1-9]")) {
                throw new Exception("Error occur.");
            } else {
                //handle the character or amount nullable scenario.
                if (character != null && amount != null) {
                    tempState = state.getState() + new String(new char[amount]).replace("\0", character);
                }
            }

            if (tempState.length() > 200) {
                logger.error("userId: \"" + state.getUserId() + "\", Character request has to be just one alphanumeric character and amount a number from 1 to 9.");

                throw new Exception("Error occur.");
            } else {
                logger.info("userId: \"" + state.getUserId() + "\", added: \"{}\", {} times", character, amount);
                state.setState(tempState);
                stringStateRepository.save(state);

                return state;
            }
        } catch (Exception e) {
            throw new Exception("Error occur.");
        }
    }

    /**
     * DELETE /chars/<character> - deletes the last occurrence of the character in the state string
     *
     * @param userId
     * @param character
     * @return StringState after delete action
     * @throws Exception
     */
    public StringState deleteChars(String userId, String character) throws Exception {
        try {
            if (character.length() > 1 || !character.matches("[A-Za-z0-9]")) {

                logger.error("Character has to be a single alphanumeric character.");

                throw new Exception("Error occur.");
            } else {
                StringState state = getStateFromDB(userId);
                String stateString = state.getState();

                int index = stateString.lastIndexOf(character);
                if (index >= 0) {
                    stateString = new StringBuilder(stateString).replace(index, index + 1, "").toString();
                    state.setState(stateString);
                    state = stringStateRepository.save(state);

                    logger.info("userId: \"" + state.getUserId() + "\", deleted: \"{}\".", character);
                } else {
                    logger.info("userId: \"" + state.getUserId() + "\", there is no character \"{}\" in the current state.", character);
                }

                return state;

            }
        } catch (Exception e) {
            throw new Exception("Error occur.");
        }
    }

    /**
     * Get the current state for current user.
     *
     * @param userId
     * @return StringState object for current user
     */
    private StringState getStateFromDB(String userId) {
        StringState state = stringStateRepository.findByUserId(userId);

        if (state == null) {
            state = new StringState(userId, "");
        }

        return state;
    }
}
