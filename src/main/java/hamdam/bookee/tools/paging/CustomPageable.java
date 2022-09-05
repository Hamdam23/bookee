package hamdam.bookee.tools.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
class CustomPageable {
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

    public CustomPageable(Integer pageNumber, Integer nextPage, Integer previousPage, Integer pageSize, Integer totalPages, Long totalElements) {
        this.pageNumber = pageNumber;
        this.nextPage = nextPage;
        this.previousPage = previousPage;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
