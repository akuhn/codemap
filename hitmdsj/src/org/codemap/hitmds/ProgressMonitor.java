package org.codemap.hitmds;

public interface ProgressMonitor {

	public boolean isCanceled();
	
	public void cancel();
	
	public void done();
	
	public void worked(int subtotal);
	
	public void setLabel(String task);
	
	public void begin(int total);
	
	public ProgressMonitor spawn(String task, int subtotal);

	class Spawn implements ProgressMonitor {

		private ProgressMonitor parent;
		private int parentSubtotal;
		private int parentWorked;
		private int thisTotal;
		private int thisWorked;
		
		public Spawn(ProgressMonitor parent, String name, int subtotal) {
			this.parent = parent;
			this.parentSubtotal = this.thisTotal = subtotal;
			this.parentWorked = this.thisWorked = 0;
			setLabel(name);
		}
		
		@Override
		public void begin(int total) {
			this.thisTotal = total;
		}

		@Override
		public void cancel() {
			parent.cancel();
		}

		@Override
		public void done() {
			this.worked(thisTotal - thisWorked);
		}

		@Override
		public boolean isCanceled() {
			return parent.isCanceled();
		}

		@Override
		public void worked(int subtotal) {
			if (subtotal == 0) return;
			if (subtotal < 0) throw new IllegalArgumentException();
			thisWorked += subtotal;
			// if (thisWorked > thisTotal) <-- shall we check this?
			parentWorked += subtotal * parentSubtotal;
			if (parentWorked >= thisTotal) {
				int temp = parentWorked / thisTotal;
				parentWorked -= temp * thisTotal;
				parent.worked(temp);
			}
		}

		@Override
		public void setLabel(String task) {
			parent.setLabel(task);
		}

		@Override
		public ProgressMonitor spawn(String task, int subtotal) {
			return new Spawn(this, task, subtotal);
		}
		
	}

	public static final ProgressMonitor NULL = new ProgressMonitor() {

		@Override
		public void begin(int length) {
			// ignore
		}

		@Override
		public void cancel() {
			// ignore
		}

		@Override
		public void done() {
			// ignore
		}

		@Override
		public void setLabel(String name) {
			// ignore
		}

		@Override
		public boolean isCanceled() {
			return false;
		}

		@Override
		public void worked(int amount) {
			// ignore
		}

		@Override
		public ProgressMonitor spawn(String task, int subtotal) {
			return this;
		}
		
	};
	
	class Console extends Spawn {

		public Console() {
			super(new ProgressMonitor() {

				private int worked;
				private boolean canceled = false;
				private String label;

				@Override
				public void begin(int total) {
					throw null; // not called
				}

				@Override
				public void cancel() {
					canceled = true;
				}

				@Override
				public void done() {
					worked(100 - worked);
				}

				@Override
				public boolean isCanceled() {
					return canceled;
				}

				@Override
				public void setLabel(String task) {
					label = task;
				}

				@Override
				public ProgressMonitor spawn(String task, int subtotal) {
					throw null; // not called
				}

				@Override
				public void worked(int subtotal) {
					// can go beyond 100%, which is useful to spot bugs
					System.out.printf("%5d%%\t%s\n", worked += subtotal, label);
				}
				
			}, "Progress", 100);
		}
		
	}
	
}
