package hamdam.bookee.tools.constants;

public class DeletionMessage {

    public static String getDeletionMessage(String resource, Long id) {
        return String.format("%s with id: %s successfully deleted!", resource, id);
    }
}
