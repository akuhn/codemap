package ch.deif.meander.ui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import ch.akuhn.hapax.corpus.Terms;
import ch.akuhn.util.Bag;
import ch.akuhn.util.Separator;
import ch.deif.meander.Location;

public class Meander {
//
//	public static void main(String... args) {
//		new MeanderWindow();
//	}
//
//	public static class TagCloud {
//
//		private final int MIN_COUNT = 1;
//		private final int MAX_HEIGHT;
//		private Terms terms;
//		private StyledText text;
//		private FontData fontData;
//		private java.util.Map<Integer, Font> fonts;
//
//		public TagCloud(StyledText styledText) {
//			terms = new Terms();
//			text = styledText;
//			fontData = styledText.getFont().getFontData()[0];
//			fonts = new TreeMap<Integer, Font>();
//			MAX_HEIGHT = fontData.getHeight() * 3;
//		}
//
//		public void clear() {
//			terms.clear();
//			clearText();
//		}
//
//		public void append(Terms t) {
//			terms.addAll(t);
//		}
//
//		public void renderText() {
//			clearText();
//			Separator separator = new Separator(" ");
//			int start = 0;
//			StringBuilder str = new StringBuilder();
//			java.util.List<StyleRange> ranges = new ArrayList<StyleRange>();
//			String tag;
//			StyleRange style;
//			for (Bag.Count<String> each : terms.counts()) {
//				if (each.count > MIN_COUNT) {
//					tag = separator + each.element;
//					str.append(tag);
//
//					style = new StyleRange();
//					style.start = start;
//					style.length = tag.length();
//					int height = fontData.getHeight() + each.count;
//					if (height > MAX_HEIGHT) {
//						height = MAX_HEIGHT;
//					}
//					Font font = fonts.get(height);
//					if (font == null) {
//						font = new Font(text.getDisplay(), fontData.getName(),
//								height, fontData.getStyle());
//						fonts.put(height, font);
//					}
//					style.font = font;
//					ranges.add(style);
//
//					start = str.length();
//				}
//			}
//			text.setText(str.toString());
//			text.setStyleRanges(ranges.toArray(new StyleRange[0]));
//		}
//
//		private void clearText() {
//			text.setText("");
//		}
//
//	}
//
//	public static class MeanderWindow extends ApplicationWindow {
//
//		private static int MAP_DIM = 700;
//
//		private List files;
//		private IEventHandler event;
//		private TagCloud tagCloud;
//
//		public MeanderWindow() {
//			super(null);
//			// Don't return from open() until window closes
//			setBlockOnOpen(true);
//			// Open the main window
//			open();
//			// Dispose the display
//			
//			// if (!getDisplay().isDisposed()) {
//			getDisplay().dispose();
//			// }
//		}
//
//		public Display getDisplay() {
//			return getShell().getDisplay();
//		}
//
//		public List files() {
//			return files;
//		}
//
//		public TagCloud cloud() {
//			return tagCloud;
//		}
//
//		public void registerHandler(IEventHandler eventHandler) {
//			this.event = eventHandler;
//		}
//
//		protected Control createContents(Composite parent) {
//			Control control = super.createContents(parent);
//			Shell shell = control.getShell();
//
//			EclipseProcessingBridge softwareMap = new EclipseProcessingBridge(shell);
//			new EventHandler(this, softwareMap.applet);
//
//			int width = softwareMap.applet.getWidth();
//			int height = softwareMap.applet.getHeight();
//
//			// mapFrame.setSize(width, height);
//			softwareMap.setMaximumSize(new Dimension(width, height));
//
//			softwareMap.setSize(width, height);
//			softwareMap.setLayout(new FillLayout());
//			// "debug" to see what components are where ...
//			// map.setBackground(new Color(Display.getCurrent(), 0, 255, 0));
//
//			Composite rightPane = new Composite(shell, SWT.NONE);
//			files = new List(rightPane, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
//
//			// increase font size
//			Font font = files.getFont();
//			FontData fontData = font.getFontData()[0];
//			fontData.height = fontData.height * 1.35F;
//			files.setFont(new Font(this.getDisplay(),
//					new FontData[] { fontData }));
//
//			files.addSelectionListener(new SelectionListener() {
//
//				public void widgetDefaultSelected(SelectionEvent e) {
//					onListSelected();
//				}
//
//				public void widgetSelected(SelectionEvent e) {
//					onListSelected();
//				}
//
//				private void onListSelected() {
//					int[] indices = files.getSelectionIndices();
//					event.onMeanderSelection(indices);
//				}
//
//			});
//
//			for (Location l : softwareMap.map.locations()) {
//				files.add(l.document.name());
//			}
//
//			StyledText text = new StyledText(rightPane, SWT.BORDER | SWT.MULTI
//					| SWT.READ_ONLY | SWT.WRAP);
//			tagCloud = new TagCloud(text);
//
//			GridDataFactory.swtDefaults().hint(MAP_DIM / 2, height / 2)
//					.applyTo(files);
//			GridDataFactory.swtDefaults().hint(MAP_DIM / 2, height / 2)
//					.applyTo(text);
//			GridLayoutFactory.fillDefaults().spacing(5, 5).generateLayout(
//					rightPane);
//
//			GridDataFactory.swtDefaults().hint(width, height).applyTo(
//					softwareMap);
//			GridDataFactory.swtDefaults().hint(MAP_DIM / 2, height).applyTo(
//					rightPane);
//			GridLayoutFactory.swtDefaults().numColumns(2).generateLayout(shell);
//
//			// get rid of magically appearing label
//			// FIXME find out where this composite comes from
//			Control[] children = shell.getChildren();
//			if (children.length > 1) {
//				Control unwanted = children[0];
//				unwanted.dispose();
//			}
//			shell.pack();
//			return control;
//		}
//	}
}
