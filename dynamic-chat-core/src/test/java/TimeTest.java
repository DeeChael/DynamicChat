import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeTest {

    public static void main(String[] args) {
        String timeString = "1y2wk3d4min20s";
        String pattern = "(\\d)*(y|m|wk|d|h|min|s)";
        Matcher matcher = Pattern.compile(pattern).matcher(timeString);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
        /*
         * Output:
         *
         * 1y
         * 2wk
         * 3d
         * 4m
         * 20s
         */
    }

}
