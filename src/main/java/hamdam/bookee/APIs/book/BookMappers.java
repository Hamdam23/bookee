package hamdam.bookee.APIs.book;

import hamdam.bookee.APIs.book.helpers.BookRequestDTO;
import hamdam.bookee.APIs.book.helpers.BookResponseDTO;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.user.AppUserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMappers {

    public BookResponseDTO mapToBookResponse(BookEntity entity) {
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        BeanUtils.copyProperties(entity, bookResponseDTO);
        bookResponseDTO.setAuthors(entity.getAuthors().stream().map(AppUserEntity::getId).collect(Collectors.toList()));
        bookResponseDTO.setGenres(entity.getGenres().stream().map(GenreEntity::getId).collect(Collectors.toList()));
        return bookResponseDTO;
    }

    public BookResponseDTO mapToBookResponse(BookRequestDTO bookRequestDTO) {
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        BeanUtils.copyProperties(bookRequestDTO, bookResponseDTO);
        return bookResponseDTO;
    }

    public BookRequestDTO mapToBookRequest(String name, List<Long> authors) {
        BookRequestDTO request = new BookRequestDTO();
        request.setName(name);
        request.setAuthors(authors);
        return request;
    }

    public BookRequestDTO mapToBookRequest(String name, Double rating, List<Long> genres) {
        BookRequestDTO request = new BookRequestDTO();
        request.setName(name);
        request.setRating(rating);
        request.setGenres(genres);
        return request;
    }

    public BookEntity mapToBookEntity(BookRequestDTO bookRequestDTO) {
        BookEntity bookEntity = new BookEntity();
        BeanUtils.copyProperties(bookRequestDTO, this, "id");
        return bookEntity;
    }

    public BookEntity mapToBookEntity(String name, String tagline, String description,
                                      List<AppUserEntity> authors, Double rating, List<GenreEntity> genres) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setName(name);
        bookEntity.setTagline(tagline);
        bookEntity.setDescription(description);
        bookEntity.setAuthors(authors);
        bookEntity.setRating(rating);
        bookEntity.setGenres(genres);
        return bookEntity;
    }
}
