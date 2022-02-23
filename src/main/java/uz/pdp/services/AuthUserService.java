package uz.pdp.services;

import org.telegram.telegrambots.meta.api.objects.User;
import uz.pdp.enums.auth.Role;
import uz.pdp.enums.auth.Status;
import uz.pdp.models.AuthUser;
import uz.pdp.models.SessionUser;
import uz.pdp.repository.AuthUserRepository;

import java.time.LocalDateTime;


public class AuthUserService {
    private static final SessionUser sessionUser = new SessionUser();

    public static void register(User user, String chatID) {
        if (user.getIsBot() || AuthUserRepository.getInstance().alreadyRegistred(user.getId().toString())) {
            return;
        }
        AuthUser authUser = new AuthUser(user.getId().toString(), user.getUserName(), user.getFirstName(), user.getLastName(),
                user.getLanguageCode(), LocalDateTime.now().toString(), Status.NON_ACTIVE.toString(), Role.USER.toString(), chatID);
        AuthUserRepository.getInstance().insert(authUser);
    }

    public static SessionUser getSessionUser() {
        return sessionUser;
    }
}
