package com.slivertrain.repository;

import com.slivertrain.domain.StringState;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ryan.zhu on 10/12/2016.
 */

public interface StringStateRepository extends CrudRepository<StringState, String> {

    StringState findByUserId(String userId);

}
