package com.aqat.cidr;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.apache.commons.validator.routines.InetAddressValidator;

@RestController
@RequestMapping(value = "/cidr")
public class IpAddressController {

    public static class JSONBodyData {
        public String ip;
    }
    //Service to abstract db calls
    private IpAddressService ipAddressService = null;
    private static final String ENDPOINT = "/";
    private static final String ENDPOINT_GET_ALL = ENDPOINT + "getAll";
    private static final String ENDPOINT_ADD = ENDPOINT + "add";
    private static final String ENDPOINT_ACQUIRE = ENDPOINT + "acquire";
    private static final String ENDPOINT_RELEASE = ENDPOINT + "release";

    private static final String ALLOW_CORS_URL = "http://localhost:4200";

    public IpAddressController(IpAddressService service)
    {
        ipAddressService = service;
    }

    /**
     *
     * @return array of IpAddressInterface: every entry of IP addresses and its status from DB
     */
    @CrossOrigin(origins = ALLOW_CORS_URL)
    @GetMapping(ENDPOINT_GET_ALL)
    public ResponseEntity<Object> getAllIpAddresses()
    {
        if(ipAddressService!=null) {
            List<IpAddressInterface> ipList = ipAddressService.getAllIpAddresses();
            return new ResponseEntity<>(ipList,HttpStatus.OK);
        }
        return null;
    }

    /**
     * Add the range of ip addresses from CIDR block of notatio x.x.x.x/mask
     * @param data JSONBodyData interface i.e. { "ip":"192.168.1.1/24" }
     * @return String message
     */
    @CrossOrigin(origins = ALLOW_CORS_URL)
    @PostMapping(path = ENDPOINT_ADD,
                 consumes= MediaType.APPLICATION_JSON_VALUE,
                 produces= MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<? extends Object> addIp(@RequestBody JSONBodyData data)
    {
        return routeHandler(ENDPOINT_ADD, data.ip);
    }

    /**
     * Given an ip address, update the status of ip to "acquired"
     * @param data JSONBodyData interface i.e. { "ip":"192.168.1.1/24" }
     * @return
     */
    @CrossOrigin(origins = ALLOW_CORS_URL)
    @PostMapping(path = ENDPOINT_ACQUIRE,
            consumes= MediaType.APPLICATION_JSON_VALUE,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<? extends Object> acquireIp(@RequestBody JSONBodyData data)
    {
        return routeHandler(ENDPOINT_ACQUIRE, data.ip);
    }

    /**
     * Given an ip address, update the status of ip to "acquired"
     * @param data JSONBodyData interface i.e. { "ip":"192.168.1.1/24" }
     * @return
     */
    @CrossOrigin(origins = ALLOW_CORS_URL)
    @PostMapping(path = ENDPOINT_RELEASE,
            consumes= MediaType.APPLICATION_JSON_VALUE,
            produces= MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<? extends Object> releaseIp(@RequestBody JSONBodyData data)
    {
        return routeHandler(ENDPOINT_RELEASE, data.ip);
    }

    /**
     * Helper function to extract ip from query in URL
     * @param queryParameters Key/Value map of all parameters in URL
     * @return Ip param value if it exists
     */
    private String handleIPQueryParam(Map<String, String> queryParameters)
    {
        if(!queryParameters.isEmpty())
        {
            String ip = queryParameters.get("ip");
            if (!ip.isEmpty())
            {
                return ip;
            }
        }
        return null;
    }

    /**
     * Checks if @param ip is of ipv4 notation only. Strips subnetmask if provided
     * @param ip String containing the address
     * @return
     */
    private boolean isValidIp(String ip)
    {
        InetAddressValidator validatorInstance = InetAddressValidator.getInstance();
        if (validatorInstance != null )
        {
            int index = ip.indexOf("/");
            if(index != -1)
            {
                return validatorInstance.isValidInet4Address(ip.substring(0,index));
            }
            else
            {
                return validatorInstance.isValidInet4Address(ip);
            }
        }
        return false;
    }

    /**
     * Handler function that takes an endpoint route and an ip for create and update operations
     * Also validates ip parameter
     * @param route The endpoint
     * @param ip String containing the address
     * @return ResponseEntity for all endpoint results
     */
    private HttpEntity<? extends Object> routeHandler(String route, String ip)
    {
        if(ipAddressService!=null)
        {
            if(ip != null)
            {
                if (isValidIp(ip))
                {
                    switch (route)
                    {
                        case ENDPOINT_ACQUIRE:
                            return new ResponseEntity<>(ipAddressService.acquireIp(ip), HttpStatus.OK);
                        case ENDPOINT_ADD:
                            if(ip.contains("/"))
                                return new ResponseEntity<>(ipAddressService.addIp(ip), HttpStatus.OK);
                            else
                                return new ResponseEntity<>("Error: Ip must contain subnet mask (CIDR): " + ip , HttpStatus.BAD_REQUEST);
                        case ENDPOINT_RELEASE:
                            return new ResponseEntity<>(ipAddressService.releaseIp(ip), HttpStatus.OK);
                        default:
                            return new ResponseEntity<>("Error: Route not found", HttpStatus.NOT_FOUND);
                    }
                }
                else
                {
                    return new ResponseEntity<>("Error: Ip is not valid: " + ip , HttpStatus.BAD_REQUEST);
                }
            }
            else
            {
                return new ResponseEntity<>("Error: IP param missing!", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Please contact administrator.",HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
