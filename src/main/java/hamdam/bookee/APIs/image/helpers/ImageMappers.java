package hamdam.bookee.APIs.image.helpers;

import hamdam.bookee.APIs.image.ImageEntity;
import org.springframework.beans.BeanUtils;

public class ImageMappers {

    public static ImageDTO mapToImageDTO(ImageEntity entity) {
        ImageDTO imageDTO = new ImageDTO();
        BeanUtils.copyProperties(entity, imageDTO);
        return imageDTO;
    }

    public static ImageEntity mapToImageEntity(String imageName, String location) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageName(imageName);
        imageEntity.setLocation(location);
        return imageEntity;
    }
}
