package pl.cezaryregec.rest;

import java.util.Map;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public interface RestBuilder {
    
    Map<String, String> buildParams(String... packages);
}
