package com.projects.userAccountApi.service;

import com.projects.userAccountApi.controller.form.LoginForm;

public interface LoginService {

    boolean authenticateUser(LoginForm login);

}
