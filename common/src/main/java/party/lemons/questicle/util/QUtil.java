package party.lemons.questicle.util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * Every project has a random utils class
 */
public class QUtil
{
    public static String titleCase(final String words) {
        return Stream.of(words.trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
