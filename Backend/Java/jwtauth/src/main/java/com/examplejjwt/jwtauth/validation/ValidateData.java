package com.examplejjwt.jwtauth.validation;

import com.examplejjwt.jwtauth.entity.Profile;
import com.examplejjwt.jwtauth.entity.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class ValidateData {
    public boolean isValidMobile(String mobile){
        return (mobile.matches("\\d{10}") || mobile != null) ;
    }

    public boolean isValidEmail(String email){
        return (email != null || email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"));
    }
    public boolean isValidAge(String age){
        try{
            int a = Integer.parseInt(age);
            return a>0;
        }
        catch (NumberFormatException e){
            return false;
        }
    }

    public boolean isMobileExist(String mobile, Set< Profile> validProfiles) {
        return validProfiles.stream().anyMatch(profile -> profile.getMobile().equals(mobile));
    }

    public boolean isEmailExist(String email, Set< Profile> validProfiles) {
        return validProfiles.stream().anyMatch(profile -> profile.getEmail().equalsIgnoreCase(email));
    }

    public boolean isUserExit(User user, Set< Profile> validProfiles) {
        return validProfiles.stream().anyMatch(profile -> profile.getUser().equals(user));
    }

}
