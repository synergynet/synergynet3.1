package synergynet3.web.commons.client.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;

/**
 * The Class MessageDialogBox.
 */
public class MessageDialogBox extends DialogBox {

	/**
	 * Instantiates a new message dialog box.
	 *
	 * @param message the message
	 */
	public MessageDialogBox(String message) {
		setText(message);

		Button ok = new Button("OK");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				MessageDialogBox.this.hide();
			}
		});
		setWidget(ok);
	}
}
