package com.po.app;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import com.po.parser.Story;
import com.po.parser.StoryManager;
import com.po.parser.StoryParser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//Huy Huynh - 14/5/2015 - In case we need the chart imports

public class AppController implements Initializable {
	StoryManager sM;

	@FXML
	private ListView<String> StoryIDList;
	@FXML
	private ListView<String> TopicList;
	@FXML
	private Label StoryCount;
	@FXML
	private TextArea StoryBody;
	// Stuart Barker 14/4/2015
	@FXML
	private Button GraphGenerate;
	@FXML
	private Button BackToText;
	
	// Huy Huynh - 14/5/2015 - test for graph generating
	@FXML
	private TextArea GraphArea;
	private String url = (new TestGraph().GenerateChart());
	
	// Stuart Barker 13/05/2015
	@FXML
	private TextField Search;
	
	// Stuart Barker 28/05/2015
	@FXML
	private TextArea GraphURL;

	@FXML
	private ChoiceBox<String> SentimentChoice;
	@FXML
	private CheckBox cbNSW;
	@FXML
	private CheckBox cbVIC;
	@FXML
	private CheckBox cbACT;
	@FXML
	private CheckBox cbTAS;
	@FXML
	private CheckBox cbQLD;
	@FXML
	private CheckBox cbSA;
	@FXML
	private CheckBox cbNT;
	@FXML
	private CheckBox cbWA;

	final ObservableList<String> listItems = FXCollections
			.observableArrayList();
	final ObservableList<String> topicListItems = FXCollections
			.observableArrayList();
	final ObservableList<String> SentimentsBox = FXCollections
			.observableArrayList("All", "Good", "Bad");

	private String strMode;
	private String strCurrentTopic;
	private int nStateSelection;

	public void initialize(URL arg0, ResourceBundle arg1) {

		/*
		 * If we can check which .fxml file is being used, we should be able to
		 * change the initialization to avoid null reference errors from using a
		 * different .fxml document with the same controller.
		 */

		String resource = arg0.toString();
		String document = resource.substring(resource.length() - 15);
		/*
		 * System.out.println(resource); System.out.println(document);
		 */

		/*
		 * Stuart Barker 15/04/2015 - Run initialize only for scene 1 (not for
		 * the scene dedicated to a generated graph, which would cause
		 * conflicts)
		 */

		if (document.equals("Controller.fxml")) {
			InitUI();
			InitStory();
			InitListeners();

			RefreshStateFlag();
			SentimentChoice.getSelectionModel().selectFirst();
			
			
		}
		
		/* 
		 * Stuart Barker 6/05/2015 - initialize controller2 for graph.
		 */
		
		if (document.equals("Controller2.fxml")) {
			InitGraphUI();
			InitGraphListeners();
		}
	}

	private void InitUI() {

		StoryIDList.setItems(listItems);
		TopicList.setItems(topicListItems);
		StoryBody.setEditable(false);
		SentimentChoice.setItems(SentimentsBox);

	}
	
	private void InitGraphUI() {
		// Huy Huynh - 14/05/2013 - Load the chart url.
		TestGraph chart = new TestGraph();
		GraphArea.setText(chart.GenerateChart());
	}

	private void InitStory() {
		StoryParser.getInstance().LoadStopWords();
		StoryParser.getInstance().LoadSentiment();
		StoryParser.getInstance().LoadWordMapping();
		sM = new StoryManager();
		// sM.LoadStory("data" + File.separator + "auStory.txt");
		sM.LoadStory("auStory.txt");
	}
	
	private void InitGraphListeners() {
		// will be blank for now, since the chart currently has no "interactive methods"
		// Huy Huynh - 14/5/2015
	}

	private void InitListeners() {
		StoryIDList.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (newValue != null && !newValue.isEmpty()) {
							StoryBody.setText(sM.GetStoryBody(newValue));
						}
					}
				});

		TopicList.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (newValue != null && !newValue.isEmpty()
								&& oldValue != newValue) {
							strCurrentTopic = newValue.replaceAll("\\(\\d+\\)",
									"").trim();
							LoadStoryIDList();
							StoryIDList.getSelectionModel().selectFirst();
						}
					}
				});

		SentimentChoice.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						if (newValue != null && !newValue.isEmpty()
								&& oldValue != newValue) {
							strMode = newValue;
							LoadTopicList();
							TopicList.getSelectionModel().selectFirst();
						}
					}
				});

		cbNSW.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				RefreshStateFlag();
				LoadStoryIDList();
			}
		});

		cbVIC.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				RefreshStateFlag();
				LoadStoryIDList();
			}
		});

		cbTAS.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				RefreshStateFlag();
				LoadStoryIDList();
			}
		});

		cbACT.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				RefreshStateFlag();
				LoadStoryIDList();
			}
		});

		cbQLD.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				RefreshStateFlag();
				LoadStoryIDList();
			}
		});

		cbSA.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				RefreshStateFlag();
				LoadStoryIDList();
			}
		});

		cbNT.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				RefreshStateFlag();
				LoadStoryIDList();
			}
		});

		cbWA.selectedProperty().addListener(new ChangeListener<Boolean>() {
			public void changed(ObservableValue<? extends Boolean> ov,
					Boolean old_val, Boolean new_val) {
				RefreshStateFlag();
				LoadStoryIDList();
			}
		});
		
		// Search keywords (jump to keyword on enter key in search bar). Stuart Barker
		// 13/5/2015
		Search.setOnAction((event) -> {
			
			// Grab the text in the search bar as well as create the hashmap
			// stating how many results (stories) exist for each list item
			// (dependent on the current 'sentiment choice' (good/bad/all))
			String searchtext = Search.getText().toLowerCase();
			Map<String, Integer> topicMap = new HashMap<String, Integer>();
			if (strMode == "All") {
				topicMap = sM.GetUserTopicTotal();
			} else if (strMode == "Good") {
				topicMap = sM.GetUserTopicGood();
			} else if (strMode == "Bad") {
				topicMap = sM.GetUserTopicBad();
			}
			
			// Because the text in the TopicList include the number of results
			// each keyword has, append the number of results/stories to the
			// search text by referring to the hashmap so the search text formatting
			// is appropriate.
			
			// The if-else checks whether the searchtext exists at all and responds
			// appropriately, defaulting the selection to the first List item ("All") if
			// the text isn't found
			if(topicMap.get(searchtext)!=null)
			{
				searchtext = searchtext + "(" + topicMap.get(searchtext) + ")";
			
				// Search for the new text in the list, select and scroll to it when found
				TopicList.getSelectionModel().select(searchtext);
				TopicList.scrollTo(TopicList.getSelectionModel().getSelectedIndex());
				Search.clear();
				Search.setPromptText("Type Search Text...");
			}
			else
			{
				Search.clear();
				Search.setPromptText("Type Search Text...");
				TopicList.getSelectionModel().selectFirst();
				TopicList.scrollTo(TopicList.getSelectionModel().getSelectedIndex());
			}
			
		});	
	}

	// Change scene (prepare graph page on button press). Stuart Barker
	// 14/4/2015

	/*public void GraphGenerate(ActionEvent e) throws IOException {
		Stage stage;
		Parent root;

		stage = (Stage) GraphGenerate.getScene().getWindow();
		// load up OTHER FXML document
		root = FXMLLoader.load(getClass().getResource("Controller2.fxml"));
		// create a new scene with root and set the stage
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}*/
	
	// Alternative graph generation - provide a URL on default scene and highlight it for copying.
	// It is IMPORTANT TO NOTE that the graph created uses only data points from the selected Topic/keyword,
	// which by default (depending on user interaction) will be "All"
	// Currently some method/class names, "TestGraph" and "example1" are subject to change by final
	// release. Further code information can be found in TestGraph.java
	// Stuart Barker, 28/05/2015
	
	public void PrepareGraph() {
        TestGraph tGraph = new TestGraph();
        String url = tGraph.example1(sM, strMode, strCurrentTopic, listItems);
        //System.out.println(url);
        GraphURL.setText(url);
        GraphURL.selectAll();
        //LoadStoryIDList();
   }
	
	public void GraphGenerate(ActionEvent e) throws IOException {

		PrepareGraph();
		
	}

	// Change from graph scene to original text screen. Huy Huynh 28/4/2015

	public void BackToText(ActionEvent e) throws IOException {
		Stage stage;
		Parent root;

		stage = (Stage) BackToText.getScene().getWindow();
		// load up OTHER FXML document
		root = FXMLLoader.load(getClass().getResource("Controller.fxml"));
		// create a new scene with root and set the stage
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	

	private void LoadTopicList() {
		topicListItems.clear();
		Map<String, Integer> topicMap = new HashMap<String, Integer>();
		if (strMode == "All") {
			topicMap = sM.GetUserTopicTotal();
		} else if (strMode == "Good") {
			topicMap = sM.GetUserTopicGood();
		} else if (strMode == "Bad") {
			topicMap = sM.GetUserTopicBad();
		}

		topicListItems.add("All(" + topicMap.size() + ")");
		Vector<String> sortedVec = StoryManager.SortTopic(topicMap);
		for (String key : sortedVec) {
			topicListItems.add(key + "(" + topicMap.get(key) + ")");
		}
	}

	private void LoadStoryIDList() {
		listItems.clear();
		Set<String> setID = new HashSet<String>();
		setID = sM.GetIDsByTopic(strMode, strCurrentTopic);

		for (String id : setID) {
			if (((sM.GetStoryStateFlag(id.replace("ID:", "").trim()) & nStateSelection) > 0)
					|| (sM.GetStoryStateFlag(id.replace("ID:", "").trim()) == 0))
				listItems.add(id.replace("ID:", "").trim());
		}

		StoryCount.setText(String.format("Total: %d", listItems.size()));
	}

	// State checkboxes

	private void RefreshStateFlag() {
		if (cbNSW.isSelected())
			nStateSelection |= Story.AU_STATE_FLAG_NSW;
		else
			nStateSelection &= ~Story.AU_STATE_FLAG_NSW;

		if (cbVIC.isSelected())
			nStateSelection |= Story.AU_STATE_FLAG_VIC;
		else
			nStateSelection &= ~Story.AU_STATE_FLAG_VIC;

		if (cbACT.isSelected())
			nStateSelection |= Story.AU_STATE_FLAG_ACT;
		else
			nStateSelection &= ~Story.AU_STATE_FLAG_ACT;

		if (cbQLD.isSelected())
			nStateSelection |= Story.AU_STATE_FLAG_QLD;
		else
			nStateSelection &= ~Story.AU_STATE_FLAG_QLD;

		if (cbTAS.isSelected())
			nStateSelection |= Story.AU_STATE_FLAG_TAS;
		else
			nStateSelection &= ~Story.AU_STATE_FLAG_TAS;

		if (cbSA.isSelected())
			nStateSelection |= Story.AU_STATE_FLAG_SA;
		else
			nStateSelection &= ~Story.AU_STATE_FLAG_SA;

		if (cbNT.isSelected())
			nStateSelection |= Story.AU_STATE_FLAG_NT;
		else
			nStateSelection &= ~Story.AU_STATE_FLAG_NT;

		if (cbWA.isSelected())
			nStateSelection |= Story.AU_STATE_FLAG_WA;
		else
			nStateSelection &= ~Story.AU_STATE_FLAG_WA;
	}

}
