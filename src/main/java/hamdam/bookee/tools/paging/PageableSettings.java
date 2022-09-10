package hamdam.bookee.tools.paging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
class PageableSettings {
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
}
