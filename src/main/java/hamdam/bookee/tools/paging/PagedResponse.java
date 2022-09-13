package hamdam.bookee.tools.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedResponse<T> {
    private List<T> content;
    @JsonProperty("page_number")
    private Integer pageNumber;
    @JsonProperty("next_page")
    private Integer nextPage;
    @JsonProperty("previous_page")
    private Integer previousPage;
    @JsonProperty("page_size")
    private Integer pageSize;
    @JsonProperty("total_pages")
    private Integer totalPages;
    @JsonProperty("total_elements")
    private Long totalElements;

    public PagedResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getPageable().getPageNumber();
        this.nextPage = getNextPage(page);
        this.previousPage = getPrevPage(page);
        this.pageSize = page.getPageable().getPageSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
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