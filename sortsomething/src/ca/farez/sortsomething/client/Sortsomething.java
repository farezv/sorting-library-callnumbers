package ca.farez.sortsomething.client;

import java.util.ArrayList;

import ca.farez.sortsomething.shared.FieldVerifier;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
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
	private HorizontalPanel cnPanel = new HorizontalPanel();
	public Quiz newQuiz;


	/**  * Entry point method.  */  public void onModuleLoad() {  
		
		// Create drag controller
		PickupDragController dragController = new PickupDragController(RootPanel.get(), true);
		
		// Assemble call number panel.
		newQuiz = new Quiz();
		cnPanel.setPixelSize(600, 200);
		Button cnb;
		
		for(int i = 0; i < newQuiz.callnums.size(); i++) {
			cnb = new Button(newQuiz.callnums.get(i));
			
			cnPanel.add(cnb);
			
			dragController.makeDraggable(cnb);
		}
		
		// Assemble Main panel.
		mainPanel.add(cnPanel);
		
		// Associate the Main panel with the HTML host page.  
		RootPanel.get("cnList").add(mainPanel);
				
		
		// TODO Get the sorted order.  
		    
	  }
	
}
