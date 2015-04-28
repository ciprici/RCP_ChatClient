package org.eclipse.hyperbola.actions;

import org.eclipse.hyperbola.IImageKeys;
import org.eclipse.hyperbola.dialogs.AddContactDialog;
import org.eclipse.hyperbola.model.ContactsEntry;
import org.eclipse.hyperbola.model.ContactsGroup;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class AddContactsEntryAction extends Action implements
		ISelectionListener, IWorkbenchAction {

	private IWorkbenchWindow window;
	public final static String ID = "org.eclipse.hyperbola.addContact";
	private IStructuredSelection selection;

	public AddContactsEntryAction(IWorkbenchWindow window) {
		this.window = window;
		setId(ID);
		setText("&Add Contact...");
		setToolTipText("Add a contact to your contacts list.");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.hyperbola", IImageKeys.AWAY));
		window.getSelectionService().addSelectionListener(this);
	}

	@Override
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part,
			ISelection incomingSelection) {
		if (incomingSelection instanceof IStructuredSelection) {
			selection = (IStructuredSelection) incomingSelection;
			setEnabled(selection.size() == 1
					&& selection.getFirstElement() instanceof ContactsGroup);
		} else {
			setEnabled(false);
		}
	}

	@Override
	public void run() {
		AddContactDialog addContactDialog = new AddContactDialog(
				window.getShell());
		// InputDialog inputDialog = new InputDialog(window.getShell(),
		// "Add contact", "Contact's name", null, null);
		if (addContactDialog.open() == Window.OK) {
			Object item = selection.getFirstElement();
			ContactsGroup group = (ContactsGroup) item;
			ContactsEntry entry = new ContactsEntry(group, "Ciprian", "Cipri",
					"localhost");
			group.addEntry(entry);
		}
	}

}
