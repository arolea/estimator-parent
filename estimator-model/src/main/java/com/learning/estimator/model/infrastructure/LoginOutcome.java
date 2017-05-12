package com.learning.estimator.model.infrastructure;

import com.learning.estimator.model.entities.User;

import java.io.Serializable;

/**
 * Abstracts the outcome of a login operation
 *
 * @author rolea
 */
@SuppressWarnings("serial")
public class LoginOutcome implements Serializable {

    private User user;
    private Outcome outcome;
    private String message;

    @SuppressWarnings("unused")
    private LoginOutcome() {
    }

    public LoginOutcome(Outcome outcome, String message) {
        this.outcome = outcome;
        this.message = message;
    }

    public LoginOutcome(Outcome outcome, User user, String message) {
        this.outcome = outcome;
        this.user = user;
        this.message = message;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((outcome == null) ? 0 : outcome.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LoginOutcome other = (LoginOutcome) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (outcome != other.outcome)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LoginOutcome [outcome=" + outcome + ", message=" + message + "]";
    }

}
