package com.aqat.cidr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpAddressAPRepo extends JpaRepository<IpAddress, Long> {
    IpAddress findByIp(String ip);

    String QUERY_GET_ALL = "select " + IpAddress.COLUMN_IP
                            +", "+IpAddress.COLUMN_STATUS + " from "+IpAddress.TABLE_NAME;
    @Query(value = QUERY_GET_ALL, nativeQuery = true)
    List<IpAddressInterface> getAll();
}