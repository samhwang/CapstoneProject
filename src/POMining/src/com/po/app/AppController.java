package com.po.app;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import com.po.parser.StoryManager;
import com.po.parser.StoryParser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView; 
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class AppController implements Initializable{
	StoryManager sM;
	
    @FXML private ListView<String> StoryIDList;
    @FXML private Label StoryCount;
    @FXML private TextArea StoryBody;
    
    final ObservableList<String> listItems = FXCollections.observableArrayList();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		StoryIDList.setItems(listItems);
		StoryBody.setEditable(false);
		
		StoryParser.getInstance().LoadStopWords();
		StoryParser.getInstance().LoadSentiment();
		StoryParser.getInstance().LoadWordMapping();
		sM = new StoryManager();
		sM.LoadStory("data\\auStory.txt");
		
		Set<String> storyIDs = sM.GetStoryIDs();
		StoryCount.setText(String.format("Total: %d", storyIDs.size()));
		for(String id : storyIDs)
		{
			listItems.add(id.replace("ID:", "").trim());
		}
	}

	@FXML public void OnStoryIDClicked(MouseEvent event) {
		
        String strID = StoryIDList.getSelectionModel().getSelectedItem();
        
        if(strID != null && !strID.isEmpty())
        {
        	StoryBody.setText(sM.GetStoryBody(strID));
        }
    }
}
