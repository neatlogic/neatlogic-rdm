package neatlogic.module.rdm.event;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.rdm.dto.IssueVo;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/** 需求事件专用深拷贝，保留 Vo 对象图，不通过序列化或无类型载荷传递业务对象。 */
final class IssueEventSnapshot {
    private IssueEventSnapshot() {
    }

    /** 直接读取字段，避免派生属性 getter 生成 ID、查询线程上下文或改变业务状态。 */
    static IssueVo copy(IssueVo source) {
        return IssueVo.class.cast(copyValue(source, new IdentityHashMap<>()));
    }

    /** 身份索引仅用于共享引用与循环关系；未知可变类型必须失败，禁止退化为原引用。 */
    private static Object copyValue(Object source, IdentityHashMap<Object, Object> seen) {
        if (source == null) {
            return null;
        }
        Class<?> type = source.getClass();
        if (type == String.class || type == Boolean.class || type == Character.class
                || type == Byte.class || type == Short.class || type == Integer.class || type == Long.class
                || type == Float.class || type == Double.class || type == BigDecimal.class
                || type == BigInteger.class || type == UUID.class || type.isEnum()) {
            return source;
        }
        if (seen.containsKey(source)) {
            return seen.get(source);
        }
        if (source instanceof Date) {
            Object target = ((Date) source).clone();
            seen.put(source, target);
            return target;
        }
        if (type.isArray()) {
            int length = Array.getLength(source);
            Object target = Array.newInstance(type.getComponentType(), length);
            seen.put(source, target);
            for (int i = 0; i < length; i++) {
                Array.set(target, i, copyValue(Array.get(source, i), seen));
            }
            return target;
        }
        // 自定义属性原本即包含 JSON 数据；仅复制其内部值，不把 IssueVo 转换为 JSON。
        if (source instanceof JSONObject) {
            JSONObject target = new JSONObject(true);
            seen.put(source, target);
            for (Map.Entry<String, Object> entry : ((JSONObject) source).entrySet()) {
                target.put(entry.getKey(), copyValue(entry.getValue(), seen));
            }
            return target;
        }
        if (source instanceof Collection) {
            Collection<Object> target;
            if (source instanceof JSONArray) {
                target = new JSONArray();
            } else if (source instanceof List) {
                target = new ArrayList<>();
            } else if (source instanceof Set) {
                target = new LinkedHashSet<>();
            } else {
                throw unsupported(type);
            }
            seen.put(source, target);
            for (Object value : (Collection<?>) source) {
                target.add(copyValue(value, seen));
            }
            return target;
        }
        if (source instanceof Map) {
            Map<Object, Object> target = new LinkedHashMap<>();
            seen.put(source, target);
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) source).entrySet()) {
                target.put(copyValue(entry.getKey(), seen), copyValue(entry.getValue(), seen));
            }
            return target;
        }
        String packageName = type.getPackage().getName();
        if (!packageName.equals("neatlogic.framework.rdm.dto")
                && !packageName.equals("neatlogic.framework.dto")
                && !packageName.equals("neatlogic.framework.common.dto")
                && !packageName.equals("neatlogic.framework.file.dto")) {
            throw unsupported(type);
        }
        try {
            Object target = type.getDeclaredConstructor().newInstance();
            seen.put(source, target);
            for (Class<?> owner = type; owner != Object.class; owner = owner.getSuperclass()) {
                for (Field field : owner.getDeclaredFields()) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        field.setAccessible(true);
                        field.set(target, copyValue(field.get(source), seen));
                    }
                }
            }
            return target;
        } catch (ReflectiveOperationException | IllegalArgumentException ex) {
            throw new IllegalArgumentException("需求事件快照无法复制类型：" + type.getName(), ex);
        }
    }

    /** 显式拒绝未覆盖的可变值类型，新增业务属性时由适配器补充复制规则。 */
    private static IllegalArgumentException unsupported(Class<?> type) {
        return new IllegalArgumentException("需求事件快照不支持类型：" + type.getName());
    }
}
