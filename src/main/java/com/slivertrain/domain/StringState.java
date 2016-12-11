package com.slivertrain.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by ryan.zhu on 9/12/2016.
 */
@Data
@Entity
@Table(name = "string_state")
public class StringState {
    @Id
    private String userId;

    private String state;

    public StringState() {

    }

    public StringState(String userId, String state) {
        this.userId = userId;
        this.state = state;
    }
}
