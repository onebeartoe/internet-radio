
package com.onebeartoe.os.shell;

import java.io.IOException;
import java.util.List;

/**
 * @author rmarquez
 */
public interface CommandLine 
{
    List<String> execute(String ... command) throws IOException;
}
