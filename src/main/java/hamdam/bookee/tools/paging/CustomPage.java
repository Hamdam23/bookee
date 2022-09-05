package hamdam.bookee.tools.paging;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class CustomPage<T> {
    List<T> content;
    CustomPageable pageable;

    public CustomPage(Page<T> page) {
        this.content = page.getContent();
        this.pageable = new CustomPageable(page.getPageable().getPageNumber(),
                getNextPage(page),
                getPrevPage(page),
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements());
    }

    private Integer getNextPage(Page<T> page) {
        if (page.isLast()) {
            return null;
        }
        return page.getNumber() + 1;
    }

    private Integer getPrevPage(Page<T> page) {
        if (page.isFirst()) {
            return null;
        }
        return page.getNumber() - 1;
    }
}