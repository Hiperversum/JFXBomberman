package view.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.managers.UserManager;
import model.user.User;
import view.components.fxmlmodifier.EditUserPane;
import view.components.pane.UserPane;
import view.components.fxmlmodifier.safepane.UserSafePane;
import view.tools.SelectablePaneManager;

import java.util.Map;
import java.util.Observable;

public class UserMenu extends Menu{
    //COMPONENTS:
    private VBox usersListVBox;
    private UserSafePane userSafePane;
    private EditUserPane editUserPane;
    private Pane loadUserPane;


    //CONSTRUCTOR:
    public UserMenu(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
        initialize();
    }

    private void initialize(){
        usersListVBox = (VBox) fxmlLoader.getNamespace().get("usersListVBox");
        loadUserPane = (Pane) fxmlLoader.getNamespace().get("loadUserPane");
        editUserPane = new EditUserPane(fxmlLoader);
        userSafePane = new UserSafePane(fxmlLoader);

    }
    //METHODS:


    //MODIFY COMPONENTS: ///////////////////////////////////////////////////////////////////////////////////////////////
    private void setUsersListVBox(Map<String, User> userMap ){
        Pane userPane = (Pane)fxmlLoader.getNamespace().get("userPane");
        usersListVBox.getChildren().clear();
        usersListVBox.setPrefHeight(userPane.getPrefHeight());
        usersListVBox.getChildren().add(userPane);
        if (!userMap.isEmpty())
        for (Map.Entry<String, User> entry : userMap.entrySet()){
            String nickName = entry.getKey();
            User user = entry.getValue();
            UserPane pane = new UserPane(fxmlLoader,nickName,user.getImage());
            //EVENTS:
            pane.getDeleteButton().setOnMouseClicked( mouseEvent -> {
                userSafePane.showUserSafePane(pane.getUserID(),pane.getImage());
                loadUserPane.setVisible(false);
                controller.setEvent(mouseEvent);
            });
            SelectablePaneManager.setEvents(pane, mouseEvent -> {
                controller.setEvent(mouseEvent);
            });

            //ADD CHILDREN:
            usersListVBox.setPrefHeight(usersListVBox.getPrefHeight() + pane.getPrefHeight());
            usersListVBox.getChildren().add(pane);
        }
    }


    ////////////////////////////////////////////
    private void userManagerUpdateHandler(UserManager userManager, UserManager.UpdateType updateType){
       if(updateType!=null){
           switch (updateType){
               case LOAD_USERS_MENU -> {
                   setUsersListVBox(userManager.getUserMap());
               }
               case USER_DELETED -> {
                   setUsersListVBox(userManager.getUserMap());
                   userSafePane.hidePane();
               }
               case NEW_USER -> {
                   editUserPane.setEditUserPane(userManager.getUserResources().getAvatarResources());
                   editUserPane.handleNickname();
               }
           }
       }
    }
    //OBSERVER & OBSERVABLE UPDATES:
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof UserManager){
            userManagerUpdateHandler((UserManager) o, (UserManager.UpdateType) arg);
        }
    }
}
