package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.model.UserAccess;
import ru.edme.repository.UserAccessRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserAccessService implements UserDetailsService {

    private final UserAccessRepository userAccessRepository;
    private final Class<UserAccess> entityClass = UserAccess.class;

    @Override // Преобразование User в User понятного спрингу
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        return userAccessRepository.findByUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    @Transactional
    public UserAccess save(UserAccess entity) {
        return userAccessRepository.save(entity);
    }

    public UserAccess findById(Long id) {
        return userAccessRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
        );
    }

    public List<UserAccess> findAll() {
        return userAccessRepository.findAll();
    }

    @Transactional
    public UserAccess update(UserAccess entity) {
        UserAccess userAccess = findById(entity.getId());
        userAccess.setUserLogin(entity.getUserLogin());
        userAccess.setUserPassword(entity.getUserPassword());
        userAccess.setFullName(entity.getFullName());
        userAccess.setUserRole(entity.getUserRole());

        return userAccessRepository.save(userAccess);
    }

    @Transactional
    public boolean delete(Long id) {
        UserAccess userAccess = findById(id);
        userAccessRepository.delete(userAccess);
        return true;
    }
}
