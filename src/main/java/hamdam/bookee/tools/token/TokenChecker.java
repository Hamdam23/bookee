package hamdam.bookee.tools.token;

import hamdam.bookee.tools.exeptions.jwtToken.RefreshTokenMissingException;

public class TokenChecker {

    public static void checkHeader(String header){
        if (header == null || !header.startsWith("Bearer ")) {
            // TODO: 9/2/22 why do you need to write error message there?
            throw new RefreshTokenMissingException("Refresh token is missing!");
        }
    }
}
