package com.hotel.service.strategy.impl;

import com.hotel.model.Booking;
import com.hotel.service.strategy.PriceCalculationStrategy;
import java.math.BigDecimal;

/**
 * 普通客户价格策略
 */
public class RegularPriceStrategy implements PriceCalculationStrategy {
    
    @Override
    public BigDecimal calculatePrice(Booking booking) {
        if (booking == null || booking.getRoom() == null || booking.getRoom().getRoomType() == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal dailyPrice = booking.getRoom().getRoomType().getPrice();
        long stayDays = booking.getStayDays();
        
        if (stayDays <= 0) {
            stayDays = 1; // 至少按一天计算
        }
        
        return dailyPrice.multiply(BigDecimal.valueOf(stayDays));
    }
    
    @Override
    public String getStrategyName() {
        return "普通客户价格策略";
    }
    
    @Override
    public double getDiscountRate() {
        return 1.0; // 无折扣
    }
}