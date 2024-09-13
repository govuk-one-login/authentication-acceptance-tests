package uk.gov.di.test.step_definitions;

import uk.gov.di.test.entity.UserCredentials;
import uk.gov.di.test.entity.UserInterventions;
import uk.gov.di.test.entity.UserProfile;

public class World {
    public UserProfile userProfile;
    public UserCredentials userCredentials;
    public UserInterventions userInterventions;

    public String getUserEmailAddress() {
        throwIfUserProfileDoesNotExist();
        return userProfile.getEmail();
    }

    public String getUserPassword() {
        throwIfUserProfileDoesNotExist();
        return userProfile.getPassword();
    }

    public static class WorldException extends RuntimeException {
        public WorldException() {}

        public WorldException(String message) {
            super(message);
        }
    }

    public World throwIfUserProfileExists() throws WorldException {
        if (userProfile != null) {
            throw new WorldException("User profile already exists");
        }
        return this;
    }

    public World throwIfUserProfileDoesNotExist() throws WorldException {
        if (userProfile == null) {
            throw new WorldException("User profile does not exist");
        }
        return this;
    }

    public World throwIfUserCredentialsExists() throws WorldException {
        if (userCredentials != null) {
            throw new WorldException("User credentials already exists");
        }
        return this;
    }

    public World throwIfUserCredentialsDoesNotExist() throws WorldException {
        if (userCredentials == null) {
            throw new WorldException("User credentials do not exist");
        }
        return this;
    }

    public World throwIfUserInterventionsExists() throws WorldException {
        if (userInterventions != null) {
            throw new WorldException("User interventions already exists");
        }
        return this;
    }

    public World throwIfUserInterventionsDoesNotExist() throws WorldException {
        if (userInterventions == null) {
            throw new WorldException("User interventions do not exist");
        }
        return this;
    }
}
