package controller;

import entity.Tuser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import model.user_dao;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "userControl")
@SessionScoped
public class userController implements Serializable {

    @ManagedProperty(value = "#{loginControl}")
    private loginController loginControl;

    private List<Tuser> userList = new ArrayList();
    private Tuser user = new Tuser();
    private String oldPass;
    private String newPass;
    private String passCheck;

    public userController() {
        refreshList();
    }

    public void viewNewUserDialog() {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        resetInputs();
        RequestContext.getCurrentInstance().openDialog("createUser", options, null);
    }

    public void viewEditUserDialog(Tuser user) {
        this.user = user;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editUser", options, null);
    }

    public void viewChangePassDialog(Tuser user) {
        this.user = user;
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("modal", true);
        options.put("resizable", false);
        RequestContext.getCurrentInstance().openDialog("editPassword", options, null);
    }

    public void closeDialog(String name) {
        RequestContext.getCurrentInstance().closeDialog(name);
    }

    public String insertUser() {

        user.setFullname(user.getFullname().trim());
        user.setUsername(user.getUsername().trim());
        user.setDescrption(user.getDescrption().trim());

        if (!user.getFullname().equals("")) {
            if (user.getFullname().length() <= 50) {
                if (!user.getUsername().equals("")) {
                    if (user.getUsername().length() <= 50) {
                        if (!user.getPassword().equals("")) {
                            if (user.getPassword().length() <= 50) {
                                if (!passCheck.equals("")) {
                                    if (user.getPassword().equals(passCheck)) {
                                        if (user.getRoleid() != null) {
                                            if (user.getValidfrom() != null) {
                                                if (user.getValidto() == null || user.getValidto().equals(user.getValidfrom()) || user.getValidto().after(user.getValidfrom())) {
                                                    if (user.getDescrption().length() <= 500) {
                                                        user_dao dao = new user_dao();
                                                        user.setTuser(loginControl.getEnteredUser());
                                                        Tuser userFromDB = dao.getUserByUsername(user.getUsername());
                                                        if (userFromDB == null) {
                                                            dao.createUser(user);
                                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                                                            refreshList();
                                                            closeDialog("createUser");
                                                            resetInputs();
                                                            return "";
                                                        } else {
                                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کاربری وارد شده تکراری است."));
                                                            return "";
                                                        }
                                                    } else {
                                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات باید حداکثر 500 کاراکتر باشد."));
                                                        return "";
                                                    }
                                                } else {
                                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "تاریخ پایان اعتبار حساب نمی تواند قبل از تاریخ شروع اعتبار باشد."));
                                                    return "";
                                                }
                                            } else {
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "تاریخ شروع اعتبار حساب کاربری را انتخاب کنید."));
                                                return "";
                                            }
                                        } else {
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نقش کاربر را انتخاب کنید."));
                                            return "";
                                        }
                                    } else {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گذرواژه و تکرار آن یکسان نیستند."));
                                        return "";
                                    }
                                } else {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "تکرار گذرواژه را وارد کنید."));
                                    return "";
                                }
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گذرواژه باید حداکثر 50 کاراکتر باشد."));
                                return "";
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گذرواژه را وارد کنید."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کاربری باید حداکثر 50 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کاربری را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کامل باید حداکثر 50 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کامل را وارد کنید."));
            return "";
        }
    }

    public String update() {

        user.setFullname(user.getFullname().trim());
        user.setUsername(user.getUsername().trim());
        user.setDescrption(user.getDescrption().trim());

        if (!user.getFullname().equals("")) {
            if (user.getFullname().length() <= 50) {
                if (!user.getUsername().equals("")) {
                    if (user.getUsername().length() <= 50) {
                        if (!user.getPassword().equals("")) {
                            if (user.getRoleid() != null) {
                                if (user.getValidfrom() != null) {
                                    if (user.getValidto() == null || user.getValidto().equals(user.getValidfrom()) || user.getValidto().after(user.getValidfrom())) {
                                        if (user.getDescrption().length() <= 500) {
                                            user_dao dao = new user_dao();
                                            Tuser userFromDB = dao.getUserByUsername(user.getUsername());
                                            if (userFromDB == null || userFromDB.getId().compareTo(user.getId()) == 0) {
                                                dao.updateUser(user);
                                                loginControl.setEnteredUser(user);
                                                refreshList();
                                                closeDialog("editUser");
                                                resetInputs();
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "عملیات موفق", "عملیات با موفقیت انجام شد."));
                                                return "";
                                            } else {
                                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کاربری وارد شده تکراری است."));
                                                return "";
                                            }
                                        } else {
                                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "توضیحات باید حداکثر 500 کاراکتر باشد."));
                                            return "";
                                        }
                                    } else {
                                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "تاریخ پایان اعتبار حساب نمی تواند قبل از تاریخ شروع اعتبار باشد."));
                                        return "";
                                    }
                                } else {
                                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "تاریخ شروع اعتبار حساب کاربری را انتخاب کنید."));
                                    return "";
                                }
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نقش کاربر را انتخاب کنید."));
                                return "";
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گذرواژه را وارد کنید."));
                            return "";
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کاربری باید حداکثر 50 کاراکتر باشد."));
                        return "";
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کاربری را وارد کنید."));
                    return "";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کامل باید حداکثر 50 کاراکتر باشد."));
                return "";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "نام کامل را وارد کنید."));
            return "";
        }

    }

    public void removeUser(Tuser user) {
        user_dao userDAO = new user_dao();
        if (loginControl.getEnteredUser().getId().compareTo(user.getId()) != 0) {
            user.setDto(new Date());
            userDAO.updateUser(user);
            refreshList();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "امکان حذف وجود ندارد", "امکان حذف کاربری که با آن وارد سامانه شده اید وجود ندارد."));
        }
    }

    public String changePass() {

        if (!oldPass.equals("")) {
            if (!newPass.equals("")) {
                if (newPass.length() <= 50) {
                    if (!passCheck.equals("")) {
                        user_dao dao = new user_dao();
                        if (oldPass.equals(user.getPassword())) {
                            if (newPass.equals(passCheck)) {
                                user.setPassword(newPass);
                                dao.updateUser(user);
                                closeDialog("editPassword");
                                resetInputs();
                            } else {
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گذرواژه جدید و تکرار آن یکسان نیستند."));
                            }
                        } else {
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گذرواژه فعلی نادرست می باشد."));
                        }
                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "تکرار گذرواژه را وارد کنید."));
                    }
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گذرواژه جدید باید حداکثر 50 کاراکتر باشد."));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گذرواژه جدید را وارد کنید."));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "هشدار", "گذرواژه فعلی را وارد کنید."));
        }
        return "";
    }

    public List<SelectItem> getUsersItem() {
        List<SelectItem> items = new ArrayList();
        user_dao dao = new user_dao();
        userList = dao.getAllUsers();
        for (Tuser project : userList) {
            items.add(new SelectItem(project.getFullname(), project.getFullname()));
        }
        return items;
    }

    public void resetInputs() {
        oldPass = "";
        newPass = "";
        passCheck = "";
        user = new Tuser();
    }

    public void refreshList() {
        user_dao dao = new user_dao();
        userList = dao.getAllUsers();
    }

    public boolean trimFilterValue(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }
        String list = value.toString().toLowerCase();
        filterText = filterText.toLowerCase();
        if (list.contains(filterText)) {
            return true;
        } else {
            return false;
        }
    }

//***********************Setters & Getters******************************//
    public loginController getLoginControl() {
        return loginControl;
    }

    public void setLoginControl(loginController loginControl) {
        this.loginControl = loginControl;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getPassCheck() {
        return passCheck;
    }

    public void setPassCheck(String passCheck) {
        this.passCheck = passCheck;
    }

    public Tuser getUser() {
        return user;
    }

    public void setUser(Tuser user) {
        this.user = user;
    }

    public List<Tuser> getUserList() {
        return userList;
    }

    public void setUserList(List userList) {
        this.userList = userList;
    }
}
