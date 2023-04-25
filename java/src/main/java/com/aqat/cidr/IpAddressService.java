package com.aqat.cidr;

import org.apache.commons.net.util.SubnetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class IpAddressService {
    @Autowired
    private IpAddressAPRepo ipRepo;

    public List<IpAddressInterface> getAllIpAddresses()
    {
        return ipRepo.getAll();
    }

    public String addIp(String ipAddress)
    {
        SubnetUtils netUtils = new SubnetUtils(ipAddress);
        String[] ips = netUtils.getInfo().getAllAddresses();
        Arrays.stream(ips).forEach(ip -> {
            IpAddress newIpAddress = new IpAddress(ip);
            ipRepo.save(newIpAddress);
        });
        return "IPs in the range of " + ipAddress + " were successfully added!";
    }

    public String acquireIp(String ip)
    {
        return updateIpStatus(ip, IpAddress.STATUS_VALUE.acquired);
    }

    public String releaseIp(String ip)
    {
        return updateIpStatus(ip, IpAddress.STATUS_VALUE.available);
    }
    private String updateIpStatus(String ip, IpAddress.STATUS_VALUE status)
    {
        IpAddress ipAddress = ipRepo.findByIp(ip);
        if(ipAddress != null)
        {
            ipAddress.setStatus(status);
            ipAddress = ipRepo.save(ipAddress);
            return "Ip " + ip + " is now " + ipAddress.getStatus().toString();
        }
        return "Ip " + ip + " not found!";
    }
}
