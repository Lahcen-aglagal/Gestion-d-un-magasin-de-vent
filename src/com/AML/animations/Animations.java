package com.AML.animations;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInUp;
import animatefx.animation.FadeOut;
import animatefx.animation.Flip;
import animatefx.animation.GlowText;
import animatefx.animation.Shake;
import animatefx.animation.ZoomIn;
import com.jfoenix.controls.JFXProgressBar;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author ay0ub
 */
public class Animations {
    public static void fadeInUp(Node node) {
        new FadeInUp(node).play();
    }

    public static void fadeOut(Node node) {
        new FadeOut(node).play();
    }

    public static void shake(Node node) {
        new Shake(node).play();
    }
    public static void fadeIn(Node node){
        new FadeIn(node).play();
    }
    public static void progressAnimation(JFXProgressBar progressBar,double value){
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(progressBar.progressProperty(), value);
        KeyFrame keyFrame = new KeyFrame(new Duration(600), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public static void zoomIn(Node info) {
        new ZoomIn(info).play();
    }

    public static void flip(Node titlePane) {
        new Flip(titlePane).play();
    }

    public static void Textglow(Label l) {
        new GlowText(l, Color.valueOf("#9145b6"), Color.valueOf("#780bb8"))
                        .setCycleCount(3)
                        .setSpeed(0.5)
                        .setResetOnFinished(true)
                        .play();
    }
}
