package uz.pdp.models;

import lombok.Getter;
import lombok.Setter;
import uz.pdp.enums.auth.State;


@Setter
@Getter
public class SessionUser {
    private State state = State.OTHER_PROCESS;
    private int page = 1;
}
