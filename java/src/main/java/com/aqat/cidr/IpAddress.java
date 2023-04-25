package com.aqat.cidr;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ip_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IpAddress {

    public static final String TABLE_NAME = "ip_addresses";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_IP = "ip";
    public static final String COLUMN_ID = "id";

    public enum STATUS_VALUE {
            available, acquired
    }

    //Set some defaults
    private static final STATUS_VALUE DEFAULT_STATUS_VALUE = STATUS_VALUE.available;
    private static final String DEFAULT_IP_VALUE = "0.0.0.0";

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = COLUMN_ID, nullable = false, unique = true)
    private Long id;

    @Column(name = COLUMN_IP, nullable = false, unique = true)
    private String ip = DEFAULT_IP_VALUE;

    @Column(name = COLUMN_STATUS, nullable = false)
    @Enumerated(EnumType.STRING)
    private STATUS_VALUE status = DEFAULT_STATUS_VALUE;

    public IpAddress(String ip, STATUS_VALUE status)
    {
        this.ip = ip;
        this.status = status;
    }
    public IpAddress(String ip)
    {
        this.ip = ip;
    }

    public IpAddress(Long id) {
        this.id = id;
    }

    public void setStatus(STATUS_VALUE status) {
        this.status = status;
    }
}