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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sortsomething implements EntryPoint {

	// Size Constants
	int leftWidth = 150;
	int leftHeight = 400;	
	int rightWidth = 1000;
	int rightHeight = 400;
	
	// Panels
	AbsolutePanel boundaryPanel = new AbsolutePanel(); // restricts drag operations
	final HorizontalPanel cnPanel = new HorizontalPanel(); // contains call number "buttons"
	HorizontalPanel mainPanel = new HorizontalPanel();
	VerticalPanel leftPanel = new VerticalPanel();
	VerticalPanel rightPanel = new VerticalPanel();
	Label mistakeLabel;
	
	// Buttons
	Button scoreMeButton = new Button("Score Me!");
	Button startQuizButton = new Button("Start Quiz!");

	// Input
	public TextArea inputArea = new TextArea();
	public Quiz newQuiz = new Quiz();


	/* Entry point method.  */  
	public void onModuleLoad() {  

		// Drag handler
		final DragHandler demoDragHandler = null;

		// Boundary panel setup
		boundaryPanel.setPixelSize(rightWidth,rightHeight);
		boundaryPanel.addStyleName("boundaryPanel");
		boundaryPanel.getElement().getStyle().setProperty("position", "relative");

		// Call number panel setup	
		cnPanel.addStyleName("cnPanel");
		cnPanel.setPixelSize(400, 90);

		// Setting up widget drag controller
		final PickupDragController widgetDragController = new PickupDragController(boundaryPanel, false);
		widgetDragController.setBehaviorMultipleSelection(false);
		//widgetDragController.addDragHandler(demoDragHandler);

		// Setting up HP drag controller
		HorizontalPanelDropController widgetDropController = new HorizontalPanelDropController(	cnPanel);
		widgetDragController.registerDropController(widgetDropController);

		// scoreMe button setup
		scoreMeButton.setPixelSize(100,40);
		//scoreMeButton.setStyleName("scoreMe");
		scoreMeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// Finds the position on buttons/callnumbers as arranged by users  
				int numOfButtons = cnPanel.getWidgetCount();
				setUserOrder(numOfButtons);

				// Compute our inter-sorted buckets
				newQuiz.builtInSortQuiz(); 				
				
				// Set the bucket indices for fine sorting later
				newQuiz.fillBucketCollection(newQuiz.callNums.size());
				
				// Compute our and intra-sorted buckets
				newQuiz.callNumberIntraBucketSorting();
				
				// Compare the two and find mistakes (if any)
				//System.out.println("Mistakes BEFORE compare() = " + newQuiz.getMistakes());
				int mistakes = newQuiz.compare();
				//System.out.println("Mistakes AFTER compare() = " + mistakes);
				if(mistakes > 0) {
					mistakeLabel = new Label("Number of mistakes = " + mistakes);
					mistakeLabel.addStyleName("yesMistakes");
				} else {
					mistakeLabel = new Label("Correct Solution!");
					mistakeLabel.addStyleName("noMistakes");
				}
				
				boundaryPanel.add(mistakeLabel);
			}
		});
		
		// startQuiz button setup
		startQuizButton.setPixelSize(100,40);
		startQuizButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				String input = inputArea.getText();
				
				if (input == null || input.trim().equals("")) { // Checking for empty strings
					Window.alert("Please type something in the text box!");
				} else {	
						int newCallNums = newQuiz.populate(input);					
						
						// Reassemble call number panel only if there are NEW call numbers!
						if(newCallNums > 0) {
							cnPanel.clear();
							// TODO figure out how to switch to next panel if we've reached the right most side
							Button cnb;		
							for(int i = 0; i < newQuiz.callNums.size(); i++) {
								cnb = new Button(newQuiz.callNums.get(i));
								// TODO cnb.addStyleName("lineBreak");
								cnPanel.add(cnb);
								cnb.setPixelSize(60,90);								
								widgetDragController.makeDraggable(cnb);
							}
						}
				}	
			}
		});

		// inputBox text box setup
		inputArea.setFocus(true);
		inputArea.setPixelSize(leftWidth,leftHeight);

		// Left panel setup
		leftPanel.setPixelSize(leftWidth + 10, leftHeight + 200);
		leftPanel.add(inputArea);
		leftPanel.add(startQuizButton);
		
		// Right panel setup
		rightPanel.setPixelSize(rightWidth, rightHeight + 200);
		rightPanel.add(boundaryPanel);
		rightPanel.add(scoreMeButton);
		
		// Populating Boundary Panel
		boundaryPanel.add(cnPanel);
		
		// Populating Main Panel		
		mainPanel.setPixelSize(960, 600);
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);

		// Adding panels & buttons to Root
		RootPanel.get().add(mainPanel);

		// Associate the Main panel with the HTML host page.  
		//RootPanel.get("cnList").add(boundaryPanel);
		
	}

	public void setUserOrder(int numButtons) {

		System.out.println("*****USER SOLUTION*****\n"); 
		for (int i = 0; i < numButtons; i++) {
			Button ucnb = (Button) cnPanel.getWidget(i);
			newQuiz.userCallNums.add( i, ucnb.getText()); // added index to i refills userCallNums from the start. Culprit behind the mistake reset bug
			System.out.println(newQuiz.userCallNums.get(i));
		}
	}

}
