package sample;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.converter.FormatStringConverter;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.io.IOException;
import java.text.DateFormat;
import java.util.List;
import java.util.Scanner;


public class Main extends Application {
    public static final String ACCOUNT_SID = "ACa5d1dd153645ec3d6537df04c16ae30a";
    public static final String AUTH_TOKEN = "64dd4b80885392aff8ae1037c551ca17";

    private static TableView<XYChart.Data<Number, Number>> getTableView() {
        var table = new TableView<XYChart.Data<Number, Number>>();
        var timeColumn = new TableColumn<XYChart.Data<Number, Number>, Number>("Access Time");
        timeColumn.setCellValueFactory(row -> row.getValue().XValueProperty());
        var dateFormat = DateFormat.getTimeInstance();
        var readableTime = new FormatStringConverter<Number>(dateFormat);
        timeColumn.setCellFactory(column -> new TextFieldTableCell<>(readableTime));
        var valueColumn = new TableColumn<XYChart.Data<Number, Number>, Number>("ID"); //random employee ID
        valueColumn.setCellValueFactory(row -> row.getValue().YValueProperty());
        table.getColumns().setAll(List.of(timeColumn, valueColumn));
        return table;
    }

    @Override
    public void start(Stage primaryStage) {
        var serialPort = SerialPortService.getSerialPort("/dev/cu.usbserial-0001");
        var table = getTableView();
        var controller = new DataController();
        serialPort.addDataListener(controller);
        table.setItems(controller.getDataPoints());
        var outputStream = serialPort.getOutputStream();
        //create window
        var pane = new BorderPane();
        var label = new Label();
        var button = new Button("Remove Access");

        button.setOnMousePressed(value -> {        //OFF
            try {
                    outputStream.write(48);
                    label.setText("ACCESS DENIED");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //sort pane
        pane.setTop(button);
        pane.setCenter(table);
        pane.setRight(label);
        pane.setPadding(new Insets(0, 20, 0, 20));
        Scene scene = new Scene(pane, 800, 600); // creates the JavaFX window
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber("+16472905266"),
                new PhoneNumber("+16305280502"),
                "Attempted Login").create();
        System.out.println(message.getSid());

        // History history = new History("day1",1,"1","2","2"); //() = constructor
        //history.print();
        //System.out.println(history.return_int());

        launch(args);
    }
}