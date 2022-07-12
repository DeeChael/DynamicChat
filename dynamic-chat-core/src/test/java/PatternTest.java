import net.deechael.dynamichat.util.StringUtils;
import net.deechael.useless.objs.DuObj;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTest {

    public static void main(String[] args) {
        String string = "Aaaaaa{1}sdasdsada{4}asdasdasd{5}w";
        Pattern pattern = Pattern.compile("\\{\\d*}");
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
        System.out.println(Arrays.toString(string.split("\\{\\d*}")));
        DuObj<String[], String[]> split = StringUtils.split(string, pattern);
        System.out.println(Arrays.toString(split.getFirst()));
        System.out.println(Arrays.toString(split.getSecond()));
    }

}
