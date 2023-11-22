package com.stpass.imes.plugin.sbt.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CommonUtils {
   public static Map<String, Object> getMapByObject(Object obj) throws IllegalAccessException {
      Map<String, Object> objectMap = new HashMap<>();
      Class<?> clazz = obj.getClass();

      for (Field field : clazz.getDeclaredFields()) {
         field.setAccessible(true);
         String fieldName = field.getName();
         Object value = field.get(obj);
         if (value == null) {
            value = "";
         }
         if (!fieldName.equals("this$0")) {
            objectMap.put(fieldName, value);
         }
      }
      return objectMap;
   }
   public static String getMessage(int errorCode){
      switch (errorCode){
          // TODO
//         case UhfBase.ErrorCode.ERROR_NO_TAG:
//            return "找不到标签!";
//         case UhfBase.ErrorCode.ERROR_INSUFFICIENT_PRIVILEGES:
//            return "没有权限访问!";
//         case UhfBase.ErrorCode.ERROR_MEMORY_OVERRUN:
//            return "数据区超限!";
//         case UhfBase.ErrorCode.ERROR_MEMORY_LOCK:
//            return "数据区被锁定!";
//         case UhfBase.ErrorCode.ERROR_TAG_NO_REPLY:
//            return "标签没有应答!";
//         case UhfBase.ErrorCode.ERROR_PASSWORD_IS_INCORRECT:
//            return "密码不正确!";
//         case UhfBase.ErrorCode.ERROR_RESPONSE_BUFFER_OVERFLOW:
//            return "缓冲区溢出!";
//         case UhfBase.ErrorCode.ERROR_NO_ENOUGH_POWER_ON_TAG:
//            return "标签能量不足!";
//         case UhfBase.ErrorCode.ERROR_OPERATION_FAILED:
//            return "操作失败!";
         default:
            return "失败!";
      }
   }
}
