package hamdam.bookee.tools.token;

import hamdam.bookee.tools.exceptions.jwt_token.RefreshTokenMissingException;

public class TokenChecker {

    public static void checkHeader(String header){
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RefreshTokenMissingException();
        }
    }
}
