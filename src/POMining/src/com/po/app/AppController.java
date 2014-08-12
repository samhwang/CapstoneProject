package com.po.app;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import com.po.parser.StoryManager;
import com.po.parser.StoryParser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView; 
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;

public class AppController implements Initializable{
	StoryManager sM;
	
    @FXML private ListView<String> StoryIDList;
    @FXML private ListView<String> TopicList;
    @FXML private Label StoryCount;
    @FXML private TextArea StoryBody;
    @FXML private ChoiceBox<String> SentimentChoice;
    
    final ObservableList<String> listItems = FXCollections.observableArrayList();
    final ObservableList<String> topicListItems = FXCollections.observableArrayList();
    final ObservableList<String> SentimentsBox = FXCollections.observableArrayList("All", "Good", "Bad");
    
    private String strMode;
    private String strCurrentTopic;
    
	public void initialize(URL arg0, ResourceBundle arg1) {
	
		InitUI();
		InitStory();
		InitListeners();
		//ReloadData();
		SentimentChoice.getSelectionModel().selectFirst();
	}
	
	private void InitUI()
	{
		StoryIDList.setItems(listItems);
		TopicList.setItems(topicListItems);
		StoryBody.setEditable(false);
		SentimentChoice.setItems(SentimentsBox);
		
	}
	
	private void InitStory()
	{
		StoryParser.getInstance().LoadStopWords();
		StoryParser.getInstance().LoadSentiment();
		StoryParser.getInstance().LoadWordMapping();
		sM = new StoryManager();
		sM.LoadStory("data" + File.separator + "auStory.txt");
	}
	
	private void InitListeners()
	{
		StoryIDList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(newValue != null && !newValue.isEmpty())
	        	{
		        	StoryBody.setText(sM.GetStoryBody(newValue));
		        }
		    }
		});
		
		TopicList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(newValue != null && !newValue.isEmpty() && oldValue != newValue)
	        	{
		    		strCurrentTopic = newValue.replaceAll("\\(\\d+\\)", "").trim();
		    		LoadStoryIDList();
		    		StoryIDList.getSelectionModel().selectFirst();
		        }
		    }
		});
		
		SentimentChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(newValue != null && !newValue.isEmpty() && oldValue != newValue)
	        	{
		    		strMode = newValue;
		    		LoadTopicList();
		        	TopicList.getSelectionModel().selectFirst();
		        }
		    }
		});
	}
	
	private void LoadTopicList()
	{
		topicListItems.clear();
		Map<String, Integer> topicMap = new HashMap<String, Integer>();
    	if(strMode == "All")
    	{
    		topicMap = sM.GetUserTopicTotal();
    	}
    	else if(strMode == "Good")
    	{
    		topicMap = sM.GetUserTopicGood();
    	}
    	else if(strMode == "Bad")
    	{
    		topicMap = sM.GetUserTopicBad();
    	}
    	
    	topicListItems.add("All(" + topicMap.size() + ")");
    	Vector<String> sortedVec = StoryManager.SortTopic(topicMap);
    	for(String key : sortedVec)
    	{
    		topicListItems.add(key+"(" + topicMap.get(key) + ")");
    	}
	}
	
	private void LoadStoryIDList()
	{
		listItems.clear();
		Set<String> setID = new HashSet<String>();
		
		setID = sM.GetIDsByTopic(strMode, strCurrentTopic);
		StoryCount.setText(String.format("Total: %d", setID.size()));
		for(String id : setID)
		{
			listItems.add(id.replace("ID:", "").trim());
		}
	}
}
