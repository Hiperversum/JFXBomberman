package view.components.button;
import javafx.scene.control.Button;

public abstract class CustomButton extends Button {
    public CustomButton(double width,double height){
        setPrefWidth(width);
        setPrefHeight(height);
    }
}
