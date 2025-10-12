package com.hotel.service.strategy.impl;

import com.hotel.model.Booking;
import com.hotel.service.strategy.PriceCalculationStrategy;
import java.math.BigDecimal;

/**
 * VIP客户价格策略
 */
public class VIPPriceStrategy implements PriceCalculationStrategy {
    
    private final int vipLevel;
    
    public VIPPriceStrategy(int vipLevel) {
        this.vipLevel = vipLevel;
    }
    
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
        
        BigDecimal totalPrice = dailyPrice.multiply(BigDecimal.valueOf(stayDays));
        
        // 应用VIP折扣
        double discountRate = getDiscountRate();
        BigDecimal discount = BigDecimal.valueOf(discountRate);
        
        return totalPrice.multiply(discount);
    }
    
    @Override
    public String getStrategyName() {
        return "VIP" + vipLevel + "级客户价格策略";
    }
    
    @Override
    public double getDiscountRate() {
        // VIP等级越高，折扣越大
        switch (vipLevel) {
            case 1: return 0.95; // 5%折扣
            case 2: return 0.90; // 10%折扣
            case 3: return 0.85; // 15%折扣
            case 4: return 0.80; // 20%折扣
            case 5: return 0.75; // 25%折扣
            default: return 1.0;  // 无折扣
        }
    }
}