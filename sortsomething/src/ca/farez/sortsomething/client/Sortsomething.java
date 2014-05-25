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
import java.util.Date;

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
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
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
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Sortsomething implements EntryPoint {

	// Constants
	int leftWidth = 150;
	int leftHeight = 400;	
	int rightWidth = 1000;
	int rightHeight = 400;
	int inputAreaOnClickCount = 0; // Never goes more than 1
	
	// Panels
	AbsolutePanel boundaryPanel = new AbsolutePanel(); // restricts drag operations
	final HorizontalPanel cnPanel = new HorizontalPanel(); // contains call number "buttons"
	VerticalPanel manualQuizPanel = new VerticalPanel();
	TabPanel mainPanel = new TabPanel();
	HorizontalPanel topPanel = new HorizontalPanel();
	HorizontalPanel bottomPanel = new HorizontalPanel();
	VerticalPanel autoQuizPanel = new VerticalPanel();
		
	// Labels
	Label mistakeLabel;
	
	// Buttons
	Button scoreMeButton = new Button("Score Me!");
	Button startQuizButton = new Button("Start Quiz!");
	Button generateQuizButton = new Button("Generate Quiz!");
	Button newQuizButton = new Button("New Quiz!");
	
	// Input
	public TextArea inputArea = new TextArea();
	public TextBox generateQuizTextBox = new TextBox();
	
	// Defined classes
	public Quiz newQuiz = new Quiz();
    private AutoQuizServiceAsync autoQuizSvc = GWT.create(AutoQuizService.class);

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
		scoreMeButton.setPixelSize(120,60);
		scoreMeButton.setVisible(false);
		//scoreMeButton.setStyleName("scoreMe");
		scoreMeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// Clear all of Quiz's data structures to avoid null pointers and index out of bounds
				newQuiz.clean();
				
				// Finds the position on buttons/callnumbers as arranged by users  
				int numOfButtons = cnPanel.getWidgetCount();
				setUserOrder(numOfButtons);

				// Compute our inter-sorted buckets
				newQuiz.builtInSortQuiz(); 				
				
				// Set the bucket indices for fine sorting later
				newQuiz.fillBucketCollection(newQuiz.callNums.size());
				newQuiz.printBucketCollection();
				
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
				if(boundaryPanel.getWidgetCount() > 1) {
					boundaryPanel.remove(1);
				}				
				boundaryPanel.add(mistakeLabel);
			}
		});
		
		// startQuiz button setup
		startQuizButton.setPixelSize(120,60);
		startQuizButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				scoreMeButton.setVisible(true);
				String input = inputArea.getText().toUpperCase();
				
				if (input == null || input.trim().equals("")) { // Checking for empty strings
					Window.alert("Please type something in the text box!");
				} else {	
						int newCallNums = newQuiz.populate(input);					
						
						// Reassemble call number panel only if there are NEW call numbers!
						if(newCallNums > 0) {
							cnPanel.clear();
							AsyncCallback<Void> callback = autoQuizVoidSetup();
							// TODO figure out how to switch to next panel if we've reached the right most side
							Button cnb;		
							for(int i = 0; i < newQuiz.callNums.size(); i++) {
								// Storing string in db asynchronously
								autoQuizSvc.addString(newQuiz.callNums.get(i), callback);
								// Adding call number to the UI					        
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
		
		// generateQuizButton setup
		generateQuizButton.setPixelSize(120, 60);
		generateQuizButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String input = generateQuizTextBox.getText();
				if (input == null || input.trim().equals("")) { // Checking for empty strings
					Window.alert("Please type something in the text box!");
				} else {
					int size = Integer.parseInt(input);					
					if (size <= 0) { // Checking for invalid size
						Window.alert("Please enter a valid quiz size!");
					} else {
						String randomQuiz = generateRandomQuiz(size);
						AsyncCallback<String> callback = autoQuizStringSetup();
						// Making the call to the auto quiz generation service
						autoQuizSvc.getQuiz(size, callback);
						inputArea.setText(randomQuiz);
					}
				}
			}
		});

		// newQuizButton Setup
		newQuizButton.setPixelSize(120, 60);
		newQuizButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cnPanel.clear();
				newQuiz.clean();
				inputArea.setText("");
				newQuiz.callNums.clear(); // The clean() call above doesn't clear the entered call number list
				if(boundaryPanel.getWidgetCount() > 1) {
					boundaryPanel.remove(1);
				}
			}			
		});
		
		// inputBox text box setup
		inputArea.setFocus(true);
		inputArea.setPixelSize(leftWidth,rightHeight);
		inputArea.setText("Enter one call number per line. For example,\n\n"
				+ "A100 TA2 2006\n"
				+ "PC2600 Z68 2012\n"
				+ "G53 XN1 2011\n");
		inputArea.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(inputAreaOnClickCount == 0) {
					inputArea.setText("");
				}
				inputAreaOnClickCount++;
			}
		});

		// Top panel setup
		topPanel.add(inputArea);
		topPanel.add(boundaryPanel);
				
		// Bottom panel setup
		bottomPanel.add(startQuizButton);
		bottomPanel.add(newQuizButton);
		bottomPanel.add(scoreMeButton);
		
		// Populating Boundary Panel
		boundaryPanel.add(cnPanel);
		
		// Populating Manual Quiz Panel		
		manualQuizPanel.setPixelSize(960, 600);
		manualQuizPanel.add(topPanel);
		manualQuizPanel.add(bottomPanel);

		// Generate Quiz setup
		generateQuizTextBox.getElement().setPropertyString("placeholder", "Enter the size of the quiz");
		
		// Populating Generated Quiz Panel
		autoQuizPanel.add(generateQuizTextBox);
		autoQuizPanel.add(generateQuizButton);
		
		// Populating Main Panel
		mainPanel.add(autoQuizPanel, "Generate Quiz Automatically");
		mainPanel.add(manualQuizPanel, "Create Quiz Manually");
		mainPanel.selectTab(0);
		
		// Adding panels & buttons to Root
		RootPanel.get().add(mainPanel);

		// Associate the Main panel with the HTML host page.  
		//RootPanel.get("cnList").add(boundaryPanel);
		
	}

	public void setUserOrder(int numButtons) {

		System.out.println("*****USER SOLUTION*****\n"); 
		for (int i = 0; i < numButtons; i++) {
			Button ucnb = (Button) cnPanel.getWidget(i);
			newQuiz.userCallNums.add( i, ucnb.getText());
			System.out.println(newQuiz.userCallNums.get(i));
		}
	}

	private AsyncCallback<String> autoQuizStringSetup() {
		// Initialising the service proxy
		if(autoQuizSvc == null) {
			autoQuizSvc = GWT.create(AutoQuizService.class);
		}
		
		// Setup callback object
		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				System.out.println("Exception caught: " + caught);
			}
			
			public void onSuccess(String quiz) {
				// Populate the quiz based on this input
				System.out.println("Successfully retrieved quiz " + quiz);
			}
		};
		return callback;
	}
	
	private AsyncCallback<Void> autoQuizVoidSetup() {
		// Initialising the service proxy
		if(autoQuizSvc == null) {
			autoQuizSvc = GWT.create(AutoQuizService.class);
		}
		
		// Setup callback object
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				System.out.println("Exception caught: " + caught);
			}
			
			public void onSuccess(Void result) {
				// Populate the quiz based on this input
				System.out.println("Successfully added string\n");
			}
		};
		return callback;
	}
	
	private String generateRandomQuiz(int size) {
		String quiz = "";
		String block = "";
		String callnumber = "";
		int numBlocks; 	// 3, 4 or 5 including year
		int cnInt;	// Range 1 - 9 no decimals for now
		int numInts; // 1, 2, 3 or 4 integers
		int numAlpha;	// 1, 2 or 3 alphabet letters
		
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		
		// For every call number
		for(int i = 0; i < size; i++) {
			
			// Make 2,3, or 4 line/blocks
			numBlocks = Random.nextInt(3) + 2;
			
			// For every block
			for(int j = 0; j < numBlocks; j++) {
				
				numInts = Random.nextInt(4) + 1;
				cnInt = Random.nextInt(10);
				numAlpha = Random.nextInt(3) + 1;
				
				block = "";
				for(int k = 0; k < numAlpha; k++) {					
					block += chars[Random.nextInt(chars.length)];
				}
				block = block.toUpperCase();
				
				for(int m = 0; m < numInts; m++) {
					block += Integer.toString(cnInt);
					cnInt = Random.nextInt(10);	
				}				
			
				if(j < numBlocks - 1) block += " ";
				callnumber += block;
				
				Date today = new Date();
				String year = DateTimeFormat.getFormat( "d-M-yyyy" ).format( new Date() ).split( "-")[2];
				callnumber += Integer.toString(Random.nextInt(2015) + 1900);
			}
			
			// Append a "year" to the call number
			
			// Append it to quiz
			quiz = callnumber + "\r\n";
		}
		
		return quiz;		
	}
}
