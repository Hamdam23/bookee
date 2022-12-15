package hamdam.bookee.APIs.image.helpers;

import hamdam.bookee.APIs.image.ImageEntity;
import org.springframework.beans.BeanUtils;

/**
 * It contains two methods that map between ImageEntity and ImageDTO
 */
public class ImageMappers {

    public static ImageDTO mapToImageDTO(ImageEntity entity) {
        if (entity == null) return null;
        ImageDTO imageDTO = new ImageDTO();
        BeanUtils.copyProperties(entity, imageDTO);
        return imageDTO;
    }
}
