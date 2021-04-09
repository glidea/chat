package top.glidea.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 针对 List 的分页工具
 */
public class ListPagingUtil {

    public static <T> List<T> getPage(List<T> list, int pageNum, int pageSize) {
        if (list == null) {
            return new ArrayList<>();
        }

        pageNum = Math.max(pageNum, 1);
        pageSize = Math.max(pageSize, 1);

        int startIndex = (pageNum - 1) * pageSize;
        if (startIndex > list.size() - 1) {
            return new ArrayList<>();
        }

        int endIndex = Math.min(list.size(), startIndex + pageSize);
        return list.subList(startIndex, endIndex);
    }
}
