package sample;

        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.fxml.Initializable;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.chart.LineChart;
        import javafx.scene.chart.NumberAxis;
        import javafx.scene.chart.XYChart;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;
        import javafx.scene.control.TextField;

        import java.io.*;
        import java.net.Socket;
        import java.net.URL;
        import java.util.ResourceBundle;

        import static jdk.nashorn.internal.objects.NativeString.substring;

public class Controller implements Initializable {

    public TextField curValue;
    public TextField presInValue;
    public TextField presOutValue;
    public TextField mh1Value;
    public TextField mh2Value;
    public TextField mhSumValue;
    public Label dateTime;


    public Button Button1;

    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;
    public String serverWord;

    ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();

    private void changeText(String text) {

        String valueParam = text.substring(text.indexOf(".") + 1, text.indexOf(":"));
        String value = text.substring(text.lastIndexOf("|") + 1);
        String Time = text.substring(text.indexOf(":") + 1, text.lastIndexOf("|"));
        dateTime.setText(Time);

        switch (valueParam) {
            case "curValue":
                curValue.setText(value);
                datas.add(new XYChart.Data(dateTime.getText(),Float.parseFloat(value),"curValue"));
                break;
            case "presInValue":
                presInValue.setText(value);
                datas.add(new XYChart.Data(dateTime.getText(),Float.parseFloat(value),"presInValue"));
                break;
            case "presOutValue":
                presOutValue.setText(value);
                datas.add(new XYChart.Data(dateTime.getText(),Float.parseFloat(value),"presOutValue"));
                break;
            case "mh1Value":
                mh1Value.setText(value);
                datas.add(new XYChart.Data(dateTime.getText(),Float.parseFloat(value),"mh1Value"));
                break;
            case "mh2Value":
                mh2Value.setText(value);
                datas.add(new XYChart.Data(dateTime.getText(),Float.parseFloat(value),"mh2Value"));
                break;
        }
    }

    public void click(ActionEvent actionEvent) {

    }

    public void startVNS3(ActionEvent actionEvent) {
        try {
            try {
                clientSocket = new Socket("8.8.8.8", 88);
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String word = "Login|User|Password";
                out.write(word + "\n");
                out.flush();
                serverWord = in.readLine();
                System.out.println(serverWord);
                if (serverWord.equals("done")) {
                    System.out.println("Соединение установлено");

                    out.write("wantedRegisters|vns3.curValue|vns3.presInValue|vns3.presOutValue|" +
                            "vns3.mh1Value|vns3.mh2Value" + "\n");

                    out.flush();

                    int i = 0;
                    while (i < 5) {
                        serverWord = in.readLine();
                        System.out.println(serverWord);
                        i++;
                        changeText(serverWord);
                    }
                    mhSumValue.setText(Float.toString(Float.parseFloat(mh1Value.getText()) +
                            Float.parseFloat(mh2Value.getText())));

                    datas.add(new XYChart.Data(dateTime.getText(),Float.parseFloat(mhSumValue.getText()),"mhSumValue"));
                    System.out.println(datas);
                }
            } finally {
                System.out.println("Соединение закрыто.");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        curValue.setText("-");
        presInValue.setText("-");
        presOutValue.setText("-");
        mh1Value.setText("-");
        mh2Value.setText("-");
        mhSumValue.setText("-");

    }
}
