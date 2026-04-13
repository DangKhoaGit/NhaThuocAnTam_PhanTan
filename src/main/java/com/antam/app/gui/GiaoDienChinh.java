//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.antam.app.gui;

import java.io.IOException;

import com.antam.app.controller.dangnhap.DangNhapController;
import com.antam.app.controller.khungchinh.KhungChinhController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GiaoDienChinh extends Application {
    public GiaoDienChinh() {
    }

    public void start(Stage stage) throws IOException {
        stage.initStyle(StageStyle.DECORATED);
        stage.setMaximized(true);
        stage.setScene(new Scene(new DangNhapController(), 800, 800));
        stage.show();
    }

    public static void main(String[] args) {
        launch(new String[0]);
    }
}