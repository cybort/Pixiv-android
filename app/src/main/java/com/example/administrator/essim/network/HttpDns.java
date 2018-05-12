package com.example.administrator.essim.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Dns;

public class HttpDns implements Dns {
    private String[] addresses = {"210.129.120.41", "210.129.120.43", "210.140.92.140"};

    public List<InetAddress> lookup(String paramString)
            throws UnknownHostException {
        try {
            ArrayList localArrayList = new ArrayList();
            String[] arrayOfString = this.addresses;
            int j = 0;
            int i = j;
            if (arrayOfString != null) {
                i = this.addresses.length;
                i = j;
            }
            while (i < this.addresses.length) {
                localArrayList.add(InetAddress.getByName(this.addresses[i]));
                i += 1;
            }
            return localArrayList;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return Dns.SYSTEM.lookup(paramString);
    }
}