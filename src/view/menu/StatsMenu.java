package view.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import model.managers.UserManager;
import model.user.Stats;
import java.util.Observable;

public class StatsMenu extends Menu{
    private Pane statsPane;
    private Label gamesPlayedLabel,gamesWonLabel,gamesLostLabel,bestScoreLabel;
    private ImageView userStatsImV;
    public StatsMenu(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
        initialize();
    }

    private void initialize(){
        statsPane = (Pane) fxmlLoader.getNamespace().get("statsPane");
        gamesPlayedLabel = (Label) fxmlLoader.getNamespace().get("gamesPlayedLabel");
        gamesWonLabel = (Label) fxmlLoader.getNamespace().get("gamesWonLabel");
        gamesLostLabel = (Label) fxmlLoader.getNamespace().get("gamesLostLabel");
        bestScoreLabel = (Label) fxmlLoader.getNamespace().get("bestScoreLabel");
        userStatsImV = (ImageView) fxmlLoader.getNamespace().get("userStatsImV");
    }

    private void showStatsPane(Stats stats){
        statsPane.setVisible(statsPane.isVisible()?false:true);
        bestScoreLabel.setText(Integer.toString(stats.getBestScore()));
        gamesPlayedLabel.setText(Integer.toString(stats.getGamesPlayed()));
        gamesWonLabel.setText(Integer.toString(stats.getGamesWon()));
        gamesLostLabel.setText(Integer.toString(stats.getGamesLost()));
    }

    private void setUserStatsImV(Image image){
        userStatsImV.setImage(image);
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof UserManager){
            UserManager userManager = (UserManager) o;
            if (arg == UserManager.UpdateType.LOAD_STATS)
                showStatsPane(userManager.getUserLoaded().getStats());
            else if (arg == UserManager.UpdateType.LOAD_USER) {
                if (userManager.getUserLoaded()!=null)
                setUserStatsImV(userManager.getUserLoaded().getImage());
            }
        }

    }
}
