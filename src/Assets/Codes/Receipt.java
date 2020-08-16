package Assets.Codes;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class Receipt implements Initializable {

    @FXML
    protected AnchorPane printablearea;

    @FXML
    private Label bill_no;

    @FXML
    private Label date;

    @FXML
    private Label name;

    @FXML
    private Label address;

    @FXML
    private TableView<Model_Receipt> receipt_table;

    @FXML
    private TableColumn<Model_Receipt, Integer> code_col;
    @FXML
    private TableColumn<Model_Receipt, String> product_col;

    @FXML
    private TableColumn<Model_Receipt, Integer> quantity_col;

    @FXML
    private TableColumn<Model_Receipt, Integer> price_col;

    @FXML
    private TableColumn<Model_Receipt, Integer> Total_col;

    @FXML
    private Label total;

    @FXML
    private Label advance;

    @FXML
    private Label due;

    @FXML
    private JFXButton print_btn;

    int sub_Total=0;
    int discount =0;
    int left=0;
    int Paid =0;
    @FXML
    void Print(ActionEvent event) throws IOException
    {
        ObservableSet<Printer> printers = Printer.getAllPrinters();
        Printer myprinter=null;
        for(Printer printer : printers)
        {
            if(printer.getName().matches("HP DeskJet 5810 series"))
            myprinter=printer;
        }
        PrinterJob job = PrinterJob.createPrinterJob();
        PageLayout layout = myprinter.createPageLayout(Paper.A4, PageOrientation.PORTRAIT,0.75,0.75,0.75,0.75);
        if(job!=null) {
            double scaleX = layout.getPrintableWidth()/printablearea.getBoundsInParent().getWidth();
            double scaleY = 500/printablearea.getBoundsInParent().getHeight();
            Scale scale = new Scale(scaleX,scaleY);
            printablearea.getTransforms().add(scale);
            job.printPage(layout,printablearea);
            job.endJob();
            printablearea.getTransforms().remove(scale);
        }

    }

    public void getItems(List<Model_Receipt> receipt, String Name, String Address,String dis,String paid,int bill)
    {
        for(int i=0;i<receipt.size();i++) {
            sub_Total=sub_Total+receipt.get(i).getTotal();
            receipt_table.getItems().addAll(new Model_Receipt(receipt.get(i).Productname,receipt.get(i).getQuantity(),receipt.get(i).getPrice(),receipt.get(i).Total,i));
        }
        name.setText(Name);
        address.setText(Address);
        discount=Integer.parseInt(dis);
        sub_Total=sub_Total-(sub_Total*discount)/100;
        Paid = Integer.parseInt(paid);
        left = sub_Total-Paid;
        if(left>0) {
            due.setText("" + left);
        }
        else
        {
            due.setText("0");
        }
        advance.setText(""+paid);
        total.setText(""+sub_Total);
        bill_no.setText(""+bill);

    }
    void initialize_table()
    {
        code_col.setCellValueFactory(new PropertyValueFactory<>("Code"));
        product_col.setCellValueFactory(new PropertyValueFactory<>("Productname"));
        quantity_col.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("Price"));
        Total_col.setCellValueFactory(new PropertyValueFactory<>("Total"));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            initialize_table();
            date.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/mm/yyyy")));

    }
}
