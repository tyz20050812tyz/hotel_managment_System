package com.hotel.service.strategy;

import com.hotel.model.Customer;
import com.hotel.service.strategy.impl.RegularPriceStrategy;
import com.hotel.service.strategy.impl.VIPPriceStrategy;

/**
 * 价格计算上下文
 * 使用策略模式管理不同的价格计算策略
 */
public class PriceCalculator {
    
    private PriceCalculationStrategy strategy;
    
    /**
     * 设置价格计算策略
     * @param strategy 价格计算策略
     */
    public void setStrategy(PriceCalculationStrategy strategy) {
        this.strategy = strategy;
    }
    
    /**
     * 根据客户类型自动选择价格策略
     * @param customer 客户对象
     */
    public void setStrategy(Customer customer) {
        if (customer == null || !customer.isVip()) {
            this.strategy = new RegularPriceStrategy();
        } else {
            this.strategy = new VIPPriceStrategy(customer.getVipLevel());
        }
    }
    
    /**
     * 计算价格
     * @param booking 预订对象
     * @return 计算后的价格
     */
    public java.math.BigDecimal calculate(com.hotel.model.Booking booking) {
        if (strategy == null) {
            throw new IllegalStateException("Price calculation strategy not set");
        }
        return strategy.calculatePrice(booking);
    }
    
    /**
     * 获取当前策略名称
     * @return 策略名称
     */
    public String getCurrentStrategyName() {
        return strategy != null ? strategy.getStrategyName() : "未设置策略";
    }
    
    /**
     * 获取当前折扣率
     * @return 折扣率
     */
    public double getCurrentDiscountRate() {
        return strategy != null ? strategy.getDiscountRate() : 1.0;
    }
}