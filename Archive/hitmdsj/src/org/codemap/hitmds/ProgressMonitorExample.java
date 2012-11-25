package org.codemap.hitmds;

public class ProgressMonitorExample {

	public static void main(String[] args) {
		
		ProgressMonitor mon = new ProgressMonitor.Console();
		
		mon.begin(3);
		mon.worked(1);
		mon.worked(1);
		mon.worked(1);
		mon.done();
		
	}
	
}
