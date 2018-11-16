package controller;

import entity.Tuser;
import java.time.LocalDate;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.user_dao;

@ManagedBean(name = "loginControl")
@SessionScoped
public class loginController {

    String user;
    String pass;
    boolean isLogged;
    private Tuser enteredUser;

    public String login() {
        user_dao dao = new user_dao();
        user = user.trim();
        pass = pass.trim();
        if (user.equals("") || pass.equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "لطفا اطلاعات را به درستی وارد نمایید."));
            return "";
        } else {
            Tuser userByUsername = dao.getUserByUsername(user);
            if (userByUsername != null
                    && userByUsername.getUsername().equals(user)
                    && userByUsername.getPassword().equals(pass)) {
                if (userByUsername.getValidfrom().before(new Date())) {
                    LocalDate today =  LocalDate.now();
                    if (userByUsername.getValidto() == null ||
                            userByUsername.getValidto().toString().equals(today.toString()) ||
                            userByUsername.getValidto().after(new Date())) {
                        enteredUser = userByUsername;
                        isLogged = true;
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", enteredUser.getUsername());
                        return "mainMenu?faces-redirect=true";
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "تاریخ اعتبار حساب شما به پایان رسیده است. با راهبر سامانه تماس بگیرید."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "زمان شروع حساب کاربری شما فرا نرسیده است. با راهبر سامانه تماس بگیرید."));
                    return "";
                }
            } else {
                isLogged = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کاربری یا گذرواژه وارد شده نادرست می باشد."));
                return "";
            }
        }
    }

    public String logout() {
        enteredUser = new Tuser();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }

//***********************Setters & Getters******************************//
    public Tuser getEnteredUser() {
        return enteredUser;
    }

    public void setEnteredUser(Tuser enteredUser) {
        this.enteredUser = enteredUser;
    }

    public boolean isIsLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
