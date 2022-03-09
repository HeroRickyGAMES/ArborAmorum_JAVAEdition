package com.herorickystudios.lovecutey.data;

import com.herorickystudios.lovecutey.data.model.LoggedInUser;

import java.io.IOException;

/**
 //Programado por HeroRickyGames
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "User Name");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Erro ao logar!", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}