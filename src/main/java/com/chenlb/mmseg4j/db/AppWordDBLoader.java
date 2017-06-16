package com.chenlb.mmseg4j.db;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.elasticsearch.common.io.PathUtils;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

import com.chenlb.mmseg4j.Dictionary;
import com.mchange.v2.c3p0.ComboPooledDataSource;


public class AppWordDBLoader {

    private static final ESLogger log = Loggers.getLogger("mmseg-analyzer");
    static {
        try {
            System.setProperty("com.mchange.v2.c3p0.management.ManagementCoordinator",
                "com.mchange.v2.c3p0.management.NullManagementCoordinator");
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Throwable e) {

        }
    }

    private ComboPooledDataSource ds;


    private AppWordDBLoader() {
        this.init();
    }


    private void init() {
        ds = new ComboPooledDataSource();
        FileInputStream in = null;
        Properties props = new Properties();

        try {
            in = new FileInputStream(PathUtils.get(Dictionary.getDictRoot(), "jdbc.properties").toFile());
            props.load(in);
            ds.setDriverClass("com.mysql.jdbc.Driver");
            ds.setJdbcUrl(props.getProperty("url"));
            ds.setUser(props.getProperty("username"));
            ds.setPassword(props.getProperty("password"));
            ds.setMaxPoolSize(Integer.parseInt(props.getProperty("maxPoolSize", "300")));
            ds.setMinPoolSize(Integer.parseInt(props.getProperty("minPoolSize", "30")));

        }
        catch (Exception e) {
            log.error("Create datasource failed.", e);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception e) {

                }
            }
        }
    }

    private static class StaticHolder {
        private static AppWordDBLoader instance = new AppWordDBLoader();
    }


    public static AppWordDBLoader getInstance() {
        return StaticHolder.instance;
    }


    public InputStream getWordStreamFromDB(String appId) throws IOException {
        if (this.ds == null)
            return null;
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {
            conn = this.ds.getConnection();
            pstmt = conn.prepareStatement("select words from app_search_words where app_id=?");
            pstmt.setString(1, appId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String words = rs.getString("words");
                if (words != null) {
                    return new ByteArrayInputStream(words.getBytes("UTF-8"));
                }
            }
        }
        catch (Exception e) {
            log.error("Execute sql failed.", e);
            throw new IOException("Execute sql failed", e);
        }
        finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                }
                catch (SQLException e) {

                }
            }
            if (conn != null) {
                try {
                    conn.close();
                }
                catch (Exception e) {

                }
            }
        }
        return null;
    }

}
