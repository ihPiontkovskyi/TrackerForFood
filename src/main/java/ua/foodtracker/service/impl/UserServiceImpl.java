package ua.foodtracker.service.impl;

import ua.foodtracker.annotation.Autowired;
import ua.foodtracker.annotation.Service;
import ua.foodtracker.dao.UserDao;
import ua.foodtracker.entity.UserEntity;
import ua.foodtracker.exception.IncorrectDataException;
import ua.foodtracker.service.UserService;
import ua.foodtracker.service.domain.User;
import ua.foodtracker.service.utility.EntityMapper;
import ua.foodtracker.validator.impl.UserValidator;

import java.util.Locale;
import java.util.Optional;

import static org.mindrot.jbcrypt.BCrypt.checkpw;
import static ua.foodtracker.service.utility.EntityMapper.mapUserEntityToUser;
import static ua.foodtracker.service.utility.EntityMapper.mapUserToEntityUser;
import static ua.foodtracker.service.utility.ServiceUtility.addByType;
import static ua.foodtracker.service.utility.ServiceUtility.deleteByStringId;
import static ua.foodtracker.service.utility.ServiceUtility.findByStringParam;
import static ua.foodtracker.service.utility.ServiceUtility.modifyByType;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserValidator userValidator;

    @Override
    public User login(String email, String pass) {
        Optional<UserEntity> userByEmail = userDao.findByEmail(email);
        if (userByEmail.isPresent() && checkpw(pass, userByEmail.get().getPassword())) {
            return mapUserEntityToUser(userByEmail.get());
        } else {
            userValidator.putIssue("user", "login.incorrect.data");
            throw new IncorrectDataException(userValidator.getErrorMessageByIssues());
        }
    }

    @Override
    public void register(User user) {
        userValidator.validate(user);
        if (userDao.findByEmail(user.getEmail()).isPresent()) {
            userValidator.putIssue("email", "user.already.exist");
            throw new IncorrectDataException(userValidator.getErrorMessageByIssues());
        }
        addByType(user, userValidator, obj -> userDao.save(mapUserToEntityUser(obj)));
    }

    @Override
    public void modify(User user) {
        modifyByType(user, userValidator, obj -> userDao.update(mapUserToEntityUser(obj)));
    }

    @Override
    public Optional<User> findById(String id) {
        return findByStringParam(id, userValidator, intId -> userDao.findById(intId).map(EntityMapper::mapUserEntityToUser));
    }

    @Override
    public void delete(String id) {
        deleteByStringId(id, userValidator, intId -> userDao.deleteById(intId));
    }

    @Override
    public void setLocale(Locale locale) {
        userValidator.setLocale(locale);
    }
}
