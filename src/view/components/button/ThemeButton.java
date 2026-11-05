package view.components.button;

public class ThemeButton extends CustomButton {
    public ThemeButton(String text,double width, double height) {
        super(width, height);
        setText(text);
        setId(text);
        initialize();
    }

    private void initialize(){
        getStyleClass().add("button-settings");
        setFocusTraversable(false);
    }
}
