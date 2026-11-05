package view.components.button;

public class BackgroundButton extends CustomButton{
    public BackgroundButton(String text,double width, double height) {
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
