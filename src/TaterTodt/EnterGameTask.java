package TaterTodt;

import simple.hooks.filters.SimpleBank;
import simple.hooks.scripts.task.Task;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.api.ClientContext;

import static TaterTodt.Main.*;

public class EnterGameTask extends Task {

    public EnterGameTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        return ctx.players.getLocal().getLocation().getY() <= 3963 || ctx.widgets.getWidget(396, 21).getText().equalsIgnoreCase("Wintertodt's Energy: 0%");

    }
    public void handleBanking() {
        if(ctx.players.getLocal().getLocation().getY() > 3963) { //If we're inside the minigame we need to leave it
            if (ctx.players.getLocal().getLocation().distanceTo(EXIT_TILE) > 3) {
                ctx.pathing.step(EXIT_TILE);
                ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getLocation().distanceTo(BANK_TILE) <= 3, 5000);
            } else {
                SimpleObject doors = ctx.objects.populate().filter("Doors of Dinh").nextNearest();
                doors.click(0);
                ctx.dialogue.clickDialogueOption(1);
                ctx.sleepCondition(() -> ctx.players.getLocal().getLocation().getY() <= 3693, 5000);// Checks if the player is in the minigame area
            }
        } else {
            if (ctx.players.getLocal().getLocation().distanceTo(BANK_TILE) > 3) {
                ctx.pathing.step(BANK_TILE);
                ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getLocation().distanceTo(BANK_TILE) <= 3, 5000);
            } else {
                if (!ctx.getBank().bankOpen()) {// If the bank screen isn't open, let's open it
                    SimpleObject bank = ctx.getObjects().populate().filter("Bank chest").nextNearest();// Grabs the nearest 'Bank booth' object to us
                    if (bank != null && bank.validateInteractable()) {// Checks if the bank isn't null, if it isn't we'll turn to it and walk to it
                        bank.click("Bank");// Once we can see the bank, we're going to open it
                        ctx.sleepCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1, 5000);// Checks if the bank is open with a 5 second time-out, so we don't spam click 'Open'
                    }
                }
                if (ctx.getBank().bankOpen()) { // If the bank is open, let's deposit our logs!
                    try {
                        ctx.getBank().deposit(Main.FOOD_NAME, SimpleBank.Amount.ALL); // Clicks the Deposit inventory button
                    } catch (Exception ex) {
                        System.out.println("No food in inventory");
                    }
                    try {
                        ctx.getBank().deposit("Supply crate", SimpleBank.Amount.ALL); // Clicks the Deposit inventory button
                    } catch (Exception ex) {
                        System.out.println("No supply crates found");
                    }
                    ctx.getBank().withdraw(Main.FOOD_NAME, Main.FOOD_COUNT);
                    ctx.getBank().closeBank(); // Close the bank once our inventory is empty
                }

            }
        }
    }
    @Override
    public void run() {
        if(ctx.inventory.populate().filter(Main.FOOD_NAME).population() < Main.FOOD_COUNT) {
            handleBanking();
        }
        else if(ctx.players.getLocal().getLocation().getY() <= 3963)
        {
            if (ctx.players.getLocal().getLocation().distanceTo(Main.DOOR_TILE) > 3) {
                Main.state = Main.State.ENTERING;
                ctx.pathing.step(Main.DOOR_TILE);// Clicks on the bank tile on the minimap
                ctx.sleepCondition(() -> ctx.players.getLocal().getLocation().distanceTo(Main.DOOR_TILE) <= 3, 5000);// Waits to check if we're at the bank, with a 5 second time-out, so we don't spam walk to the bank
            } else {
                SimpleObject doors = ctx.objects.populate().filter("Doors of Dinh").nextNearest();
                doors.click(0);
                ctx.sleepCondition(() -> ctx.players.getLocal().getLocation().getY() <= 3693, 5000);// Checks if the player is in the minigame area

            }
        }
    }

    @Override
    public String status() {
        return "Entering Minigame";
    }
}
