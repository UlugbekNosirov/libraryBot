package uz.pdp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.enums.auth.Role;
import uz.pdp.enums.auth.Status;



@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String languageCode;
    private String joinedAt;
    private String status = Status.NON_ACTIVE.toString();
    private String role = Role.USER.toString();
    private String chatID;

}
