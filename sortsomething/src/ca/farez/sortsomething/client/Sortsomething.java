package ca.farez.sortsomething.client;

import java.util.ArrayList;

import ca.farez.sortsomething.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sortsomething implements EntryPoint {
	
	private VerticalPanel mainPanel = new VerticalPanel();  
	private FlexTable cnFlexTable = new FlexTable();  
	private HorizontalPanel addPanel = new HorizontalPanel();  
	private TextBox newCNTextBox = new TextBox();  
	private Button addCNButton = new Button("Add");
	private Label lastUpdatedLabel = new Label();
	private ArrayList<String> callnumbers = new ArrayList<String>();

	/**  * Entry point method.  */  public void onModuleLoad() {  
		
		// Create table for call numbers.
		cnFlexTable.setText(0, 0, "Please enter some call numbers");
		
		// TODO Assemble call number panel.
		addPanel.add(newCNTextBox);
		addPanel.add(addCNButton);
		addPanel.setCellHorizontalAlignment(newCNTextBox, HasHorizontalAlignment.ALIGN_CENTER);
		addPanel.setCellHorizontalAlignment(addCNButton, HasHorizontalAlignment.ALIGN_CENTER);

		// Assemble Main panel.
		mainPanel.add(cnFlexTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdatedLabel);
		mainPanel.setCellHorizontalAlignment(cnFlexTable, HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setCellHorizontalAlignment(addPanel, HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.setCellHorizontalAlignment(lastUpdatedLabel, HasHorizontalAlignment.ALIGN_CENTER);
		
		// Associate the Main panel with the HTML host page.  
		RootPanel.get("cnList").add(mainPanel);
		
		// Move cursor focus to the input box.
		newCNTextBox.setFocus(true);
		
		// Listen for mouse events on the Add button.
	    addCNButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	        addCallNums();
	      }
	    });
	    
	 // Listen for keyboard events in the input box.
	    newCNTextBox.addKeyDownHandler(new KeyDownHandler() {
	      public void onKeyDown(KeyDownEvent event) {
	        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	          addCallNums();
	        }
	      }
	    });
	}
	
	 /**
	   * Add call numbers to FlexTable. Executed when the user clicks the addCNButton or
	   * presses enter in the newCNTextBox.
	   */
	  private void addCallNums() { //addStock() in StockWatcher.java
	    // TODO Auto-generated method stub
		  
		  final String symbol = newCNTextBox.getText().toUpperCase().trim();
		  newCNTextBox.setFocus(true);

		    // Call numbers must be between 1 and 10 chars that are numbers, letters, or dots.
//		    if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
//		      Window.alert("'" + symbol + "' is not a valid call number.");
		      newCNTextBox.selectAll();
//		      return;
//		    }

		    newCNTextBox.setText("");

		    // Don't add the call number if it's already in the table.
		    if (callnumbers.contains(symbol)) {
		    	Window.alert("'" + symbol + "' is already in the table");
		      return;
		    }
		    
		    // Add the call number to the table.
		    int row = cnFlexTable.getRowCount();
		    callnumbers.add(symbol);
		    cnFlexTable.setText(row, 0, symbol);
		    	
		    // Add a button to remove this call number from the table.
		    Button removeStockButton = new Button("x");
		    removeStockButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        int removedIndex = callnumbers.indexOf(symbol);
		        callnumbers.remove(removedIndex);        
		        cnFlexTable.removeRow(removedIndex + 1);
		      }
		    });
		    cnFlexTable.setWidget(row, 3, removeStockButton);


		    // TODO Get the sorted order.  

	  }
	
}
