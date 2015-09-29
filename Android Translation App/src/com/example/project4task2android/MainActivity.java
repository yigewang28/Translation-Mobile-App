package com.example.project4task2android;


import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class defines the main activities for android application. 
 * It allows user to type a word or sentence, and get the Chinese translation for it.
 * 
 * @author Yige
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final MainActivity trans = this;

		//Find the "submit" button, and add a listener to it
		Button submitButton = (Button)findViewById(R.id.submit);

		// Add a listener to the send button
		submitButton.setOnClickListener(new OnClickListener(){
			public void onClick(View viewParam) {
				String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
				GetTranslation gt = new GetTranslation();
				gt.search(searchTerm, trans); 
			}
		});
	}


	/**
	 * This is called by the GetTranslation object when the translation is ready.  
	 * This allows for passing back the translation for updating the TextView
	 * @param trans xml message from the server
	 */
	public void translationReady(String trans) {
		// extract translation from the xml string
		Document doc = getDocument(trans);
		doc.getDocumentElement().normalize();
		NodeList nl = doc.getElementsByTagName("translation");
		Node n = nl.item(0);
        String result = n.getTextContent();
        
		TextView searchView = (EditText)findViewById(R.id.searchTerm);
		String searchTerm = ((EditText)findViewById(R.id.searchTerm)).getText().toString();
		TextView searchResult = null;
		// set the font	
		Typeface tf = Typeface.createFromAsset(getAssets(), "song.ttf");
		searchResult = (TextView)findViewById(R.id.searchResult);
		searchResult.setTypeface(tf);
		// set the text
		if (result != null) {
			searchResult.setText("Translation for " + searchTerm + ": " + result);
		} else {		
			searchResult.setText("Sorry, translation for " + searchTerm + " is not available.");
		}
		searchView.setText("");
	}

	/**
     * Build a document using xml string
     * @param xmlString string in xml format
     * @return Document Document object
     */
    private static Document getDocument(String xmlString) { 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document spyDoc = null; 
        try {
            builder = factory.newDocumentBuilder();
            spyDoc = builder.parse( new InputSource( new StringReader( xmlString ) ) );
        } 
        catch (Exception e) 
        { 
            e.printStackTrace();
        }
        return spyDoc; 
    }

}
