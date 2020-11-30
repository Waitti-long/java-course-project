package cn.waitti.jcp.Tools;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TextTool implements EnabledTool {
    public Text text = new Text();
    public Pane pane;
    public ColorPicker colorPicker;
    public ComboBox fontBox;
    public ComboBox sizeBox;
    public List<Text> textList = new ArrayList<>();
    public CheckBox boldCheck;
    public CheckBox italicCheck;

    TextTool(Pane pane, ColorPicker colorPicker, ComboBox fontBox, ComboBox sizeBox, CheckBox boldCheck, CheckBox italicCheck) {
        this.colorPicker = colorPicker;
        this.pane = pane;
        this.fontBox = fontBox;
        this.sizeBox = sizeBox;
        this.italicCheck = italicCheck;
        this.boldCheck = boldCheck;
    }

    @Override
    public void activate() {
        pane.setOnMousePressed(this::start);
    }

    @Override
    public void deactivate() {
        pane.setOnMousePressed(null);
    }

    @Override
    public void start(MouseEvent mouseEvent) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Input Text Dialog");
        inputDialog.setHeaderText("Confirm");
        inputDialog.setContentText("Enter your text:");
        Optional<String> result = inputDialog.showAndWait();
        if (result.isPresent()) {
            text.setText(result.get());
            text.setFill(colorPicker.getValue());
            if (fontBox.getValue() == null && sizeBox.getValue() == null)
                text.setFont(Font.font("Arial", 10));
            else if (fontBox.getValue() == null && sizeBox.getValue() != null) {
                double value = Double.parseDouble(sizeBox.getValue().toString());
                text.setFont(Font.font("Arial", value));
            } else if (fontBox.getValue() != null && sizeBox.getValue() == null)
                text.setFont(Font.font(fontBox.getValue().toString(), 10));
            else {
                double value = Double.parseDouble(sizeBox.getValue().toString());
                text.setFont(Font.font(fontBox.getValue().toString(), value));
            }
            if (boldCheck.isSelected() && !italicCheck.isSelected())
                text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, text.getFont().getSize()));
            else if (!boldCheck.isSelected() && italicCheck.isSelected())
                text.setFont(Font.font(text.getFont().getFamily(), FontPosture.ITALIC, text.getFont().getSize()));
            else if (boldCheck.isSelected() && italicCheck.isSelected())
                text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, FontPosture.ITALIC, text.getFont().getSize()));
            text.setOnMouseDragged(
                    event -> {
                        Text p = (Text) event.getSource();
                        if (pane.contains(event.getX(), event.getY())) {
                            p.setX(event.getX());
                            p.setY(event.getY());
                        }
                    });
            text.setX(mouseEvent.getX());
            text.setY(mouseEvent.getY());
            pane.getChildren().add(text);
            textList.add(text);
            text = new Text();
        }
    }

    @Override
    public void end(MouseEvent event) {
        Revocation.push();
    }
}
