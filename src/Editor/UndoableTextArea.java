package Editor;
import java.awt.TextArea;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.Hashtable;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;

public class UndoableTextArea extends TextArea implements StateEditable {
	
	
	private static final long serialVersionUID = 1L;
	private final static String KEY_STATE = "UndoableTextAreaKey";
	private boolean textChanged = false;
	private UndoManager undoManager;
	private StateEdit currentEdit;

	public UndoableTextArea() {
		super();
		initUndoable();
	}

	public UndoableTextArea(String string) {
		super(string);
		initUndoable();
	}

	public UndoableTextArea(int rows, int columns) {
		super(rows, columns);
		initUndoable();
	}

	public UndoableTextArea(String string, int rows, int columns) {
		super(string, rows, columns);
		initUndoable();
	}

	public UndoableTextArea(String string, int rows, int columns, int scrollbars) {
		super(string, rows, columns, scrollbars);
		initUndoable();
	}

	public boolean undo() {
		try {
			undoManager.undo();
			return true;
		} catch (CannotUndoException e) {
			System.out.println("cannot undo");
			return false;
		}
	}

	public boolean redo() {
		try {
			undoManager.redo();
			return true;
		} catch (CannotRedoException e) {
			System.out.println("cannot redo");
			return false;
		}
	}

	public void storeState(Hashtable state) {
		state.put(KEY_STATE, getText());
	}

	public void restoreState(Hashtable state) {
		Object data = state.get(KEY_STATE);
		if (data != null) {
			setText((String) data);
		}
	}

	private void takeSnapshot() {
		if (textChanged) {
			currentEdit.end();
			undoManager.addEdit(currentEdit);
			textChanged = false;
			currentEdit = new StateEdit(this);
		}
	}

	private void initUndoable() {
		undoManager = new UndoManager();
		currentEdit = new StateEdit(this);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if (event.isActionKey()) {
					takeSnapshot();
				}
			}
		});

		addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent fe) {
				takeSnapshot();
			}
		});

		addTextListener(new TextListener() {
			public void textValueChanged(TextEvent e) {
				textChanged = true;
				takeSnapshot();
			}
		});

	}
}




