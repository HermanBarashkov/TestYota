package service;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Employer {
    String token;
    roleEnum role;

    public enum roleEnum{
        ADMIN, USER
    }

    @Override
    public String toString(){
        return role.toString();
    }
}
