package com.hotel.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置管理器
 * 使用单例模式管理应用程序配置
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    
    // 单例实例
    private static volatile ConfigManager instance;
    
    // 配置属性
    private Properties properties;
    
    // 私有构造函数
    private ConfigManager() {
        loadConfiguration();
    }
    
    /**
     * 获取单例实例（双重锁定检查）
     * @return ConfigManager实例
     */
    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * 加载配置文件
     */
    private void loadConfiguration() {
        properties = new Properties();
        
        // 加载数据库配置
        loadPropertiesFile("db.properties");
        
        // 可以加载其他配置文件
        // loadPropertiesFile("app.properties");
        
        logger.info("Configuration loaded successfully");
    }
    
    /**
     * 加载属性文件
     * @param fileName 文件名
     */
    private void loadPropertiesFile(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream != null) {
                Properties tempProps = new Properties();
                tempProps.load(inputStream);
                properties.putAll(tempProps);
                logger.debug("Loaded configuration file: {}", fileName);
            } else {
                logger.warn("Configuration file not found: {}", fileName);
            }
        } catch (IOException e) {
            logger.error("Failed to load configuration file: " + fileName, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.warn("Failed to close input stream for file: " + fileName, e);
                }
            }
        }
    }
    
    /**
     * 获取字符串类型的配置值
     * @param key 配置键
     * @return 配置值，如果不存在返回null
     */
    public String getString(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * 获取字符串类型的配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值，如果不存在返回默认值
     */
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * 获取整数类型的配置值
     * @param key 配置键
     * @return 配置值，如果不存在或格式错误返回0
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }
    
    /**
     * 获取整数类型的配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值，如果不存在或格式错误返回默认值
     */
    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                logger.warn("Invalid integer value for key {}: {}", key, value);
            }
        }
        return defaultValue;
    }
    
    /**
     * 获取长整数类型的配置值
     * @param key 配置键
     * @return 配置值，如果不存在或格式错误返回0
     */
    public long getLong(String key) {
        return getLong(key, 0L);
    }
    
    /**
     * 获取长整数类型的配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值，如果不存在或格式错误返回默认值
     */
    public long getLong(String key, long defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Long.parseLong(value.trim());
            } catch (NumberFormatException e) {
                logger.warn("Invalid long value for key {}: {}", key, value);
            }
        }
        return defaultValue;
    }
    
    /**
     * 获取布尔类型的配置值
     * @param key 配置键
     * @return 配置值，如果不存在返回false
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }
    
    /**
     * 获取布尔类型的配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值，如果不存在返回默认值
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value.trim());
        }
        return defaultValue;
    }
    
    /**
     * 获取双精度浮点数类型的配置值
     * @param key 配置键
     * @return 配置值，如果不存在或格式错误返回0.0
     */
    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }
    
    /**
     * 获取双精度浮点数类型的配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值，如果不存在或格式错误返回默认值
     */
    public double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Double.parseDouble(value.trim());
            } catch (NumberFormatException e) {
                logger.warn("Invalid double value for key {}: {}", key, value);
            }
        }
        return defaultValue;
    }
    
    /**
     * 检查配置键是否存在
     * @param key 配置键
     * @return true如果存在
     */
    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }
    
    /**
     * 获取所有配置键
     * @return 配置键数组
     */
    public String[] getKeys() {
        return properties.keySet().toArray(new String[0]);
    }
    
    /**
     * 重新加载配置
     */
    public void reload() {
        logger.info("Reloading configuration...");
        loadConfiguration();
    }
    
    // 数据库相关配置的便捷方法
    public String getDbDriver() {
        return getString("db.driver");
    }
    
    public String getDbUrl() {
        return getString("db.url");
    }
    
    public String getDbUsername() {
        return getString("db.username");
    }
    
    public String getDbPassword() {
        return getString("db.password");
    }
    
    public int getDbInitialSize() {
        return getInt("db.initialSize", 5);
    }
    
    public int getDbMaxActive() {
        return getInt("db.maxActive", 20);
    }
    
    public int getDbMinIdle() {
        return getInt("db.minIdle", 5);
    }
    
    public long getDbMaxWait() {
        return getLong("db.maxWait", 60000);
    }
}