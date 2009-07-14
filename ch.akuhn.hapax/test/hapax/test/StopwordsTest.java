package hapax.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.hapax.corpus.Stopwords;
import ch.unibe.jexample.JExample;


@RunWith(JExample.class)
public class StopwordsTest {
	
	@Test
	public void stopWords() {
		Stopwords basicEnglish = Stopwords.BASIC_ENGLISH;
		assertTrue(basicEnglish.contains("a"));
	}
	

}
