package com.slivertrain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slivertrain.domain.StringState;
import com.slivertrain.repository.StringStateRepository;
import com.slivertrain.representation.StringStateRep;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

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
     * @param httpSession
     * @return the current state
     * @throws Exception
     */
    public String getState(HttpSession httpSession) throws Exception {
        try {
            StringState state = getStateFromDB(httpSession);

            logger.info("userId: \"" + state.getUserId() + "\", check the current state.");

            return state.getState();
        } catch (Exception e) {
            throw new Exception("Error occur.");
        }

    }

    /**
     * GET /sum - sums all numbers in a string, e.g. “5abc141def” returns 146, if there are no numbers
     * return 0
     *
     * @param httpSession
     * @return Integer sum result
     * @throws Exception
     */
    public Integer getSum(HttpSession httpSession) throws Exception {
        try {
            Integer sum = 0;
            StringState state = getStateFromDB(httpSession);

            if (StringUtils.isNotBlank(state.getState())) {
                String filtered = state.getState().replaceAll("\\D+", " ");
                String[] numbers = filtered.split("\\s+");

                for (String number : numbers) {
                    if (StringUtils.isNotBlank(number)) {
                        sum += Integer.valueOf(number);
                    }
                }
            }

            logger.info("userId: \"" + state.getUserId() + "\", sum request.");

            return sum;
        } catch (Exception e) {
            throw new Exception("Error occur.");
        }
    }

    /**
     * GET /chars - shows the current state without numbers, e.g. “5abc141def” returns abcdef
     *
     * @param httpSession
     * @return String - the current state without numbers
     * @throws Exception
     */
    public String getChars(HttpSession httpSession) throws Exception {
        try {
            StringState state = getStateFromDB(httpSession);

            String chars = state.getState().replaceAll("\\d+", "");

            logger.info("userId: \"" + state.getUserId() + "\", shows the current state without numbers.");

            return chars;
        } catch (Exception e) {
            throw new Exception("Error occur.");
        }
    }

    /**
     * POST /chars - adds the character/s to the string state
     * e.g. with JSON input {“character”:”a”,”amount”:3} adds “aaa” to the state string
     *
     * @param httpSession
     * @param json
     * @return StringState object
     * @throws Exception
     */
    public StringState addChars(HttpSession httpSession, String json) throws Exception {
        try {
            String tempState = "";

            ObjectMapper om = new ObjectMapper();
            StringStateRep stringStateRep = om.readValue(json, StringStateRep.class);

            String character = stringStateRep.getCharacter();
            Integer amount = stringStateRep.getAmount();

            StringState state = getStateFromDB(httpSession);

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
     * @param httpSession
     * @param character
     * @throws Exception
     */
    public void deleteChars(HttpSession httpSession, String character) throws Exception {
        try {
            if (character.length() > 1 || !character.matches("[A-Za-z0-9]")) {

                logger.error("Character has to be a single alphanumeric character.");

                throw new Exception("Error occur.");
            } else {
                StringState state = getStateFromDB(httpSession);
                String stateString = state.getState();

                int index = stateString.lastIndexOf(character);
                if (index >= 0) {
                    stateString = new StringBuilder(stateString).replace(index, index + 1, "").toString();
                    state.setState(stateString);
                    stringStateRepository.save(state);

                    logger.info("userId: \"" + state.getUserId() + "\", deleted: \"{}\".", character);
                } else {
                    logger.info("userId: \"" + state.getUserId() + "\", there is no character \"{}\" in the current state.", character);
                }

            }
        } catch (Exception e) {
            throw new Exception("Error occur.");
        }
    }

    /**
     * Get the current state for current user.
     *
     * @param httpSession
     * @return StringState object for current user
     */
    private StringState getStateFromDB(HttpSession httpSession) {
        String userId = httpSession.getId();
        StringState state = stringStateRepository.findByUserId(userId);

        if (state == null) {
            state = new StringState(userId, "");
        }

        return state;
    }
}
