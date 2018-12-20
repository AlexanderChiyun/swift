package com.certus.swifttest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import com.csvreader.CsvWriter;

public class App
{
    /*public void save(String csv, String[] content)
    {
        try
        {
            CsvWriter csvWriter = new CsvWriter(csv, ',', Charset.forName("GBK"));
            csvWriter.writeRecord(content);
            csvWriter.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void upload(String path, Container bucket, String csv)
    {
        String prefix= "/tmp/photos";
        //遍历path下的所有文件
        File[] files = new File(path).listFiles();
        for (File f:files)
        {            
            //是目录则递归
            if(f.isDirectory())
            {
                upload(f.getAbsolutePath(), bucket, csv);
            }
            //不是目录则改名上传
            else
            {
                try
                {
                String abs_path = f.getAbsolutePath();
                String upload_path = abs_path.substring(prefix.length() + 1);                
                StoredObject object = bucket.getObject(abs_path);
                object.uploadObject(new File(upload_path));
                String publicURL = object.getPublicURL();
                String[] content = {abs_path, publicURL};
                //文件名写入csv
                save(csv, content);
                return;
                }catch (Exception e) {
                    e.printStackTrace();
                }              
            }
        }
    }*/
    
    public static void main( String[] args )
    {
        String username = "chaoaipass:swift";
        String password = "MiVZ3UFF8tuwAgCet92rCm2vhRgKth20iTRqxSGN";
        String authUrl  = "https://oss.juneyaopaas.com:7480/auth";
        
        //String username = "testuser:swift";
        //String password = "6WOxew6EdFx4gCNF2XqivyUqaCMs2FWLItDXnklz";
        //String authUrl  = "http://172.16.166.244:7480/auth";
        
        String container = "history";
        String path= "/tmp/photos";
        String configPath = "./uploader.cfg";
        String csvPath = "./path.csv";

        Properties properties = new Properties();
        try
        {
            properties.load(new BufferedReader(new FileReader(configPath)));
            System.out.println("==========Load Config==========");
            username = properties.getProperty("username");
            System.out.println("username : " + username);
            password = properties.getProperty("password");
            System.out.println("password : " + password);
            authUrl = properties.getProperty("authUrl");
            System.out.println("authUrl : " + authUrl);
            container = properties.getProperty("container");
            System.out.println("container : " + container);
            path = properties.getProperty("path");
            System.out.println("path : " + path);
            System.out.println("==========End Load config==========");
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        AccountConfig config = new AccountConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setAuthUrl(authUrl);
        config.setAuthenticationMethod(AuthenticationMethod.BASIC);
        config.setDisableSslValidation(true);
        Account account = new AccountFactory(config).createAccount();
        
        Container bucket = account.getContainer(container);
        if(!bucket.exists())
        {
            bucket.create();
            //bucket.makePublic();
        }
        
        CsvWriter csvWriter = new CsvWriter(csvPath, ',', Charset.forName("GBK"));
        String[] csvHead = {"path", "publicURL"};
        try
        {
            csvWriter.writeRecord(csvHead);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        File[] days = new File(path).listFiles();
        for(File d:days)
        {
            if(d.isDirectory())
            {
                String day_path = d.getAbsolutePath();
                File[] IDs = new File(day_path).listFiles();
                for(File i:IDs)
                {
                    if(i.isDirectory())
                    {
                        String id_path = i.getAbsolutePath();
                        File[] times = new File(id_path).listFiles();
                        for(File t:times)
                        {
                            if(t.isDirectory())
                            {
                                String time_path = t.getAbsolutePath();
                                File[] pics = new File(time_path).listFiles();
                                for(File pic:pics)
                                {
                                    if(!pic.isDirectory())
                                    {
                                        try
                                        {
                                        String pic_path = pic.getAbsolutePath();
                                        System.out.println("pic_path : " + pic_path);
                                        String pic_new_name = d.getName() + "_" + i.getName() + "_" + t.getName() + "_" + pic.getName();
                                        System.out.println("Start to upload : " + pic_new_name);
                                        StoredObject object = bucket.getObject(pic_new_name);
                                        object.uploadObject(new File(pic_path));
                                        String publicURL = object.getPublicURL();
                                        String[] content = {pic_path, publicURL};
                                        
                                        csvWriter.writeRecord(content);
                                        
                                        }catch (IOException e)
                                        {
                                            e.printStackTrace();
                                        }
                                        //return;
                                    }
                                }
                            }
                        }
                    }
                }
            }            
        }
        csvWriter.close();       
    }
}
