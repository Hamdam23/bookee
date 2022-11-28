package hamdam.bookee.APIs.book.helpers;

import hamdam.bookee.APIs.book.BookEntity;
import hamdam.bookee.APIs.genre.GenreEntity;
import hamdam.bookee.APIs.user.AppUserEntity;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BookMappers {

    public static BookResponseDTO mapToBookResponse(BookEntity entity) {
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        BeanUtils.copyProperties(entity, bookResponseDTO);
        bookResponseDTO.setAuthors(entity.getAuthors().stream().map(AppUserEntity::getId).collect(Collectors.toList()));
        bookResponseDTO.setGenres(entity.getGenres().stream().map(GenreEntity::getId).collect(Collectors.toList()));
        return bookResponseDTO;
    }

    public static BookResponseDTO mapToBookResponse(BookRequestDTO bookRequestDTO) {
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        BeanUtils.copyProperties(bookRequestDTO, bookResponseDTO);
        return bookResponseDTO;
    }

    public static BookRequestDTO mapToBookRequest(String name, List<Long> authors) {
        BookRequestDTO request = new BookRequestDTO();
        request.setName(name);
        request.setAuthors(authors);
        return request;
    }

    public static BookRequestDTO mapToBookRequest(String name, Double rating, List<Long> genres) {
        BookRequestDTO request = new BookRequestDTO();
        request.setName(name);
        request.setRating(rating);
        request.setGenres(genres);
        return request;
    }

    public static BookEntity mapToBookEntity(BookRequestDTO bookRequestDTO) {
        BookEntity bookEntity = new BookEntity();
        BeanUtils.copyProperties(bookRequestDTO, bookEntity, "id");
        return bookEntity;
    }

    public static BookEntity mapToBookEntity(String name, String tagline, String description,
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
