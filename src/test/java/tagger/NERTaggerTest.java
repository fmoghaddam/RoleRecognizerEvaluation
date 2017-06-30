package tagger;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class NERTaggerTest {

	@Test
	public void test() {
		final String text = "Pope Francis does not talk with Mr. Pope";
		Set<String> headRoles = new HashSet<>(Arrays.asList("Pope"));
		System.err.println(NERTagger.runTaggerString(text));
		System.err.println(NERTagger.runTaggerStringWithoutHeadRoleReplacement(text,headRoles));
	}

}
