package TaterTodt;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.scripts.task.Task;
import simple.hooks.scripts.task.TaskScript;
import simple.hooks.simplebot.ChatMessage;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@ScriptManifest(author = "RSPUnit", name = "TaterTodt", category = Category.FIREMAKING, version = "1.0",
        description = "Does Wintertodt for fast firemaking XP. Start in WT camp with warm clothing with an axe, knife, and tinderbox. Have lobsters in bank.", discord = "toad#6339", servers = { "Zenyte" })

public class Main extends TaskScript {
    static State state;
    static State lastState;

    public static boolean interrupted = true;
    public static int EAT_THRESHOLD = 20;
    public static int FOOD_COUNT = 10;
    public static String FOOD_NAME = "Lobster";

    public static final WorldPoint BANK_TILE = new WorldPoint(1640, 3944, 0); // The bank tile we want to step to for banking
    public static final WorldPoint DOOR_TILE = new WorldPoint(1630, 3963, 0);
    public static final WorldPoint EXIT_TILE = new WorldPoint(1630, 3968, 0);
    public static final WorldPoint SAFE_TILE = new WorldPoint(1630, 3982, 0);
    public static final WorldPoint VIEW_TILE = new WorldPoint(1630, 3989, 0);

    public enum State {
        ENTERING,
        AWAITING_START,
        LIGHTING_BRAZIER,
        CHOPPING,
        FLETCHING,
        FEEDING,
        HEALING,
        BANKING,
        EATING,
        MAKING_POTS
    }

    private List<Task> tasks = new ArrayList<Task>();

    @Override
    public void onExecute() {
        tasks.addAll(Arrays.asList(new EnterGameTask(ctx), new EatTask(ctx), new HealTask(ctx), new MakePotsTask(ctx),
                new FletchTask(ctx), new FeedTask(ctx), new ChopTask(ctx), new BankTask(ctx) ));// Adds our tasks to our {task} list for execution
        System.out.println("Started TaterTodt!");
    }


    @Override
    public List<Task> tasks() {
        return tasks;// Tells our TaskScript these are the tasks we want executed
    }

    @Override
    public boolean prioritizeTasks() {
        return true;// Will prioritize tasks in order added in our {tasks} List
    }

    // This method is not needed as the TaskScript class will call it, itself
    @Override
    public void onProcess() {
        if(lastState != state) //Every time state changes, we update interrupted back to true
            interrupted = true;
        lastState = state;

        super.onProcess();
        // Can add anything here before tasks have been ran
        // Needed for the TaskScript to process the tasks
        //Can add anything here after tasks have been ran
    }

    @Override
    public void onTerminate() {
    }

    @Override public void onChatMessage(ChatMessage e) {
        if(e.message.equalsIgnoreCase("the cold of the wintertodt seeps into your bones."))
        {
            interrupted = true;
        }
    }


    private State getState() {
        return Main.state;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("State: " + getState(), 12, 90);
    }
}