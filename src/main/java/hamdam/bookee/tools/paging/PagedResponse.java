package hamdam.bookee.tools.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedResponse<T> {
    List<T> content;
    @JsonProperty("pagination_settings")
    PageableSettings paginationSettings;

    public PagedResponse(Page<T> page) {
        this.content = page.getContent();
        this.paginationSettings = new PageableSettings(page.getPageable().getPageNumber(),
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