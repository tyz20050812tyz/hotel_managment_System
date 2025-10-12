package com.hotel.service.strategy;

import com.hotel.model.Booking;
import java.math.BigDecimal;

/**
 * 价格计算策略接口
 */
public interface PriceCalculationStrategy {
    
    /**
     * 计算预订价格
     * @param booking 预订对象
     * @return 计算后的价格
     */
    BigDecimal calculatePrice(Booking booking);
    
    /**
     * 获取策略名称
     * @return 策略名称
     */
    String getStrategyName();
    
    /**
     * 获取折扣率
     * @return 折扣率（0.0-1.0）
     */
    double getDiscountRate();
}