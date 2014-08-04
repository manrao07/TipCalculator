package com.mm.client;

import com.mm.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TipCalculator implements EntryPoint {




	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		final TextBox amountField = new TextBox();
		amountField.setText("0.00");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		
		final ListBox percentageField = new ListBox();
		for(int i=5;i<51;i++){
			percentageField.addItem(String.valueOf(i));
		}

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("amountFieldContainer").add(amountField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		RootPanel.get("tipFieldContainer").add(percentageField);

		// Focus the cursor on the name field when the app loads
		amountField.setFocus(true);
		amountField.selectAll();

		//setting itemcount value to 1 turns listbox into a drop-down list.
		percentageField.setVisibleItemCount(1);

	      

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				calculateTip();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					calculateTip();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void calculateTip() {
				// First, we validate the input.
				errorLabel.setText("");
				String amount = amountField.getText();
				String percentage = percentageField.getItemText(percentageField.getSelectedIndex());
				if (!FieldVerifier.isValidAmount(amount)) {
					errorLabel.setText("Please enter the amount");
					return;
				}
				float floatAmount = 0;
				try{
					floatAmount = Float.parseFloat(amount);
				}catch(Exception e){
					errorLabel.setText("Please enter the valid amount");
					return;
				}
				Integer intPercentage = Integer.parseInt(percentage);
				errorLabel.setText("Tip = $" + round(intPercentage * floatAmount/100,2));


			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		amountField.addKeyUpHandler(handler);
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
