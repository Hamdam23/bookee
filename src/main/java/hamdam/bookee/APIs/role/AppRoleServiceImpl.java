package hamdam.bookee.APIs.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppRoleServiceImpl implements AppRoleService{
    private final AppRoleRepository appRoleRepository;

    @Override
    public AppRole addRole(AppRoleDTO appRole) {
        return appRoleRepository.save(new AppRole(appRole));
    }

    @Override
    public List<AppRole> getAllRoles() {
        return appRoleRepository.findAll();
    }

    @Override
    public void deleteRoleById(long id) {
        appRoleRepository.deleteById(id);
    }
}
