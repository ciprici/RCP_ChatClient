package org.eclipse.hyperbola.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class ChatEditor extends EditorPart {

	private static final String ID = "org.eclipse.hyperbola.editors.chat";

	private Text transcript;
	private Text entry;

	public ChatEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	@Override
	public void doSaveAs() {

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(getUser());
	}

	private String getUser() {
		return null;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		top.setLayout(layout);

		transcript = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		transcript.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, true));
		transcript.setEditable(false);
		transcript.setBackground(transcript.getDisplay().getSystemColor(
				SWT.COLOR_INFO_BACKGROUND));
		transcript.setForeground(transcript.getDisplay().getSystemColor(
				SWT.COLOR_INFO_FOREGROUND));

		entry = new Text(parent, SWT.BORDER | SWT.WRAP);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				true);
		gridData.heightHint = entry.getLineHeight() * 2;
		entry.setLayoutData(gridData);
		entry.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR) {
					sendMessage();
					e.doit = false;
				}
			}
		});
	}

	private void sendMessage() {
		String body = entry.getText();
		if (body.length() == 0) {
			return;
		}
		transcript.append(renderMessage(getUser(), body));
		transcript.append("\n");
		scrollToEnd();
		entry.setText("");
	}

	private void scrollToEnd() {
		int n = transcript.getCharCount();
		transcript.setSelection(n, n);
		transcript.showSelection();
	}

	private String renderMessage(String fromUser, String body) {
		if (fromUser == null) {
			return body;
		}
		int j = fromUser.indexOf("@");
		if (j > 0) {
			fromUser = fromUser.substring(0, j);
		}
		return "<" + fromUser + "> " + body;
	}

	@Override
	public void setFocus() {
		if (entry != null && !entry.isDisposed()) {
			entry.setFocus();
		}
	}

}
