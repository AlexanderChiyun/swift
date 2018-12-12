package com.certus.swifttest;

import java.io.File;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;

public class App
{
    public static void main( String[] args )
    {
        String username = "testuser:swift";
        String password = "6WOxew6EdFx4gCNF2XqivyUqaCMs2FWLItDXnklz";
        String authUrl  = "http://:7480/auth";
        String container = "testuser-b";
        String large_file = "D:\\CentOS-7-x86_64-DVD-1708.iso";

        AccountConfig config = new AccountConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setAuthUrl(authUrl);
        config.setAuthenticationMethod(AuthenticationMethod.BASIC);
        Account account = new AccountFactory(config).createAccount();
        
        Container bucket = account.getContainer(container);
        //bucket.create();
        
        StoredObject object = bucket.getObject("1708.iso");
        object.uploadObject(new File(large_file));
    }
}
