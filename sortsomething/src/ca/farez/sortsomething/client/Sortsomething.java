/*
 * Copyright 2009 Fred Sauer
 * Copyright 2013 Farez Vadsaria
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package ca.farez.sortsomething.client;

import java.util.ArrayList;

import ca.farez.sortsomething.shared.FieldVerifier;

import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
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
import com.google.gwt.user.client.ui.AbsolutePanel;
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
	
	// Panels
	AbsolutePanel boundaryPanel = new AbsolutePanel(); // restricts drag operations
	final HorizontalPanel cnPanel = new HorizontalPanel(); // contains call number "buttons"
	
	// Buttons
	Button scoreMeButton = new Button("Score Me!");
	
	public Quiz newQuiz;


	/* Entry point method.  */  
	public void onModuleLoad() {  
	
		// Drag handler
		final DragHandler demoDragHandler = null;
		
		// Boundary panel setup
		boundaryPanel.setPixelSize(960,400);
		boundaryPanel.addStyleName("boundaryPanel");
		boundaryPanel.getElement().getStyle().setProperty("position", "relative");
		
		// Call number panel setup	
		cnPanel.addStyleName("cnPanel");
		cnPanel.setPixelSize(400, 90);
				
		// ScoreMe button setup
		scoreMeButton.setPixelSize(100,100);
		scoreMeButton.setStyleName("scoreMe");
	    scoreMeButton.addClickHandler(new ClickHandler() {
	      public void onClick(ClickEvent event) {
	    	// Finds the position on buttons/callnumbers as arranged by users  
	        int numOfButtons = cnPanel.getWidgetCount();
	        setUserOrder(numOfButtons);
	        
	        // Get our computed solution
	        
	        // Compare the two and find mistakes (if any)
	      }
	    });
		
		// Adding panels & buttons to Root
		RootPanel.get().add(boundaryPanel);
		boundaryPanel.add(cnPanel);
		boundaryPanel.add(scoreMeButton);		
		
		// Create drag controller
		PickupDragController widgetDragController = new PickupDragController(boundaryPanel, false);
	    widgetDragController.setBehaviorMultipleSelection(false);
	    //widgetDragController.addDragHandler(demoDragHandler);
		
		// Creating HP drag controller
		HorizontalPanelDropController widgetDropController = new HorizontalPanelDropController(	cnPanel);
		widgetDragController.registerDropController(widgetDropController);
		
		// Assemble call number panel.
		newQuiz = new Quiz();
		Button cnb;
		
		for(int i = 0; i < newQuiz.callNums.size(); i++) {
			cnb = new Button(newQuiz.callNums.get(i));
			// TODO cnb.addStyleName("lineBreak");
			cnPanel.add(cnb);
			cnb.setPixelSize(50,70);
			
			widgetDragController.makeDraggable(cnb);
		}
		
		
		// Associate the Main panel with the HTML host page.  
		//RootPanel.get("cnList").add(boundaryPanel);
		
		// TODO Get the solution order.  
		    newQuiz.sortQuiz();
		    		    
	  }
	
	public void setUserOrder(int numButtons) {
		
		for (int i = 0; i < numButtons; i++) {
			Button ucnb = (Button) cnPanel.getWidget(i);
			newQuiz.userCallNums.add(ucnb.getText());
			System.out.println(ucnb.getText() + " ");
		}
	}
	
}
