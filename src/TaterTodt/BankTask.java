package TaterTodt;

import simple.hooks.filters.SimpleBank;
import simple.hooks.scripts.task.Task;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.api.ClientContext;

import static TaterTodt.Main.BANK_TILE;

public class BankTask extends Task {


    public BankTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        return ctx.inventory.populate().filter(Main.FOOD_NAME).population() < Main.FOOD_COUNT && ctx.players.getLocal().getLocation().getY() <= 3963;
    }

    @Override
    public void run() {
        if(ctx.players.getLocal().getLocation().getY() > 3963) { //If we're inside the minigame we need to leave it

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
                    ctx.getBank().deposit(Main.FOOD_NAME, SimpleBank.Amount.ALL); // Clicks the Deposit inventory button
                    ctx.getBank().withdraw(Main.FOOD_NAME, Main.FOOD_COUNT);
                    ctx.getBank().closeBank(); // Close the bank once our inventory is empty
                }

            }
        }
    }



    @Override
    public String status() {
        return "Banking logs";
    }
}
