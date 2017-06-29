package tagger;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.XMLOutputter;
import model.NER_TAG;
import model.NerTag;
import nu.xom.Document;

public class NERTagger {
	private final static Properties props;
	private final static StanfordCoreNLP pipeline;

	static {
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner");
		pipeline = new StanfordCoreNLP(props);
	}

	/**
	 * Run NER and returns full XML
	 * 
	 * @param text
	 * @return
	 */
	public static String runTaggerXML(String text) {
		final Annotation document = new Annotation(text);
		pipeline.annotate(document);
		final Document doc = XMLOutputter.annotationToDoc(document, pipeline);
		return doc.toXML();
	}

	public static Map<Integer, NerTag> nerXmlParser(final String xml) {
		try {
			Map<Integer, NerTag> result = new LinkedHashMap<>();
			final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			final org.w3c.dom.Document document = docBuilder
					.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

			final NodeList nodeList = document.getElementsByTagName("*");
			for (int i = 0; i < nodeList.getLength(); i++) {
				final Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE && isValidTag(node)) {

					String word = null;
					String nerTag = null;
					int startPosition = 0;
					int endPosition = 0;

					if (node.hasChildNodes()) {
						for (int j = 0; j < node.getChildNodes().getLength(); j++) {
							final Node childNode = node.getChildNodes().item(j);

							if (childNode.getNodeType() == Node.ELEMENT_NODE) {
								if (childNode.getNodeName().equals("word")) {
									word = childNode.getTextContent();
								} else if (childNode.getNodeName().equals("NER")) {
									nerTag = childNode.getTextContent();
								} else if (childNode.getNodeName().equals("CharacterOffsetBegin")) {
									startPosition = Integer.parseInt(childNode.getTextContent());
								} else if (childNode.getNodeName().equals("CharacterOffsetEnd")) {
									endPosition = Integer.parseInt(childNode.getTextContent());
								}
							}
						}
					}
					result.put(startPosition, new NerTag(word, NER_TAG.resolve(nerTag), startPosition, endPosition));
				}
			}
			result = aggregateTagPositions(result);
			result = filterOnlyValidNerTags(result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<Integer, NerTag> filterOnlyValidNerTags(Map<Integer, NerTag> inputMap) {
		final Map<Integer, NerTag> result = new LinkedHashMap<>();
		for (Entry<Integer, NerTag> entry : inputMap.entrySet()) {
			if (entry.getValue().getNerTag() != null && NER_TAG.resolve(entry.getValue().getNerTag().text) != null) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	private static Map<Integer, NerTag> aggregateTagPositions(final Map<Integer, NerTag> inputMap) {
		final Map<Integer, NerTag> result = new LinkedHashMap<>();
		final List<NerTag> tags = new ArrayList<>(inputMap.values());
		for (int i = 0; i < tags.size(); i++) {
			final NerTag nerTag = tags.get(i);
			StringBuilder words = new StringBuilder(nerTag.getWord());
			int updatedEndposition = nerTag.getEndPosition();
			final NER_TAG tag = nerTag.getNerTag();

			for (int j = i+1; j < tags.size() && tag == tags.get(j).getNerTag(); j++, i++) {
				final NerTag nextNerTag = tags.get(j);
				updatedEndposition = nextNerTag.getEndPosition();
				int endOfFirstTag = tags.get(i).getEndPosition();
				while(endOfFirstTag<nextNerTag.getStartPosition()){
					words.append(" ");
					endOfFirstTag++;
				}
				words.append(nextNerTag.getWord());
			}
			result.put(nerTag.getStartPosition(),
					new NerTag(words.toString(), nerTag.getNerTag(), nerTag.getStartPosition(), updatedEndposition));
		}

		return result;
	}

//	private static boolean isValidNerTag(Node childNode) {
//		if (NER_TAG.resolve(childNode.getTextContent()) != null) {
//			return true;
//		} else {
//			return false;
//		}
//	}

	private static boolean isValidTag(Node node) {
		if (node.getNodeName().equals("token")) {
			return true;
		} else {
			return false;
		}
	}

	public static String runTaggerString(String fullContentPlain) {
		StringBuilder result = new StringBuilder(fullContentPlain); 
		final Map<Integer, NerTag> nerXmlParser = nerXmlParser(runTaggerXML(fullContentPlain));
		int offset = 0;
		for(NerTag tag:nerXmlParser.values()){ 
			result.replace(tag.getStartPosition()+offset, tag.getEndPosition()+offset, "<"+tag.getNerTag().text+">");
			int diff = tag.getEndPosition()-tag.getStartPosition();
			int tagLength = 2+tag.getNerTag().text.length();
			if(diff>=tagLength){
				offset -= Math.abs(diff-tagLength);
			}else{
				offset += Math.abs(diff-tagLength);
			}
			
		}
		return result.toString();
	}
}
