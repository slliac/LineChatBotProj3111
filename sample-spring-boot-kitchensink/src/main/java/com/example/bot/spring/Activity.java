package com.example.bot.spring;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * Activity interface
 * 
 * An interface for Activity that used.
 * 
 * 
 * 
 * @author Gordon
 *
 */
public interface Activity{
    public int getPrice() ;
}
