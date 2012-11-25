package ch.deif.meander.ui;

import java.util.EventObject;

@SuppressWarnings("serial")
public class CodemapEvent extends EventObject {

	private String kind;
	private Object value;

	public CodemapEvent(String kind, Object source, Object value) {
		super(source);
		this.kind = kind.intern();
		this.value = value;
	}

	public String getKind() {
		return kind;
	}

	public Object getValue() {
		return value;
	}

}
