package hamdam.bookee.APIs.role;

// A list of permissions that a user can have.
public enum Permissions {

    GET_BOOK,
    CREATE_BOOK,
    UPDATE_BOOK,
    //all above 3 should be replaced with MONITOR_BOOK
    DELETE_BOOK,

    GET_GENRE,
    CREATE_GENRE,
    UPDATE_GENRE,
    DELETE_GENRE,

    MONITOR_ROLE,

    GET_USER, //himself/herself
    UPDATE_USER, //himself/herself
    DELETE_USER, //himself/herself
    MONITOR_USER, //anyone

    GET_ROLE_REQUEST,
    CREATE_ROLE_REQUEST,
    UPDATE_ROLE_REQUEST,
    DELETE_ROLE_REQUEST,
    MONITOR_ROLE_REQUEST,
}
