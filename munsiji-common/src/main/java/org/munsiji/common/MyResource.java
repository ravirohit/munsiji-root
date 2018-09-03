package org.munsiji.common;

/**
 * Root resource (exposed at "myresource" path)
 */

public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
  
    public String getIt() {
        return "Got it!";
    }
}
