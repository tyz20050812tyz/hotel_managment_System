package com.hotel.controller;

import com.hotel.dao.DAOFactory;
import com.hotel.dao.RoomTypeDAO;
import com.hotel.model.Room;
import com.hotel.model.RoomType;
import com.hotel.service.RoomService;
import com.hotel.service.ServiceFactory;
import com.hotel.util.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 房间管理控制器
 */
public class RoomController extends BaseController {
    
    private static final Logger logger = LogManager.getLogger(RoomController.class);
    private RoomService roomService;
    private RoomTypeDAO roomTypeDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        this.roomService = ServiceFactory.createRoomService();
        this.roomTypeDAO = DAOFactory.createRoomTypeDAO();
        logger.info("RoomController initialized");
    }
    
    @Override
    protected String handleBusinessLogic(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            return "redirect:/admin/room/list";
        }
        
        String action = pathInfo.substring(1); // 去掉开头的"/"
        String method = request.getMethod();
        
        if ("GET".equals(method)) {
            return handleGetRequest(request, action);
        } else if ("POST".equals(method)) {
            return handlePostRequest(request, action);
        }
        
        throw new IllegalArgumentException("不支持的HTTP方法: " + method);
    }
    
    private String handleGetRequest(HttpServletRequest request, String action) throws Exception {
        // 处理多级路径
        String[] pathParts = action.split("/");
        String mainAction = pathParts[0];
        
        switch (mainAction) {
            case "list":
                return listRooms(request);
            case "add":
                return showAddForm(request);
            case "edit":
                return showEditForm(request);
            case "detail":
                return showRoomDetail(request);
            case "type":
                return handleRoomTypeRequest(request, pathParts);
            case "status":
                return handleRoomStatusRequest(request, pathParts);
            default:
                throw new IllegalArgumentException("不支持的操作: " + action);
        }
    }
    
    private String handlePostRequest(HttpServletRequest request, String action) throws Exception {
        // 处理多级路径
        String[] pathParts = action.split("/");
        String mainAction = pathParts[0];
        
        switch (mainAction) {
            case "save":
                return saveRoom(request);
            case "delete":
                return deleteRoom(request);
            case "changeStatus":
                return changeRoomStatus(request);
            case "type":
                return handleRoomTypePostRequest(request, pathParts);
            default:
                throw new IllegalArgumentException("不支持的操作: " + action);
        }
    }
    
    /**
     * 处理房间类型POST请求
     */
    private String handleRoomTypePostRequest(HttpServletRequest request, String[] pathParts) throws Exception {
        if (pathParts.length < 2) {
            throw new IllegalArgumentException("房间类型操作不完整");
        }
        
        String typeAction = pathParts[1];
        switch (typeAction) {
            case "save":
                return saveRoomType(request);
            default:
                throw new IllegalArgumentException("不支持的房间类型操作: " + typeAction);
        }
    }
    
    /**
     * 保存房间类型
     */
    private String saveRoomType(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "typeId");
        String typeName = getParameter(request, "typeName");
        String priceStr = getParameter(request, "price");
        String bedCountStr = getParameter(request, "bedCount");
        String maxGuestsStr = getParameter(request, "maxGuests");
        String description = getParameter(request, "description");
        String amenities = getParameter(request, "amenities");
        
        // 参数验证
        if (Utils.isEmpty(typeName)) {
            request.setAttribute("error", "类型名称不能为空");
            return Utils.isEmpty(idStr) ? showAddRoomTypeForm(request) : showEditRoomTypeForm(request);
        }
        
        if (Utils.isEmpty(priceStr)) {
            request.setAttribute("error", "价格不能为空");
            return Utils.isEmpty(idStr) ? showAddRoomTypeForm(request) : showEditRoomTypeForm(request);
        }
        
        if (Utils.isEmpty(bedCountStr)) {
            request.setAttribute("error", "床位数不能为空");
            return Utils.isEmpty(idStr) ? showAddRoomTypeForm(request) : showEditRoomTypeForm(request);
        }
        
        if (Utils.isEmpty(maxGuestsStr)) {
            request.setAttribute("error", "最大客人数不能为空");
            return Utils.isEmpty(idStr) ? showAddRoomTypeForm(request) : showEditRoomTypeForm(request);
        }
        
        try {
            java.math.BigDecimal price = new java.math.BigDecimal(priceStr);
            int bedCount = Integer.parseInt(bedCountStr);
            int maxGuests = Integer.parseInt(maxGuestsStr);
            
            RoomType roomType = new RoomType();
            roomType.setTypeName(typeName);
            roomType.setPrice(price);
            roomType.setBedCount(bedCount);
            roomType.setMaxGuests(maxGuests);
            roomType.setDescription(description);
            roomType.setAmenities(amenities);
            
            boolean success;
            if (!Utils.isEmpty(idStr)) {
                // 更新房间类型
                roomType.setTypeId(Integer.parseInt(idStr));
                success = roomTypeDAO.update(roomType);
            } else {
                // 新增房间类型
                Integer typeId = roomTypeDAO.insert(roomType);
                success = typeId != null;
            }
            
            if (success) {
                request.getSession().setAttribute("message", "操作成功");
                request.getSession().setAttribute("messageType", "success");
                return "redirect:/admin/room/type/list";
            } else {
                request.setAttribute("error", "操作失败");
                return Utils.isEmpty(idStr) ? showAddRoomTypeForm(request) : showEditRoomTypeForm(request);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "数字格式错误");
            return Utils.isEmpty(idStr) ? showAddRoomTypeForm(request) : showEditRoomTypeForm(request);
        } catch (Exception e) {
            logger.error("保存房间类型失败", e);
            request.setAttribute("error", "保存失败: " + e.getMessage());
            return Utils.isEmpty(idStr) ? showAddRoomTypeForm(request) : showEditRoomTypeForm(request);
        }
    }
    
    private String listRooms(HttpServletRequest request) throws Exception {
        String pageStr = getParameter(request, "page");
        String sizeStr = getParameter(request, "size");
        String typeIdStr = getParameter(request, "typeId");
        String status = getParameter(request, "status");
        
        int page = getIntParameter(request, "page", 1);
        int size = getIntParameter(request, "size", 10);
        
        List<Room> rooms;
        
        if (!Utils.isEmpty(typeIdStr)) {
            int typeId = Integer.parseInt(typeIdStr);
            rooms = roomService.getRoomsByType(typeId);
        } else if (!Utils.isEmpty(status)) {
            rooms = roomService.getRoomsByStatus(Room.RoomStatus.valueOf(status));
        } else {
            rooms = roomService.getAllRooms();
        }
        
        // 获取房间类型列表
        List<RoomType> roomTypes = roomTypeDAO.findAll();
        
        // 简单分页
        int total = rooms.size();
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, total);
        
        if (startIndex < total) {
            rooms = rooms.subList(startIndex, endIndex);
        } else {
            rooms.clear();
        }
        
        request.setAttribute("rooms", rooms);
        request.setAttribute("roomTypes", roomTypes);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", size);
        request.setAttribute("totalCount", total);
        request.setAttribute("totalPages", (total + size - 1) / size);
        request.setAttribute("selectedTypeId", typeIdStr);
        request.setAttribute("selectedStatus", status);
        
        return "/admin/room-list.jsp";
    }
    
    private String showAddForm(HttpServletRequest request) throws Exception {
        List<RoomType> roomTypes = roomTypeDAO.findAll();
        request.setAttribute("roomTypes", roomTypes);
        return "/admin/room-add.jsp";
    }
    
    private String showEditForm(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            throw new IllegalArgumentException("房间ID不能为空");
        }
        
        int roomId = Integer.parseInt(idStr);
        Room room = roomService.getRoomById(roomId);
        
        if (room == null) {
            throw new IllegalArgumentException("房间不存在");
        }
        
        List<RoomType> roomTypes = roomTypeDAO.findAll();
        request.setAttribute("room", room);
        request.setAttribute("roomTypes", roomTypes);
        
        return "/admin/room-edit.jsp";
    }
    
    /**
     * 显示房间详情
     */
    private String showRoomDetail(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            throw new IllegalArgumentException("房间ID不能为空");
        }
        
        int roomId = Integer.parseInt(idStr);
        Room room = roomService.getRoomById(roomId);
        
        if (room == null) {
            throw new IllegalArgumentException("房间不存在");
        }
        
        request.setAttribute("room", room);
        request.setAttribute("pageTitle", "房间详情");
        
        return "/admin/room-detail.jsp";
    }
    
    private String saveRoom(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "roomId");
        String roomNumber = getParameter(request, "roomNumber");
        String typeIdStr = getParameter(request, "typeId");
        String floorStr = getParameter(request, "floor");
        String statusStr = getParameter(request, "status");
        
        // 参数验证
        if (Utils.isEmpty(roomNumber)) {
            request.setAttribute("error", "房间号不能为空");
            return showAddForm(request);
        }
        
        if (Utils.isEmpty(typeIdStr)) {
            request.setAttribute("error", "房间类型不能为空");
            return showAddForm(request);
        }
        
        if (Utils.isEmpty(floorStr)) {
            request.setAttribute("error", "楼层不能为空");
            return showAddForm(request);
        }
        
        try {
            int typeId = Integer.parseInt(typeIdStr);
            int floor = Integer.parseInt(floorStr);
            
            Room room = new Room();
            room.setRoomNumber(roomNumber);
            room.setTypeId(typeId);
            room.setFloor(floor);
            
            if (!Utils.isEmpty(statusStr)) {
                room.setStatus(Room.RoomStatus.valueOf(statusStr));
            } else {
                room.setStatus(Room.RoomStatus.AVAILABLE);
            }
            
            boolean success;
            if (!Utils.isEmpty(idStr)) {
                // 更新房间
                room.setRoomId(Integer.parseInt(idStr));
                success = roomService.updateRoom(room);
            } else {
                // 新增房间
                Integer roomId = roomService.addRoom(room);
                success = roomId != null;
            }
            
            if (success) {
                request.getSession().setAttribute("message", "操作成功");
                request.getSession().setAttribute("messageType", "success");
                return "redirect:/admin/room/list";
            } else {
                request.setAttribute("error", "操作失败");
                return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "数字格式错误");
            return Utils.isEmpty(idStr) ? showAddForm(request) : showEditForm(request);
        }
    }
    
    private String deleteRoom(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            return "json:" + createErrorResponse("房间ID不能为空", 400);
        }
        
        try {
            int roomId = Integer.parseInt(idStr);
            boolean success = roomService.deleteRoom(roomId);
            
            if (success) {
                return "json:" + createSuccessResponse("删除成功");
            } else {
                return "json:" + createErrorResponse("删除失败", 500);
            }
            
        } catch (NumberFormatException e) {
            return "json:" + createErrorResponse("房间ID格式错误", 400);
        }
    }

    
    private String changeRoomStatus(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        String statusStr = getParameter(request, "status");
        
        if (Utils.isEmpty(idStr) || Utils.isEmpty(statusStr)) {
            request.getSession().setAttribute("message", "参数不能为空");
            request.getSession().setAttribute("messageType", "danger");
            return "redirect:/admin/room/list";
        }
        
        try {
            int roomId = Integer.parseInt(idStr);
            Room.RoomStatus status = Room.RoomStatus.valueOf(statusStr);
            
            // 获取房间对象
            Room room = roomService.getRoomById(roomId);
            if (room == null) {
                request.getSession().setAttribute("message", "房间不存在");
                request.getSession().setAttribute("messageType", "danger");
                return "redirect:/admin/room/list";
            }
            
            // 更新状态
            room.setStatus(status);
            boolean success = roomService.updateRoom(room);
            
            if (success) {
                request.getSession().setAttribute("message", "房间状态修改成功");
                request.getSession().setAttribute("messageType", "success");
            } else {
                request.getSession().setAttribute("message", "房间状态修改失败");
                request.getSession().setAttribute("messageType", "danger");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "房间ID格式错误");
            request.getSession().setAttribute("messageType", "danger");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("message", "状态值错误");
            request.getSession().setAttribute("messageType", "danger");
        } catch (Exception e) {
            logger.error("修改房间状态失败", e);
            request.getSession().setAttribute("message", "修改失败: " + e.getMessage());
            request.getSession().setAttribute("messageType", "danger");
        }
        
        return "redirect:/admin/room/list";
    }
    
    /**
     * 处理房间状态相关请求
     */
    private String handleRoomStatusRequest(HttpServletRequest request, String[] pathParts) throws Exception {
        if (pathParts.length < 2) {
            throw new IllegalArgumentException("房间状态操作不完整");
        }
        
        String statusAction = pathParts[1];
        switch (statusAction) {
            case "change":
                return changeRoomStatus(request);
            default:
                throw new IllegalArgumentException("不支持的房间状态操作: " + statusAction);
        }
    }
    
    /**
     * 处理房间类型相关请求
     */
    private String handleRoomTypeRequest(HttpServletRequest request, String[] pathParts) throws Exception {
        if (pathParts.length < 2) {
            throw new IllegalArgumentException("房间类型操作不完整");
        }
        
        String typeAction = pathParts[1];
        switch (typeAction) {
            case "list":
                return listRoomTypes(request);
            case "add":
                return showAddRoomTypeForm(request);
            case "edit":
                return showEditRoomTypeForm(request);
            case "delete":
                return deleteRoomType(request);
            default:
                throw new IllegalArgumentException("不支持的房间类型操作: " + typeAction);
        }
    }
    
    /**
     * 显示添加房间类型表单
     */
    private String showAddRoomTypeForm(HttpServletRequest request) throws Exception {
        request.setAttribute("pageTitle", "添加房间类型");
        return "/admin/room-type-add.jsp";
    }
    
    /**
     * 显示编辑房间类型表单
     */
    private String showEditRoomTypeForm(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            throw new IllegalArgumentException("房间类型ID不能为空");
        }
        
        int typeId = Integer.parseInt(idStr);
        RoomType roomType = roomTypeDAO.findById(typeId);
        
        if (roomType == null) {
            throw new IllegalArgumentException("房间类型不存在");
        }
        
        request.setAttribute("roomType", roomType);
        request.setAttribute("pageTitle", "编辑房间类型");
        return "/admin/room-type-edit.jsp";
    }
    
    /**
     * 删除房间类型
     */
    private String deleteRoomType(HttpServletRequest request) throws Exception {
        String idStr = getParameter(request, "id");
        if (Utils.isEmpty(idStr)) {
            request.getSession().setAttribute("message", "房间类型ID不能为空");
            request.getSession().setAttribute("messageType", "danger");
            return "redirect:/admin/room/type/list";
        }
        
        try {
            int typeId = Integer.parseInt(idStr);
            boolean success = roomTypeDAO.delete(typeId);
            
            if (success) {
                request.getSession().setAttribute("message", "房间类型删除成功");
                request.getSession().setAttribute("messageType", "success");
            } else {
                request.getSession().setAttribute("message", "房间类型删除失败");
                request.getSession().setAttribute("messageType", "danger");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "房间类型ID格式错误");
            request.getSession().setAttribute("messageType", "danger");
        } catch (Exception e) {
            logger.error("删除房间类型失败", e);
            request.getSession().setAttribute("message", "删除失败: " + e.getMessage());
            request.getSession().setAttribute("messageType", "danger");
        }
        
        return "redirect:/admin/room/type/list";
    }

    /**
     * 查看房间类型列表
     */
    private String listRoomTypes(HttpServletRequest request) throws Exception {
        List<RoomType> roomTypes = roomTypeDAO.findAll();
        request.setAttribute("roomTypes", roomTypes);
        request.setAttribute("pageTitle", "房间类型管理");
        return "/admin/room-type-list.jsp";
    }
}