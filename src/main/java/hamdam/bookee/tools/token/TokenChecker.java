package hamdam.bookee.tools.token;

import hamdam.bookee.tools.exceptions.jwt_token.TokenMissingException;

public class TokenChecker {

    public static void checkHeader(String header, boolean isAccessToken){
        if (header == null || !header.startsWith("Bearer ")) {
            if (isAccessToken) throw new MissingTokenException("Access token is missing!");
            throw new MissingTokenException("Refresh token is missing!");
        }
    }
}
