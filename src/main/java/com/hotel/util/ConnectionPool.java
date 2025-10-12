package com.hotel.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接池工具类
 * 使用单例模式管理数据库连接池
 */
public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
    
    // 单例实例
    private static volatile ConnectionPool instance;
    
    // 数据源
    private DataSource dataSource;
    
    // 私有构造函数
    private ConnectionPool() {
        initDataSource();
    }
    
    /**
     * 获取单例实例（双重锁定检查）
     * @return ConnectionPool实例
     */
    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }
    
    /**
     * 初始化数据源
     */
    private void initDataSource() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        
        try {
            // 读取数据库配置文件
            inputStream = ConnectionPool.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            
            if (inputStream == null) {
                throw new RuntimeException("Cannot find db.properties file");
            }
            
            properties.load(inputStream);
            
            // 创建Druid数据源
            dataSource = DruidDataSourceFactory.createDataSource(properties);
            
            logger.info("Database connection pool initialized successfully");
            
        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool", e);
            throw new RuntimeException("Failed to initialize database connection pool", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.warn("Failed to close input stream", e);
                }
            }
        }
    }
    
    /**
     * 获取数据库连接
     * @return 数据库连接
     * @throws SQLException SQL异常
     */
    public Connection getConnection() throws SQLException {
        try {
            Connection connection = dataSource.getConnection();
            logger.debug("Database connection obtained");
            return connection;
        } catch (SQLException e) {
            logger.error("Failed to get database connection", e);
            throw e;
        }
    }
    
    /**
     * 获取数据源
     * @return 数据源
     */
    public DataSource getDataSource() {
        return dataSource;
    }
    
    /**
     * 关闭连接池
     */
    public void close() {
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            druidDataSource.close();
            logger.info("Database connection pool closed");
        }
    }
    
    /**
     * 获取连接池状态信息
     * @return 连接池状态信息
     */
    public String getPoolStatus() {
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            StringBuilder status = new StringBuilder();
            status.append("Connection Pool Status:\n");
            status.append("Active Connections: ").append(druidDataSource.getActiveCount()).append("\n");
            status.append("Pool Size: ").append(druidDataSource.getPoolingCount()).append("\n");
            status.append("Max Active: ").append(druidDataSource.getMaxActive()).append("\n");
            status.append("Initial Size: ").append(druidDataSource.getInitialSize()).append("\n");
            return status.toString();
        }
        return "Connection pool status not available";
    }
    
    /**
     * 测试数据库连接
     * @return true如果连接成功
     */
    public boolean testConnection() {
        try (Connection connection = getConnection()) {
            boolean isValid = connection.isValid(5); // 5秒超时
            logger.info("Database connection test: {}", isValid ? "SUCCESS" : "FAILED");
            return isValid;
        } catch (SQLException e) {
            logger.error("Database connection test failed", e);
            return false;
        }
    }
}