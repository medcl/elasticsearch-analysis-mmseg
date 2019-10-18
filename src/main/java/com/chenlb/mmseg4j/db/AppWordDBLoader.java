package com.chenlb.mmseg4j.db;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.mchange.v2.c3p0.ComboPooledDataSource;


import org.elasticsearch.SpecialPermission;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.PrivilegedAction;

public class AppWordDBLoader {

    private static final Logger log = LogManager.getLogger();
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


    private String getEnv(String key) {
        return getEnv(key, null);
    }

    private String getEnv(String key, String defaultVal) {
        String val = System.getenv(key);
        return val == null ? defaultVal : val;
    }

    private void init() {

        // see https://www.elastic.co/guide/en/elasticsearch/plugins/current/plugin-authors.html
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            // unprivileged code such as scripts do not have SpecialPermission
            sm.checkPermission(new SpecialPermission());
        }
        ds = (ComboPooledDataSource) AccessController.doPrivileged((PrivilegedAction<ComboPooledDataSource>)
                () -> {
                    return new ComboPooledDataSource();
                }
        );

        try {
            ds.setDriverClass("com.mysql.jdbc.Driver");
            ds.setJdbcUrl(getEnv("JDBC_URL"));
            ds.setUser(getEnv("JDBC_USERNAME"));
            ds.setPassword(getEnv("JDBC_PASSWORD"));
            ds.setMaxPoolSize(Integer.parseInt(getEnv("JDBC_MAX_POOL_SIZE", "300")));
            ds.setMinPoolSize(Integer.parseInt(getEnv("JDBC_MIN_POOL_SIZE", "3")));
        }
        catch (Exception e) {
            log.error("Create datasource failed.", e);
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
        String words;

        try {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new SpecialPermission());
            }
            words = (String) AccessController.doPrivileged((PrivilegedExceptionAction<String>)
                    () -> {
                        PreparedStatement pstmt = null;
                        Connection conn = null;

                        try {
                            conn = this.ds.getConnection();
                            pstmt = conn.prepareStatement("select words from app_search_words where app_id=?");
                            pstmt.setString(1, appId);
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next()) {
                                return rs.getString("words");
                            }
                        } finally {
                            if (pstmt != null) {
                                try {
                                    pstmt.close();
                                } catch (SQLException e) {

                                }
                            }
                            if (conn != null) {
                                try {
                                    conn.close();
                                } catch (Exception e) {

                                }
                            }
                        }
                        return null;
                    });
        } catch (PrivilegedActionException e) {

            // e.getException() should be an instance of IOException
            // as only checked exceptions will be wrapped in a
            // PrivilegedActionException.
            log.error("Execute sql failed.", e.getException());
            throw (IOException) e.getException();
        }

        if (words != null) {
            return new ByteArrayInputStream(words.getBytes("UTF-8"));
        }
        return null;
    }

}
