package aau.losamigos.wizard;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Salut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import aau.losamigos.wizard.base.GameConfig;
import aau.losamigos.wizard.base.Message;
import aau.losamigos.wizard.elements.PlayerRoundState;
import aau.losamigos.wizard.elements.PlayerRoundStateParser;
import aau.losamigos.wizard.network.DataCallback;
import aau.losamigos.wizard.network.ICallbackAction;
import aau.losamigos.wizard.rules.Actions;
import aau.losamigos.wizard.rules.Client2HostAction;

public class ScoreTableActivity extends AppCompatActivity {

    private int card_cnt = 60;

    private String idPatternActualScore = "actualScoreCell";
    private String idPatternEstimatedStich = "estimatedStich";
    private String idPatternActualStich = "actualStich";

    private HashMap<String, Integer> mapOfIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);
        setTitle(R.string.title_score_sheet);
        mapOfIds = new HashMap<>();

        int playerCount = 3;
        try {
            playerCount = getIntent().getIntExtra("PLAYER_COUNT", 3);
        } catch (Exception e) {
            Log.e("SCORETABLE", "failed to get Playercount");
        }

        generateNewScoreSheet(playerCount);

        Salut network = GameConfig.getInstance().getSalut();

        if(network.isRunningAsHost) {
            setData(GameConfig.getInstance().getCurrentGamePlay().getPlayerRoundStates());
        } else {
            final DataCallback callback = GameConfig.getInstance().getCallBack();
            callback.addFireOnceCallBackAction(new ICallbackAction() {
                @Override
                public void execute(Message message) {
                    if(message.playerStates != null && message.action == Actions.PLAYERSTATES_SENT) {
                        setData(message.playerStates);
                    } else {
                        Log.e("CLIENT", "oh nooo.. no data received");
                    }
                }
            });

            Message message = new Message();
            message.client2HostAction = Client2HostAction.PLAYERSTATES_REQUESTED;
            message.sender = network.thisDevice.deviceName;
            network.sendToHost(message, new SalutCallback() {
                @Override
                public void call() {
                    Log.e("CLIENT", "Failed to receive score data from host");
                }
            });
        }
        //generateFakeData(playerCount);
    }

    private void setData(Map<String, List<String>> states) {
        for (Map.Entry<String, List<String>> entry : states.entrySet())
        {
            List<String> roundStates = entry.getValue();
            for(int i = 0; i < roundStates.size(); i++) {
                PlayerRoundState state = PlayerRoundStateParser.parse(roundStates.get(i));
                setActualStich(i, i, state.getActualStiches());
                setEstimatedStich(i, i, state.getCalledStiches());
                setActualPoints(i, i, state.getPoints());
            }
        }
    }

    private void generateNewScoreSheet(int numberOfPlayers) {
        if(numberOfPlayers < 3) {
            numberOfPlayers = 3;
        }
        int numberOfRounds = card_cnt / numberOfPlayers;

        TableLayout scoreSheetTable = findViewById(R.id.scoreSheetTable);

        //create the header row; run count + 1 times because we need an empty first cell
        TableRow headerRow = new TableRow(ScoreTableActivity.this);
        for(int i = 0; i < numberOfPlayers + 1; i++) {
            TextView cell = new TextView(ScoreTableActivity.this);

            headerRow.addView(cell);
            if(i > 0) {
                cell.setText("Player " + i);
                cell.setTypeface(null, Typeface.BOLD);
                cell.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                cell.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            }

        }
        scoreSheetTable.addView(headerRow);
        TableLayout.LayoutParams headerParams = (TableLayout.LayoutParams)headerRow.getLayoutParams();
        headerParams.weight = 1;
        headerRow.setLayoutParams(headerParams);


        //create cells for each round
        for(int i = 1; i <= numberOfRounds; i++) {
            //create new row and set styles
            TableRow row = new TableRow(ScoreTableActivity.this);
            scoreSheetTable.addView(row);
            TableLayout.LayoutParams params = (TableLayout.LayoutParams)row.getLayoutParams();
            params.weight = 1;
            row.setLayoutParams(params);

            for(int j = 0; j < numberOfPlayers + 1; j++) {

                //add new cell to row and set styles
                LinearLayout cell = new LinearLayout(ScoreTableActivity.this);
                row.addView(cell);
                cell.setOrientation(LinearLayout.HORIZONTAL);
                cell.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                cell.setGravity(Gravity.CENTER_VERTICAL);
                TableRow.LayoutParams cellParams = (TableRow.LayoutParams)cell.getLayoutParams();
                cellParams.leftMargin = 5;
                cellParams.rightMargin = 5;
                cell.setLayoutParams(cellParams);

                TextView tf1 = new TextView(ScoreTableActivity.this); //actual score
                tf1.setPadding(10, 0, 0, 0);
                tf1.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                cell.addView(tf1);

                //first cell that shows the number of the round
                if(j == 0) {
                    tf1.setText("" + i);
                    tf1.setTypeface(null, Typeface.BOLD);
                    tf1.setGravity(Gravity.CENTER_HORIZONTAL);
                }

                //we need to add 2 cells
                //1 for the actual score of the player
                //and one for the guessed amount of stiches
                else {
                    TextView textViewEstimatedStich = new TextView(ScoreTableActivity.this);
                    textViewEstimatedStich.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    TextView textViewActualStich = new TextView(ScoreTableActivity.this);
                    textViewActualStich.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    TextView placeHolder = new TextView(ScoreTableActivity.this);
                    placeHolder.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                    placeHolder.setText("|");

                    LinearLayout stichContainer = new LinearLayout(ScoreTableActivity.this);
                    stichContainer.setOrientation(LinearLayout.HORIZONTAL);
                    cell.addView(stichContainer);
                    stichContainer.setGravity(Gravity.RIGHT);
                    LinearLayout.LayoutParams containerParams = (LinearLayout.LayoutParams)stichContainer.getLayoutParams();
                    containerParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                    stichContainer.setLayoutParams(containerParams);
                    stichContainer.setPadding(0,0,10,0);


                    stichContainer.addView(textViewEstimatedStich);
                    stichContainer.addView(placeHolder);
                    stichContainer.addView(textViewActualStich);

                    //generate ids
                    tf1.setId(View.generateViewId());
                    textViewEstimatedStich.setId(View.generateViewId());
                    textViewActualStich.setId(View.generateViewId());


                    //we have to remember the ids of the cells in a map in order to be able to find them later
                    mapOfIds.put(buildIdKey(i, j, idPatternActualScore), tf1.getId());
                    mapOfIds.put(buildIdKey(i, j, idPatternEstimatedStich), textViewEstimatedStich.getId());
                    mapOfIds.put(buildIdKey(i, j, idPatternActualStich), textViewActualStich.getId());
                }
            }


        }
    }

    private String buildIdKey(int roundNumber, int playerNumber, String pattern) {
        return pattern + "_" + roundNumber + "_" + playerNumber;

    }

    private TextView getSelectedCell(String key) {
        int viewId = mapOfIds.get(key);
        return findViewById(viewId);
    }

    public void setEstimatedStich(int roundNumber, int playerNumber, int value) {
        TextView cell = getSelectedCell(buildIdKey(roundNumber, playerNumber, idPatternEstimatedStich));
        cell.setText("" + value);
    }

    public int getEstimatedStich(int roundNumber, int playerNumber) {
        TextView cell = getSelectedCell(buildIdKey(roundNumber, playerNumber, idPatternEstimatedStich));
        String value = cell.getText().toString();
        if(value != null)
            return Integer.parseInt(value);
        return 0;
    }

    public void setActualStich(int roundNumber, int playerNumber, int value) {
        TextView cell = getSelectedCell(buildIdKey(roundNumber, playerNumber, idPatternActualStich));
        cell.setText("" + value);
    }


    public void setActualPoints(int roundNumber, int playerNumber, int value) {
        TextView cell = getSelectedCell(buildIdKey(roundNumber, playerNumber, idPatternActualScore));
        cell.setText("" + value);
        if(value < 0) {
            cell.setTextColor(getResources().getColor(R.color.colorRed));
        } else {
            cell.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
        }
    }

    public void addPoints(int roundNumber, int playerNumber, int value) {
        int points = 0;
        if(roundNumber > 1) {
            points += getActualPoints(roundNumber-1, playerNumber);
        }
        setActualPoints(roundNumber, playerNumber, points + value);
    }

    public int getActualPoints(int roundNumber, int playerNumber) {
        TextView cell = getSelectedCell(buildIdKey(roundNumber, playerNumber, idPatternActualScore));
        String value = cell.getText().toString();
        if(value != null && value != "")
            return Integer.parseInt(value);
        return 0;
    }

    private void generateFakeData(int playerCount) {
        Random rand = new Random();
        int rounds =card_cnt /playerCount;

        rounds = rounds - 3;

        for(int round = 1; round <= rounds; round++) {
            for(int player = 1; player <= playerCount; player++) {
                int actual = rand.nextInt(4);
                int overUnder = rand.nextInt(3)+1;
                boolean guessedRight = rand.nextBoolean();
                int points =0;
                if(guessedRight) {
                    points += 20 +(10*actual);
                    setActualStich(round, player, actual);
                } else {
                    points -= 10*overUnder;
                    int guessed;
                    if(overUnder < actual)
                        guessed = actual - overUnder;
                    else
                        guessed = overUnder + actual;
                    setActualStich(round, player, guessed);
                }
                addPoints(round, player, points);
                setEstimatedStich(round, player, actual);
            }
        }
    }
}
