package pl.cezaryregec.rest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author SudoWaster <cezaryre@gmail.com>
 */
public class RestBuilderImpl implements RestBuilder {
    
    public RestBuilderImpl() {
        
    }

    private boolean packageExists(String pkg) {
        boolean exists = false;
        
        String resourcePath = pkg.replace(".", "/");
        URL resource = getClass().getClassLoader().getResource(resourcePath);
        exists = (resource != null);
        
        return exists;
    }
    
    @Override
    public Map<String, String> buildParams(String... packages) {
        String p = new String();

        for(String pkg : packages) {
            if(packageExists(pkg)) {
            
                if(p.length() > 0) {
                    p += ",";
                }

                p += pkg;
            }
        }

        Map<String, String> params = new HashMap<>();
        if(p.length() > 0) {
            params.put("jersey.config.server.provider.packages", p);
        }
        
        return params;
    }
    
}
